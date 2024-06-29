package org.calc.token;

public class NumberToken extends Token {
    public final int value;

    public NumberToken(int value) {
        super(Type.Number);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
