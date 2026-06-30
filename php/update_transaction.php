<?php

header("Content-Type: application/json");

require_once "connection.php";
require_once "auth.php";

$user = requireAuth();
$userId = $user["userId"];

$data = json_decode(file_get_contents("php://input"), true);

$id = $data["id"] ?? null;
$description = trim($data["description"] ?? "");
$amount = $data["amount"] ?? 0;
$date = $data["date"] ?? null;
$type = $data["type"] ?? null;

if (!$id || !$description || !$date || !$type) {
    http_response_code(400);
    echo json_encode(["error" => "Dados inválidos"]);
    exit;
}

$sql = "
UPDATE transactions
SET description = ?, amount = ?, date = ?, type = ?
WHERE id = ? AND user_id = ?
";

$stmt = $conn->prepare($sql);

$stmt->bind_param(
    "sdssii",
    $description,
    $amount,
    $date,
    $type,
    $id,
    $userId
);

$stmt->execute();

echo json_encode([
    "success" => true
]);

$stmt->close();
$conn->close();