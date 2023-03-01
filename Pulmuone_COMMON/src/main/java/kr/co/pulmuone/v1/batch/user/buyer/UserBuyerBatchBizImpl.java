package kr.co.pulmuone.v1.batch.user.buyer;

import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserBuyerBatchBizImpl implements UserBuyerBatchBiz {

    @Autowired
    private UserBuyerBatchService userBuyerBatchService;

    @Override
    public List<UserBuyerVo> getBuyerList(String batchYearMonth, String batchUserType) {
        return userBuyerBatchService.getBuyerList(batchYearMonth, batchUserType);
    }

    @Override
    public void putBuyerGroup(List<UserBuyerVo> userBuyerList) {
        userBuyerBatchService.putBuyerGroup(userBuyerList);
    }

    @Override
    public List<UserBuyerVo> getBuyerMoveList(String batchYearMonth, String batchUserType) {
        return userBuyerBatchService.getBuyerMoveList(batchYearMonth, batchUserType);
    }

    @Override
    public void putBuyerMoveGroup(List<UserBuyerVo> userBuyerList) {
        userBuyerBatchService.putBuyerMoveGroup(userBuyerList);
    }

    @Override
    public void addChangeLog(List<UserBuyerVo> userBuyerList) {
        userBuyerBatchService.addChangeLog(userBuyerList);
    }

}
