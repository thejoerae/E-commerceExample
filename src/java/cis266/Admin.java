package cis266;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

public class Admin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.getAttribute("login")==null)
        {
            response.sendRedirect("/shop/index.jsp");
        }
        else
        {
            if (((String)session.getAttribute("admin")).equals("0"))
            {
                response.sendRedirect("/shop/main");
            }
            else
            {
                PrintWriter out = response.getWriter();
                response.setContentType("text/html");
                out.println("<html><head><title>Store Admin</title></head><body>");
                out.println("<h2>Online Products</h2>");
                out.println("<p><a href=\"/shop/admin/add.jsp\">Add a new product</a></p>");
                out.println("<table>");
                out.println("<tr><th>Product Name</th><th>Price</th><th>ISBN</th><th></th></tr>");
                try
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection dbConnect = DriverManager.getConnection(
                        "jdbc:mysql://cis.luzerne.edu/cis266?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "cis266","Password");
                    Statement dbState = dbConnect.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
                    ResultSet dbRs = dbState.executeQuery("select * from inventory order by productname");
                    while (dbRs.next())
                    {
                        out.println("<tr><td>" + dbRs.getString("productname") + "</td><td>" + 
                                dbRs.getString("price") + "</td><td>" + dbRs.getString("isbn") + "</td>" + 
                                "<td>[<a href=\"/shop/editproduct?isbn=" + dbRs.getString("isbn") + 
                                "\">edit</a>] [<a href=\"/shop/deleteproduct?isbn=" + dbRs.getString("isbn") + "\">delete</a>]</td></tr>");
                    }
                    dbRs.close();
                    dbState.close();
                    dbConnect.close();
                }
                catch (SQLException e)
                {
                    out.println("<p>Error: " + e.getMessage() + "</p>");
                }
                catch (ClassNotFoundException e)
                {
                    out.println("<p>ERROR: Can't load driver!</p>");
                }
                out.println("</table>");
                out.println("</body></html>");
                out.close();
            }
        }
    }
}
