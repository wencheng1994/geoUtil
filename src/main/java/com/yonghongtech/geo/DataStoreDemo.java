package com.yonghongtech.geo;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;

public class DataStoreDemo {

   public static void main(String[] args) throws IOException {
      ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
      ShapefileDataStore sds = (ShapefileDataStore) dataStoreFactory.createDataStore(
         new File("/Users/Charles/Projects/geoUtil/db/example/ne_50m_admin_0_countries_lakes/ne_50m_admin_0_countries_lakes.shp").toURI().toURL());
      sds.setCharset(Charset.forName("UTF-8"));
      // 只有一种类型: typeName DLTB
      SimpleFeatureSource featureSource = sds.getFeatureSource();
      SimpleFeatureIterator itertor = featureSource.getFeatures().features();
      while(itertor.hasNext()) {
         SimpleFeature feature = itertor.next();
         Iterator<Property> it = feature.getProperties().iterator();
         while(it.hasNext()) {
            Property pro = it.next();
            System.out.print(pro.getName() + " " + pro.getType()+" "+pro.getDescriptor());
         }
      }
      itertor.close();

   }
}
