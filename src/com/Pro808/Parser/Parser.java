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
        if (posToken > tokens.size() - 1) {
            posToken--;
        } else {
            currentToken = tokens.get(posToken);
        }
        currentToken.getAttrib().put("scope", Integer.toString(curScope));
        return currentToken;
    }

    private void setTokenPos(int pos) {
        currentToken = tokens.get(pos);
        posToken = pos;
    }

    private TypeToken typeToken() {
        return currentToken.getType();
    }

    private Integer posToken() {
        return Integer.parseInt(currentToken.getAttrib().get("pos"));
    }

    private TypeToken typeNextToken() {
        int temp = posToken + 1;
        if (temp > tokens.size() - 1) {
            temp = tokens.size() - 1;
            return tokens.get(temp).getType();
        } else {
            return tokens.get(temp).getType();
        }
    }

    /*        program -> expr*/
    public void program() throws LanguageException {
        while (posToken < tokens.size() - 1) {
            if (!expr()) {
                throw exception("Ошибка ! ");
            }
        }
    }

    /*
            expr -> assign_Expr | while_statement | for_statement | if_statement
     */
    private boolean expr() throws LanguageException {
        nextToken();
        System.out.println("Следующий токен: " + currentToken);
        if (!isDecl() && !isIf() && !isFor()) {
            System.out.println("Expr return false;");
            return false;
        } else {
            System.out.println("Обработано токенов " + posToken + " из " + (tokens.size() - 1));
            return true;
        }
    }

    /*
    declr -> var KW_Assign assign_Expr | var KW_End
     */
    private boolean isDecl() throws LanguageException {
        if (isVar()) {
            Variable temp = getLastVar();
            if (typeToken() == TypeToken.KW_Assign) {
                if (isAssign_Expr(temp)) {
                    nextToken();
                    if (typeToken() == TypeToken.KW_End) {
                        return true;
                    } else {
                        throw exception("Ожидалась ; : " + posToken());
                    }
                } else {
                    throw exception("Ожидалась операция присваения с объявлением переменной: " + posToken());
                }
            } else if (typeToken() == TypeToken.KW_End) {
                return true;
            } else {
                throw exception("Ожидалась операция объявления переменной: " + posToken());
            }
        } else {
            System.out.println("Decl return false;");
            return false;
        }
    }

    private boolean isIf() throws LanguageException {
        if (typeNextToken() == TypeToken.KW_If) {
            nextToken();
        }
        if (typeToken() == TypeToken.KW_If) {
            nextToken();
            if (typeToken() == TypeToken.KW_Round_Open_Bracket) {
                nextToken();
                if (isBool().reduce().getType() == TypeToken.KW_Bool_Value) {
                    nextToken();
                    if (typeToken() == TypeToken.KW_Round_Close_Bracket) {
                        nextToken();
                        if (typeToken() == TypeToken.KW_Figure_Open_Bracket) {
                            curScope++;
                            while (expr() == true) {
                                continue;
                            }
                            System.out.println("Текущий токен: " + currentToken);
                            if (typeToken() == TypeToken.KW_End) {
                                nextToken();
                            }
                            if (typeToken() == TypeToken.KW_Figure_Close_Bracket) {
                                curScope--;
                                return true;
                            } else {
                                throw exception("Не удалось найти конец тела If");
                            }
                        } else {
                            throw exception("Не удалось найти тело If");
                        }
                    } else {
                        throw exception("Нету закрывающей скобки выражения If");
                    }
                } else {
                    throw exception("В If возможно только булево выражение: " + posToken());
                }
            } else {
                throw exception("В If ожидалась ( перед логическим выражением: " + posToken());
            }
        } else {
            System.out.println("If return false;");
            return false;
        }
    }

    private boolean isFor() throws LanguageException {
        if (typeToken() == TypeToken.KW_For) {
            nextToken();
            if (typeToken() == TypeToken.KW_Round_Open_Bracket) {
                nextToken();
                if (isDecl()) {
                    nextToken();
                    if (isBool().reduce().getType() == TypeToken.KW_Bool_Value) {
                        nextToken();
                        if (typeToken() == TypeToken.KW_End) {
                            nextToken();
                            if (isDecl()) {
                                nextToken();
                                if (typeToken() == TypeToken.KW_Round_Close_Bracket) {
                                    nextToken();
                                    if (typeToken() == TypeToken.KW_Figure_Open_Bracket) {
                                        curScope++;
                                        while (expr()) {
                                        }{}
                                        if (typeToken() == TypeToken.KW_End) {
                                            nextToken();
                                        }
                                        if (typeToken() == TypeToken.KW_Figure_Close_Bracket) {
                                            curScope--;
                                            return true;
                                        } else {
                                            throw exception("Не удалось найти конец тела for");
                                        }

                                    } else {
                                        throw exception("Не удалось найти начало тела for");
                                    }
                                } else {
                                    throw exception("Не удалось найти конец выражения for. Ожидалась ;");
                                }
                            } else {
                                throw exception("ожидалось выражения цикла for");
                            }
                        } else {
                            throw exception("Ожидалась точка с запятой");
                        }
                    } else {
                        throw exception("Условие выполнения цикла for должно иметь тип логики ");
                    }
                } else {
                    throw exception("Ожидалось объявление переменной дляя цикла for");
                }
            } else {
                throw exception("Ожидалась (  после for");
            }
        } else {
            System.out.println("For return false;");
            return false;
        }
    }

    /*
     assign_Expr ->  constant_Expr | value KW_End
     */
    private boolean isAssign_Expr(Token assignToken) throws LanguageException {
        nextToken();
//        System.out.println("isAssignExpr получила токен под номером: " + posToken);
        Token temp = isBool().reduce();
        System.out.println("isAssignExpr ответ: " + temp + " Для: " + assignToken);
        switch (assignToken.getType()) {
            case KW_Int: {
                if (temp.getType() == TypeToken.KW_Num_Value) {
                    System.out.println("Ответ числового выражения: " + temp.getAttrib().get("numValue"));
                    assignToken.getAttrib().put("numValue", temp.getAttrib().get("numValue"));
                    return true;
                } else {
                    setTokenPos(Integer.parseInt(assignToken.getAttrib().get("tokenNumber")));
                    throw exception("Неправильное приведение типов: " + assignToken.getAttrib().get("pos"));
                }
            }
            case KW_Bool: {
                if (temp.getType() == TypeToken.KW_Bool_Value) {
                    System.out.println("Ответ логического выражения: " + temp.getAttrib().get("boolValue"));
                    assignToken.getAttrib().put("boolValue", temp.getAttrib().get("boolValue"));
                    return true;
                } else {
                    setTokenPos(Integer.parseInt(assignToken.getAttrib().get("tokenNumber")));
                    throw exception("Неправильное приведение типов: " + assignToken.getAttrib().get("pos"));
                }
            }
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
    private Token isBool() throws LanguageException {
//        System.out.println("isBool получила токен: " + currentToken);
        Token x = isLeft();
        while (typeNextToken() == TypeToken.KW_Eq || typeNextToken() == TypeToken.KW_Low || typeNextToken() == TypeToken.KW_More) {
            Token next = nextToken();
            nextToken();
            x = new Logic(next, x, isRight());
            if (x.getType() == TypeToken.KW_Error) {
                throw exception(x.getOpToken().getAttrib().get("name") + " " + currentToken.getAttrib().get("pos"));
            }
        }
//        System.out.println("isBool вернуло токен: " + x);
        return x;
    }

    private Token isLeft() throws LanguageException {
//        System.out.println("isLeft получила токен: " + currentToken);
        Token x = isRight();
        while (typeNextToken() == TypeToken.KW_Op_Plus || typeNextToken() == TypeToken.KW_Op_Minus) {
            Token next = nextToken();
            nextToken();
            x = new Arith(next, x, isRight());
            if (x.getType() == TypeToken.KW_Error) {
                throw exception(x.getOpToken().getAttrib().get("name") + " " + currentToken.getAttrib().get("pos"));
            }
        }
//        System.out.println("isLeft вернуло токен: " + x);
        return x;
    }

    private Token isRight() throws LanguageException {
//        System.out.println("isRight получила токен: " + currentToken);
        Token x = isUnary();
        while (typeNextToken() == TypeToken.KW_Op_Mul || typeNextToken() == TypeToken.KW_Op_Div || typeNextToken() == TypeToken.KW_Op_Pow) {
            Token next = nextToken();
            nextToken();
            x = new Arith(next, x, isUnary());
            if (x.getType() == TypeToken.KW_Error) {
                throw exception(x.getOpToken().getAttrib().get("name") + " " + currentToken.getAttrib().get("pos"));
            }
        }
//        System.out.println("isRight вернуло токен: " + x);
        return x;
    }

    private Token isUnary() throws LanguageException {
//        System.out.println("isUnary получила токен: " + currentToken);
        Token x = currentToken;
        if (x.getType() == TypeToken.KW_Op_Minus) {
            nextToken();
            x = new Unary(x, isUnary());
            if (x.getType() == TypeToken.KW_Error) {
                throw exception(x.getOpToken().getAttrib().get("name") + " " + currentToken.getAttrib().get("pos"));
            }
            return x;
        } else {
            return isValue();
        }
    }

    private Token isValue() throws LanguageException {
//        System.out.println("isValue получила токен: " + currentToken);
        switch (typeToken()) {
            case KW_Round_Open_Bracket: {
                nextToken();
                Token x = isBool();
                nextToken();
                if (typeToken() == TypeToken.KW_Round_Close_Bracket) {
                    return x;
                } else {
                    throw exception("Нету закрывающей скобки: " + posToken());
                }
            }
            case KW_String_Value:
            case KW_Bool_Value:
            case KW_Num_Value: {
//                System.out.println("isValue вернуло число: " + currentToken);
                return currentToken;
            }
            case KW_Name: {
                if (existVar(currentToken.getAttrib().get("name"))) {
                    Token clone = vars.get(curVar);
                    Token temp = new Token(clone.getType(), "temp", "temp");
                    temp.cloneAttr(clone.getAttrib());
//                    System.out.println("Получили перменную по имени: " + temp);
                    if (temp.getType() == TypeToken.KW_Int) {
                        temp.setType(TypeToken.KW_Num_Value);
                    }
                    if (temp.getType() == TypeToken.KW_Bool) {
                        temp.setType(TypeToken.KW_Bool_Value);
                    }
                    return temp;
                } else {
                    throw exception("Использованная переменная в выражении не определена: " + posToken());
                }
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
                    throw exception("Не удалось инициализировать переменную типа int: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_Int, temp);
                        return true;
                    } else {
                        createVar(TypeToken.KW_Int, temp);
                        return true;
                    }
                }
            }
            case KW_String: {
                nextToken();
                if (typeToken() != TypeToken.KW_Name) {
                    throw exception("Не удалось инициализировать переменную типа String: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_String, temp);
                        return true;
                    } else {
                        createVar(TypeToken.KW_String, temp);
                        return true;
                    }
                }
            }
            case KW_Bool: {
                nextToken();
                if (typeToken() != TypeToken.KW_Name) {
                    throw exception("Не удалось инициализировать переменную типа bool: " + posToken());
                } else if (existVar(currentToken.getAttrib().get("name"))) {
                    throw exception("Повторная инициализация переменной: " + posToken());
                } else {
                    Token temp = currentToken;
                    nextToken();
                    if (isClose()) {
                        createVar(TypeToken.KW_Bool, temp);
                        return true;
                    } else {
                        createVar(TypeToken.KW_Bool, temp);
                        return true;
                    }
                }
            }
            case KW_Name: {
                if (existVar(currentToken.getAttrib().get("name"))) {
                    nextToken();
                    return true;
                } else {
                    throw exception("Переменная не инициализирована: " + posToken());
                }
            }
            default:
                return false;
        }
    }

    private void createVar(TypeToken type, Token tok) {
        Variable temp = new Variable(type, "name", tok.getAttrib().get("name"));
        temp.cloneAttr(tok.getAttrib());
        temp.addAttr("value", "NULL");
        temp.addAttr("scope", Integer.toString(curScope));
        temp.addAttr("tokenNumber", Integer.toString(posToken - 1));
        vars.add(temp);
        curVar = vars.indexOf(temp);
    }

    private boolean existVar(String name) {
        for (Variable var : vars) {
            if (var.getAttrib().get("name").equals(name) && Integer.parseInt(var.getAttrib().get("scope")) <= curScope) {
                curVar = vars.indexOf(var);
                return true;
            }
        }
        return false;
    }

    private Variable getLastVar() {
        return vars.get(curVar);
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

    public ArrayList<Token> getTokens() {
        return tokens;
    }

}
