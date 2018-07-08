package com.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class MainFrame extends JFrame implements ActionListener {
    //中心容器
    private Container container;
    //两个label，输入和输出目录
    private JLabel labela;
    private JLabel labelb;
    //两个textfield,保存路径
    private JTextField textField1;
    private JTextField textField2;
    //三个按钮，负责触发事件
    private JButton jb1;
    private JButton jb2;
    private JButton jb3;
    //文件选择器包装类
    static FileChooser fileChooser=new FileChooser();

    public MainFrame(){
        //初始化容器
        container=getContentPane();
        //初始化各组件
        labela=new JLabel("选择输入目录");
        labelb=new JLabel("选择输出目录");
        textField1=new JTextField();
        textField2=new JTextField();
        jb1=new JButton("...");
        jb2=new JButton("...");
        jb3=new JButton("开始转换");
        //一些基本设置(布局，大小。可视)
        double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation(new Point((int) (lx / 2) - 150, (int) (ly / 2) - 150));
        labela.setBounds(10, 10, 70, 20);
        labelb.setBounds(10, 35, 70, 20);
        textField1.setBounds(75, 10, 120, 20);
        textField2.setBounds(75, 35, 120, 20);
        jb1.setBounds(210, 10, 50, 20);
        jb2.setBounds(210, 35, 50, 20);
        jb3.setBounds(30, 60, 60, 20);
        //给按钮添加监听
        jb1.addActionListener(this);
        jb2.addActionListener(this);
        jb3.addActionListener(this);
        //往中心容器中加入组件
        container.add(labela);
        container.add(labelb);
        container.add(textField1);
        container.add(textField2);
        container.add(jb1);
        container.add(jb2);
        container.add(jb3);
       // this.add(container);
        this.setLayout(null);
        this.setSize(280,200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public static void main(String[] args) {
new MainFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       //获得JFileChooser对象
        JFileChooser jfc=fileChooser.getFileChooser();
        File inputDir=null;
        File outputDir=null;
        //选择输入目录
         if(e.getSource()==jb1){
             jfc.setFileSelectionMode(1);//代表只能选择文件夹
             int status=jfc.showOpenDialog(null);
             if(status==1)//如果取消选择，返回
                 return;
             else{
                 inputDir=jfc.getSelectedFile();
                 textField1.setText(inputDir.getAbsolutePath());
             }
         }
         //选择输出目录
         else if(e.getSource()==jb2){
             jfc.setFileSelectionMode(1);//代表只能选择文件夹
             int status=jfc.showOpenDialog(null);
             if(status==1)//如果取消选择，返回
                 return;
             else {
                 outputDir = jfc.getSelectedFile();
                 textField2.setText(outputDir.getAbsolutePath());
             }
         }
    }
}
