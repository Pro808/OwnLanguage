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

    private Stack<String> stack = new Stack<String>();
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
            case KW_Op_Plus:
            case KW_Op_Minus:
            case KW_Op_Mul:
            case KW_Op_Div:
            case KW_Op_Pow: {
                return true;
            }
            default: {
                return false;
            }
        }
    }

    public int GetPriority (Token token) {
        switch (token.getType()) {
            case KW_Assign:
                return 0;

            case KW_Op_Pow:
                return 5;

            case KW_Op_Mul:
            case KW_Op_Div:
                return 4;

            case KW_Low:
            case KW_More:
            case KW_Eq:
                return 3;

            case KW_Op_Plus:
            case KW_Op_Minus:
                return 2;

            default:
                return 1;
        }
    }

    public void generate() {
        System.out.println("Generation Polk Notation");
        while (positionToken < tokens.size() - 2) {
            nextToken();
            if (isOp(tokenType())) {



                stack.push(translate(currentToken));
            } else {
                result.add(translate(currentToken));
            }
        }
        while (stack.size() > 0) {
            result.add(stack.pop());
        }

        for (String value : result)
            System.out.print(value + " ");
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
