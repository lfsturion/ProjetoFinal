<?php

require_once "connection.php";

$name = "Teste User";
$email = "teste@teste.com";
$password = "123456";

$passwordHash = password_hash($password, PASSWORD_DEFAULT);

$stmt = $conn->prepare("
INSERT INTO users (name, email, password_hash)
VALUES (?, ?, ?)
");

$stmt->bind_param("sss", $name, $email, $passwordHash);

$stmt->execute();

echo "Usuário criado com sucesso!";