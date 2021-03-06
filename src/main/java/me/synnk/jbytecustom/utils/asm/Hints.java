package me.synnk.jbytecustom.utils.asm;

public class Hints {
    public final static String[] hints = new String[256];

    /**
     * By koivusa
     */
    static {
        hints[0] = "Do nothing";
        hints[1] = "Push null";
        hints[2] = "Push int constant -1";
        hints[3] = "Push int constant 0";
        hints[4] = "Push int constant 1";
        hints[5] = "Push int constant 2";
        hints[6] = "Push int constant 3";
        hints[7] = "Push int constant 4";
        hints[8] = "Push int constant 5";
        hints[9] = "Push the long constant 0 onto the operand stack.";
        hints[10] = "Push the long constant 1 onto the operand stack.";
        hints[11] = "Push the float constant 0.0 onto the operand stack.";
        hints[12] = "Push the float constant 1.0 onto the operand stack.";
        hints[13] = "Push the float constant 2.0 onto the operand stack.";
        hints[14] = "Push the double constant 0.0 onto the operand stack";
        hints[15] = "Push the double constant 1.0 onto the operand stack";
        hints[16] = "Push byte";
        hints[17] = "Push short";
        hints[18] = "Push item from constant pool";
        hints[19] = "Push item from constant pool";
        hints[20] = "Push long or double from constant pool";
        hints[21] = "Load int from local variable";
        hints[22] = "Load long from local variable";
        hints[23] = "Load float from local variable";
        hints[24] = "Load double from local variable";
        hints[25] = "Load reference from local variable";
        hints[26] = "Load int from local variable 0";
        hints[27] = "Load int from local variable 1";
        hints[28] = "Load int from local variable 2";
        hints[29] = "Load int from local variable 3";
        hints[30] = "Load long from local variable 0";
        hints[31] = "Load long from local variable 1";
        hints[32] = "Load long from local variable 2";
        hints[33] = "Load long from local variable 3";
        hints[34] = "Load float from local variable 0";
        hints[35] = "Load float from local variable 1";
        hints[36] = "Load float from local variable 2";
        hints[37] = "Load float from local variable 3";
        hints[38] = "Load double from local variable 0";
        hints[39] = "Load double from local variable 1";
        hints[40] = "Load double from local variable 2";
        hints[41] = "Load double from local variable 3";
        hints[42] = "Load reference from local variable 0";
        hints[43] = "Load reference from local variable 1";
        hints[44] = "Load reference from local variable 2";
        hints[45] = "Load reference from local variable 3";
        hints[46] = "Load int from array";
        hints[47] = "Load long from array";
        hints[48] = "Load float from array";
        hints[49] = "Load double from array";
        hints[50] = "Load reference from array";
        hints[51] = "Load byte or boolean from array";
        hints[52] = "Load char from array";
        hints[53] = "Load short from array";
        hints[54] = "Store int into local variable";
        hints[55] = "Store long into local variable";
        hints[56] = "Store float into local variable";
        hints[57] = "Store double into local variable";
        hints[58] = "Store reference into local variable";
        hints[59] = "Store int into local variable 0";
        hints[60] = "Store int into local variable 1";
        hints[61] = "Store int into local variable 2";
        hints[62] = "Store int into local variable 3";
        hints[63] = "Store long into local variable 0";
        hints[64] = "Store long into local variable 1";
        hints[65] = "Store long into local variable 2";
        hints[66] = "Store long into local variable 3";
        hints[67] = "Store float into local variable 0";
        hints[68] = "Store float into local variable 1";
        hints[69] = "Store float into local variable 2";
        hints[70] = "Store float into local variable 3";
        hints[71] = "Store double into local variable 0";
        hints[72] = "Store double into local variable 1";
        hints[73] = "Store double into local variable 2";
        hints[74] = "Store double into local variable 3";
        hints[75] = "Store reference into local variable 0";
        hints[76] = "Store reference into local variable 1";
        hints[77] = "Store reference into local variable 2";
        hints[78] = "Store reference into local variable 3";
        hints[79] = "Store into int array";
        hints[80] = "Store into long array";
        hints[81] = "Store into float array";
        hints[82] = "Store into double array";
        hints[83] = "Store into reference array (..., arrayref, index -> ..., value)";
        hints[84] = "Store into byte or boolean array";
        hints[85] = "Store into char array";
        hints[86] = "Store into short array";
        hints[87] = "Pop top operand stack word";
        hints[88] = "Pop top two operand stack words";
        hints[89] = "Duplicate top operand stack word";
        hints[90] = "Duplicate the top operand stack value and insert two values down";
        hints[91] = "Duplicate the top operand stack value and insert two or three values down";
        hints[92] = "Duplicate the top one or two operand stack values";
        hints[93] = "Duplicate the top one or two operand stack values and insert two or three values down";
        hints[94] = "Duplicate the top one or two operand stack values and insert two, three, or four values down";
        hints[95] = "Swap top two operand stack words";
        hints[96] = "Add int";
        hints[97] = "Add long";
        hints[98] = "Add float";
        hints[99] = "Add long";
        hints[100] = "Subtract int";
        hints[101] = "Subtract long";
        hints[102] = "Subtract float";
        hints[103] = "Subtract double";
        hints[104] = "Multiply int";
        hints[105] = "Multiply long";
        hints[106] = "Multiply float";
        hints[107] = "Multiply double";
        hints[108] = "Divide int";
        hints[109] = "Divide long";
        hints[110] = "Divide float";
        hints[111] = "Divide double";
        hints[112] = "Remainder int";
        hints[113] = "Remainder long";
        hints[114] = "Remainder float";
        hints[115] = "Remainder double";
        hints[116] = "Negate int";
        hints[117] = "Negate long";
        hints[118] = "Negate float";
        hints[119] = "Negate double";
        hints[120] = "Shift left int";
        hints[121] = "Shift left";
        hints[122] = "Arithmetic shift right int";
        hints[123] = "Arithmetic shift right long";
        hints[124] = "Logical shift right int";
        hints[125] = "Logical shift right long";
        hints[126] = "Boolean AND int";
        hints[127] = "Boolean AND long";
        hints[128] = "Boolean OR int";
        hints[129] = "Boolean OR long";
        hints[130] = "Boolean XOR int";
        hints[131] = "Boolean XOR long";
        hints[132] = "Increment local variable by constant";
        hints[133] = "Convert int to long";
        hints[134] = "Convert int to float";
        hints[135] = "Convert int to double";
        hints[136] = "Convert long to int";
        hints[137] = "Convert long to float";
        hints[138] = "Convert long to double";
        hints[139] = "Convert float to int";
        hints[140] = "Convert float to long";
        hints[141] = "Convert float to double";
        hints[142] = "Convert double to int";
        hints[143] = "Convert double to long";
        hints[144] = "Convert double to float";
        hints[145] = "Convert int to byte";
        hints[146] = "Convert int to char";
        hints[147] = "Convert int to short";
        hints[148] = "Compare long";
        hints[149] = "Compare float";
        hints[150] = "Compare float";
        hints[151] = "Compare double";
        hints[152] = "Compare double";
        hints[153] = "Branch if int comparison with zero succeeds(if int is zero)";
        hints[154] = "Branch if int comparison with zero succeeds(if int is not zero)";
        hints[155] = "Branch if int comparison with zero succeeds(if int is lower than zero)";
        hints[156] = "Branch if int comparison with zero succeeds(if int is greater than or equal to zero)";
        hints[157] = "Branch if int comparison with zero succeeds(if int is greater than zero)";
        hints[158] = "Branch if int comparison with zero succeeds(if int is lower than or equal to zero)";
        hints[159] = "Branch if int comparison(equals) succeeds.";
        hints[160] = "Branch if int comparison(not equals) succeeds.";
        hints[161] = "Branch if int comparison(lower than) succeeds.";
        hints[162] = "Branch if int comparison(greater than or equal) succeeds.";
        hints[163] = "Branch if int comparison(greater than) succeeds.";
        hints[164] = "Branch if int comparison(lower than or equal) succeeds.";
        hints[165] = "Branch if reference comparison(equals) succeeds.";
        hints[166] = "Branch if reference comparison(not equals) succeeds.";
        hints[167] = "Branch always";
        hints[168] = "Jump subroutine";
        hints[169] = "Return from subroutine";
        hints[170] = "Access jump table by index and jump";
        hints[171] = "Access jump table by key match and jump";
        hints[172] = "Return int from method";
        hints[173] = "Return long from method";
        hints[174] = "Return float from method";
        hints[175] = "Return float from method";
        hints[176] = "Return reference from method";
        hints[177] = "Return void from method";
        hints[178] = "Get static field from class";
        hints[179] = "Set static field in class";
        hints[180] = "Fetch field from object";
        hints[181] = "Set field in object";
        hints[182] = "Invoke instance method";
        hints[183] = "Invoke instance method; special handling for superclass, private, and instance initialization method invocations";
        hints[184] = "Invoke a class (static) method";
        hints[185] = "Invoke interface method";
        hints[186] = "Invoke dynamic method";
        hints[187] = "Create new object";
        hints[188] = "Create new array";
        hints[189] = "Create new array of reference";
        hints[190] = "Get length of array";
        hints[191] = "Throw exception";
        hints[192] = "Check whether object is of given type";
        hints[193] = "Determine if object is of given type";
        hints[194] = "Enter monitor for object";
        hints[195] = "Exit monitor for object";
        hints[196] = "(wide) Increment local variable by constant";
        hints[197] = "Create new multidimensional array";
        hints[198] = "Branch if reference is null";
        hints[199] = "Branch if reference not null";
        hints[200] = "Branch always(wide index)";
        hints[201] = "Jump subroutine(wide index)";
        hints[202] = "(Reserved)breakpoint";
        hints[203] = "???";
        hints[204] = "???";
        hints[205] = "Push long or double from constant pool";
        hints[206] = "???";
        hints[207] = "???";
        hints[208] = "???";
        hints[209] = "???";
        hints[210] = "Get static field from class";
        hints[211] = "???";
        hints[212] = "???";
        hints[213] = "???";
        hints[214] = "???";
        hints[215] = "???";
        hints[216] = "???";
        hints[217] = "???";
        hints[218] = "???";
        hints[219] = "???";
        hints[220] = "";
        hints[221] = "???";
        hints[222] = "???";
        hints[223] = "???";
        hints[224] = "Check whether object is of given type";
        hints[225] = "???";
        hints[226] = "???";
        hints[227] = "???";
        hints[228] = "???";
        hints[229] = "";
        hints[230] = "";
        hints[231] = "";
        hints[232] = "";
        hints[233] = "";
        hints[234] = "";
        hints[235] = "";
        hints[236] = "";
        hints[237] = "";
        hints[238] = "";
        hints[239] = "";
        hints[240] = "";
        hints[241] = "";
        hints[242] = "";
        hints[243] = "";
        hints[244] = "";
        hints[245] = "";
        hints[246] = "";
        hints[247] = "";
        hints[248] = "";
        hints[249] = "";
        hints[250] = "";
        hints[251] = "";
        hints[252] = "";
        hints[253] = "";
        hints[254] = "(Reserved) impdep1";
        hints[255] = "(Reserved) impdp2";
    }

}
