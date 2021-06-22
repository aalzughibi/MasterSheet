package master.sheet.mastersheet.User;

import java.util.Date;

public class User{
    private String username;
    private String email;
    private int role;
    private String first_name;
    private String last_name;
    private Date BirthDate;
    private String display_name;
    private int first_time;
    private String uid;
    public User(){
        username = " ";
        email=" ";
        role=-1;
        first_name=" ";
        last_name = " ";
        // BirthDate=" ";
        display_name=" ";
        first_time=-1;
        uid=" ";
    }
    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    public int getFirst_time() {
        return first_time;
    }

    public void setFirst_time(int first_time) {
        this.first_time = first_time;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return String return the role
     */
    public int getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(int role) {
        this.role = role;
    }

    /**
     * @return String return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    /**
     * @return String return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return String return the BirthDate
     */
    public Date getBirthDate() {
        return BirthDate;
    }

    /**
     * @param BirthDate the BirthDate to set
     */
    public void setBirthDate(Date BirthDate) {
        this.BirthDate = BirthDate;
    }

    /**
     * @return String return the display_name
     */
    public String getDisplay_name() {
        return display_name;
    }

    /**
     * @param display_name the display_name to set
     */
    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }


    /**
     * @return String return the uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

}