/*
 * Copyright 2011 Yonghong Technology Corp, Inc. All rights reserved.
 * Yonghong Technology Corp PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package g5.geo.mapping.algorithm;

import g5.geo.GeoData;
import g5.geo.mapping.GeoNames;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.Soundex;

/**
 * GeoMappingAlgorithm mapping the data and the shapefile.
 * @author Yonghong Technology Corp, Inc.
 */
public final class MappingAlgorithm {
   /**
    * String encoder.
    */
   public static StringEncoder EQUAL = new EqualEncoder();
   public static StringEncoder DEFAULT = new DefaultEncoder();
   public static StringEncoder SOUNDEX = new Soundex();
   public static StringEncoder METAPHONE = new Metaphone();
   public static StringEncoder DOUBLEMETAPHONE = new DoubleMetaphone();

   /**
    * String encoder index.
    */
   public static final int EQUAL_IDX = 1;
   public static final int DEFAULT_IDX = 2;
   public static final int SOUNDEX_IDX = 4;
   public static final int METAPHONE_IDX = 8;
   public static final int DOUBLEMETAPHONE_IDX = 16;

   /**
    * Get the algorithm by the encoder type.
    */
   public static MappingAlgorithm getAlgorithm(StringEncoder type) {
      return new MappingAlgorithm(type);
   }

   /**
    * Constructor.
    */
   private MappingAlgorithm(StringEncoder encoder) {
      this.encoder = encoder;
   }

   /**
    * Get the mapping names for the data from shapefile.
    */
   public List<String> getMappingNames(String name, GeoNames geonames, GeoData gdata)
      throws Exception
   {
      List<MappingResult> results = getMappingResults(name, geonames, gdata);
      List<String> strs = new ArrayList<String>();

      for(int i = 0; i < results.size(); i++) {
         strs.add(results.get(i).getName());
      }

      return strs;
   }

   /**
    * Get the mapping results for the data from shapefile.
    * if the map type is area, use the world or country geonames, or if map type is point, use point
    */
   public List<MappingResult> getMappingResults(String name, GeoNames geonames, GeoData gdata)
      throws Exception
   {
      String[] ids = geonames.getIdNames();
      String[] names = geonames.getColNames();
      MappingFinder finder = null;

      if(encoder instanceof EqualEncoder) {
         finder = new EqualFinder(encoder, ids, name, names);
      }
      else {
         finder = new MappingFinder(encoder, ids, name, names);
      }

      getFinder(finder, geonames, gdata);

//      if(gdata != null && !geonames.getLevel().equals(gdata.getLevel())) {
//         List shpnames = gdata.getShapeNames();
//         GeoNames gnames = GeoData.getGeoName(gdata.getLevel());
//         finder = getFinder(name, finder, gnames, null);
//      }

      List<MappingResult> results = finder.getResults();
      Collections.sort(results);
      return results;
   }

   /**
    * Find the mapping data.
    */
   private MappingFinder getFinder(MappingFinder finder, GeoNames geonames,
      GeoData gdata) throws Exception
   {
      List subNames = geonames.getSubAreas(gdata);
      String[] names = geonames.getNames();
      Set<String> set = subNames == null ? null : new HashSet<String>();

      for(int i = 0; subNames != null && i < subNames.size(); i++) {
         set.add(subNames.get(i).toString());
      }

      for(int i = 0; i < names.length; i++) {
         String name = names[i];

         if(set != null && !set.contains(name)) {
            continue;
         }

         String[] datanames = geonames.getData(name);
         String[] dataalias = geonames.getAlias(name);
         finder.find(name, datanames, dataalias);
      }

      if(finder instanceof EqualFinder && finder.getResults().size() == 0) {
         for(int i = 0; i < names.length; i++) {
            String name = names[i];

            if(set != null && !set.contains(name)) {
               continue;
            }

            String[] datanames = geonames.getData(name);
            String[] dataalias = geonames.getAlias(name);
            ((EqualFinder) finder).find0(name, datanames, dataalias);
         }
      }

      return finder;
   }

   public static void main(String[] args) {
      GeoNames gn = new GeoNames("2", "hehe", GeoNames.COUNTRY_CODE,
         "COUNTRYCODE_alias.txt", new String[] {"NAME"}, null, true);
      MappingAlgorithm ma = MappingAlgorithm.getAlgorithm(MappingAlgorithm.EQUAL);
      List<MappingResult> list = null;

      try {
         list = ma.getMappingResults("中国", gn, null);
      }
      catch(Exception e) {
         log.log(Level.SEVERE, e.getMessage(), e);
      }

      for(int i = 0; i < list.size(); i++) {
         System.out.println("..." + list.get(i).getName());
      }
   }

   private StringEncoder encoder;

   private static final class DefaultEncoder implements StringEncoder {
      public String encode(String str) throws EncoderException {
         return str;
      }

      public Object encode(Object obj) throws EncoderException {
         return obj;
      }
   }

   private static final class EqualEncoder implements StringEncoder {
      public String encode(String str) throws EncoderException {
         return str;
      }

      public Object encode(Object obj) throws EncoderException {
         return obj;
      }
   }

   /**
    * Equal finder find the exact name from the geonames.
    */
   private static final class EqualFinder extends MappingFinder {
      public EqualFinder(StringEncoder encoder, String[] idNames, String name, String[] names) {
         super(encoder, idNames, name, names);
      }

      @Override
      public void find(String id, String[] cols, String[] alias) {
         boolean equal = false;

         if(idNames != null) {
            for(int i = 0; i < idNames.length; i++) {
               int colIndex = getColIdx(idNames[i]);

               if(colIndex == -1) {
                  continue;
               }

               if(equals(name, cols[colIndex])) {
                  equal = true;
                  break;
               }
            }
         }

         if(!equal && alias != null) {
            for(int i = 0; i < alias.length; i++) {
               if(equals(name, alias[i])) {
                  equal = true;
                  break;
               }
            }
         }

         if(equal) {
            results.add(new MappingResult(id, 0));
         }
      }

      public void find0(String id, String[] cols, String[] alias) {
         boolean equal = false;

         if(idNames != null) {
            for(int i = 0; i < idNames.length; i++) {
               int colIndex = getColIdx(idNames[i]);

               if(colIndex == -1) {
                  continue;
               }

               String colName = cols[colIndex];

               if(name != null && name.indexOf(colName) >= 0) {
                  equal = true;
                  break;
               }
               else if(colName != null && name != null && colName.startsWith(name) &&
                  suffix.contains(colName.substring(name.length())))
               {
                  equal = true;
                  break;
               }
            }
         }

         if(!equal && alias != null) {
            for(int i = 0; i < alias.length; i++) {
               if(name != null && name.indexOf(alias[i]) >= 0) {
                  equal = true;
                  break;
               }
               else if(alias[i] != null && name != null && alias[i].startsWith(name) &&
                  suffix.contains(alias[i].substring(name.length())))
               {
                  equal = true;
                  break;
               }
            }
         }

         if(equal) {
            results.add(new MappingResult(id, 0));
         }
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
   }

   /**
    * Get the encoder.
    */
   public static StringEncoder getEncoder(int encodeIdx) {
      switch(encodeIdx) {
      case EQUAL_IDX:
         return EQUAL;
      case DEFAULT_IDX:
         return DEFAULT;
      case SOUNDEX_IDX:
         return SOUNDEX;
      case METAPHONE_IDX:
         return METAPHONE;
      case DOUBLEMETAPHONE_IDX:
         return DOUBLEMETAPHONE;
      default:
         return null;
      }
   }

   private static Set<String> suffix = new HashSet<String>();
   private static final Logger log =
      Logger.getLogger(MappingAlgorithm.class.getPackage().getName());

   {
      suffix.add("\u7701"); // Shen
      suffix.add("\u5e02"); // Shi
      suffix.add("\u53bf"); // Xian
      suffix.add("\u533a"); // Qu
      suffix.add("\u81ea\u6cbb\u5dde"); // ZiZhiZhou
      suffix.add("\u81ea\u6cbb\u53bf"); // ZiZhiXian
   }
}