// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class CharacterServlet extends HttpServlet {  // JDK 6 and above only
 
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
         String sqlStr = "select * from characters where CID='" + request.getParameter("CID") +"'";
              //+ " and qty > 0 order by price desc";
 	 
	
	 out.println("<html><head><title>Login</title></head><body>");
	//out.println("<p>You query is: " + sqlStr + "</p>"); 
	ResultSet cset = stmt.executeQuery(sqlStr);
	String charname="";
	String charlevel="";
	String charraceid="";
	if(cset.next()){
		
		charname=cset.getString("CharacterName");
		charlevel=cset.getString("CharacterLevel");
		charraceid=cset.getString("search_RID");
	//out.println("<p>query is processed</p>");
	out.println("<p>Character Details</p>");
	out.println("<table border=\"1\" style=\"width:50%\">");
	out.println("<tr><td>Character Name</td><td>"+charname+"</td></tr>");
	out.println("<tr><td>Character Level</td><td>"+charlevel+"</td></tr>");
	ResultSet rset =stmt.executeQuery("select * from races where RID='"+charraceid+"'");
	//out.println("<p>query is processed</p>");
	if(rset.next())
	out.println("<tr><td>Character Race</td><td>"+rset.getString("RaceName")+"</td></tr>");

	out.println("</table>");
	//out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/editcharacter\">");
	//out.println("<input type=\"hidden\" value=\""+request.getParameter("CID")+"\" name=\"CID\">");
	//out.println("<input type=\"submit\" value=\"Edit Character\"></form>");
	 // SQL SELECT query
         String sqlStr2 = "select * from inventorys where INID in (select s_INID from inventoryowner where s_CID = "+ request.getParameter("CID")+")";
	//out.println("<p>"+sqlStr2+"</p>");
         ResultSet oset = stmt.executeQuery(sqlStr2);  // Send the query to the server
	//out.println("<p>"+sqlStr2+"</p>");
	out.println("<table border=\"1\" style=\"width:50%\"><tr><th>Inventory Name</th><th>ACCESS</th></tr>");		
	while(oset.next()){
        	out.println("<tr><td>"+ oset.getString("InventoryName")+"</td><td><form method=\"get\" action=\"http://localhost:9999/gameserver/inventory\">"+"<input type=\"hidden\" value=\""+oset.getString("INID")+"\" name=\"INID\"><input type=\"submit\" value=\"Edit Inventory\"></form><td></tr>");
         
	}}
	out.println("</table>");
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/addinventory\"><input type=\"hidden\" value=\""+request.getParameter("CID")+"\" name=\"CID\"><input type=\"submit\" value=\"Add Inventory\"></form>"); 
	out.println("</body></html>");
     } catch (SQLException ex) {
 	out.println("<dialog open>");
	StringWriter sw = new StringWriter();
	ex.printStackTrace(new PrintWriter(sw));
	out.println(sw.toString());
	out.println("</dialog>");
        //ex.printStackTrace();
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