package org.calc.token;

public class VariableToken extends Token {
    public final String name;

    public VariableToken(String name) {
        super(Type.Variable);
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
