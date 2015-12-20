// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class AddInventoryServlet extends HttpServlet {  // JDK 6 and above only
 
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
         String sqlStr = "select * from characters where CID = " + request.getParameter("CID");
         // Print an HTML page as the output of the query
         out.println("<html><head><title>Login</title></head><body>");
         out.println("<h3>Inventory Creation</h3>");
	 //out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
	 ResultSet cset = stmt.executeQuery(sqlStr);  // Send the query to the server
 	 if(cset.next()){
		sqlStr="select Count(s_CID) from inventoryowner where s_CID="+request.getParameter("CID");
		//out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
		ResultSet icount=stmt.executeQuery(sqlStr);  // Send the query to the server
		
		
 	        
		icount.next();
		out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/doaddinventory\">");
		out.println("<input type=\"hidden\" name=\"CID\" value=\""+request.getParameter("CID")+"\">");
		out.println("<table border=\"1\" style=\"width:50%\">");
		out.println("<tr><th>Inventory Name:</th><th><input type=\"text\" name=\"InventoryName\" value=\"MyInventory"+icount.getString(1)+"\"></th></tr>"); 
		
	  	out.println("<tr><th>CashAmmount:</th><th> $<input type=\"float\" name=\"InventoryMoney\" value=\"200\"></th></tr>"); 
		out.println("</table>");
		out.println("<input type=\"submit\" value=\"Create\"></form>");
		
	} else{
		out.println("Error No user Exists");
	}
        
     } catch (SQLException ex) {
       out.println("<dialog open>");
	StringWriter sw = new StringWriter();
	ex.printStackTrace(new PrintWriter(sw));
	out.println(sw.toString());
	out.println("</dialog>");
        //ex.printStackTrace();
     } finally {
	out.println("</body></html>");
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