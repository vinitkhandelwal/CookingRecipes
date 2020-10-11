package util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vin on 5/13/2015.
 */
public class PopulateData implements Serializable{

    public static String BEVARGES = "bevarges";
    public static String Main_Course = "Main Course";
    public static String ITALIAN = "Italian";
    public static String CAKE = "Cake";
    public static String VEG = "Veg";
    public static String ALCOHOLIC_COCKTAILS = "Alcoholic Cocktails";
    public static String NON_ALCOHOLIC_COCKTAILS = "Non Alcoholic Cocktails";
    public static String FRESH_JUICE = "Non Alcoholic Cocktails";
    public static String SHAKES = "Shakes";
    public static String SMOOTHIES = "Smoothies";

        public  HashMap<String,List<String>> beveragesMap = new HashMap<String,List<String>>();

    public void populateBevrageMap(){

    }
    public void populateAlcholicCocktails(){

    }

}
