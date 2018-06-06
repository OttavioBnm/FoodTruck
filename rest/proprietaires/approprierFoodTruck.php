<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : proprietaires/approprierFoodTruck.php
 * Description  : Permet à un propriétaire de s'approprier un food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require du script d'authentification
require "../fonctions.inc/verifieProprietaire.php";

/**
 * Associe un food truck à un proprietaire
 * @param int $idFoodTruck      - id du food truck à associer
 * @param int $idProprietaire   - id du proprietaire à associer
 */
function sApproprierUnFoodTruck($idFoodTruck, $idProprietaire) {
    if (verifieProprietaire($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW'])) {
        try {
            $db = getDB();
            $request = $db->prepare("UPDATE `TFOODTRUCK` SET `idProprietaire`= :idProprietaire WHERE idFoodTruck = :idFoodTruck");
            $request->bindParam(':idProprietaire', $idProprietaire, PDO::PARAM_INT);
            $request->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
            $request->execute();
        } catch (Exception $exc) {
            header("HTTP/1.1 500 Internal Server Error");
        }
    } else {
        header("HTTP/1.1 403 Forbidden");
    }
}

// récupération des données de l'application
$idProprietaire = filter_input(INPUT_POST, 'idProprietaire', FILTER_VALIDATE_INT);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

sApproprierUnFoodTruck($idFoodTruck, $idProprietaire);