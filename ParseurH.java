
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

public class ParseurH {
	protected Urgence urgence;
	protected Etat etatInitial;
	protected But but;

	/*******************************************************
	 * Cette fonction parse le fichier de map determinant 
	 * - la position initiale de l'ambulance (A)
	 * - la position de l'hopital (H)
	 * - la position des individus-patients (I)
	 *******************************************************/

	public void parse(String nomFichier) throws IOException {
		
		//Ouverture du fichier
		FileInputStream in = null;
		try {
			File inputFile = new File(nomFichier);
			in = new FileInputStream(inputFile);
		} catch (Exception e) {
			return;
		}
		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		
		/*Lecture du fichier*/
		
		//recupération des deux premieres lignes contenant
		//1) le nombre de lignes de la map
		//2) le nombre de colonnes de la map
		String input;
		input = bin.readLine();
		int nbLignes = Integer.parseInt(input);
		input = bin.readLine();
		int nbColonnes = Integer.parseInt(input);
		
		//initialisation des variables
		String nomCol = "";
		urgence = new Urgence();
		etatInitial = new Etat(urgence);
		Vector<String> positionsPatients = new Vector<>();
		Map<String, Emplacement> destinations = new TreeMap<>();
		
		
		
		// création des emplacements
		for (int i = 0; i < nbLignes; i++) {
			input = bin.readLine();
			for (int j = 0; j < nbColonnes; j++) {
				
				/*input.charAt(j) est le caractere lu 
				* si ' ' ne rien faire (ca sera une route bloquée)
				* si 'A' c'est la position initiale de l'ambulance
				* si 'H' c'est la position de l'hopital
				* si 'I' c'est la position d'un patient
				* si '#' c'est une zone à traffic normal
				* si '-' c'est une zone d'embouteillage
				* aucun autre caractere n'est accepté
				*/
				
				
				if(input.charAt(j)!=' '
						&& input.charAt(j)!='A'
						&& input.charAt(j)!='H'
						&& input.charAt(j)!='I'
						&& input.charAt(j)!='#'
						&& input.charAt(j)!='-') {
					System.err.println("FICHIER MAL FORMÉ");
					System.exit(-1);
				}
					
				if (input.charAt(j) != ' ') {
					//emplacement créé
					String name = i + "-" + j;
					Emplacement location = new Emplacement(name, i, j, "" + input.charAt(j));
					urgence.emplacements.put(name, location);
					
					//les cas particuliers 
					if (input.charAt(j) == 'A')
						etatInitial.emplacementAmbulance = urgence.emplacements.get(name);
					if (input.charAt(j) == 'I') {
						positionsPatients.add(name);
					}
					if (input.charAt(j) == 'H') {
						Emplacement e = urgence.emplacements.get(name);
						destinations.put(name, e);
						nomCol = name;
						etatInitial.emplacementHopital = location;
					}
				}
			}
		}
		
        //Pour chaque emplacement on teste l'existence d'emplacements adjacents
		// si un adjacent existe, on rajoute une route qui va de l'emplacement
		// vers l'adjacent.
		for (int i = 0; i < nbLignes; i++) {
			for (int j = 0; j < nbColonnes; j++) {
				//On teste l'existence de l'emplacement position "i-j"
				if (urgence.emplacements.get(i + "-" + j) != null) {
					Emplacement l1 = urgence.emplacements.get(i + "-" + j);
					
					//on crée les routes existantes (4 directions a tester)
					if (urgence.emplacements.get(i + "-" + (j - 1)) != null) {
						Emplacement l2 = urgence.emplacements.get(i + "-" + (j - 1));
						l1.routes.add(new Route(l1, l2));
					}
					if (urgence.emplacements.get((i - 1) + "-" + j) != null) {
						Emplacement l2 = urgence.emplacements.get((i - 1) + "-" + j);
						l1.routes.add(new Route(l1, l2));
					}
					if (urgence.emplacements.get(i + "-" + (j + 1)) != null) {
						Emplacement l2 = urgence.emplacements.get(i + "-" + (j + 1));
						l1.routes.add(new Route(l1, l2));
					}
					if (urgence.emplacements.get((i + 1) + "-" + j) != null) {
						Emplacement l2 = urgence.emplacements.get((i + 1) + "-" + j);
						l1.routes.add(new Route(l1, l2));
					}
				}
			}
		}
		int nbPatients = positionsPatients.size();
		
		//creation d'un masque qui indique si les patients ont été récupérés
		//et on transfert les emplacements des patients à l'etat initial
		etatInitial.patientsRecuperes = new boolean[nbPatients];
		etatInitial.emplacementsPatients = new Emplacement[nbPatients];
		for (int i = 0; i < nbPatients; i++) 

				etatInitial.emplacementsPatients[i] = 
						urgence.emplacements.get(positionsPatients.get(i));
		
			
		
		
		//generation des buts, but identiques tous egaux a l'hopital... super interessant
		but = new But();
		but.destinationsPatients = new Emplacement[nbPatients];
		for (int i = 0; i < nbPatients; i++)
			but.destinationsPatients[i] = destinations.get(nomCol);

	}

}
