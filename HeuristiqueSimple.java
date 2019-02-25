
public class HeuristiqueSimple extends Heuristique {

	public HeuristiqueSimple(Urgence urgence) {
		super(urgence);
	}

	public double estimerCoutRestant(Etat etat, But but) {
		if (but.butEstStatisfait(etat)) {
			return 0;
		}
		double min = Double.MAX_VALUE;
		for (Emplacement e : etat.emplacementsPatients) {
			double delta = e.positionGeographique.distance(etat.emplacementAmbulance.positionGeographique);
			min = Math.min(min, delta);
		}
		min = Math.min(min, etat.emplacementHopital.positionGeographique.distance(etat.emplacementAmbulance.positionGeographique));
		return min;
	}
}
