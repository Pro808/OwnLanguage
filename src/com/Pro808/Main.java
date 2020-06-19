package com.Pro808;

import com.Pro808.Lexer.Lexer;
import com.Pro808.Parser.Parser;
import com.Pro808.Polk.Polis;
import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;
import com.Pro808.Utils.ColorFy;
import com.Pro808.Utils.ParseArgs;

import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        ParseArgs settings = new ParseArgs(args);
        boolean debug = Boolean.parseBoolean(settings.values.get("-debug"));
        String path = settings.values.get("-path");

        LoadProgram loader = new LoadProgram(path);
        Lexer lexer = new Lexer(loader.getProgram());

        lexer.debug(debug);

        if(debug) {
            System.out.println(loader.getProgram());
        }

        lexer.scan();

        if(debug) {
            lexer.getTokens().forEach((token) -> {
                TypeToken type = token.getType();
                HashMap<String, String> attrs = token.getAttrib();
                System.out.println("TypeToken: " + type.name());
                System.out.println("\tAttrs: {");
                attrs.forEach((key, value) -> {
                    System.out.println("\t\tKey: " + key.toString() + "|Value: " + value.toString());
                });
            });
        }

        Parser parser = new Parser(lexer.getTokens());

        try {
            parser.program();
        } catch (LanguageException e) {
            e.printStackTrace();
        }

        ColorFy coloredProgram = new ColorFy(parser.getTokens(), path);

        Polis polis = new Polis(parser.getTokens());

        polis.generate();

    }
}
