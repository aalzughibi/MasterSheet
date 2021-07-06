package master.sheet.mastersheet.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import master.sheet.mastersheet.Entity.ItemEntity;
import master.sheet.mastersheet.Repository.ItemRepository;
import master.sheet.mastersheet.Service.interfaces.ItemInterface;


@Service
public class ItemService implements ItemInterface{
    
    @Autowired
    ItemRepository itemRepository;
    public List<ItemEntity> getAllItems(){
        List<ItemEntity> ItemList = itemRepository.findAll();
        return ItemList;
    }
    public ItemEntity getItemById(String item_id)throws Exception{
        Optional<ItemEntity> ItemList = itemRepository.findByItemId(item_id);
        return ItemList.get();
    }
    public ItemEntity updateItem(ItemEntity Item)throws Exception{
        ItemEntity pe = getItemById(Item.getItem_id());
        if (pe !=null){
            pe.setItem_name(Item.getItem_name());
            pe.setStart_date(Item.getStart_date());
            pe.setEnd_date(Item.getEnd_date());
            pe.setItem_type(Item.getItem_type());
            pe.setProject_id(Item.getProject_id());
            pe.setItem_remarks(Item.getItem_remarks());
            pe.setPo_no(pe.getPo_no());
            pe.setPo_remarks(pe.getPo_remarks());
            pe.setPo_value(pe.getPo_value());
            pe = itemRepository.save(pe);
        return pe;
        }
        else
        throw new Exception("Item not Found");
    }
    public ItemEntity insertItem(ItemEntity Item){
       ItemEntity pe= itemRepository.save(Item);
        return pe;
    }
    public boolean isExist(String item_id){
        return itemRepository.existsByItemId(item_id);
    }
}
