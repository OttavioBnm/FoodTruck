<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : fonctions.inc/supprimerImageServer.php
 * Description  : Supprime un image qui est sur le server
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

/**
 * Supprime l'image du server
 * @param string $nomMedia - nom de l'image à supprimer
 */
function supprimerMediaDuServer($nomMedia) {
    unlink("img/" . $nomMedia);
}
