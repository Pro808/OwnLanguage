package com.Pro808;

import com.Pro808.Token.Token;
import com.Pro808.Utils.ColorFy;

import java.util.ArrayList;

public class LanguageException extends Exception {

    String exception = "";
    int posToken = 0;
    ArrayList<Token> tokens = new ArrayList<Token>();

    public LanguageException(String stringException){
        super(stringException);
    }

    public void setPosToken(int posToken){
        this.posToken = posToken;
        ColorFy coloredProgram = new ColorFy(this.tokens, "./error.html", posToken);
    }

    public void setTokens(ArrayList<Token> tokens){
        this.tokens = tokens;
    }

    public void setException(String Exception){
        this.exception = Exception;
    }
}
