<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : proprietaires/index.php
 * Description  : Récupère un propriétaire de la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Classe qui modélise un propriétaire
 */
class Proprietaire {

    public $idProprietaire;
    public $nom;
    public $prenom;
    public $pseudo;
    public $courriel;
    public $motDePasse;

}

/**
 * Récupère un propriétaire sur la base de données selon un id
 * @param int $id - id du propriétaire à récupérer
 * @return json - propriétaire sous forme de json
 */
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

// donnée de l'application
$idProprietaire = filter_input(INPUT_POST, 'idProprietaire', FILTER_VALIDATE_INT);
echo getProprietareSelonId($idProprietaire);