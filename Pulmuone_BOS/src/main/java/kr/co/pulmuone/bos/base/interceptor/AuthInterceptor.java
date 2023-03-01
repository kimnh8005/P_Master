package kr.co.pulmuone.bos.base.interceptor;

import kr.co.pulmuone.v1.base.service.ComnBiz;
import kr.co.pulmuone.v1.base.service.StComnBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.SystemConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums.ServerType;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.inspect.service.InspectNoticeBiz;
import kr.co.pulmuone.v1.send.slack.service.SendSlackBiz;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuRequestDto;
import kr.co.pulmuone.v1.system.menu.dto.GetMenuResponseDto;
import kr.co.pulmuone.v1.system.menu.dto.SystemUrlRequestDto;
import kr.co.pulmuone.v1.system.menu.service.SystemMenuBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter{

	@Autowired
	private ComnBiz comnBiz;

	@Autowired
	private StComnBiz stComnBiz;

	@Autowired
	private SystemMenuBiz systemMenuBiz;

    @Autowired
    private SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

	@Autowired
	private InspectNoticeBiz inspectNoticeBiz;

	@Autowired
	private SendSlackBiz sendSlackBiz;

	@Autowired
	private SendTemplateBiz sendTemplateBiz;

	@Value("${spring.profiles}")
	private String profiles;

    @Value("${app.domain}")
    private String domain; // 도메인

	/** Controller 실행 직전에 동작 **/
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		boolean isSuccess = false;

		log.info("===================================preHandle===================================");
		//request Uri
		String requestURI = request.getRequestURI();

		// CSRF 처리 S
		GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
		getEnvironmentListRequestDto.setSearchEnvironmentKey("CSRF_BOS_CHECK_YN"); // BOS CSRF 체크 설정 확인
		GetEnvironmentListResultVo getEnvironmentListResultVo = systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto);

        if ("Y".equals(getEnvironmentListResultVo.getEnvironmentValue())) {
    		String requestMethod = request.getMethod();
    		if (!"GET".equals(requestMethod)) { // 우선 GET 메소드는 제외
    			String origin = request.getHeader(HttpHeaders.ORIGIN);

// DEV1, DEV2 서버에 대한 CSRF 임시 처리 S
				// 현재 yml에서 관리하는 domain은 dev0로 설정되어 있기 때문에 dev1, dev2에서는 항상 throw exception을 던진다.
				// 때문에 dev1, dev2에 대해 임시처리 진행
                if ("https://dev1shopbos.pulmuone.online".equals(origin) || "https://dev2shopbos.pulmuone.online".equals(origin)) {
                	domain = origin;
                }
//DEV1, DEV2 서버에 대한 CSRF 임시 처리 E

    			// ORIGIN referer 체크
    			if (!domain.equals(origin)) {
					String errorMessage = "## BOS CSRF ERROR - ORIGIN domain failed ## : "+ requestURI;
    				log.error(errorMessage);
					sendSlackBiz.notify(errorMessage);
					if (!SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE.equals("0")) {
						sendTemplateBiz.sendSmsApi(AddSmsIssueSelectRequestDto.builder()
								.content(errorMessage)
								.urUserId("0")
								.mobile(SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE)
								.reserveYn("N")
								.build());
					}
    				throw new BaseException(UserEnums.Join.NEED_LOGIN);
    			}

// ORIGIN referer만 체크하기 위해 comment 처리 S
// custom header 체크시 - 엑셀 다운로드시 문제 발생
//    			// custom header 체크
//    			if (request.getHeader("X-CSRF") == null) { // custom header : X-CSRF - 이름 변경 시, admVerify.html, layout.html, modal.html 같이 수정 필요
//					String errorMessage = "## BOS CSRF ERROR - custom header failed ## : "+ requestURI;
//    				log.error(errorMessage);
//					sendSlackBiz.notify(errorMessage);
//					if (!SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE.equals("0")) {
//						sendTemplateBiz.sendSmsApi(AddSmsIssueSelectRequestDto.builder()
//								.content(errorMessage)
//								.urUserId("0")
//								.mobile(SystemConstants.AUTH_CSRF_SMS_TARGET_MOBILE)
//								.reserveYn("N")
//								.build());
//					}
//    				throw new BaseException(UserEnums.Join.NEED_LOGIN);
//    			}
// ORIGIN referer만 체크하기 위해 comment 처리 E
    		}
        }
		// CSRF 처리 E

		UserVo userVo     = SessionUtil.getBosUserVO();
		if(userVo == null) {
			log.info("===================================셔센끊김==================================="+ requestURI);
			if( requestURI.indexOf("/admin/login/hasLoginData") > -1 ) {
				isSuccess = true;
			} else {
				throw new BaseException(UserEnums.Join.NEED_LOGIN);
				//throw new BosCustomException(UserEnums.Join.NEED_LOGIN.getCode(),UserEnums.Join.NEED_LOGIN.getMessage());
			}
		}
		else {
			if ((userVo.getAuthCertiNo() == null || "".equals(userVo.getAuthCertiNo())) && userVo.getAuthCertiFailCount() < 5) { // 2차인증 비활성화 또는 인증완료 시
				isSuccess = true;

				CookieUtil.setCookieSecure(response, "sessionExpireTime", "" + userVo.getSessionExpireTime(), (60 * 60 * 24), true); // 세션 만료시간 설정

				try {
					comnBiz.addMenuAccessLog(request, userVo);
				} catch(Exception e)  {
					log.info("addMenuAccessLog Exception:"+ e);
				}
			}
			else {
				if (
					(requestURI.indexOf("/admin/login/hasLoginData") > -1) // 로그인
					|| (requestURI.indexOf("/admin/login/putPassWordByLogin") > -1) // 비밀번호 변경
					|| (requestURI.indexOf("/admin/login/bosTwoFactorAuthetificationVeriyfy") > -1) // 2차인증
				) {
					isSuccess = true;
				}
				else {
					throw new BaseException(UserEnums.Join.NEED_LOGIN);
				}
			}
		}

		// URL 권한 관련 체크

		if (isSuccess == true && !ServerType.PROD.getCode().equals(profiles)) {
			if (requestURI.indexOf("/admin/login/") == -1 && requestURI.indexOf("/admin/comn/") == -1) {
				if (!stComnBiz.isRoleMenuAuthUrl(userVo.getRoleId(), request.getHeader("authMenuID"), requestURI)) {
					throw new BaseException(BaseEnums.CommBase.URL_AUTH_FAIL);
				}
			}
		}
		return isSuccess;
	}

//	/** Controller 진입 후 view 랜더링 전에 동작**/
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

		log.info("===================================postHandle===================================");

		// 시스템 URL 등록 (이후 삭제 예정)
		String requestURI = request.getRequestURI();
		String authMenuID = request.getHeader("authMenuID");
		if (response.getStatus() == 200 && authMenuID != null && requestURI != null
				&& !"".equals(requestURI)) {
			GetMenuResponseDto menu = systemMenuBiz.getMenu(new GetMenuRequestDto(authMenuID));

			if (menu != null && menu.getRows().getMenuName() != null || true) {

				String[] urlSplitArray = requestURI.split("/");
				String method = urlSplitArray[urlSplitArray.length - 1];
				String inputAuthority = "";

				SystemUrlRequestDto dto = new SystemUrlRequestDto();
				dto.setInputUrl(requestURI);
				dto.setInputUrlName(menu.getRows().getMenuName() + "." + method);
				dto.setInputUrlUsageYn("N");

				switch (method.substring(0, 3)) {
				case "get":
					inputAuthority = "CRUD_TP.R";
					break;
				case "add":
					inputAuthority = "CRUD_TP.C";
					break;
				case "put":
					inputAuthority = "CRUD_TP.U";
					break;
				case "del":
					inputAuthority = "CRUD_TP.D";
					break;
				default:
					if ("save".equals(method.substring(0, 4))) {
						inputAuthority = "CRUD_TP.C";
					} else if ("create".equals(method.substring(0, 6))) {
						inputAuthority = "CRUD_TP.DL";
					} else {
						inputAuthority = "CRUD_TP.UNDEFINED";
					}
					break;
				}
				dto.setInputAuthority(inputAuthority);
				dto.setInputContent("");
				systemMenuBiz.addSystemUrl(dto);

// 프로그램 권한용 임시 소스코드 코멘트 처리 S
//				// --------------------------------------------------------------------
//				// 메뉴프로그램권한URL정보수집 (오류처리 없음)
//				// --------------------------------------------------------------------
//				try{
//					if (requestURI.indexOf("/admin/login/") == -1 && requestURI.indexOf("/admin/comn/") == -1) {
//						dto.setUrl(requestURI);
//						dto.setStPrgramId(menu.getRows().getStProgramId());
//						dto.setMemo("stMenuId:" + menu.getRows().getStMenuId() + ", url:" + requestURI);
//						ApiResult<?> result = systemMenuBiz.addSystemUrlMenuInfo(dto);
//						log.info("# 메뉴프로그램권한URL정보수집 결과 ["+result.getCode()+"]["+result.getMessage()+"] :: ["+dto.getStPrgramId()+"]["+dto.getUrl()+"]");
//					}
//				} catch(Exception e) {
//					log.info("# 메뉴프로그램권한URL정보등록건 오류");
//				}
// 프로그램 권한용 임시 소스코드 코멘트 처리 E

			}
		}
	}

	/** Controller 진입 후 view 랜더링 후에 동작 **/
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exp) throws Exception {

		log.info("===================================afterCompletion===================================");

	}

	/**비동기 요청 시 PostHandle와 afterCompletion메서드를 수행하지 않고 이 메서드를 수행**/
	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {

		log.info("===================================afterConcurrentHandlingStarted===================================");

	}

}
