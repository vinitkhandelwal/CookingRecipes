package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vin on 5/20/2015.
 */
public class SubCategoryDataWrapper implements Serializable {



        private HashMap<String,List<Recipe>> subCategoryMap;

        public SubCategoryDataWrapper(HashMap<String,List<Recipe>> data) {
            this.subCategoryMap = data;
        }

        public HashMap<String,List<Recipe>> getsubCategoryMap() {
            return this.subCategoryMap;
        }
    }

