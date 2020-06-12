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
        if(!isNumeric() && !isBooleans()){
            Token error = new Token(TypeToken.KW_Error, "name", "Не возможно выполнить логическую операцию. Оба токена должны иметь тип числа или логики: ");
            this.op = error;
        }
        if(op.getType() == TypeToken.KW_Low || op.getType() == TypeToken.KW_More){
            Token error = new Token(TypeToken.KW_Error, "name", "Не возможное применение операции к boolean: ");
            this.op = error;
        }
        System.out.println("Создали Logic c OP: " + this.op.getType());
    }

    public TypeToken getType(){
        return this.op.getType();
    }

    public Token getOpToken(){
        return this.op;
    }

    private boolean isBooleans() {
        if (expr1.getType() == TypeToken.KW_Bool_Value && expr2.getType() == TypeToken.KW_Bool_Value) {
            if(expr1.getAttrib().get("boolValue") != null && expr2.getAttrib().get("boolValue") != null) {
                return true;
            }else{
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isNumeric() {
        if (expr1.getType() == TypeToken.KW_Num_Value && expr2.getType() == TypeToken.KW_Num_Value) {
            return true;
        } else {
            return false;
        }
    }

    public Token reduce() {
        Token temp = new Token(TypeToken.KW_Bool_Value, "name", "BoolValue");
        if (isBooleans()) {
            boolean value1 = Boolean.parseBoolean(expr1.getAttrib().get("boolValue"));
            boolean value2 = Boolean.parseBoolean(expr2.getAttrib().get("boolValue"));
            boolean result = false;
            switch (op.getType()) {
                case KW_Eq: {
                    result = value1 == value2;
                    break;
                }
                case KW_Low:
                case KW_More:{
                    Token error = new Token(TypeToken.KW_Error, "name", "Не возможное применение операции к boolean: ");
                    this.op = error;
                    return error;
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
            Token error = new Token(TypeToken.KW_Error, "name", "Не возможно выполнить логическую операцию. Оба токена должны иметь тип числа или логики: ");
            this.op = error;
            return error;
        }
    }

    @Override
    public String toString() {
        return "Token Logic с OP " + op.getType() + " Left: " + expr1 + " Right: " + expr2;
    }
}
