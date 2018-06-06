<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : localisation/index.php
 * Description  : Récupère une localisation
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Classe qui modélise une localisation
 */
class Localisation {

    public $idLocalisation;
    public $latitude;
    public $longitude;

}

/**
 * Récupère une localisation selon son id
 * @param int $id           - id de la localisation
 * @return array de json    - localisation en json
 */
function getLocalisationSelonId($id) {
    $db = getDB();
    $request = $db->prepare("SELECT * FROM `TLOCALISATION` WHERE `idLocalisation` = :id");
    $request->bindParam(':id', $id, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    $array = array();
    foreach ($data as $entry) {
        $obj = new Localisation();
        $obj->idLocalisation = $entry['idLocalisation'];
        $obj->latitude = $entry['Latitude'];
        $obj->longitude = $entry['Longitude'];
        $array[] = $obj;
    }
    return json_encode($array);
}
echo getLocalisationSelonId(1);