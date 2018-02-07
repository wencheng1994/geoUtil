package com.yonghongtech.geo;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class QueryServlet extends HttpServlet {

   @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      this.doPost(req, resp);
   }

   @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
      resp.getWriter().write("hello");
   }
}
