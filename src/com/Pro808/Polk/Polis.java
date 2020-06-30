package com.Pro808.Polk;

import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

import java.util.ArrayList;
import java.util.Stack;

import static com.Pro808.Token.TypeToken.*;

public class Polis {

    private ArrayList<Token> tokens;

    private Token currentToken;
    private int positionToken = -1;
    private int currentScope = 0;

    public Polis(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    private Stack<Token> stack = new Stack<Token>();
    private ArrayList<String> result = new ArrayList<>();

    private Token nextToken() {
        positionToken++;
        currentToken = tokens.get(positionToken);
        return currentToken;
    }

    private TypeToken tokenType() {
        return currentToken.getType();
    }

    public boolean isOp(TypeToken typeToken) {
        switch (typeToken) {
            case KW_Assign:
            case KW_Op_Plus:
            case KW_Op_Minus:
            case KW_Op_Mul:
            case KW_Op_Div:
            case KW_Op_Pow:
            case KW_Round_Open_Bracket:
            case KW_Low:
            case KW_More:
            case KW_Eq:
            case KW_Round_Close_Bracket: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int GetPriority(Token token) {
        switch (token.getType()) {
            case KW_Assign:
                return 0;

            case KW_Round_Open_Bracket:
                return 2;
            case KW_Round_Close_Bracket:
                return 3;

            case KW_Op_Plus:
            case KW_Op_Minus:
                return 4;

            case KW_Low:
            case KW_More:
            case KW_Eq:
                return 5;

            case KW_Op_Mul:
            case KW_Op_Div:
                return 6;

            case KW_Op_Pow:
                return 7;

            default:
                return 1;
        }
    }

    public void generate() {
        System.out.println("Generation Polk Notation");

        while (positionToken < tokens.size() - 1) {
            nextToken();

            if (tokenType() == KW_End) {
                while (stack.size() > 0) {
                    result.add(translate(stack.pop()));
                }
                continue;
            }

            if (isOp(tokenType())) {
                if (tokenType() == KW_Round_Open_Bracket) {
                    stack.push(currentToken);
                } else if (tokenType() == KW_Round_Close_Bracket) {
                    Token s = stack.pop();
                    while (s.getType() != KW_Round_Open_Bracket) {
                        result.add(translate(s));
                        s = stack.pop();
                    }
                } else {
                    while (stack.size() > 0 && GetPriority(currentToken) <= GetPriority(stack.peek())) {
                        result.add(translate(stack.pop()));
                    }
                    stack.push(currentToken);
                }
            }else{
                result.add(translate(currentToken));
            }
        }



        if (stack.size() > 0)
            for (Token token : stack) {
                result.add(translate(token));
            }

        for (String s : result) {
            System.out.print(s + " ");
        }
    }


    private String translate(Token token) {
        switch (token.getType()) {
            case KW_Int:
            case KW_String:
            case KW_Bool:
            case KW_List: {
                return token.getAttrib().get("name");
            }
            case KW_Num_Value: {
                return token.getAttrib().get("numValue");
            }
            case KW_String_Value: {
                return token.getAttrib().get("stringValue");
            }
            case KW_Bool_Value: {
                return token.getAttrib().get("boolValue");
            }
            case KW_If: {
                return "if";
            }
            case KW_For: {
                return "for";
            }
            case KW_While: {
                return "while";
            }
            case KW_Assign: {
                return "=";
            }
            case KW_Op_Pow: {
                return "**";

            }
            case KW_Op_Plus: {
                return "+";
            }
            case KW_Op_Minus: {
                return "-";
            }
            case KW_Op_Div: {
                return "/";
            }
            case KW_Op_Mul: {
                return "*";
            }
            case KW_Eq: {
                return "==";
            }
            case KW_Low: {
                return "<";
            }
            case KW_More: {
                return ">";
            }
            case KW_Round_Open_Bracket: {
                return "(";
            }
            case KW_Round_Close_Bracket: {
                return ")";
            }
            case KW_Quotes: {
                return "\"";
            }
            case KW_Name: {
                return token.getAttrib().get("name");
            }
            case KW_End: {
                return ";";
            }
            case KW_Figure_Open_Bracket: {
                return "{";
            }
            case KW_Figure_Close_Bracket: {
                return "}";
            }
            case KW_Access: {
                return ".";
            }
        }
        return "EXPECTED";
    }

}
