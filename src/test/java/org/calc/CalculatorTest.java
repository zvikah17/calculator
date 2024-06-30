package org.calc;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    Calculator calculator = new Calculator();

    @Test
    void calc() {
        List<String> expressions = Arrays.asList(
                "i = 0",
                "j = ++i",
                "x = i++ + 5",
                "y = 5 + 3 * 10",
                "i += y"
        );

        Map<String, Double> result = calculator.calc(expressions);
        System.out.println(result);
    }
}
