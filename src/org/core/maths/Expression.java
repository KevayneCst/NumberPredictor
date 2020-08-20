package org.core.maths;

import java.util.ArrayList;
import java.util.Collections;
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

	public double computeExpression(double d) {
		int lengthInputExpression = inputExpression.length();
		for (int i=0; i<lengthInputExpression; i++) {
			
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
		throw new UnsupportedOperationException("La chaîne de remplacement contient des inconnus, veuillez les supprimer");
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
			if (!ignoredIndexs.contains(i) && (target.charAt(i)+"").matches("[a-zA-Z]+")) {
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
		System.out.println("\n"+e.assertNoMathematicalUnknow("sqrt x e pi"));
		System.out.println(e.genericSimpleComputation("9 - 3", '-'));
		System.out.println(e.genericSimpleComputation("4+3", '+'));
	}
}
