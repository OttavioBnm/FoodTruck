<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : avis/nouveau.php
 * Description  : Ajoute un nouvel avis sur un food truck dans la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Ajoute un avis à la base pour un FoodTruck
 * @param int $idFoodTruck      - id du food truck concerné par l'avis
 * @param int $note             - note attribuée
 */
function ajouterAvis($idFoodTruck, $note) {
    if (empty($idFoodTruck) || empty($note)) {
        header("HTTP/1.1 500 Internal Server Error");
    } else {
        try {
            $db = getDB();
            $request = $db->prepare("INSERT INTO `TAVIS`(`Note`, `idFoodTruck`) VALUES (:note, :idFoodTruck)");
            $request->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
            $request->bindParam(':note', $note, PDO::PARAM_INT);
            $request->execute();
        } catch (Exception $exc) {
            header("HTTP/1.1 500 Internal Server Error");
        }
    }
}

// Récupération des données de l'application
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);
$note = filter_input(INPUT_POST, 'note', FILTER_VALIDATE_INT);

ajouterAvis($idFoodTruck, $note);