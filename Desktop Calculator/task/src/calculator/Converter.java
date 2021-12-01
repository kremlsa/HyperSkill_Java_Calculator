package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Converter {
    public static String toRPN(List<String> equation) {

        Stack<String> stack = new Stack<String>();
        ArrayList<String> out = new ArrayList<String>();

        for (String operator : equation) {

            if (operator.matches("[\\+\\-\\*\\/]+")) {
                if (stack.empty() || (stack.peek()).matches("[\\(]+")) {
                    stack.push(operator);
                } else if (operator.equals("*") && (stack.peek()).equals("+") ||
                        operator.equals("*") && (stack.peek()).equals("-") ||
                        operator.equals("/") && (stack.peek()).equals("+") ||
                        operator.equals("/") && (stack.peek()).equals("-")) {
                    stack.push(operator);
                } else {
                    while (!stack.empty() && (stack.peek()).matches("[\\+\\-\\*\\/]+")) {

                        if (operator.equals("*") && (stack.peek()).equals("+") ||
                                operator.equals("*") && (stack.peek()).equals("-") ||
                                operator.equals("/") && (stack.peek()).equals("+") ||
                                operator.equals("/") && (stack.peek()).equals("-") ||
                                (stack.peek()).equals("(")) {
                            stack.push(operator);
                            continue;
                        }
                        out.add(stack.pop());
                        break;
                    }
                    stack.push(operator);
                }

            } else if (operator.equals("(")) {
                stack.push(operator);
            }
            else if (operator.equals(")")) {
                while (!stack.empty() && !stack.peek().equals("(")) {
                    out.add(stack.pop());
                }
                stack.pop();
            } else if (operator.matches("[+-]*[0-9.]+")) {
                out.add(operator);
            }
        }

        while (!stack.empty()) {
            out.add(stack.pop());
        }
        String[] output = new String[out.size()];
        Stack<String> stackRes = new Stack<String>();
        for (String token : out) {
            if (token.matches("[+-]*[0-9.]+")) {
                stackRes.push(token);
            } else {
                double d2 = Double.parseDouble(stackRes.pop());
                double d1 = Double.parseDouble(stackRes.pop());
                double result = token.compareTo("+") == 0 ? d1 + d2 :
                        token.compareTo("-") == 0 ? d1 - d2 :
                                token.compareTo("*") == 0 ? d1 * d2 :
                                        d1 / d2;
                String resultToString = String.valueOf(result);
                if (resultToString.endsWith(".0")) {
                    resultToString = resultToString.replace(".0", "");
                }
                stackRes.push(resultToString);

            }

        }
        return stackRes.pop();
    }
}
