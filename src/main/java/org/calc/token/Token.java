package org.calc.token;

public class Token {
    public final Type type;
    public final int position;

    public Token(Type type, int position) {
        this.type = type;
        this.position = position;
    }

    @Override
    public String toString() {
        return type.name();
    }

    public boolean isAssign() {
        return type == Type.Assign || type == Type.AssignAdd || type == Type.AssignSubtract || type == Type.AssignMultiply ||
                type == Type.AssignDivide;
    }

    public enum Type {
        AssignAdd("\\+="),
        AssignSubtract("\\-="),
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
        Variable("[a-zA-Z]\\w*"),
        Error("[~`!@#$^&,<>.?]"),
        End("$");

        public final String group;

        Type(String s) {
            group = "(?<" + name() + ">" + s + ")";
        }
    }
}
