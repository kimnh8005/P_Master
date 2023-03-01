package kr.co.pulmuone.v1.comm.mapper.user.buyer;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStatusHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopHistoryListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetBuyerStopLogResultVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BuyerStatusMapper {

	Page<GetBuyerStopListResultVo> getBuyerStopList(GetUserStopListRequestDto dto) throws Exception;

	int getBuyerStopListCount(GetUserStopListRequestDto dto) throws Exception;

	GetBuyerStopLogResultVo getBuyerStopLog(GetUserStopHistoryRequestDto dto) throws Exception;

	Page<GetBuyerStopHistoryListResultVo> getBuyerStopHistoryList(GetUserStopHistoryListRequestDto dto) throws Exception;

	int getBuyerStopHistoryListCount(GetUserStopHistoryListRequestDto dto) throws Exception;

	Page<GetBuyerStatusHistoryListResultVo> getBuyerStatusHistoryList(GetBuyerStatusHistoryListRequestDto dto) throws Exception;

	int putBuyerStop(PutBuyerStopParamDto dto);

	int addBuyerStatusLog(AddBuyerStatusLogParamDto dto) throws Exception;

	int putBuyerNormal(PutBuyerNormalParamDto dto) throws Exception;

	BuyerStatusResultVo getBuyerStatusConvertInfo(String urUserId);

	MarketingInfoDto getMarketingInfo(Long urUserId) throws Exception;
}
