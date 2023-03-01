package kr.co.pulmuone.v1.comm.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PhoneUtil {

    //휴대전화 Validation
    public static boolean isCellValidate(String number) {
        //빈값인지 확인
        if(StringUtil.isEmpty(number)) return false;
        //숫자만있는지 확인
        if(!isNumberNumeric(number)) return false;
        //폰타입확인
        //if(!isCellNumberType(number)) return false;
        return true;
    }

    //일반전화 Validation
    public static boolean isLandlineValidate(String number) {
        //빈값인지 확인
        if(StringUtil.isEmpty(number)) return false;
        //숫자만있는지 확인
        if(!isNumberNumeric(number)) return false;
        //폰타입확인
        //if(!isLandlineNumberType(number)) return false;
        return true;
    }
    
    // 전화번호 문자포함 여부 확인
    public static boolean isNumberNumeric(String number) {
        return StringUtil.isNumeric(number.replaceAll("-", ""));
    }

    // 휴대전화 형식 확인
    public static boolean isCellNumberType(String str) {
        return Pattern.matches("(01[016789])-(\\d{3,4})-(\\d{4})", str);
    }

    // 일반 전화 형식 확인
    public static boolean isLandlineNumberType(String str) {
        return Pattern.matches("(\\d{2,3})-(\\d{3,4})-(\\d{4})", str);
    }

    // 연락처 형식 만들기
    public static String makePhoneNumber(String src) {

        if(src == null || "".equals(src)) return "";

        if(src.length() == 8) {
            return src.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
        } else if (src.length() == 12) {
            return src.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
        }

        return src.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }
    public static boolean isValid(String num) {
        if (num == null) {
            return false;
        }
        num = StringUtil.blankTo(num, "");

        num = StringUtil.digitsOnly(num);

        if (StringUtils.isEmpty(num)) {
            return false;
        }
        if (num.length() < 7 || num.length() > 12) {
            return false;
        }
        return true;
    }
}
