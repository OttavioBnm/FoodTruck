<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : horaire/modifier.php
 * Description  : Permet de modifier l'horaire d'un food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

/**
 * Modifie uniquement les informations de la table TESTA
 * @param string $heureDebut    - nouvelle heure de début du food truck
 * @param string $heureFin      - nouvelle heure de fin du food truck
 * @param string $jourSemaine   - nouveau jour de la semaine du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
 */
function modifierHoraire($heureDebut, $heureFin, $jourSemaine, $idFoodTruck) {
    if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("UPDATE `TESTA` SET `HoraireDebut`= :heureDebut,`HoraireFin`= :heureFin,`JourSemaine`= :jourSemaine WHERE `idFoodTruck` = :idFoodTruck");
    }
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->bindParam(':heureDebut', $heureDebut, PDO::PARAM_STR);
    $requestUpdate->bindParam(':heureFin', $heureFin, PDO::PARAM_STR);
    $requestUpdate->bindParam(':jourSemaine', $jourSemaine, PDO::PARAM_STR);
    $requestUpdate->execute();
}

$heureDebutFoodTruck = filter_input(INPUT_POST, 'heureDebutFoodTruck', FILTER_SANITIZE_STRING);
$heureFinFoodTruck = filter_input(INPUT_POST, 'heureFinFoodTruck', FILTER_SANITIZE_STRING);
$jourSemaineFoodTruck = filter_input(INPUT_POST, 'jourSemaineFoodTruck', FILTER_SANITIZE_STRING);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);
