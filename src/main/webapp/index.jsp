<%-- 
    Document   : index
    Created on : Sep 28, 2014, 7:01:44 PM
    Author     : Atanas Chilingirov
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="uk.ac.dundee.computing.aec.instagrim.stores.*" %>
<%@page import="uk.ac.dundee.computing.aec.instagrim.servlets.*" %>
<!DOCTYPE html>
<html>
    <head>
    <title>InstaGrim</title>
    
    <link rel="stylesheet" href="css/reset.css"> 
    <link rel="stylesheet" href="css/style.css">

  </head>

  <body>

    <section class="container">
        <article class="half">
            <h1>InstaGrim</h1>
            <div class="tabs">
                <%
                LoggedIn log = (LoggedIn) session.getAttribute("LoggedIn");
                if(log == null)
                {
                %>
                <span class="tab signin active"><a href="#signin">Sign in</a></span>
                <span class="tab signup"><a href="#signup">Sign up</a></span>  
                <% 
                }
                %>
                <%
                LoggedIn lgo = (LoggedIn) session.getAttribute("LoggedIn");
                if (lgo != null) {
                if (lgo.getlogedin()) {
                %>
                <span class="tab editprofile"><a href="#editprofile">Edit profile</a></span>
                <span class="tab viewprofile"><a href="#viewprofile">View profile</a></span>
                <% 
                }
                }   
                %>  
            </div>
            <div class="content">
                <div class="signin-cont cont" id="signin-c">
                    <form method="POST" action="Login">
                        <input type="text" name="username" id="username" class="inpt" required="required" placeholder="Your username">
                        <input type="password" name="password" id="password" class="inpt" required="required" placeholder="Your password">
                        <div class="submit-wrap">
                        <input type="submit" value="Login" class="submit">
                        <a href="#" class="more">Forgot your password?</a>
                        </div>
                    </form>
                </div>
                <div class="signup-cont cont">
                    <form method="POST"  action="Register">
                        <input type="text" name="firstname" id="firstname" class="inpt" required="required" placeholder="Your name">
                        <input type="text" name="lastname" id="lastname" class="inpt" required="required" placeholder="Your last name">
                        <input type="text" name="username" id="name" class="inpt" required="required" placeholder="Your username">
                        <input type="email" name="email" id="email" class="inpt" required="required" placeholder="Your email">
                        <input type="password" name="password" id="password" class="inpt" required="required" placeholder="Your password">
                        <div class="submit-wrap">
                        <input type="submit" value="Sign Up" class="submit">
                        </div>
                    </form>
                </div>

                <div class="editprofile-cont cont">
                
                <% 
                LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
                if (lg != null) {
                
                if (lg.getlogedin()) {
                %>
                        <form method="POST" enctype="multipart/form-data" action="EditProfile">
                            <input type="file" id="file" class="inputfile" data-multiple-caption="{count} files selected" multiple>
                            <label for="file" id="filelable">Upload a Profile Picture </label>
                            <label for="date" id="doblabel">Enter your Date of Birth </label>
                            <input type="date" name="dob" id="dob" class="inpt">
                            <input type="text" name="firstname" id="firstname" class="inpt" placeholder="Change your first name">
                            <input type="text" name="lastname" id="lastname" class="inpt" placeholder="Change your last name">
                            <input type="number" name="tel" id="number" class="inpt" placeholder="Enter your mobile number">
                            <textarea rows="5" cols="32" name="status" id="textarea" class="inpt" placeholder="Update your status"></textarea>          
                            <input type="email" name="email" id="email" class="inpt" placeholder="Update your email">
                            <input type="password" name="password" id="password" class="inpt" placeholder="Change your password"> 
                            <div class="submit-wrap">
                            <input type="submit" value="Update" class="submit" id="update">
                            </div>
                            <a href = "/Instagrim/Images/<%=lg.getUsername()%>" class="submit" id="gallery">Gallery</a>
                            <a href = "/Instagrim/Logout" class="submit" id="logout">Logout</a>
                        </form>
                <% 
                }
                }   
                %>
                </div>
                <% 
                LoggedIn lgv = (LoggedIn) session.getAttribute("LoggedIn");
                if (lgv != null) {
                
                if (lgv.getlogedin()) {
                %>
                <div class="viewprofile-cont cont">
                <p><b>Username:</b><%=request.getAttribute("Username")%></p>
                <p><b>First Name:</b><%=request.getAttribute("Firstname")%></p>
                <p><b>Last Name:</b><%=request.getAttribute("Lastname")%></p>
                <p><b>Date of Birth:</b><%=request.getAttribute("DOB")%></p>
                <p><b>Mobile Number:</b><%=request.getAttribute("Tel")%></p>
                <p><b>Status:</b><%=request.getAttribute("Status")%></p>
                <p><b>Email:</b><%=request.getAttribute("Email")%></p>
                <% 
                LoggedIn lgr = (LoggedIn) session.getAttribute("LoggedIn");
                if (lgr != null) {
                if (lgr.getRefresh()) {
                response.setHeader("Refresh", "0;url=/Instagrim/ViewProfile");
                lgr.refreshV();
                }
                }
                %>
                </div>
                <% 
                }
                }   
                %>
            </div>
        </article>
            <div class="half bg"></div>
    </section>
            <%-- 
             else {
                
                <% } %>
            <img src="">
            <section class="right-container">
               
            </section>--%>


    <script src='http://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js'></script>
    <script src='http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js'></script>
    <script src="js/index.js"></script>

    <div class="footer">
        Coursework for AC32007 by <strong>Atanas Chilingirov - Matriculation #14003353</strong>.
    </div>

</body>


</html>

