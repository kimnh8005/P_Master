package kr.co.pulmuone.v1.comm.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.pulmuone.v1.system.basic.service.SystemBasicSiteConfigBiz;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetPsConfigListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetStShopListResultVo;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetUrGroupResultResultVo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SiteConfig {

	public static final String SITE_CONFIG = "SITE_HASH";

	static WebApplicationContext wctx = GetSpringInfo.appContext();
    //스프링 빈 가져오기 & casting
	static SystemBasicSiteConfigBiz siteConfBean = (SystemBasicSiteConfigBiz)wctx.getBean("SystemBasicSiteConfigBizImpl");
	static RedisTemplate redisTemplate  = (RedisTemplate)wctx.getBean("redisTemplate");

    private static volatile SiteConfig INSTANCE;
    private static Map<String, Object> SITE_CONFIG_MAP = new HashMap<String, Object>();

    public static SiteConfig getInstance() {
        if (INSTANCE == null)
            synchronized (SiteConfig.class) {
                if (INSTANCE == null)
                    INSTANCE = new SiteConfig();
            }
        return INSTANCE;
    }

    public static void reload(){
    	INSTANCE = new SiteConfig();
    }

    /**
     * 생성자
     */
	private SiteConfig() {

        try {
        	setConfig();
        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
        }
    }

	private void setConfig() {
        try {

        	SITE_CONFIG_MAP = siteConfBean.siteConfigList();

			redisTemplate.opsForValue().set(SITE_CONFIG, SITE_CONFIG_MAP);
			SITE_CONFIG_MAP = new HashMap<String, Object>();

        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
        }
	}

	/**
	 * 회원그룹정보를 리턴한다.
	 * @param 회원그룹아이디
	 * @return GetUrGroupResultResultVo (회원그룹정보)
	 */
    public GetUrGroupResultResultVo getUrGrpValue(String k) {

        Map<String, Object> redisSiteMap = (HashMap<String, Object>)redisTemplate.opsForValue().get(SITE_CONFIG);
        List<GetUrGroupResultResultVo> listUserGroup = (List<GetUrGroupResultResultVo>)redisSiteMap.get("UR_GROUP");

        int cnt = 0;
    	GetUrGroupResultResultVo userGroup = new GetUrGroupResultResultVo();

        for (GetUrGroupResultResultVo vo : listUserGroup) {
        	if ( vo == null ) { continue; }

        	if ( k.equals(StringUtil.nvl( vo.getGroupId() )) ) {
        		cnt = cnt + 1;
        		userGroup = vo ;
        		break;
        	}
        }

        if (cnt < 1) {
            return null;
        }

        return userGroup;
    }


    /**
     * 상점 마스터 정보를 리턴한다.
     * @param
     * @return GetStShopListResultVo (상점정보)
     */
    public GetStShopListResultVo getShopValue() {

        Map<String, Object> redisSiteMap = (HashMap<String, Object>)redisTemplate.opsForValue().get(SITE_CONFIG);
        List<GetStShopListResultVo> listStShop = (List<GetStShopListResultVo>)redisSiteMap.get("ST_SHOP");

        GetStShopListResultVo stShop = new GetStShopListResultVo();

        if ( listStShop != null && !listStShop.isEmpty() ) {
        	stShop = listStShop.get(0); // 첫번째 Row 만 Set
        } else {
            return null;
        }

        return stShop;
    }


    /**
     * 상점 상세정보를 리턴한다.
     * @param k
     * @return String (상점설정 정보)
     */
    public String getPsConfingValue(String k) {

        Map<String, Object> redisSiteMap = (HashMap<String, Object>)redisTemplate.opsForValue().get(SITE_CONFIG);
        List<GetPsConfigListResultVo> listPsConfig = (List<GetPsConfigListResultVo>)redisSiteMap.get("PS_CONFIG");

        int cnt = 0;
        String psValue = "";

        for (GetPsConfigListResultVo vo : listPsConfig) {
        	if ( vo == null ) { continue; }

        	if ( k.equals(StringUtil.nvl( vo.getPsKey() )) ) {
        		cnt = cnt + 1;
        		psValue = vo.getPsValue();
        		break;
        	}
        }

        if (cnt < 1) {
            return null;
        }

        return psValue;
    }


    /**
     * 메모리 초기화
     */
    public void reset() {
        INSTANCE = null;
    }


	static class GetSpringInfo{
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
			try{
				//현재 요청중인 thread local의 HttpServletRequest 객체 가져오기
		        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		        //HttpSession 객체 가져오기
		        HttpSession session = request.getSession();
		        //ServletContext 객체 가져오기 -> Spring Context 가져오기
		        wctx = WebApplicationContextUtils.getWebApplicationContext( session.getServletContext() );
			}catch(Exception e){
				e.printStackTrace();
			}
			return wctx;

		}
	}


}
