package com.io;
import java.io.*;
public class FileUtil {

    public static void FileHandler(File file){
          File[] files=file.listFiles();
          if(files.length==0)
              return;
          for(File subfile:files){
              //如果是文件夹，递归调用
              if(subfile.isDirectory())
                  FileHandler(subfile);
              else{

              }
          }
    }
}
