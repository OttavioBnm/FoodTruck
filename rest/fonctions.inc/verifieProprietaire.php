<?php

require "../pdo.php";

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

$user = $_SERVER['PHP_AUTH_USER'];
$pass = $_SERVER['PHP_AUTH_PW'];
if (verifieProprietaire($user, $pass)) {
    return TRUE;
} else {
    header("HTTP/1.0 401 unauthorized");
    return FALSE;
}

function verifieProprietaire($courriel, $motDePasse) {
    if (empty($courriel) || empty($motDePasse)) {
        return false;
    }
    try {
        $request = "SELECT * FROM `TPROPRIETAIRE` WHERE `Courriel` = :courriel AND `MotDePasse` = :motDePasse";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':courriel', $courriel);
        $dbQuery->bindParam(':motDePasse', $motDePasse);
        $dbQuery->execute();
        $data = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
        if (!empty($data)) {
            return TRUE;
        }
    } catch (Exception $e) {
        return FALSE;
    }
}