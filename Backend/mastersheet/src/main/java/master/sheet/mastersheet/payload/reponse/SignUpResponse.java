package master.sheet.mastersheet.payload.reponse;

public class SignUpResponse {
    private String uid;
    public SignUpResponse(String uid){
        this.uid=uid;
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
