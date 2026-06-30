<?php

require_once "connection.php";
require_once "JwtHandler.php";
require_once "helpers.php";
require_once "response.php";

$data = jsonBody();

$refreshToken = trim(
    $data["refresh_token"] ?? ""
);

if (empty($refreshToken)) {
    errorResponse("Refresh Token inválido.", 400);
}

/*
|--------------------------------------------------------------------------
| Procura usuário
|--------------------------------------------------------------------------
*/

$stmt = $conn->prepare("
SELECT
    id,
    email
FROM users
WHERE
    refresh_token = ?
AND
    refresh_expiration > NOW()
LIMIT 1
");

$stmt->bind_param(
    "s",
    $refreshToken
);

$stmt->execute();

$user = $stmt->get_result()->fetch_assoc();

if (!$user) {

    errorResponse(
        "Refresh Token expirado.",
        401
    );
}

/*
|--------------------------------------------------------------------------
| Novo JWT
|--------------------------------------------------------------------------
*/

$newAccessToken = JwtHandler::generateToken(

    $user["id"],

    $user["email"]

);

/*
|--------------------------------------------------------------------------
| Novo Refresh Token (ROTATIVO)
|--------------------------------------------------------------------------
*/

$newRefresh = randomToken(64);

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
    $newRefresh,
    $days,
    $user["id"]
);

$stmt->execute();

/*
|--------------------------------------------------------------------------
| Resposta
|--------------------------------------------------------------------------
*/

success([
    "token" => $newAccessToken,
    "refresh_token" => $newRefresh
]);