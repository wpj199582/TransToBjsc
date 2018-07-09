package com.io;
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
        String line="12345678.java";
        int n=5;
        System.out.println(line.substring(0,line.length()-n));
        System.out.println(line.substring(0,line.length()-n)+".bjsc");
    }
}
