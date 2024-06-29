package org.calc.node;

import java.util.Map;

public class NegateNode implements Node {
    private final Node node;

    public NegateNode(Node node) {
        this.node = node;
    }

    @Override
    public double eval(Map<String, Double> context) {
        return -node.eval(context);
    }

    @Override
    public String toString() {
        return "( - " + node.toString() + ")";
    }
}
