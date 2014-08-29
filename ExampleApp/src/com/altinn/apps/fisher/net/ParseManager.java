package com.altinn.apps.fisher.net;

/**
 * This class offers different kind of parser exists, we can get them from this Factory
 *
 */



public class ParseManager {
	
	public static final int PARSER_TYPE_JSON = 100;
	public static final int PARSER_TYPE_XML = 101;
	
	
	private static ParseManager mInstance;
	public ParseManager(){
		
	}
	public static IParser getParser(int parseType){
		if(mInstance == null){
			mInstance = new ParseManager();
		}
		return mInstance.createParser(parseType);
	}
	
	private IParser createParser(int parseType){
		IParser parser = null;
			switch(parseType){
			case PARSER_TYPE_JSON:
				parser = new JSParser();
				break;
			case PARSER_TYPE_XML:
				//parser = new XMLParser();
				break;
			}
		return parser;
	}
	
	

}
