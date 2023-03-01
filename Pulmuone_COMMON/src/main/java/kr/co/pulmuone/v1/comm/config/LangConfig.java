package kr.co.pulmuone.v1.comm.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvConfigBizImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.basic.dto.GetLangListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetLangListResultVo;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class LangConfig {

	public static final String LANG_CONFIG = "LANG_HASH";

	static WebApplicationContext wctx = GetSpringInfo.appContext();
    //스프링 빈 가져오기 & casting
	static SystemBasicEnvConfigBizImpl envConf_Bean = (SystemBasicEnvConfigBizImpl)wctx.getBean("SystemBasicEnvConfigBizImpl");
	static RedisTemplate redisTemplate = (RedisTemplate)wctx.getBean("redisTemplate");

    private static volatile LangConfig INSTANCE;
    private static Map<String, List<GetLangListResultVo>> LANG_CONFIG_MAP = new HashMap<String, List<GetLangListResultVo>> ();

    public static LangConfig getInstance() {
        if (INSTANCE == null)
            synchronized (LangConfig.class) {
                if (INSTANCE == null)
                    INSTANCE = new LangConfig();
            }
        return INSTANCE;
    }

    public static void reload(){
    	INSTANCE = null;
    	INSTANCE = new LangConfig();
    }

    /**
     * 생성자
     */
	private LangConfig() {

        try {
        	setConfig();
        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
        }
    }


	private void setConfig(){
        try {

        	GetLangListResponseDto dto = envConf_Bean.getLangList();
        	List<GetLangListResultVo> listLanguage = dto.getRows();

			String lang     = null;
	    	String pgm      = null;
	    	String HashKey  = null;
	    	List<GetLangListResultVo> langList = new ArrayList<GetLangListResultVo> ();

            for( int i = 0; i < listLanguage.size(); i++ ){
				GetLangListResultVo voLanguage = listLanguage.get(i);

                if (i == 0) {
                    lang = StringUtil.nvl(voLanguage.getGbLangId());
                    pgm  = StringUtil.nvl(voLanguage.getPgId())    ;

                    HashKey = lang + "_" + pgm;
                    langList.add(voLanguage);

                    continue;
                }

                if ( !StringUtil.nvl(voLanguage.getGbLangId()).equals(lang) || !StringUtil.nvl(voLanguage.getPgId()).equals(pgm) ) {

                	LANG_CONFIG_MAP.put(HashKey, langList);

					langList = new ArrayList<GetLangListResultVo>();

					lang = StringUtil.nvl(voLanguage.getGbLangId());
					pgm  = StringUtil.nvl(voLanguage.getPgId())    ;

					HashKey = lang + "_" + pgm;
					langList.add(voLanguage);

					continue;
				}

				langList.add(voLanguage);

				if( listLanguage.size() == i+1 ){
					LANG_CONFIG_MAP.put(HashKey, langList);
					langList = new ArrayList<GetLangListResultVo>();
					lang = null;
			    	pgm  = null;
			    	HashKey = null;
				}
	    	}

	    	redisTemplate.opsForValue().set(LANG_CONFIG, LANG_CONFIG_MAP);

	    	LANG_CONFIG_MAP = new HashMap<String, List<GetLangListResultVo>> ();

        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
        }
	}


    /**
     * 설정값을 리턴해준다.
     * @param  (String) HashMap KEY
     * @return (String) HashMap VALUE
     */
    public String getValue(String k) {

    	String rData = null;

    	Map<String, List<GetLangListResultVo>> redisLangMap = (HashMap<String, List<GetLangListResultVo>>) redisTemplate.opsForValue().get(LANG_CONFIG);

    	if (redisLangMap == null || redisLangMap.size() < 1 || redisLangMap.get(k) == null) {
            new LangConfig();
        }

    	rData = StringUtil.nvl(redisLangMap.get(k), "Not found data!!");

        return rData;
    }


    /**
     * 설정값을 리턴해준다.
     * @param  (String) HashMap KEY
     * @return List<GetLangListResultVo>
     */
    public List<GetLangListResultVo> getValueList(Map<String, String> c) {

        Map<String, List<GetLangListResultVo>> redisLangMap = (HashMap<String, List<GetLangListResultVo>>) redisTemplate.opsForValue().get(LANG_CONFIG);

        List<GetLangListResultVo> list = (ArrayList<GetLangListResultVo>) redisLangMap.get( StringUtil.nvl(c.get("GB_LANG_ID")) +"_common" );

		if ( redisLangMap.containsKey( StringUtil.nvl(c.get("GB_LANG_ID")) + "_" + StringUtil.nvl(c.get("PG_ID"))) ) {
    		list.addAll( (ArrayList<GetLangListResultVo>) redisLangMap.get( StringUtil.nvl(c.get("GB_LANG_ID")) +"_"+ StringUtil.nvl(c.get("PG_ID"))) );
    	}

        return list;
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
