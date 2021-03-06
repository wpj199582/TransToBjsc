package com.io;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
    public  static void startTransTo(File inputDir,File outputDir){

//                File inputDir=FileUtil.inputDir;
//                File outputDir=FileUtil.outputDir;
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
                      else if(subfile.isDirectory()){
                          startTransTo(subfile,outputDir);
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
        LinkedList<String> newlist=new LinkedList<>();
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
                newlist.add("\n");
            }
            //转换枚举定义
            else if(StringUtils.contains(line,"public enum")){
                String tmp=StringUtils.trimToNull(line);
                String enumDef=tmp.substring(6,tmp.length());
                newlist.add(enumDef);
            }
            //添加枚举的值，要带上备注，java枚举类转换bjsc转换完毕
            else if(StringUtils.contains(line,"@XmlEnumValue")){
                //要更仔细一点
                 if(isEnumValueCommit(map.get(count-3),map.get(count-2),map.get(count-1))){
                     newlist.add("    "+map.get(count-3));//当有@XmlEnumValue时，将上面的三行注释加入
                     newlist.add("    "+map.get(count-2));//当有@XmlEnumValue时，将上面的三行注释加入
                     newlist.add("    "+map.get(count-1));//当有@XmlEnumValue时，将上面的三行注释加入
                 }
                 String tmp=map.get(count+1);
                 if(!tmp.endsWith(";"))
                     newlist.add("    "+StringUtils.substringBefore(tmp,"(")+";");//当有@XmlEnumValue时，将下面一行加入,
                 else
                     newlist.add("    "+StringUtils.substringBefore(tmp,"("));//当有@XmlEnumValue时，将下面一行加入,
                 newlist.add("\n");
            }
            //class类定义的头部
            else if(StringUtils.contains(line,"public class")){
                String tmp=StringUtils.trimToNull(line);
                if(!StringUtils.contains(line,"extends"))
                    newlist.add(StringUtils.substringAfter(StringUtils.substringBefore(tmp,"implements"),"public")+"{");
                else
                    newlist.add(StringUtils.substringAfter(StringUtils.substringBefore(tmp,"extends"),"public")+"{");
            }

            //类、成员变量的注释
            else if(StringUtils.contains(line,"@FieldDoc")||StringUtils.contains(line,"@DtoDoc")){
                newlist.add(" /**");
                newlist.add(getCommit(line));
                newlist.add("  */");
            }
            //成员变量以及需要引入的文件
            else if(StringUtils.contains(line,"private")||StringUtils.contains(line,"public")||StringUtils.contains(line,"protected")){
                includeField(line,newlist);
            }

            count++;
        }

        newlist.add("}");
        return newlist;
    }

    /**
     * 添加成员变量，以及需要include的文件
     * @param line
     * @param newlist
     */
     public static void includeField(String line,LinkedList<String> newlist){
         String tmp=StringUtils.trimToNull(line);
         String[] strings=tmp.split(" ");
         if(strings.length==3&&!"public".equals(strings[0])){
             //如果strings[1]是简单类型则需要转换成小写
             String zhu=strings[1];
             if(BasicType.isBasicType(strings[1])){
                 strings[1]=toLowerCase(strings[1]);//如果是基本类型，转成小写
                 strings[1]=mapType(strings[1]);
             }
             //list和map中可能存放了其它idl文件定义的数据类型
             else if(strings[1].startsWith("List")){
                 strings[1]=toLowerCaseFirstCh(strings[1]);//list首字母小写
                 String type=StringUtils.substringBefore(StringUtils.substringAfter(strings[1],"<"),">");
                 //当集合中不是基本类型时，需要引入idl文件
                 if(!BasicType.isBasicType(type)){
                     if(!"ResponseStatusType".equals(type)){
                         String include="include '"+type+".bjsc'";
                         //需要避免重复
                         newlist= removeDou(newlist,include);
                         newlist.addFirst(include);
                         String newtype=type+"."+type;
                         strings[1]=StringUtils.substringBefore(strings[1],"<")+"<"+newtype+">";
                     }
                     else{
                         String newtype="BaijiCommonTypes"+"."+type;
                         strings[1]=StringUtils.substringBefore(strings[1],"<")+"<"+newtype+">";
                     }
                 }else{
                     //基本类型所有字符都是小写
                     String newtype=toLowerCase(type);
                     newtype=mapType(newtype);
                     strings[1]=StringUtils.substringBefore(strings[1],"<")+"<"+newtype+">";
                 }

             }
//             //是map类型时  private Map<String,TYPE> aa
//             else if(strings[1].startsWith("Map")){
//                 strings[1]=toLowerCaseFirstCh(strings[1]);//map首字母小写
//                String type=StringUtils.substringBefore(StringUtils.substringAfter(strings[1],","),">");
//                 if(!BasicType.isBasicType(type)){
//                     if(!"ResponseStatusType".equals(type)){
//                         String include="include '"+type+".bjsc'";
//                         //需要避免重复
//                         newlist= removeDou(newlist,include);
//                         newlist.addFirst(include);
//                         String newtype=type+"."+type;
//                         strings[1]=StringUtils.substringBefore(strings[1],"<")+"<string,"+newtype+">";
//                     }
//                     else{
//                         String newtype="BaijiCommonTypes"+"."+type;
//                         strings[1]=StringUtils.substringBefore(strings[1],"<")+"<string,"+newtype+">";
//                     }
//
//                 }else{
//                     //基本类型所有字符都是小写
//                     String newtype=toLowerCase(type);
//                     newtype=mapType(newtype);
//                     strings[1]=StringUtils.substringBefore(strings[1],"<")+"<string,"+newtype+">";
//                 }
//             }
             //如果strings[1]是其他idl文件中定义的数据类型,就include相应的bjsc文件
             else{
                 if(!"ResponseStatusType".equals(strings[1])){
                     String include="include '"+strings[1]+".bjsc'";
                     //需要避免重复
                     newlist= removeDou(newlist,include);
                     newlist.addFirst(include);
                     strings[1]=zhu+"."+strings[1];
                 }
                 else{
                     strings[1]="BaijiCommonTypes."+strings[1];
                 }

             }
             //存入list
             newlist.add("    "+strings[1]+" "+strings[2]);
             newlist.add("\n");
         }
     }
     public static  String mapType(String str){
         if("boolean".equals(str))
             str="bool";//IDL内置的bool类型为 “bool”
         else if("calendar".equals(str))
             str="datetime";//IDL对应calendar的为datetime
         else if("byte[]".equals(str))
             str="binary";//IDL对应byte[]的是binary
         else if("bigdecimal".equals(str))
             str="decimal";
         else if("integer".equals(str))
             str="int";
         return str;
     }

    /**
     * 去除 LinkedList中重复元素
     * @param list
     * @param line
     * @return
     */
     public static LinkedList<String> removeDou(LinkedList<String> list,String line){
         List<String> tmplist=new LinkedList<>();
         for(String str:list){
             if(line.equals(str))
                 tmplist.add(str);
         }
         list.removeAll(tmplist);
         return list;
     }
    /**
     *将字符串全部转换成小写
     * @param line
     * @return
     */
    public static  String toLowerCase(String line) {

            return line.toLowerCase();
    }
    public static String toLowerCaseFirstCh(String line){
        if(Character.isLowerCase(line.charAt(0)))
            return line;
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

    /**
     * 判断是否是枚举变量的注释文字信息
     * @param s1
     * @param s2
     * @param s3
     * @return
     */
    public static boolean isEnumValueCommit(String s1,String s2,String s3){
        boolean result=false;
        s1=StringUtils.trimToNull(s1);
        s2=StringUtils.trimToNull(s2);
        s3=StringUtils.trimToNull(s3);
        result=s1.startsWith("/**")&&s2.startsWith("*")&&s3.startsWith("*/");
        return result;
    }
}
