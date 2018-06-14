<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/modifier.php
 * Description  : Modifie un food truck avec les nouvelles données provenant de l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require des différents script
require '../fonctions.inc/sauvegarderImage.php';
require '../fonctions.inc/supprimerImageServer.php';
require '../fonctions.inc/verifieProprietaire.php';

/**
 * Modifie uniquement les informations de la table TFOODTRUCK
 * @param string $nom           - nouveau nom du food truck
 * @param string $image         - nouvelle image du food truck
 * @param string $contact       - nouveau contact du food truck
 * @param int $idFoodTruck      - id du food truck à modifier
 */
function modifierSeulementFoodTruck($idFoodTruck, $nom, $image, $contact) {
    if (verifieProprietaire($_SERVER['PHP_AUTH_USER'], $_SERVER['PHP_AUTH_PW'])) {
        supprimerAncienMedia($idFoodTruck);
        try {
            if ($requestUpdate == NULL) {
                $db = getDB();
                $requestUpdate = $db->prepare("UPDATE `TFOODTRUCK` SET `Nom`= :nom,`Image`=:image,`Contact`= :contact WHERE `idFoodTruck` = :idFoodTruck");
            }
            $requestUpdate->bindParam(':nom', $nom, PDO::PARAM_STR);
            $requestUpdate->bindParam(':image', $image, PDO::PARAM_STR);
            $requestUpdate->bindParam(':contact', $contact, PDO::PARAM_STR);
            $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
            $requestUpdate->execute();
        } catch (Exception $exc) {
            echo $exc->getTraceAsString();
        }
    }else{
        header("HTTP/1.1 403 Forbidden");
    }
}

/**
 * Supprimer l'ancien media du server
 * @param int $idFoodTruck - identifiant unique du food truck
 */
function supprimerAncienMedia($idFoodTruck) {
    $db = getDB();
    $requestUpdate = $db->prepare("SELECT `Image` FROM `TFOODTRUCK` WHERE `idFoodTruck` = :idFoodTruck");
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->execute();
    $data = $requestUpdate->fetchAll(PDO::FETCH_ASSOC);
    supprimerMediaDuServer($data['image']);
}

// Données provenant de l'application
$nomFoodTruck = filter_input(INPUT_POST, 'nomFoodTruck', FILTER_SANITIZE_STRING);
$imageFoodTruck = filter_input(INPUT_POST, 'imageFoodTruck', FILTER_SANITIZE_STRING);
$contactFoodTruck = filter_input(INPUT_POST, 'contactFoodTruck', FILTER_SANITIZE_STRING);
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

// Modification
modifierSeulementFoodTruck($idFoodTruck, $nomFoodTruck, $imageFoodTruck, $contactFoodTruck);
