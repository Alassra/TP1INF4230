/*
 * INF4230 - Intelligence artificielle
 * UQAM / Département d'informatique
 * 
 * Hiver 2017 / TP1
 * 
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.List;

/**
 * Point d'entrée du programme.
 */
public class TP1 {

	// DECLARATION DU PLAN
	public static void main(String args[]) throws IOException {
		
		
		ParseurH parseur = new ParseurH();
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		String line = ""; 
		System.out.println("Donne le nom du fichier contenant ton mondeH.");
		System.out.println("Remarque: le fichier doit être à la racine du projet et dois se terminer par .txt");
		System.out.println("Exemple: urgenceH01.txt");
		
		// Recuperation du fichier map et parseur du fichier
		try {
			line = keyboard.readLine();
		} catch (IOException e) {
			System.err.println("err");
		}
		parseur.parse(line);

		// Création de l'objet évaluateur d'heuristique
		Heuristique h = new Heuristique(parseur.urgence);
		System.out.println("distance_heuristique(depart,arrivee) = " + h.estimerCoutRestant(parseur.etatInitial, parseur.but));

		/* Appel à l'algorithme A* : enregistrement du plan dans la List
		 * 
		 * À la fin de l'appel plan doit intégrer une liste de String donnant
		 * la suite d'instructions pour effectuer tous les deplacements selon
		 * le format donné par la chaine de caratères (cf. classe Route)
		 * "Ouest = Lieu " +  route.origine.nom + " -> Lieu " + route.destination.nom + ")"
		 * 
		 * la strategie à effectuer est : 
		 * Partir de l'entrée (emplacement actuel de l'ambulance)
		 * aller au premier patient
		 * ramener le premier patient à l'hopital
		 * aller chercher le second patient
		 * le ramener à l'hopital etc...
		 *
		 */
		List<String> plan = AStar.genererPlan(parseur.etatInitial, parseur.but, h);

		// Écriture du plan dans le fichier de sortie + affichage console
		File f = new File("planH" + line.substring(8, 10) + ".txt");
		try {
			FileWriter fw = new FileWriter(f);
			System.out.println("Plan {");
			fw.write("Plan  { \n");
			fw.flush();
			for (String action : plan) {
				System.out.println(action + ";");
				fw.write(action + "; \n");
				fw.flush();
			}
			System.out.println("}");
			fw.write("}");
			fw.close();
		} catch (IOException e) {
			System.out.println("err");
		}

	}

}
