<?php

header("Content-Type: application/json");

require_once "connection.php";
require_once "auth.php";

$user = requireAuth();
$userId = $user["userId"];

$data = json_decode(file_get_contents("php://input"), true);

$id = $data["id"] ?? null;

if (!$id) {
    http_response_code(400);
    echo json_encode(["error" => "ID inválido"]);
    exit;
}

$sql = "
DELETE FROM transactions
WHERE id = ? AND user_id = ?
";

$stmt = $conn->prepare($sql);

$stmt->bind_param("ii", $id, $userId);

$stmt->execute();

echo json_encode([
    "success" => true
]);

$stmt->close();
$conn->close();