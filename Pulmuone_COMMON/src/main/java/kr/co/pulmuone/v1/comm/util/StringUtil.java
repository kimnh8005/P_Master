package kr.co.pulmuone.v1.comm.util;

import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.exception.UserException;

@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
public class StringUtil {

    /**
     * String 의 값을 체크하여 null 일 경우 공백을 리턴한다.
     * @param String
     * @return 문자열
     */
    public static String nvl( String str ) {
        if (str == null || "".equals(str)) {
            str = "";
        }
        return str;
    }

    /**
     * String 의 값을 체크하여 true | false 값을 리턴한다.
     * @param str
     * @return
     */
    public static boolean isNvl(String str) {
		if(str == null || str.length() == 0) {
			return true;
		}else {
			return false;
		}
	}

    /**
     * ### 작업 Start : YoonHyunhee ###
     * String의 값을 비교하여 동일한지 판단하여 true|false 리턴한다.
     */
    public static boolean isEquals(String str, String compareStr) {
	    try {
		    if(nvl(str).equals(compareStr)) {
		    	return true;
		    } else {
		    	return false;
		    }
	    }catch( Exception e ){
	    	return false;
	    }
    }
    /**
     * ### 작업 Start : youngmin ###
     * String의 값을 비교하여 동일한지 판단하여 true|false 리턴한다.
     */
    public static boolean isNotEquals(String str, String compareStr) {
	    try {
		    if(!nvl(str).equals(compareStr)) {
		    	return true;
		    } else {
		    	return false;
		    }
	    }catch( Exception e ){
	    	return false;
	    }
    }
    /**
     * ### 작업 Start : YoonHyunhee ###
     * 빈 문자열이 아닌지 체크하여 true|false 리턴한다.
     */
    public static boolean isNotEmpty(Object str) {
    	if(!"".equals( nvl(str))) {
	    	return true;
	    } else {
	    	return false;
	    }
    }

    public static boolean isEmpty(Object str) {
    	return isNotEmpty(str) ? false : true;
    }

    /**
     * Object 의 값을 체크하여 null 일 경우 공백을 리턴한다.
     * @param Object
     * @return 문자열
     */
    public static String nvl( Object str ) {
        if (str == null || "".equals(str)) {
        	return "";
        }else{
        	return str.toString();
        }
    }

    /**
     * Object의 값을 체크하여 null 일 경우 대체문자열을 리턴한다.
     * @param Object str
     * @param String replaceStr
     * @return 문자열
     */
    public static String nvl( Object str, String replaceStr ) {
        if (str == null || "".equals(str)) {
        	return replaceStr;
        }else{
        	return str.toString();
        }
    }

    /**
     * Object 의 값을 체크하여 숫자로 변환 후 리턴한다.
     * @param String
     * @return 형변환(숫자) 리턴
     */
    public static int nvlInt( Object str ) {
    	try{
            if (str == null || "".equals(str)) {
            	return 0;
            }else{
            	return Integer.parseInt( str.toString() );
            }
    	}catch( Exception e ){
    		System.out.println( "ERROR : StringUtil.nvlInt >> Param : " + str );
    		return 0;
    	}

    }

	public static long nvlLong(Object str ) {
		try{
			if (str == null || "".equals(str)) {
				return 0L;
			}else{
				return Long.parseLong( str.toString() );
			}
		}catch( Exception e ){
			System.out.println( "ERROR : StringUtil.nvlInt >> Param : " + str );
			return 0L;
		}

	}

    /**
     * length 만큼 앞에 0을 추가한다.
     * @param str
     * @param length
     * @return
     */
	public static String addZero (String str, int length) {
		String temp = "";
		for (int i = str.length(); i < length; i++)
			temp += "0";
		temp += str;
		return temp;
	}

    /**
     * json Array 데이터를 List<Map>로 변환 후 리턴한다.
     * @param jsonData
     * @return List<Map>
     * @throws UserException : 사용자 정의 메세지
     */
	public static List<Map> convertJsonToList( String jsonData ) throws Exception {
		try{
			jsonData = org.apache.commons.lang.StringEscapeUtils.unescapeHtml(jsonData.replace("&amp;#39;", "`").replace("&apos;", "'"));

			if (false) {
				JSONArray jsonArr = JSONArray.fromObject(jsonData.replace("&quot;", "\"").replaceAll(":null", ":\"\""));
				List<Map> list = JSONArray.toList(jsonArr, HashMap.class);
				return list;
			}
			else { // JSONArray를 사용할 경우 문자열 안에 대괄호가 들어간 경우에 parsing error가 발생하여, jackson lib로 대체
				ObjectMapper mapper = new ObjectMapper();
//				List<Map> list = mapper.readValue(jsonData.replace("&quot;", "\"").replaceAll(":null", ":\"\""), new TypeReference<List<Map>>(){});
				List<Map> list = mapper.readValue(jsonData.replace("&quot;", "\"").replaceAll(":null", ":\"\""), List.class);
				return list;
			}
		}catch(Exception e){
			throw new UserException("000000500", "Json 변환시 에러가 발생하였습니다.");
		}
	}

	/**
	 * <pre>
	 * json MorphDynaBean Array 데이터를 List<Map>로 변환 후 리턴한다.
	 * ex) input format : net.sf.ezmorph.bean.MorphDynaBean@11bd0f3b[  {CONSTRUCTION_CNT=1, ADD_PRICE=0}, {CONSTRUCTION_CNT=1, ADD_PRICE=0}  ]
	 * </pre>
	 * @param obj
	 * @return List<Map>
	 * @throws Exception
	 */
	public static List<Map> convertJsonToListTp1( Object obj ) throws Exception {
		try{

			List objList = (List)obj;
			List rtnList = new ArrayList();

			for( int i=0; i<objList.size(); i++ ){
				try{
					net.sf.ezmorph.bean.MorphDynaBean bean = (net.sf.ezmorph.bean.MorphDynaBean)objList.get(i);
					rtnList.add(JSONObject.fromObject( bean ));
				}catch(ClassCastException e1){
					System.out.println( "StringUtil > convertJsonToListTp1 > ClassCastException Message :: " + e1.getMessage() );
					HashMap bean = (HashMap)objList.get(i);
					rtnList.add(JSONObject.fromObject( bean ));
				}catch(Exception e2){
					System.out.println( "StringUtil > convertJsonToListTp1 > Exception Message :: " + e2.getMessage() );
				}
			}

			return rtnList;
		}catch(Exception e){
			throw new UserException("000000500", "Json 변환시 에러가 발생하였습니다.");
		}
	}

	/**
	 * Map에서 Json 으로 변경한다.
	 * @param transData
	 * @return
	 */
	public static JSONObject jsonConveter( Map<String, Object> transData ){

    	JSONObject jsonObject = new JSONObject();

    	for( Map.Entry<String, Object> entry : transData.entrySet() ) {
			String key = entry.getKey();
			Object value = entry.getValue();
			jsonObject.put(key, value);
		}

    	return jsonObject;
	}

	/**
	 * 입력받은 가격과 수량을 나누었을때 소수인지 소수가 아닌지 구분
	 * @param argv1	가격
	 * @param argv2	수량
	 * @return
	 */
    public static HashMap isPrime( int argv1, int argv2 ){

    	HashMap rtnMap = new HashMap();

        // 소수를 판별할 논리형 변수
        boolean isPrime = false;

        //int argvInt1 = StringUtil.nvlInt(argv1);
        //int argvInt2 = StringUtil.nvlInt(argv2);

        if( argv1 % argv2 == 0 || argv2 == 1 ){
            // 나누어지는 수가 있을 경우 isPrime의 값을 true로 바꾼다.
            isPrime = true;
        }else{
        	isPrime = false;
        }

        // 위 조건문의 결과에 따라 아래의 조건문을 실행한다.
        if (isPrime) {
        	System.out.println("--------------------------------------\n");
        	System.out.println(argv1 + "은(는) 소수가 아닙니다.\n");
        	System.out.println("분할금액 : " + (argv1 / argv2) + "\n");
        	System.out.println("나머지금액 : " + (argv1 % argv2) + "\n");
        	System.out.println("--------------------------------------\n");

        } else {
        	System.out.println("--------------------------------------\n");
        	System.out.println(argv1 + "은(는) 소수입니다.");
        	System.out.println("분할금액 : " + (argv1 / argv2) + "\n");
        	System.out.println("나머지금액 : " + (argv1 % argv2) + "\n");
        	System.out.println("--------------------------------------\n");
        }

        rtnMap.put("divisionNum", argv1 / argv2);
        rtnMap.put("remainder", argv1 % argv2);

        return rtnMap;

    }

	/*
	 * ∀ 구분자로 붙여진 문자열을 ArrayList로 리턴한다.
	 * @param : String
	 * @return : List<String>
	 */
	public static ArrayList<String> getArrayList(String str) {
		if(!StringUtil.isNvl(StringUtil.nvl(str)) && !Constants.ARRAY_SEPARATORS.equals(StringUtil.nvl(str))) {
			return new ArrayList<String>(Arrays.asList(str.split(Constants.ARRAY_SEPARATORS)));
		}else {
			return new ArrayList<String>();
		}
	}

	/*
	 * ∀ 구분자로 붙여진 문자열을 ArrayList로 리턴한다.
	 * @param : String
	 * @return : List<String>
	 */
	public static List<String> getArrayListWithoutAll(String filter) {
		return Stream.of(filter.split(Constants.ARRAY_SEPARATORS))
				.map(String::trim)
				.filter(x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x))
				.collect(Collectors.toList());
	}

	/*
	 * , 구분자로 붙여진 문자열을 ArrayList로 리턴한다.
	 * @param : String
	 * @return : List<String>
	 */
	public static ArrayList<String> getArrayListComma(String str) {
		if(!StringUtil.isNvl(StringUtil.nvl(str)) && !Constants.ARRAY_SEPARATORS.equals(StringUtil.nvl(str))) {
			return new ArrayList<String>(Arrays.asList(str.split(",+")));
		}else {
			return new ArrayList<String>();
		}
	}

	/*
	 *  StringEncryptor 를 이용한 데이타 암호화.
	 * @param : String
	 * @return : Encryptied String
	 */
	public static String encryptStr(String encKey, String str) {
		try {
			if(str != null && str.length() > 0) {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setAlgorithm("PBEWithMD5AndDES");
				pbeEnc.setPassword(encKey);
				return pbeEnc.encrypt(str);
			} else {
				return "";
			}
		} catch (Exception ex) {
			return "";
		}
	}

	/*
	 *  StringEncryptor 를 이용한 데이타 복호화
	 * @param : String
	 * @return : Encryptied String
	 */
	public static String decryptStr(String encKey, String encStr) {
		try {
			if(encStr != null && encStr.length() > 0) {
				StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
				pbeEnc.setAlgorithm("PBEWithMD5AndDES");
				pbeEnc.setPassword(encKey);
				return pbeEnc.decrypt(encStr);
			} else {
				return "";
			}
		} catch (Exception ex) {
			return "";
		}
	}


	/*
	 * ECS 연동시 특수 문자코드를 문자로 치환
	 * @param String
	 * @return String
	 */
	public static String htmlSingToText(String returnStr){

        returnStr = returnStr.replaceAll("&gt;", ">");
        returnStr = returnStr.replaceAll("&lt;", "<");
        returnStr = returnStr.replaceAll("&gt", ">");
        returnStr = returnStr.replaceAll("&lt", "<");
        returnStr = returnStr.replaceAll("&quot;", "\"");
        returnStr = returnStr.replaceAll("&nbsp;", " ");
        returnStr = returnStr.replaceAll("&amp;", "&");
        returnStr = returnStr.replaceAll("&#45;", "-");
        returnStr = returnStr.replaceAll("&#45", "-");
        return returnStr;
    }

	/*
	 * String byte 길이 return
	 * @param str String
	 * @return int
	 */
    public static int getByteLength(String str){
		//return str.getBytes("EUC-KR").length;
		byte[] bytTemp = null;
		int iLength = 0;
		try {
			bytTemp = str.getBytes("EUC-KR");
			iLength = bytTemp.length;
		} catch (Exception e){

		}

		return iLength;
		// return str.getBytes(StandardCharsets.UTF_8).length;
	}

	/*
	 * 숫자여부
	 * @param str String
	 * @return boolean
	 */
	public static boolean isNumeric(String str){
		return StringUtils.isNumeric(str);
	}

	/**
	 * 전화번호, 휴대폰 문자열 제거
	 */
	public static String getPhoneStringReplace(String str){
		str = str.trim();
		str = str.replaceAll("-", "");
		str = str.replaceAll("\\*", "");
		str = str.replaceAll("\\.", "");
		str = str.replaceAll("[+]", "");
		str = str.replaceAll(" ", "");
		str = str.replaceAll("\\p{Z}", "");
		return str;
	}

	/**
	 * 특정문자열 대체
	 * @param str
	 * @param replacement
	 * @return
	 */
	public static String blankTo(String str, String replacement) {
		return str.replaceAll("\\s", replacement);
	}

	/**
	 * 숫자인지 체크
	 * @param str
	 * @return
	 */
	public static String digitsOnly(String str) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(str)) {
			return str;
		}
		return str.replaceAll("[^0-9]+","");
	}

	/**
	 * 숫자인지 체크
	 * @param str
	 * @return
	 */
	public static boolean isDigits(String str) {
		if(str.matches("^[0-9]*$")) {
			return true;
		}

		return false;
	}

	/**
	 * boolean false to blank
	 * @param flag
	 * @return
	 */
	public static String booleanFalseToBlank(boolean flag) {

		if (StringUtil.isEmpty(flag)) {
			return "";
		} else if (flag == false){
			return "";
		}
		return ""+ flag;
	}

	public static String strSubString(String str, int length){
		return str.length() > length ? str.substring(0, length) : str;
	}

	public static String numberFormat(long inValue) {
		DecimalFormat df = new DecimalFormat("#,##0");
		return df.format(inValue);
	}

	/**
	 * 문자열 자르기
	 *
	 * @param str the 문자
	 * @param len the 제한길이
	 * @param isAddDot the is '...'추가여부
	 * @return the string
	 */
	public static String cutString(String strData, int iByteLength , String addDot  ) {
		byte[] bytTemp = null;
		int iRealStart = 0;
		int iRealEnd = 0;
		int iLength = 0;
		int iChar = 0;
		int iStartPos = 0;
		try {
			// UTF-8로 변환하는경우 한글 2Byte, 기타 1Byte로 떨어짐
			bytTemp = strData.getBytes("EUC-KR");
			iLength = bytTemp.length;

			for(int iIndex = 0; iIndex < iLength; iIndex++) {
				if(iStartPos <= iIndex) {
					break;
				}
				iChar = (int)bytTemp[iIndex];
				if((iChar > 127)|| (iChar < 0)) {
					// 한글의 경우(2byte 통과처리)
					// 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
					iRealStart++;
					iIndex++;
				} else {
					// 기타 글씨(1Byte 통과처리)
					iRealStart++;
				}
			}

			iRealEnd = iRealStart;
			int iEndLength = iRealStart + iByteLength;
			for(int iIndex = iRealStart; iIndex < iEndLength; iIndex++)
			{
				iChar = (int)bytTemp[iIndex];
				if((iChar > 127)|| (iChar < 0)) {
					// 한글의 경우(2byte 통과처리)
					// 한글은 2Byte이기 때문에 다음 글자는 볼것도 없이 스킵한다
					iRealEnd++;
					iIndex++;
				} else {
					// 기타 글씨(1Byte 통과처리)
					iRealEnd++;
				}
			}
		} catch(Exception e) {
			//
			System.out.println("e.getMessage() : " + e.getMessage());
		}

		return strData.substring(iRealStart, iRealEnd) + addDot;
	}

	/**
	 * 바이트 계산하여 범위에 속한 문자열을 리턴. (해당 문자가 종료바이트보다 작으면 문자 그대로 리턴)
	 * @param str 문자열
	 * @param beginByte 시작바이트
	 * @param endByte 종료바이트
	 * @param hangleByte 한글 바이트 단위 (default=2, UTF-8=3)
	 * @param isTrim trim() 사용여부
	 * @return
	 */
	public static String getByteStr(String str, int beginByte, int endByte, int hangleByte, boolean isTrim) {
		int beginIndex = -1;
		int endIndex = -1;
		String resutName = str;

		int accByte = 0;
		for(int i=0; i<str.length(); i++){
			String ch = str.substring(i,i+1);
			accByte += ch.getBytes().length >= 2 ? hangleByte : 1;

			if(beginIndex == -1 && accByte >= beginByte){
				beginIndex = i;
			}
			if(endIndex == -1 && accByte >= endByte){
				if(accByte > endByte) {
					endIndex = i;
				} else {
					endIndex = i+1;
				}
				break;
			}
		}

		if(accByte >= endByte) resutName = isTrim ? str.substring(beginIndex, endIndex).trim() : str.substring(beginIndex, endIndex);

		return resutName;
	}

}
