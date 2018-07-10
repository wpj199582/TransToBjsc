package com.io;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class demo {

    public static void main(String[] args) {

//        String line="  public TripDTO() {";
//        String tmp=StringUtils.trimToNull(line);
//        System.out.println(tmp.split(" ").length);
//        //System.out.println(StringUtils.substringAfter(StringUtils.substringBefore(tmp,"implements"),"public"));
        LinkedList<String> list=new LinkedList<>();
        list.add("a");
        list.add("a");
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        System.out.println(list.toString());
        list.addFirst("nihaoya");
        System.out.println("\n");
        System.out.println(list.toString());

        System.out.println();
//        String s1=" /**";
//        String s2="  * 国内系统机票订单";
//        String s3="  *";
//        String aa="abcdEFGD";
//        System.out.println(aa.toLowerCase());
//        //System.out.println(isEnumValueCommit(s1,s2,s3));
//        String test="list<PolicyUnitFcaInfoDTO>";
//        System.out.println(StringUtils.substringBefore(StringUtils.substringAfter(test,"<"),">"));

}
}
