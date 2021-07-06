package master.sheet.mastersheet.Service.interfaces;

import java.util.List;

import master.sheet.mastersheet.Entity.PoEntity;

public interface PoInterface {
    public List<PoEntity> getAllPos();
    public PoEntity getPoById(String po_id)throws Exception;
    public PoEntity updatePo(PoEntity Po)throws Exception;
    public PoEntity insertPo(PoEntity Po);
    public boolean isExist(String po_id);
}
