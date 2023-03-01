package kr.co.pulmuone.mall.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomerController {

	/**
	 * 고객센터 메인
	 */
	@GetMapping(value={"{mall}/customer", "/customer"})
	public String customerClause() throws Exception {
		log.info("###  events");
		return DeviceUtil.getDirInfo() + "/customer/index";
	}

	/**
	 * faq
	 */
	@GetMapping(value={"{mall}/customer/faq", "/customer/faq"})
	public String faqClause() throws Exception {
		log.info("### faq");
		return DeviceUtil.getDirInfo() + "/customer/faq/index";
	}

	/**
	 * 공지사항
	 */
	@GetMapping(value={"{mall}/customer/notices", "/customer/notices"})
	public String noticesClause() throws Exception {
		log.info("### notices");
		return DeviceUtil.getDirInfo() + "/customer/notices/index";
	}

	/**
	 * 공지사항
	 */
	@GetMapping(value={"{mall}/customer/notices/view", "/customer/notices/view"})
	public String noticeClause() throws Exception {
		log.info("### notice");
		return DeviceUtil.getDirInfo() + "/customer/notices/view/index";
	}

	/**
	 * 1:1 문의
	 */
	@GetMapping(value={"{mall}/customer/qna", "/customer/qna"})
	public String qnaClause() throws Exception {
		log.info("### qna");
		return DeviceUtil.getDirInfo() + "/customer/qna/index";
	}

	/**
	 * 상품입점상담안내
	 */
	@GetMapping(value={"{mall}/customer/consult", "/customer/consult"})
	public String consultClause() throws Exception {
		log.info("### consult");
		return DeviceUtil.getDirInfo() + "/customer/consult/index";
	}

	/**
	 * 상품입점상담안내
	 */
	@GetMapping(value={"{mall}/customer/consult/apply", "/customer/consult/apply"})
	public String consultApplyClause() throws Exception {
		log.info("### consultApply");
		return DeviceUtil.getDirInfo() + "/customer/consult/apply/index";
	}

	/**
	 * 고객보상제
	 */
	@GetMapping(value={"{mall}/customer/compensation", "/customer/compensation"})
	public String compensationClause() throws Exception {
		log.info("### compensation");
		return DeviceUtil.getDirInfo() + "/customer/compensation/index";
	}

	/**
	 * 고객보상제 등록/수정
	 */
	@GetMapping(value={"{mall}/customer/compensation/apply", "/customer/compensation/apply"})
	public String compensationApplyClause() throws Exception {
		log.info("### compensation apply");
		return DeviceUtil.getDirInfo() + "/customer/compensation/apply/index";
	}
}
