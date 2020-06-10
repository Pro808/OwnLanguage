package com.Pro808.Token;

import java.util.HashMap;

public class Word extends Token {
    public static Word
            KW_Int = new Word(TypeToken.KW_Int, "name", "int"),
            KW_Bool = new Word(TypeToken.KW_Bool, "name", "bool"),
            KW_String = new Word(TypeToken.KW_String, "name", "string"),
            KW_Print = new Word(TypeToken.KW_Print, "name", "print"),
            KW_If = new Word(TypeToken.KW_If, "name", "if"),
            KW_Else = new Word(TypeToken.KW_Else, "name", "else"),
            KW_While = new Word(TypeToken.KW_For, "name", "while"),
            KW_For = new Word(TypeToken.KW_While, "name", "for"),
            KW_True = new Word(TypeToken.KW_True, "name", "true"),
            KW_False = new Word(TypeToken.KW_False, "name", "false"),

            KW_Assign = new Word(TypeToken.KW_Assign, "name", "KW_Assign"),
            KW_Eq= new Word(TypeToken.KW_Eq, "name", "KW_Eq"),
            KW_More = new Word(TypeToken.KW_More, "name", "KW_More"),
            KW_Low = new Word(TypeToken.KW_Low, "name", "KW_Low"),
            KW_Op_Plus = new Word(TypeToken.KW_Op_Plus, "name", "KW_Op_Plus"),
            KW_Op_Minus = new Word(TypeToken.KW_Op_Minus, "name", "KW_Op_Minus"),
            KW_Op_Mul = new Word(TypeToken.KW_Op_Mul, "name", "KW_Op_Mul"),
            KW_Op_Div = new Word(TypeToken.KW_Op_Div, "name", "KW_Op_Div"),
            KW_Op_Pow = new Word(TypeToken.KW_Op_Pow, "name", "KW_Op_Pow"),

            KW_Quotes = new Word(TypeToken.KW_Quotes, "name", "KW_Quotes"),
            KW_Round_Open_Bracket = new Word(TypeToken.KW_Round_Open_Bracket, "name", "KW_Round_Open_Bracket"),
            KW_Round_Close_Bracket = new Word(TypeToken.KW_Round_Close_Bracket, "name", "KW_Round_Close_Bracket"),
            KW_Figure_Open_Bracket = new Word(TypeToken.KW_Figure_Open_Bracket, "name", "KW_Figure_Open_Bracket"),
            KW_Figure_Close_Bracket = new Word(TypeToken.KW_Figure_Close_Bracket, "name", "KW_Figure_Close_Bracket"),

            KW_End = new Word(TypeToken.KW_End, "name", "KW_End");

    public Word(TypeToken type, String nameAttrib, String valueAttrib) {
        super(type, nameAttrib, valueAttrib);
    }
}
