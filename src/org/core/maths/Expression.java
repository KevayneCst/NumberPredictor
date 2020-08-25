package org.core.maths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Classe représentant une expression mathématique. Voici la liste des fonctions
 * mathématiques actuellements prisent en compte :<br>
 * <i>Pour ce qui va suivre, X est a remplacer par votre valeur (ça peut être un
 * chiffre ou un autre calcul)<br>
 * Et pour les fonctions mathématiques et constantes, veillez à avoir un espace
 * avant et après (sauf si ce ne sont pas des lettres)</i>
 * <ul>
 * <li>Exponentiel => écrire <b>e</b></li>
 * <li>Pi => écrire <b>pi</b></li>
 * <li>Racine carré => écrire <b>sqrt(X)</b></li>
 * <li>Log => écrire <b>log(X)</b></li>
 * <li>Logarithme népérien => écrire <b>ln(X)</b></li>
 * <li>Sinus => écrire <b>sin(X)</b></li>
 * <li>Cosinus => écrire <b>cos(X)</b></li>
 * <li>Tangente => écrire <b>tan(X)</b></li>
 * <li>Puissance => écrire <b>X^X</b></li>
 * <li>Diviser => écrire <b>/</b></li>
 * <li>Multiplier => écrire <b>*</b></li>
 * <li>Addition => écrire <b>+</b></li>
 * <li>Soustraction => écrire <b>-</b></li>
 * </ul>
 * 
 * @author Kévin Constantin
 *
 */
public class Expression {

	private static final String SQUARE_ROOT = "sqrt";
	private static final String EXPONENTIAL = "e";
	private static final String LOG = "log";
	private static final String LN = "ln";
	private static final String SIN = "sin";
	private static final String COS = "cos";
	private static final String TAN = "tan";
	private static final String PI = "pi";
	private static final char POWER = '^';
	private static final char PLUS = '+';
	private static final char MINUS = '-';
	private static final char MULTIPLY = '*';
	private static final char DIVIDE = '/';
	private static final char SPACE = ' ';

	private String inputExpression;

	public Expression(String expression) {
		this.inputExpression = expression;
	}

	/**
	 * Effectue l'évaluation de l'expression. Ce code comprend une grande partie du
	 * code trouvable ici (domaine libre)
	 * <a><u>https://stackoverflow.com/a/26227947</u></a>. J'ai modifié quelques
	 * parties du code et je vais rajouter progressivement (comme c'est déjà le cas)
	 * de nouvelles fonctionnalitées (exemple; log, ln, pi, et exponential n'étaient
	 * pas pris en compte avant)...
	 * 
	 * @return
	 */
	public double eval() {
		return new Object() {
			String str = inputExpression;
			int pos = -1, ch;

			/**
			 * Remplace les constantes mathématiques de la chaîne par leurs valeurs
			 * numériques.
			 */
			void replaceFinalValues() {
				str = str.replaceAll("\\b" + PI + "\\b", String.valueOf(Math.PI));
				str = str.replaceAll("\\b" + EXPONENTIAL + "\\b", String.valueOf(Math.E));
			}

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			/**
			 * Vérifie que le prochain caractère rencontré dans la chaîne est celui passé en
			 * paramètre (ignore les espaces)
			 * 
			 * @param charToEat
			 * @return
			 */
			boolean eat(int charToEat) {
				while (ch == SPACE)
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				replaceFinalValues();
				nextChar();
				double x = parseExpression();
				if (pos < str.length())
					throw new RuntimeException(
							"Charactère inconnu: " + (char) ch + " arrêté sur: " + str.substring(0, pos));
				return x;
			}

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+'))
						x += parseTerm();
					else if (eat('-'))
						x -= parseTerm();
					else
						return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat(MULTIPLY))
						x *= parseFactor();
					else if (eat(DIVIDE))
						x /= parseFactor();
					else
						return x;
				}
			}

			double parseFactor() {
				if (eat(PLUS))
					return parseFactor();
				if (eat(MINUS))
					return -parseFactor();

				double x;
				int startPos = this.pos;
				if (eat('(')) {
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') {
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') {
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals(SQUARE_ROOT))
						x = Math.sqrt(x);
					else if (func.equals(SIN))
						x = Math.sin(Math.toRadians(x));
					else if (func.equals(COS))
						x = Math.cos(Math.toRadians(x));
					else if (func.equals(TAN))
						x = Math.tan(Math.toRadians(x));
					else if (func.equals(LN))
						x = Math.log(x);
					else if (func.equals(LOG))
						x = Math.log10(x);
					else
						throw new RuntimeException("Fonction mathématique inconnue: " + func);
				} else {
					throw new RuntimeException(
							"Charactère inconnu: " + (char) ch + " arrêté sur: " + str.substring(0, pos));
				}

				if (eat(POWER))
					x = Math.pow(x, parseFactor());

				return x;
			}
		}.parse();
	}

	public boolean matches(List<Double> numberSequence) {
		int lengthNumberSequence = numberSequence.size();
		if (numberSequence.size() < 2) throw new IllegalArgumentException("La suite de nombre est trop peu fournis pour produire un résultat, mettez au moins 2 chiffres actuellement il y en a "+lengthNumberSequence);
		double supposedNextValue = computeReplaceUnknows(numberSequence.get(0));
		for (int i = 1; i < lengthNumberSequence; i++) {
			double currentNumberSequenceValue = numberSequence.get(i);
			if (supposedNextValue == currentNumberSequenceValue) {
				supposedNextValue = computeReplaceUnknows(currentNumberSequenceValue);
			} else {
				return false;
			}
		}
		return true;
	}

	public double computeReplaceUnknows(Double replacement) {
		return new Expression(inputExpression.replaceAll("x", String.valueOf(replacement))).eval();
	}

	public String getInputExpression() {
		return inputExpression;
	}

	public static void main(String[] args) {
		Expression f = new Expression("(1+9*7/(7*5/9))");
		System.out.println(f.eval());
		Expression fe = new Expression("( 1 + 9 * 856 / ( 7 * 5 / 78))");
		System.out.println(fe.eval());
		String s = "3 + 9 * log(10) - 9 * pi";
		Expression fez = new Expression(s);
		System.out.println(fez.eval());
		Expression yes = new Expression("1 + x - 5");
		List<Double> list = Arrays.asList(1., -3., -7., -11.);
		System.out.println("Est-ce "+yes.getInputExpression()+" suit la suite suivante : "+list+" : "+yes.matches(list));
	}
}
