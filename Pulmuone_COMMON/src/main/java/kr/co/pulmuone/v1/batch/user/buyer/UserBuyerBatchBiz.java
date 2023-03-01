package kr.co.pulmuone.v1.batch.user.buyer;

import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;

import java.util.List;

public interface UserBuyerBatchBiz {

    List<UserBuyerVo> getBuyerList(String batchYearMonth, String batchUserType);

    void putBuyerGroup(List<UserBuyerVo> userBuyerList);

    List<UserBuyerVo> getBuyerMoveList(String batchYearMonth, String batchUserType);

    void putBuyerMoveGroup(List<UserBuyerVo> userBuyerList);

	void addChangeLog(List<UserBuyerVo> userBuyerList);

}
