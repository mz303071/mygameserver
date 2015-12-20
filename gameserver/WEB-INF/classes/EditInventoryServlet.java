// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class EditInventoryServlet extends HttpServlet {  // JDK 6 and above only
 
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
		
		out.println("<p>Inventory:" +iset.getString("InventoryName")+" exists</p>"); 
		
		// get ammount of sale
		int itemammount=Integer.parseInt(request.getParameter("ammount"));	
		// get ammount of money
		float availiblecash=Float.parseFloat(iset.getString("InventoryMoney")); 
		ResultSet itemtcheck= stmt.executeQuery("select * from items where ITID='"+request.getParameter		("ITID")+"'");
		if(itemtcheck.next()){
			out.println("<p>Item:" +itemtcheck.getString("ItemName")+" exists</p>"); 	
			// get itemprice
			float itemprice=Float.parseFloat(itemtcheck.getString("ItemPrice")); 	
			if(request.getParameter("buy")!=null){
     				if(itemammount*itemprice<=availiblecash){
					out.println("<p>Inventory Action: buying </p>");
					stmt.executeUpdate("UPDATE Inventorys SET InventoryMoney=InventoryMoney-"+ itemammount*itemprice+" WHERE INID="+request.getParameter("INID"));
					// check if need to insert the item
					ResultSet checkitem=stmt.executeQuery("Select * From InventoryHasItems where search_INID="+request.getParameter("INID")+" and search_ITID="+request.getParameter("ITID"));
					if(checkitem.next()){
					// item already exists
					out.println("<p>Inventory Action: inventory updating item count</p>");
						stmt.executeUpdate("Update InventoryHasItems SET itemammount=itemammount+"+itemammount+" Where search_INID="+request.getParameter("INID")+" and search_ITID="+request.getParameter("ITID"));	
					}else{
					// making item
					out.println("<p>Inventory Action: inventory inserting item count</p>");
						stmt.executeUpdate("Insert into InventoryHasItems values("+request.getParameter("INID")+","+request.getParameter("ITID")+","+itemammount+")");
					}
					out.println("<h3>SUCCESS</h3>");
				}else{	
					out.println("<p>Inventory Action: not enough funds to buy</p>");
					out.println("<h3>FAILURE</h3>"); 			     			
				}		
			}else if(request.getParameter("sell")!=null){
					ResultSet checkitem=stmt.executeQuery("Select * From InventoryHasItems where search_INID="+request.getParameter("INID")+" and search_ITID="+request.getParameter("ITID"));
					if(checkitem.next()){
						if(Integer.parseInt(checkitem.getString("itemammount"))>=itemammount){
							out.println("<p>Inventory Action: selling </p>");
							stmt.executeUpdate("UPDATE Inventorys SET InventoryMoney=InventoryMoney+"+ itemammount*itemprice+" WHERE INID="+request.getParameter("INID"));
							stmt.executeUpdate("Update InventoryHasItems SET itemammount=itemammount-"+itemammount+" Where search_INID="+request.getParameter("INID")+" and search_ITID="+request.getParameter("ITID"));
							out.println("<h3>SUCCESS</h3>");
						}else{
							out.println("<p>Inventory Action: not enough supply to sell</p>");
							out.println("<h3>FAILURE</h3>"); 
						}
					}else{
						out.println("<p>Inventory Action: none supply to sell</p>");
						out.println("<h3>FAILURE</h3>");
					}
			}else{
				out.println("<p>Inventory Action: you must either buy or sell</p>"); 	
				out.println("<h3>ERROR</h3>"); 	
			}	
		}else{
			out.println("<p>Item: doesnt exists</p>"); 	
			out.println("<h3>ERROR</h3>"); 	
		}	
	}else{
		out.println("<p>Inventory: doesnt exist</p>"); 	
		out.println("<h3>ERROR</h3>"); 	
	}
	
	out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/inventory\">"+"<input type=\"hidden\" value=\""+request.getParameter("INID")+"\" name=\"INID\"><input type=\"submit\" value=\"Back to Inventory\"></form>");
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