// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class EditAccountServlet extends HttpServlet {  // JDK 6 and above only
 
   // The doGet() runs once per HTTP GET request to this servlet.
   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws ServletException, IOException {
      // Set the MIME type for the response message
      response.setContentType("text/html");
      // Get a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
 
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
         String sqlStr = "select * from players where Username='" + request.getParameter("uname") + "' and Password ='"+request.getParameter("pswrd")+"'";
              //+ " and qty > 0 order by price desc";
 	 
	
	 out.println("<html><head><title>Login</title></head><body>");
	//out.println("<p>You query is: " + sqlStr + "</p>"); 
	ResultSet rset = stmt.executeQuery(sqlStr);
	if(rset.next()){
	//out.println("<p>query is processed</p>");
	out.println("<p>Account Details</p>");
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/doeditaccount?prevaccname="+request.getParameter("pswrd")+"\">");
	out.println("<table border=\"1\" style=\"width:50%\"><input type=\"hidden\" value=\""+request.getParameter("uname")+"\" name=\"uname\">");
	out.println("<tr><td>Username</td><td><input type=\"text\" value=\""+rset.getString("Username")+"\" name=\"duname\"></td></tr>");
	out.println("<tr><td>Password</td><td><input type=\"text\" value=\""+rset.getString("Password")+"\" name=\"dpswrd\"></td></tr>");
	out.println("<tr><td>Firstname</td><td><input type=\"text\" value=\""+rset.getString("Firstname")+"\" name=\"dfname\"></td></tr>");
	out.println("<tr><td>Lastname</td><td><input type=\"text\" value=\""+rset.getString("Lastname")+"\" name=\"dlname\"></td></tr></table>");
	out.println("<input type=\"submit\" value=\"Submit\"></form>");
	 // SQL SELECT query 
	}
        out.println("</table></body></html>");
     } catch (SQLException ex) {
        ex.printStackTrace();
     } finally {
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