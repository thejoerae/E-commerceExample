package cis266;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

public class AddProduct extends HttpServlet {
   
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
                        ResultSet dbRs = dbState.executeQuery("select * from inventory limit 1");
                        dbRs.moveToInsertRow();
                        dbRs.updateString("productname", request.getParameter("productname"));
                        dbRs.updateFloat("price", Float.parseFloat(request.getParameter("price")));
                        dbRs.updateLong("isbn", Long.parseLong(request.getParameter("isbn")));
                        dbRs.updateString("description",request.getParameter("description"));
                        dbRs.insertRow();
                        dbRs.close();
                        dbState.close();
                        dbConnect.close();
                        response.sendRedirect("/shop/admin");
                    }
                    catch (SQLException e)
                    {
                        log("<p>Error: " + e.getMessage() + "</p>");
                    }
                    catch (ClassNotFoundException e)
                    {
                        log("<p>ERROR: Can't load driver!</p>");
                    }
                }
            }
        }
    }

}
