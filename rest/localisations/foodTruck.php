<?php
static $requestUpdate = NULL;

// Require pour le script
require '../pdo.php';

/**
 * Modifie uniquement les informations de la table TLOCALISATION
 * @param double $lat           - nouvelle latitude du food truck
 * @param double $lon           - nouvelle longitude du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
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
