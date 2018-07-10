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
                          BufferedWriter  bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outputDir,newName)),"UTF-8"));//输出流
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
            //枚举类的注释
            else if(StringUtils.contains(line,"@XmlType")){
                newlist.add("    "+map.get(count-3));//当有@XmlType时，将上面的三行注释加入
                newlist.add("    "+map.get(count-2));//当有@XmlType时，将上面的三行注释加入
                newlist.add("    "+map.get(count-1));//当有@XmlType时，将上面的三行注释加入
                newlist.add("             ");
            }
            //转换枚举定义
            else if(StringUtils.contains(line,"public enum")){
                String tmp=StringUtils.trimToNull(line);
                String enumDef=tmp.substring(6,tmp.length());
                newlist.add(enumDef);
            }
            //添加枚举的值，要带上备注，java枚举类转换bjsc转换完毕
            else if(StringUtils.contains(line,"@XmlEnumValue")){
                 newlist.add("    "+map.get(count-3));//当有@XmlEnumValue时，将上面的三行注释加入
                 newlist.add("    "+map.get(count-2));//当有@XmlEnumValue时，将上面的三行注释加入
                 newlist.add("    "+map.get(count-1));//当有@XmlEnumValue时，将上面的三行注释加入
                 //newlist.add("             ");
                 String tmp=map.get(count+1);
                 if(!tmp.endsWith(";"))
                     newlist.add("    "+StringUtils.substringBefore(tmp,"(")+",");//当有@XmlEnumValue时，将下面一行加入,
                 else
                     newlist.add("    "+StringUtils.substringBefore(tmp,"("));//当有@XmlEnumValue时，将下面一行加入,
                 newlist.add("             ");
            }
            //class类定义的头部
            else if(StringUtils.contains(line,"public class")){
                String tmp=StringUtils.trimToNull(line);
                newlist.add(StringUtils.substringAfter(StringUtils.substringBefore(tmp,"implements"),"public")+"{");
            }

            //类、成员变量的注释
            else if(StringUtils.contains(line,"@FieldDoc")||StringUtils.contains(line,"@DtoDoc")){
                newlist.add(" /**");
                newlist.add(getCommit(line));
                newlist.add("  */");
                //newlist.add("             ");
            }
            //成员变量
            else if(StringUtils.contains(line,"private")||StringUtils.contains(line,"public")||StringUtils.contains(line,"protected")){
                String tmp=StringUtils.trimToNull(line);
                String[] strings=tmp.split(" ");
                //这里有很大问题，不能直接指定为3
                if(strings.length==3&&!"public".equals(strings[0])){
                    String member="   "+toLowerCase(strings[1])+" "+strings[2];
                    newlist.add(member);
                    newlist.add("             ");
                }
                String add="";
//                if(strings.length==7){
//
//                }

            }
            count++;
        }

        newlist.add("}");
        return newlist;
    }

    /**
     *将字符串首字母转换成小写
     * @param line
     * @return
     */
    public static  String toLowerCase(String line){
        if(Character.isLowerCase(line.charAt(0)))
            return line;
        else
            return (new StringBuilder()).append(Character.toLowerCase(line.charAt(0))).append(line.substring(1)).toString();
    }

    /**
     * 将类以及其成员变量的注释取出
     * @param line
     * @return
     */
    public static  String getCommit(String line){
        String tmp=StringUtils.trimToNull(line);
        tmp=tmp.replace("\"","");
        return "  *"+StringUtils.substringBefore(StringUtils.substringAfter(tmp,"("),")");
    }
}
