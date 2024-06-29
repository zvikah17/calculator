package org.calc;

import org.calc.token.NumberToken;
import org.calc.token.Token;
import org.calc.token.VariableToken;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static private final Pattern pattern = Pattern.compile(regex());
    private final Matcher matcher;
    private Token nextToken;

    public Tokenizer(String line) {
        matcher = pattern.matcher(line);
        next();
    }

    static private String regex() {
        StringBuilder buffer = new StringBuilder();
        for(Token.Type token : Token.Type.values())
            buffer.append("|").append(token.group);

        return buffer.substring(1);
    }

    Token nextToken() {
        return nextToken;
    }

    Tokenizer next() {
        //noinspection ResultOfMethodCallIgnored
        matcher.find();

        for (Token.Type tokenType : Token.Type.values()) {
            String string = matcher.group(tokenType.name());
            if (string != null) {
                nextToken = switch (tokenType) {
                    case Variable -> new VariableToken(string);
                    case Number -> new NumberToken(Integer.parseInt(string));
                    case Error -> throw new RuntimeException("Unexpected token " + string);
                    default -> new Token(tokenType);
                };
                break;
            }
        }
        return this;
    }
}
