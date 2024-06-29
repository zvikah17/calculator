package org.calc.node;

import java.util.Map;

public interface Node {
    double eval(Map<String, Double> context);
}
