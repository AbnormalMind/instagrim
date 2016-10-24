package uk.ac.dundee.computing.aec.instagrim.models;

/*
 * Expects a cassandra columnfamily defined as
 * use keyspace2;
 CREATE TABLE Tweets (
 user varchar,
 interaction_time timeuuid,
 tweet varchar,
 PRIMARY KEY (user,interaction_time)
 ) WITH CLUSTERING ORDER BY (interaction_time DESC);
 * To manually generate a UUID use:
 * http://www.famkruithof.net/uuid/uuidgen
 */
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.Bytes;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import javax.imageio.ImageIO;
import static org.imgscalr.Scalr.*;
import org.imgscalr.Scalr.Method;


import uk.ac.dundee.computing.aec.instagrim.lib.*;
import uk.ac.dundee.computing.aec.instagrim.stores.Pic;
//import uk.ac.dundee.computing.aec.stores.TweetStore;

public class PicModel {

    Cluster cluster;

    public void PicModel() {

    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }
/*
    public void insertProfilePic(byte[] b, String type, String name, String user){
        try {    
            Convertors convertor = new Convertors();
            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            Boolean success = (new File("/var/tmp/instagrim/profilepic/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/profilepic/" + picid));
            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into userprofiles ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,type,name) values(?,?,?,?,?,?,?,?,?,?,?)");
            //PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }
*/

    /*

    public boolean addComment(UUID picid, String commentText, String username)
    {
        //this is the string that's returned by an empty input box: da39a3ee5e6b4b0d3255bfef95601890afd80709

        Session session = cluster.connect("instagrim");
        //PreparedStatement ps = session.prepare("INSERT INTO piccomments SET comments = ? WHERE picid = ?");
        //PreparedStatement ps = session.prepare("insert into piccomments (commentid, picid,user,com_added,com_text) Values(?,?,?,?,?)");
        //PreparedStatement ps = session.prepare("UPDATE Pics SET comments = comments + ?, comuser = comuser + ? WHERE picid = ?");
        PreparedStatement ps1 = session.prepare("UPDATE Pics SET comments = comments + ? WHERE picid = ?");
        PreparedStatement ps2 = session.prepare("UPDATE Pics SET comuser = comuser + ? WHERE picid = ?");

        BoundStatement boundStatement1 = new BoundStatement(ps1);
        BoundStatement boundStatement2 = new BoundStatement(ps2);

        Set<String> toADDUsername = new HashSet<>();
        boolean add1 = toADDUsername.add(username);

        Set<String> toADDComment = new HashSet<>();
        boolean add2 = toADDComment.add(commentText);

        session.execute(boundStatement1.bind(toADDComment, picid));
        session.execute(boundStatement2.bind(toADDUsername, picid));
        return true;
    }


    */



    public boolean insertComment(UUID picid, String commentText, String username)
    {
        Session session = cluster.connect("instagrim");
        PreparedStatement psInsertComment = session.prepare("UPDATE Pics SET comments = comments + ? WHERE picid = ?");
        PreparedStatement psInsertComUser = session.prepare("UPDATE Pics SET comuser = comuser + ? WHERE picid = ?");
        BoundStatement boundStatementComment = new BoundStatement(psInsertComment);
        BoundStatement boundStatementComUser = new BoundStatement(psInsertComUser);

        Set<String> addComment = new HashSet<>();
        boolean add1 = addComment.add(commentText);
        Set<String> addUsername = new HashSet<>();
        boolean add2 = addUsername.add(username);
        session.execute(boundStatementComment.bind(addComment, picid));
        session.execute(boundStatementComUser.bind(addUsername, picid));
        System.out.println("COMMENT INSERTED!");
        return true;
    }

public Set<String> getUsersSet(UUID uuid){

    Set<String> userSet = new HashSet<String>(Arrays.asList(""));

    Session session = cluster.connect("instagrim");
    PreparedStatement ps = session.prepare("select comuser from Pics where picid =?");
    ResultSet rs = null;
    BoundStatement boundStatement = new BoundStatement(ps);
        rs=session.execute(boundStatement.bind(uuid));
         if (rs.isExhausted())
         {
            System.out.println("No Images returned");
         }
         else
         {
            //int i=0;
            for (Row row : rs) {
                //comments[i]=row.getString("com_text");
                //i++;
                //comment = row.getString("comments");
                userSet=row.getSet("comuser", String.class);
                System.out.println("Users which commented: " + userSet);
            }
        }
        return userSet;
    }


public Set<String> getComments(UUID uuid)
    {
        Set<String> commentset = new HashSet<String>(Arrays.asList(""));

        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select comments from Pics where picid =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs=session.execute(boundStatement.bind(uuid));
         if (rs.isExhausted())
         {
            System.out.println("No Images returned");
         }
         else
         {
            //int i=0;
            for (Row row : rs) {
                //comments[i]=row.getString("com_text");
                //i++;
                //comment = row.getString("comments");
                commentset=row.getSet("comments", String.class);
                System.out.println("The Comments: " + commentset);
            }
        }
        return commentset;
    }


    public void insertPic(byte[] b, String type, String name, String user) {
        int l = 0;
        try {

            Convertors convertor = new Convertors();
            String types[]=Convertors.SplitFiletype(type);
            ByteBuffer buffer = ByteBuffer.wrap(b);
            int length = b.length;
            java.util.UUID picid = convertor.getTimeUUID();
            
            //The following is a quick and dirty way of doing this, will fill the disk quickly !
            Boolean success = (new File("/var/tmp/instagrim/")).mkdirs();
            FileOutputStream output = new FileOutputStream(new File("/var/tmp/instagrim/" + picid));

            output.write(b);
            byte []  thumbb = picresize(picid.toString(),types[1]);
            int thumblength= thumbb.length;
            ByteBuffer thumbbuf=ByteBuffer.wrap(thumbb);
            byte[] processedb = picdecolour(picid.toString(),types[1]);
            ByteBuffer processedbuf=ByteBuffer.wrap(processedb);
            int processedlength=processedb.length;
            Session session = cluster.connect("instagrim");

            PreparedStatement psInsertPic = session.prepare("insert into pics ( picid, image,thumb,processed, user, interaction_time,imagelength,thumblength,processedlength,likes,type,name) values(?,?,?,?,?,?,?,?,?,?,?,?)");
            PreparedStatement psInsertPicToUser = session.prepare("insert into userpiclist ( picid, user, pic_added) values(?,?,?)");
            BoundStatement bsInsertPic = new BoundStatement(psInsertPic);
            BoundStatement bsInsertPicToUser = new BoundStatement(psInsertPicToUser);

            Date DateAdded = new Date();
            session.execute(bsInsertPic.bind(picid, buffer, thumbbuf,processedbuf, user, DateAdded, length,thumblength,processedlength,l, type, name));
            session.execute(bsInsertPicToUser.bind(picid, user, DateAdded));
            session.close();

        } catch (IOException ex) {
            System.out.println("Error --> " + ex);
        }
    }

    public byte[] picresize(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage thumbnail = createThumbnail(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(thumbnail, type, baos);
            baos.flush();
            
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }
    
    public byte[] picdecolour(String picid,String type) {
        try {
            BufferedImage BI = ImageIO.read(new File("/var/tmp/instagrim/" + picid));
            BufferedImage processed = createProcessed(BI);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(processed, type, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException et) {

        }
        return null;
    }

    public static BufferedImage createThumbnail(BufferedImage img) {
        img = resize(img, Method.SPEED, 250, OP_ANTIALIAS, OP_GRAYSCALE);
        // Let's add a little border before we return result.
        return pad(img, 2);
    }
    
   public static BufferedImage createProcessed(BufferedImage img) {
        int Width=img.getWidth()-1;
        img = resize(img, Method.SPEED, Width, OP_ANTIALIAS, OP_GRAYSCALE);
        return pad(img, 4);
    }
   // GETS THE PICTURES FOR THE CURRENT USER
    public java.util.LinkedList<Pic> getPicsForUser(String User) {
        java.util.LinkedList<Pic> Pics = new java.util.LinkedList<>();
        Session session = cluster.connect("instagrim");
        PreparedStatement ps = session.prepare("select picid from userpiclist where user =?");
        ResultSet rs = null;
        BoundStatement boundStatement = new BoundStatement(ps);
        rs = session.execute( // this is where the query is executed
                boundStatement.bind( // here you are binding the 'boundStatement'
                        User));
        if (rs.isExhausted()) {
            System.out.println("No Images returned");
            return null;
        } else {
            for (Row row : rs) {
                Pic pic = new Pic();
                java.util.UUID UUID = row.getUUID("picid");
                System.out.println("UUID" + UUID.toString());
                pic.setUUID(UUID);
                //pic.setCommentUUID(UUID);
                Pics.add(pic);

            }
        }
        return Pics;
    }
    // GET DATA FROM DATA
    public Pic getPic(int image_type, java.util.UUID picid) {

        Session session = cluster.connect("instagrim");
        ByteBuffer bImage = null;
        String type = null;
        int length = 0;
        try {
            Convertors convertor = new Convertors();
            ResultSet rs = null;
            PreparedStatement ps = null;

            
            if (image_type == Convertors.DISPLAY_IMAGE) {
                
                ps = session.prepare("select image,imagelength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_THUMB) {
                ps = session.prepare("select thumb,imagelength,thumblength,type from pics where picid =?");
            } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                ps = session.prepare("select processed,processedlength,type from pics where picid =?");
            }            

            BoundStatement boundStatement = new BoundStatement(ps);

            rs = session.execute( // this is where the query is executed
                    boundStatement.bind( // here you are binding the 'boundStatement'
                            picid));


            if (rs.isExhausted()) {
                System.out.println("No Images returned");
                return null;
            } else {
                for (Row row : rs) {
                    if (image_type == Convertors.DISPLAY_IMAGE) {
                        bImage = row.getBytes("image");
                        length = row.getInt("imagelength");
                    } else if (image_type == Convertors.DISPLAY_THUMB) {
                        bImage = row.getBytes("thumb");
                        length = row.getInt("thumblength");
                
                    } else if (image_type == Convertors.DISPLAY_PROCESSED) {
                        bImage = row.getBytes("processed");
                        length = row.getInt("processedlength");
                    }
                    
                    type = row.getString("type");



                    /*
                    Set<String> commentText = new HashSet<>();
                    Set<String> commentUsers = new HashSet<>();
                    PreparedStatement ps1 = null;
                    ps1 = session.prepare("select comments,comuser pics where picic =?");
                    BoundStatement boundStatement1 = new BoundStatement(ps1);
                    if (rs.isExhausted()) {
                    System.out.println("No Images returned");
                    return null;
                    }
                    else {
                        for (Row row : rs)
                        {
                            commentText = row.getSet("comments");
                            commentUsers = row.getSet("comuser");
                        }
                    }
                    */

                }
            }
        } catch (Exception et) {
            System.out.println("Can't get Pic" + et);
            return null;
        }
        session.close();
        Pic p = new Pic();
        p.setPic(bImage, length, type);

        /*
            p.setComment();
        */

        return p;

    }


///////////////////////////////     LIKES       //////////////////////////////////////////////////////////

    public boolean userLikedPic (java.util.UUID picid, String username)
    {
        /*
        Session session = cluster.connect("instagrim");

        PreparedStatement ps = new session.prepare("UPDATE Pics SET likeuser = likeuser - {'?'} WHERE picid = ?;");
        BoundStatement boundStatement = new BoundStatement(psU); 
        session.execute(boundStatement.bind(likesNow, picid));  
        */

        return false;

    }

    public boolean addLike(java.util.UUID picid, String username, boolean add)
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

    public int getLikes(java.util.UUID picid)
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

}
