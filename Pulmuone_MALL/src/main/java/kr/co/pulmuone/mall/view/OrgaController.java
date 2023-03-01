package kr.co.pulmuone.mall.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class OrgaController {

	@GetMapping("/orga")
	public String orgaMain() throws Exception {
		log.info("### orgaMain index");
		return DeviceUtil.getDirInfo() + "/orga/main/index";
	}

	@GetMapping("/orga/newItem")
	public String newItemClause() throws Exception {
		log.info("### orga newItem index");
		return DeviceUtil.getDirInfo() + "/orga/new-item/index";
	}

	@GetMapping("/orga/bestItem")
	public String bestItemClause() throws Exception {
		log.info("### orga bestItem index");
		return DeviceUtil.getDirInfo() + "/orga/best-item/index";
	}

	@GetMapping("/orga/flyerItem")
	public String flyerItemClause() throws Exception {
		log.info("### orga flyerItem index");
		return DeviceUtil.getDirInfo() + "/orga/flyer-item/index";
	}

	@GetMapping("/orga/localItem")
	public String localItemClause() throws Exception {
		log.info("### orga localItem index");
		return DeviceUtil.getDirInfo() + "/orga/local-item/index";
	}

	@GetMapping("/orga/pbItem")
	public String pbItemClause() throws Exception {
		log.info("### orga pbItem index");
		return DeviceUtil.getDirInfo() + "/orga/pb-item/index";
	}

	@GetMapping("/orga/shopDelivery")
	public String shopDeliveryClause() throws Exception {
		log.info("### orga shopDelivery index");
		return DeviceUtil.getDirInfo() + "/orga/shop-delivery/index";
	}
}
