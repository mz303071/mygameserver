// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class AddCharacterServlet extends HttpServlet {  // JDK 6 and above only
 
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
         // Print an HTML page as the output of the query
         out.println("<html><head><title>Login</title></head><body>");
         //out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
	 ResultSet rset = stmt.executeQuery(sqlStr);  // Send the query to the server
 	 if(rset.next()){
		out.println("<h3>Character Creation</h3>");
		out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/doaddcharacter\">");
		out.println("<input type=\"hidden\" name=\"uname\" value=\""+request.getParameter("uname")+"\">");
		out.println("Character Name: <input type=\"text\" name=\"cname\" value=\"MyCharName\"><br>Race:<br>"); 
	  	ResultSet raceset=stmt.executeQuery("select * from races");
		while(raceset.next()){
			out.println("<input type=\"radio\" name=\"rid\" value=\""+raceset.getString("RID")+"\">"+raceset.getString("RaceName")+"<br>");
		}
		out.println("<input type=\"submit\" value=\"Create\"></form>");
	} else{
		out.println("Error No user Exists");
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