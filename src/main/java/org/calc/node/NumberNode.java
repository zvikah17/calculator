package org.calc.node;

import java.util.Map;

public class NumberNode implements Node {
    final private int value;

    public NumberNode(int value) {
        this.value = value;
    }

    @Override
    public double eval(Map<String, Double> context) {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
