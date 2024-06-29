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
            throw new RuntimeException("variable expected at " /*+ token.position*/);
        VariableToken variableToken = (VariableToken) tokenizer.nextToken();
        tokenizer.next();

        if (tokenizer.nextToken().type != Token.Type.Assign) // TODO more ass
            throw new RuntimeException("assignment expected at " /*+ token.position*/);
        Token assignment = tokenizer.nextToken();
        tokenizer.next();

        Node expr = parseE();
        if (tokenizer.nextToken().type != Token.Type.End)
            throw new RuntimeException("End expected at " /*+ token.position*/);

        return new AssignNode(variableToken.name, expr); // TODO more ass
    }

    private Node parseE() {
        Node leftTerm = parseT();
        while (tokenizer.nextToken().type == Token.Type.Plus || tokenizer.nextToken().type == Token.Type.Minus) {
            Token op = tokenizer.nextToken();
            tokenizer.next();
            Node rightTerm = parseT();

            if (op.type == Token.Type.Plus)
                leftTerm = new AddNode(leftTerm, rightTerm);
            else
                leftTerm = new SubstractNode(leftTerm, rightTerm);
        }

        return leftTerm;
    }

    private Node parseT() {
        Node leftTerm = parseF();
        while (tokenizer.nextToken().type == Token.Type.Multiply || tokenizer.nextToken().type == Token.Type.Divide) {
            Token op = tokenizer.nextToken();
            tokenizer.next();
            Node rightTerm = parseF();

            if (op.type == Token.Type.Multiply)
                leftTerm = new MultiplyNode(leftTerm, rightTerm);
            else if (op.type == Token.Type.Divide)
                leftTerm = new DivideNode(leftTerm, rightTerm);
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
            default -> throw new RuntimeException("Unexpected token " + tokenizer.nextToken().type /* token.position*/);
        }
        return node;
    }
}
