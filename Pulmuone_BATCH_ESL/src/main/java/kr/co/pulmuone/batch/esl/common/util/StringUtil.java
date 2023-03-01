package kr.co.pulmuone.batch.esl.common.util;

@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
public class StringUtil {

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
     * @param str
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
