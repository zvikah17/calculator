package org.calc;

import org.calc.token.NumberToken;
import org.calc.token.Token;
import org.calc.token.VariableToken;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    static private final Pattern pattern = Pattern.compile(regex());
    private final Matcher matcher;

    public Tokenizer(String line) {
        matcher = pattern.matcher(line);
    }

    static private String regex() {
        StringBuilder buffer = new StringBuilder();
        for(Token.Type token : Token.Type.values())
            buffer.append("|").append(token.group);

        return buffer.substring(1);
    }

    Token next() {
        //noinspection ResultOfMethodCallIgnored
        matcher.find();

        Token token = null;
        for (Token.Type tokenType : Token.Type.values()) {
            String string = matcher.group(tokenType.name());
            if (string != null) {
                int position = matcher.start();
                token = switch (tokenType) {
                    case Variable -> new VariableToken(string, position);
                    case Number -> new NumberToken(Integer.parseInt(string), position);
                    case Error -> throw new RuntimeException("Unexpected character '" + string + "' at " + position);
                    default -> new Token(tokenType, position);
                };
                break;
            }
        }
        return token;
    }
}
