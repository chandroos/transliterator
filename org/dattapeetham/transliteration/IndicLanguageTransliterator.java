package org.dattapeetham.transliteration;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Class to transliterate unicode text from various Indian languages to other languages
 * Currently supports conversion from Telugu to Samskrutam and Kannada
 *  
 * @author Chandra Sekhar Chaganti
 * @deprecated Use ICUHelper instead. It already supports many languages not supported by this class
 *
 */
public class IndicLanguageTransliterator {
	/**
	 * The difference between the unicode charset start range for telugu (U0C00) and Kannada (U0900)
	 */
	private static final int TELUGU_KANNADA_OFFSET = 0x80;
	/**
	 * The difference between the unicode charset start range for telugu (U0C00) and devanagari (U0900)
	 */
	private static final int TELUGU_SAMSKRUTAM_OFFSET = -0x300;
	
	// TODO Create Offsets for other Indian Languages by looking at the Unicode Table

	/**
	 * Contains the source languages currently supported
	 */
	public static final ArrayList<String> supportedSourceLanguages = new ArrayList<String>();

	/**
	 * Contains the destination languages currently supported by the program
	 */
	public static final ArrayList<String> supportedDestinationLanguages = new ArrayList<String>();
	private static final HashMap<String, LanguageConverter> converters = new HashMap<String, LanguageConverter>();

	public IndicLanguageTransliterator(){
		converters.put(LanguageConverter.getKey(TELUGU,SAMSKRUTAM), new IndicLanguageTransliterator.LanguageConverter(TELUGU, SAMSKRUTAM, TELUGU_SAMSKRUTAM_OFFSET));
		converters.put(LanguageConverter.getKey(TELUGU,KANNADA), new IndicLanguageTransliterator.LanguageConverter(TELUGU, KANNADA, TELUGU_KANNADA_OFFSET));
		
		// TODO add converters for other Indian languages 
	}
	/**
	 * Exception thrown when an Unsupported language is passed in the arguments
	 * @author Chandra Sekhar Chaganti
	 *
	 */
	public class UnsupportedLanguageException extends Exception {


		private static final long serialVersionUID = 1L;
		/**
		 * Constructor
		 * @param string
		 */
		public UnsupportedLanguageException(String string) {
			super(string);
		}

	}
/**
 * Converter objects for each pair of languages with offset and other rules
 * @author Chandra Sekhar Chaganti
 *
 */
	private static class LanguageConverter {
		String sourceLanguage;
		String destLanguage;
		int offset;

		LanguageConverter(String src, String dest, int off) {
			sourceLanguage = src;
			destLanguage = dest;
			offset = off;
		}

		/**
		 * generates source and destination language key to be used for looking up in converters hashmap
		 * @param sourceLanguage
		 * @param destLanguage
		 * @return
		 */
		public static String getKey(String sourceLanguage, String destLanguage) {
			return sourceLanguage + "-" + destLanguage;
		}
	}
	public static String TELUGU = "TELUGU";
	public static String SAMSKRUTAM = "SAMSKRUTAM";
	public static String KANNADA = "KANNADA";
	public static String TAMIL = "TAMIL";
	public static String HINDI = "HINDI";
	public static String GUJARATI = "GUJARATI";
	public static String MALAYALAM = "MALAYALAM";


	static {
		supportedSourceLanguages.add(TELUGU);
		supportedDestinationLanguages.add(SAMSKRUTAM);
		supportedDestinationLanguages.add(KANNADA);
	}
	/**
	 * Converts the specified source string from sourceLanguage to destination Language
	 * @param source
	 * @param sourceLanguage
	 * @param destLanguage
	 * @return String in destination language
	 * @throws UnsupportedLanguageException when a language currently not supported is passed
	 */
	public  String convert (String source, String sourceLanguage, String destLanguage) throws UnsupportedLanguageException {
		if(!supportedSourceLanguages.contains(sourceLanguage)) {
			throw new UnsupportedLanguageException("Source Language not supported");
		}
		if(!supportedDestinationLanguages.contains(destLanguage)) {
			throw new UnsupportedLanguageException("Destination Language not supported");
		}
		
		
		char[]  destBts = new char[source.length()];
		for(int i=0;i<source.length();i++) {
			destBts[i] = convert(source.charAt(i), sourceLanguage, destLanguage);
		}
		return new String(destBts);
	}

	/**
	 * Converts a single character from source to destination language
	 * @param c
	 * @param sourceLanguage
	 * @param destLanguage
	 * @return
	 * @throws UnsupportedLanguageException
	 */
	public char convert(char c, String sourceLanguage, String destLanguage) throws UnsupportedLanguageException {
		if(Character.isWhitespace(c)) return c; //return white space characters without conversion
		 LanguageConverter converter = converters.get(LanguageConverter.getKey(sourceLanguage,destLanguage));
		if (converter != null) {
			return (char) (c + converter.offset); 
		} else {
			throw new UnsupportedLanguageException("Source Language not supported");
		}


	}

	public static void main(String[] args) {
		IndicLanguageTransliterator conv = new IndicLanguageTransliterator();

		try {
			String sourceString = 
				"        శ్రీగణపతి సచ్చిదానంద స్వామీజీ                                 ";
			
			
			System.out.println("Source in Telugu:" + sourceString);
			
			String string = conv.convert(sourceString, TELUGU, SAMSKRUTAM);
			System.out.println("Converted to Samskrutam: " + string);
			
			string = conv.convert(sourceString, TELUGU, KANNADA);
			System.out.println("converted to Kannada:" + string);
			
			
		} catch (UnsupportedLanguageException e) {
			e.printStackTrace();
		}

	}

}