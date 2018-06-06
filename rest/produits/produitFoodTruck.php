<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : produits/produitFoodTruck.php
 * Description  : Récupère tous les produits sur la base de données vendus par un food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

/**
 * Récupère les produits selon l'id d'un food truck
 * @param int $id - id du food truck
 * @return string - tous les produits vendus
 */
function getProduitSelonIdFoodTruck($id) {
    $db = getDB();
    $request = $db->prepare("SELECT TPRODUIT.`idProduit`, TPRODUIT.`Nom` FROM `TPRODUIT`, TFOODTRUCK, TVEND WHERE TFOODTRUCK.idFoodTruck = :id AND TVEND.idProduit = TPRODUIT.idProduit");
    $request->bindParam(':id', $id, PDO::PARAM_INT);
    $request->execute();
    $data = $request->fetchAll(PDO::FETCH_ASSOC);
    $array = array();
    for ($i=0; $i < count($data); $i++) {
        if ($i == count($data)-1) {
            $str .= $data[$i]['Nom'];
        }
        else{
            $str .= $data[$i]['Nom'] . ", ";
        }
    }
    return $str;
}