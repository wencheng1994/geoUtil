/*
 * Copyright 2011 Yonghong Technology Corp, Inc. All rights reserved.
 * Yonghong Technology Corp PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package g5.geo.mapping;

import g5.Setting;
import g5.geo.GeoData;
import g5.geo.GeoMgr;
import g5.geo.GeoTool;
import g5.io.FileSystem;
import g5.util.Bundle;
import g5.util.Util;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * GeoNames stores the graphics names data.
 * @author Yonghong Technology Corp, Inc.
 */
public final class GeoNames {
   /**
    * Continent data.
    */
   public static String CONTINENT_CODE = "CONTINENTCODE.txt";
   /**
    * Country data.
    */
   public static String COUNTRY_CODE = "COUNTRYCODE.txt";
   /**
    * Province data.
    */
   public static String PROVINCE_CODE = "PROVINCECODE.txt";
   /**
    * Point code data.
    */
   public static String POINT_CODE = "POINTCODE.txt";
   /**
    * Continent level.
    */
   public static String CONTINENT_LEVEL = "0";
   /**
    * Country level.
    */
   public static String COUNTRY_LEVEL = "1";
   /**
    * Province level.
    */
   public static String PROVINCE_LEVEL = "2";
   /**
    * Point level.
    */
   public static String POINT_LEVEL = "3";
   /**
    * City level.
    */
   public static String CITY_LEVEL = "4";
   /**
    * Point gmi code data.
    */
   public static String POINT_GMI_CODE = "COUNTRY GMI";

   /**
    * Constructor.
    */
   public GeoNames(String id, String name, String namefile, String aliasfile, String[] idNames,
      String parent, boolean load)
   {
      this.id = id;
      this.name = name;
      this.namefile = namefile;
      this.aliasfile = aliasfile;
      this.idNames = idNames;
      this.parent = parent;

      if(load) {
         initNames();
         initAlias();
      }
   }

   /**
    * Init the names for the geo.
    */
   private void initNames() {
      String line = null;

      try {
         InputStream in = null;

         if(FileSystem.get().exists(namefile)) {
            in = FileSystem.get().getInput(namefile);
         }

         if(in == null) {
            in = getClass().getResourceAsStream(namefile);
         }

         if(in == null) {
            in = new FileInputStream(namefile);
         }

         BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
         line = reader.readLine();

         if(line != null) {
            line = line.trim();
            names = line.split(GeoTool.MAP_DATA_SEP, -1);
            nameCount = names.length;
         }
      }
      catch(Exception ex) {
         log.log(Level.SEVERE, ex.getMessage(), ex);
      }
   }

   /**
    * Init the alias for the geo.
    */
   private void initAlias() {
      if(aliasfile == null) {
         return;
      }

      String line = null;
      InputStream in = null;

      try {
         if(FileSystem.get().exists(aliasfile)) {
            in = FileSystem.get().getInput(aliasfile);
         }

         if(in == null) {
            in = getClass().getResourceAsStream(aliasfile);
         }

         if(in == null) {
            in = new FileInputStream(aliasfile);
         }

         BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
         line = reader.readLine();

         if(line != null) {
            line = line.trim();
            aliasnames = line.split(GeoTool.MAP_DATA_SEP, -1);
            aliasCount = aliasnames.length;
         }
      }
      catch(Exception ex) {
         log.log(Level.SEVERE, ex.getMessage(), ex);
      }
   }

   /**
    * Load the names of the map.
    */
   private void loadNames() {
      if(data != null) {
         return;
      }

      Map<String, String[]> ndata = new HashMap<String, String[]>();
      BufferedReader bufReader = null;
      String line = null;

      try {
         InputStream in = getClass().getResourceAsStream(namefile);

         if(in == null) {
            in = new FileInputStream(Setting.getHome() + File.separator + namefile);
         }

         if(in == null) {
            in = new FileInputStream(namefile);
         }

         bufReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
         bufReader.readLine(); // remove column names
         line = bufReader.readLine();

         while(line != null) {
            line = line.trim();

            if(line.length() != 0) {
               String[] fields = line.split(GeoTool.MAP_DATA_SEP, -1);
               String[] names = new String[nameCount];
               System.arraycopy(fields, 0, names, 0, nameCount);
               String id = fields[nameCount];
               ndata.put(id, names);
               line = bufReader.readLine();
            }
         }

         data = ndata;
      }
      catch(Exception ex) {
         log.log(Level.SEVERE, ex.getMessage(), ex);
      }
      finally {
         if(bufReader != null) {
            try {
               bufReader.close();
            }
            catch(Exception ex) {
               log.log(Level.SEVERE, ex.getMessage(), ex);
            }
         }
      }
   }

   /**
    * Load the alias of the map.
    */
   private void loadAlias() {
      if(alias != null) {
         return;
      }

      Map<String, String[]> nalias = new HashMap<String, String[]>();

      if(aliasfile == null) {
         alias = nalias;
         return;
      }

      BufferedReader bufReader = null;
      String line = null;

      try {
         InputStream in = getClass().getResourceAsStream(aliasfile);

         if(in == null) {
            in = new FileInputStream(Setting.getHome() + File.separator + aliasfile);
         }

         if(in == null) {
            in = new FileInputStream(aliasfile);
         }

         bufReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
         bufReader.readLine(); // remove column names
         line = bufReader.readLine();

         while(line != null) {
            line = line.trim();

            if(line.length() != 0) {
               String[] fields = line.split(GeoTool.MAP_DATA_SEP, -1);
               String[] names = new String[aliasCount];
               System.arraycopy(fields, 0, names, 0, aliasCount);
               String id = fields[aliasCount - 1];
               nalias.put(id, names);
               line = bufReader.readLine();
            }
         }

         alias = nalias;
      }
      catch(Exception ex) {
         log.log(Level.SEVERE, ex.getMessage(), ex);
      }
      finally {
         if(bufReader != null) {
            try {
               bufReader.close();
            }
            catch(Exception ex) {
               log.log(Level.SEVERE, ex.getMessage(), ex);
            }
         }
      }
   }

   /**
    * Load the parent gmi of the map.
    */
   private void loadParent() {
      if(parents != null) {
         return;
      }

      Map<String, String> ndata = new HashMap<String, String>();
      BufferedReader bufReader = null;
      String line = null;

      try {
         InputStream in = getClass().getResourceAsStream(namefile);

         if(in == null) {
            in = new FileInputStream(Setting.getHome() + File.separator + namefile);
         }

         if(in == null) {
            in = new FileInputStream(namefile);
         }

         bufReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
         bufReader.readLine(); // remove column names
         line = bufReader.readLine();
         int idx = 0;

         for(int i = 0; i < names.length; i++) {
            if(names[i].equals(parent)) {
               idx = i;
               break;
            }
         }

         while(line != null) {
            line = line.trim();

            if(line.length() != 0) {
               String[] fields = line.split(GeoTool.MAP_DATA_SEP, -1);
               String id = fields[nameCount];
               String parent = fields[idx];
               ndata.put(id, parent);
               line = bufReader.readLine();
            }
         }

         parents = ndata;
      }
      catch(Exception ex) {
         log.log(Level.SEVERE, ex.getMessage(), ex);
      }
      finally {
         if(bufReader != null) {
            try {
               bufReader.close();
            }
            catch(Exception ex) {
               log.log(Level.SEVERE, ex.getMessage(), ex);
            }
         }
      }
   }

   /**
    * Gets a display label for the specified map feature.
    */
   public final String getLabel(String id, boolean single, String language) throws Exception {
      loadDatas();
      String[] aliaNames = alias.get(id);
      StringBuilder label = new StringBuilder();

      if(single) {
         String preLabel = aliaNames == null ? id : aliaNames[0];

         if(preLabel.equals("_Others_")) {
             preLabel = Bundle.getBundle().local("_Others_");
         }

         label.append(preLabel);

         int idx = id.indexOf(GeoMgr.GMI_REGION_SEP);

         if(idx > 0) {
            GeoNames province = GeoMgr.getGeoName(GeoNames.PROVINCE_LEVEL);
            String alias = province.getAlias(id.substring(0, idx), language);

            if(alias != null) {
               label.append("(").append(alias).append(")");
            }
            else {
               GeoNames country = GeoMgr.getGeoName(GeoNames.COUNTRY_LEVEL);
               String cAlias = country.getAlias(id.substring(0, idx), language);
               label.append("(").append(cAlias).append(")");
            }
         }
      }
      else {
         String[] labels = data.get(id);

         if(labels != null) {
            for(int i = 0; i < idNames.length; i++) {
               if(label.length() > 0) {
                  label.append(", ");
               }

               label.append(labels[getColIdx(idNames[i])]);
            }
         }

         if(aliaNames != null && aliaNames.length > 0) {
            label.append("(");
            String alia = null;

            for(int i = 0; i < aliaNames.length; i++) {
               if(alia != null) {
                  label.append(", ");
               }

               alia = aliaNames[i];
               label.append(alia);
            }

            label.append(")");
         }
      }

      return label.toString();
   }

   /**
    * Load the data from geonames.
    */
   private void loadDatas() {
      if(data == null) {
         slock.lock();

         try {
            loadNames();
         }
         finally {
            slock.unlock();
         }
      }

      if(alias == null) {
         slock.lock();

         try {
            loadAlias();
         }
         finally {
            slock.unlock();
         }
      }

      if(parent != null) {
         slock.lock();

         try {
            loadParent();
         }
         finally {
            slock.unlock();
         }
      }
   }

   /**
    * Get data.
    */
   public Map<String, String[]> getData() {
      loadDatas();
      return data;
   }

   /**
    * Get id names.
    */
   public String[] getIdNames() {
      return idNames;
   }

   /**
    * Get name.
    */
   public String getName() {
      return name;
   }

   /**
    * Get alias file path.
    */
   public String getAliasFile() {
      return aliasfile;
   }

   /**
    * Get name file path.
    */
   public String getNameFile() {
      return namefile;
   }

   /**
    * Get names.
    */
   public String[] getNames() {
      loadDatas();
      return data.keySet().toArray(new String[data.size()]);
   }

   /**
    * Get alias.
    */
   public String[] getAlias() {
      loadDatas();
      return alias.keySet().toArray(new String[alias.size()]);
   }

   /**
    * Get data by name.
    */
   public String[] getData(String name) {
      loadDatas();
      return data.get(name);
   }

   /**
    * Get data province gmi.
    */
   public String getDataCountryGMI(String name) {
      String[] data = getData(name);

      if(data == null) {
         return null;
      }

      if(Util.equals(this.name, "citys")) {
         return data[5];
      }
      else if(Util.equals(this.name, "prefectures")) {
         return data[7];
      }
      else if(Util.equals(this.name, "states")) {
         return data[4];
      }

      return null;
   }

   /**
    * Get data province gmi.
    */
   public String getDataProvinceGMI(String name) {
      String[] data = getData(name);

      if(data == null) {
         return null;
      }

      if(Util.equals(this.name, "citys")) {
         return data[3];
      }
      else if(Util.equals(this.name, "prefectures")) {
         return data[5];
      }

      return null;
   }

   /**
    * get alias by name.
    */
   public String[] getAlias(String name) {
      loadDatas();
      return alias.get(name);
   }

   /**
    * Get the alias.
    */
   public String getAlias(String name, String language) {
      String[] alias = getAlias(name);

      if(alias == null || alias.length == 0) {
         return null;
      }

      // @temp humming, support more language, convert the array to map
      if(alias.length == 1 || !"zh".equals(language)) {
         return alias[0];
      }

      return alias[1];
   }

   /**
    * Get the gmi.
    */
   public String getParentGMI(String id) {
      loadDatas();
      return parents != null ? parents.get(id) : null;
   }

   /**
    * Get the column headers.
    */
   public String[] getColNames() {
      return names;
   }

   /**
    * Get the sub areas according to the geo data.
    */
   public List getSubAreas(GeoData gdata) throws Exception {
      loadDatas();
      List<String> areas = null;

      if(gdata == null) {
         return areas;
      }

      //@temp leim, the geo data should be country level or province level.
      if(POINT_LEVEL.equals(id)) {
         areas = new ArrayList<String>();
         List<String> areaNames = null;
         boolean all = false;

         if(COUNTRY_LEVEL.equals(gdata.getLevel())) {
            areaNames = gdata.getShapeNames();
         }
         else if(PROVINCE_LEVEL.equals(gdata.getLevel())) {
            areaNames = new ArrayList<String>();
            String parentGMI = gdata.getGMICode();
            all = parentGMI == null;
            areaNames.add(parentGMI);
         }

         if(areaNames == null) {
            return areas;
         }

         int gmiIdx = -1;

         for(int i = 0; i < names.length; i++) {
            if(POINT_GMI_CODE.equals(names[i])) {
               gmiIdx = i;
               break;
            }
         }

         if(gmiIdx == -1) {
            log.log(Level.INFO, "Cannot find the GMI CODE in " + namefile);
            return areas;
         }

         Object[] values = data.values().toArray();
         Object[] keys = data.keySet().toArray();

         for(int j = 0; j < values.length; j++) {
            String[] strs = (String[]) values[j];

            for(int i = 0; i < areaNames.size(); i++) {
               if(all || (strs[gmiIdx] != null && strs[gmiIdx].equals(areaNames.get(i)))) {
                  areas.add((String) keys[j]);
                  break;
               }
            }
         }
      }
      else {
         areas = gdata.getShapeNames();
      }

      return areas;
   }

   /**
    * Get id, meams level.
    */
   public String getLevel() {
      return id;
   }

   @Override
   public String toString() {
      return "GeoNames[" + id + ", name=" + name + "]";
   }

   /**
    * Get the index of the column index.
    */
   private int getColIdx(String col) {
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

   public void setData(Map<String, String[]> data, Map<String, String[]> alias,
      Map<String, String> parents)
   {
      this.data = data;
      this.alias = alias;
      this.parents = parents;
   }

   /**
    * Save user geo names.
    */
   public void save() throws Exception {
      saveName();
      saveAlias();
   }

   /**
    * Save name file.
    */
   private void saveName() throws Exception {
      FileSystem fs = FileSystem.get();

      if(!fs.exists(ROOT)) {
         fs.createDirs(ROOT);
      }

      Writer writer = null;

      try {
         String path = namefile;
         OutputStream out = fs.getOutput(path);
         writer = new OutputStreamWriter(out, "UTF-8");
         nameCount = 3;
         names = new String[3];
         names[0] = "NAME";
         names[1] = "PARENT";
         names[2] = "GMI";
         StringBuffer header = new StringBuffer(names[0]).append(GeoTool.MAP_DATA_SEP).append(names[1]).
            append(GeoTool.MAP_DATA_SEP).append(names[2]).append("\r\n");
         writer.write(header.toString());

         for(String name: data.keySet()) {
            String[] vals = data.get(name);
            StringBuffer str = new StringBuffer();

            for(int i = 0; i < vals.length; i++) {
               str.append(vals[i]).append(GeoTool.MAP_DATA_SEP);
            }

            str.append(vals[2]).append("\r\n");
            writer.write(str.toString());
            writer.flush();
         }
      }
      finally {
         try {
            if(writer != null) {
               writer.close();
            }
         }
         catch(Throwable ex) {
            // ignore it
         }
      }
   }

   /**
    * Save alias file.
    */
   private void saveAlias() throws Exception {
      FileSystem fs = FileSystem.get();

      if(!fs.exists(ROOT)) {
         fs.createDirs(ROOT);
      }

      Writer writer = null;

      try {
         String path = aliasfile;
         OutputStream out = fs.getOutput(path);
         writer = new OutputStreamWriter(out, "UTF-8");
         StringBuffer header = new StringBuffer("CHINESE_NAME").append(GeoTool.MAP_DATA_SEP).
            append("CHINESE_NAME").append(GeoTool.MAP_DATA_SEP).append("GMI").append("\r\n");
         writer.write(header.toString());

         for(String name: alias.keySet()) {
            String[] vals = alias.get(name);
            StringBuffer str = new StringBuffer();

            for(int i = 0; i < vals.length; i++) {
               str.append(vals[i]);

               if(i != vals.length -1) {
                  str.append(GeoTool.MAP_DATA_SEP);
               }
            }

            str.append("\r\n");
            writer.write(str.toString());
         }
      }
      finally {
         try {
            if(writer != null) {
               writer.close();
            }
         }
         catch(Throwable ex) {
            // ignore it
         }
      }
   }

   private static final String ROOT = "geo/mapping/";
   private static final Logger log = Logger.getLogger(GeoNames.class.getPackage().getName());
   private static final byte _cid_ = -107;
   private String[] idNames;
   private final Lock slock = new ReentrantLock();
   private int nameCount = 0;
   private int aliasCount = 0;
   private String id;
   private String name;
   private String[] names;
   private String[] aliasnames;
   private String parent;
   private String namefile;
   private String aliasfile;
   private Map<String, String[]> data;
   private Map<String, String[]> alias;
   private Map<String, String> parents;
}
