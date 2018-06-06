<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : fonctions.inc/sauvegarderImages.php
 * Description  : Sauvegarde les images sur le server
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require du pdo
require "../pdo.php";

// id du food truck pour laquelle on met l'image sur le server
$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

/**
 * Transfère une image sur le server
 * @param string $typeMedia     - définit le type d'image sur le server
 * @param array $ext            - tableau des extensions permises
 * @param int $idFoodTruck      - id du food truck
 */
function transfererSurServeur($typeMedia, $ext, $idFoodTruck) {
    var_dump($_FILES['image']['name']);
    if (isset($_FILES['image'])) {
        $dossier = "../img/";
        $extensions = $ext;
        $taille_maxi = 100000000;
        var_dump($_FILES);
        $fichier = microtime() . basename($_FILES['image']['name']);
        if (!isset($erreur)) { //S'il n'y a pas d'erreur, on upload
            var_dump($_FILES['image']['name']);
            //On formate le nom du fichier ici...
            $fichier = strtr($fichier, 'ÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜÝàáâãäåçèéêëìíîïðòóôõöùúûüýÿ', 'AAAAAACEEEEIIIIOOOOOUUUUYaaaaaaceeeeiiiioooooouuuuyy');
            $fichier = preg_replace('/([^.a-z0-9]+)/i', '-', $fichier);
            if (move_uploaded_file($_FILES['image']['tmp_name'], $dossier.$fichier)) { //Si la fonction renvoie TRUE, c'est que ça a fonctionné...
                var_dump($idFoodTruck);
                assignerFoodTruck($fichier, $idFoodTruck);
                json_encode("Réussi");
            } else { //Sinon (la fonction renvoie FALSE).
                json_encode('Echec de l\'upload !');
                header("Status: HTTP/1.0 500 Internal Error");
            }
        } else {
            header("Status: HTTP/1.0 500 Internal Error");
            echo $erreur;
        }
    }
}

/**
 * Assigne le nouveau media au food truck
 * @param string $nomMedia    - nom du media
 * @param int $idFoodTruck - id du food truck
 */
function assignerFoodTruck($nomMedia, $idFoodTruck){
    try {
        $db = getDB();
        $request = $db->prepare("UPDATE `TFOODTRUCK` SET `Image` = :nomMedia WHERE `idFoodTruck` = :id");
        $request->bindParam(':nomMedia',$nomMedia,PDO::PARAM_STR);
        $request->bindParam(':id',$idFoodTruck,PDO::PARAM_INT);
        $request->execute();
    } catch (Exception $e) {
        header("Status: HTTP/1.0 500 Internal Error");
    }
}

// Transfert
transfererSurServeur("img", array('.png', '.gif', '.jpg', '.jpeg'), $idFoodTruck);