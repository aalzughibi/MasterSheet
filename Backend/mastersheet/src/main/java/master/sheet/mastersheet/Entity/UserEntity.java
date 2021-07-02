package master.sheet.mastersheet.Entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.*;

import master.sheet.mastersheet.payload.request.SignUpRequest;
import master.sheet.mastersheet.payload.request.UpdateUserRequest;

@Entity
@Table(name="User")
public class UserEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "username",nullable = false,length=15,unique = true)
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    private int role;
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "BirthDate")
    @Temporal(TemporalType.DATE)
    private Date BirthDate;
    @Column(name = "display_name")
    private String display_name;
    @Column(name = "first_time")
    private boolean first_time;
    @Column(name = "uid",nullable=false)
    private String uid;
    
public UserEntity(){

}
public UserEntity(SignUpRequest sr) throws ParseException{
username = sr.getUsername();
email = sr.getEmail();
role = sr.getRole();
first_name = sr.getFirst_name();
last_name=sr.getLast_name();
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    BirthDate = formatter.parse(sr.getBirthDate());

display_name = sr.getDisplay_name();
}
public UserEntity(UpdateUserRequest sr) throws ParseException{
    email = sr.getEmail();
    role = sr.getRole();
    first_name = sr.getFirst_name();
    last_name=sr.getLast_name();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        BirthDate = formatter.parse(sr.getBirthDate());
    
    display_name = sr.getDisplay_name();
    }
    /**
     * @return Long return the Id
     */
    public Long getId() {
        return Id;
    }

    /**
     * @param Id the Id to set
     */
    public void setId(Long Id) {
        this.Id = Id;
    }

    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "{" +
            " Id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", role='" + getRole() + "'" +
            ", first_name='" + getFirst_name() + "'" +
            ", last_name='" + getLast_name() + "'" +
            ", BirthDate='" + getBirthDate() + "'" +
            ", display_name='" + getDisplay_name() + "'" +
            ", first_time='" + isFirst_time() + "'" +
            ", uid='" + getUid() + "'" +
            "}";
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
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return int return the role
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
     * @return Date return the BirthDate
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
     * @return boolean return the first_time
     */
    public boolean isFirst_time() {
        return first_time;
    }

    /**
     * @param first_time the first_time to set
     */
    public void setFirst_time(boolean first_time) {
        this.first_time = first_time;
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
