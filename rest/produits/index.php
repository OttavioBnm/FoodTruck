<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : produits/index.php
 * Description  : Récupère tous les produits sur la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Classe modélisant un produit
 */
class Produit {

    public $idProduit;
    public $nom;

}

/**
 * Récupère les produits dur la base de données
 * @return array de json - tableau de produits en json
 */
function getProduits() {
    $db = getDB();
    $request = $db->prepare("SELECT * FROM `TPRODUIT`");
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    $array = array();
    foreach ($data as $entry) {
        $obj = new Produit();
        $obj->idProduit = $entry['idProduit'];
        $obj->nom = $entry['Nom'];
        $array[] = $obj;
    }
    return json_encode($array);
}
echo getProduits();
