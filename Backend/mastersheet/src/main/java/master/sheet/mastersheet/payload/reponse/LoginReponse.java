package master.sheet.mastersheet.payload.reponse;

public class LoginReponse {
    private String jwtToken;
    private int Role;
    private boolean first_time_login;
    private long expiredAt;

    /**
     * @return String return the jwtToken
     */
    public String getJwtToken() {
        return jwtToken;
    }

    /**
     * @param jwtToken the jwtToken to set
     */
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    /**
     * @return int return the Role
     */
    public int getRole() {
        return Role;
    }

    /**
     * @param Role the Role to set
     */
    public void setRole(int Role) {
        this.Role = Role;
    }

    /**
     * @return boolean return the first_time_login
     */
    public boolean isFirst_time_login() {
        return first_time_login;
    }

    /**
     * @param first_time_login the first_time_login to set
     */
    public void setFirst_time_login(boolean first_time_login) {
        this.first_time_login = first_time_login;
    }

    /**
     * @return int return the expiredAt
     */
    public long getExpiredAt() {
        return expiredAt;
    }

    /**
     * @param expiredAt the expiredAt to set
     */
    public void setExpiredAt(long expiredAt) {
        this.expiredAt = expiredAt;
    }

}
