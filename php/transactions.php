<?php

header("Content-Type: application/json");

require_once "connection.php";
require_once "auth.php";

$user = requireAuth();
$userId = $user["userId"];

$sql = "
SELECT id, description, amount, date, type
FROM transactions
WHERE user_id = ?
ORDER BY date DESC
";

$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $userId);
$stmt->execute();

$result = $stmt->get_result();

$data = [];

while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

echo json_encode($data);

$stmt->close();
$conn->close();