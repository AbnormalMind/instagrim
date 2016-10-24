package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.Cluster;
import java.io.IOException;
import java.io.PrintWriter;
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
import uk.ac.dundee.computing.aec.instagrim.lib.CassandraHosts;
import uk.ac.dundee.computing.aec.instagrim.models.User;
import uk.ac.dundee.computing.aec.instagrim.stores.LoggedIn;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import uk.ac.dundee.computing.aec.instagrim.lib.AeSimpleSHA1;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
import uk.ac.dundee.computing.aec.instagrim.models.PicModel;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.UUID;



@WebServlet(name = "Like", urlPatterns = {
    "/Images/Like"
})
@MultipartConfig
public class Like extends HttpServlet {

    Cluster cluster;
    UUID saveUUID = null;

    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        cluster = CassandraHosts.getCluster();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //String path = request.getParameter("currentUrl");
        User us=new User();
        us.setCluster(cluster);
        HttpSession session = request.getSession();
        LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        UUID picid = UUID.fromString(request.getParameter("picid"));
        saveUUID = picid;
        String username = request.getParameter("username");
        System.out.println("picid and username in Likes post: " + picid + username);
        boolean add = userLikedPic (picid,username);
        addLike(picid, username, add);
        response.sendRedirect("/Instagrim/Images/"+lg.getUsername());
    }

    public boolean userLikedPic (UUID picid, String username)
    {
        /*
        Session session = cluster.connect("instagrim");

        PreparedStatement ps = new session.prepare("UPDATE Pics SET likeuser = likeuser - {'?'} WHERE picid = ?;");
        BoundStatement boundStatement = new BoundStatement(psU); 
        session.execute(boundStatement.bind(likesNow, picid));  
        */

        return false;

    }

    public boolean addLike(UUID picid, String username, boolean add)
    {
        //this is the string that's returned by an empty input box: da39a3ee5e6b4b0d3255bfef95601890afd80709
        Session session = cluster.connect("instagrim");
        //get likes at current time
        int likesNow=getLikes(picid);
        //add like if add==true else remove a like
        if (userLikedPic(picid, username)==false)
        {
            //add like
            likesNow=likesNow+1;
        }
        /*
        else
        {
            Session session2 = cluster.connect("instagrim");

            //remove like
            likesNow=likesNow-1;
            //remove user from liked users in PIC
            PreparedStatement psU = new session.prepare("UPDATE Pics SET likeuser = likeuser - {'?'} WHERE picid = ?;");
            BoundStatement boundStatementU = new BoundStatement(psU);
            session2.execute(boundStatementU.bind(username, picid));
        }
        */
        
        PreparedStatement ps = session.prepare("UPDATE Pics SET likes=? WHERE picid = ?");
        BoundStatement boundStatement = new BoundStatement(ps);
        session.execute(boundStatement.bind(likesNow, picid));
        return true;
    }

    public int getLikes(UUID picid)
    {
        int likes=0;

        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select likes from Pics where picid =?");
        ResultSet rs=null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs=session.execute(boundStatement.bind(picid));
         if (rs.isExhausted())
         {
            System.out.println("No Images returned");
         }
         else
         {
            for (Row row : rs) {

                likes = row.getInt("likes");
            }
        }
        return likes;   
    }

       public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        HttpSession session=request.getSession();
        //LoggedIn lg = (LoggedIn) session.getAttribute("LoggedIn");
        UUID picid = saveUUID;
        System.out.println("Picture ID "+picid+" Number of Likes "+getLikes(picid));
        String stored = Integer.toString(getLikes(picid));
        System.out.println(stored);
        RequestDispatcher rd=request.getRequestDispatcher("/UsersPics.jsp");
        request.setAttribute("Likes", stored);
        rd.forward(request,response);
    }


}