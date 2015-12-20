// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class AccountServlet extends HttpServlet {  // JDK 6 and above only
 
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
	out.println("<table border=\"1\" style=\"width:50%\">");
	out.println("<tr><td>FirstName</td><td>"+rset.getString("FirstName")+"</td></tr>");
	out.println("<tr><td>LastName</td><td>"+rset.getString("LastName")+"</td></tr>");
	out.println("<tr><td>UserName</td><td>"+rset.getString("Username")+"</td></tr>");
	out.println("<tr><td>Password</td><td>"+rset.getString("Password")+"</td></tr>");
	out.println("</table>");
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/editaccount\">");
	out.println("<input type=\"hidden\" value=\""+request.getParameter("uname")+"\" name=\"uname\">");
	out.println("<input type=\"hidden\" value=\""+request.getParameter("pswrd")+"\" name=\"pswrd\">");
	out.println("<input type=\"submit\" value=\"Edit Account\"></form>");
	 // SQL SELECT query
         String sqlStr2 = "select * from characters where CID in (select search_CID from playerownscharacters where search_PID = "+ rset.getString("PID")+")";
	//out.println("<p>"+sqlStr2+"</p>");
         ResultSet rset2 = stmt.executeQuery(sqlStr2);  // Send the query to the server
	//out.println("<p>"+sqlStr2+"</p>");
	out.println("<table border=\"1\" style=\"width:50%\"><tr><th>Character</th><th>  LVL  </th><th>  Options  </th></tr>");		
	while(rset2.next()){
        	out.println("<tr><td>"+ rset2.getString("CharacterName")+"</td><td>"+rset2.getString("CharacterLevel")+"</td><td><form method=\"get\" action=\"http://localhost:9999/gameserver/character\">"+"<input type=\"hidden\" value=\""+rset2.getString("CID")+"\" name=\"CID\"><input type=\"submit\" value=\"Sign In\"></form><td></tr>");
         
	}}
	out.println("</table>");
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/addcharacter\"><input type=\"hidden\" value=\""+request.getParameter("uname")+"\" name=\"uname\"><input type=\"submit\" value=\"Add Character\"></form>"); 
        out.println("</body></html>");
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