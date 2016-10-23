<%-- 
    Document   : UsersPics
    Created on : Sep 24, 2014, 2:52:48 PM
    Author     : Administrator
--%>

<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="javax.servlet.http.Part" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Instagrim</title>
        <link href="<%= request.getContextPath() %>/css/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <header>
        <h1>InstaGrim ! </h1>
        <h2>Your world in Black and White</h2>
        </header>
        <%
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        lg.refreshViewProfile(true);
        %>
        <nav>
            <ul>
                <a href="/Instagrim/Upload" class="submit">Upload</a>
                <a href="/Instagrim/Images/<%=lg.getUsername()%>" class="submit" >Sample Images</a>
                <a href="/Instagrim" class="submit">Home</a>
        <article>
            <h1>Your Pics</h1>
        <label>Number of Likes: <%=request.getAttribute("Likes")%></label>
        <%
            java.util.Set<String> comm = (java.util.Set<String>) request. getAttribute("commentsToShow");
            if(comm == null){ %> <p>No comments found.</p>
        <%
        } else {
            Iterator<String> it = comm.iterator();
            while(it.hasNext()) {
                String com = it.next();
        %>
        <p><b>Comment:</b><%=com%></p>
        <%
            java.util.LinkedList<Pic> lsPics = (java.util.LinkedList<Pic>) request.getAttribute("Pics");
            if (lsPics == null) {
        %>
        <p>No Pictures found</p>
        <%
        } else {
            Iterator<Pic> iterator;
            iterator = lsPics.iterator();
            while (iterator.hasNext()) {
                Pic p = (Pic) iterator.next();
        %>
        <form method="POST"  action="Like" class='containerPic'>
        <a href="/Instagrim/Image/<%=p.getSUUID()%>" ><img src="/Instagrim/Thumb/<%=p.getSUUID()%>"></a> 
        <input type="hidden" name="picid" value="<%=p.getSUUID()%>">
        <input type="hidden" name="username" value="<%=lg.getUsername()%>">
        <input type="submit" class='heart' value="♥">
        </form>
        <form method="POST" enctype="multipart/form-data" action="Image">
        <input type="hidden" name="currentUrl" value="<%=request.getAttribute("javax.servlet.forward.request_uri")%>">
        <textarea name="comment" id="comment" placeholder="Write your comment here"></textarea>
        
        <input type="hidden" name="picid" value="<%=p.getSUUID()%>">
        <input type="hidden" name="hiddenParam" value="userpics">
        <input type="hidden" name="username" value="<%=lg.getUsername()%>"> 
        <input type="submit" value="Submit a Comment" class="submit"> 
        </form>
        <div class='bubble'></div>
        <%
        }
        %>
        <%
        }
        %>
        </article>
        <div id="footer">
        Coursework for AC32007 by <strong>Atanas Chilingirov - Matriculation #14003353</strong>.
        </div>
        <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
        <script src="<%= request.getContextPath() %>/js/index.js"></script>
    </body>
</html>