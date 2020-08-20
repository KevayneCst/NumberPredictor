package org.core.maths;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Classe représentant une expression mathématique (par exemple <i>3x + 2</i>)
 * et apportant différentes fonctionnalitées et opérations possibles sur cette
 * expression (comme par exemple calculer l'expression en remplaçant les
 * inconnus par un nombre donné en entré).
 * 
 * @author Kévin Constantin
 *
 */
public class Expression {

	private static final String SQUARE_ROOT = "sqrt";
	private static final String PI = "pi";
	private static final char EXPONENTIAL = 'e';
	private static final char SQUARE = '²';
	private static final char PLUS = '+';
	private static final char MINUS = '-';
	private static final char MULTIPLY = '*';
	private static final char DIVIDE = '/';

	private String inputExpression;

	public Expression(String expression) {
		if (assertParenthesesFormed(expression)) {
			this.inputExpression = expression;
		} else {
			throw new UnsupportedOperationException("Les parenthèses ne sont pas fermés ou commencent par une fermée");
		}
	}

	/**
	 * Découpe la chaîne de caractère passée en paramètre et va renvoyer le contenu
	 * de celle-ci. Dans le cas où il n'y aurait pas de parenthèses, on renvoie la
	 * chaîne en paramètre initial
	 * 
	 * @param target
	 * @return
	 */
	private String cutFirstParenthesis(String target) {
		int indexStartParenthesis = target.indexOf("(");
		if (indexStartParenthesis != -1) {
			int indexEndParenthesis = findIndexClosingParenthesisOfFirstParentheses(target);
			String parenthesesContent = target.substring(indexStartParenthesis + 1, indexEndParenthesis);
			if (parenthesesContent.isBlank()) {
				return "";
			} else {
				return parenthesesContent;
			}
		}
		return target;
	}

	/**
	 * Renvoie l'indice de la parenthèse fermante qui ferme les parenthèses de la
	 * première parenthèse ouverte.
	 * 
	 * Exemples :
	 * <ul>
	 * <li>(hello(no)) => 10</li>
	 * <li>(good(morning?)night) => 20</li>
	 * <li>(hi how are you)(not fine) => 15</li>
	 * </ul>
	 * 
	 * Si la parenthèse fermante n'est pas trouvé, renvoie -1, mais ce cas ne
	 * devrait jamais se présenter (on s'en est assuré à l'initialisation)
	 * 
	 * @param target
	 * @return
	 */
	private int findIndexClosingParenthesisOfFirstParentheses(String target) {
		Deque<Boolean> stack = new ArrayDeque<>();
		int lengthTarget = target.length();
		for (int i = 0; i < lengthTarget; i++) {
			if (target.charAt(i) == '(') {
				stack.push(true);
			} else if (target.charAt(i) == ')') {
				stack.pop();
				if (stack.isEmpty()) {
					return i;
				}
			}
		}
		return -1;
	}

	private double genericSimpleComputation(String target, char operator) {
		String[] expressions = target.split("\\" + operator);
		if (expressions.length != 2) {
			throw new IllegalArgumentException("Trop de signes " + operator);
		}
		double operator1 = Double.parseDouble(expressions[0]);
		double operator2 = Double.parseDouble(expressions[1]);
		switch (operator) {
		case MULTIPLY:
			return operator1 * operator2;
		case DIVIDE:
			return operator1 / operator2;
		case PLUS:
			return operator1 + operator2;
		case MINUS:
			return operator1 - operator2;
		default:
			throw new UnknownError("Operateur inconnu : " + operator);
		}
	}

	private String replaceXs(String target, String replacement) {
		if (assertNoMathematicalUnknow(replacement)) {
			return target.replace("x", replacement);
		}
		throw new UnsupportedOperationException(
				"La chaîne de remplacement contient des inconnus, veuillez les supprimer");
	}

	private boolean assertParenthesesFormed(String target) {
		int openParentheses = 0;
		int closeParentheses = 0;
		int lengthTarget = target.length();
		for (int i = 0; i < lengthTarget; i++) {
			switch (target.charAt(i)) {
			case ')':
				if (i == 0 || openParentheses == closeParentheses) {
					return false;
				} else {
					closeParentheses++;
				}
				break;
			case '(':
				openParentheses++;
				break;
			default:
				continue;
			}
		}
		return openParentheses == closeParentheses;
	}

	private boolean assertNoMathematicalUnknow(String target) {
		List<Integer> ignoredIndexs = listOfMathemicalExpressionIndexs(target);
		int lengthString = target.length();
		for (int i = 0; i < lengthString; i++) {
			if (!ignoredIndexs.contains(i) && (target.charAt(i) + "").matches("[a-zA-Z]+")) {
				return false;
			}
		}
		return true;
	}

	private List<Integer> listOfMathemicalExpressionIndexs(String target) {
		List<Integer> allIndexs = new ArrayList<>();
		allIndexs.addAll(findIndexsOfE(target));
		allIndexs.addAll(findIndexsOfPI(target));
		allIndexs.addAll(findIndexsOfSqrt(target));
		return allIndexs;
	}

	private List<Integer> findIndexsOfSqrt(String target) {
		return findIndexsOfOccurences(target, SQUARE_ROOT);
	}

	private List<Integer> findIndexsOfPI(String target) {
		return findIndexsOfOccurences(target, PI);
	}

	private List<Integer> findIndexsOfE(String target) {
		return findIndexsOfOccurences(target, EXPONENTIAL + "");
	}

	private List<Integer> findIndexsOfOccurences(String target, String occurence) {
		List<Integer> indexs = new ArrayList<>();
		int index = target.indexOf(occurence);
		while (index >= 0) {
			int lengthOccurence = occurence.length();
			for (int i = 0; i < lengthOccurence; i++) {
				indexs.add(index + i);
			}
			index = target.indexOf(occurence, index + 1);
		}
		return indexs;
	}

	public String getInputExpression() {
		return inputExpression;
	}

	public static void main(String[] args) {
		Expression e = new Expression("e + sqrt sqresqrttes");
		System.out.println(e.findIndexsOfSqrt(e.getInputExpression()));
		System.out.println(e.findIndexsOfE(e.getInputExpression()));
		System.out.println(e.assertParenthesesFormed(e.getInputExpression()));
		System.out.println(e.assertParenthesesFormed("(bonjour (yolo))"));
		System.out.println(e.assertParenthesesFormed("(bo(njour (yolo))"));
		System.out.println(e.assertParenthesesFormed(")(bonjour (yolo))"));
		System.out.println(e.assertParenthesesFormed("(bonjour (yolo)))"));
		System.out.println(e.assertParenthesesFormed("o)(bonjour (y(olo))"));
		System.out.println("\n" + e.assertNoMathematicalUnknow("sqrt x e pi"));
		System.out.println(e.genericSimpleComputation("9 - 3", '-'));
		System.out.println(e.genericSimpleComputation("4+3", '+'));
		System.out.println(e.cutFirstParenthesis("(4+2)(2+9)"));
		System.out.println(e.cutFirstParenthesis("()(2+9)"));
		System.out.println(e.cutFirstParenthesis("(       )(2+9)"));
		System.out.println(e.cutFirstParenthesis("(4+2(2+9))"));
		System.out.println(e.findIndexClosingParenthesisOfFirstParentheses("(hello(no))") + " => 10");
		System.out.println(e.findIndexClosingParenthesisOfFirstParentheses("(good(morning?)night)") + " => 20");
		System.out.println(e.findIndexClosingParenthesisOfFirstParentheses("(hi how are you)(not fine)") + " => 15");
	}
}
