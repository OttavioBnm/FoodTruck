<?php

// Require du PDO
require "../pdo.php";

class Produit {

    public $idProduit;
    public $nom;

}

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
