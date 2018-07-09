package com.io;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUtil {
    public  static  File inputDir;
    public  static File outputDir;
    //测试...
    public static void Fileshow(File file){
          File[] files=file.listFiles();

          for(File subfile:files){
              //如果是文件夹，递归调用
              if(subfile.isDirectory())
                  Fileshow(subfile);
              else{
                    if(subfile.isFile()&&subfile.getName().endsWith(".java"))
                        System.out.println(subfile.getName());
              }
          }
    }

    /**
     * 读取文件，并将转换后的结果写出到指定文件夹
     */
    public  static void startTransTo(){

                File inputDir=FileUtil.inputDir;
                File outputDir=FileUtil.outputDir;
                File[] files=inputDir.listFiles();
              try{
                  //开始遍历input目录
                  for(File subfile:files){
                      if(subfile.isFile()&&subfile.getName().endsWith(".java")){
                          BufferedReader   bufferedReader = new BufferedReader(new FileReader(subfile));
                          String newName=subfile.getName().substring(0,subfile.getName().length()-5)+".bjsc";//这里输出文件的后缀要改为bjsc
                          BufferedWriter  bufferedWriter=new BufferedWriter(new FileWriter(new File(outputDir,newName)));//输出流
                          List<String> fileLines=new ArrayList<>();//存储文件中数据
                          String  line;
                          while((line=bufferedReader.readLine())!=null){
                              fileLines.add(line);
                          }
                          //调用转换函数
                          List<String> newlist=bjscFun(fileLines);
                          //写出到文件
                          for (String str:newlist){
                              bufferedWriter.write(str);
                              bufferedWriter.newLine();
                          }
                          //关闭io流
                          bufferedReader.close();
                          bufferedWriter.close();
                      }
                  }

              }catch (Exception e){
                  e.printStackTrace();
              }
    }
    /**
     * 将集合中的数据按照bjsc语法转换
     */
    public static List<String> bjscFun(List<String> list){
        List<String> newlist=new ArrayList<>();
        //添加文件包含
        newlist.add("include 'BaijiCommonTypes.bjsc'");
        //添加命名空间namespace定义
        Iterator<String> iterator=list.iterator();
        Map<Integer,String> map=new HashMap<>();
        int index=1;
        //存入map，行号，行的内容
        for(String str:list){
            map.put(index,str);
            index++;
        }
        int count=1;
        while(iterator.hasNext()){
            //拿到一整行字符串
            String line=iterator.next();
            //namespace
            if(line.startsWith("package")){
                String tmp=StringUtils.deleteWhitespace(line);
                String namespace= "namespace java "+"'"+tmp.substring(7,tmp.length()-1)+"'";
                newlist.add(namespace);
            }
            //转换枚举定义
            else if(StringUtils.contains(line,"public enum")){
                String tmp=StringUtils.deleteWhitespace(line);
                String enumDef="enum "+tmp.substring(10,tmp.length());
                newlist.add(enumDef);
            }
            //添加枚举的值，java枚举类转换bjsc转换完毕
            else if(StringUtils.contains(line,"@XmlEnumValue")){
                 newlist.add("    "+map.get(count+1));//当有@XmlEnumValue时，将下面一行加入
            }
            //class类
            else if(StringUtils.contains(line,"public class")){

            }
            count++;
        }

        newlist.add("}");
        return newlist;
    }
}
