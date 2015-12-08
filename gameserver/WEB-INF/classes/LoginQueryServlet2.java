// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class LoginQueryServlet2 extends HttpServlet {  // JDK 6 and above only
 
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
      	 
		response.setContentType("text/html");
     		out = response.getWriter();
		out.println("<html><head><title>Login</title></head><body>"); 	 

	if(rset.next()){
		out.println("<table>");
	out.println("<tr><th>FirstName</th><th>"+rset.getString("FirstName")+"</th></tr>");
	out.println("<tr><th>LastName</th><th>"+rset.getString("LastName")+"</th></tr>");
	out.println("<tr><th>UserName</th><th>"+rset.getString("Username")+"</th></tr>");
	out.println("<tr><th>Password</th><th>"+rset.getString("Password")+"</th></tr>");
	out.println("</table>");
	 // SQL SELECT query
         String sqlStr2 = "select *from characters where CID in (select * from playerownscharacters where search_PID = "+ rset.getString("PID")+";);";

         ResultSet rset2 = stmt.executeQuery(sqlStr2);  // Send the query to the server
	out.println("<table><tr><th>Character</th><th>LVL</th></tr>");		
	while(rset2.next()){
        	out.println("<tr><th>"+ rset2.getString("CharacterName")+"</th><th>"+rset2.getString("CharacterLevel")+"</th></tr>");
	} 
     } else{
		
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