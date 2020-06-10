package com.Pro808.Parser;

import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

public class Variable extends Token{
    public Variable(TypeToken type, String nameAttrib, String valueAttrib) {
        super(type, nameAttrib, valueAttrib);
    }
  /*
    public String name;
    public TypeToken type;
    public String value;

    public Variable(String name, TypeToken type, ){
        super();
        this.name = name;
       this.type = type;
       this.value = value;
    }
   */


}
