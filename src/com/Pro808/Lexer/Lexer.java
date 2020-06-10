package com.Pro808.Lexer;

import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;
import com.Pro808.Token.Word;

import java.util.ArrayList;

public class Lexer {

    private String inputProgramData;
    /*
        private int currentPos = 0;
        private int startPos = 0;

        private TypeToken nextTypeToken = null;
        private TypeToken currentTypeToken = null;
        private TypeToken preventTypeToken = null;
    */

    public boolean debug = false;
    private int from = 0;
    private int current = -1;
    private Character curChar = null;
    private Character nextChar = null;

    private ArrayList<Token> words = new ArrayList<>();

    private ArrayList<Token> tokens = new ArrayList<>();

    private void addWord(String word, TypeToken type) {
        words.add(new Word(type, "name", word));
    }

    public Lexer(String programData) {
        this.inputProgramData = programData + ";";
        addWord("int", TypeToken.KW_Int);
        addWord("bool", TypeToken.KW_Bool);
        addWord("string", TypeToken.KW_String);
        addWord("print", TypeToken.KW_Print);
        addWord("if", TypeToken.KW_If);
        addWord("else", TypeToken.KW_Else);
        addWord("while", TypeToken.KW_For);
        addWord("for", TypeToken.KW_While);
        addWord("true", TypeToken.KW_True);
        addWord("false", TypeToken.KW_False);
    }
    public void debug(boolean enable){
        this.debug = enable;
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    private Character nextChar() {
        current++;
        if(current >= inputProgramData.length()) {
           return null;
        }
        Character c = inputProgramData.charAt(current);
        curChar = c;
        return c;
    }

    private void returnBack(){
        current--;
        curChar = inputProgramData.charAt(current);
    }

    private boolean nextCharIs(Character c) {
        Character next = nextChar();
        if(next != null){
            return next.equals(c);
        }else{
            return true;
        }

    }

    private void addToken(Token token){
        if(debug){
            System.out.println("Added Token: " + token.getAttrib().get("name"));
        }
        Token temp = new Token(token);
        temp.getAttrib().put("pos", Integer.toString(current-1));
        tokens.add(temp);
    }

    public void scan() {
        while (nextChar() != null) {
            if(debug) {
                System.out.println("Cur: " + curChar + " | Pos: " + current);
            }
            switch (curChar) {
                case '=': {
                    if (nextCharIs('=')) {
                        addToken(Word.KW_Eq);
                    } else {
                        addToken(Word.KW_Assign);
                    }
                    break;
                }
                case '>': {
                    addToken(Word.KW_More);
                    break;
                }
                case '<': {
                    addToken(Word.KW_Low);
                    break;
                }
                case '+':{
                    addToken(Word.KW_Op_Plus);
                    break;
                }
                case '-':{
                    addToken(Word.KW_Op_Minus);
                    break;
                }
                case '/':{
                    addToken(Word.KW_Op_Div);
                    break;
                }
                case '*':{
                    if(nextCharIs('*')){
                        addToken(Word.KW_Op_Pow);
                    }else{
                        addToken(Word.KW_Op_Mul);
                    }
                    break;
                }
                case ' ': {
                    continue;
                }
                case '(': {
                    addToken(Word.KW_Round_Open_Bracket);
                    break;
                }
                case ')': {
                    addToken(Word.KW_Round_Close_Bracket);
                    break;
                }
                case '{': {
                    addToken(Word.KW_Figure_Open_Bracket);
                    break;
                }
                case '}': {
                    addToken(Word.KW_Figure_Close_Bracket);
                    break;
                }
                case '"': {
                    addToken(Word.KW_Quotes);
                    StringBuilder text = new StringBuilder();
                    while(!nextCharIs('"')){
                        text.append(curChar);
                    }
                    Token t = new Token(TypeToken.KW_String_Value, "name", "StringValue");
                    t.addAttr("stringValue", text.toString());
                    addToken(t);

                    addToken(Word.KW_Quotes);
                    break;
                }
                case ';': {
                    addToken(Word.KW_End);
                    break;
                }
            }

            if (Character.isDigit(curChar)) {
                int v = 0;
                do {
                    v = v * 10 + Character.digit(curChar, 10);
                } while (Character.isDigit(nextChar()));
                returnBack();
                Token num = new Token(TypeToken.KW_Num_Value, "name", "NumValue");
                num.addAttr("numValue", Integer.toString(v));
                addToken(num);
            }
            if (Character.isLetter(curChar)) {
                StringBuilder strB = new StringBuilder();
                do {
                    strB.append(curChar);
                } while (Character.isLetterOrDigit(nextChar()));
                returnBack();
                String str = strB.toString();
                Token newToken = new Token(TypeToken.KW_Name, "name", str);
                Token exist = null;
                for (Token token : words) {
                    if (str.equals(token.getAttrib().get("name"))) {
                        exist = token;
                    }
                }
                if(exist == null){
                    words.add(newToken);
                    addToken(newToken);
                }else{
                    addToken(exist);
                }
            }
        }
    }
}
