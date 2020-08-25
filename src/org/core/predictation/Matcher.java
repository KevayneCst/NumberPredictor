package org.core.predictation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.core.maths.Expression;

public class Matcher {

	private List<Expression> testedExpression;
	private static final String PATH_EXTENTION_FILE = "expressions.txt";

	public Matcher() {
		this.testedExpression = new ArrayList<>();
		readExpressionsFile();
	}

	private void readExpressionsFile() {
		File file = new File(PATH_EXTENTION_FILE);
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitedLine = line.split(";");
				testedExpression.add(new Expression(splitedLine[0].trim(), parseVars(splitedLine[1].trim())));
			}
		} catch (Exception e) {
			System.out.println("Erreur lors de la lecture du fichier: \"" + PATH_EXTENTION_FILE + "\":");
			e.printStackTrace();
		}
	}

	private List<String> parseVars(String target) {
		String[] splitedVars = target.split(",");
		return Arrays.asList(splitedVars);
	}

	/**
	 * Test toutes les expressions récupérées dans le fichier des expressions à
	 * tester et regarde si l'une d'elle correspond à la suite de nombre passée en
	 * paramètre. Si aucune des expressions ne correspond, renvoie <b>null</b> sinon
	 * l'expression qui correpond.
	 * 
	 * @param sequenceNumber
	 * @return
	 */
	public Expression tryAll(List<Double> numberSequence) {
		for (Expression currentExpression : testedExpression) {
			if (currentExpression.matches(numberSequence)) {
				return currentExpression;
			}
		}
		return null;
	}
}
