package master.sheet.mastersheet.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@Entity
@Table(name="po")
public class PoEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "po_id",unique = true,nullable=false)
    private String po_id;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date start_date;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date end_date;
    

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

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
     * @return Date return the start_date
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
     * @return Date return the end_date
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
