package org.calc.node;

import java.util.Map;

public class PostDecrementNode implements Node {
    private final String name;

    public PostDecrementNode(String name) {
        this.name = name;
    }

    @Override
    public double eval(Map<String, Double> context) {
        Double value = context.get(name);
        if (value == null)
            throw new RuntimeException("Variable unknown " + name);

        context.put(name, value - 1);
        return value;
    }

    @Override
    public String toString() {
        return "( " + name + "-- )";
    }
}
