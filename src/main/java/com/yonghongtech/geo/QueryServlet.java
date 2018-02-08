package com.yonghongtech.geo;

import com.google.gson.Gson;
import com.yonghongtech.StatusCode;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Accept keyword and give tips data
 */
public class QueryServlet extends HttpServlet {

   @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      req.setCharacterEncoding("utf-8");
      String input = req.getParameter("input");
      Gson gson = new Gson();
      Map<String, Object> resultMap = new HashMap<>();

      if(input == null || input.equals("")) {
         resultMap.put("code", StatusCode.ERROR);
         resultMap.put("msg", "input error");
         resp.getWriter().write(gson.toJson(resultMap));
      } else {

      }
   }

   /*
   * match similar geo names for s
    */
   private void matchGeoNames(String s) {

   }

}

