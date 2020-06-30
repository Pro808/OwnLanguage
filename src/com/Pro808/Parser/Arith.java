package com.Pro808.Parser;

import com.Pro808.LanguageException;
import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

public class Arith extends Token {

    Token op, expr1, expr2;

    public Arith(Token op, Token expr1, Token expr2){
        super(op.getType(), "name", "Arith");
        this.op = op;
        this.expr1 = expr1.reduce();
        this.expr2 = expr2.reduce();
        if(!isNumeric()) {
            Token error = new Token(TypeToken.KW_Error, "name", "Не возможно выполнить арифметическую операцию. Оба токена должны иметь тип числа: ");
            this.op = error;
        }
        System.out.println("Создали Arith c OP: " + this.op.getType() + "expr1 " + expr1 + " expr2: " + expr2);
    }

    private boolean isNumeric(){
        if(expr1.getType() == TypeToken.KW_Return || expr2.getType() == TypeToken.KW_Return)
        {
            return true;
        }else if(expr1.getType() ==  TypeToken.KW_Num_Value && expr2.getType() == TypeToken.KW_Num_Value){
            return true;
        }else{
            return false;
        }
    }

    public TypeToken getType(){
        return this.op.getType();
    }

    public Token getOpToken(){
        return this.op;
    }

    public Token reduce(){
        Token temp = new Token(TypeToken.KW_Num_Value, "name", "NumValue");
        temp.addAttr("numValue", "1");
        return temp;
    }

    @Override
    public String toString(){
        return "Token Arith с OP " + op.getType() + " Left: " + expr1 + " Right: " + expr2;
    }
}
