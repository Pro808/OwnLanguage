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
        System.out.println("Создали Arith c OP: " + this.op.getType());
    }

    private boolean isNumeric(){
        if(expr1.getType() ==  TypeToken.KW_Num_Value || expr2.getType() == TypeToken.KW_Num_Value){
            return true;
        }else{
            return false;
        }
    }

    public Token reduce(){
        int value1 = Integer.parseInt(expr1.getAttrib().get("numValue"));
        int value2 = Integer.parseInt(expr2.getAttrib().get("numValue"));
        int value = 0;

        Token temp = new Token(TypeToken.KW_Num_Value, "name", "NumValue");

        if(isNumeric()){
            temp.cloneAttr(expr1.getAttrib());
            switch(op.getType()){
                case KW_Op_Plus:{
                    value = value1 + value2;
                    break;
                }
                case KW_Op_Minus:{
                    value = value1 - value2;
                    break;
                }
                case KW_Op_Mul:{
                    value = value1 * value2;
                    break;
                }
                case KW_Op_Div:{
                    value = (int) value1 / value2;
                    break;
                }
                case KW_Op_Pow:{
                    value = value1;
                    for(int i =0; i < value2-1; i++){
                        value *= value1;
                    }
                    break;
                }
            }
            temp.addAttr("numValue", Integer.toString(value));
            return temp;
        }else{
            try {
                throw new LanguageException("Не возможное приведение типов: " + expr1.getAttrib().get("pos"));
            } catch (LanguageException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public String toString(){
        return "Token Arith с OP " + op.getType() + " Left: " + expr1 + " Right: " + expr2;
    }
}
