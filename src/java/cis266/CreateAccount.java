package cis266;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CreateAccount extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!request.getParameter("password1").equals(request.getParameter("password2")))
        {
            response.sendRedirect("/shop/newacct.jsp?error=1");
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
                ResultSet dbRs = dbState.executeQuery("select * from accounts where accountname='" 
                                                      + request.getParameter("username") + "'");
                if (dbRs.next())
                {
                    // Account already exists
                    response.sendRedirect("/shop/newacct.jsp?error=2");
                }
                else
                {
                    dbRs.moveToInsertRow();
                    dbRs.updateString("accountname", request.getParameter("username"));
                    String password=request.getParameter("username") + request.getParameter("password1") + 
                            request.getParameter("username");
                    MessageDigest hash = MessageDigest.getInstance("SHA-512");
                    hash.reset();
                    hash.update(password.getBytes("UTF-8"));
                    String passwordHash;
                    passwordHash=String.format("%040x", new BigInteger(1,hash.digest()));
                    for (int iteration=1; iteration<=150000; iteration++)
                    {
                        hash.update(passwordHash.getBytes("UTF-8"));
                        passwordHash=String.format("%040x", new BigInteger(1,hash.digest()));
                    }
                    dbRs.updateString("password", passwordHash);
                    dbRs.insertRow();
                    response.sendRedirect("/shop/index.jsp?error=1");
                }
                dbRs.close();
                dbState.close();
                dbConnect.close();
            }
            catch (SQLException e)
            {
               System.out.println("Error: " + e.getMessage());
            }
            catch (ClassNotFoundException e)
            {
               System.out.println("ERROR: Can't load driver!");
            }
            catch (NoSuchAlgorithmException e)
            {
                System.out.println("Error: Hashing algorithm not present.");
            }
        }
    }
}
