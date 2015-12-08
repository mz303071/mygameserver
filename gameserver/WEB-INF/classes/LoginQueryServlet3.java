// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class LoginQueryServlet3 extends HttpServlet {  // JDK 6 and above only
 
   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
     
      PrintWriter out=null;
      Connection conn = null;
      Statement stmt = null;
      try {
         // Step 1: Allocate a database Connection object
         conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:8888/gamedb", "myuser", "xxxx"); // <== Check!
            // database-URL(hostname, port, default database), username, password
 
         // Step 2: Allocate a Statement object within the Connection
         stmt = conn.createStatement();
 
         // Step 3: Execute a SQL SELECT query
         String sqlStr = "select * from players where Username = "
              + "'" + request.getParameter("uname") + "' and Password = '"+request.getParameter("pswrd")+"';";
              //+ " and qty > 0 order by price desc";
 	 // Print an HTML page as the output of the query
        
       //  out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
	 ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
	 // Get a output writer to write the response message into the network socket
      	 
		 

	if(rset.next()){
		response.sendRedirect("/gameserver/account?uname="+request.getParameter("uname")+"&pswrd="+request.getParameter("pswrd"));
	//request.setAttribute("message", "Hello world");
	//RequestDispatcher dispatcher = servletContext().getRequestDispatcher(url);
	//dispatcher.forward(request, response);

		
     } else{
		response.setContentType("text/html");
     		out = response.getWriter();
		out.println("<html><head><title>Login</title></head><body>"); 	
		out.println("<h3>Login Failed</h3>");
		out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/loginquery2.html\"><input type=\"submit\" value=\"Try Again\"></form><form method=\"get\" action=\"http://localhost:9999/gameserver/createaccount.html\"><input type=\"submit\" value=\"Sign Up\"></form>");
		
	}out.println("</body></html>");
		out.close(); 
        
     } catch (SQLException ex2){
    	 ex2.printStackTrace();
     }catch (Exception ex) {
        ex.printStackTrace();
     } finally {
    	 if(out!=null)
        out.close();  // Close the output writer
        try {
           // Step 5: Close the resources
           if (stmt != null) stmt.close();
           if (conn != null) conn.close();
        } catch (SQLException ex) {
           ex.printStackTrace();
        }
     }
   }
}