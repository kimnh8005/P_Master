package kr.co.pulmuone.mall.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class GuideController {
	@GetMapping(value={"{mall}/guide", "/guide"})
	public String guide() throws Exception {
		log.info("###guide ");
		return "pc/guide/index";
	}

	@GetMapping(value={"{mall}/guide/layout", "/guide/layout"})
	public String guideLayout() throws Exception {
		log.info("###guide/layout ");
		return DeviceUtil.getDirInfo() + "/guide/layout/index";
	}

	@GetMapping(value={"{mall}/guide/button", "/guide/button"})
	public String guideButton() throws Exception {
		log.info("###guide/button ");
		return DeviceUtil.getDirInfo() + "/guide/button/index";
	}

	@GetMapping(value={"{mall}/guide/format", "/guide/format"})
	public String guideFormat() throws Exception {
		log.info("###guide/format ");
		return DeviceUtil.getDirInfo() + "/guide/format/index";
	}
}
