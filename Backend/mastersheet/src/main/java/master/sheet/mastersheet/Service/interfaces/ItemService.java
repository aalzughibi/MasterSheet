package master.sheet.mastersheet.Service.interfaces;

import java.util.*;

import master.sheet.mastersheet.Entity.ItemEntity;

public interface ItemService {
    public List<ItemEntity> getAllItems();
    public ItemEntity getItemById(String item_id);
    public ItemEntity updateItem(ItemEntity Item);
    public ItemEntity insertItem(ItemEntity Item);
    public boolean isExist(String item_id);
}
