package org.calc;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner in = new Scanner(System.in);

        List<String> expressions = new ArrayList<>();
        while (true) {
            String line = in.nextLine();
            if (line.isBlank()) {
                try {
                    Map<String, Double> result = calculator.calc(expressions);
                    System.out.println(result);
                } catch (Exception x) {
                    for (String ex : expressions) System.out.println(ex);
                    System.out.println("Error: " + x.getMessage());
                }
                expressions.clear();
            } else if (line.equals("$")) {
                System.exit(0);
            } else {
                expressions.add(line);
            }
        }
    }
}
