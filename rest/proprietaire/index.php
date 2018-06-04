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

function getProprietareByIdFoodTruck($id) {
    $db = getDB();
    $request = $db->prepare("SELECT `tProprietaire`.`IdProprietaire`, `tProprietaire`.`Nom`, `Prenom`, `Pseudo`, `Email`, `Password` FROM `tProprietaire`, `tFoodTruck` WHERE `tFoodTruck`.`IdProprietaire` = `tProprietaire`.`IdProprietaire` AND tFoodTruck.IdFoodTruck = :id");
    $request->bindParam(':id', $id);
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
echo getProprietareByIdFoodTruck(filter_input(INPUT_GET, 'idFoodTruck', FILTER_VALIDATE_INT));