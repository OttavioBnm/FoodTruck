<?php

// Require du PDO
require "../pdo.php";

function connexionApplication() {
    try {
        $request = "SELECT * FROM `TPROPRIETAIRE` WHERE `Courriel` = :courriel AND `MotDePasse` = :motDePasse";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':courriel', strtolower($_SERVER['PHP_AUTH_USER']));
        $dbQuery->bindParam(':motDePasse', $_SERVER['PHP_AUTH_PW']);
        $dbQuery->execute();
        $data = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
        $isCo = (count($data) > 0) ? json_encode($data) : header("HTTP/1.1 403 Forbidden");
    } catch (Exception $e) {
        return FALSE;
    }
    return $isCo;
}

echo connexionApplication();