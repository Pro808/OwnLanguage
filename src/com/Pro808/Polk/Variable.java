package com.Pro808.Polk;

import com.Pro808.Token.TypeToken;

public class Variable {

    public TypeToken type;
    public String name;
    public String value;
    public int scope;
    public Variable(TypeToken type, String name, String value, int scope){
        this.type = type;
        this.name = name;
        this.value = value;
        this.scope = scope;
    }

    public TypeToken getType(){
        return this.type;
    }

    public void setType(TypeToken type){
        this.type = type;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getValue(){
        return this.value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public int getScope(){
        return this.scope;
    }

    public void setScope(int scope){
        this.scope = scope;
    }
    @Override
    public String toString(){
        return String.format("Type: %s \n" +
                            "Name: %s \n" +
                            "Value: %s" +
                            "scope: %d", type, name, value, scope);
    }
}
