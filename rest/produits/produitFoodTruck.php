<?php

// Require du PDO
require "../pdo.php";

class Produit {

    public $idProduit;
    public $nom;

}

function getProduitSelonIdFoodTruck($id) {
    $db = getDB();
    $request = $db->prepare("SELECT TPRODUIT.`idProduit`, TPRODUIT.`Nom` FROM `TPRODUIT`, TFOODTRUCK, TVEND WHERE TFOODTRUCK.idFoodTruck = :id AND TVEND.idProduit = TPRODUIT.idProduit");
    $request->bindParam(':id', $id, PDO::PARAM_INT);
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
echo getProduitSelonIdFoodTruck(1);