package com.Pro808.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class Token {

    private TypeToken type;
    private HashMap<String, String> attrib = new HashMap<>();

    public Token(TypeToken type,String  nameAttrib, String valueAttrib) {
        this.type = type;
        this.attrib.put(nameAttrib, valueAttrib);
    }

    public Token(Token token) {
        this.type = token.getType();
        this.attrib.putAll(token.getAttrib());
    }

    public String toString(){
        StringBuilder str = new StringBuilder("TypeToken: " + getType() + " attr : { ");
        this.attrib.forEach((key, value)->{
            str.append("\nKey: " + key + "\nValue: " + value);
        });
        str.append("\n}");
        return str.toString();
    }

    public Token reduce(){
        return this;
    }

    public void setType(TypeToken type){
        this.type = type;
    }

    public TypeToken getType() {
        return type;
    }

    public HashMap<String, String> getAttrib() {
        return attrib;
    }

    public void addAttr(String nameAttr, String valueAttr){
        this.attrib.put(nameAttr, valueAttr);
    }

    public void cloneAttr(HashMap<String, String> attrs){
        this.attrib.putAll(attrs);
    }

}
