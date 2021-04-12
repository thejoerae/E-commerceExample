package cis266;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

public class EditProduct extends HttpServlet {

    
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
                String productname, price, isbn, description;
                try
                {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection dbConnect = DriverManager.getConnection(
                        "jdbc:mysql://cis.luzerne.edu/cis266?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        "cis266","Password");
                    Statement dbState = dbConnect.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
                    ResultSet dbRs = dbState.executeQuery("select * from inventory where isbn=" + request.getParameter("isbn"));
                    if (dbRs.next())
                    {
                        productname=dbRs.getString("productname");
                        price=dbRs.getString("price");
                        isbn=dbRs.getString("isbn");
                        description=dbRs.getString("description");
                        out.println("<html><head><title>E-Commerce</title></head><body>");
                        out.println("<form method=\"post\" action=\"/shop/editproduct?origisbn=" + isbn + "\">");
                        out.println("<p>Product Name: <input type=\"text\" name=\"productname\" maxlength=\"200\" value=\"" + 
                                     productname + "\"/><br/>");
                        out.println("Price: <input type=\"text\" name=\"price\" value=\"" + price + "\"/><br/>");
                        out.println("ISBN: <input type=\"number\" name=\"isbn\" value=\"" + isbn + "\"/><br/>");
                        out.println("Description:<br/>");
                        out.println("<textarea rows=\"10\" cols=\"70\" name=\"description\">" + description + "</textarea></p>");
                        out.println("<p><input type=\"submit\" name=\"button\" value=\"Save Changes\"/>");
                        out.println("<input type=\"submit\" name=\"button\" value=\"Cancel\"/></p>");
                        out.println("</form>");
                        out.println("</body></html>");
                    }
                    else
                    {
                        // Display error because record was deleted before we got here.
                        response.sendRedirect("/shop/admin");
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
                out.close();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
                String productname, price, isbn, description, origisbn;
                if (request.getParameter("button").equals("Cancel"))
                {
                    response.sendRedirect("/shop/admin");
                }
                else
                {
                    try
                    {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection dbConnect = DriverManager.getConnection(
                            "jdbc:mysql://cis.luzerne.edu/cis266?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                            "cis266","Password");
                        Statement dbState = dbConnect.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
                        ResultSet dbRs = dbState.executeQuery("select * from inventory where isbn=" + request.getParameter("origisbn"));
                        if (dbRs.next())
                        {
                            dbRs.updateString("productname", request.getParameter("productname"));
                            dbRs.updateString("description", request.getParameter("description"));
                            dbRs.updateLong("isbn", Long.parseLong(request.getParameter("isbn")));
                            dbRs.updateFloat("price",Float.parseFloat(request.getParameter("price")));
                            dbRs.updateRow();
                            dbRs.close();
                        }
                        dbState.close();
                        dbConnect.close();
                        response.sendRedirect("/shop/admin");
                    }
                    catch (SQLException e)
                    {
                       System.out.println("Error: " + e.getMessage());
                    }
                    catch (ClassNotFoundException e)
                    {
                       System.out.println("ERROR: Can't load driver!");
                    }
                }
            }
        }
    }

}
