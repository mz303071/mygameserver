// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class DoAddCharacterServlet extends HttpServlet {  // JDK 6 and above only
 
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
	 ResultSet urset = stmt.executeQuery(sqlStr);  // Send the query to the server
         out.println("<h3>Creating Character</h3>");
	 
 	 if(urset.next()){
		String PID=urset.getString("PID");
		out.println("user exists");
		ResultSet crset=stmt.executeQuery("select * from characters where CharacterName='"+request.getParameter("cname")+"'");
		if(request.getParameter("cname")==null){
			out.println("Character Name cant be null");
		}else if(request.getParameter("cname").length()==0){
			out.println("Character Name cant be empty");
		}else if(crset.next()){
			out.println("Character Name already exists");
		}else{
			
				out.println("Updating1");
			stmt.executeUpdate("insert into characters (CharacterName,CharacterLevel,search_RID) values('"+request.getParameter("cname")+ "',1,"+request.getParameter("rid")+")");
			out.println("Updating2");
			ResultSet charinfo=stmt.executeQuery("select * from characters where CharacterName='"+request.getParameter("cname")+"'");
			charinfo.next();
String CID=charinfo.getString("CID");
			stmt.executeUpdate("insert into playerownscharacters (search_PID,search_CID) values('"+PID+ 

"','"+CID+"')");
			out.println("Success");
			out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/character\">");
			out.println("<input type=\"hidden\" name=\"cname\" value=\""+charinfo.getString("CID")+"\">");
			out.println("<input type=\"submit\" value=\"view Character\"></form>");
		}
	} else{
		out.println("Error No user Exists");
	}
        out.println("</body></html>");
     } catch (SQLException ex) {
        out.println("<dialog open>");
	StringWriter sw = new StringWriter();
	ex.printStackTrace(new PrintWriter(sw));
	out.println(sw.toString());
	out.println("</dialog>");
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