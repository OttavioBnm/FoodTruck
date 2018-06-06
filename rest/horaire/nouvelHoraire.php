<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : horaire/nouvelHoraire.php
 * Description  : Ajoute un nouvel horaire à un food truck
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

/**
 * Ajoute la nouvel horaire du food truck dans la base de données
 * @param int $idFoodTruck      - Id du food truck concerné par le nouvel horaire
 * @param int $idLocalisation   - Id du lieu concerné
 * @param string $heureDebut    - Heure de début de la vente
 * @param string $heureFin      - Heure de fin de la vente 
 * @param string $jourSemaine   - Jour de la semaine concerné
 * @return boolean              - Si l'ajout est effectué avec succès -> retourne true
 *                              - Si l'ajout n'est pas effectué avec succès -> retourne false
 */
function creerHoraire($idFoodTruck, $idLocalisation, $heureDebut, $heureFin, $jourSemaine) {
    try {
        $request = "INSERT INTO `TESTA`(`idFoodTruck`, `idLocalisation`, `HoraireDebut`, `HoraireFin`, `JourSemaine`) VALUES (:idFoodTruck, :idLocalisation, :heureDebut, :heureFin, :jourSemaine)";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
        $dbQuery->bindParam(':idLocalisation', $idLocalisation, PDO::PARAM_INT);
        $dbQuery->bindParam(':heureDebut', $heureDebut, PDO::PARAM_STR);
        $dbQuery->bindParam(':heureFin', $heureFin, PDO::PARAM_STR);
        $dbQuery->bindParam(':jourSemaine', $jourSemaine, PDO::PARAM_STR);
        $dbQuery->execute();
    } catch (Exception $exc) {
        return FALSE;
    }
    return TRUE;
}
