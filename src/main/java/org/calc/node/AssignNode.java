package org.calc.node;

import java.util.Map;

public class AssignNode implements Node {
    private final String name;
    private final Node node;

    public AssignNode(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    @Override
    public double eval(Map<String, Double> context) {
        double value = node.eval(context);
        context.put(name, value);
        return value;
    }

    @Override
    public String toString() {
        return name + " = " + node.toString();
    }
}
