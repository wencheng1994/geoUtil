/*
 * Copyright 2011 Yonghong Technology Corp, Inc. All rights reserved.
 * Yonghong Technology Corp PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package g5.geo.mapping.algorithm;

/**
 * MappingResult the result from finder.
 * @author Yonghong Technology Corp, Inc.
 */
public class MappingResult implements Comparable<MappingResult> {
   public MappingResult(String name, int difference) {
      this.name = name;
      this.difference = difference;
   }

   public int compareTo(MappingResult o) {
      if(difference < o.difference) {
         return -1;
      }
      else if(difference > o.difference) {
         return 1;
      }

      return 0;
   }

   /**
    * Get name.
    */
   public String getName() {
      return name;
   }

   /**
    * Get distance.
    */
   public int getDifference() {
      return difference;
   }

   private final String name;
   private int difference = 0;
}