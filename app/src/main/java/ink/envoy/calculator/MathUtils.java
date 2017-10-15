package ink.envoy.calculator;

import java.math.BigInteger;

class MathUtils {
    private static final int BOUND = 3201;
    private static BigInteger[] list = new BigInteger[BOUND];
    private static boolean isInitialized = false;

    private static void initialize() {
        list[0] = list[1] = BigInteger.ONE;
        for (int i = 2; i < BOUND; ++i) {
            list[i] = BigInteger.ZERO;
        }
    }

    static BigInteger factorial(int n) throws NegativeFactorialException, CalculationOverflowException {
        if (!isInitialized) {
            initialize();
            isInitialized = true;
        }
        if (n < 0) throw new NegativeFactorialException();
        if (n >= BOUND) throw new CalculationOverflowException();
        return _factorial(n);
    }

    private static BigInteger _factorial(int n) {
        if (list[n].compareTo(BigInteger.ZERO) != 0) return list[n];
        return list[n] = _factorial(n - 1).multiply(BigInteger.valueOf(n));
    }
}

class CalculationOverflowException extends Exception {}
class NegativeFactorialException extends Exception {}