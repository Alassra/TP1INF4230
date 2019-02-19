/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2019 / TP1
 * 
 */

public class Heuristique {

    protected Urgence urgence;

    public Heuristique(Urgence urgence){
        this.urgence = urgence;
    }

    /** Estime et retourne le coût restant pour atteindre le but à partir de état.
     *  Attention : pour être admissible, cette fonction heuristique ne doit pas
     *  surestimer le coût restant.
     */
    public double estimerCoutRestant(Etat etat, But but){

    	// À Compléter.
    	// -- Vous devez réflechir à deux façons d'estimer la distance entre l'état courant et l'état but
    	// -- Pensez en termes de cout des déplacements de l'ambulance (vers le prochain patient et vers l'hopital) 
    	// ainsi que de chargement/déchargement des patients
    	// -- Par exemple, pour calculer une distance, vous pouvez utiliser les positions géographiques des emplacements
    	// -- Commencez avec une version plus simple, puis raffinez vos estimations tout en veillant de ne pas surestimer le cout

        return 0;
    }
    
    public double estimerCoutRestantManhattan(Etat etat, But but) {
    	
    	double distanceHeuristique = 0;
    	
    	distanceHeuristique += etat.emplacementHopital.positionGeographique.distance(etat.emplacementAmbulance.positionGeographique);
    	
    	for (Emplacement emplacement : etat.emplacementsPatients) {
    		 distanceHeuristique += emplacement.positionGeographique.distance(etat.emplacementAmbulance.positionGeographique);
    	}
    	
    	distanceHeuristique = distanceHeuristique/(etat.emplacementsPatients.length + 1);
    	
    	return distanceHeuristique;
    }

}
