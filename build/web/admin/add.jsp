<%
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
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Store Admin - Add a product</title>
    </head>
    <body>
        <h2>Add a Product</h2>
        <form method="post" action="/shop/addproduct">
            <p>Product Name: <input type="text" name="productname" maxlength="200"/><br/>
               Price: <input type="text" name="price"/><br/>
               ISBN: <input type="number" name="isbn"/><br/>
               Description:<br/>
               <textarea rows="10" cols="70" name="description"></textarea></p>
            <p><input type="submit" name="button" value="Save Product"/>
               <input type="submit" name="button" value="Cancel"/></p>
        </form>
    </body>
</html>
<%
        }
    }
%>