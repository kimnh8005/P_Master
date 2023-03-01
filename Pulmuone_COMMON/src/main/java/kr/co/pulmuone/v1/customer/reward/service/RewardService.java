package kr.co.pulmuone.v1.customer.reward.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.customer.reward.RewardMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardTargetGoodsResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosDetailVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardTargetGoodsBosVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RewardService {

	 private final RewardMapper rewardMapper;

	 /**
     * @Desc  고객보상제 리스트 조회
     * @param rewardBosRequestDto
     * @return RewardBosResponseDto
     */
	 protected Page<RewardBosListVo> getRewardList(RewardBosRequestDto rewardBosRequestDto) throws Exception {
	 	PageMethod.startPage(rewardBosRequestDto.getPage(), rewardBosRequestDto.getPageSize());

	 	return rewardMapper.getRewardList(rewardBosRequestDto);
	 }
	
	 /**
     * @Desc  고객보상제 등록
     * @param rewardBosRequestDto
     * @return RewardBosResponseDto
     */
    protected RewardBosResponseDto addReward(RewardBosRequestDto rewardBosRequestDto) throws Exception{
    	RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();

    	int result = rewardMapper.addReward(rewardBosRequestDto);
        List<RewardTargetGoodsBosVo> rewardTargetGoodsList = rewardBosRequestDto.getRewardTargetGoodsList();

        if(result > 0) {
        	//고객보상제 적용대상 상품 등록
        	if(rewardBosRequestDto.getRewardGoodsTp().equals("TARGET_GOODS") &&
				rewardTargetGoodsList != null && rewardTargetGoodsList.size() > 0){
				rewardMapper.addRewardTargetGoods(rewardTargetGoodsList, rewardBosRequestDto.getCsRewardId());
			}
			rewardBosResponseDto.setCsRewardId(rewardBosRequestDto.getCsRewardId());
		}

        return rewardBosResponseDto;
    }

	/**
     * @Desc  고객보상제 수정
     * @param rewardBosRequestDto
     * @return RewardBosResponseDto
     */
	protected RewardBosResponseDto putReward(RewardBosRequestDto rewardBosRequestDto)  throws Exception{
		RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();
		int result = rewardMapper.putReward(rewardBosRequestDto);
        List<RewardTargetGoodsBosVo> rewardTargetGoodsList = rewardBosRequestDto.getRewardTargetGoodsList();

        if(result > 0) {
        	//고객보상제 적용대상 상품 삭제 후 재등록
        	rewardMapper.delRewardTargetGoodsByCsRewardId(rewardBosRequestDto.getCsRewardId());

        	if(rewardBosRequestDto.getRewardApplyStandard().equals("REWARD_APPLY_STANDARD.ORDER_GOODS")) {
        		if (rewardBosRequestDto.getRewardGoodsTp().equals("TARGET_GOODS") &&
				rewardTargetGoodsList != null && rewardTargetGoodsList.size() > 0) {
					rewardMapper.addRewardTargetGoods(rewardTargetGoodsList, rewardBosRequestDto.getCsRewardId());
				}
			}

			rewardBosResponseDto.setCsRewardId(rewardBosRequestDto.getCsRewardId());
		}

        return rewardBosResponseDto;
	}

	/**
     * @Desc  상세조회 - 기본정보, 지급
     * @param rewardBosRequestDto
     * @return RewardBosResponseDto
     */
	protected RewardBosResponseDto getRewardInfo(RewardBosRequestDto rewardBosRequestDto) throws Exception{
		RewardBosResponseDto rewardBosResponseDto = new RewardBosResponseDto();
		RewardBosDetailVo rewardDetlInfo = rewardMapper.getRewardInfo(rewardBosRequestDto);
		
		if(rewardDetlInfo != null) {
			rewardBosResponseDto.setRewardDetlInfo(rewardDetlInfo);
		}

		return rewardBosResponseDto;
	}

	/**
     * @Desc  상세조회 - 적용대상 상품
     * @param csRewardId
     * @return RewardBosResponseDto
     */
	protected RewardTargetGoodsResponseDto getRewardTargetGoodsInfo(String csRewardId) throws Exception {
		RewardTargetGoodsResponseDto rewardTargetGoodsResponseDto = new RewardTargetGoodsResponseDto();
		List<RewardTargetGoodsBosVo> rewardTargetGoodsInfo = rewardMapper.getRewardTargetGoodsInfo(csRewardId);

		rewardTargetGoodsResponseDto.setRows(rewardTargetGoodsInfo);

		return rewardTargetGoodsResponseDto;
	}

	/**
     * 고객보상제 신청관리 조회
     *
     * @param rewardApplyRequestBosDto
     * @return Page<RewardApplyListVo>
     * @throws Exception Exception
     */
    protected Page<RewardApplyVo> getRewardApplyList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        PageMethod.startPage(rewardApplyRequestBosDto.getPage(), rewardApplyRequestBosDto.getPageSize());
        return rewardMapper.getRewardApplyList(rewardApplyRequestBosDto);
    }

    /**
     * 보상제 정보
     * @param rewardApplyRequestBosDto
     * @return List<RewardApplyListVo>
     * @throws Exception
     */
    protected List<RewardApplyVo> getRewardNmList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception{
        return rewardMapper.getRewardNmList(rewardApplyRequestBosDto);
    }

    /**
     * 고객보상제 신청 상세
     * @param rewardApplyRequestBosDto
     * @return RewardApplyVo
     * @throws Exception
     */
    protected RewardApplyVo getRewardApplyDetail(RewardApplyRequestBosDto rewardApplyRequestBosDto)throws Exception {
        return rewardMapper.getRewardApplyDetail(rewardApplyRequestBosDto);
    }

    /**
     * @Desc 고객보상제 신청 첨부파일 이미지
     * @param csRewardApplyId
     * @return List<RewardApplyVo>
     */
    protected List<RewardApplyVo> getImageList(String csRewardApplyId) {
        return rewardMapper.getImageList(csRewardApplyId);
    }

    /**
     * 처리진행 상태변경
     * @param rewardApplyRequestBosDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> putRewardApplyConfirmStatus(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        rewardApplyRequestBosDto.setUrUserId(SessionUtil.getBosUserVO().getUserId());
        int result = rewardMapper.putRewardApplyConfirmStatus(rewardApplyRequestBosDto);

        return ApiResult.success(result);
    }


    /**
     * 보상제 신청 상세 수정
     * @param rewardApplyRequestBosDto
     * @return
     * @throws Exception
     */
    protected ApiResult<?> putRewardApplyInfo(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        rewardApplyRequestBosDto.setUrUserId(SessionUtil.getBosUserVO().getUserId());
        int result = rewardMapper.putRewardApplyInfo(rewardApplyRequestBosDto);

        return ApiResult.success(result);
    }

	/**
	 * 고객보상제 신청관리 내역 엑셀 다운로드
	 *
	 * @param rewardApplyRequestBosDto
	 * @return List<RewardBosListVo>
	 * @throws Exception
	 */
	@UserMaskingRun(system = "MUST_MASKING")
	protected List<RewardApplyVo> getRewardApplyListExportExcel(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception
	{

		List<RewardApplyVo> result = rewardMapper.getRewardApplyListExportExcel(rewardApplyRequestBosDto);

		return result;
	}
}
