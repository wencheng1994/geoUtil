package com.yonghongtech.geo;
/*
 * Copyright 2011 Yonghong Technology Corp, Inc. All rights reserved.
 * Yonghong Technology Corp PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Util, xml utility class.
 *
 * @author Yonghong Technology Corp, Inc.
 */
public class XMLUtil {
   /**
    * Get the children of the node by name.
    */
   public static List<Element> getChildrenByName(Node node, String name) {
      if(node == null || name == null) {
         return null;
      }

      NodeList list = node.getChildNodes();
      int size = list != null ? list.getLength() : 0;

      if(size == 0) {
         return null;
      }

      List<Element> children = new ArrayList<Element>();

      for(int i = 0; i < size; i++) {
         Node child = list.item(i);

         if(child instanceof Element) {
            Element elem = (Element) child;
            String tname = elem.getTagName();

            if(tname.equals(name)) {
               children.add(elem);
            }
         }
      }

      return children;
   }

   /**
    * Get element by name from node.
    */
   public static Element getChildByName(Node node, String name) {
      NodeList nodes = node.getChildNodes();
      int size = nodes != null ? nodes.getLength() : 0;

      for(int i = 0; i < size; i++) {
         Node elem = nodes.item(i);

         if((elem instanceof Element) && name.equals(elem.getNodeName())) {
            return (Element) elem;
         }
      }

      return null;
   }

   /**
    * Get node attribute from node.
    */
   public static String getNodeAttr(Element node, String name) {
      Attr attribute = node.getAttributeNode(name);

      if(attribute == null) {
         return null;
      }

      return attribute.getValue();
   }

   /**
    * Get the node value.
    */
   public static String getNodeValue(Node node) {
      if(node == null) {
         return null;
      }

      String value = "";
      NodeList list = node.getChildNodes();
      final int size = list.getLength(); // optimize

      for(int i = 0; i < size; i++) {
         Node subNode = list.item(i);

         switch(subNode.getNodeType()) {
         case Element.TEXT_NODE:
            String subValue = subNode.getNodeValue();

            if(subValue.trim().length() == 0) {
               subValue = subValue.trim();
            }

            if(subValue.length() > 0) {
               value = subValue;
            }

            break;
         case Element.CDATA_SECTION_NODE:
            value = subNode.getNodeValue();
         }
      }

      return value;
   }

   /**
    * Parse XML Data from an InputStream.
    */
   public static Document parseXML(InputStream input) throws Exception {
      if(factory == null) {
         factory = DocumentBuilderFactory.newInstance();
      }

      DocumentBuilder builder = factory.newDocumentBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(input, "UTF8"));
      InputSource src = new InputSource(br);
      return builder.parse(src);
   }

   private static DocumentBuilderFactory factory;
}
