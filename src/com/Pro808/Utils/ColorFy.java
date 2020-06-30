package com.Pro808.Utils;

import com.Pro808.Token.Token;

import java.awt.image.TileObserver;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.StringConcatFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class ColorFy {

    private ArrayList<Token> tokens;
    private String path;
    private int tokenErrorPos = -10;
    private int curToken = 0;
    private int curScope = 0;
    public ColorFy(ArrayList<Token> tokens, String path){
        this.tokens = tokens;
        this.path = path;
        try {
            generateFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ColorFy(ArrayList<Token> tokens, String path, int tokenErrorPos){
        this.tokens = tokens;
        this.path = path;
        this.tokenErrorPos = tokenErrorPos;
        try {
            generateFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFile() throws IOException{
        String[] arrPath = path.split("/");
        String fileName = arrPath[arrPath.length-1];
        String[] arrFileName = fileName.split("[.]");
        arrFileName[1] = "html";
        String newFileName = String.join(".", arrFileName);

        arrPath[arrPath.length-1] = newFileName;
        path = String.join("/", arrPath);

        File file = new File(path);
        BufferedWriter wr = new BufferedWriter(new FileWriter(file));
        if(file.exists()){
            wr.write("");
            wr.close();
            writeToFile();
        }else{
            file.createNewFile();
            writeToFile();
        }
    }

    private void writeToFile() throws IOException{
        File file = new File(path);
        BufferedWriter wr = new BufferedWriter(new FileWriter(file));
        StringBuilder str = new StringBuilder();

        str.append("<html>\n" +
                "<head>\n" +
                "<style rel=\"stylesheet\">\n" +
                "        .token_"+tokenErrorPos+"{\n" +
                "            color: red !important;\n" +
                "        }\n" +
                "    </style>" +
                "</head>\n" +
                "<body style=\"background: #2B2B2B;\">\n");

        for (Token token: tokens
             ) {
            String scTemp = token.getAttrib().get("scope");

            if(scTemp != null){
                curScope = Integer.parseInt(scTemp);
            }else{
                curScope = 0;
            }
            switch (token.getType()){
                case KW_Int:
                case KW_String:
                case KW_Bool:
                case KW_List:{
                    str.append(createSpan("blue", addTabs() + token.getAttrib().get("name")));
                    break;
                }
                case KW_Num_Value:{
                    str.append(createSpan("#6897BBFF;", token.getAttrib().get("numValue")));
                    break;
                }
                case KW_String_Value:{
                    str.append(createSpan("#6897BBFF;", token.getAttrib().get("stringValue")));
                    break;
                }
                case KW_Bool_Value:{
                    str.append(createSpan("#6897BBFF;", token.getAttrib().get("boolValue")));
                    break;
                }
                case KW_If:{
                    str.append(createSpan("orange", addTabs() + "if"));
                    break;
                }
                case KW_For:{
                    str.append(createSpan("orange", addTabs() + "for"));
                    break;
                }
                case KW_While:{
                    str.append(createSpan("orange", addTabs() + "while"));
                    break;
                }
                case KW_Assign:
                {
                    str.append(createSpan("#FFE959", "="));
                    break;
                }
                case KW_Op_Pow:
                {
                    str.append(createSpan("#FFE959", "**"));
                    break;
                }
                case KW_Op_Plus:
                {
                    str.append(createSpan("#FFE959", "+"));
                    break;
                }
                case KW_Op_Minus:
                {
                    str.append(createSpan("#FFE959", "-"));
                    break;
                }
                case KW_Op_Div:
                {
                    str.append(createSpan("#FFE959", "/"));
                    break;
                }
                case KW_Op_Mul:{
                    str.append(createSpan("#FFE959", "*"));
                    break;
                }
                case KW_Eq:{
                    str.append(createSpan("#FFE959", "=="));
                    break;
                }
                case KW_Low:{
                    str.append(createSpan("#FFE959", "<"));
                    break;
                }
                case KW_More:{
                    str.append(createSpan("#FFE959", ">"));
                    break;
                }
                case KW_Round_Open_Bracket:{
                    str.append(createSpan("#FFE959", "("));
                    break;
                }
                case KW_Round_Close_Bracket:{
                    str.append(createSpan("#FFE959", ")"));
                    break;
                }
                case KW_Quotes:{
                    str.append(createSpan("#FFE959", "\""));
                    break;
                }
                case KW_Name:{
                    str.append(createSpan("#00AC00", token.getAttrib().get("name")));
                    break;
                }
                case KW_End:{
                    str.append(createSpan("orange", ";"));
                    str.append("<br/>\n");
                    break;
                }
                case KW_Figure_Open_Bracket:{
                    str.append(createSpan("orange",  "{"));
                    str.append("<br/>\n");
                    break;
                }
                case KW_Figure_Close_Bracket:{
                    str.append(createSpan("orange", addTabs() + "}"));
                    str.append("<br/>\n");
                    break;
                }
                case KW_Access:{
                    str.append(createSpan("orange",  "."));
                    break;
                }
            }
            curToken++;
        }
        str.append("</body>\n");
        str.append("</html>\n");
        wr.write(str.toString());
        wr.close();
    }

    private String createSpan(String color, String text){
        StringBuilder span = new StringBuilder();
        span.append("<span class=\"token_"+curToken+"\" style=\"color:" + color + "\">" + text + "</span>\n");
        return span.toString();
    }

    private String addTabs(){
        return "\u00A0\u00A0\u00A0\u00A0".repeat(curScope);
    }

}
