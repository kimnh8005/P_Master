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
public class ShopController {

	@Autowired
	KcpConfig kcpConfig;

	@Autowired
	InicisConfig inicisConfig;

	@GetMapping(value={"{mall}/shop/search", "/shop/search"})
	public String searchClause() throws Exception {
		log.info("###  search");
		return DeviceUtil.getDirInfo() + "/shop/search/index";
	}

	@GetMapping(value={"{mall}/orga/search", "/orga/search"})
	public String orgaSearchClause() throws Exception {
		log.info("###  orga search");
		return DeviceUtil.getDirInfo() + "/shop/search/index";
	}

	@GetMapping(value={"{mall}/shop/goodsList", "/shop/goodsList"})
	public String goodsListClause() throws Exception {
		log.info("###  goodsList");
		return DeviceUtil.getDirInfo() + "/shop/goodsList/index";
	}

	@GetMapping(value={"{mall}/shop/goodsView", "/shop/goodsView"})
	public String goodsViewClause() throws Exception {
		log.info("###  goodsView");
		return DeviceUtil.getDirInfo() + "/shop/goods-view/index";
	}

	@GetMapping(value={"{mall}/shop/modals/diet", "/shop/modals/diet"})
	public String dietClause() throws Exception {
		log.info("### diet modal");
		return DeviceUtil.getDirInfo() + "/shop/goods-view/modals/diet/index";
	}

	@GetMapping(value={"{mall}/shop/itemView", "/shop/itemView"})
	public String itemsViewClause() throws Exception {
		log.info("###  itemsView");

		return DeviceUtil.getDirInfo() + "/shop/goods-view/index";
	}

	@GetMapping(value={"{mall}/shop/orderInput", "/shop/orderInput"})
	public String orderInputClause(Model model) throws Exception {
		log.info("### order input");

		String dir = DeviceUtil.getDirInfo();
		String pgScript = "";
		if ("pc".equals(dir)) {
			pgScript = "<script type=\"text/javascript\" src=\"" + kcpConfig.getG_conf_js_url() + "\"></script>";
			pgScript += "<script type=\"text/javascript\" src=\"" + inicisConfig.getPayScriptUrl() + "\"  charset=\"UTF-8\"></script>";
		}
		model.addAttribute("pgScript", pgScript);
		return dir + "/shop/order-input/index";
	}

	@GetMapping(value={"{mall}/shop/orderCompleted", "/shop/orderCompleted"})
	public String orderCompletedClause() throws Exception {
		log.info("### order completed");

		return DeviceUtil.getDirInfo() + "/shop/order-completed/index";
	}

	@GetMapping(value={"{mall}/shop/cart", "/shop/cart"})
	public String cartClause() throws Exception {
		log.info("###  cart");
		return DeviceUtil.getDirInfo() + "/shop/cart/index";
	}

	@GetMapping(value={"{mall}/shop/bridge", "/shop/bridge"})
	public String bridgeClause() throws Exception {
		log.info("###  bridge");
		return DeviceUtil.getDirInfo() + "/shop/bridge/index";
	}

	@GetMapping(value={"{mall}/shop/receivePresent", "/shop/receivePresent"})
	public String receivePresentClause() throws Exception {
		log.info("### receivePresent");
		return DeviceUtil.getDirInfo() + "/shop/receive-gift/index";
	}

	@GetMapping(value={"/shop/externalMallPreview"})
	public String externalMallPreviewClause() throws Exception {
		log.info("### external-mall-preview");
		return "pc/shop/external-mall-preview/index";
	}

}
