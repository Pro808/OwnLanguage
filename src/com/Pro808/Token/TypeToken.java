package com.Pro808.Token;

public enum TypeToken {
    KW_Int(1),
    KW_String(2),
    KW_Bool(3),

    KW_Name(3),

    KW_Num_Value(4),
    KW_String_Value (5),
    KW_Bool_Value (6),

    KW_Op_Plus(7),
    KW_Op_Minus(8),
    KW_Op_Div(9),
    KW_Op_Mul(10),
    KW_Op_Pow(11),

    KW_Assign(12),

    KW_If(13),
    KW_Else(14),
    KW_While(15),
    KW_For(16),
    KW_True(27),
    KW_False(28),
    KW_Quotes(17),
    KW_Round_Open_Bracket(18),
    KW_Round_Close_Bracket(19),
    KW_Figure_Open_Bracket(20),
    KW_Figure_Close_Bracket(21),

    KW_More(22),
    KW_Low(23),
    KW_Eq(24),

    KW_Print(29),

    KW_End(26);

    private int type;

    TypeToken(Integer type) {
        this.type = type;
    }

}