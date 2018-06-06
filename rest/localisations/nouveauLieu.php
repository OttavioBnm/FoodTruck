<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : localisation/nouveauLieu.php
 * Description  : Création d'un nouveau lieu ou mise à jour d'un existant
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require du pdo
require '../pdo.php';

/**
 * Ajoute un lieu à la base de données ou récupère l'id d'un lieu existant
 * @param double $lat   - Latitude du lieu
 * @param double $lon   - Longitude du lieu
 * @return boolean|int  - L'id du lieu concerné
 *                      - Si une erreur se produit -> retourne false
 */
function creerLieu($lat, $lon) {
    try {
        $LieuExiste = verifierSiLieuExiste($lat, $lon);
        if ($LieuExiste === FALSE) {
            return ajouterLieu($lat, $lon);
        } else if ($LieuExiste === "Erreur") {
            return FALSE;
        } else {
            return $LieuExiste;
        }
    } catch (Exception $ex) {
        return FALSE;
    }
}

/**
 * Ajoute un lieu à la base de données
 * @param double $lat       - Latitude du lieu
 * @param double $lon       - Longitude du lieu
 * @return boolean|int      - Si le lieu est ajouté -> retourne l'id de celui-ci 
 *                          - Si le lieu n'est pas ajouté -> retourne false
 */
function ajouterLieu($lat, $lon) {
    try {
        $request = "INSERT INTO `TLOCALISATION`(`Latitude`, `Longitude`) VALUES (:latitude, :longitude)";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':latitude', $lat);
        $dbQuery->bindParam(':longitude', $lon);
        $dbQuery->execute();
    } catch (Exception $exc) {
        return FALSE;
    }
    $requestMaxId = "SELECT MAX(idLocalisation) AS dernierId FROM TLOCALISATION LIMIT 1";
    $dbQuery = $connect->prepare($requestMaxId);
    $dbQuery->execute();
    $array = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
    foreach ($array as $value) {
        return $value['dernierId'];
    }
}

/**
 * Vérifie si le lieu existe sur la base de données
 * @param double $lat - Latitude du lieu
 * @param double $lon - Longitude du lieu
 * @return boolean|string|int   - Si le lieu existe -> retourne l'id de celui-ci
 *                              - Si le lieu n'existe pas -> retourne false
 *                              - Si une erreur se produit -> retourne "Erreur"
 */
function verifierSiLieuExiste($lat, $lon) {
    try {
        $request = "SELECT * FROM `TLOCALISATION` WHERE `Latitude` = :latitude AND `Longitude` = :longitude LIMIT 1";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':latitude', $lat);
        $dbQuery->bindParam(':longitude', $lon);
        $dbQuery->execute();
        $locArray = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
        if (empty($locArray)) {
            return FALSE;
        } else {
            foreach ($locArray as $value) {
                return $value['idLocalisation'];
            }
        }
    } catch (Exception $exc) {
        return "Erreur";
    }
}
