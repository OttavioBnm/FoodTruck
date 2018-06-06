<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : proprietaires/nouveauProprietaire.php
 * Description  : Permet de créer un nouveau propriétaire sur la base de données
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// Require du PDO
require "../pdo.php";

/**
 * Création du nouveau propriétaire
 * @param string $nom           - nom du propriétaire
 * @param string $prenom        - prenom du propriétaire
 * @param string $pseudo        - pseudo du propriétaire
 * @param string $courriel      - email du propriétaire
 * @param string $motDePasse    - mdp du propriétaire
 */
function ajouterProprietaire($nom, $prenom, $pseudo, $courriel, $motDePasse) {
    if (empty($nom) || empty($prenom) || empty($pseudo) || empty($courriel) || empty($motDePasse)) {
        header("HTTP/1.1 500 Internal Server Error");
    } else {
        try {
            $db = getDB();
            $request = $db->prepare("INSERT INTO `TPROPRIETAIRE`(`Nom`, `Prenom`, `Pseudo`, `Courriel`, `MotDePasse`) VALUES (:nom,:prenom,:pseudo,:courriel,:motDePasse)");
            $request->bindParam(':nom', $nom, PDO::PARAM_STR);
            $request->bindParam(':prenom', $prenom, PDO::PARAM_STR);
            $request->bindParam(':pseudo', $pseudo, PDO::PARAM_STR);
            $request->bindParam(':courriel', $courriel, PDO::PARAM_STR);
            $request->bindParam(':motDePasse', $motDePasse, PDO::PARAM_STR);
            $request->execute();
        } catch (Exception $exc) {
            header("HTTP/1.1 500 Internal Server Error");
        }
    }
}

// données du proprétaire provenant de l'application
$nom = filter_input(INPUT_POST, 'Nom', FILTER_SANITIZE_STRING);
$prenom = filter_input(INPUT_POST, 'Prenom', FILTER_SANITIZE_STRING);
$pseudo = filter_input(INPUT_POST, 'Pseudo', FILTER_SANITIZE_STRING);
$courriel = filter_input(INPUT_POST, 'Courriel', FILTER_SANITIZE_STRING);
$motDePasse = filter_input(INPUT_POST, 'MotDePasse', FILTER_SANITIZE_STRING);

ajouterProprietaire($nom, $prenom, $pseudo, $courriel, $motDePasse);