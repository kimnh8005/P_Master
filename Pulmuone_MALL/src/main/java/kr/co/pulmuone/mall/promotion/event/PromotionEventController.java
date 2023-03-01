package kr.co.pulmuone.mall.promotion.event;

import io.swagger.annotations.*;
import kr.co.pulmuone.mall.promotion.event.service.PromotionEventMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.EventGroupByUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PromotionEventController {
    @Autowired
    public PromotionEventMallService promotionEventMallService;

    @PostMapping(value = "/promotion/event/getEventListByUser")
    @ApiOperation(value = "이벤트 목록 조회", httpMethod = "POST", notes = "이벤트 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventListByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getEventListByUser(EventListByUserRequestDto dto) throws Exception {
        return promotionEventMallService.getEventListByUser(dto);
    }

    @PostMapping(value = "/promotion/event/getNormalByUser")
    @ApiOperation(value = "일반이벤트 조회", httpMethod = "POST", notes = "일반이벤트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NormalByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getNormalByUser(HttpServletRequest request, Long evEventId) throws Exception {
        return promotionEventMallService.getNormalByUser(request, evEventId);
    }

    @PostMapping(value = "/promotion/event/addNormalJoin")
    @ApiOperation(value = "일반이벤트 댓글 저장", httpMethod = "POST", notes = "일반이벤트 댓글 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = NormalJoinResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" +
                    "[ALREADY_JOIN_DATE] ALREADY_JOIN_DATE - 1일1회 중복 참여 \n" +
                    "[ALREADY_JOIN_EVENT] ALREADY_JOIN_EVENT - 1이벤트1회 중복 참여 \n" +
                    "[EMPTY_COMMENT_VALUE] EMPTY_COMMENT_VALUE - 댓글 구분값 없음 \n" +
                    "[NOT_DATE] NOT_DATE - 이벤트 기간 아님 \n" +
                    "[NOT_DEVICE] NOT_DEVICE - 디바이스 다름 \n" +
                    "[NOT_EMPLOYEE] NOT_EMPLOYEE - 임직원 참여 불가 \n" +
                    "[NOT_GROUP] NOT_GROUP - 등급 다름 \n" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> addNormalJoin(NormalJoinRequestDto dto) throws Exception {
        return promotionEventMallService.addNormalJoin(dto);
    }

    @PostMapping(value = "/promotion/event/getNormalJoinListByUser")
    @ApiOperation(value = "이벤트 댓글 목록 조회", httpMethod = "POST", notes = "이벤트 댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventJoinByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getNormalJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return promotionEventMallService.getNormalJoinByUser(dto);
    }

    @PostMapping(value = "/promotion/event/getStampByUser")
    @ApiOperation(value = "스탬프 이벤트 조회", httpMethod = "POST", notes = "스탬프 이벤트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "evEventId", value = "이벤트 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StampByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getStampByUser(HttpServletRequest request, Long evEventId) throws Exception {
        return promotionEventMallService.getStampByUser(request, evEventId);
    }

    @PostMapping(value = "/promotion/event/addStampJoin")
    @ApiOperation(value = "스탬프 이벤트 참여 저장", httpMethod = "POST", notes = "스탬프 이벤트 참여 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = StampJoinResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" +
                    "[ALREADY_JOIN_DATE] ALREADY_JOIN_DATE - 1일1회 중복 참여 \n" +
                    "[ALREADY_JOIN_EVENT] ALREADY_JOIN_EVENT - 1이벤트1회 중복 참여 \n" +
                    "[NOT_DATE] NOT_DATE - 이벤트 기간 아님 \n" +
                    "[NOT_DEVICE] NOT_DEVICE - 디바이스 다름 \n" +
                    "[NOT_EMPLOYEE] NOT_EMPLOYEE - 임직원 참여 불가 \n" +
                    "[NOT_GROUP] NOT_GROUP - 등급 다름 \n" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> addStampJoin(StampJoinRequestDto dto) throws Exception {
        return promotionEventMallService.addStampJoin(dto);
    }

    @PostMapping(value = "/promotion/event/getRouletteByUser")
    @ApiOperation(value = "룰렛 이벤트 조회", httpMethod = "POST", notes = "룰렛 이벤트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RouletteByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getRouletteByUser(HttpServletRequest request, Long evEventId) throws Exception {
        return promotionEventMallService.getRouletteByUser(request, evEventId);
    }

    @PostMapping(value = "/promotion/event/addRouletteJoin")
    @ApiOperation(value = "룰렛 이벤트 참여 저장", httpMethod = "POST", notes = "룰렛 이벤트 참여 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RouletteJoinResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" +
                    "[ALREADY_JOIN_DATE] ALREADY_JOIN_DATE - 1일1회 중복 참여 \n" +
                    "[ALREADY_JOIN_EVENT] ALREADY_JOIN_EVENT - 1이벤트1회 중복 참여 \n" +
                    "[NOT_DATE] NOT_DATE - 이벤트 기간 아님 \n" +
                    "[NOT_DEVICE] NOT_DEVICE - 디바이스 다름 \n" +
                    "[NOT_EMPLOYEE] NOT_EMPLOYEE - 임직원 참여 불가 \n" +
                    "[NOT_GROUP] NOT_GROUP - 등급 다름 \n" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> addRouletteJoin(RouletteJoinRequestDto dto) throws Exception {
        return promotionEventMallService.addRouletteJoin(dto);
    }

    @PostMapping(value = "/promotion/event/getSurveyByUser")
    @ApiOperation(value = "설문 이벤트 조회", httpMethod = "POST", notes = "설문 이벤트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SurveyByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getSurveyByUser(HttpServletRequest request, Long evEventId) throws Exception {
        return promotionEventMallService.getSurveyByUser(request, evEventId);
    }

    @PostMapping(value = "/promotion/event/addSurveyJoin")
    @ApiOperation(value = "설문 이벤트 참여 저장", httpMethod = "POST", notes = "설문 이벤트 참여 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = SurveyJoinResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" +
                    "[ALREADY_JOIN_DATE] ALREADY_JOIN_DATE - 1일1회 중복 참여 \n" +
                    "[ALREADY_JOIN_EVENT] ALREADY_JOIN_EVENT - 1이벤트1회 중복 참여 \n" +
                    "[NOT_DATE] NOT_DATE - 이벤트 기간 아님 \n" +
                    "[NOT_DEVICE] NOT_DEVICE - 디바이스 다름 \n" +
                    "[NOT_EMPLOYEE] NOT_EMPLOYEE - 임직원 참여 불가 \n" +
                    "[NOT_GROUP] NOT_GROUP - 등급 다름 \n" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> addSurveyJoin(SurveyJoinRequestDto dto) throws Exception {
        return promotionEventMallService.addSurveyJoin(dto);
    }

    @PostMapping(value = "/promotion/event/getExperienceByUser")
    @ApiOperation(value = "체험단 이벤트 조회", httpMethod = "POST", notes = "체험단 이벤트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = ExperienceByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NOT_GROUP_NONE] NOT_GROUP_NONE - 비회원 \n" +
                    "[NOT_GROUP_NORMAL] NOT_GROUP_NORMAL - 일반 \n" +
                    "[NOT_GROUP_PREMIUM] NOT_GROUP_PREMIUM - 프리미엄"
            )
    })
    public ApiResult<?> getExperienceByUser(HttpServletRequest request, Long evEventId) throws Exception {
        return promotionEventMallService.getExperienceByUser(request, evEventId);
    }

    @PostMapping(value = "/promotion/event/getExperienceJoinListByUser")
    @ApiOperation(value = "체험단 댓글 목록 조회", httpMethod = "POST", notes = "체험단 댓글 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventJoinByUserResponseDto.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getExperienceJoinByUser(EventJoinByUserRequestDto dto) throws Exception {
        return promotionEventMallService.getExperienceJoinByUser(dto);
    }

    @PostMapping(value = "/promotion/event/addExperienceJoin")
    @ApiOperation(value = "체험단 이벤트 참여 저장", httpMethod = "POST", notes = "체험단 이벤트 참여 저장")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventJoinByUserResponseDto.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인 필요 \n" +
                    "[ALREADY_JOIN_DATE] ALREADY_JOIN_DATE - 1일1회 중복 참여 \n" +
                    "[ALREADY_JOIN_EVENT] ALREADY_JOIN_EVENT - 1이벤트1회 중복 참여 \n" +
                    "[NOT_DATE] NOT_DATE - 이벤트 기간 아님 \n" +
                    "[NOT_DEVICE] NOT_DEVICE - 디바이스 다름 \n" +
                    "[NOT_EMPLOYEE] NOT_EMPLOYEE - 임직원 참여 불가 \n" +
                    "[NOT_GROUP] NOT_GROUP - 등급 다름 \n" +
                    "[NO_EVENT] NO_EVENT - 정상적인 이벤트 아님 \n" +
                    "[ONLY_EMPLOYEE] ONLY_EMPLOYEE - 임직원 전용이벤트 \n" +
                    "[FIRST_COME_CLOSE] FIRST_COME_CLOSE - 선착순 종료 \n" +
                    "[PASS_VALIDATION] PASS_VALIDATION - 통과"
            )
    })
    public ApiResult<?> addExperienceJoin(ExperienceJoinRequestDto dto) throws Exception {
        return promotionEventMallService.addExperienceJoin(dto);
    }

    @GetMapping(value = "/promotion/event/getGroupList/{evEventId}/{deviceType}/{userType}")
    @ApiOperation(value = "이벤트 그룹 정보 조회", httpMethod = "GET", notes = "이벤트 그룹 정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "evEventId", value = "이벤트 코드", required = true, dataType = "String")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = EventGroupByUserVo.class),
            @ApiResponse(code = 901, message = "")
    })
    public ApiResult<?> getGroupList(@PathVariable(value = "evEventId") Long evEventId, @PathVariable(value = "deviceType") String deviceType, @PathVariable(value = "userType") String userType) throws Exception {
        return promotionEventMallService.getGroupList(evEventId, deviceType, userType);
    }

}
