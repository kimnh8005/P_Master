package kr.co.pulmuone.batch.erp.common.util;

@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
public class StringUtil {

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
     * @param str
     * @return 문자열
     */
    public static String nvl( Object str ) {
        if (str == null || "".equals(str)) {
        	return "";
        }else{
        	return str.toString();
        }
    }
}
