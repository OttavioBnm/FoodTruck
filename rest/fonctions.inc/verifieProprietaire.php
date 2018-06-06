<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : fonctions.inc/verifieProprietaire.php
 * Description  : Permet de érifier si l'utilisateur est authentifié
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

require "../pdo.php";

// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// récupère les données dans les headers
$user = $_SERVER['PHP_AUTH_USER'];
$pass = $_SERVER['PHP_AUTH_PW'];

// vérification si l'utilisateur à le droit d'effectuer une requête
if (verifieProprietaire($user, $pass)) {
    return TRUE;
} else {
    header("HTTP/1.0 401 unauthorized");
    return FALSE;
}

/**
 * Vérifie si le propriétaire est connecté
 * @param string $courriel      - email du poprietaire
 * @param string $motDePasse    - mdp du proprietaire
 * @return boolean              - a le droit à la modification
 */
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