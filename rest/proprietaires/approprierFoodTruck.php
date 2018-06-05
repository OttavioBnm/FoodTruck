<?php

include "../fonctions.inc/verifieProprietaire.php";

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
$idProprietaire = filter_input(INPUT_POST, 'idProprietaire', FILTER_VALIDATE_INT);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

sApproprierUnFoodTruck($idFoodTruck, $idProprietaire);