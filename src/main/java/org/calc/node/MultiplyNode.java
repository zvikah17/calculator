package org.calc.node;

import java.util.Map;

public class MultiplyNode implements Node {
    private final Node left;
    private final Node right;

    public MultiplyNode(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public double eval(Map<String, Double> context) {
        return left.eval(context) * right.eval(context);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " * " + right.toString() + ")";
    }
}
