package com.Pro808.Parser;

   /*

    Определение грамматики:

    expr - выражение
    stmt - название выражения - это может быть просто число, либо число и операторы
    expr, stmt - нетерминалы
    KeyWord - название терминала
    -> это продукция или "может иметь вид"


    Для хранения типа переменной в ведем следующие терминалы:
    KW_Int - тип переменной int
    KW_String - тип переменной string
    KW_Bool - тип переменной bool
    KW_Int_Value - тип переменной int
    KW_String_Value - тип переменной string
    KW_Bool_Value - тип переменной bool
    KW_Name - имя переменной

    KW_End - конец
                                                   и нетерминалы:
    var -> KW_Int KW_Name| KW_String KW_Name ... KW_End


    Для основной операции присваения = введем следующие терминалы:
    KW_Assign - присвоение
                                                       и нетерминалы:
    declr -> var KW_Assign assign_Expr | var KW_End
    constant_Expr -> (KW_Int_Value | KW__Quotes KW_String_Value  KW__Quotes| KW_Bool_Value) KW_End
    assign_Expr ->  constant_Expr | value KW_End


    Для основных операций(инструкций) в языке как +, -, *, /, ** введем следующие терминалы:
        KW_( или KW_Round_Open_Bracket, и KW_( или KW_Round_Close_Bracket - определяют приоритетность;
        KW_Op_Plus
        KW_Op_Minus
        KW_Op_Div
        KW_Op_Mul
        KW_Op_Pow
                                                                 и нетерминалы:
        value - высшее приоритетное значение
            value -> KW_Num_Value | KW_True | KW_False | KW_Name |  KW_( left KW_) KW_End
        right - более приоритетное значение
            right -> right KW_Op_Mul value | right KW_Op_Div value | right KW_Op_Pow value | value
        left - менее преоритетное значение
            left -> left KW_Op_Plus right | left KW_Op_Minus right | right


    Для основых логических опреаций < и > введем следующие терминалы:
        KW_More
        KW_Low
        KW_Eq
                                                          и нетермналы:
        logic_Expr -> KW_Int_value (KW_More | KW_Low | KW_Eq) KW_Int_Value
        logic_value  -> KW_Bool_Value | logic_Expr
        KW_Bool_Value -> KW_True | KW_False

    Для основных инструкций языка как if, for, while введем следующие терминалы:
    KW_Open_Figure_Bracket - открывающая скобка
    KW_Close_Figure_Bracket - открывающая скобка
    KW_If - начало условие
    Kw_Else - обратное условие
    KW_For
    KW_While
    KW_If
                                                                    и нетерминалы:
    assign_Expr
    program
        if_statement -> KW_If KW_( logic_Expr KW_) KW_Open_Figure_Bracket expr KW_Close_Figure_Bracket
                 KW_Else KW_Open_Figure_Bracket expr KW_Close_Figure_Bracket
        for_statement -> KW_For KW_( assign_Expr KW_End logic_Expr KW_End value KW_End KW_) KW_Open_Figure_Bracket
                          expr
                         KW_Close_Figure_Bracket
        while_statement -> KW_While KW_( logic_Expr KW_)  KW_Open_Figure_Bracket
                           expr
                           KW_Close_Figure_Bracket
        expr -> declr | assign_Expr | while_statement | for_statement | if_statement

        program -> expr
     */


import com.Pro808.LanguageException;
import com.Pro808.Token.Token;
import com.Pro808.Token.TypeToken;

import java.util.ArrayList;
import java.util.Iterator;

public class Parser {

    private ArrayList<Variable> vars = new ArrayList<Variable>();
    private ArrayList<Token> tokens;
    private Token currentToken;

    private int posToken = -1;
    private int curScope = 0;
    private int curVar = 0;
    private LanguageException exception;

    public Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    private Token nextToken() throws LanguageException {
        posToken++;
        try{
            currentToken = tokens.get(posToken);
        } catch (Exception ex) {
            posToken--;
            throw exception("Ошибка обработки кода : " + posToken + " Tokens: " + tokens.size());
        }
        return currentToken;
    }

    private void setTokenPos(int pos) {
        posToken = pos;
        currentToken = tokens.get(pos);
    }

    private TypeToken typeToken() {
        return currentToken.getType();
    }

    private Integer posToken() {
        return Integer.parseInt(currentToken.getAttrib().get("pos"));
    }

    private TypeToken typeNextToken() {
        int temp = posToken + 1;
        return tokens.get(temp).getType();
    }

    /*        program -> expr*/
    public void program() throws LanguageException {
        while (posToken < tokens.size() - 1) {
            expr();
        }
    }

    /*
            expr -> assign_Expr | while_statement | for_statement | if_statement
     */
    private void expr() throws LanguageException {
        nextToken();
        if (!isDecl()) {
            throw exception("Не правильное написание начала кода");
        }
    }

    /*
    declr -> var KW_Assign assign_Expr | var KW_End
     */
    private boolean isDecl() throws LanguageException {
        if (isVar()) {
            Variable temp = getLastVar();
            if (typeToken() == TypeToken.KW_Assign) {
                if (isAssign_Expr(temp.getType())) {
                    nextToken();
                    return true;
                } else {
                    throw exception("Ожидалась операция присваения с объявлением переменной: " + posToken());
                }
            }else if(typeToken() == TypeToken.KW_End) {
                nextToken();
                return true;
            }else{
                throw exception("Ожидалась операция объявления переменной: " + posToken());
            }
        }
        return true;
    }

    /*
     assign_Expr ->  constant_Expr | value KW_End
     */
    private boolean isAssign_Expr(TypeToken assignType) throws LanguageException {
        int interPosToken = posToken;
        nextToken();
        System.out.println("isAssignExpr получила токен под номером: " + posToken);
       /* if (isConstant_Expr(assignType)) {
            return true;
        } else if (isValue()) {
            return true;
        } else {
            throw exception("Ожидалась операция присваения: " + posToken());
        }*/
       /* if (isConstant_Expr(assignType)) {
            return true;
        }*/
        Token temp = isLeft().reduce();
        switch (assignType){
            case KW_Int:{
                if(temp.getType() == TypeToken.KW_Num_Value){
                    System.out.println("Ответ числового выражения: " + temp.getAttrib().get("numValue"));
                    return true;
                }else{
                    throw exception("Неправильное приведение типов: " + posToken());
                }
            }
        }
        return false;
    }

    /*
        constant_Expr -> (KW_Int_Value | KW__Quotes KW_String_Value  KW__Quotes| KW_Bool_Value) KW_End
     */
    private boolean isConstant_Expr(TypeToken assignType) throws LanguageException {
        nextToken();
        switch (currentToken.getType()) {
            case KW_Num_Value: {
                if (assignType == TypeToken.KW_Int) {
                    nextToken();
                    if (isClose()) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    throw exception("Неправильное приведение типов : " + posToken());
                }
            }
            case KW_Quotes: {
                nextToken();
                if (assignType == TypeToken.KW_String && typeToken() == TypeToken.KW_String_Value) {
                    nextToken();
                    if (typeToken() == TypeToken.KW_Quotes) {
                        nextToken();
                        if (typeToken() == TypeToken.KW_End) {
                            return true;
                        } else {
                            throw exception("Ожидалась ; : " + posToken());
                        }
                    } else {
                        throw exception("Не удалось найти конец строки : " + posToken());
                    }
                } else {
                    throw exception("Неправильное приведение типов : " + posToken());
                }
            }
            default:
                System.out.println("Miss type in isConstant_Expr");
        }
        return false;
    }

    /*
            value - высшее приоритетное значение
            value -> KW_Num_Value | KW_True | KW_False | KW_Name |  KW_( left KW_) KW_End
        right - более приоритетное значение
            right -> right KW_Op_Mul value | right KW_Op_Div value | right KW_Op_Pow value | value
        left - менее преоритетное значение
            left -> left KW_Op_Plus right | left KW_Op_Minus right | right
     */
    private Token isLeft() throws LanguageException {
        System.out.println("isLeft получила токен: " + currentToken);
        Token x = isRight();
        while(typeNextToken() == TypeToken.KW_Op_Plus || typeNextToken() == TypeToken.KW_Op_Minus){
            Token next = nextToken();
            nextToken();
            x = new Arith(next, x, isRight());
        }
        System.out.println("isLeft вернуло токен: " + x);
        return x;
    }

    private Token isRight() throws LanguageException {
        System.out.println("isRight получила токен: " + currentToken);
        Token x = isValue();
        while(typeNextToken() == TypeToken.KW_Op_Mul || typeNextToken() == TypeToken.KW_Op_Div || typeNextToken() == TypeToken.KW_Op_Pow){
            Token next = nextToken();
            nextToken();
            x = new Arith(next, x, isRight());
        }
        System.out.println("isRight вернуло токен: " + x);
        return x;
    }

    private Token isValue() throws LanguageException {
        System.out.println("isValue получила токен: " + currentToken);
        switch (typeToken()){
            case KW_Round_Open_Bracket:{
                nextToken();
                isLeft();
                break;
            }
            case KW_Num_Value:{
                System.out.println("isValue вернуло число: " + currentToken);
                return currentToken;
            }
        }
        return null;
    }

    /*
        var -> KW_Int KW_Space KW_Name| KW_String KW_Name ... KW_End |
               KW_Int KW_Space KW_Name| KW_String KW_Name ...
     */
    private boolean isVar() throws LanguageException {
        switch (typeToken()) {
            case KW_Int: {
                nextToken();
                if (typeToken() != TypeToken.KW_Name) {
                    throw exception("Не удалось инициализировать переменную: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_Int, temp.getAttrib().get("name"), "NULL");
                        return false;
                    } else {
                        createVar(TypeToken.KW_Int, temp.getAttrib().get("name"), "NULL");
                        return true;
                    }
                }
            }
            case KW_String: {
                nextToken();
                if (typeToken() != TypeToken.KW_Name) {
                    throw exception("Не удалось инициализировать переменную: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_String, temp.getAttrib().get("name"), "NULL");
                    } else {
                        createVar(TypeToken.KW_String, temp.getAttrib().get("name"), "NULL");
                        return true;
                    }
                }
                break;
            }
            case KW_Bool:{
                nextToken();
                if (typeToken() != TypeToken.KW_Name) {
                    throw exception("Не удалось инициализировать переменную: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_Bool, temp.getAttrib().get("name"), "NULL");
                    } else {
                        createVar(TypeToken.KW_Bool, temp.getAttrib().get("name"), "NULL");
                        return true;
                    }
                }
                break;
            }
            case KW_Name:{
                if(existVar(currentToken.getAttrib().get("name"))){
                    nextToken();
                    return true;
                }else{
                    throw exception("Переменная не инициализирована: " + posToken());
                }
            }
            default:
              return false;
        }
        return false;
    }

    private void createVar(TypeToken type, String name, String value) {
        Variable temp = new Variable(type, "name", name);
        temp.addAttr("value", value);
        temp.addAttr("scope", Integer.toString(curScope));
        vars.add(temp);
    }

    private boolean existVar(String name) {
        for (Variable var : vars) {
            if (var.getAttrib().get("name").equals(name)) {
                return true;
            }
        }
        return false;
    }

    private Variable getLastVar() {
        return vars.get(vars.size() - 1);
    }

    private boolean isClose() {
        return typeToken() == TypeToken.KW_End;
    }

    private LanguageException exception(String text) {
        exception = new LanguageException(text);
        exception.setTokens(tokens);
        exception.setPosToken(posToken);
        return exception;
    }
}
