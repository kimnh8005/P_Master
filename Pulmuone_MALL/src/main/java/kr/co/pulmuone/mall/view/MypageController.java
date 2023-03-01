package kr.co.pulmuone.mall.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.pg.service.inicis.service.InicisConfig;
import kr.co.pulmuone.v1.pg.service.kcp.service.KcpConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MypageController {

	@Autowired
	KcpConfig kcpConfig;

	@Autowired
	InicisConfig inicisConfig;

	@GetMapping(value={"{mall}/mypage", "/mypage"})
	public String mypageClause() throws Exception {
		log.info("###  mypage index");
		return DeviceUtil.getDirInfo() + "/mypage/main/index";
	}

	@GetMapping(value={"{mall}/noMember", "/noMember"})
	public String nomemberClause() throws Exception {
		log.info("###  noMember index");
		return DeviceUtil.getDirInfo() + "/mypage/noMember/index";
	}


	@GetMapping(value={"{mall}/mypage/reserves", "/mypage/reserves"})
	public String reservesClause() throws Exception {
		log.info("###  reserves index");
		return DeviceUtil.getDirInfo() + "/mypage/reserves/index";
	}

	@GetMapping(value={"{mall}/mypage/coupon", "/mypage/coupon"})
	public String couponClause() throws Exception {
		log.info("###  coupon index");
		return DeviceUtil.getDirInfo() + "/mypage/coupon/index";
	}

	@GetMapping(value={"{mall}/mypage/ticket", "/mypage/ticket"})
	public String ticketClause() throws Exception {
		log.info("###  ticket index");
		return DeviceUtil.getDirInfo() + "/mypage/ticket/index";
	}

	@GetMapping(value={"{mall}/mypage/membership", "/mypage/membership"})
	public String membershipClause() throws Exception {
		log.info("###  membership index");
		return DeviceUtil.getDirInfo() + "/mypage/membership/index";
	}

	@GetMapping(value={"{mall}/mypage/review", "/mypage/review"})
	public String reviewClause() throws Exception {
		log.info("###  review index");
		return DeviceUtil.getDirInfo() + "/mypage/review/index";
	}

	@GetMapping(value={"{mall}/mypage/wish", "/mypage/wish"})
	public String wishClause() throws Exception {
		log.info("###  wish index");
		return DeviceUtil.getDirInfo() + "/mypage/wish/index";
	}

	@GetMapping(value={"{mall}/mypage/recent", "/mypage/recent"})
	public String recentClause() throws Exception {
		log.info("###  recent index");
		return DeviceUtil.getDirInfo() + "/mypage/recent/index";
	}

	@GetMapping(value={"{mall}/mypage/profile", "/mypage/profile"})
	public String profileClause() throws Exception {
		log.info("###  profile index");
		return DeviceUtil.getDirInfo() + "/mypage/profile/index";
	}

	@GetMapping(value={"{mall}/mypage/withdrawal", "/mypage/withdrawal"})
	public String withdrawalClause() throws Exception {
		log.info("###  withdrawal index");
		return DeviceUtil.getDirInfo() + "/mypage/withdrawal/index";
	}

	@GetMapping(value={"{mall}/mypage/refund", "/mypage/refund"})
	public String refundClause() throws Exception {
		log.info("###  refund index");
		return DeviceUtil.getDirInfo() + "/mypage/refund/index";
	}

	@GetMapping(value={"{mall}/mypage/qna", "/mypage/qna"})
	public String qnaClause() throws Exception {
		log.info("###  qna index");
		return DeviceUtil.getDirInfo() + "/mypage/qna/index";
	}

	@GetMapping(value={"{mall}/mypage/inquiry", "/mypage/inquiry"})
	public String inquiryClause() throws Exception {
		log.info("###  inquiry index");
		return DeviceUtil.getDirInfo() + "/mypage/inquiry/index";
	}

	@GetMapping(value={"{mall}/mypage/event", "/mypage/event"})
	public String eventClause() throws Exception {
		log.info("###  event index");
		return DeviceUtil.getDirInfo() + "/mypage/event/index";
	}

	@GetMapping(value={"{mall}/mypage/employees", "/mypage/employees"})
	public String employeesClause() throws Exception {
		log.info("###  employees index");
		return DeviceUtil.getDirInfo() + "/mypage/employees/index";
	}

	@GetMapping(value={"{mall}/mypage/shipping", "/mypage/shipping"})
	public String shippingClause() throws Exception {
		log.info("###  shipping index");
		return DeviceUtil.getDirInfo() + "/mypage/shipping/index";
	}

	@GetMapping(value={"{mall}/mypage/setting", "/mypage/setting"})
	public String settingClause() throws Exception {
		log.info("###  setting index");
		return DeviceUtil.getDirInfo() + "/mypage/setting/index";
	}

	@GetMapping(value={"{mall}/mypage/orderHistory", "/mypage/orderHistory"})
	public String orderHistoryClause() throws Exception {
		log.info("###  orderHistory index");
		return DeviceUtil.getDirInfo() + "/mypage/orderHistory/index";
	}

	@GetMapping(value={"{mall}/mypage/orderDetail", "/mypage/orderDetail"})
	public String orderDetailClause() throws Exception {
		log.info("###  orderDetail index");
		return DeviceUtil.getDirInfo() + "/mypage/orderDetail/index";
	}

	// @GetMapping(value={"{mall}/mypage/cancelHistory", "/mypage/cancelHistory"})
	// public String cancelHistoryClause() throws Exception {
	// 	log.info("###  cancelHistory index");
	// 	return DeviceUtil.getDirInfo() + "/mypage/cancelHistory/index";
	// }

	@GetMapping(value={"{mall}/mypage/cancelDetail", "/mypage/cancelDetail"})
	public String cancelDetailClause() throws Exception {
		log.info("###  cancelDetail index");
		return DeviceUtil.getDirInfo() + "/mypage/cancelDetail/index";
	}

//	@GetMapping(value={"{mall}/mypage/cancelInput", "/mypage/cancelInput"})
//	public String cancelInputClause() throws Exception {
//		log.info("###  cancelInput index");
//		return DeviceUtil.getDirInfo() + "/mypage/cancel-input/index";
//	}
	@GetMapping(value={"{mall}/mypage/cancelInput", "/mypage/cancelInput"})
	public String cancelInputClause(Model model) throws Exception {
		log.info("###  cancelInput index");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);

		return DeviceUtil.getDirInfo() + "/mypage/cancel-input/index";
	}

	@GetMapping(value={"{mall}/mypage/cancelOrder", "/mypage/cancelOrder"})
	public String cancelOrderClause(Model model) throws Exception {
		log.info("### order input");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);
		return dir + "/mypage/cancelOrder/index";
	}

	@GetMapping(value={"{mall}/mypage/returnOrder", "/mypage/returnOrder"})
	public String returnOrderClause(Model model) throws Exception {
		log.info("###  returnOrder index");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);
		return dir + "/mypage/returnOrder/index";
	}

	@GetMapping(value={"{mall}/mypage/regularManage", "/mypage/regularManage"})
	public String regularManageClause(Model model) throws Exception {
		log.info("###  regularManage index");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);

		return DeviceUtil.getDirInfo() + "/mypage/regularManage/index";
	}

	@GetMapping(value={"{mall}/mypage/regularCancel", "/mypage/regularCancel"})
	public String regularCancelClause() throws Exception {
		log.info("###  regularCancel index");
		return DeviceUtil.getDirInfo() + "/mypage/regularCancel/index";
	}

	@GetMapping(value={"{mall}/mypage/regularSkip", "/mypage/regularSkip"})
	public String regularSkipClause() throws Exception {
		log.info("###  regularSkip index");
		return DeviceUtil.getDirInfo() + "/mypage/regularSkip/index";
	}

	@GetMapping(value={"{mall}/mypage/addPayment", "/mypage/addPayment"})
	public String addPaymentClause() throws Exception {
		log.info("###  addPayment index");
		return DeviceUtil.getDirInfo() + "/mypage/addPayment/index";
	}

	@GetMapping(value={"{mall}/mypage/deviceUpdate", "/mypage/deviceUpdate"})
	public String deviceUpdateClause() throws Exception {
		log.info("###  deviceUpdate index");
		return DeviceUtil.getDirInfo() + "/mypage/deviceUpdate/index";
	}

	@GetMapping(value={"{mall}/mypage/dailyManage", "/mypage/dailyManage"})
	public String dailyManageClause() throws Exception {
		log.info("###  dailyManage index");
		return DeviceUtil.getDirInfo() + "/mypage/dailyManage/index";
	}

	@GetMapping(value={"{mall}/mypage/orderInput", "/mypage/orderInput"})
	public String orderDirectInputClause(Model model) throws Exception {
		log.info("### order direct input");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);
		return dir + "/mypage/order-input/index";
	}

	@GetMapping(value={"{mall}/order/claim/claimCompleted", "/order/claim/claimCompleted"})
	public String claimCompletedClause() throws Exception {
		log.info("### mypage claim completed");
		return DeviceUtil.getDirInfo() + "/order/claim/claim-completed/index";
	}

	@GetMapping(value={"{mall}/mypage/compensation", "/mypage/compensation"})
	public String mypageCompensationClause() throws Exception {
		log.info("### mypage compensation");
		return DeviceUtil.getDirInfo() + "/mypage/compensation/index";
	}
}