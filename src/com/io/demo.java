package com.io;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
public class demo {
    public static void main(String[] args) {
//        String path="D:\\mygitwork\\TransToBjsc\\src\\com\\frame";
//        File file=new File(path);
//        String[] lists=file.list();
//        File[] files=file.listFiles();
//        for (String str:lists) {
//            System.out.println(str);
//        }
//        for (File f:files){
//            if(f.isFile()&&f.getName().endsWith(".java")){
//                System.out.println(f.getName());
//
//            }
//
//        }
        String line="  public TripDTO() {";
        String tmp=StringUtils.trimToNull(line);
        System.out.println(tmp.split(" ").length);
        //System.out.println(StringUtils.substringAfter(StringUtils.substringBefore(tmp,"implements"),"public"));


    }
}
