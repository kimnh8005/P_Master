package kr.co.pulmuone.v1.comm.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.*;

public class CookieUtil {
    /**
     * Cookie를 설정한다.
     * @param response
     * @param name
     * @param value
     * @throws UnsupportedEncodingException
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int keepTime) throws UnsupportedEncodingException{

    	if( value != null ){
    		value = URLEncoder.encode(value, "UTF-8");
            //value = value.replaceAll("\r", "").replaceAll("\n", "");
    	}

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(keepTime);
        response.addCookie(cookie);
    }

    public static void setCookieSecure(HttpServletResponse response, String name, String value, int keepTime, boolean isSecure) throws UnsupportedEncodingException{

    	if( value != null ){
    		value = URLEncoder.encode(value, "UTF-8");
            //value = value.replaceAll("\r", "").replaceAll("\n", "");
    	}

        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(keepTime);
        cookie.setSecure(isSecure);
        response.addCookie(cookie);
    }

    /**
     * 설정된 Cookie를 가져간다.
     * @param request
     * @param cookieName
     * @return
     * @throws Exception
     */
    public static String getCookie(HttpServletRequest request, String cookieName) throws Exception {
        Cookie [] cookies = request.getCookies();
        if (cookies==null) return "";
        String value = "";
        for(int i=0;i<cookies.length;i++) {
            if(cookieName.equals(cookies[i].getName())) {
                value = URLDecoder.decode(cookies[i].getValue(), "UTF-8");
                //value = value.replaceAll("\r", "").replaceAll("\n", "");
                break;
            }
        }
        return value;
    }

    /**
     * cookieName이 포함된 Cookie 리스트 조회
     * @param request
     * @param cookieName
     * @return
     * @throws Exception
     */
    public static List<String> getLikeNameCookieList(HttpServletRequest request, String cookieName) throws Exception {
        Cookie [] cookies = request.getCookies();
        List<String> cookieList = new ArrayList<>();
        if (cookies==null) return cookieList;

        for(Cookie cookie : cookies) {
			if(cookie.getName().contains(cookieName)) {
				cookieList.add(URLDecoder.decode(cookie.getValue(), "UTF-8"));
			}
		}
        return cookieList;
    }
}
