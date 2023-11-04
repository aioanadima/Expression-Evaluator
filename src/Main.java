import java.util.Scanner;
import java.util.Deque;
import java.util.LinkedList;

class PostfixEvaluator {

	private String expression;
	private String postfix;
	private final Deque<Character> operatorStack;

	public PostfixEvaluator(String expression) {
		this.expression = expression.trim().replaceAll("\\s+", "");
		this.operatorStack = new LinkedList<>();

		generateNotation();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
		generateNotation();
	}

	private static int getPrecedence(char operator) {
		switch (operator) {
		case '+':
		case '-':
			return 11;
		case '*':
		case '/':
			return 12;
		case '^':
			return 13;
		default:
			return -1;
		}
	}

	private static int getAssociativity(char operator) {
		if (operator == '^') {
			return 1;
		}
		return 0;
	}

	private void generateNotation() {
		StringBuilder postfixForm = new StringBuilder();
		char[] chars = expression.toCharArray();

		for (char c : chars) {
			if (Character.isDigit(c)) {
				postfixForm.append(c);
			} else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
				while (!this.operatorStack.isEmpty() && this.operatorStack.peek() != '('
						&& (getPrecedence(c) < getPrecedence(this.operatorStack.peek())
								|| getPrecedence(c) == getPrecedence(this.operatorStack.peek())
										&& getAssociativity(this.operatorStack.peek()) != 1)) {
					postfixForm.append(this.operatorStack.pop());
				}
				this.operatorStack.addFirst(c);
			} else if (c == '(') {
				this.operatorStack.addFirst(c);
			} else if (c == ')') {
				while (this.operatorStack.peek() != '(') {
					postfixForm.append(this.operatorStack.pop());
					if (this.operatorStack.isEmpty()) {
						System.err.println("[ERROR] open parenthesis has not been found");
						break;
					}
				}
				if (this.operatorStack.peek() == '(') {
					this.operatorStack.pop();
				}
			}
		}

		while (!this.operatorStack.isEmpty()) {
			if (this.operatorStack.peek() == '(') {
				System.err.println("[ERROR] initial expression had unmatching parentheses");
			}
			postfixForm.append(this.operatorStack.pop());
		}
		this.postfix = postfixForm.toString();
	}

	public static int evaluateNotation(String postfix) {
		Deque<Integer> st = new LinkedList<>();
		for (Character s : postfix.replaceAll("\\s", "").toCharArray()) {
			if (Character.isDigit(s)) {
				st.push(Integer.valueOf(s) - 48);
			} else {
				int o1 = 'a';
				int o2 = 'a';

				if (st.peek() != null) {
					o1 = st.pop();
					if (st.peek() != null) {
						o2 = st.pop();
					} else {
						System.out.println("Incorrect expression");
					}
				}

				switch (s) {
				case '+':
					st.push(o2 + o1);
					break;
				case '-':
					st.push(o2 - o1);
					break;
				case '*':
					st.push(o2 * o1);
					break;
				case '/':
					st.push(o2 / o1);
					break;
				case '^':
					st.push((int) Math.pow(o2, o1));
					break;
				default:
				}
			}
		}
		return st.peek();
	}

	public String getPostfix() {
		return postfix;
	}
}

public class Main {
	public static final int POSTFIX_GENERATE = 1;
	public static final int POSTFIX_EVAL = 2;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int exercise = scanner.nextInt();
		scanner.nextLine();
		String expression = scanner.nextLine();

		switch (exercise) {
		case POSTFIX_GENERATE:
			PostfixEvaluator e = new PostfixEvaluator(expression);
			System.out.println(e.getPostfix());
			break;

		case POSTFIX_EVAL:
			System.out.println(PostfixEvaluator.evaluateNotation(expression));
			break;

		// 3+(2+1)*2^3^2-8/(5-1*2/2)
		// 3 2 1 + 2 3 2 ^ ^ * + 8 5 1 2 * 2 / - / -

		}
	}
}
