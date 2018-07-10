package com.io;

/**
 * 基本类型
 */
public enum BasicType {
       INTLOW("int"),
       INITHIGH("Integer"),
       BYTELOW("byte"),
       BYTEHIGH("Byte"),
       BYTEARRAY("byte[]"),
       SHORTLOW("short"),
       SHORTHIGH("Short"),
       LONGLOW("long"),
       LONGHIGH("Long"),
       BOOLLOW("boolean"),
       BOOLHIGH("Boolean"),
       STRING("String"),
       FLOATLOW("float"),
       FLOATHIGH("Float"),
       DUOBLELOW("double"),
       DUOBLEHIGH("Double"),
       BIGDECIMAL("BigDecimal"),
       CALENDAR("Calendar");

       private final String value;

    BasicType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isBasicType(String v){
        for(BasicType basicType:BasicType.values()){
            if(basicType.value.equals(v))
                return true;
        }
        return false;
    }
}
