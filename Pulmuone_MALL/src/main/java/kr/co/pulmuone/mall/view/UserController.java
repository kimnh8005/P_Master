package kr.co.pulmuone.mall.view;

import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.welcomeLogin.WelcomeCertificationBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class UserController {

	@Autowired
	private WelcomeCertificationBiz welcomeCertificationBiz;

	@Autowired
	private UserCertificationBiz userCertificationBiz;

	@GetMapping(value={"{mall}/user/joinInput", "/user/joinInput"})
	public String joinInput(HttpServletRequest request) throws Exception {
		log.info("### user join input");

		String eid = request.getParameter("eid");

		//as-is회원 정보 세션에서 초기화
		if(StringUtil.isEmpty(eid)) {
			BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
			buyerVo.setAsisCustomerNumber("");
			buyerVo.setAsisLoginId("");
			SessionUtil.setUserVO(buyerVo);
		}

		return "any/user/join-input/index";
	}

	@GetMapping(value={"{mall}/user/login", "/user/login"})
	public String login() throws Exception {
		log.info("### user login");
		return "any/user/login/index";
	}

	@GetMapping(value={"{mall}/user/login/{employeeId}", "/user/login/{employeeId}"})
	public ModelAndView login(@PathVariable(value = "employeeId") String employeeId, HttpServletResponse response) throws Exception {

		log.info("### user login For as-is");

		ModelAndView mav = new ModelAndView();
		RedirectView redirectView = new RedirectView();

		// 임직원 사번일경우 만 회원 인증 토큰을 생성한다.
		if(employeeId.length() == 5 || employeeId.length() == 6) {
			welcomeCertificationBiz.publishWelcomeToeken(response, employeeId);
		}

		// 임직원 회원 가입여부 확인
		boolean isBuyerEmp = userCertificationBiz.isUrBuyerEmployeesExist(employeeId);

		if(!isBuyerEmp) {
			// 가입되지 않았다면 가입페이지로 이동
			redirectView.setUrl("/user/joinInput");
		} else {
			// 가입되어있으면 로그인 페이지로 이동
			redirectView.setUrl("/user/login");
		}

		redirectView.setExposeModelAttributes(false);
		mav.setView(redirectView);

		return mav;
	}

	@GetMapping(value={"{mall}/user/find", "/user/find"})
	public String find() throws Exception {
		log.info("### user find");
		return "any/user/find/index";
	}

	@GetMapping(value={"{mall}/user/employeeAuth", "/user/employeeAuth"})
	public String employeeAuth() throws Exception {
		log.info("### user employee auth");
		return "any/user/employee-auth/index";
	}

	@GetMapping(value={"{mall}/clause", "/clause"})
	public String clause() throws Exception {
		log.info("### clause");
		return "any/clause/index";
	}

	@GetMapping(value={"{mall}/user/reserves", "/user/reserves"})
	public String userReserves() throws Exception {
		log.info("### user reserves");
		return "any/user/reserves/index";
	}
	
	// 임직원오픈 접근제한
	@GetMapping(value={"{mall}/welcomeLogin", "/welcomeLogin"})
	public String welcomeLogin() throws Exception {
		log.info("### welcomeLogin");
		return "any/welcome-login/index";
	}

	// 임직원오픈 접근제한 인증
	@GetMapping(value={"{mall}/welcomeLogin/employeeAuth", "/welcomeLogin/employeeAuth"})
	public String welcomeLoginEmployeeAuth() throws Exception {
		log.info("###  welcomeLogin - employeeAuth");
		return "any/user/employee-auth/index";
	}
	
	// 메인 접근 게이트페이지
	@GetMapping(value={"{mall}/mainGate", "/mainGate"})
	public String mainGate() throws Exception {
		log.info("### mainGate");
		return "any/main-gate/index";
	}

	// 풀무원스토리
	@GetMapping(value={"{mall}/story", "/story"})
	public String pulmuoneStory() throws Exception {
		log.info("###  pulmuoneStory");
		return "any/story/index";
	}

	// 회원 인증 이벤트
	@GetMapping(value={"{mall}/user/preJoin", "/user/preJoin"})
	public String preJoin(HttpServletRequest request) throws Exception {
		log.info("### user pre join");

		String eid = request.getParameter("eid");

		//as-is회원 정보 세션에서 초기화
		if(StringUtil.isEmpty(eid)) {
			BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
			buyerVo.setAsisCustomerNumber("");
			buyerVo.setAsisLoginId("");
			SessionUtil.setUserVO(buyerVo);
		}

		return "any/user/preJoin/index";
	}

	// 임직원 가족 hipass URL
	@GetMapping(value={"{mall}/SHOPCBT", "/SHOPCBT", "{mall}/shopcbt", "/shopcbt"})
	public ModelAndView shopCbtPassUrl(HttpServletResponse response) throws Exception {
		log.info("### user hipass");

		ModelAndView mav = new ModelAndView();
		RedirectView redirectView = new RedirectView();

		welcomeCertificationBiz.publishWelcomeToeken(response, "000000");	// hipass 쿠키생성용 temp 사번
		redirectView.setUrl("/");
		redirectView.setExposeModelAttributes(false);
		mav.setView(redirectView);

		return mav;
	}
}