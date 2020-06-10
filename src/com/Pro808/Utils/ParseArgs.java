package com.Pro808.Utils;

import java.util.*;

public class ParseArgs {

    private String[] args;

    public HashMap<String, String> values = new HashMap<>();

    public ParseArgs(String[] args){
         this.args = args;
         parse();
    }

    private void parse(){
        Iterator<String> strIter = Arrays.asList(args).iterator();
        while (strIter.hasNext()){
            String key = strIter.next();
            if(strIter.hasNext()){
                values.put(key, strIter.next());
            }else{
                values.put(key, "NULL");
            }
        }
    }

}
