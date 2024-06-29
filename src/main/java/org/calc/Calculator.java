package org.calc;

import org.calc.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    Map<String, Double> calc(List<String> expressions) {
        Map<String, Double> context = new HashMap<>();
        for (String expression: expressions) {
            Parser parser = new Parser(expression);
            Node node = parser.parse();
            node.eval(context);
        }
        return context;
    }
}
