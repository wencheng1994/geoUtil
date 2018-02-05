/*
 * Copyright 2011 Yonghong Technology Corp, Inc. All rights reserved.
 * Yonghong Technology Corp PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package g5.geo.mapping.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.StringEncoder;

/**
 * MappingFinder find suitable mapping name according to the shapefile.
 * @author Yonghong Technology Corp, Inc.
 */
public class MappingFinder {
   /**
    * Constructor.
    */
   public MappingFinder(StringEncoder encoder, String[] idNames, String name, String[] names) {
      this.encoder = encoder;
      this.idNames = idNames;
      this.name = name;
      this.names = names;
   }

   /**
    * Find the cols.
    */
   public void find(String id, String[] cols, String[] alias) {
      if(cols == null || cols.length == 0) {
         return;
      }

      int diff = -1;

      for(int i = 0; i < idNames.length; i++) {
         int colIndex = getColIdx(idNames[i]);

         if(colIndex == -1) {
            continue;
         }

         String col = cols[colIndex];

         try {
            col = col == null ? null : encoder.encode(col);
            col = col == null ? null : col.toLowerCase();
            name = name == null ? null : encoder.encode(name);
            name = name == null ? null : name.toLowerCase();
         }
         catch(Throwable ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
         }

         diff = diff(name, col);
      }

      if(diff != 0 && alias != null) {
         for(int i = 0; i < alias.length; i++) {
            String col = alias[i];

            if(equals(name, col)) {
               diff = 0;
               break;
            }
            else {
               int diffAlias = diff(name, col);
               diff = diff == -1 ? diffAlias : Math.min(diff, diffAlias);

               if(diff == 0) {
                  break;
               }
            }
         }
      }

      results.add(new MappingResult(id, diff));
   }

   protected int diff(String name, String col) {
      if(name == null) {
         return col == null ? 0 : col.length();
      }

      if(col == null) {
         return name.length();
      }

      int nlen = name.length();
      int clen = col.length();

      if(clen == 0) {
         return nlen;
      }

      if(nlen == 0) {
         return clen;
      }

      if(nlen > clen) {
         String tmp = name;
         name = col;
         col = tmp;
         nlen = clen;
         clen = col.length();
      }

      if(col.indexOf(name) >= 0) {
         return 0;
      }

      int p, q, diff;
      char c;
      int[] m = new int[nlen + 1];
      int[] n = new int[nlen + 1];

      for(p = 0; p <= nlen; p++) {
         m[p] = p;
      }

      for(q = 1; q <= clen; q++) {
         c = col.charAt(q - 1);
         n[0] = q;

         for(p = 1; p <= nlen; p++) {
            diff = name.charAt(p - 1) == c ? 0 : 1;
            n[p] = Math.min(Math.min(n[p - 1] + 1, m[p] + 1), m[p - 1] + diff);
         }

         m = n;
         n = new int[nlen + 1];
      }

      return m[nlen];
   }

   private boolean equals(Object obj1, Object obj2) {
      if(obj1 == null) {
         return obj2 == null;
      }
      else if(obj1 instanceof String && obj2 instanceof String) {
         return ((String) obj1).equalsIgnoreCase((String) obj2);
      }
      else {
         return obj1.equals(obj2);
      }
   }

   /**
    * Get the index of the column.
    */
   protected int getColIdx(String col) {
      if(names == null || col == null) {
         return -1;
      }

      for(int i = 0; i < names.length; i++) {
         if(col.equals(names[i])) {
            return i;
         }
      }

      return -1;
   }

   /**
    * Get the found mapping results.
    */
   public List<MappingResult> getResults() {
      return results;
   }

   protected List<MappingResult> results = new ArrayList<MappingResult>();
   protected String[] idNames;
   protected String name;
   private String[] names;
   private StringEncoder encoder;
   private static final Logger log = Logger.getLogger(MappingFinder.class.getPackage().getName());
}