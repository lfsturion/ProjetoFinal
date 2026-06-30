<?php

require_once "JwtHandler.php";

function getBearerToken() {
    $headers = getallheaders();

    if (!isset($headers["Authorization"])) {
        return null;
    }

    if (preg_match("/Bearer\s(\S+)/", $headers["Authorization"], $matches)) {
        return $matches[1];
    }

    return null;
}

function requireAuth() {

    $token = getBearerToken();

    if (!$token) {
        http_response_code(401);
        echo json_encode(["error" => "Token ausente"]);
        exit;
    }

    $decoded = JwtHandler::validateToken($token);

    if (!$decoded) {
        http_response_code(401);
        echo json_encode(["error" => "Token inválido ou expirado"]);
        exit;
    }

    return $decoded;
}