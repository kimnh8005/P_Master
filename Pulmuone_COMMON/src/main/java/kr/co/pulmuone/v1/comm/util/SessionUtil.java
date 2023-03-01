package kr.co.pulmuone.v1.comm.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;



/**
 * 2014.05.22 서버 이중화 작업으로 Session Cluster 관련 추가작업. implements Serializable
 * @author sang-gu
 *
 */

@SuppressWarnings({"serial"})
public class SessionUtil implements Serializable{

	static WebApplicationContext wctx = GetSpringInfo.appContext();
	static RedisTemplate redisTemplate = (RedisTemplate)wctx.getBean("redisTemplate");

	public static final String SESSION_ATTR_NAME = "userInfo";

	/**
	 * 사용자 세션정보를 반환환다.
	 * @param session
	 * @return 세션정보
	 */
//	private static Object getUserVO(HttpSession session) {
//
//		try{
//			/*
//            if (session.getAttribute(SESSION_ATTR_NAME) != null) {
//				UserVO objUser = (UserVO)session.getAttribute(SESSION_ATTR_NAME);
//				return objUser;
//			}
//			*/
//
//	    	//************************ redis logic start ************************
//            //System.out.println("get session(SessionUtil - getUserVO(session)) session id : " + session.getId());
//
//			return redisTemplate.opsForValue().get(session.getId());
//
//            //************************ redis logic end **************************
//		}catch(Exception ex){
//			return null;
//		}
//
//	}

	/**
	 * 사용자 세션정보 반영한다.
	 * @param vo
	 * @return
	 */
	public static void setUserVO(Object vo){
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	    HttpServletRequest req = servletRequestAttributes.getRequest();
		HttpSession session = req.getSession();

//		redisTemplate.opsForValue().set(session.getId(), vo);
	    session.setAttribute("userVo", vo); // 사용자 정보를 새로운 key로 저장하지 않고 session에 저장
	}

	/**
	 * 사용자 세션정보를 반환환다.
	 * @return 세션정보
	 */
	private static Object getUserVO(){

		Object sessionInfo = null;

		//UserVo sessionInfo = null;

		try{
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest req = servletRequestAttributes.getRequest();
		    HttpSession session = req.getSession(false);

            if ( session != null ) {
		    	//************************ redis logic start ************************
                //System.out.println("get session(SessionUtil - getUserVO(session)) session id : " + session.getId());

//		    	sessionInfo = redisTemplate.opsForValue().get(session.getId());
		    	sessionInfo = session.getAttribute("userVo"); // session에 저장된 사용자 정보를 활용

		    	//************************ redis logic end **************************
                //sessionInfo = (UserVO)session.getAttribute(SESSION_ATTR_NAME);
		    }

		    return sessionInfo;

		} catch(Exception ex) {
			return null;
		}
	}

	public static UserVo getBosUserVO(){

        UserVo vo = (UserVo)getUserVO();

        if (vo != null) {
            if (vo.getRoleId() != null) { // 권한 ID를 list형태로 변환
            	String roleId = vo.getRoleId();
            	String[] splitRoleId = roleId.split(",");

            	List<String> listRoleId = new ArrayList<String>();
            	for (int i = 0; i < splitRoleId.length; i++) {
            		listRoleId.add(splitRoleId[i]);
            	}

            	vo.setListRoleId(listRoleId);
            }

            if (vo.getAuthSupplierId() != null) { // 공급업체 ID를 list형태로 변환
            	String authSupplierId = vo.getAuthSupplierId();
            	String[] splitSupplierId = authSupplierId.split(",");

            	List<String> listAuthSupplierId = new ArrayList<String>();
            	for (int i = 0; i < splitSupplierId.length; i++) {
            		listAuthSupplierId.add(splitSupplierId[i]);
            	}

            	vo.setListAuthSupplierId(listAuthSupplierId);
            }

            if (vo.getAuthWarehouseId() != null) { // 출고처 ID를 list형태로 변환
            	String authWarehouseId = vo.getAuthWarehouseId();
            	String[] splitWarehouseId = authWarehouseId.split(",");

            	List<String> listAuthWarehouseId = new ArrayList<String>();
            	for (int i = 0; i < splitWarehouseId.length; i++) {
            		listAuthWarehouseId.add(splitWarehouseId[i]);
            	}

            	vo.setListAuthWarehouseId(listAuthWarehouseId);
            }

            if (vo.getAuthWarehouseId() != null) { // 판매처 ID를 list형태로 변환
            	String authSellersId = vo.getAuthSellersId();
            	String[] splitSellersId = authSellersId.split(",");

            	List<String> listAuthSellersId = new ArrayList<String>();
            	for (int i = 0; i < splitSellersId.length; i++) {
            		listAuthSellersId.add(splitSellersId[i]);
            	}

            	vo.setListAuthSellersId(listAuthSellersId);
            }

            if (vo.getAuthStoreId() != null) { // 매장 ID를 list형태로 변환
            	String authStoreId = vo.getAuthStoreId();
            	String[] splitStoreId = authStoreId.split(",");

            	List<String> listAuthStoreId = new ArrayList<String>();
            	for (int i = 0; i < splitStoreId.length; i++) {
            		listAuthStoreId.add(splitStoreId[i]);
            	}

            	vo.setListAuthStoreId(listAuthStoreId);
            }

            // 세션의 expire time 저장
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest req = servletRequestAttributes.getRequest();
		    HttpSession session = req.getSession(false);
		    if (session == null) {
		    	return null;
		    } else {
		    	vo.setSessionExpireTime(session.getMaxInactiveInterval()*1000 + session.getLastAccessedTime()); // 세션 만료 시각 설정
		    }
        }

        return vo;
	}

	public static BuyerVo getBuyerUserVO(){

		BuyerVo vo = (BuyerVo)getUserVO();

        return vo;
	}

	public static void createNewSessionAfterLogin() {
		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest req = servletRequestAttributes.getRequest();
		HttpSession session = req.getSession(false);

		if (session != null)
		{
			Enumeration keys = session.getAttributeNames();
			HashMap<String, Object> hm = new HashMap<String, Object>();
			while (keys.hasMoreElements())
			{
				String key = (String)keys.nextElement();
				hm.put(key, session.getAttribute(key));
				session.removeAttribute(key);
			}
			session.invalidate();
			session = req.getSession(true);
			for (Map.Entry m : hm.entrySet())
			{
				session.setAttribute((String)m.getKey(), m.getValue());
				hm.remove(m);
			}
		}
	}

	public static String getDirInfo(){

		//UserVO sessionInfo = null;

		try{
			ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		    HttpServletRequest req = servletRequestAttributes.getRequest();
		    //HttpSession session = req.getSession(false);
		    /*
		    if( session != null ){
		    	sessionInfo = (UserVO)session.getAttribute(SESSION_ATTR_NAME);
		    }
		    */

		    //************************ redis logic start ************************
	    	/*
	    	StringBuffer sb = new StringBuffer();
			sb.append("-------------------------- get session(SessionUtil - getDirInfo) --------------------------\n");
			sb.append("session id : " + session.getId() + "\n");

			System.out.println( sb.toString() );

	    	sessionInfo = (UserVO)redisTemplate.opsForValue().get(session.getId());
	    	*/
	        //************************ redis logic end **************************

		    //return sessionInfo.getUserAgentType();

		    String userAgent = DeviceUtil.getDeviceInfo( req.getHeader("User-Agent") );

			if( "PC".equals(userAgent) ){
				return "pc";
			}else{
				return "mobile";
			}

		}catch(Exception ex){
			return null;
		}
	}


	static class GetSpringInfo {
		/**
		 *
		 * Find the root WebApplicationContext for this web application, which is
		 * typically loaded via {@link org.springframework.web.context.ContextLoaderListener}.
		 * <p>Will rethrow an exception that happened on root context startup,
		 * to differentiate between a failed context startup and no context at all.
		 *
		 * @param sc ServletContext to find the web application context for
		 * @return the root WebApplicationContext for this web app, or <code>null</code> if none
		 * @see org.springframework.web.context.WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
		 */
		public static WebApplicationContext appContext(){
			WebApplicationContext wctx = null;

			try {
				//현재 요청중인 thread local의 HttpServletRequest 객체 가져오기
		        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		        //HttpSession 객체 가져오기
		        HttpSession session = request.getSession();

		        //ServletContext 객체 가져오기 -> Spring Context 가져오기
		        wctx = WebApplicationContextUtils.getWebApplicationContext( session.getServletContext() );
			} catch (Exception e) {
				e.printStackTrace();
			}
			return wctx;
		}
	}

}