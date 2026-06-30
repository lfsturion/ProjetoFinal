<?php

require_once "config.php";

class JwtHandler
{
    private static function base64UrlEncode($data)
    {
        return rtrim(strtr(base64_encode($data), '+/', '-_'), '=');
    }

    private static function base64UrlDecode($data)
    {
        return base64_decode(strtr($data, '-_', '+/'));
    }

    /**
     * Gera um Access Token
     */
    public static function generateToken($userId, $email)
    {
        $header = [
            "alg" => JWT_ALGORITHM,
            "typ" => "JWT"
        ];

        $payload = [
            "userId" => (int)$userId,
            "email" => $email,
            "iat" => time(),
            "exp" => time() + JWT_EXPIRATION
        ];

        $headerEncoded = self::base64UrlEncode(
            json_encode($header)
        );

        $payloadEncoded = self::base64UrlEncode(
            json_encode($payload)
        );

        $signature = hash_hmac(
            "sha256",
            $headerEncoded . "." . $payloadEncoded,
            JWT_SECRET,
            true
        );

        $signatureEncoded = self::base64UrlEncode($signature);

        return $headerEncoded .
            "." .
            $payloadEncoded .
            "." .
            $signatureEncoded;
    }

    /**
     * Valida um JWT
     */
    public static function validateToken($token)
    {
        $parts = explode(".", $token);

        if (count($parts) != 3) {
            return false;
        }

        list($header, $payload, $signature) = $parts;

        $expected = self::base64UrlEncode(
            hash_hmac(
                "sha256",
                $header . "." . $payload,
                JWT_SECRET,
                true
            )
        );

        if (!hash_equals($expected, $signature)) {
            return false;
        }

        $payloadData = json_decode(
            self::base64UrlDecode($payload),
            true
        );

        if (!$payloadData) {
            return false;
        }

        if (!isset($payloadData["exp"])) {
            return false;
        }

        if ($payloadData["exp"] < time()) {
            return false;
        }

        return $payloadData;
    }
}