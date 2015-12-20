// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class DoAddInventoryServlet extends HttpServlet {  // JDK 6 and above only
 
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
         String sqlStr = "select * from characters where CID = "
              + "'" + request.getParameter("CID") + "'";
         // Print an HTML page as the output of the query
         out.println("<html><head><title>Login</title></head><body>");
         //out.println("<p>You query is: " + sqlStr + "</p>"); // Echo for debugging 
	 ResultSet cset = stmt.executeQuery(sqlStr);  // Send the query to the server
 	 if(cset.next()){
		out.println("<p>Character: " + cset.getString("CharacterName") + " exists</p>"); // Echo for debugging 
		ResultSet icount=stmt.executeQuery("select Count(*) from inventorys where INID in (select s_INID from inventoryowner where s_CID="+request.getParameter("CID")+") and InventoryName='"+request.getParameter("InventoryName") + "'");  // Send the query to the server
		icount.next();
		//out.println("<p>"+icount.getString(1)+"</p>"); // Echo for debugging 
		if(Integer.parseInt(icount.getString(1))!=0){
			// Echo for debugging
			out.println("<p>Inventory: " + request.getParameter("InventoryName") + " exists</p>"); 
			
			out.println("<h3>FAILED</h3>"); // Echo for debugging 	
		}else{
			stmt.executeUpdate("insert into inventorys (InventoryName,InventoryMoney) values('"+request.getParameter("InventoryName")+"',"+request.getParameter("InventoryMoney")+")");
//INSERT INTO table_name (col1, col2,...) VALUES ('val1', 'val2'...);
//SELECT LAST_INSERT_ID();
			ResultSet inventidq=stmt.executeQuery("Select LAST_INSERT_ID()");
			inventidq.next();
			
			stmt.executeUpdate("insert into inventoryowner (s_CID,s_INID) values('"+request.getParameter("CID")+"','"+inventidq.getString(1)+"')");
			out.println("<p>Inventory: Creating " + request.getParameter("InventoryName") + " </p>"); // Echo for debugging 
			out.println("<h3>SUCCESS</h3>"); // Echo for debugging 
		
		}		
	}else{
		out.println("<p>Character: " + cset.getString("CharacterName") + "does not exist</p>"); // Echo for debugging 
		out.println("<h3>ERROR</h3>"); // Echo for debugging 	
	}  
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/character\">");
	out.println("<input type=\"hidden\" name=\"CID\" value=\""+request.getParameter("CID")+"\">");
	out.println("<input type=\"submit\" value=\"Back To Character\"></form>");
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