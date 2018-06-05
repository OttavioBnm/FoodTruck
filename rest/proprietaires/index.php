<?php

// Require du PDO
require "../pdo.php";

class Proprietaire {

    public $idProprietaire;
    public $nom;
    public $prenom;
    public $pseudo;
    public $courriel;
    public $motDePasse;

}

function getProprietareSelonId($id) {
    $db = getDB();
    $request = $db->prepare("SELECT `TPROPRIETAIRE`.`idProprietaire`, `TPROPRIETAIRE`.`Nom`, `Prenom`, `Pseudo`, `Courriel`, `MotDePasse` FROM `TPROPRIETAIRE` WHERE `TPROPRIETAIRE`.`IdProprietaire` = :id");
    $request->bindParam(':id', $id, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    $array = array();
    foreach ($data as $entry) {
        $obj = new Proprietaire();
        $obj->idProprietaire = $entry['idProprietaire'];
        $obj->nom = $entry['Nom'];
        $obj->prenom = $entry['Prenom'];
        $obj->pseudo = $entry['Pseudo'];
        $obj->courriel = $entry['Courriel'];
        $obj->motDePasse = $entry['MotDePasse'];
        $array[] = $obj;
    }
    return json_encode($array);
}
echo getProprietareSelonId(1);