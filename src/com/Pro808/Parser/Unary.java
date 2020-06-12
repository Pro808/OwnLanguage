package com.Pro808.Parser;

import com.Pro808.LanguageException;
import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

public class Unary extends Token {

    Token op, expr1;

    public Unary(Token op, Token expr1) {
        super(op.getType(), "name", "Unary");
        this.op = op;
        this.expr1 = expr1.reduce();
        if(expr1.getType() !=  TypeToken.KW_Num_Value){
            Token error = new Token(TypeToken.KW_Error, "name", "Унарный минус применим только к числам: ");
            this.op = error;
        }
        System.out.println("Создали Unary c OP: " + this.op.getType() + " expr: " + expr1);
    }

    public TypeToken getType(){
        return this.op.getType();
    }

    public Token getOpToken(){
        return this.op;
    }

    public Token reduce() {
        switch (op.getType()) {
            case KW_Op_Minus: {
                int value1 = 0;
                if(expr1.getType() ==  TypeToken.KW_Num_Value) {
                    value1 = Integer.parseInt(expr1.getAttrib().get("numValue"));
                }else{
                    Token error = new Token(TypeToken.KW_Error, "name", "Унарный минус применим только к числам: ");
                    this.op = error;
                    return error;
                }
                value1 = -value1;
                Token temp = new Token(TypeToken.KW_Num_Value, "name", "NumValue");
                temp.addAttr("numValue", Integer.toString(value1));
                return temp;
            }
            default: {
                return this;
            }
        }
    }

}
