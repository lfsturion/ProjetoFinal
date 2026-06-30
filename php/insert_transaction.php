<?php

header("Content-Type: application/json");

require_once "connection.php";
require_once "auth.php";

$user = requireAuth();
$userId = $user["userId"];

$data = json_decode(file_get_contents("php://input"), true);

if (!$data) {
    http_response_code(400);
    echo json_encode(["error" => "JSON inválido"]);
    exit;
}

$description = trim($data["description"] ?? "");
$amount = $data["amount"] ?? 0;
$date = $data["date"] ?? null;
$type = $data["type"] ?? null;

if (!$description || !$date || !$type) {
    http_response_code(400);
    echo json_encode(["error" => "Campos obrigatórios"]);
    exit;
}

$sql = "
INSERT INTO transactions
(description, amount, date, type, user_id)
VALUES (?, ?, ?, ?, ?)
";

$stmt = $conn->prepare($sql);

$stmt->bind_param(
    "sdssi",
    $description,
    $amount,
    $date,
    $type,
    $userId
);

$stmt->execute();

echo json_encode([
    "success" => true
]);

$stmt->close();
$conn->close();