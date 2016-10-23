<%-- 
    Document   : upload
    Created on : Sep 22, 2014, 6:31:50 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link rel="stylesheet" href="css/reset.css"> 
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <h1>InstaGrim !</h1>
        <h2>Your world in Black and White</h2>
        <nav>
            <ul>
                <%
                LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                //String UserName = lg.getUsername();
                %>
                <li class="nav"><a href="/Instagrim/Upload">Upload</a></li>
                <li class="nav"><a href="/Instagrim/Images/<%=lg.getUsername()%>">Your Images</a></li>
            </ul>
        </nav>
 
        <article>
            <h3>File Upload</h3>
            <form method="POST" enctype="multipart/form-data" action="Image">
                File to upload: <input type="file" name="upfile">
                <br/>
                <br/>
                <input type="submit" value="Press"> to upload the file!
                <input type="hidden" name="currentUrl" value="<%=request.getAttribute("javax.servlet.forward.request_uri")%>">
                <input type="hidden" name="hiddenParam" value="upload">
            </form>

        </article>
        <footer>
            <ul>
                <li class="footer"><a href="/Instagrim">Home</a></li>
            </ul>
        </footer>
    </body>
</html>
