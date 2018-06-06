<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : fonctions.inc/connexion.php
 * Description  : Permet de connecter un propriétaire à l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Vérifie si un propriétaire est présent dans la base de données
 * @return array de json|boolean    - retourne le propriétaire
 *                                  - retourne false si son mdp ou son email est faux
 */
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