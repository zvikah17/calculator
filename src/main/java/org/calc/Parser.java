package org.calc;

import org.calc.node.*;
import org.calc.token.NumberToken;
import org.calc.token.Token;
import org.calc.token.VariableToken;

/**
 * A -> V = E | V += E | ...
 * E -> T {[+-] T}
 * T -> F {[*\/%] F}
 * F -> (E) | [+-]F | ++var | --var | var(--) | var(++) | num
 */
public class Parser {
    private final Tokenizer tokenizer;

    public Parser(String expression) {
        tokenizer = new Tokenizer(expression);
    }

    public Node parse() {
        return parseA();
    }

    private Node parseA() {
        if (tokenizer.nextToken().type != Token.Type.Variable)
            throw new RuntimeException("variable expected at " + tokenizer.nextToken().position);
        VariableToken variableToken = (VariableToken) tokenizer.nextToken();
        tokenizer.next();

        if (tokenizer.nextToken().type != Token.Type.Assign &&
                tokenizer.nextToken().type != Token.Type.AssignAdd &&
                tokenizer.nextToken().type != Token.Type.AssignSubtract &&
                tokenizer.nextToken().type != Token.Type.AssignMultiply &&
                tokenizer.nextToken().type != Token.Type.AssignDivide
        )
            throw new RuntimeException("assignment expected at " + tokenizer.nextToken().position);
        Token assignment = tokenizer.nextToken();
        tokenizer.next();

        Node expr = parseE();
        if (tokenizer.nextToken().type != Token.Type.End)
            throw new RuntimeException("End expected at "  + tokenizer.nextToken().position);

        return switch(assignment.type) {
            case Assign -> new AssignNode(variableToken.name, expr);
            case AssignAdd -> new AssignAddNode(variableToken.name, expr);
            case AssignSubtract -> new AssignSubtractNode(variableToken.name, expr);
            case AssignMultiply -> new AssignMultiplyNode(variableToken.name, expr);
            default -> new AssignDivideNode(variableToken.name, expr);
        };
    }

    private Node parseE() {
        Node leftTerm = parseT();
        while (tokenizer.nextToken().type == Token.Type.Plus || tokenizer.nextToken().type == Token.Type.Minus) {
            Token op = tokenizer.nextToken();
            tokenizer.next();
            Node rightTerm = parseT();

            leftTerm =  (op.type == Token.Type.Plus)?
                 new AddNode(leftTerm, rightTerm):
                 new SubstractNode(leftTerm, rightTerm);
        }

        return leftTerm;
    }

    private Node parseT() {
        Node leftTerm = parseF();
        while (tokenizer.nextToken().type == Token.Type.Multiply || tokenizer.nextToken().type == Token.Type.Divide) {
            Token op = tokenizer.nextToken();
            tokenizer.next();
            Node rightTerm = parseF();

            leftTerm =  (op.type == Token.Type.Multiply)?
                 new MultiplyNode(leftTerm, rightTerm):
                 new DivideNode(leftTerm, rightTerm);
        }

        return leftTerm;
    }

    private Node parseF() {
        Node node;
        switch (tokenizer.nextToken().type) {
            case Open -> {
                tokenizer.next();
                node = parseE();
                if (tokenizer.nextToken().type != Token.Type.Close)
                    throw new RuntimeException("Expecting Close but found " + tokenizer.nextToken().type /* token.position*/);
                tokenizer.next();
            }
            case Plus -> {
                tokenizer.next();
                node = parseF();
            }
            case Minus -> {
                tokenizer.next();
                node = new NegateNode(parseF());
            }
            case Increment -> {
                tokenizer.next();
                if (tokenizer.nextToken().type != Token.Type.Variable)
                    throw new RuntimeException("Expecting Variable but found " + tokenizer.nextToken().type /* token.position*/);
                node = new PreIncrementNode(((VariableToken) tokenizer.nextToken()).name);
                tokenizer.next();
            }
            case Decrement -> {
                tokenizer.next();
                if (tokenizer.nextToken().type != Token.Type.Variable)
                    throw new RuntimeException("Expecting Variable but found " + tokenizer.nextToken().type /* token.position*/);
                node = new PreDecrementNode(((VariableToken) tokenizer.nextToken()).name);
                tokenizer.next();
            }
            case Variable -> {
                VariableToken variableToken = (VariableToken) tokenizer.nextToken();
                tokenizer.next();

                if (tokenizer.nextToken().type == Token.Type.Increment) {
                    node = new PostIncrementNode(variableToken.name);
                    tokenizer.next();
                } else if (tokenizer.nextToken().type == Token.Type.Decrement) {
                    node = new PostDecrementNode(variableToken.name);
                    tokenizer.next();
                } else {
                    node = new VariableNode(variableToken);
                }
            }
            case Number -> {
                int value = ((NumberToken) tokenizer.nextToken()).value;
                tokenizer.next();
                node = new NumberNode(value);
            }
            default ->
                    throw new RuntimeException("Unexpected token " + tokenizer.nextToken().type + " at " + tokenizer.nextToken().position);
        }
        return node;
    }
}
