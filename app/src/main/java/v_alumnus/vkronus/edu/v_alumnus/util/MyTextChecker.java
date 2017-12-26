package v_alumnus.vkronus.edu.v_alumnus.util;

/**
 * Created by ZAID on 8/6/2015.
 */
public class MyTextChecker {

        public static String getValue(String text){
            text.replaceAll(" ","%20");
            text.replaceAll("'","%27");
            return text;
        }

    }


