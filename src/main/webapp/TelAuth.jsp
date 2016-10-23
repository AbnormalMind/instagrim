<%@page import="java.util.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServlet" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="javax.servlet.http.HttpSession" %>
<%@ page import="javax.servlet.http.Part" %>
<html>
<head>
<title>Authentication Failed</title>
</head>

<body>

<h1>Telephone Authentication Failed</h1>
<p>

<blockquote>

The URL you've requested,    <b><!--#echo var="REDIRECT_URL"--></b>
requires a correct Telephone number (10 digits), or your browser doesn't support 
this feature.
<p>
<%
LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
lg.refreshViewProfile(true);
%>
Please return to the home page and try again, 
<b><a href="/Instagrim"> Click
<!--#echo var="HTTP_REFERER"--></a></b> if you think this was a mistake.

</blockquote>

</body>
</html>