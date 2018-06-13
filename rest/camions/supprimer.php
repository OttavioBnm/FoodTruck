<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/supprimer.php
 * Description  : Supprime un food truck de l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

require '../pdo.php';
require '../fonctions.inc/verifieProprietaire.php';
require '../fonctions.inc/supprimerImageServer.php';

/**
 * Supprime un food truck de la base de données
 * @param type $idFoodTruck
 */
function supprimerFoodTruckSelonId($idFoodTruck) {
    if (verifieProprietaire($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW'])) {
        var_dump($idFoodTruck);
        try {
            $db = getDB();
            $request = $db->prepare("DELETE FROM `TFOODTRUCK` WHERE `idFoodTruck` = :idFoodTruck");
            $request->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
            $request->execute();
        } catch (Exception $exc) {
            header("HTTP/1.1 500 Internal Server Error");
        }
    } else {
        header("HTTP/1.1 403 Forbidden");
    }
}

$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);
$imageFoodTruck = filter_input(INPUT_POST, 'image', FILTER_SANITIZE_STRING);

// Supprime l'image du server
supprimerMediaDuServer($imageFoodTruck);

// Supprime le food truck
supprimerFoodTruckSelonId($idFoodTruck);