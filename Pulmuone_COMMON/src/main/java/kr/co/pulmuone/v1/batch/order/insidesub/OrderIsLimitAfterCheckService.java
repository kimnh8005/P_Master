package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.LimitIsOrderCountSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.OrderLimitInfoVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.insidesub.OrderIsLimitAfterCheckMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문체크 후 제한 배치 Service
 * </PRE>
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderIsLimitAfterCheckService {

    private final OrderIsLimitAfterCheckMapper orderIsLimitAfterCheckMapper;

    /**
     * 주문제한정보 조회
     * @param
     * @return OrderLimitInfoVo
     * @throws BaseException
     */
    protected OrderLimitInfoVo selectOrderLimitInfo() throws BaseException {
        return orderIsLimitAfterCheckMapper.selectOrderLimitInfo();
    }

    /**
     * 제한시간내 주문건수 조회
     * @param params
     * @return int
     * @throws BaseException
     */
    protected int selectLimitIsOrderCount(LimitIsOrderCountSearchRequestDto params) throws BaseException {
        return orderIsLimitAfterCheckMapper.selectLimitIsOrderCount(params);
    }

    /**
     * 오늘 SMS 발송이력 카운트 조회
     * @param
     * @return int
     * @throws BaseException
     */
    protected int selectTodaySmsSendHistCount() throws BaseException {
        return orderIsLimitAfterCheckMapper.selectTodaySmsSendHistCount();
    }

    /**
     * SMS 발송 이력저장
     * @param
     * @return void
     * @throws BaseException
     */
    protected void putSmsSendHist() throws BaseException {
        orderIsLimitAfterCheckMapper.putSmsSendHist();
    }

}
