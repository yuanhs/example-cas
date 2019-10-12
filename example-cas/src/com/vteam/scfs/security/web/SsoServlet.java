package com.vteam.scfs.security.web;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;

/**
 * 重置用户名servlet
 * @author winter.hu
 *
 */
public class SsoServlet extends HttpServlet  {
 private static final long serialVersionUID = -7259462042314670330L;

 @Override
 protected void doGet(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {
  doPost(req, resp);
 }

 @Override
 protected void doPost(HttpServletRequest req, HttpServletResponse resp)
   throws ServletException, IOException {
  Enumeration<String> names = req.getSession().getAttributeNames();
  while(names.hasMoreElements()){
   System.out.println("***********************"+names.nextElement());
  }

  
  Object objAssertion = req.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
  
  try {
	Method method = objAssertion.getClass().getMethod("getPrincipal");
	Object objPrincipal= method.invoke(objAssertion);
	System.out.println("33333333333333------objPrincipal: " + objPrincipal.toString());
	
	
	Method method2 = objPrincipal.getClass().getMethod("getAttributes");
	Map<String, String> attributes = (Map<String, String>) method2.invoke(objPrincipal);
	
	System.out.println("tttttttttttttttttt------: " + attributes.toString());
	
} catch (Throwable t) {
	// TODO Auto-generated catch block
	t.printStackTrace();
} 
  
  System.out.println("-----------assertion-----------" + objAssertion.toString());
  System.out.println("assertion classloader-----------" + objAssertion.getClass().getClassLoader().toString()); 
  
  System.out.println("OSGI2222222-----------"+Assertion.class.getClassLoader().toString()); 
  System.out.println("OSGI2222222 parent-----------"+Assertion.class.getClassLoader().getParent().toString()); 

  
  resp.getWriter().println("Done.");
  
//Assertion assertion = (Assertion) req.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);  
//
//   AttributePrincipal principal = assertion.getPrincipal(); 
////  AttributePrincipal principal = (AttributePrincipal) req.getUserPrincipal();
//   String userId = "";
//   Map<String, String> attributes = principal.getAttributes();  
////   userId = attributes.get("loginName");
//      for (Map.Entry<String, String> entry : attributes.entrySet()) {
//       if("loginName".equals(entry.getKey())){
//        userId = URLDecoder.decode(entry.getValue());
//       }
//
//      }
//      resp.setCharacterEncoding("UTF-8");
//      resp.getWriter().println(userId);
 }

}