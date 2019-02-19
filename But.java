/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2019 / TP1
 * 
 */

public class But {

    /* Array contenant la destination des patients. Dans le même ordre que dans la classe Urgence */
    protected Emplacement[]   destinationsPatients;

    /* Retourne vrai si et seulement si le but est satisfait dans l'état passé en paramètre.
     * Le but ici est : tous les patients ont été amenés à l'hopital */
    public boolean butEstStatisfait(Etat etat)
    {

        for(int i=0;i<destinationsPatients.length;i++)
            if(etat.emplacementsPatients[i] != destinationsPatients[i])
                return false;
        
        if (!etat.patientCharge) {
        	return true;
        } else {
        	return false;
        }
    }

}
