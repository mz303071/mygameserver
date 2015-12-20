// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class InventoryServlet extends HttpServlet {  // JDK 6 and above only
   public String rnum(String n){
	if(n==null)return "0";
   	if(n.equals("null"))return "0";
	return n;
   }
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
         String sqlStr = "select * from inventorys where INID='" + request.getParameter("INID") + "'";
              //+ " and qty > 0 order by price desc";
 	 
	
	 out.println("<html><head><title>Login</title></head><body>");
	//out.println("<p>You query is: " + sqlStr + "</p>"); 
	ResultSet iset = stmt.executeQuery(sqlStr);
	if(iset.next()){
	//out.println("<p>query is processed</p>");
	out.println("<p>"+iset.getString("InventoryName")+" Details            $"+iset.getString("InventoryMoney") +"</p>");
	out.println("<table border=\"1\" style=\"width:50%\"><tr>");
	// Header row
	out.println("<th>Item</th>");
	out.println("<th>Ammount</th>");
	out.println("<th>Price</th>");
	out.println("<th>Buy Ammount</th>");
	out.println("<th>Purchase</th>");
	out.println("<th>Sell</th>");
	// 
	out.println("</tr></table>");
	ResultSet itemset = stmt.executeQuery("select * from items left join (select * from InventoryHasItems where InventoryHasItems.search_INID = "+request.getParameter("INID")+")IHI on items.ITID = IHI.search_ITID");
	while(itemset.next()){
		out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/editinventory\">");
		out.println("<table border=\"1\" style=\"width:50%\"><tr>");
		//
		out.println("<th>"+itemset.getString("items.ItemName")+"</th>");
		out.println("<th>"+rnum(itemset.getString("IHI.itemammount"))+"</th>");
		out.println("<th>$"+rnum(itemset.getString("items.ItemPrice"))+"</th>");
		out.println("<input type=\"hidden\" value=\""+request.getParameter("INID")+"\" name=\"INID\">");
		out.println("<input type=\"hidden\" value=\""+itemset.getString("items.ITID")+"\" name=\"ITID\">");
		out.println("<th><input type=\"number\" name=\"ammount\" value=0></th>");
		out.println("<th><input type=\"submit\" name=\"buy\" value=\"BUY\"</th>");
		out.println("<th><input type=\"submit\" name=\"sell\" value=\"SELL\"</th>");
		//
		out.println("</tr></table>");
		out.println("</form>");
	}
	 // SQL SELECT query 
	}
        
     } catch (SQLException ex) {
       out.println("<dialog open>");
	StringWriter sw = new StringWriter();
	ex.printStackTrace(new PrintWriter(sw));
	out.println(sw.toString());
	out.println("</dialog>");
        //ex.printStackTrace();
     } finally {
	out.println("</table></body></html>");
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