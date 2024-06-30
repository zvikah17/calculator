package org.calc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    void calcOK() {
        List<String> expressions = Arrays.asList(
                "i = 0",
                "j = ++i",
                "x = i++ + 5",
                "y = 5 + 3 * 10",
                "i += y"
        );

        Map<String, Double> result = calculator.calc(expressions);
        assertEquals("{x=6.0, i=37.0, y=35.0, j=1.0}", result.toString());
    }

    @Test
    void calcError() {
        List<String> expressions = Arrays.asList(
                "i = 0",
                "j = ++i",
                "x = i++ + (5",
                "y = 5 + 3 * 10",
                "i += y"
        );

        Exception exception = assertThrows(RuntimeException.class, () ->  calculator.calc(expressions));
        assertEquals("Line 2: Expecting Close at 12", exception.getMessage());
    }
}
