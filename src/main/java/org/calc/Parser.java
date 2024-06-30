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
    private Token nextToken;

    public Parser(String expression) {
        tokenizer = new Tokenizer(expression);
        nextToken = tokenizer.next();
    }

    public Node parse() {
        return parseA();
    }

    private Node parseA() {
        assertTokenType(Token.Type.Variable, "variable expected at " + nextToken.position);
        VariableToken variableToken = (VariableToken) nextToken;
        nextToken = tokenizer.next();

        if (!nextToken.isAssign())
            throw new RuntimeException("assignment expected at " + nextToken.position);
        Token assignment = nextToken;
        nextToken = tokenizer.next();

        Node expr = parseE();
        assertTokenType(Token.Type.End, "End expected at " + nextToken.position);

        return switch (assignment.type) {
            case Assign -> new AssignNode(variableToken.name, expr);
            case AssignAdd -> new AssignAddNode(variableToken.name, expr);
            case AssignSubtract -> new AssignSubtractNode(variableToken.name, expr);
            case AssignMultiply -> new AssignMultiplyNode(variableToken.name, expr);
            default -> new AssignDivideNode(variableToken.name, expr);
        };
    }

    private Node parseE() {
        Node leftTerm = parseT();
        while (nextToken.type == Token.Type.Plus || nextToken.type == Token.Type.Minus) {
            Token op = nextToken;
            nextToken = tokenizer.next();
            Node rightTerm = parseT();

            leftTerm = (op.type == Token.Type.Plus) ?
                    new AddNode(leftTerm, rightTerm) :
                    new SubstractNode(leftTerm, rightTerm);
        }

        return leftTerm;
    }

    private Node parseT() {
        Node leftTerm = parseF();
        while (nextToken.type == Token.Type.Multiply || nextToken.type == Token.Type.Divide) {
            Token op = nextToken;
            nextToken = tokenizer.next();
            Node rightTerm = parseF();

            leftTerm = (op.type == Token.Type.Multiply) ?
                    new MultiplyNode(leftTerm, rightTerm) :
                    new DivideNode(leftTerm, rightTerm);
        }

        return leftTerm;
    }

    private Node parseF() {
        Node node;
        switch (nextToken.type) {
            case Open -> {
                nextToken = tokenizer.next();
                node = parseE();
                assertTokenType(Token.Type.Close, "Expecting Close at " + nextToken.position);
                nextToken = tokenizer.next();
            }
            case Plus -> {
                nextToken = tokenizer.next();
                node = parseF();
            }
            case Minus -> {
                nextToken = tokenizer.next();
                node = new NegateNode(parseF());
            }
            case Increment -> {
                nextToken = tokenizer.next();
                assertTokenType(Token.Type.Variable, "Expecting Variable but found " + nextToken.type + " at " + nextToken.position);
                node = new PreIncrementNode(((VariableToken) nextToken).name);
                nextToken = tokenizer.next();
            }
            case Decrement -> {
                nextToken = tokenizer.next();
                assertTokenType(Token.Type.Variable, "Expecting Variable but found " + nextToken.type + " at " + nextToken.position);
                node = new PreDecrementNode(((VariableToken) nextToken).name);
                nextToken = tokenizer.next();
            }
            case Variable -> {
                VariableToken variableToken = (VariableToken) nextToken;
                nextToken = tokenizer.next();

                if (nextToken.type == Token.Type.Increment) {
                    node = new PostIncrementNode(variableToken.name);
                    nextToken = tokenizer.next();
                } else if (nextToken.type == Token.Type.Decrement) {
                    node = new PostDecrementNode(variableToken.name);
                    nextToken = tokenizer.next();
                } else {
                    node = new VariableNode(variableToken);
                }
            }
            case Number -> {
                int value = ((NumberToken) nextToken).value;
                nextToken = tokenizer.next();
                node = new NumberNode(value);
            }
            default -> throw new RuntimeException("Unexpected token " + nextToken.type + " at " + nextToken.position);
        }
        return node;
    }

    private void assertTokenType(Token.Type type, String msg) {
        if (nextToken.type != type)
            throw new RuntimeException(msg);
    }
}
