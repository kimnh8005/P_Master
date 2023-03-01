package kr.co.pulmuone.mall.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MainController {

	@GetMapping(value={"{mall}/", "/"})
	public String main() throws Exception {
		log.info("### main index");
		return DeviceUtil.getDirInfo() + "/main/index";
	}

	@GetMapping(value={"{mall}/brand", "/brand"})
	public String brandClause() throws Exception {
		log.info("### brand index");
		return DeviceUtil.getDirInfo() + "/main/brand/index";
	}

	@GetMapping(value={"{mall}/newItem", "/newItem"})
	public String newItemClause() throws Exception {
		log.info("### newItem index");
		return DeviceUtil.getDirInfo() + "/main/new-item/index";
	}

	@GetMapping(value={"{mall}/bestItem", "/bestItem"})
	public String bestItemClause() throws Exception {
		log.info("### bestItem index");
		return DeviceUtil.getDirInfo() + "/main/best-item/index";
	}

	@GetMapping(value={"{mall}/keywordItem", "/keywordItem"})
	public String keywordItemClause() throws Exception {
		log.info("### keywordItem index");
		return DeviceUtil.getDirInfo() + "/main/keyword-item/index";
	}
	@GetMapping(value={"{mall}/shippingGuide", "/shippingGuide"})
	public String shippingGuideClause() throws Exception {
		log.info("### brand index");
		return DeviceUtil.getDirInfo() + "/main/shipping-guide/index";
	}

	@GetMapping(value={"{mall}/dailyShipping", "/dailyShipping"})
	public String dailyShippingClause() throws Exception {
		log.info("### dailyShipping index");
		return DeviceUtil.getDirInfo() + "/main/daily-shipping/index";
	}

	@GetMapping(value={"{mall}/newMemberBenifit", "/newMemberBenifit"})
	public String newMemberBenifitClause() throws Exception {
		log.info("### brand index");
		return DeviceUtil.getDirInfo() + "/main/new-member-benifit/index";
	}

	@GetMapping(value={"{mall}/lohas", "/lohas"})
	public String lohasClause() throws Exception {
		log.info("### lohas index");
		return DeviceUtil.getDirInfo() + "/main/lohas/index";
	}

	@GetMapping(value={"{mall}/lohasAll", "/lohasAll"})
	public String lohasAllClause() throws Exception {
		log.info("### lohasAll index");
		return DeviceUtil.getDirInfo() + "/main/lohas-all/index";
	}

	@GetMapping(value={"{mall}/nowSale", "/nowSale"})
	public String nowSaleClause() throws Exception {
		log.info("### nowSale index");
		return DeviceUtil.getDirInfo() + "/main/now-sale/index";
	}

	@GetMapping(value={"{mall}/inspectNotice", "/inspectNotice"})
	public String inspectNoticeClause() throws Exception {
		log.info("### inspectNotice index");
		return "any/maintenance/index";
	}

	// 8층 라운지 전시용 자사 제품 홍보 페이지
	@GetMapping(value={"{mall}/brand/display", "/brand/display"})
	public String brandDisplayClause() throws Exception {
		log.info("### brandDisplay index");
		return "pc/main/brand-display/index";
	}
	@GetMapping(value={"{mall}/brand/display/goodsView", "/brand/display/goodsView"})
	public String brandDisplayGoodsViewClause() throws Exception {
		log.info("###  brandDisplay goodsView");
		return "pc/main/brand-display/goods-view/index";
	}
}
