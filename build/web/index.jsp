<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shop</title>
    </head>
    <body>
         <%
            if (request.getParameter("error")!=null)
            {
                switch (request.getParameter("error")) 
                {
                    case "1":
                        out.println("<p>Your account has been created.   Please login.</p>");
                        break;
                    case "2":
                        out.println("<p>Incorrect username or password.</p>");
                        break;
                }
            }
        %>
        <form method="post" action="/shop/login">
            <p>Username: <input type="text" name="username" maxlength="20"/><br/>
                Password: <input type="password" name="password"/></p>
            <p><input type="submit" value="Login"/></p>
        </form>
        <p><a href="newacct.jsp">Click to create an account</a></p>
    </body>
</html>
