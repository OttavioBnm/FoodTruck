<?php

require '../localisations/nouveau.php';

static $requestUpdate = NULL;

/**
 * Modifie uniquement les informations de la table TLOCALISATION
 * @param double $lat           - nouvelle latitude du food truck
 * @param double $lon           - nouvelle longitude du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
 */
function modifierLocalisationInfo($lat, $lon, $idFoodTruck, $jSemaine) {
    $idLocalisation = creerLieu($lat, $lon);
    error_log("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
    error_log($idFoodTruck);
    try {
        if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("UPDATE `TESTA` SET `idLocalisation`= :idLocalisation WHERE `idFoodTruck` = :idFoodTruck AND `JourSemaine` = :jSemaine");
    }
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->bindParam(':idLocalisation', $idLocalisation, PDO::PARAM_INT);
    $requestUpdate->bindParam(':jSemaine', $jSemaine, PDO::PARAM_INT);
    $requestUpdate->execute();
    } catch (Exception $exc) {
        error_log($exc);
    }
}

$latFoodTruck = filter_input(INPUT_POST, 'latitude', FILTER_VALIDATE_FLOAT);
$lonFoodTruck = filter_input(INPUT_POST, 'longitude', FILTER_VALIDATE_FLOAT);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_SANITIZE_STRING);
$jSemaine = filter_input(INPUT_POST, 'jourSemaine', FILTER_SANITIZE_STRING);

modifierLocalisationInfo($lat, $lon, $idFoodTruck, $jSemaine);