<?php

require_once __DIR__ . "/env.php";

date_default_timezone_set("America/Sao_Paulo");

/*
|--------------------------------------------------------------------------
| Banco de Dados
|--------------------------------------------------------------------------
*/

define("DB_HOST", $_ENV["DB_HOST"]);
define("DB_USER", $_ENV["DB_USER"]);
define("DB_PASS", $_ENV["DB_PASS"]);
define("DB_NAME", $_ENV["DB_NAME"]);

/*
|--------------------------------------------------------------------------
| JWT
|--------------------------------------------------------------------------
*/

define("JWT_SECRET", $_ENV["JWT_SECRET"]);
define("JWT_ALGORITHM", "HS256");
define("JWT_EXPIRATION", 3600);          // 1 hora
define("REFRESH_EXPIRATION_DAYS", 7);

/*
|--------------------------------------------------------------------------
| Segurança
|--------------------------------------------------------------------------
*/

header("Content-Type: application/json; charset=utf-8");

header("X-Content-Type-Options: nosniff");
header("X-Frame-Options: SAMEORIGIN");
header("Referrer-Policy: no-referrer");
header("X-XSS-Protection: 1; mode=block");