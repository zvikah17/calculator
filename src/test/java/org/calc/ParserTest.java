package org.calc;

import org.calc.node.Node;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {

    @Test
    void parseError() {
        Exception exception = assertThrows(RuntimeException.class, () -> testExpression("a = 1 - * 2 + 3", 2));
        assertEquals("Unexpected token Multiply", exception.getMessage());
    }

    @Test
    void parseAdd() {
        testExpression("a = 1 - 2 + 3", 2);
    }

    @Test
    void parseMultiply() {
        testExpression("a = 1 - 2 * 3 / 4 + 3", 2.5);
    }

    @Test
    void parseParentheses() {
        testExpression("a = (1 - 2) * 3 / 4 + 3", 2.25);
    }

    @Test
    void parsePreIncrement() {
        testExpression("a = 1 - ++b + 3", -4, mapOf("b", 7D));
    }

    @Test
    void parsePreDecrement() {
        testExpression("a = -- b + ++b * 2", 20, mapOf("b", 7D));
    }

    @Test
    void parsePostDecrement() {
        testExpression("a = b-- + b * 2", 19, mapOf("b", 7D));
    }

    @Test
    void parsePostIncrement() {
        testExpression("a = b++ + b * 2", 23, mapOf("b", 7D));
    }

    private static void testExpression(String expression, double expected) {
        testExpression(expression, expected, new HashMap<>());
    }

    private static void testExpression(String expression, double expected, Map<String, Double> context) {
        Node node = new Parser(expression).parse();
        System.out.println(node.toString());
        double value = node.eval(context);
        assertEquals(expected, value);
    }

    private static Map<String, Double> mapOf(String k, Double v) {
        return new HashMap<>() {{
            put(k, v);
        }};
    }
}
