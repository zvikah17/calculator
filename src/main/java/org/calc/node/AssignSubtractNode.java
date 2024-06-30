package org.calc.node;

import java.util.Map;

public class AssignSubtractNode implements Node {
    private final String name;
    private final Node node;

    public AssignSubtractNode(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    @Override
    public double eval(Map<String, Double> context) {
        Double originalValue = context.get(name);
        if (originalValue == null)
            throw new RuntimeException("Variable unknown " + name);
        double value = originalValue - node.eval(context);
        context.put(name, value);
        return value;
    }

    @Override
    public String toString() {
        return name + " -= " + node.toString();
    }
}
