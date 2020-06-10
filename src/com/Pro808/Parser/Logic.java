package com.Pro808.Parser;

import com.Pro808.LanguageException;
import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

public class Logic extends Token {

    Token op, expr1, expr2;

    public Logic(Token op, Token expr1, Token expr2) {
        super(op.getType(), "name", "Logic");
        this.op = op;
        this.expr1 = expr1.reduce();
        this.expr2 = expr2.reduce();
        System.out.println("Создали Logic c OP: " + this.op.getType());
    }

    private boolean isBooleans() {
        if (expr1.getType() == TypeToken.KW_Bool_Value || expr2.getType() == TypeToken.KW_Bool_Value) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNumeric() {
        if (expr1.getType() == TypeToken.KW_Num_Value || expr2.getType() == TypeToken.KW_Num_Value) {
            return true;
        } else {
            return false;
        }
    }

    public Token reduce() {
          /*  int value1 = Integer.parseInt(expr1.getAttrib().get("numValue"));
            int value2 = Integer.parseInt(expr2.getAttrib().get("numValue"));
            int value = 0;
            */
        Token temp = new Token(TypeToken.KW_Bool_Value, "name", "BoolValue");
        if (isBooleans()) {
            boolean value1 = Boolean.parseBoolean(expr1.getAttrib().get("boolValue"));
            boolean value2 = Boolean.parseBoolean(expr1.getAttrib().get("boolValue"));
            boolean result = false;
            switch (op.getType()) {
                case KW_Eq: {
                    result = value1 == value2;
                    break;
                }
                case KW_Low:
                case KW_More:{
                    try {
                        throw new LanguageException("Не возможное применение операции к boolean: " + expr1.getAttrib().get("pos"));
                    } catch (LanguageException e) {
                        e.printStackTrace();
                    }
                }
            }
            temp.addAttr("boolValue", Boolean.toString(result));
            return temp;
        } else if(isNumeric()) {
            int value1 = Integer.parseInt(expr1.getAttrib().get("numValue"));
            int value2 = Integer.parseInt(expr2.getAttrib().get("numValue"));
            boolean result = false;
            switch (op.getType()){
                case KW_Eq:{
                    result = value1 == value2;
                    break;
                }
                case KW_Low:{
                    result = value1 < value2;
                }
                case KW_More:{
                    result = value1 > value2;
                }
            }
            temp.addAttr("boolValue", Boolean.toString(result));
            return temp;
        } else{
            try {
                throw new LanguageException("Не возможное приведение типов: " + expr1.getAttrib().get("pos"));
            } catch (LanguageException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public String toString() {
        return "Token Logic с OP " + op.getType() + " Left: " + expr1 + " Right: " + expr2;
    }
}
