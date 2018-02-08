package com.yonghongtech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackDemo {

   public static void main(String[] args) {
      logger.debug("let do");
      logger.error("errro");
      logger.info("hahah)   ");

   }

  static  Logger logger = LoggerFactory.getLogger(LogbackDemo.class.getName());


}
