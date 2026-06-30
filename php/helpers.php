<?php

function jsonBody()
{
    $json = file_get_contents("php://input");

    if (empty($json)) {
        return [];
    }

    $data = json_decode($json, true);

    if (json_last_error() !== JSON_ERROR_NONE) {
        http_response_code(400);
        echo json_encode([
            "success" => false,
            "message" => "JSON inválido."
        ]);
        exit;
    }

    return $data;
}

function randomToken($size = 64)
{
    return bin2hex(random_bytes($size));
}

function isValidEmail($email)
{
    return filter_var(
        $email,
        FILTER_VALIDATE_EMAIL
    );
}

function currentDate()
{
    return date("Y-m-d H:i:s");
}