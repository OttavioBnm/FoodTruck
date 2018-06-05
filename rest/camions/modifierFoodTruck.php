<?php

// Require du PDO
require "../pdo.php";

function modifierFoodTruckId($idFoodTruck, $nom, $image, $lon, $lat, $heureDebut, $heureFin, $jourSemaine, $note, $proprietaire, $contact) {
    if ($requestUpdate == NULL) {
        $db = getDB();
        $requestUpdate = $db->prepare("UPDATE
  TFOODTRUCK
INNER JOIN
  TESTA ON TFOODTRUCK.idFoodTruck = TFOODTRUCK.idFoodTruck
SET
  `Nom`= :nom,
  `Image` = :image,
  `Contact` = :contact,
  `idProprietaire` = :idProprietaire,
  TESTA.`idLocalisation` = :idLocalisation,
  TESTA.HoraireDebut = :heureDebut,
  TESTA.HoraireFin = :heureFin,
  TESTA.JourSemaine = :jourSemaine
WHERE
  TFOODTRUCK.idFoodTruck = :idFoodTruck
AND
  TESTA.idFoodTruck = :idFoodTruck");
    }
    $requestUpdate->bindParam(':nom', $nom, PDO::PARAM_STR);
    $requestUpdate->bindParam(':image', $image, PDO::PARAM_STR);
    $requestUpdate->bindParam(':heureDebut', $heureDebut, PDO::PARAM_STR);
    $requestUpdate->bindParam(':heureFin', $heureFin, PDO::PARAM_STR);
    $requestUpdate->bindParam(':jourSemaine', $jourSemaine, PDO::PARAM_STR);
    $requestUpdate->bindParam(':idProprietaire', $proprietaire, PDO::PARAM_INT);
    $requestUpdate->bindParam(':contact', $contact, PDO::PARAM_STR);
    $requestUpdate->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
    $requestUpdate->execute();
}
