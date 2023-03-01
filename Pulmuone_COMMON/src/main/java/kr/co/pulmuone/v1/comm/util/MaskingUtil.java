package kr.co.pulmuone.v1.comm.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

@Slf4j
public class MaskingUtil {
    public static String name(String str) {
        if (str == null || str.length() == 0 || str.length() == 1) return str;
        if (str.length() == 2) {
            return str.charAt(0) + "*";
        }
        StringBuilder result = new StringBuilder();
        result.append(str.charAt(0));
        for (int i = 0; i < str.length() - 2; i++) {
            result.append("*");
        }
        result.append(str.charAt(str.length() - 1));
        return result.toString();
    }

    public static String address(String str) {
        int flag = Math.max(Math.max(Math.max(Math.max(str.lastIndexOf("로"), str.lastIndexOf("읍")), str.lastIndexOf("면")), str.lastIndexOf("동")), str.lastIndexOf("길")) + 1;

        StringBuilder result = new StringBuilder();
        result.append(str, 0, flag);
        for (int i = 0; i < str.length() - flag; i++) {
            result.append("*");
        }

        return result.toString();
    }

    public static String addressDetail(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            result.append("*");
        }

        return result.toString();
    }

    public static String cellPhone(String str) {
        if (str.length() < 8) return str;
        StringBuilder result = new StringBuilder();
        result.append(str, 0, 3);
        for (int i = 0; i < str.length() - 7; i++) {
            result.append("*");
        }
        result.append(str.substring(str.length() - 4));

        return result.toString();
    }

    public static String telePhone(String str) {
        if (str.length() < 7) return str;
        StringBuilder result = new StringBuilder();
        if (str.startsWith("02")) {
            result.append(str, 0, 2);
            for (int i = 0; i < str.length() - 6; i++) {
                result.append("*");
            }
        } else {
            result.append(str, 0, 3);
            for (int i = 0; i < str.length() - 7; i++) {
                result.append("*");
            }
        }
        result.append(str.substring(str.length() - 4));

        return result.toString();
    }

    public static String email(String str) {
        String[] strArr = str.split("@");
        if (strArr.length < 2) return str;

        StringBuilder result = new StringBuilder();
        int len = 3;
        if (strArr[0].length() > 2) {
            result.append(strArr[0], 0, strArr[0].length() - 3);
        } else if (strArr[0].length() == 1 || strArr[0].length() == 2) {
            len = 1;
            result.append(strArr[0], 0, strArr[0].length() - 1);
        } else {
            return str;
        }

        for (int i = 0; i < len; i++) {
            result.append("*");
        }
        result.append("@");
        result.append(strArr[1]);

        return result.toString();
    }

    public static String accountNumber(String str) {
        if (str.length() < 6) return str;

        return str.substring(0, str.length() - 6) + "******";
    }

    public static String birth(String str) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            result.append("*");
        }

        return result.toString();
    }

    public static String loginId(String str) {
        if (str.length() < 4) return str;
        StringBuilder result = new StringBuilder();
        result.append(str, 0, 3);
        for (int i = 0; i < str.length() - 3; i++) {
            result.append("*");
        }
        return result.toString();
    }

    // 이름+ID 마스킹
    public static String nameLoginId(String str) {
      String maskingName = "";
      String maskingLoginId = "";
      String[] strArr = str.split("/");
      if (strArr != null) {
        if (strArr[0] != null && strArr[0] != "") {
          maskingName = name(strArr[0]);
        }
        if (strArr.length >= 2) {
          maskingLoginId = loginId(strArr[1]);
          maskingLoginId = " / " + maskingLoginId;
        }
      }
      return  maskingName + maskingLoginId;
    }

    // 카드 마스킹
    public static String cardNumber(String str) {
        if(StringUtils.isEmpty(str)){
            return "";
        }
		String[] strArray = str.replace("-", "").split("");
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < strArray.length; i++) {
			if (i != 0 && i % 4 == 0) {
				result.append("-");
			}
			if (6 <= i && i <= 11) {
				result.append("*");
			} else {
				result.append(strArray[i]);
			}
		}
		return result.toString();
	}

    // 이용권 번호 마스킹
    public static String serialNumber(String str) {
        if (str.length() < 5) return str;
        StringBuilder result = new StringBuilder();
        result.append(str, 0, 2);
        for (int i = 2; i < str.length() - 2; i++) {
            result.append("*");
        }
        result.append(str.substring(str.length() - 2));
        return result.toString();
    }
}
