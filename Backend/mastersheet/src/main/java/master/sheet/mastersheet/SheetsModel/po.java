package master.sheet.mastersheet.SheetsModel;

import java.util.Date;

public class po {
    private String po_id;
    private Date start_date;
    private Date end_date;

    /**
     * @return String return the po_id
     */
    public String getPo_id() {
        return po_id;
    }

    /**
     * @param po_id the po_id to set
     */
    public void setPo_id(String po_id) {
        this.po_id = po_id;
    }

    /**
     * @return String return the start_date
     */
    public Date getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    /**
     * @return String return the end_date
     */
    public Date getEnd_date() {
        return end_date;
    }

    /**
     * @param end_date the end_date to set
     */
    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

}
