package com.yonghongtech.geo;

import java.util.*;

/**
 * GeoUtil store geo info and manipulate it
 */
public class GeoUtil {
   /**
    * Store for all geo names
    */
   private static List<String> names = new ArrayList<>();

   /**
    * geo id map geo obj
    */
   private static Map<String, Object> id2geo = new HashMap<>();

   /**
    * geo name map id
    */
   private static Map<String, String> name2id = new HashMap<>();

   /**
    * Match name
    */
   public Set<String> matchNames(CharSequence c) {
      Set<String> set = new HashSet<>();

      for(String s : names) {
         if(s.contains(c)) {
            set.add(s);
         }
      }

      return set;
   }

   /**
    * get id by name
    */
   public String getId(String name) {
      if(name == null) {
         return null;
      }

      return name2id.get(name);
   }

   /**
    *
    */
}
