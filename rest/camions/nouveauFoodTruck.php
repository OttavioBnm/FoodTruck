<?php

// Require du PDO
require "../pdo.php";

// required headers
header("Access-Control-Allow-Methods: POST");

/**
 * Permet l'ajout d'un nouveau food truck à la base de données avec toutes les caracteristiques
 * @param string $nom           - Nom du FT
 * @param string $image         - Nom de l'image du FT
 * @param double $lon           - Position longitudinale du FT
 * @param double $lat           - Position latitudinale du FT
 * @param string $heureDebut    - Heure de début de vente du FT
 * @param string $heureFin      - Heure de fin de vente du FT
 * @param string $jourSemaine   - Jour de la semaine poour la position du FT
 * @param int $note             - Note du FT (peut êstre NULL)
 * @param int $proprietaire     - Id du proprietaire du FT (peut être NULL)
 * @param string $contact       - Contact pour un FT (peut être NULL)
 * @return boolean $flagOK      - Témoin pour l'ajout du FT (true ou false)
 */
function creerFoodTruck($nom, $image, $lon, $lat, $heureDebut, $heureFin, $jourSemaine, $note, $proprietaire, $contact) {
    $flagOk = true;
    if (empty($nom) || empty($lon) || empty($lat) || empty($heureDebut) || empty($heureFin) || empty($jourSemaine) || empty($image)) {
        return false;
    }
    try {
        $truckExiste = verifierSiFoodTruckExiste($nom);
        $idLieu = creerLieu($lat, $lon);
        if ($idLieu !== FALSE) {
            if ($truckExiste === FALSE) {
                $idFoodTruck = ajouterFoodTruck($nom, $image, $contact, $proprietaire);
                creerHoraire($idFoodTruck, $idLieu, $heureDebut, $heureFin, $jourSemaine);
                if (!ajouterNote($idFoodTruck, $note)) {
                    $flagOk = FALSE;
                }
            } else if ($truckExiste === "Erreur") {
                $flagOk = FALSE;
            } else {
                creerHoraire($truckExiste, $idLieu, $heureDebut, $heureFin, $jourSemaine);
            }
        } else {
            $flagOk = FALSE;
        }
    } catch (Exception $e) {
        $flagOk = false;
    }
    return $flagOk;
}

/**
 * Ajoute un food truck à la base de données
 * @param string $nom           - Nom du FT
 * @param string $image         - Nom de l'image du FT
 * @param string $contact       - Contact pour un FT (peut être NULL)
 * @param int $idProprietaire   - Id du proprietaire du FT (peut être NULL)
 * @return boolean|int          - Si l'ajout est réussi -> retourne l'id du nouveau FT
 *                              - Si l'ajout n'est pas réussi -> retourne false
 */
function ajouterFoodTruck($nom, $image, $contact, $idProprietaire) {
    try {
        $request = "INSERT INTO `TFOODTRUCK`(`Nom`, `Image`, `Contact`, `idProprietaire`) VALUES (:nomFoodTruck, :image, :contact, :idProprietaire)";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':nomFoodTruck', $nom);
        $dbQuery->bindParam(':image', $image);
        $dbQuery->bindParam(':contact', $contact);
        $dbQuery->bindParam(':idProprietaire', $proprietaire);
    } catch (Exception $exc) {
        return FALSE;
    }
    $requestMaxId = "SELECT MAX(idFoodTruck) AS dernierId FROM TFOODTRUCK LIMIT 1";
    $dbQuery = $connect->prepare($requestMaxId);
    $dbQuery->execute();
    $array = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
    foreach ($array as $value) {
        return $value['dernierId'];
    }
}

/**
 * Vérifie si le food truck est déjà présent sur la base de données
 * @param string $nom             - Nom du FT
 * @return boolean|int|string   - Si le FT existe -> retourne l'id de celui-ci
 *                              - Si le FT n'existe pas -> retourne false
 *                              - Si une erreur s'est produite -> retourne "Erreur"
 */
function verifierSiFoodTruckExiste($nom) {
    try {
        $request = "SELECT * FROM `TFOODTRUCK` WHERE `Nom` = :nomFoodTruck";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':nomFoodTruck', $nom);
        $dbQuery->execute();
        $foodTrucks = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
        if (empty($foodTrucks)) {
            return FALSE;
        } else {
            foreach ($foodTrucks as $value) {
                return $value['idFoodTruck'];
            }
        }
    } catch (Exception $exc) {
        return "Erreur";
    }
}

/**
 * Ajoute un lieu à la base de données ou récupère l'id d'un lieu existant
 * @param double $lat   - Latitude du lieu
 * @param double $lon   - Longitude du lieu
 * @return boolean|int  - L'id du lieu concerné
 *                      - Si une erreur se produit -> retourne false
 */
function creerLieu($lat, $lon) {
    try {
        $LieuExiste = verifierSiLieuExiste($lat, $lon);
        if ($LieuExiste === FALSE) {
            return ajouterLieu($lat, $lon);
        } else if ($LieuExiste === "Erreur") {
            return FALSE;
        } else {
            return $LieuExiste;
        }
    } catch (Exception $ex) {
        return FALSE;
    }
}

/**
 * Ajoute un lieu à la base de données
 * @param double $lat       - Latitude du lieu
 * @param double $lon       - Longitude du lieu
 * @return boolean|int      - Si le lieu est ajouté -> retourne l'id de celui-ci 
 *                          - Si le lieu n'est pas ajouté -> retourne false
 */
function ajouterLieu($lat, $lon) {
    try {
        $request = "INSERT INTO `TLOCALISATION`(`Latitude`, `Longitude`) VALUES (:latitude, :longitude)";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':latitude', $lat);
        $dbQuery->bindParam(':longitude', $lon);
        $dbQuery->execute();
    } catch (Exception $exc) {
        return FALSE;
    }
    $requestMaxId = "SELECT MAX(idLocalisation) AS dernierId FROM TLOCALISATION LIMIT 1";
    $dbQuery = $connect->prepare($requestMaxId);
    $dbQuery->execute();
    $array = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
    foreach ($array as $value) {
        return $value['dernierId'];
    }
}

/**
 * Vérifie si le lieu existe sur la base de données
 * @param double $lat - Latitude du lieu
 * @param double $lon - Longitude du lieu
 * @return boolean|string|int   - Si le lieu existe -> retourne l'id de celui-ci
 *                              - Si le lieu n'existe pas -> retourne false
 *                              - Si une erreur se produit -> retourne "Erreur"
 */
function verifierSiLieuExiste($lat, $lon) {
    try {
        $request = "SELECT * FROM `TLOCALISATION` WHERE `Latitude` = :latitude AND `Longitude` = :longitude LIMIT 1";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':latitude', $lat);
        $dbQuery->bindParam(':longitude', $lon);
        $dbQuery->execute();
        $locArray = $dbQuery->fetchAll(PDO::FETCH_ASSOC);
        if (empty($locArray)) {
            return FALSE;
        } else {
            foreach ($locArray as $value) {
                return $value['idLocalisation'];
            }
        }
    } catch (Exception $exc) {
        return "Erreur";
    }
}

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

/**
 * Ajoute un avis sur un food truck 
 * @param type $idFoodTruck - FT concerné
 * @param type $note        - Note attribuée
 * @return boolean          - Retourne false si une erreure est survenue
 */
function ajouterNote($idFoodTruck, $note) {
    if ($note !== NULL) {
        try {
            $request = "INSERT INTO `TAVIS`(`Note`, `idFoodTruck`) VALUES (:note,:idFoodTruck)";
            $connect = getDB();
            $dbQuery = $connect->prepare($request);
            $dbQuery->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
            $dbQuery->bindParam(':note', $note, PDO::PARAM_INT);
            $dbQuery->execute();
        } catch (Exception $exc) {
            return FALSE;
        }
    }
}

$nomFoodTruck = filter_input(INPUT_POST, 'nomFoodTruck', FILTER_SANITIZE_STRING);
$imageFoodTruck = filter_input(INPUT_POST, 'imageFoodTruck', FILTER_SANITIZE_STRING);
$contactFoodTruck = filter_input(INPUT_POST, 'contactFoodTruck', FILTER_SANITIZE_STRING);
$latFoodTruck = filter_input(INPUT_POST, 'latFoodTruck', FILTER_VALIDATE_FLOAT);
$lonFoodTruck = filter_input(INPUT_POST, 'lonFoodTruck', FILTER_VALIDATE_FLOAT);
$heureDebutFoodTruck = filter_input(INPUT_POST, 'heureDebutFoodTruck', FILTER_SANITIZE_STRING);
$heureFinFoodTruck = filter_input(INPUT_POST, 'heureFinFoodTruck', FILTER_SANITIZE_STRING);
$jourSemaineFoodTruck = filter_input(INPUT_POST, 'jourSemaineFoodTruck', FILTER_SANITIZE_STRING);
$idProprietaireFoodTruck = filter_input(INPUT_POST, 'idProprietaireFoodTruck', FILTER_VALIDATE_INT);
$noteFoodTruck = filter_input(INPUT_POST, 'noteFoodTruck', FILTER_VALIDATE_INT);

if (creerFoodTruck($nomFoodTruck, $imageFoodTruck, $lonFoodTruck, $latFoodTruck, $heureDebutFoodTruck, $heureFinFoodTruck, $jourSemaineFoodTruck, $noteFoodTruck, $idProprietaireFoodTruck, $contactFoodTruck)) {
    echo json_encode("Succès");
} else {
    header("HTTP/1.0 501 Intrnal Error");
}
