package kr.co.pulmuone.v1.comm.mappers.batch.master.order.insidesub;

import kr.co.pulmuone.v1.batch.order.insidesub.dto.LimitIsOrderCountSearchRequestDto;
import kr.co.pulmuone.v1.batch.order.insidesub.dto.vo.OrderLimitInfoVo;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.apache.ibatis.annotations.Mapper;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문체크 후 제한 배치 Mapper
 * </PRE>
 */

@Mapper
public interface OrderIsLimitAfterCheckMapper {

    /**
     * 주문제한정보 조회
     * @param
     * @return OrderLimitInfoVo
     * @throws
     */
    OrderLimitInfoVo selectOrderLimitInfo() throws BaseException;

    /**
     * 제한시간내 주문건수 조회
     * @param params
     * @return int
     * @throws
     */
    int selectLimitIsOrderCount(LimitIsOrderCountSearchRequestDto params) throws BaseException;

    /**
     * 오늘 SMS 발송이력 카운트 조회
     * @param
     * @return int
     * @throws BaseException
     */
    int selectTodaySmsSendHistCount() throws BaseException;

    /**
     * SMS 발송 이력저장
     * @param
     * @return void
     * @throws BaseException
     */
    void putSmsSendHist() throws BaseException;

}
