package kr.co.pulmuone.v1.batch.user.buyer;

import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.UserBuyerBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserBuyerBatchService {

    private final UserBuyerBatchMapper userBuyerBatchMapper;

    /**
     * 회원 정보 조회
     *
     * @return List<UserBuyerVo>
     */
    public List<UserBuyerVo> getBuyerList(String batchYearMonth, String batchUserType) {
        return userBuyerBatchMapper.getBuyerList(batchYearMonth, batchUserType);
    }

    /**
     * 회원 정보 수정 - 등급
     *
     */
    public void putBuyerGroup(List<UserBuyerVo> userBuyerList) {
        userBuyerBatchMapper.putBuyerGroup(userBuyerList);
    }

    /**
     * 휴면 회원 정보 조회
     *
     * @return List<UserBuyerVo>
     */
    public List<UserBuyerVo> getBuyerMoveList(String batchYearMonth, String batchUserType) {
        return userBuyerBatchMapper.getBuyerMoveList(batchYearMonth, batchUserType);
    }

    /**
     * 휴면 회원 정보 수정 - 등급
     *
     */
    public void putBuyerMoveGroup(List<UserBuyerVo> userBuyerList) {
        userBuyerBatchMapper.putBuyerMoveGroup(userBuyerList);
    }

    /**
     * 회원 등급 변경 이력 등록
     *
     */
    public void addChangeLog(List<UserBuyerVo> userBuyerList) {
    	userBuyerBatchMapper.addChangeLog(userBuyerList);
    }

}
