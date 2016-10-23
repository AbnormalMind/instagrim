package uk.ac.dundee.computing.aec.instagrim.servlets;

import com.datastax.driver.core.Cluster;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
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

/**
 * Servlet implementation class Image
 */
@WebServlet(urlPatterns = {
    "/Image",
    "/Image/*",
    "/Thumb/*",
    "/Images",
    "/Images/*"
})
@MultipartConfig

public class Image extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Cluster cluster;
    private HashMap CommandsMap = new HashMap();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Image() {
        super();
        // TODO Auto-generated constructor stub
        CommandsMap.put("Image", 1);
        CommandsMap.put("Images", 2);
        CommandsMap.put("Thumb", 3);

    }

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String args[] = Convertors.SplitRequestPath(request);
        int command;
        try {
            command = (Integer) CommandsMap.get(args[1]);
        } catch (Exception et) {
            error("Bad Operator", response);
            return;
        }
        switch (command) {
            case 1:
                DisplayImage(Convertors.DISPLAY_PROCESSED, args[2], response);
                break;
            case 2:
                DisplayImageList(args[2], request, response);
               // DisplayComments(request, response);
                break;
            case 3:
                DisplayImage(Convertors.DISPLAY_THUMB, args[2], response);
                break;
            default:
                error("Bad Operator", response);
        }
    }

    /*private void DisplayComments(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn"); 
        RequestDispatcher rd=request.getRequestDispatcher("/UsersPics.jsp");
        
        UUID picid=UUID.fromString(request.getParameter("picid"));
        ////// pic.setcommentText
        request.setAttribute("user", lg.getUsername());
        request.setAttribute("comments", tm.getComments(picid));
        rd.forward(request,response);
    }*/

    private void DisplayImageList(String User, HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        PicModel tm = new PicModel();
        tm.setCluster(cluster);
        UUID picid=UUID.fromString(request.getParameter("picid"));

        Set<String> commentsToShow = tm.getComments(picid);
        java.util.LinkedList<Pic> lsPics = tm.getPicsForUser(User);

        RequestDispatcher rd = request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Pics", lsPics);
        request.setAttribute("Comments", commentsToShow);
        request.setAttribute("User", User);
        //LIKES ATTEMPT
        //UUID picid = UUID.fromString(request.getParameter("picid"));

        rd.forward(request, response);
    }

    private void DisplayImage(int type, String Image, HttpServletResponse response) throws ServletException, IOException {
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
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getParameter("currentUrl");
        String hiddenParam=request.getParameter("hiddenParam");
        if(hiddenParam.compareTo("userpics") == 0){
            //form 1 was posted
            PicModel tm = new PicModel();
            tm.setCluster(cluster);
            UUID picid=UUID.fromString(request.getParameter("picid"));
            String commentText = request.getParameter("comment");
            String username = request.getParameter("username");
            tm.insertComment(picid,commentText,username);
            if(path.compareTo("/Instagrim/Upload") == 0)
            {
                RequestDispatcher rd = request.getRequestDispatcher("upload.jsp");
                rd.include(request, response);   
            }
            else
            {
                RequestDispatcher rd1 = request.getRequestDispatcher("/UsersPics.jsp");
                rd1.include(request, response);
            }
        }
        else if(hiddenParam.compareTo("upload") == 0){
            //form 2 was posted       
            for (Part part : request.getParts()) {
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
                    tm.insertPic(b, type, filename, username);

                    is.close();
                }
                if(path.compareTo("/Instagrim/Upload") == 0)
                {
                    RequestDispatcher rd = request.getRequestDispatcher("upload.jsp");
                    rd.include(request, response);   
                }
                else
                {
                    RequestDispatcher rd1 = request.getRequestDispatcher("/UsersPics.jsp");
                    rd1.include(request, response);
                }
            }
        }

        /*
  
        UUID picid=UUID.fromString(request.getParameter("picid"));

        request.setAttribute("user", lg.getUsername());
        request.setAttribute("comments", user.getComments(picid));

        rd.forward(request,response);

        */
    }

    private void error(String mess, HttpServletResponse response) throws ServletException, IOException {

        PrintWriter out = null;
        out = new PrintWriter(response.getOutputStream());
        out.println("<h1>You have a na error in your input</h1>");
        out.println("<h2>" + mess + "</h2>");
        out.close();
        return;
    }
}
