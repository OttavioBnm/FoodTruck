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
//$idFoodTruck = filter_input(INPUT_POST, 'idFoodTruck', FILTER_VALIDATE_INT);

/**
 * Transfère une image sur le server
 * @param string $typeMedia     - définit le type d'image sur le server
 * @param array $ext            - tableau des extensions permises
 */
function transfererSurServeur($typeMedia, $ext) {
    if (isset($_FILES['image'])) {
        $dossier = "../img/";
        $extensions = $ext;
        $taille_maxi = 100000000;
        $fichier = basename($_FILES['image']['name']);
        if (!isset($erreur)) { //S'il n'y a pas d'erreur, on upload
            var_dump($_FILES['image']['name']);
            //On formate le nom du fichier ici...
            $fichier = strtr($fichier, 'ÀÁÂÃÄÅÇÈÉÊËÌÍÎÏÒÓÔÕÖÙÚÛÜÝàáâãäåçèéêëìíîïðòóôõöùúûüýÿ', 'AAAAAACEEEEIIIIOOOOOUUUUYaaaaaaceeeeiiiioooooouuuuyy');
            $fichier = preg_replace('/([^.a-z0-9]+)/i', '-', $fichier);
            if (move_uploaded_file($_FILES['image']['tmp_name'], $dossier.$fichier)) { //Si la fonction renvoie TRUE, c'est que ça a fonctionné...
                json_encode("Réussi");
            } else { //Sinon (la fonction renvoie FALSE).
                json_encode('Echec de l\'upload !');
                header("HTTP/1.1 500 Internal Error");
            }
        } else {
            header("HTTP/1.1 500 Internal Error");
            echo $erreur;
        }
    }
 else {
        header("HTTP/1.1 500 Internal Error");
    }
}

// Transfert
transfererSurServeur("img", array('.png', '.gif', '.jpg', '.jpeg'));