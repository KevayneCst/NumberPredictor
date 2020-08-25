package org.core.predictation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.core.maths.Expression;

public class Predictor {

	private Matcher matcheur;
	private List<Double> numberSequence;

	public Predictor(Double... numberSequence) {
		this.matcheur = new Matcher();
		this.numberSequence = Arrays.asList(numberSequence);
		this.numberSequence = new ArrayList<>(this.numberSequence);
	}

	private Double predictNextNumber(boolean displaySequence) {
		Expression predicted = matcheur.tryAll(numberSequence);
		if (predicted == null) {
			return null;
		} else {
			if (displaySequence) {
				System.out.println("La suite de nombre suit la suite logique suivante : "+predicted.getInputExpression());
			}
			return predicted.computeReplaceUnknows((numberSequence.get(numberSequence.size() - 1)));
		}
	}

	public void iteratePrediction(int iteration) {
		if (iteration < 1)
			System.err.println("Veuillez entrer un nombre n'itérations supérieur à 0");
		for (int i = 0; i < iteration; i++) {
			Double res = predictNextNumber(i==0);
			if (res == null) {
				System.out.println("Aucune suite logique trouvé pour : " + numberSequence);
				return;
			} else {
				numberSequence.add(res);
			}
		}
		System.out.println("État de la suite après "+iteration+ " itération(s) :\n"+numberSequence);
	}
	
	public static void main(String[] args) {
		Predictor p = new Predictor(1., 2., 3., 5., 8.);
		p.iteratePrediction(4);
	}
}
