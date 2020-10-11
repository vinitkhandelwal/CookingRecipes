package util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vin on 5/14/2015.
 */
public class XMLParser extends DefaultHandler
{


   public List<Recipe> list=null;
//     public static String URL_ADDRESS = "http://192.168.1.101/imageprocessor/" ;
    public static String URL_ADDRESS = "http://imageprocess.comli.com/imageprocessor/" ;
    // string builder acts as a buffer
    StringBuilder builder;

    Recipe recipe=null;


    // Initialize the arraylist
    // @throws SAXException

    @Override
    public void startDocument() throws SAXException {

        /******* Create ArrayList To Store XmlValuesModel object ******/
        list = new ArrayList<Recipe>();
    }


    // Initialize the temp XmlValuesModel object which will hold the parsed info
    // and the string builder that will store the read characters
    // @param uri
    // @param localName ( Parsed Node name will come in localName  )
    // @param qName
    // @param attributes
    // @throws SAXException

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        /****  When New XML Node initiating to parse this function called *****/

        // Create StringBuilder object to store xml node value
        builder=new StringBuilder();

        if(localName.equals("login")){

            //Log.i("parse","====login=====");
        }
        else if(localName.equals("status")){

            // Log.i("parse","====status=====");
        }
        else if(localName.equals("jobs")){

        }
        else if(localName.equals("record")){

            // Log.i("parse","----Job start----");
            /********** Create Model Object  *********/
            recipe = new Recipe();
            recipe.setFavourite(false);

        }
    }



    // Finished reading the login tag, add it to arraylist
    // @param uri
    // @param localName
    // @param qName
    // @throws SAXException

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {


        if(localName.equals("record")){

            /** finished reading a job xml node, add it to the arraylist **/
            list.add( recipe );

        }
        else  if(localName.equalsIgnoreCase("status")){


        }
        else  if(localName.equalsIgnoreCase("RecipeName")){

            recipe.setRecipeName(builder.toString());
        }
        else if(localName.equalsIgnoreCase("RecipeDescription")){

            recipe.setRecipeDesc(builder.toString());
        }
        else if(localName.equalsIgnoreCase("Category")){
            recipe.setCategory(builder.toString());
        }
        else if(localName.equalsIgnoreCase("Subcategory")){
            recipe.setSubCategoryType(builder.toString());
        }
        else if(localName.equalsIgnoreCase("Ingredients")){
          /*  String [] ingredients = (builder.toString()).split(".");
            List<String> temp = new ArrayList<String>();
            for(String temp1 : ingredients){
                temp.add(temp1);
            }*/
            recipe.setIngreditents(builder.toString());
        }
        else if(localName.equalsIgnoreCase("Procedure")){
            /*String [] ingredients = (builder.toString()).split(".");
            List<String> temp = new ArrayList<String>();
            for(String temp1 : ingredients){
                temp.add(temp1);
            }*/
            recipe.setProceDure(builder.toString());
        }
        else if(localName.equalsIgnoreCase("Duration")){
            recipe.setDuration(builder.toString());
        }

        else if(localName.equalsIgnoreCase("Servings")){
            recipe.setServings(builder.toString());
        }

        if(recipe != null) {
            if(recipe.getRecipeName() != null) {
                String recipeImage = recipe.getRecipeName().replace(" ", "_").toLowerCase();
                recipe.setReceipeImageUrl(recipeImage.replaceAll("[^\\w\\s.]+", ""));
            }else{
                System.out.println("when name is null" +recipe.getCategory());
            }
        }




        // Log.i("parse",localName.toString()+"========="+builder.toString());
    }


    // Read the value of each xml NODE
    // @param ch
    // @param start
    // @param length
    // @throws SAXException

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        /******  Read the characters and append them to the buffer  ******/
        String tempString=new String(ch, start, length);
        builder.append(tempString);
    }
}