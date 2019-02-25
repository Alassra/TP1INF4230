
public class HeuristiqueManhattan extends Heuristique {

	public HeuristiqueManhattan(Urgence urgence) {
		super(urgence);
	}


	public double estimerCoutRestant(Etat etat, But but) {
		if (but.butEstStatisfait(etat)) {
			return 0;
		}
		double min = Double.MAX_VALUE;
		for (Emplacement e : etat.emplacementsPatients) {
			double deltaY = Math.abs(e.positionGeographique.getY()-etat.emplacementAmbulance.positionGeographique.getY());
			double deltaX = Math.abs(e.positionGeographique.getX()-etat.emplacementAmbulance.positionGeographique.getX());
			min = Math.min(min, deltaY+deltaX);
		}
		double deltaYHopital = Math.abs(etat.emplacementHopital.positionGeographique.getY()-etat.emplacementAmbulance.positionGeographique.getY());
		double deltaXHopital = Math.abs(etat.emplacementHopital.positionGeographique.getX()-etat.emplacementAmbulance.positionGeographique.getX());
		min = Math.min(min, deltaYHopital+deltaXHopital);
		return min;
	}
	
}
