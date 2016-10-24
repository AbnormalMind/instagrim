/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.dundee.computing.aec.instagrim.stores;

import com.datastax.driver.core.utils.Bytes;
import java.nio.ByteBuffer;
import java.lang.Math;
import java.util.*;


/**
 *
 * @author Administrator
 */
public class Pic {

    private Set<String> commentUser = new HashSet<>();
    private Set<String> commentText = new HashSet<>();
    private int like = 0;
    private ByteBuffer bImage = null;
    private int length;
    private String type;
    private java.util.UUID UUID=null;
    
    public void Pic() {

    }
    public void setUUID(java.util.UUID UUID){
        this.UUID =UUID;
    }
    public java.util.UUID getUUID(){
        return UUID;
    }

    public String getSUUID(){
        return UUID.toString();
    }
    public void setPic(ByteBuffer bImage, int length,String type) {
        this.bImage = bImage;
        this.length = length;
        this.type=type;
    }

    public void setComment(Set<String> commentText, Set<String> commentUser){
        this.commentText = commentText;
        this.commentUser = commentUser;
    }

    public Set<String> getCommentText(){
        return commentText;
    }

    public Set<String> getCommentAuthor(){
        return commentUser;
    }

    public ByteBuffer getBuffer() {
        return bImage;
    }

    public int getLength() {
        return length;
    }
    
    public String getType(){
        return type;
    }

    public byte[] getBytes() {
         
        byte image[] = Bytes.getArray(bImage);
        return image;
    }

}
