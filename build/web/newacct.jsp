<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create An Account</title>
    </head>
    <body>
        <%
            if (request.getParameter("error")!=null)
            {
                switch (request.getParameter("error")) 
                {
                    case "1":
                        out.println("<p>Sorry, your password and verification didn't match.   Try again.</p>");
                        break;
                    case "2":
                        out.println("<p>Sorry, that account name already exists.   Please choose another.</p>");
                        break;
                }
            }
        %>
        <form method="post" action="/shop/createacct">
            <p>Username: <input type="text" name="username" maxlength="20"/><br/>
               Password: <input type="password" name="password1"/><br/>
               Password (for verification): <input type="password" name="password2"/></p>
            <p><input type="submit" name="button" value="Create Account"/>
               <input type="submit" name="button" value="Cancel"/></p>
        </form>
        <p>
    </body>
</html>
