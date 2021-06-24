package master.sheet.mastersheet.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import master.sheet.mastersheet.Entity.PoEntity;
import master.sheet.mastersheet.Repository.PoReposiory;

@Service
public class PoService {
    @Autowired
    PoReposiory poReposiory;
    public List<PoEntity> getAllPos(){
        List<PoEntity> PoList = poReposiory.findAll();
        return PoList;
    }
    public PoEntity getPoById(String po_id)throws Exception{
        List<PoEntity> PoList = getAllPos();
        for(PoEntity ul:PoList){
            if(ul.getPo_id().equals(po_id))
                return ul;
        }
        throw new Exception("Po not Found");
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
}
