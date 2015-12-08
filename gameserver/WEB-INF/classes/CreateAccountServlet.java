// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class CreateAccountServlet extends HttpServlet {  // JDK 6 and above only
 
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
         String sqlStr = "select * from players where Username = "
              + "'" + request.getParameter("uname") + "'";
              //+ " and qty > 0 order by price desc";
 	 // Print an HTML page as the output of the query
         out.println("<html><head><title>Login</title></head><body>");
         out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
	 ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
 	 if(rset.next()){
		out.println("<h3>Cannot Make Account</h3>");
		// Print a paragraph <p>...</p> for each record
        	out.println("<h3> user: "+ rset.getString("Username")+" already exists");
		out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/createaccount.html\"><input type=\"submit\" value=\"Try Again\"></form>");
	} else{
		stmt.executeUpdate("insert into players (Username,Password) values (\'"+request.getParameter("uname")+"\',\'"+request.getParameter("pswrd")+"\');");
		// Print a paragraph <p>...</p> for each record
        	out.println("<h3>Success, Welcome "+ request.getParameter("uname")+"</h3>");
		
	}
        out.println("</body></html>");
     } catch (SQLException ex) {
        ex.printStackTrace();
	out.println("<dialog open>"+ex.getMessage()+"</dialog>");
        out.close();
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