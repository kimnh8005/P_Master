package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import java.util.List;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.*;
import kr.co.pulmuone.v1.user.certification.dto.PutPasswordClearRequestDto;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BuyerMapper
{
	Page<GetBuyerListResultVo> getBuyerList(GetBuyerListRequestDto dto) throws Exception;

  Page<GetCouponListResultVo> getCouponList(GetCouponListRequestDto dto) throws Exception;

	List<GetBuyerListResultVo> getBuyerListExcel(GetBuyerListRequestDto dto) throws Exception;

	GetBuyerResultVo getBuyer(GetBuyerRequestDto dto) throws Exception;

	List<GetBuyerGroupChangeHistoryListResultVo> getBuyerGroupChangeHistoryList(GetBuyerGroupChangeHistoryListRequestDto dto) throws Exception;

	List<GetBuyerGroupChangeHistoryListResultVo> getBuyerGroupChangeHistoryListForNoChangeLog(GetBuyerGroupChangeHistoryListRequestDto dto) throws Exception;

	List<GetBuyerRecommendListResultVo> getBuyerRecommendList(GetBuyerRecommendListRequestDto dto) throws Exception;

	int putBuyer(PutBuyerRequestDto dto) throws Exception;

	GetBuyerResultVo getBuyerMove(GetBuyerRequestDto dto) throws Exception;

	int getUserChangeHistoryListCount(GetUserChangeHistoryListRequestDto dto) throws Exception;

	Page<GetUserChangeHistoryResultVo> getUserChangeHistoryList(GetUserChangeHistoryListRequestDto dto) throws Exception;

	int putPasswordClear(PutPasswordClearRequestDto putPasswordClearRequestDto) throws Exception;

	int putCertificationFailCntClear(PutPasswordClearRequestDto putPasswordClearRequestDto) throws Exception;

	List<GetUserMaliciousClaimHistoryListResultVo> getUserMaliciousClaimHistoryList(GetUserMaliciousClaimHistoryListRequestDto dto) throws Exception;

	Page<GetPointInfoVo> getPointInfo(GetBuyerListRequestDto dto) throws Exception;

	GetPointInfoVo getLoginInfo(GetBuyerListRequestDto getBuyerListRequestDto) throws Exception;
}
