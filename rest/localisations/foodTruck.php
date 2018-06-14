<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : localisation/foodTruck.php
 * Description  : Récupère une localisation selon l'identifiant du food trcuk
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

static $requestUpdate = NULL;

// Require pour le script
require '../pdo.php';

/**
 * Récupère les informations sur une localisation
 * @param int $idFoodTruck      - id du food truck
 */
function getLocalisationInfo($idFoodTruck) {
    if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("SELECT TLOCALISATION.Latitude, TLOCALISATION.Longitude, `HoraireDebut`, `HoraireFin`, `JourSemaine` FROM `TESTA`, TLOCALISATION WHERE TESTA.idLocalisation = TLOCALISATION.idLocalisation AND `idFoodTruck` = :idFoodTruck");
    }
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->execute();
    $data = $requestUpdate->fetchAll(PDO::FETCH_ASSOC);
    $arrayStr = array();
    for ($index = 0; $index < count($data); $index++) {
        $arrayStr[$index]= trim(implode(",", $data[$index]));
    }
    echo json_encode($arrayStr);
}

$idFoodTruck = filter_input(INPUT_GET, 'idFoodTruck', FILTER_VALIDATE_INT);
getLocalisationInfo($idFoodTruck);
