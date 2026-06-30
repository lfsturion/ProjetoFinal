<?php

function sanitizeString($value)
{
    return trim(
        htmlspecialchars(
            $value,
            ENT_QUOTES,
            "UTF-8"
        )
    );
}

function sanitizeEmail($email)
{
    return filter_var(
        trim($email),
        FILTER_SANITIZE_EMAIL
    );
}

function sanitizeInt($value)
{
    return filter_var(
        $value,
        FILTER_VALIDATE_INT
    );
}

function sanitizeFloat($value)
{
    return filter_var(
        $value,
        FILTER_VALIDATE_FLOAT
    );
}

function sanitizeDate($date)
{
    $d = DateTime::createFromFormat(
        "Y-m-d\TH:i:s",
        $date
    );

    if (!$d) {

        $d = DateTime::createFromFormat(
            "Y-m-d H:i:s",
            $date
        );
    }

    if (!$d) {
        return false;
    }

    return $d->format("Y-m-d H:i:s");
}