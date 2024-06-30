package org.calc;

import org.calc.token.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TokenizerTest {

    @Test
    void validTokens() {
        List<Token> tokens = getTokens("134 --9a_dd");
        assertEquals(Token.Type.Number, tokens.get(0).type);
    }

    @Test
    void invalidTokens() {
        Exception x = assertThrows(RuntimeException.class, () -> getTokens("134- --<a_dd"));
        assertEquals("Unexpected character '<' at 7", x.getMessage());
    }

    private static List<Token> getTokens(String string) {
        Tokenizer tokenizer = new Tokenizer(string);
        List<Token> tokens = new ArrayList<>();
        Token token = tokenizer.nextToken();
        while (token.type != Token.Type.End) {
            tokens.add(token);
            token = tokenizer.next().nextToken();
        }
        return tokens;
    }
}