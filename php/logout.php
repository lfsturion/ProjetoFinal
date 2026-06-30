<?php

header("Content-Type: application/json");

require_once "connection.php";
require_once "auth.php";
require_once "response.php";

$user = requireAuth();
$userId = $user["userId"];

$stmt = $conn->prepare("
UPDATE users
SET refresh_token = NULL,
    refresh_expiration = NULL
WHERE id = ?
");

$stmt->bind_param("i", $userId);
$stmt->execute();

$stmt->close();
$conn->close();

success([
    "message" => "Logout realizado com sucesso"
]);