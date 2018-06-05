<?php

// Require du PDO
require "../pdo.php";

class Localisation {

    public $idLocalisation;
    public $latitude;
    public $longitude;

}

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