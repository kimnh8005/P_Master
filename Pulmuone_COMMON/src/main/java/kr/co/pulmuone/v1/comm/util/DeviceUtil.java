package kr.co.pulmuone.v1.comm.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;

@SuppressWarnings({"rawtypes"})
public class DeviceUtil {

	/**
	 * 디바이스 정보를 리턴한다.
	 * @param (String) userAgent - Header 정보
	 * @return (String) 디바이스명
	 */
	public static String getDeviceInfo(String userAgent) {

		String rtnS = "PC";
		userAgent = StringUtil.nvl(userAgent).toLowerCase();
		if( !"".equals(userAgent) ){
			// TODO : 모바일 기종 수집하기...
			String[] mobileOS = { "iphone","ipod","android","blackberry","windows ce",
		                          "nokia","webos","opera mini","sonyericsson","opera mobi",
		                          "ipad","iemobile","mobile"};
			for(int i = 0 ; i<mobileOS.length ; i++){
				if(userAgent.indexOf(mobileOS[i]) > -1){
					rtnS = mobileOS[i];
					break;
				}
			}
		}
		return rtnS;
	}
	/**
	 * 디바이스 정보를 리턴한다.
	 * @param (String) userAgent - Header 정보
	 * @return (String) 디바이스명
	 */
	public static String getOSInfo(String userAgent) {

		String os = "PC";
		 if (userAgent.toLowerCase().indexOf("windows") >= 0 ){
             os = "Windows";
         } else if(userAgent.toLowerCase().indexOf("mac") >= 0){
             os = "Mac";
         } else if(userAgent.toLowerCase().indexOf("x11") >= 0){
             os = "Unix";
         } else if(userAgent.toLowerCase().indexOf("android") >= 0){
             os = "Android";
         } else if(userAgent.toLowerCase().indexOf("iphone") >= 0){
             os = "IPhone";
         }else{
             os = "UnKnown, More-Info: "+userAgent;
         }
		 return os;
	}
	/**
	 * 디바이스 정보를 리턴한다.
	 * @param (String) userAgent - Header 정보
	 * @return (String) 디바이스명
	 */
	public static String getBrowserInfo(String userAgent) {
		String browser="";
		String user=userAgent.toLowerCase();
		   //===============Browser===========================
        if (user.contains("msie")){
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        }else if (user.contains("safari") && user.contains("version")){
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        }else if ( user.contains("opr") || user.contains("opera")){
            if(user.contains("opera"))
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]+"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            else if(user.contains("opr"))
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-")).replace("OPR", "Opera");
        } else if (user.contains("chrome")){
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)  || (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1) || (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1) ){
            browser = "Netscape-?";
        } else if (user.contains("firefox")){
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv")){
            browser="IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
        } else{
            browser = "UnKnown, More-Info: "+userAgent;
        }
        return browser;
	}
	/**
	 * 디바이스 정보를 리턴한다.
	 * @param (String) userAgent - Header 정보
	 * @return (Map) 디바이스의 모든 정도
	 */

	public static HashMap getInfo(String userAgent) {
		HashMap h = new HashMap();
		return h;
	}


	/**
	 * 서버 아이피 정보를 가져온다.
	 *
	 * @param info
	 * @return 아이피
	 */
	public static String getServerIp() {

		String hostAddr = "";

		try {

			Enumeration<NetworkInterface> nienum = NetworkInterface.getNetworkInterfaces();

			while (nienum.hasMoreElements()) {

				NetworkInterface ni = nienum.nextElement();

				Enumeration<InetAddress> kk= ni.getInetAddresses();

				while (kk.hasMoreElements()) {

					InetAddress inetAddress = kk.nextElement();

					if (!inetAddress.isLoopbackAddress() &&

					!inetAddress.isLinkLocalAddress()

					//&&inetAddress.isSiteLocalAddress()
					) {
						 hostAddr = inetAddress.getHostAddress().toString();
					}
				}
			}

		} catch (SocketException e) {
			e.printStackTrace();
		}

		return hostAddr;

	}

	private static String getHeaderValue(String header){
		try{
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest req = servletRequestAttributes.getRequest();

		    return req.getHeader("User-Agent");
		}catch(Exception ex){
			return null;
		}
	}

	public static String getDirInfo(){
		try{
		    String userAgent = DeviceUtil.getDeviceInfo(DeviceUtil.getHeaderValue("User-Agent"));

			if( "PC".equals(userAgent) ){
				return "pc";
			}else{
				return "mobile";
			}

		}catch(Exception ex){
			return null;
		}
	}


	/**
	 * 앱체크
	 * @return boolean
	 */
	public static boolean isApp() {

		try {
		    String userAgent = DeviceUtil.getHeaderValue("User-Agent");

			if (userAgent.indexOf("IOSApp") >= 0 ){
				return true;
			}else if (userAgent.indexOf("AOSApp") >= 0 ){
				return true;
			}
			return false;

		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 사용자 device 로 GoodsEnum.DeviceType 리턴
	 * @return DeviceType
	 */
	public static DeviceType getGoodsEnumDeviceTypeByUserDevice() {
		if("pc".equals(getDirInfo())) {
			return GoodsEnums.DeviceType.PC;
		} else {
			if(isApp()) {
				return GoodsEnums.DeviceType.APP;
			} else {
				return GoodsEnums.DeviceType.MOBILE;
			}
		}

	}

	public static boolean isIos() {
	    String userAgent = DeviceUtil.getHeaderValue("User-Agent");

		if (userAgent.indexOf("IOSApp") >= 0 ){
			return true;
		} else {
			return false;
		}
	}
}