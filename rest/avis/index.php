<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : avis/index.php
 * Description  : Retourne la moyenne des notes d'un food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

/**
 * Fait la moyenne de tous les avis d'un food truck
 * @param int $idFoodTruck  - id du food truck dont on souhaite les avis
 * @return                  - double moyenne de tous les avis d'un food truck
 */
function getNoteFoodTruck($idFoodTruck) {
    $db = getDB();
    $request = $db->prepare("SELECT AVG(`Note`) FROM `TAVIS` WHERE `idFoodTruck` = :id");
    $request->bindParam('id', $idFoodTruck, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    foreach ($data as $value) {
        $str = $value['AVG(`Note`)'];
    }
    return $str;
}

