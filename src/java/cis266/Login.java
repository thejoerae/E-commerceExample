package cis266;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Login extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try
        {
            HttpSession session = request.getSession();
            Class.forName("com.mysql.jdbc.Driver");
            Connection dbConnect = DriverManager.getConnection(
                "jdbc:mysql://cis.luzerne.edu/cis266?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                "cis266","Password");
            Statement dbState = dbConnect.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_READ_ONLY);
            ResultSet dbRs = dbState.executeQuery("select password, admin from accounts where accountname='" 
                                                  + request.getParameter("username") + "'");
            if (dbRs.next())
            {
                // Username exists - Check password
                String password=request.getParameter("username") + request.getParameter("password") + 
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
                if (dbRs.getString("password").equals(passwordHash))
                {
                    session.setAttribute("login", request.getParameter("username"));
                    session.setAttribute("admin", dbRs.getString("admin"));
                    if (dbRs.getInt("admin")==0)
                    {
                        response.sendRedirect("/shop/main");
                    }
                    else
                    {
                        response.sendRedirect("/shop/admin");
                    }
                }
                else
                {
                    // Bad password
                    response.sendRedirect("index.jsp?error=2");
                }
                dbRs.close();
            }
            else
            {
                // Username doesn't exist
                response.sendRedirect("index.jsp?error=2");
            }
            dbState.close();
            dbConnect.close();
        }
        catch (SQLException e)
        {
            
        }
        catch (ClassNotFoundException e)
        {
            
        }
        catch (NoSuchAlgorithmException e)
        {
            
        }
    }

}
