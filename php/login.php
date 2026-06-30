<?php

require_once "connection.php";
require_once "JwtHandler.php";
require_once "helpers.php";
require_once "sanitize.php";
require_once "response.php";

$data = jsonBody();

$email = sanitizeEmail($data["email"] ?? "");
$password = trim($data["password"] ?? "");

if (!$email || empty($password)) {
    errorResponse("Email ou senha inválidos.", 400);
}

$stmt = $conn->prepare("
SELECT
    id,
    name,
    email,
    password_hash
FROM users
WHERE email = ?
LIMIT 1
");

$stmt->bind_param("s", $email);
$stmt->execute();

$user = $stmt->get_result()->fetch_assoc();

if (!$user) {
    errorResponse("Credenciais inválidas.", 401);
}

if (!password_verify($password, $user["password_hash"])) {
    errorResponse("Credenciais inválidas.", 401);
}

/*
|--------------------------------------------------------------------------
| Geração dos Tokens
|--------------------------------------------------------------------------
*/

$accessToken = JwtHandler::generateToken(
    $user["id"],
    $user["email"]
);

$refreshToken = randomToken(64);

/*
|--------------------------------------------------------------------------
| Salva Refresh Token
|--------------------------------------------------------------------------
*/

$stmt = $conn->prepare("
UPDATE users
SET
    refresh_token = ?,
    refresh_expiration = DATE_ADD(NOW(), INTERVAL ? DAY)
WHERE id = ?
");

$days = REFRESH_EXPIRATION_DAYS;

$stmt->bind_param(
    "sii",
    $refreshToken,
    $days,
    $user["id"]
);

$stmt->execute();

/*
|--------------------------------------------------------------------------
| Retorno
|--------------------------------------------------------------------------
*/

success([
    "token" => $accessToken,
    "refresh_token" => $refreshToken,
    "user" => [
        "id" => (int)$user["id"],
        "name" => $user["name"],
        "email" => $user["email"]
    ]
]);