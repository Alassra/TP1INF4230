/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2013 / TP1
 * 
 */

import java.util.Collection;
import java.util.LinkedList;


public class Etat implements Comparable<Etat> 
{

    // Référence sur la situation de l'urgence
    protected   Urgence           urgence;

    
    // Noyau de la représentation d'un état. Ici, on met tout ce qui rend l'état unique.
    /* Emplacement de l'ambulance. */
    protected Emplacement     emplacementAmbulance;
    /* Array indicant l'emplacement de chaque patient. */
    protected Emplacement     emplacementsPatients[];
    /* Array indicant l'état de chargement de chaque patient par l'ambulance. */
    protected boolean         patientsRecuperes[];
    /* Etat de chargement de l'ambulance */
    protected boolean            patientCharge = false;
    protected int 				patientTransporter;
    protected Emplacement 		emplacementHopital;
    
    // Variables pour l'algorithme A*.
    /* État précédent permettant d'atteindre cet état. */
    protected Etat            parent;
    /* Action à partir de parent permettant d'atteindre cet état. */
    protected String          actionFromParent;
    /* f=g+h. */
    protected double          f;
    /* Meilleur coût trouvé pour atteindre cet état à partir de l'état initial. */    
    protected double          g;
    /* Estimation du coût restant pour atteindre le but. */
    protected double          h;

    
    public Etat(Urgence urgence){
        this.urgence = urgence;
    }

    /** Fonction retournant les états successeurs à partir de cet état.
     *  Aussi appelé fonction de transition.
     *  Cela permet d'explorer l'espace d'état (le graphe de recherche).
     */
    
    

    public Collection<Successeur> genererSuccesseurs()
    {
        LinkedList<Successeur> successeurs = new LinkedList<Successeur>();

        // À compléter.
        //
        //   - Les actions possibles sont : 
        //   ----> emprunter une route de l'emplacement courant pour aller sur un emplacement voisin,
        //   ----> charger un patient lorsque l'ambulance : 1) est vide et 2) se trouve sur un emplacement de client _pas encore transporté_
        //   ----> décharger un patient 
        
        //   - Pour toute action possible 
        //   --- Instancier un objet Successeur s;
        //   --- Cloner l'état courant dans la variable état du successeur (s.etat = clone()).
        //   --- Créer la chaîne de caractère représentant l'action dans s.action (voir plans fournis).
        //   -----> ex. d'un déplacement à l'ouest  "Ouest = Lieu " +  route.origine.nom + " -> Lieu " + route.destination.nom + ")"       
        //   --- Calculer le coût de cette action dans s.cout.
        //   -----> ex. pour un déplacement, le cout est 1 + le cout de l'emplacement
        //   --- Modifier la valeur des variables appropriées dans s.etat pour refléter l'effet de l'action (qu'est-ce qui change?) 
        //   --- Ajouter s dans la liste successeurs.
                
        for (int i=0;emplacementsPatients.length > i ; i++) {
        	if (!patientCharge && !patientsRecuperes[i] &&
        			emplacementsPatients[i].compareTo(emplacementAmbulance)  == 0 ) {
        		Successeur successeur = new Successeur();
        		successeur.etat = clone();
        		successeur.action = "Charger()";
        		successeur.etat.patientsRecuperes[i] = true;
        		successeur.cout = urgence.dureeChargement;
        		successeur.etat.patientCharge = true;
        		successeur.etat.parent = this;
        		successeur.etat.actionFromParent = successeur.action;
        		successeur.etat.patientTransporter = i;
        		successeurs.add(successeur);
        	}
        }
        if (patientCharge && emplacementAmbulance.type.equalsIgnoreCase("H")) {
        	Successeur successeur = new Successeur();
    		successeur.etat = clone();
    		successeur.action = "Decharger()";
    		successeur.cout = urgence.dureeDechargement;
    		successeur.etat.patientCharge = false;
    		successeur.etat.parent = this;
    		successeur.etat.actionFromParent = successeur.action;
    		successeurs.add(successeur);
        }
        for (Route route : emplacementAmbulance.routes) {
        	Successeur successeur = new Successeur();
    		successeur.etat = clone();
    		successeur.etat.emplacementAmbulance = route.destination;
    		successeur.etat.parent = this;
    		if (successeur.etat.patientCharge) {
    			successeur.etat.emplacementsPatients[successeur.etat.patientTransporter] = route.destination;
    		}
    		if (route.destination.type.equalsIgnoreCase("#")) {
    			successeur.cout = 2;
    		} else {
    			successeur.cout = 3;
    		}
    		if (route.origine.positionGeographique.getX() == route.destination.positionGeographique.getX()+1) {
    			successeur.action = "Nord = Lieu " + route.origine.nom + " -> Lieu " + route.destination.nom + ")";
    		}
    		if (route.origine.positionGeographique.getX() == route.destination.positionGeographique.getX()-1) {
    			successeur.action = "Sud = Lieu " + route.origine.nom + " -> Lieu " + route.destination.nom + ")";
    		}
    		if (route.origine.positionGeographique.getY() == route.destination.positionGeographique.getY()+1) {
    			successeur.action = "Ouest = Lieu " + route.origine.nom + " -> Lieu " + route.destination.nom + ")";
    		}
    		if (route.origine.positionGeographique.getY() == route.destination.positionGeographique.getY()-1) {
    			successeur.action = "est = Lieu " + route.origine.nom + " -> Lieu " + route.destination.nom + ")";
    		}
    		
    		successeur.etat.actionFromParent = successeur.action;
    		successeurs.add(successeur);
        }

        return successeurs;
    }


    /* Crée un nouvel État en clonant le contenu pertinent de l'état actuel */
    @Override
    public Etat  clone()
    {
        Etat etat2 = new Etat(urgence);
        etat2.patientCharge = patientCharge;
        etat2.emplacementAmbulance = emplacementAmbulance;
        etat2.emplacementsPatients = new Emplacement[emplacementsPatients.length];
        for(int i=0;i<emplacementsPatients.length;i++)
            etat2.emplacementsPatients[i] = emplacementsPatients[i];

        etat2.patientsRecuperes = new boolean[patientsRecuperes.length];
        for(int i=0;i<patientsRecuperes.length;i++)
            etat2.patientsRecuperes[i] = patientsRecuperes[i];
        etat2.emplacementHopital = emplacementHopital;
        return etat2;
    }

    /* Relation d'ordre nécessaire pour le TreeSet checkOpen . */
    @Override
    public int compareTo(Etat o) {
        int c;
        c = this.emplacementAmbulance.compareTo(o.emplacementAmbulance);
        if(c!=0) return c;

        if(patientCharge == o.patientCharge)
        	c=0;
        else if(patientCharge == true )
        	return 1;
        else 
        	return -1;
       
        for(int i=0;i<emplacementsPatients.length;i++){
            c = (patientsRecuperes[i]?1:0) - (o.patientsRecuperes[i]?1:0);
            if(c!=0) return c;
            if(!patientsRecuperes[i]){
                c = emplacementsPatients[i].compareTo(o.emplacementsPatients[i]);
                if(c!=0) return c;
            }
        }
        return 0;
    }

    @Override
    public String toString(){
        String s = "ETAT: f=" + f + "  g=" + g + "\n";
        s += "  Pos=" + emplacementAmbulance.nom + "";
        for(int i=0;i<emplacementsPatients.length;i++){
            s += "\n  PosColis[i]=";
            s += emplacementsPatients[i]==null ? "--" : emplacementsPatients[i].nom;
        }
        s += "\n";
        return s;
    }
    

}
