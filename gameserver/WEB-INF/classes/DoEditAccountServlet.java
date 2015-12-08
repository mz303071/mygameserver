// To save as "<TOMCAT_HOME>\webapps\hello\WEB-INF\classes\QueryServlet.java".
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class DoEditAccountServlet extends HttpServlet {  // JDK 6 and above only
 
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
 
         // Step 3: Check conditions
	 out.println("<html><head><title>Login</title></head><body>");
        String sqlPlayerExists = "select * from players where Username='" + request.getParameter("uname") +"';";
        ResultSet rplayerexists = stmt.executeQuery(sqlPlayerExists);
	out.println("Attempting Changes<br>");
	//out.println("Your query is"+sqlPlayerExists+"<br>");
	if(rplayerexists.next()){// does exist;
		out.println("<p>user exists</p>");
		sqlPlayerExists = "select * from players where Username='" + request.getParameter("duname") +"';";
 	 	//out.println("Your query is "+sqlPlayerExists);
		rplayerexists = stmt.executeQuery(sqlPlayerExists);
		if(request.getParameter("duname")==null){
			out.println("Error: Username cant be Null");
		}else if(request.getParameter("duname").length()==0){
			out.println("Error: Username cant be Empty");
		}else if(!request.getParameter("duname").equals(request.getParameter("uname"))&&rplayerexists.next()){
			out.println("<p>Error: username in use</p>");	
		}else{
			out.println("<p> username is availible</p>");
			if(request.getParameter("dpswrd")!=null&&request.getParameter("dpswrd").length()>0){
				out.println("Updating<br>");
				stmt.executeUpdate("UPDATE players SET Username='"+request.getParameter("duname")+
"', Password='"+request.getParameter("dpswrd")+"', Firstname='"+request.getParameter("dfname")+"', Lastname='"+request.getParameter("dlname")+"' WHERE Username='"+request.getParameter("uname")+"'");
				out.println("Success");
				out.println("<form method=\"get\" action=\"http://localhost:9999/gameserver/account\"><input type=\"hidden\" value=\""+request.getParameter("duname")+"\" name=\"uname\"><input type=\"hidden\" value=\""+request.getParameter("dpswrd")+"\" name=\"pswrd\"><input type=\"submit\" value=\"Go to Account\"></form>");
			}else{
				out.println("Error: password is invalid");
			}		
		}
	}else{
		out.println("<p>Error: user doesnt exist</p>");
	}
	 out.println("</table></body></html>");
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