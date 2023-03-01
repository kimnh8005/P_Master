package kr.co.pulmuone.bos.comm.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvConfigBiz;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvListResultVo;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class EnvConfig {

	public static final String ENV_CONFIG = "ENV_HASH";

	static WebApplicationContext wctx = GetSpringInfo.appContext();

    //스프링 빈 가져오기 & casting
	static SystemBasicEnvConfigBiz envConf_Bean = (SystemBasicEnvConfigBiz)wctx.getBean("SystemBasicEnvConfigBizImpl");
	static RedisTemplate redisTemplate = (RedisTemplate)wctx.getBean("redisTemplate");

    private static volatile EnvConfig INSTANCE;
    private static Map<String, String> ENV_CONFIG_MAP = new HashMap<String, String>();

    public static EnvConfig getInstance() {
        if (INSTANCE == null)
            synchronized (EnvConfig.class) {
                if (INSTANCE == null)
                    INSTANCE = new EnvConfig();
            }
        return INSTANCE;
    }

    public static void reload(){
        //단일 서버와 데이터베이스 관계가 1대 다중이라 주석처리
        //CONFIG_MAP = new HashMap<String, String>();
    	INSTANCE = null;
    	INSTANCE = new EnvConfig();
    }

    /**
     * 생성자
     */
	private EnvConfig() {
        try {
        	setConfig();
        } catch (Exception e) {
            e.printStackTrace();
            this.reset();
        }
    }

	private void setConfig(){
        try {
        	GetEnvListResponseDto resultDto = envConf_Bean.getEnvList();
            List<GetEnvListResultVo> l = resultDto.getRows();

            for (GetEnvListResultVo vo : l) {
                if (vo == null) { continue; }

                ENV_CONFIG_MAP.put(StringUtil.nvl(vo.getEnvKey()), StringUtil.nvl(vo.getEnvValue()));
            }

			redisTemplate.opsForValue().set(ENV_CONFIG, ENV_CONFIG_MAP);
			ENV_CONFIG_MAP = new HashMap<String, String>();

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

    	HashMap<String, String> redisEnvMap = (HashMap<String, String>)redisTemplate.opsForValue().get(ENV_CONFIG);

    	if (redisEnvMap == null || redisEnvMap.size() < 1 || redisEnvMap.get(k) == null) {
            new EnvConfig();
        }

    	rData = StringUtil.nvl(redisEnvMap.get(k), "Not found data!!");

        return rData;
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
