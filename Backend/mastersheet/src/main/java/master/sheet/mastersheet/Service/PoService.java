package master.sheet.mastersheet.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.sheet.mastersheet.Entity.PoEntity;
import master.sheet.mastersheet.Repository.PoReposiory;
import master.sheet.mastersheet.Service.interfaces.PoInterface;

@Service
public class PoService implements PoInterface{
    @Autowired
    PoReposiory poReposiory;
    public List<PoEntity> getAllPos(){
        List<PoEntity> PoList = poReposiory.findAll();
        return PoList;
    }
    public PoEntity getPoById(String po_id)throws Exception{
        Optional<PoEntity> PoList = poReposiory.findByPoId(po_id);
        return PoList.get();
    }
    public PoEntity updatePo(PoEntity Po)throws Exception{
        PoEntity pe = getPoById(Po.getPo_id());
        if (pe !=null){
            pe.setStart_date(Po.getStart_date());
            pe.setEnd_date(Po.getEnd_date());
            pe = poReposiory.save(pe);
        return pe;
        }
        else
        throw new Exception("Po not Found");
    }
    public PoEntity insertPo(PoEntity Po){
       PoEntity pe= poReposiory.save(Po);
        return pe;
    }
    public boolean isExist(String po_id){
        return poReposiory.existsByPoId(po_id);
    }
}
