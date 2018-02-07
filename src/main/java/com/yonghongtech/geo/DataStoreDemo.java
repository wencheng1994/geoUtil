package com.yonghongtech.geo;

import org.geotools.data.*;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.geometry.Geometry;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

public class DataStoreDemo {

   public static void main(String[] args) throws Exception {
      FeatureCollection featureCollection = getCollection("/Users/Charles/Projects/geoUtil/db/example/CHN_adm_shp/CHN_adm0.shp");
      FeatureIterator iterator = featureCollection.features();

      while(iterator.hasNext()) {

         Feature feature = iterator.next();

         Object obj = feature.getProperty("the_geom").getValue();

         StringWriter writer = new StringWriter();
//         System.out.println(obj);
         break;

      }
   }

   public static FeatureCollection getCollection(String shpFile) throws Exception {
      File file = new File(shpFile);
      URL shapeUrl = file.toURI().toURL();
      ShapefileDataStore store = new ShapefileDataStore(shapeUrl);
      String[] names = store.getTypeNames();
      FeatureSource source = store.getFeatureSource(names[0]);
      FeatureCollection features = source.getFeatures();
      return features;
   }
}
