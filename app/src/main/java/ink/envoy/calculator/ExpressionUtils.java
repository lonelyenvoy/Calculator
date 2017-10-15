package ink.envoy.calculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

class ExpressionUtils {

    static BigDecimal compute(String expr)
            throws EmptyExpressionException, IllegalExpressionException, InvalidComputationException,
            ArithmeticException, NumberFormatException, CalculationOverflowException,
            NonIntegerFactorialException, NegativeFactorialException {
        expr = expr.trim();
        if (expr.length() == 0) {
            throw new EmptyExpressionException();
        }
        expr = preprocessExpression(expr);
        if (!isValidExpression(expr) || !braceMatches(expr)) {
            throw new IllegalExpressionException();
        }
        return _compute(convertToPostFix(expr)).stripTrailingZeros();
    }

    private static String preprocessExpression(String expr) {
        expr = expr.replace("×", "*");
        expr = expr.replace("÷", "/");
        expr = expr.replace("√", "(0)$");
        expr = expr.replace("!", "!(0)");
        expr = expr.replace("π", "(3.1415926535897932384626433832795)");
        return expr;
    }

    private static BigDecimal _compute(String postExpression)
            throws InvalidComputationException, ArithmeticException, NumberFormatException,
            CalculationOverflowException, NonIntegerFactorialException, NegativeFactorialException {
        Stack<BigDecimal> numberStack = new Stack<>(); // store all numbers and results

        String[] units = postExpression.split(" ");

        for (String currentHandlingString : units) { // loop until there is nothing in string stream
            char currentHandlingChar = currentHandlingString.charAt(0);
            if (currentHandlingString.length() == 1 && isOperator(currentHandlingChar)) { // currentHandlingString is operator
                if (numberStack.size() < 2) { // no enough numbers in stack, failed
                    throw new InvalidComputationException();
                }
                BigDecimal operand1 = numberStack.peek();
                numberStack.pop();
                BigDecimal operand2 = numberStack.peek();
                numberStack.pop();
                switch (currentHandlingChar) { // compute and push
                    case '+': numberStack.push(operand2.add(operand1)); break;
                    case '-': numberStack.push(operand2.subtract(operand1)); break;
                    case '*': numberStack.push(operand2.multiply(operand1)); break;
                    case '/': numberStack.push(operand2.divide(operand1, 50, RoundingMode.HALF_EVEN)); break;
                    case '%': numberStack.push(operand2.divideAndRemainder(operand1)[1]); break;
                    case '^': {
                        double operand1Double = operand1.doubleValue();
                        double operand2Double = operand2.doubleValue();
                        numberStack.push(new BigDecimal(Math.pow(operand2Double, operand1Double)));
                        break;
                    }
                    case '$': {
                        double operand1Double = operand1.doubleValue();
                        numberStack.push(new BigDecimal(Math.sqrt(operand1Double)));
                        break;
                    }
                    case '!': {
                        int operand2Int = operand2.intValue();
                        if (operand2.compareTo(BigDecimal.valueOf(operand2Int)) != 0) {
                            throw new NonIntegerFactorialException();
                        }
                        numberStack.push(new BigDecimal(MathUtils.factorial(operand2Int)));
                        break;
                    }
                }
            } else { // currentHandlingString is number
                numberStack.push(new BigDecimal(currentHandlingString));
            }
        }

        if (numberStack.size() > 1) {
            throw new InvalidComputationException(); // more than 1 number in stack, invalid computation
        }
        return numberStack.peek(); // the last number in stack is result
    }

    // 中缀表达式转后缀表达式
    private static String convertToPostFix(String infixExpression) {
        return toPostfix(infixExpression);
    }

    // 中缀转后缀核心算法
    private static String toPostfix(String expr) {
        expr += "#";
        String result = "";
        Stack<Character> operatorStack = new Stack<>();
        operatorStack.push('#');

        for (int i = 0; i < expr.length(); ) {
            char c = expr.charAt(i);
            if (isDigit(c) || c == '.') { // 如果c是数字，放入结果中
                result += c;
                if (!isDigit(expr.charAt(i + 1)) && expr.charAt(i + 1) != '.') { // 确保结果中的数字在一起显示
                    result += " ";
                }
                ++i;
            } else if (isOperator(c)) {
                char topChar = operatorStack.peek();
                if (getIcp(c) > getIsp(topChar)) { // 如果 icp(c) > isp(op)，令c入栈并继续循环
                    operatorStack.push(c);
                    ++i;
                } else { // 如果 icp(c) <= isp(op)，弹栈
                    operatorStack.pop();
                    if (getIcp(c) < getIsp(topChar)) { // 如果 icp(c) < isp(op)，令op入栈
                        result += topChar;
                        result += " ";
                    } else if (leftBraces.contains(topChar)) { // 循环至c和栈顶字符均为左括号，再继续读入下一字符
                        ++i;
                    }
                }
            } else if (c == '#') { // 到达字符串尾部，将所有操作数放入结果中并结束
                if (operatorStack.peek() == '#')
                    break;
                result += operatorStack.pop();
                result += " ";
            }
        }
        return result;
    }

    private static final String validChars = " 0123456789.+-*/%^$!()[]{}"; // 有效的字符
    private static final String operators = "+-*/%^$!()[]{}"; // 操作符

    // 判断字符是否为数字
    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    // 判断字符是否为操作符
    private static boolean isOperator(char c) {
        return operators.contains("" + c);
    }

    // 判断表达式是否有效
    private static boolean isValidExpression(String expr) {
        for (char c : expr.toCharArray()) {
            if (!validChars.contains("" + c)) return false;
        }
        return true;
    }

    // 运算符栈内优先级（In-stack priority）
    private static final Map<Character, Integer> ispMap = new HashMap<Character, Integer>() {{
        put('#', 0);
        put('(', 1);
        put('[', 1);
        put('{', 1);
        put('*', 5);
        put('/', 5);
        put('%', 5);
        put('+', 3);
        put('-', 3);
        put('^', 6);
        put('$', 7);
        put('!', 8);
        put(')', 10);
        put(']', 10);
        put('}', 10);
    }};

    // 运算符栈外优先级（Incoming priority）
    private static final Map<Character, Integer> icpMap = new HashMap<Character, Integer>() {{
        put('#', 0);
        put('(', 10);
        put('[', 10);
        put('{', 10);
        put('*', 4);
        put('/', 4);
        put('%', 4);
        put('+', 2);
        put('-', 2);
        put('^', 5);
        put('$', 6);
        put('!', 7);
        put(')', 1);
        put(']', 1);
        put('}', 1);
    }};

    // 取运算符栈内优先级
    private static int getIsp(char c) {
        return ispMap.get(c);
    }

    // 取运算符栈外优先级
    private static int getIcp(char c) {
        return icpMap.get(c);
    }

    // 左括号列表
    private static final List<Character> leftBraces = new ArrayList<Character>() {{
        add('(');
        add('[');
        add('{');
    }};

    // 右括号列表
    private static final List<Character> rightBraces = new ArrayList<Character>() {{
        add(')');
        add(']');
        add('}');
    }};

    // 判断表达式中的括号是否匹配
    private static boolean braceMatches(String str) {
        Stack<Character> stack = new Stack<>();
        for (char c : str.toCharArray()) {
            int index;
            if ((index = leftBraces.indexOf(c)) != -1) {
                stack.push(rightBraces.get(index));
            } else if (rightBraces.contains(c)) {
                if (stack.empty() || stack.pop() != c) return false;
            }
        }
        return stack.empty();
    }
}

class EmptyExpressionException extends Exception {}
class IllegalExpressionException extends Exception {}
class InvalidComputationException extends Exception {}
class NonIntegerFactorialException extends Exception {}