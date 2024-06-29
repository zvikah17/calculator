package org.calc.node;

import org.calc.token.VariableToken;

import java.util.Map;

public class VariableNode implements Node {
    final private String name;

    public VariableNode(VariableToken variableToken) {
        this.name = variableToken.name;
    }

    @Override
    public double eval(Map<String, Double> context) {
        Double value = context.get(name);
        if (value == null)
            throw new RuntimeException("Variable unknown " + name);
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
