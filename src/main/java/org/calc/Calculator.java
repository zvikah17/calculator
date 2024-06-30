package org.calc;

import org.calc.node.Node;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calculator {
    Map<String, Double> calc(List<String> expressions) {
        Map<String, Double> context = new HashMap<>();
        for (int i=0; i < expressions.size(); i++) {
            Parser parser = new Parser(expressions.get(i));
            try {
                Node node = parser.parse();
                node.eval(context);
            } catch (Exception x) {
                throw new RuntimeException("Line " + i + ": " + x.getMessage());
            }
        }
        return context;
    }
}
