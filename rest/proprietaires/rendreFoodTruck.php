<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : proprietaires/rendreFoodTruck.php
 * Description  : Permet à un propriétaire de rendre un food truck approprié auparavant
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require du script d'authentification
require "../fonctions.inc/verifieProprietaire.php";

/**
 * Rends un food truck sans propriétaire
 * @param int $idFoodTruck      - id du food truck à rendre
 * @param int $idProprietaire   - id du propriétaire qui veut rendre
 */
function rendreUnFoodTruck($idFoodTruck, $idProprietaire) {
    if (verifieProprietaire($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW'])) {
        try {
            $db = getDB();
            $request = $db->prepare("UPDATE `TFOODTRUCK` SET `idProprietaire`= NULL WHERE idFoodTruck = :idFoodTruck AND `idProprietaire`= :idProprietaire");
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

// données provenant de l'application
$idProprietaire = filter_input(INPUT_POST, 'idProprietaire', FILTER_VALIDATE_INT);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

rendreUnFoodTruck($idFoodTruck, $idProprietaire);
