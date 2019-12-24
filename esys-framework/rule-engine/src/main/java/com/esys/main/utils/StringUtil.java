package com.esys.main.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author onurd
 *
 */
public class StringUtil {
	private static Logger log = LoggerFactory.getLogger(StringUtil.class);
	public static Logger getLog() {
		return log;
	}

	public static void setLog(Logger log) {
		StringUtil.log = log;
	}

	public static boolean equal(String first, String second) {
		return (first == null && second == null)
				|| (first != null && second != null && first.trim().equals(
						second.trim()));
	}

	public static String concat(String s1, String s2) {
		return s1 + s2;
	}

	public static String blobToString(Blob blob) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		InputStream in;
		int n = 0;
		try {
			in = blob.getBinaryStream();
			while ((n = in.read(buf)) >= 0) {
				baos.write(buf, 0, n);
			}
			in.close();
		} catch (SQLException e) {
			LoggerFactory.getLogger("ResourceReaderUtil").error(e.getMessage());
		} catch (IOException e) {
			LoggerFactory.getLogger("ResourceReaderUtil").error(e.getMessage());
		}
		byte[] bytes = baos.toByteArray();
		String blobString = new String(bytes);
		return blobString;
	}

	public static String clobToString(Clob clob) {
		if (clob == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		try {
			Reader reader = clob.getCharacterStream();
			BufferedReader br = new BufferedReader(reader);
			int b;
			while (-1 != (b = br.read())) {
				sb.append((char) b);
			}
			br.close();
		} catch (SQLException e) {
			LoggerFactory.getLogger("ResourceReaderUtil").error(e.getMessage());
		} catch (IOException e) {
			LoggerFactory.getLogger("ResourceReaderUtil").error(e.getMessage());
		}
		return sb.toString();
	}

	public static List<String> parseTaggedCode(String code) {
		List<String> list = new ArrayList<String>();
		if (isNotNullAndNotEmpty(code)) {
			if (code.contains("><")) {
				String[] s = code.split("><");
				s[0] = s[0].replace("<", "");
				s[s.length - 1] = s[s.length - 1].replace(">", "");
				for (String str : s) {
					list.add(str);
				}
				return list;
			} else {
				code = code.replace("<", "");
				code = code.replace(">", "");
				list.add(code);
				return list;
			}
		} else {
			return list;
		}
	}

	public static String subString(String str, Integer begIndex,
			Integer endIndex) {
		String result = "";
		if (str != null) {
			if (begIndex > str.length() || endIndex > str.length()
					|| begIndex > endIndex)
				result = str.substring(0, str.length());
			else
				result = str.substring(begIndex, endIndex);
		}

		return result;
	}

	public static Double stringToDouble(String s) {
		return Double.parseDouble(s);
	}

	public static boolean isNothing(String str) {
		return (null == str || str.equals(""));
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.trim().equals(""));
	}

	public static String getString(String str) {
		return ((null == str) ? "" : str);
	}

	public static String getString(BigDecimal num) {
		return ((null == num) ? "" : num.toString());
	}

	public static String getString(Long num) {
		return ((null == num) ? "" : num.toString());
	}

	public static String getAsString(Object obj) {
		return ((null == obj) ? null : obj.toString());
	}

	public static BigDecimal getZero(BigDecimal bigDec) {
		return ((null == bigDec) ? BigDecimal.ZERO : bigDec);
	}

	public static String getHTML(String str) {
		return ((isNothing(str)) ? "&nbsp;" : str);
	}

	public static String getStringOrNull(Object o) {
		try {
			return o.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Sonu * ile biten String'leri sql'e uygun like'li aramada kullanilabilecek
	 * %'li string'e cevirir.
	 * 
	 * @return
	 */
	public static String getAsSQLLikeString(String value) {
		if ((value).length() > 1) {
			String subStr = (value).substring(0, (value).length() - 1) + "%";
			return subStr;
		}
		return null;
	}

	public static List<Long> convertCsvToLongList(String csvString) {
		List<Long> convertedPolicyList = new ArrayList<Long>();
		if (csvString == null || csvString.length() == 0)
			return null;
		String[] policyList = csvString.split(",");

		for (String policyIdInString : policyList) {
			try {
				Long policyId = Long.parseLong(policyIdInString);
				convertedPolicyList.add(policyId);
			} catch (Exception e) {
				return null;
			}
		}
		return convertedPolicyList;
	}

	public static List<String> convertCsvToStringList(String csvString) {
		List<String> convertedPolicyList = new ArrayList<String>();
		if (csvString == null || csvString.length() == 0)
			return null;
		String[] policyList = csvString.split(",");

		for (String policyNo : policyList) {
			try {
				convertedPolicyList.add(policyNo);
			} catch (Exception e) {
				return null;
			}
		}
		return convertedPolicyList;
	}

	public static <T extends Object> String convertCollectionToCsv(
			Collection<T> collection) {
		if (collection == null || collection.size() == 0) {
			return null;
		}

		Iterator<T> it = collection.iterator();

		StringBuffer sb = new StringBuffer();
		String csvString = it.next().toString();
		sb.append(csvString);

		while (it.hasNext()) {
			String str = it.next().toString();
			sb.append(",").append(str);
		}
		return sb.toString();
	}

	public static String convertListToNewLinedString(List<String> list) {
		String newLine = "\n";
		StringBuffer sb = new StringBuffer();
		for (String line : list) {
			sb.append(line).append(newLine);
		}
		return sb.toString();
	}

	/**
	 * Girilen string sonuna "bosluk" karakteri ekleyerek istenen boyuta
	 * gelmesini saglar Boyu size parametresi ile ayn� ise herhangi bir islem
	 * yapilmaz ve aynen dondurulur Boyu daha buyuk ise verilen size kadar olan
	 * kismi dondurulur Boyu kucuk ise de eksik miktar kadar "bosluk" karakteri
	 * eklenir.
	 * 
	 * @param str
	 *            boyu ayarlanmak istenen string
	 * @param size
	 *            string in gelmesi istenen boyut
	 * @return girilen string in "bosluk" karakteri ile istenen boyuta
	 *         getirilmis hali
	 */
	public static String fillWithSpaces(String str, int size) {
		char spaceChar = ' ';
		return appendCharacterAfter(str, size, spaceChar);
	}

	/**
	 * Girilen string sonuna ch karakteri ekleyerek istenen boyuta gelmesini
	 * saglar Boyu size parametresi ile ayn� ise herhangi bir islem yapilmaz ve
	 * aynen dondurulur Boyu daha buyuk ise verilen size kadar olan kismi
	 * dondurulur Boyu kucuk ise de eksik miktar kadar ch karakteri eklenir.
	 * 
	 * @param str
	 *            boyu ayarlanmak istenen string
	 * @param size
	 *            string in gelmesi istenen boyut
	 * @param ch
	 *            sona eklenecek karakter
	 * @return girilen string in ch karakteri ile istenen boyuta getirilmis hali
	 */
	public static String appendCharacterAfter(String str, int size, char ch) {
		return appendCharacter(str, size, ch, false);
	}

	/**
	 * Girilen string in basina ch karakteri ekleyerek istenen boyuta gelmesini
	 * saglar Boyu size parametresi ile ayn� ise herhangi bir islem yapilmaz ve
	 * aynen dondurulur Boyu daha buyuk ise verilen size kadar olan kismi
	 * dondurulur Boyu kucuk ise de eksik miktar kadar ch karakteri eklenir.
	 * 
	 * @param str
	 *            boyu ayarlanmak istenen string
	 * @param size
	 *            string in gelmesi istenen boyut
	 * @param ch
	 *            basa eklenecek karakter
	 * @return girilen string in ch karakteri ile istenen boyuta getirilmis hali
	 */
	public static String appendCharacterBefore(String str, int size, char ch) {
		return appendCharacter(str, size, ch, true);
	}

	/**
	 * Girilen string sonuna ve ya basina (isBofore parametresine gore) ch
	 * karakteri ekleyerek istenen boyuta gelmesini saglar Boyu size parametresi
	 * ile ayn� ise herhangi bir islem yapilmaz ve aynen dondurulur Boyu daha
	 * buyuk ise verilen size kadar olan kismi dondurulur Boyu kucuk ise de
	 * eksik miktar kadar ch karakteri eklenir.
	 * 
	 * @param str
	 *            boyu ayarlanmak istenen string
	 * @param size
	 *            string in gelmesi istenen boyut
	 * @param ch
	 *            sona ve ya basa eklenecek karakter
	 * @return girilen string in ch karakteri ile istenen boyuta getirilmis hali
	 */
	private static String appendCharacter(String str, int size, char ch,
			boolean isBefore) {
		if (str == null) {
			return null;
		} else if (str.length() == size) {
			return str;
		} else if (str.length() > size) {
			return str.substring(0, str.length());
		} else {
			int diff = size - str.length();
			String charSeq = getCharSequenceString(ch, diff);
			if (isBefore) {
				return charSeq.concat(str);
			} else {
				return str.concat(charSeq);
			}
		}
	}

	public static String getCharSequenceString(char chr, int size) {
		return getCharSequenceBuffer(chr, size).toString();
	}

	public static StringBuffer createAndFillWithSpaces(int size) {
		return getCharSequenceBuffer(' ', size);
	}

	public static StringBuffer createAndFillWithZero(int size) {
		return getCharSequenceBuffer('0', size);
	}

	public static StringBuffer getCharSequenceBuffer(char chr, int size) {
		StringBuffer sb = new StringBuffer(size);
		for (int i = 0; i < size; i++)
			sb.append(chr);
		return sb;
	}

	/**
	 * @param arg
	 * @return A <tt>String</tt> where '*' in the front and end of the argument
	 *         arg are replaced with '%'.
	 */
	public static String generateSqlLikePattern(String arg) {
		if (arg.endsWith("*")) {
			arg = arg.substring(0, arg.length() - 1) + "%";
		}
		if (arg.startsWith("*")) {
			arg = "%" + arg.substring(1, arg.length());
		}
		return arg;
	}

	/**
	 * A convenience method for query parameter validation check before
	 * generating related sql.
	 * 
	 * @param arg
	 * @return <tt>true</tt> if the trimmed arg's length is greater than 0
	 */
	public static boolean isNotNullAndNotEmpty(String arg) {
		return (arg != null && arg.trim().length() != 0);
	}

	public static boolean isNotEmpty(String arg) {
		return (arg != null && arg.trim().length() != 0);
	}

	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()){
		    if (!Character.isDigit(c)){ 
		    	return false;
		    }
		}
	    return true;
	}

	/**
	 * @param str
	 *            boyu ayarlanmak istenen string
	 * @param size
	 *            kirpilacak uzunluk
	 * */
	public static String leftString(String str, int size) {
		String ret = "";
		if (str != null) {
			if (str.length() <= size)
				ret = str;
			else
				ret = str.substring(0, size);
		}
		return ret;
	}

	public static String trimWhiteSpacesInAString(String str) {
		if (str != null) {
			return str.replace(" ", "");
		} else {
			return null;
		}
	}

	public static String toUpperCase(String str, Locale locale) {
		return str.toUpperCase(locale);
	}
	public static String toLowerCase(String str, Locale locale) {//dilekb TYH-61189
		return str.toLowerCase(locale);
	}
	
	public static final String PATTERN_OPTIONALFRACTION = "##.###.###.##0,########";

	

	

	public static String convertCodeToTaggedCode(List<String> list) {
		if (list != null && list.size() > 0) {
			String result = new String();
			for (String s : list) {
				result += s + " ";
			}
			if (list.size() == 1) {
				return "<" + result.trim() + ">";
			}
			result = result.trim();
			result = result.replace(" ", "><");
			return "<" + result + ">";
		}
		return null;
	}



	public static String getString(Boolean val) {
		if (Boolean.TRUE.equals(val)) {
			return "T";
		} else if (Boolean.FALSE.equals(val)) {
			return "F";
		}
		return null;
	}

	
	
	
	
	

	public static int collatorCompare(Object o1, Object o2, String language,
			String country) {
		Collator collator = Collator.getInstance(new Locale(language, country));
		return collator.compare(o1, o2);
	}

	

	@SuppressWarnings("unused")
	public static String EAN13(String chaine) {
		int i;
		int first;
		int checksum = 0;
		String CodeBarre = "";
		boolean tableA;

		// if (Regex.IsMatch(chaine, "^\\d{12}$"))
		// {

		// for (i=1; i<12; i+=2)
		// {
		// getLog().debug(chaine.substring(i, 1));
		//
		// checksum += Integer.valueOf(chaine.substring(i, 1));
		// }
		// checksum *= 3;
		// for (i=0; i<12; i+=2)
		// {
		// checksum += Integer.valueOf(chaine.substring(i, 1));
		// }
		//
		// chaine += (10 - checksum % 10) % 10;

		chaine = "1234567891248";

		CodeBarre = chaine.substring(0, 1)
				+ (char) (65 + Integer.valueOf(chaine.substring(1, 2)));

		first = Integer.valueOf(chaine.substring(0, 1));
		getLog().debug("first --->" + first);
		for (i = 2; i <= 6; i++) {
			tableA = false;
			switch (i) {
			case 2:
				if (first >= 0 && first <= 3)
					tableA = true;
				break;
			case 3:
				if (first == 0 || first == 4 || first == 7 || first == 8)
					tableA = true;
				break;
			case 4:
				if (first == 0 || first == 1 || first == 4 || first == 5
						|| first == 9)
					tableA = true;
				break;
			case 5:
				if (first == 0 || first == 2 || first == 5 || first == 6
						|| first == 7)
					tableA = true;
				break;
			case 6:
				if (first == 0 || first == 3 || first == 6 || first == 8
						|| first == 9)
					tableA = true;
				break;
			}

			if (tableA) {
				getLog().debug("65 + "
						+ Integer.valueOf(chaine.substring(i, i + 1))
						+ " :===="
						+ (char) (65 + Integer.valueOf(chaine.substring(i,
								i + 1))));
				CodeBarre += (char) (65 + Integer.valueOf(chaine.substring(i,
						i + 1)));
			} else {
				getLog().debug("75 +"
						+ Integer.valueOf(chaine.substring(i, i + 1))
						+ ":===="
						+ (char) (75 + Integer.valueOf(chaine.substring(i,
								i + 1))));
				CodeBarre += (char) (75 + Integer.valueOf(chaine.substring(i,
						i + 1)));
			}

		}
		CodeBarre += "*";

		for (i = 7; i <= 12; i++) {
			if (i < 12) {
				CodeBarre += (char) (97 + Integer.valueOf(chaine.substring(i,
						i + 1)));
			} else
				CodeBarre += (char) (97 + Integer.valueOf(chaine.substring(i)));
		}
		CodeBarre += "+";
		// }
		getLog().debug(CodeBarre);
		return CodeBarre;
	}

	public static String getListAsString(List<String> stringList){
		String result=null;
		
		if(stringList!=null && stringList.size()>0){
			result="";
			for(String st : stringList){
				result+=st+";";
			}
		}
		
		return result;
	}
	
	public static boolean isNullOrEmpty(String value){
		return value == null || value.length() == 0;
	}

	public static boolean isNullOrEmptyOrWhitespace(String value){
		return value == null || value.trim().length() == 0;
	}

	
	
	public static String maskCreditCardNo(String ccNo) {
		return "************".concat(ccNo.substring(12));
	}
	
	public static String fixedLengthString(String input, int length) {
		if (input != null) {
			if (input.length() >= length) {
				return input.substring(0, length);
			} else {
				return fillWithSpaces(input, length);
			}
		}
		return null;
	}
	
	public static String substring(String string, int beginIndex, int endIndex) {
		if (string == null || beginIndex > endIndex
				|| beginIndex >= string.length()) {
			return null;
		}
		if (endIndex > string.length()) {
			return string.substring(beginIndex);
		} else {
			return string.substring(beginIndex, endIndex);
		}
	}
	
	public static String startWithLowerCase(String s) {
		return s.substring(0, 1).toLowerCase() + s.substring(1);
	}
	
        public static String CapitalizeOnlyFirstLetters(String s) {
            if(s != null) {
                Locale trLocale = Locale.forLanguageTag("tr-TR");
                String[] sArr = s.split(" ");
                String returnStr = "";
                for(int i=0; i<sArr.length; i++) {
                    if(!sArr[i].contains(".")) {
                        sArr[i] = sArr[i].toLowerCase(trLocale);
                    }
                    if(sArr[i].length() > 0) {
                        String firstLetter = sArr[i].substring(0, 1).toUpperCase(trLocale);
                        sArr[i] = firstLetter + sArr[i].substring(1);
                        returnStr += sArr[i] + " ";
                    }                    
                }
                return returnStr.trim();
            }
            return s;
        }
        
        public static boolean isValidMail(String mail) {
            if(isNullOrEmpty(mail))
                return false;
            if(".".equals(mail))
                return false;
            
            String EMAIL_PATTERN = 
                            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            
            return Pattern.matches(EMAIL_PATTERN, mail);
        }
        
        public static String regexChanger(String template,Map<String,Object> matcherMap,String betweenStartChar,String betweenEndChar){
        	String in=template;
			if(template!=null && matcherMap!=null){
				for(String key:matcherMap.keySet()){
					Pattern p = Pattern.compile("\\"+betweenStartChar+"("+key+")\\"+betweenEndChar);
					Matcher m = p.matcher(in);
					 in=m.replaceAll((String)matcherMap.get(key));
		        
				} 
			}
			return in;
        }
        
        public static Map<String,String> regexTagMap(String xmlTemplate){
    	    
        	if(xmlTemplate!=null){
	    	    String in=xmlTemplate;
			//Pattern p = Pattern.compile("\\<\\/([^\\<]+)\\>");
				Pattern p = Pattern.compile("\\</(.*?)\\>");
				Matcher m = p.matcher(in);
				List<String> keyList=new ArrayList<String>();
				while (m.find()) {
					keyList.add(m.group(1));
				}
				if(keyList.size()>0){
					Map<String,String> mapText= new HashMap<String,String>();
					for(String key:keyList){
						Pattern x = Pattern.compile("\\<"+key+"\\>(.*?)\\<\\/"+key+"\\>");
						Matcher xm = x.matcher(in);
						while (xm.find()) {
							mapText.put(key, xm.group(1));
						}
						
					}
					if(mapText.size()>0){
						return mapText;
					}
				}
        	}
        	return null;
		
        }
        
//        public static String regexWithTemplateAndTagMap(String xmlParameter,String template,String betweenStartChar,String betweenEndChar){
//        	Map<String,String> map=regexTagMap(xmlParameter);
//        	if(map!=null){
//        		return regexChanger(template,map,betweenStartChar,betweenEndChar);
//        	}else{
//        		return null;
//        	}
//        }

		public static Object regexMapToCreateTag(
				Map<String, String> parameterMap) {
			
			if(parameterMap!=null){
				String tagBody="";
				for(String key:parameterMap.keySet()){
					tagBody+="<"+key+">"+parameterMap.get(key)+"</"+key+">";
				}
				return tagBody;
			}
			
			// TODO Auto-generated method stub
			return null;
		}
		
		public static String convertTurkishToEnglish(String str){
		if (isNotNullAndNotEmpty(str)) {
			return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("\\p{Mn}", "");
		} else
			return str;
		}
        
}
