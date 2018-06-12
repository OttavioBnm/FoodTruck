<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/proprietaire.php
 * Description  : Retourne tous les food trucks d'un propriétaire
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require pour le script
require '../pdo.php';
require "../produits/produit.php";
require "../avis/index.php";

/**
 * Classe modélisant un food truck
 */
class FoodTruck {

    public $idFoodTruck;
    public $nom;
    public $image;
    public $evaluation;
    public $latitude;
    public $heureDebut;
    public $longitude;
    public $heureFin;
    public $jourSemaine;
    public $lstProduits;
    public $contact;

}

/**
 * Récupère tous les food truck de la base de donnée et les transforme en JSON selon le propriétaire
 * @return array de json - tous les food trucks dans un tableau
 */
function getFoodTrucksSelonProprieatire($idProprietaire) {
    try {
        $db = getDB();
        $request = $db->prepare("SELECT * FROM TFOODTRUCK WHERE TFOODTRUCK.idProprietaire = :idProprietaire");
        $request->bindParam('idProprietaire', $idProprietaire, PDO::PARAM_INT);
        $request->execute();
        $data = $request->fetchAll(PDO::FETCH_ASSOC);
        $array = array();
        foreach ($data as $entry) {
            $obj = new FoodTruck();
            $obj->idFoodTruck = $entry['idFoodTruck'];
            $obj->nom = $entry['Nom'];
            $obj->image = $entry['Image'];
            $obj->evaluation = getNoteFoodTruck($obj->idFoodTruck);
            $obj->latitude = $entry['Latitude'];
            $obj->longitude = $entry['Longitude'];
            $obj->heureDebut = $entry['HoraireDebut'];
            $obj->heureFin = $entry['HoraireFin'];
            $obj->jourSemaine = $entry['JourSemaine'];
            $obj->contact = $entry['Contact'];
            $obj->lstProduits = getProduitSelonIdFoodTruck($obj->idFoodTruck);
            $array[] = $obj;
        }
        return json_encode($array);
    } catch (Exception $exc) {
        header("HTTP/1.1 500 Internal Server Error");
    }
}

$idProprietaire = filter_input(INPUT_POST, 'idProprietaire', FILTER_VALIDATE_INT);
// Affichage du Json
echo getFoodTrucksSelonProprieatire($idProprietaire);
