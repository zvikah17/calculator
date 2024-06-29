package org.calc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        List<String> expressions = Arrays.asList(
                "a = 1",
                "b = 1 + a"
        );
        try {
            Map<String, Double> result = calculator.calc(expressions);
            System.out.println(result);
        } catch (Exception x) {
            System.out.println("Error: " + x.getMessage());
        }
    }
}
