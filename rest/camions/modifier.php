<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/modifier.php
 * Description  : Modifie un food truck avec les nouvelles données provenant de l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require du scripte d'ajout de localisation
require '../pdo.php';

/**
 * Modifie un food truck sur la base de données
 * @param int $idFoodTruck      - id du food truck à modifier
 * @param string $nom           - nouveau nom du food truck
 * @param string $image         - nouvelle image du food truck
 * @param double $lat           - nouvelle latitude du food truck
 * @param double $lon           - nouvelle longitude du food truck
 * @param string $heureDebut    - nouvelle heure de début du food truck
 * @param string $heureFin      - nouvelle heure de fin du food truck
 * @param string $jourSemaine   - nouveau jour de la semaine du food truck
 * @param string $contact       - nouveau contact du food truck
 */
/*function modifierFoodTruckSelonId($idFoodTruck, $nom, $image, $lat, $lon, $heureDebut, $heureFin, $jourSemaine, $contact) {
    modifierSeulementFoodTruck($nom, $image, $contact, $idFoodTruck);
    modifierLocalisationInfo($lat, $lon, $idFoodTruck);
    modifierHoraire($heureDebut, $heureFin, $jourSemaine, $idFoodTruck);
}*/

/**
 * Modifie uniquement les informations de la table TFOODTRUCK
 * @param string $nom           - nouveau nom du food truck
 * @param string $image         - nouvelle image du food truck
 * @param string $contact       - nouveau contact du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
 */
function modifierSeulementFoodTruck($idFoodTruck, $nom, $image, $contact){
    if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("UPDATE `TFOODTRUCK` SET `Nom`= :nom,`Image`=:image,`Contact`= :contact WHERE `idFoodTruck` = :idFoodTruck");
    }
    $requestUpdate->bindParam(':nom', $nom, PDO::PARAM_STR);
    $requestUpdate->bindParam(':image', $image, PDO::PARAM_STR);
    $requestUpdate->bindParam(':contact', $contact, PDO::PARAM_STR);
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->execute();
}

// Données provenant de l'application
$nomFoodTruck = filter_input(INPUT_POST, 'nomFoodTruck', FILTER_SANITIZE_STRING);
$imageFoodTruck = filter_input(INPUT_POST, 'imageFoodTruck', FILTER_SANITIZE_STRING);
$contactFoodTruck = filter_input(INPUT_POST, 'contactFoodTruck', FILTER_SANITIZE_STRING);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

// Modification
modifierSeulementFoodTruck($idFoodTruck, $nomFoodTruck, $imageFoodTruck, $contactFoodTruck);
