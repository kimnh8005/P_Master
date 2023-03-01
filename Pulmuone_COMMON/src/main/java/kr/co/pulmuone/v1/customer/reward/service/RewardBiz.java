package kr.co.pulmuone.v1.customer.reward.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;

public interface RewardBiz {

	/** 고객보상제 리스트 **/

	// 고객보상제 리스트 조회
	ApiResult<?> getRewardList(RewardBosRequestDto rewardBosRequestDto) throws Exception;
	
	// 고객보상제 등록
    ApiResult<?> addReward(RewardBosRequestDto rewardBosRequestDto) throws Exception;

    // 고객보상제 수정
	ApiResult<?> putReward(RewardBosRequestDto rewardBosRequestDto) throws Exception;

	// 고객보상제 상세조회 - 기본정보, 지급
	ApiResult<?> getRewardInfo(RewardBosRequestDto rewardBosRequestDto) throws Exception;

	// 고객보상제 상세조회 - 적용대상 상품
	ApiResult<?> getRewardTargetGoodsInfo(String csRewardId) throws Exception;

	/** 고객보상제 신청관리 **/

	// 고객보상제 신청관리 조회
    ApiResult<?> getRewardApplyList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    // 보상제 정보 조회
    ApiResult<?> getRewardNmList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    // 고객보상제 신청 상세
    ApiResult<?> getRewardApplyDetail(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    // 고객보상제 신청 첨부파일 이미지
    ApiResult<?> getImageList(String csRewardApplyId) throws Exception;

    // 처리진행 상태변경
    ApiResult<?> putRewardApplyConfirmStatus(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    // 보상제 신청 상세 수정
    ApiResult<?> putRewardApplyInfo(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    ExcelDownloadDto getRewardApplyListExportExcel(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception;

    //답변 확인중으로 상태 변경 시 자동메일/SMS 발송
    void getRewardApplyCompensation(RewardApplyRequestBosDto rewardApplyRequestBosDto);

    //답변 보상여부 결정(보상완료) 시 자동메일/SMS 발송
    void getRewardApplyComplete(RewardApplyRequestBosDto rewardApplyRequestBosDto);

    //답변 보상여부 결정(보상불가) 시 자동메일/SMS 발송
    void getRewardApplyDeniedComplete(RewardApplyRequestBosDto rewardApplyRequestBosDto);

}
