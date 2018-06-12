<?php

/**
 * Modifie uniquement les informations de la table TLOCALISATION
 * @param double $lat           - nouvelle latitude du food truck
 * @param double $lon           - nouvelle longitude du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
 */
function modifierLocalisationInfo($lat, $lon, $idFoodTruck) {
    $idLocalisation = creerLieu($lat, $lon);
    if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("UPDATE `TESTA` SET `idLocalisation`= :idLocalisation WHERE `idFoodTruck` = :idFoodTruck");
    }
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->bindParam(':idLocalisation', $idLocalisation, PDO::PARAM_INT);
    $requestUpdate->execute();
}

$latFoodTruck = filter_input(INPUT_POST, 'latFoodTruck', FILTER_VALIDATE_FLOAT);
$lonFoodTruck = filter_input(INPUT_POST, 'lonFoodTruck', FILTER_VALIDATE_FLOAT);