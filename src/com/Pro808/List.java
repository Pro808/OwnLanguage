package com.Pro808;

import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

import java.util.ArrayList;

public class List {

    public static ArrayList<Token> voids = new ArrayList<Token>();
    static {
        voids.add(new Token(TypeToken.KW_Void, "name", "add"));
        voids.add(new Token(TypeToken.KW_Void, "name", "remove"));
        voids.add(new Token(TypeToken.KW_Return, "name", "next"));
        voids.add(new Token(TypeToken.KW_Return, "name", "back"));
        voids.add(new Token(TypeToken.KW_Return, "name", "get"));
    }

}
