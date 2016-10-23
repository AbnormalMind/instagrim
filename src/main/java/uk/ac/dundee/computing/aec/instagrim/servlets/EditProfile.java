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

@WebServlet(name = "EditProfile", urlPatterns = {
	"/EditProfile"

})
@MultipartConfig
public class EditProfile extends HttpServlet 
{

    Cluster cluster=null;
    //private HashMap CommandsMap = new HashMap();

    public EditProfile() 
    {
        
    }



    public void init(ServletConfig config) throws ServletException 
    {
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        // something that holds the picture
        String page = "/Instagrim/ViewProfile";
        HttpSession session=request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        String username = lg.getUsername();
        String dob = request.getParameter("dob");
        String tel = request.getParameter("tel");
        String status = request.getParameter("status");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstname = request.getParameter("firstname");
        String lastname = request.getParameter("lastname");
        User us = new User();
        us.setCluster(cluster);
        boolean isValidPassword = us.IsValidPassword(password);
        if(tel.compareTo("") == 0) {
            page = "/Instagrim/ViewProfile";
        }
        if(tel.length()!=10 && tel.compareTo("") != 0) {
            page = "/Instagrim/TelephoneIsWrong";
        }
        if(isValidPassword == false && password.compareTo("") != 0){
            page = "/Instagrim/Authentication Failed";
        }
// ADDING PROFILE PIC
/*        for (Part part : request.getParts()) {
            System.out.println("Part Name " + part.getName());
            String type = part.getContentType();
            String filename = part.getSubmittedFileName();
            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session = request.getSession();
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            String username = "";
            if (lg.getlogedin()) {
                username = lg.getUsername();
            }
            if (i > 0) {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertProfilePic(b, type, filename, username);
                is.close();
            }
        }
*/
        us.EditUserProfile(firstname, lastname, username, password, dob, tel, status, email);
        response.sendRedirect(page);



        //RequestDispatcher rd = request.getRequestDispatcher("/Instagrim/Signup_OK");
        //rd.forward(request, response);
    }

}








     /*


        for (Part part : request.getParts()) 
        {
            System.out.println("Part Name " + part.getName());

            String type = part.getContentType();
            String filename = part.getSubmittedFileName();

            InputStream is = request.getPart(part.getName()).getInputStream();
            int i = is.available();
            HttpSession session = request.getSession();
            LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
            String username = "";
            if (lg.getlogedin()) 
            {
                username = lg.getUsername();
            }
            if (i > 0) 
            {
                byte[] b = new byte[i + 1];
                is.read(b);
                System.out.println("Length : " + b.length);
                PicModel tm = new PicModel();
                tm.setCluster(cluster);
                tm.insertPic(b, type, filename, username);

                is.close();
            }


     public EditProfile() 
     {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
    }*/



/*    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) 
        {
            case 1:
                DisplayProfilePic(Convertors.DISPLAY_PROCESSED, args[2], response);
            case 2:
                DisplayProfilePic(Convertors.DISPLAY_THUMB, args[2], response);
            default:
                error("Bad Operator", response);
        }
    } 

    private void DisplayProfilePic(int type, String Image, HttpServletResponse response) throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);

        Pic p = tm.getPic(type, java.util.UUID.fromString(Image));

        OutputStream out = response.getOutputStream();

        response.setContentType(p.getType());
        response.setContentLength(p.getLength());
        //out.write(Image);
        InputStream is = new ByteArrayInputStream(p.getBytes());
        BufferedInputStream input = new BufferedInputStream(is);
        byte[] buffer = new byte[8192];
        for (int length = 0; (length = input.read(buffer)) > 0;) {
            out.write(buffer, 0, length);
        }
        out.close();
    }*/
