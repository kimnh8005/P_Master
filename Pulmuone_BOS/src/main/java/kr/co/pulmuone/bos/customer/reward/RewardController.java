package kr.co.pulmuone.bos.customer.reward;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.customer.reward.service.RewardBiz;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RewardController {

	@Autowired
	private RewardBiz rewardBiz;

	@Autowired(required=true)
    private HttpServletRequest request;

	/**
     * @Desc 고객보상제 리스트 조회
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value="고객보상제 리스트 조회")
    @PostMapping(value="/admin/customer/reward/getRewardList")
	@ApiResponse(code = 900, message = "response data", response = RewardBosResponseDto.class)
    public ApiResult<?> getRewardList(RewardBosRequestDto rewardBosRequestDto) throws Exception {
        return rewardBiz.getRewardList((RewardBosRequestDto) BindUtil.convertRequestToObject(request, RewardBosRequestDto.class));
    }

    /**
     * @Desc 고객보상제 등록
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "고객보상제 등록")
	@PostMapping(value = "/admin/customer/reward/addReward")
	public ApiResult<?> addReward(@RequestBody RewardBosRequestDto rewardBosRequestDto) throws Exception {
		return rewardBiz.addReward(rewardBosRequestDto);
	}

	/**
     * @Desc 고객보상제 등록
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "고객보상제 수정")
	@PostMapping(value = "/admin/customer/reward/putReward")
	public ApiResult<?> putReward(@RequestBody RewardBosRequestDto rewardBosRequestDto) throws Exception {
		return rewardBiz.putReward(rewardBosRequestDto);
	}

	/**
     * @Desc 고객보상제 상세조회 - 기본정보, 지급
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "고객보상제 상세조회 - 기본정보, 지급")
	@PostMapping(value = "/admin/customer/reward/getRewardInfo")
	public ApiResult<?> getRewardInfo(RewardBosRequestDto rewardBosRequestDto) throws Exception {
		return rewardBiz.getRewardInfo(rewardBosRequestDto);
	}

	/**
     * @Desc 고객보상제 상세조회 - 적용대상 상품
     * @throws Exception
     * @return ApiResult<?>
     */
    @ApiOperation(value = "고객보상제 상세조회 - 적용대상 상품")
	@PostMapping(value = "/admin/customer/reward/getRewardTargetGoodsInfo")
	public ApiResult<?> getRewardTargetGoodsInfo(String csRewardId) throws Exception {
		return rewardBiz.getRewardTargetGoodsInfo(csRewardId);
	}


}
