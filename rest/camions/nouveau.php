<?php
/*
 * Projet       : Food Truck Tracker (service web)
 * Nom          : camions/nouveau.php
 * Description  : Ajoute un nouveau food truck avec les nouvelles données provenant de l'application
 * Auteur       : Ottavio Buonomo
 * Date         : 06.06.2018
 * Version      : 1.0
 */

// require des différents script
require '../localisations/nouveau.php';
require '../horaire/nouveau.php';

static $request = "";

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
 * @param array $products       - Contact pour un FT (peut être NULL)
 * @return boolean $flagOK      - Témoin pour l'ajout du FT (true ou false)
 */
function creerFoodTruck($nom, $image, $lon, $lat, $heureDebut, $heureFin, $jourSemaine, $note, $proprietaire, $contact, $products) {
    $flagOk = true;
    
    if (empty($nom) || empty($lon) || empty($lat) || empty($heureDebut) || empty($heureFin) || empty($jourSemaine) || empty($image)) {
        return false;
    }
    if ($proprietaire === -1) {
        $proprietaire = NULL;
    }
    try {
        $truckExiste = verifierSiFoodTruckExiste($nom);
        $idLieu = creerLieu($lat, $lon);
        if ($idLieu !== FALSE) {
            if ($truckExiste === FALSE) {
                $idFoodTruck = ajouterFoodTruck($nom, $image, $contact, $proprietaire);
                if ($idFoodTruck !== FALSE) {
                    creerHoraire($idFoodTruck, $idLieu, $heureDebut, $heureFin, $jourSemaine);
                    if (!ajouterNote($idFoodTruck, $note)) {
                        $flagOk = FALSE;
                    }
                    $arrayOfIds = explode(";", $products);
                    for ($index = 0; $index < count($arrayOfIds); $index++) {
                        ajouterDesProduits($arrayOfIds[$index], $idFoodTruck);
                    }
                } else {
                    $flagOk = FALSE;
                }
            } else if ($truckExiste === "Erreur") {
                $flagOk = FALSE;
            } else {
                creerHoraire($truckExiste, $idLieu, $heureDebut, $heureFin, $jourSemaine);
                if (!ajouterNote($truckExiste, $note)) {
                    $flagOk = FALSE;
                }
                $arrayOfIds = explode(";", $products);
                for ($index = 0; $index < count($arrayOfIds); $index++) {
                    ajouterDesProduits($arrayOfIds[$index], $truckExiste);
                }
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
        $dbQuery->bindParam(':idProprietaire', $idProprietaire);
        $dbQuery->execute();
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
 * Ajoute des produits pour un food trcuk
 * @param int $idProduit    - identifiant du produit 
 * @param int $idFoodTruck  - identifiant du food trcuk
 */
function ajouterDesProduits($idProduit, $idFoodTruck) {
    try {
        $request = "INSERT INTO `TVEND`(`idFoodTruck`, `idProduit`) VALUES (:idFoodTruck, :idProduit)";
        $connect = getDB();
        $dbQuery = $connect->prepare($request);
        $dbQuery->bindParam(':idFoodTruck', $idFoodTruck, PDO::PARAM_INT);
        $dbQuery->bindParam(':idProduit', $idProduit, PDO::PARAM_INT);
        $dbQuery->execute();
    } catch (Exception $exc) {
        error_log($exc);
    }
}

/**
 * Vérifie si le food truck est déjà présent sur la base de données
 * @param string $nom           - Nom du FT
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
            return TRUE;
        } catch (Exception $exc) {
            return FALSE;
        }
    }
}

// Données provenant de l'application
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
$produitsFoodTruck = filter_input(INPUT_POST, 'idProduits', FILTER_SANITIZE_STRING);

// Ajout du food truck
if (creerFoodTruck($nomFoodTruck, $imageFoodTruck, $lonFoodTruck, $latFoodTruck, $heureDebutFoodTruck, $heureFinFoodTruck, $jourSemaineFoodTruck, $noteFoodTruck, $idProprietaireFoodTruck, $contactFoodTruck, $produitsFoodTruck)) {
    echo json_encode("Succès");
} else {
    header("HTTP/1.0 500 Intrnal Error");
}
