package master.sheet.mastersheet.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@Entity
@Table(name="item")
public class ItemEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "item_id",unique = true,nullable=false)
    private String itemId;
    @Column(name = "item_type")
    private String item_type;
    @Column(name = "item_name")
    private String item_name;
    @Column(name = "project_id")
    private String project_id;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date start_date;
    @Column(name = "item_remarks")
    private String item_remarks;
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date end_date;
    @Column(name = "po_remarks")
    private String po_remarks;
    @Column(name = "po_no")
    private String po_no;
    @Column(name = "po_value")
    private String po_value;
    @Column(name = "payment_value",nullable=true)
    private String payment_value;
    @Column(name = "payment_date",nullable=true)
    private Date payment_date;
    @Column(name = "version")
	@Version
	private Long version;
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
     * @return String return the item_id
     */
    public String getItem_id() {
        return itemId;
    }

    /**
     * @param item_id the item_id to set
     */
    public void setItem_id(String item_id) {
        this.itemId = item_id;
    }

    /**
     * @return String return the item_type
     */
    public String getItem_type() {
        return item_type;
    }

    /**
     * @param item_type the item_type to set
     */
    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    /**
     * @return String return the item_name
     */
    public String getItem_name() {
        return item_name;
    }

    /**
     * @param item_name the item_name to set
     */
    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    /**
     * @return String return the project_id
     */
    public String getProject_id() {
        return project_id;
    }

    /**
     * @param project_id the project_id to set
     */
    public void setProject_id(String project_id) {
        this.project_id = project_id;
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
     * @return String return the item_remarks
     */
    public String getItem_remarks() {
        return item_remarks;
    }

    /**
     * @param item_remarks the item_remarks to set
     */
    public void setItem_remarks(String item_remarks) {
        this.item_remarks = item_remarks;
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

    /**
     * @return String return the po_remarks
     */
    public String getPo_remarks() {
        return po_remarks;
    }

    /**
     * @param po_remarks the po_remarks to set
     */
    public void setPo_remarks(String po_remarks) {
        this.po_remarks = po_remarks;
    }

    /**
     * @return String return the po_no
     */
    public String getPo_no() {
        return po_no;
    }

    /**
     * @param po_no the po_no to set
     */
    public void setPo_no(String po_no) {
        this.po_no = po_no;
    }

    /**
     * @return String return the po_value
     */
    public String getPo_value() {
        return po_value;
    }

    /**
     * @param po_value the po_value to set
     */
    public void setPo_value(String po_value) {
        this.po_value = po_value;
    }


    /**
     * @return String return the payment_value
     */
    public String getPayment_value() {
        return payment_value;
    }

    /**
     * @param payment_value the payment_value to set
     */
    public void setPayment_value(String payment_value) {
        this.payment_value = payment_value;
    }

    /**
     * @return Date return the payment_date
     */
    public Date getPayment_date() {
        return payment_date;
    }

    /**
     * @param payment_date the payment_date to set
     */
    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }


    /**
     * @return String return the itemId
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @param itemId the itemId to set
     */
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    /**
     * @return Long return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

}
