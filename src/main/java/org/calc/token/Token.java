package org.calc.token;

public class Token {
    public final Type type;

    public Token(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type.name();
    }

    public enum Type {
        AssignAdd("\\+="),
        AssignDecrement("\\-="),
        AssignMultiply("\\*="),
        AssignDivide("/="),
        Assign("="),
        Increment("\\+\\+"),
        Decrement("\\-\\-"),
        Open("\\("),
        Close("\\)"),
        Number("\\d+"),
        Plus("\\+"),
        Minus("\\-"),
        Multiply("\\*"),
        Divide("/"),
        Modulo("%"),
        Variable("[a-zA-Z]\\w*"),
        Error("[~`!@#$^&,<>.?]"),
        End("$");

        public final String group;

        Type(String s) {
            group = "(?<" + name() + ">" + s + ")";
        }
    }
}
