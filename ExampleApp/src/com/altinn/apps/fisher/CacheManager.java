package com.altinn.apps.fisher;
/**
 * A Singleton class...
 * This class is used to get Respective ROBOTO font.
 * Here a hash-table is maintained for caching purpose if a font is already created 
 * then the same reference will returned. other wise it will be loaded from asset-folder and cached.
 */
import java.util.Hashtable;

import android.graphics.Typeface;

public class CacheManager {
	private static CacheManager mInstance;
	private Hashtable<Integer,Typeface> mFontTable;
    private CacheManager(){
    	mFontTable = new Hashtable<Integer,Typeface>();
    }
    
    public static CacheManager getInstance(){
    	if(mInstance == null){
    		mInstance = new CacheManager();
    	}
    	return mInstance;
    }
    
    public Typeface getTypeFace(int key){
    	Typeface typeFace = null;
    	if(mFontTable.containsKey(key)){
    		typeFace =  mFontTable.get(key);
    	}else{
    		typeFace = Typeface.createFromAsset(AppContext.getInstance().getAssets(), getTypefaceFile(key));
    		mFontTable.put(key, typeFace);
    	}
    	return typeFace;
    }
    
    
    
    
    /** FONTS **/
   private final int ROBOTO_THIN = 0;
   private final int ROBOTO_THIN_ITALIC = 1;
   private final int ROBOTO_LIGHT = 2;
   private final int ROBOTO_LIGHT_ITALIC = 3;
   private final int ROBOTO_REGULAR = 4;
   private final int ROBOTO_ITALIC = 5;
   private final int ROBOTO_MEDIUM = 6;
   private final int ROBOTO_MEDIUM_ITALIC = 7;
   private final int ROBOTO_BOLD = 8;
   private final int ROBOTO_BOLD_ITALIC = 9;
   private final int ROBOTO_BLACK = 10;
   private final int ROBOTO_BLACK_ITALIC = 11;
   private final int ROBOTO_CONDENSED_REGULAR = 12;
   private final int ROBOTO_CONDENSED_ITALIC = 13;
   private final int ROBOTO_CONDENSED_BOLD = 14;
   private final int ROBOTO_CONDENSED_BOLD_ITALIC = 15;   
   private final int ROBOTO_CONDENSED_LIGHT = 16;
   private final int ROBOTO_CONDENSED_LIGHT_ITALIC = 17;
    
    private String getTypefaceFile( int typefaceValue) {
        String typefaceFile;
        switch (typefaceValue) {
            case ROBOTO_THIN:
                typefaceFile =  "fonts/roboto/Roboto-Thin.ttf";
                break;
            case ROBOTO_THIN_ITALIC:
                typefaceFile = "fonts/roboto/Roboto-ThinItalic.ttf";
                break;
            case ROBOTO_LIGHT:
                typefaceFile =  "fonts/roboto/Roboto-Light.ttf";
                break;
            case ROBOTO_LIGHT_ITALIC:
                typefaceFile = "fonts/roboto/Roboto-LightItalic.ttf";
                break;
            case ROBOTO_REGULAR:
                typefaceFile = "fonts/roboto/Roboto-Regular.ttf";
                break;
            case ROBOTO_ITALIC:
                typefaceFile =  "fonts/roboto/Roboto-Italic.ttf";
                break;
            case ROBOTO_MEDIUM:
                typefaceFile = "fonts/roboto/Roboto-Medium.ttf";
                break;
            case ROBOTO_MEDIUM_ITALIC:
                typefaceFile = "fonts/roboto/Roboto-MediumItalic.ttf";
                break;
            case ROBOTO_BOLD:
                typefaceFile = "fonts/roboto/Roboto-Bold.ttf";
                break;
            case ROBOTO_BOLD_ITALIC:
                typefaceFile = "fonts/roboto/Roboto-BoldItalic.ttf";
                break;
            case ROBOTO_BLACK:
                typefaceFile = "fonts/roboto/Roboto-Black.ttf";
                break;
            case ROBOTO_BLACK_ITALIC:
                typefaceFile = "fonts/roboto/Roboto-BlackItalic.ttf";
                break;
            case ROBOTO_CONDENSED_REGULAR:
                typefaceFile = "fonts/roboto/RobotoCondensed-Regular.ttf";
                break;
            case ROBOTO_CONDENSED_ITALIC:
                typefaceFile = "fonts/roboto/RobotoCondensed-Italic.ttf";
                break;
            case ROBOTO_CONDENSED_BOLD:
                typefaceFile = "fonts/roboto/RobotoCondensed-Bold.ttf";
                break;
            case ROBOTO_CONDENSED_BOLD_ITALIC:
                typefaceFile = "fonts/roboto/RobotoCondensed-BoldItalic.ttf";
                break;
            case ROBOTO_CONDENSED_LIGHT:
                typefaceFile = "fonts/roboto/RobotoCondensed-Light.ttf";
                break;
            case ROBOTO_CONDENSED_LIGHT_ITALIC:
                typefaceFile = "fonts/roboto/RobotoCondensed-LightItalic.ttf";
                break;
            default:
            	 typefaceFile = "fonts/roboto/Roboto-Regular.ttf";
            	 break;
        }
        return typefaceFile;
    }
	
}
