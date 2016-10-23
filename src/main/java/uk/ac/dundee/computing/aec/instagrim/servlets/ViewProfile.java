package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.lib.Convertors;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.models.User;
@WebServlet(name = "/ViewProfile", urlPatterns = {
	"/ViewProfile"

})
@MultipartConfig
public class ViewProfile extends HttpServlet 
{
	 Cluster cluster = null;
    //private HashMap CommandsMap = new HashMap();

    public ViewProfile() 
    {
        
    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
    	User us = new User();
    	us.setCluster(cluster);
    	HttpSession session = request.getSession();
    	LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
    	RequestDispatcher rd = request.getRequestDispatcher("/");
        request.setAttribute("Username", us.GetUsername(lg.getUsername()));
        request.setAttribute("Firstname", us.GetUserFirstname(lg.getUsername()));
        request.setAttribute("Lastname", us.GetUserLastname(lg.getUsername()));
        request.setAttribute("DOB", us.GetDOB(lg.getUsername()));
        request.setAttribute("Tel", us.GetMobile(lg.getUsername()));
        request.setAttribute("Status", us.GetStatus(lg.getUsername()));
        request.setAttribute("Email", us.GetEmail(lg.getUsername()));
        rd.forward(request,response);
    }

}