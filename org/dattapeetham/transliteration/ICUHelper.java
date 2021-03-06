package org.dattapeetham.transliteration;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.logging.Logger;

import com.ibm.icu.text.Transliterator;

public class ICUHelper {
	static Logger logger = Logger.getAnonymousLogger();
	public static final String BENGALI = "Bengali";
	public static final String MALAYALAM = "Malayalam";
	public static final String GUJARATI = "Gujarati";
	public static final String TAMIL = "Tamil";
	public static final String KANNADA = "Kannada";
	public static final String DEVANAGARI = "Devanagari";
	public static final String TELUGU = "Telugu";
	private static final HashMap<String,String> rulesMap = new HashMap<String,String>();
	static String ta_rules = "{\u0B82}[^\u0BA4]>ம்; {\u0B82}\u0BA4>\u0BA9\u0BCD; {\u0BA8}\u0BC1>\u0BA9; [\u0B80-\u0BFF]{ந}>ன";
	static String en_rules = "{n}[hsśṣ]>m;r̥>ṛ";  //ex: sinha to simha
	
	static{
		rulesMap.put(TAMIL, ta_rules);
		rulesMap.put("ta", ta_rules);
		rulesMap.put("en", en_rules);
		rulesMap.put("Latin", en_rules);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
//			String sourceString = "3.	వాంఛసి యది తత్పద మవినాశం\nనాశవిదూరం భజదత్తం\nసంసృతి బాధా శమ మిచ్ఛసి చేత్\nశమదమ ధీరం భజదత్తం\nశ్రీగణపతి సచ్చిదానంద స్వామీజీ        ";
			String sourceString = "అద్దం సింహే భజిసువెను నశంత మునివంశ மஂகளம் పితృ abc";
			

			System.out.println("Source in Telugu:" + sourceString);
			// Transliterate the telugu text to all available target languages 
			for(Enumeration<String> e = Transliterator.getAvailableTargets(TELUGU); e.hasMoreElements();) {
				transliterate(sourceString, e.nextElement());
			}
			System.out.println("Using Te-dest :" + transliterateFromTelugu(sourceString, "hi"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	/**
	 * Transliterates the given source string from any language into the destination language specified, if ICU4j allows the pair
	 * @param sourceString
	 * @param destLanguage
	 * @return
	 */
	public static String transliterate(final String sourceString, final String sourceLanguage, final String destLanguage) {
		Transliterator trans = Transliterator
				.getInstance(sourceLanguage +"-"+ destLanguage);
		String string = trans.transliterate(sourceString);
		logger.info("Converted to " + destLanguage + ":" + string);
			String rules=rulesMap.get(destLanguage);
			if( rules!=null) {
				trans = Transliterator.createFromRules(sourceLanguage +"-" + destLanguage, rules, 0);
				string = trans.transliterate(string);
			
				logger.info("Using Rules " + destLanguage + ":" + string);
			}
			return string;
	}
	
			public static String transliterate(final String sourceString, final String destLanguage) {
				return transliterate(sourceString,"Any",destLanguage);
			}
	
			public static String transliterateFromTelugu(final String sourceString, final String destLanguage) {
				return transliterate(sourceString,"te",destLanguage);
			}
	


	public static ArrayList<String> getAvailableTargets(String sourceLanguage) {
		ArrayList<String> availableLanguages = new ArrayList<String>();
		for(Enumeration<String> e = Transliterator.getAvailableTargets(sourceLanguage); e.hasMoreElements();) {
			availableLanguages.add(e.nextElement());
		}
		return availableLanguages;

	}
	

	public static ArrayList<String> getAvailableSources() {
		ArrayList<String> availableLanguages = new ArrayList<String>();
		for(Enumeration e = Transliterator.getAvailableSources(); e.hasMoreElements();) {
			availableLanguages.add((String)e.nextElement());
		}
		return availableLanguages;

	}

}