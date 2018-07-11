package com.io;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

public class demo {

    public static void main(String[] args) {

      String de="private Map<String,TYPE> aa";
      String out=StringUtils.substringBefore(StringUtils.substringAfter(de,","),">");
        System.out.println(out);
}
}
