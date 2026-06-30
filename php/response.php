<?php

function success($data = [], $code = 200)
{
    http_response_code($code);
    echo json_encode([
        "success" => true,
        "data" => $data
    ]);
    exit;
}

function errorResponse($message, $code = 400)
{
    http_response_code($code);
    echo json_encode([
        "success" => false,
        "message" => $message
    ]);
    exit;
}