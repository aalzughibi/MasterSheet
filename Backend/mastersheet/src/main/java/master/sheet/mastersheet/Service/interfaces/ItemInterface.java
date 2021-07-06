package master.sheet.mastersheet.Service.interfaces;

import java.util.*;

import master.sheet.mastersheet.Entity.ItemEntity;

public interface ItemInterface {
    public List<ItemEntity> getAllItems();
    public ItemEntity getItemById(String item_id)throws Exception;
    public ItemEntity updateItem(ItemEntity Item)throws Exception;
    public ItemEntity insertItem(ItemEntity Item);
    public boolean isExist(String item_id);
}
