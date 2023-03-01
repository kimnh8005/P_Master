package kr.co.pulmuone.mall.view;

import kr.co.pulmuone.mall.view.service.EventsService;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
public class EventsController {

	@Autowired
	private EventsService eventsService;

	/*
	*	이벤트 리스트
	*/
	@GetMapping(value={"{mall}/events", "/events"})
	public String eventsClause() throws Exception {
		log.info("###  events");
		return DeviceUtil.getDirInfo() + "/events/index";
	}

	/*
	*	이벤트 상세
	*/

	//룰렛이벤트
	@GetMapping(value={"{mall}/events/roulette", "/events/roulette"})
	public String rouletteClause(HttpServletRequest request) throws Exception {
		log.info("###  events - roulette");
		Long reEventId = eventsService.getReDirectEventId(request);
		if(reEventId != null)
			return "redirect:" + request.getServletPath() + "?event=" + reEventId;
		return DeviceUtil.getDirInfo() + "/events/roulette/index";
	}

	// 댓글이벤트
	@GetMapping(value={"{mall}/events/reply", "/events/reply"})
	public String replyClause(HttpServletRequest request) throws Exception {
		log.info("###  events - reply");
		Long reEventId = eventsService.getReDirectEventId(request);
		if(reEventId != null)
			return "redirect:" + request.getServletPath() + "?event=" + reEventId;
		return DeviceUtil.getDirInfo() + "/events/reply/index";
	}

	// 스탬프이벤트
	@GetMapping(value={"{mall}/events/stamp", "/events/stamp"})
	public String stampClause(HttpServletRequest request) throws Exception {
		log.info("###  events - stamp");
		Long reEventId = eventsService.getReDirectEventId(request);
		if(reEventId != null)
			return "redirect:" + request.getServletPath() + "?event=" + reEventId;
		return DeviceUtil.getDirInfo() + "/events/stamp/index";
	}

	// 설문조사 이벤트
	@GetMapping(value={"{mall}/events/survey", "/events/survey"})
	public String surveyClause(HttpServletRequest request) throws Exception {
		log.info("###  events - survey");
		Long reEventId = eventsService.getReDirectEventId(request);
		if(reEventId != null)
			return "redirect:" + request.getServletPath() + "?event=" + reEventId;
		return DeviceUtil.getDirInfo() + "/events/survey/index";
	}

	// 체험단 이벤트
	@GetMapping(value={"{mall}/events/experience", "/events/experience"})
	public String experienceClause(HttpServletRequest request) throws Exception {
		log.info("###  events - experience");
		Long reEventId = eventsService.getReDirectEventId(request);
		if(reEventId != null)
			return "redirect:" + request.getServletPath() + "?event=" + reEventId;
		return DeviceUtil.getDirInfo() + "/events/experience/index";
	}

	/*
	*	기획전
	*/

	//일반/증정 기획전
	@GetMapping(value={"{mall}/events/promotion", "/events/promotion"})
	public String promotionClause() throws Exception {
		log.info("###  promotion index");
		return DeviceUtil.getDirInfo() + "/events/promotion/index";
	}


	//균일가 골라담기 리스트
	@GetMapping(value={"{mall}/pickingOut", "/pickingOut"})
	public String pickingOutClause() throws Exception {
		log.info("###  pickingOut index");
		return DeviceUtil.getDirInfo() + "/pickingOut/list/index";
	}

	//균일가 골라담기 상세
	@GetMapping(value={"{mall}/pickingOutView", "/pickingOutView"})
	public String pickingOutViewClause() throws Exception {
		log.info("###  pickingOutView index");
		return DeviceUtil.getDirInfo() + "/pickingOut/view/index";
	}

	// 녹즙 내맘대로 주문하기
	@GetMapping(value={"{mall}/freeOrder", "/freeOrder"})
	public String freeOrderClause() throws Exception {
		log.info("###  freeOrder index");
		return DeviceUtil.getDirInfo() + "/freeOrder/index";
	}

	// 적립금 전환 서비스
	@GetMapping(value={"{mall}/events/convertReserves", "/events/convertReserves"})
	public String convertReserves() throws Exception {
		log.info("###  events - convertReserves");
		return DeviceUtil.getDirInfo() + "/events/convertReserves/index";
	}

	// 샵라이브 방송 페이지 호출
	@GetMapping(value={"{mall}/shopLive", "/shopLive"})
	public String shopLiveClause() throws Exception {
		log.info("###  shopLive");
		return "any/shoplive/index";
	}
}
