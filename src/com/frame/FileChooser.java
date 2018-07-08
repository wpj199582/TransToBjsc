package com.frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
public class FileChooser {
    private JFileChooser fileChooser ;

    public JFileChooser getFileChooser() {
        return fileChooser;
    }

    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }

    private final String[][] fileENames ={{".java", "JAVA源程序 文件(*.java)"}};//这里可以增加文件类型
    public FileChooser(){
        fileChooser=new JFileChooser();
        // 设置当前目录
        fileChooser.setCurrentDirectory(new File("d://"));
//        fileChooser.setAcceptAllFileFilterUsed(false);
//        fileChooser.addChoosableFileFilter(new FileFilter() {
//
//            public boolean accept(File file) {
//
//                return true;
//            }
//
//            @Override
//            public String getDescription() {
//                return null;
//            }
//
//        });

        // 循环添加需要显示的文件
//        for (final String[] fileEName : fileENames) {
//
//            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
//
//                public boolean accept(File file) {
//
//                    if (file.getName().endsWith(fileEName[0]) || file.isDirectory()) {
//
//                        return true;
//                    }
//
//                    return false;
//                }
//
//                public String getDescription() {
//
//                    return fileEName[1];
//                }
//
//            });
//        }

        //fileChooser.showDialog(null, null);
    }
//    public static void main(String[] args) {
////
////    new FileChooser();
////
////
////    }
}

