package kr.co.pulmuone.mall.customer.reward;

import io.swagger.annotations.*;
import kr.co.pulmuone.mall.customer.reward.service.CustomerRewardMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.reward.dto.*;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyInfoVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardPageInfoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CustomerRewardController {

    @Autowired
    private CustomerRewardMallService customerRewardMallService;

    @GetMapping(value = "/customer/reward/getRewardPageInfo")
    @ApiOperation(value = "고객보상제 페이지 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardPageInfoVo.class)
    })
    public ApiResult<?> getRewardPageInfo() throws Exception {
        return customerRewardMallService.getRewardPageInfo();
    }

    @PostMapping(value = "/customer/reward/getRewardGoods")
    @ApiOperation(value = "고객보상제 상품 리스트 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardGoodsResponseDto.class)
    })
    public ApiResult<?> getRewardGoods(RewardGoodsRequestDto dto) throws Exception {
        return customerRewardMallService.getRewardGoods(dto);
    }

    @PostMapping(value = "/customer/reward/getRewardInfo")
    @ApiOperation(value = "고객보상제 상품 리스트 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardId", value = "고객보상제 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardInfoResponseDto.class)
    })
    public ApiResult<?> getRewardInfo(Long csRewardId) throws Exception {
        return customerRewardMallService.getRewardInfo(csRewardId);
    }

    @PostMapping(value = "/customer/reward/addRewardApply")
    @ApiOperation(value = "고객보상제 등록")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> addRewardApply(RewardRequestDto dto) {
        return customerRewardMallService.addRewardApply(dto);
    }

    @PostMapping(value = "/customer/reward/getRewardApplyInfo")
    @ApiOperation(value = "고객보상제 신청내역 정보 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardApplyInfoVo.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getRewardApplyInfo(RewardApplyRequestDto dto) {
        return customerRewardMallService.getRewardApplyInfo(dto);
    }

    @PostMapping(value = "/customer/reward/getRewardApplyList")
    @ApiOperation(value = "고객보상제 신청내역 list 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardApplyResponseDto.class),
            @ApiResponse(code = 901, message = "" + "NEED_LOGIN - 로그인필요")})
    public ApiResult<?> getRewardApplyList(RewardApplyRequestDto dto) throws Exception {
        return customerRewardMallService.getRewardApplyList(dto);
    }

    @PostMapping(value = "/customer/reward/getRewardApply")
    @ApiOperation(value = "고객보상제 신청내역 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardApplyId", value = "고객보상제 신청 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = RewardApplyVo.class),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[PROCESS_FAIL] PROCESS_FAIL - 처리불가 \n" +
                    "[NOT_USER] NOT_USER - 동일유저아님"
            )
    })
    public ApiResult<?> getRewardApply(Long csRewardApplyId) {
        return customerRewardMallService.getRewardApply(csRewardApplyId);
    }

    @PostMapping(value = "/customer/reward/delRewardApply")
    @ApiOperation(value = "고객보상제 신청철회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardApplyId", value = "고객보상제 신청 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[APPLY_CONFIRM] APPLY_CONFIRM - 관리자 확인중 \n" +
                    "[APPLY_DONE] APPLY_DONE - 관리자 처리 완료 \n" +
                    "[NOT_USER] NOT_USER - 동일유저아님"
            )
    })
    public ApiResult<?> delRewardApply(Long csRewardApplyId) throws Exception {
        return customerRewardMallService.delRewardApply(csRewardApplyId);
    }

    @PostMapping(value = "/customer/reward/putRewardApplyDelYn")
    @ApiOperation(value = "고객보상제 신청내역 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardApplyId", value = "고객보상제 신청 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요 \n" +
                    "[APPLY_CONFIRM] APPLY_CONFIRM - 관리자 확인중 \n" +
                    "[NOT_USER] NOT_USER - 동일유저아님"
            )
    })
    public ApiResult<?> putRewardApplyDelYn(Long csRewardApplyId) throws Exception {
        return customerRewardMallService.putRewardApplyDelYn(csRewardApplyId);
    }

    @PostMapping(value = "/customer/reward/getRewardOrderInfo")
    @ApiOperation(value = "고객보상제 주문정보 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardId", value = "고객보상제 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> getRewardOrderInfo(Long csRewardId) throws Exception {
        return customerRewardMallService.getRewardOrderInfo(csRewardId);
    }

    @PostMapping(value = "/customer/reward/putRewardUserCheck")
    @ApiOperation(value = "고객보상제 답변확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "csRewardApplyId", value = "고객보상제 신청 PK", required = true, dataType = "Long")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data"),
            @ApiResponse(code = 901, message = "" +
                    "[NEED_LOGIN] NEED_LOGIN - 로그인필요"
            )
    })
    public ApiResult<?> putRewardUserCheckYn(Long csRewardApplyId) throws Exception {
        return customerRewardMallService.putRewardUserCheckYn(csRewardApplyId);
    }

}
