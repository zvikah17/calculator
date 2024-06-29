package org.calc.node;

import java.util.Map;

public class PreIncrementNode implements Node {
    private final String name;

    public PreIncrementNode(String name) {
        this.name = name;
    }

    @Override
    public double eval(Map<String, Double> context) {
        Double value = context.get(name);
        if (value == null)
            throw new RuntimeException("Variable unknown " + name);

        context.put(name, ++value);
        return value;
    }

    @Override
    public String toString() {
        return "( ++" + name + ")";
    }
}
