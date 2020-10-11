package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vin on 5/21/2015.
 */
public class ItemListDataWrapper implements Serializable {

    private List<Recipe> itemList;

    public ItemListDataWrapper(List<Recipe> itemList) {
        this.itemList = itemList;
    }

    public List<Recipe> getitemList() {
        return this.itemList;
    }
}

