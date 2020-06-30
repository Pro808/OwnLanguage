package com.Pro808.Token;

public enum TypeToken {
    KW_Int(1),
    KW_String(2),
    KW_Bool(3),

    KW_Name(4),

    KW_Num_Value(5),
    KW_String_Value(6),
    KW_Bool_Value(7),
    KW_True(8),
    KW_False(9),

    KW_Assign(10),

    KW_Op_Plus(11),
    KW_Op_Minus(12),
    KW_Op_Div(13),
    KW_Op_Mul(14),
    KW_Op_Pow(15),

    KW_For(16),
    KW_If(17),
    KW_Else(18),
    KW_While(19),

    KW_Quotes(20),
    KW_Round_Open_Bracket(21),
    KW_Round_Close_Bracket(22),
    KW_Figure_Open_Bracket(23),
    KW_Figure_Close_Bracket(24),

    KW_More(25),
    KW_Low(26),
    KW_Eq(27),

    KW_End(28),
    KW_Print(29),
    KW_Error(30),
    KW_List(31),
    KW_Access(32),
    KW_Return(33),
    KW_Void(34);

    private int type;

    TypeToken(Integer type) {
        this.type = type;
    }

}