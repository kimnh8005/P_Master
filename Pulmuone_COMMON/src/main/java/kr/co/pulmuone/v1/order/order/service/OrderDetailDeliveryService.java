package kr.co.pulmuone.v1.order.order.service;


import kr.co.pulmuone.v1.comm.mapper.order.order.OrderDateMapper;
import kr.co.pulmuone.v1.comm.mapper.order.order.OrderDetailMapper;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMapper;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailGoodsListDto;
import kr.co.pulmuone.v1.order.order.dto.mall.OrderDetailGoodsListMallDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 상세 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 28.            이명수         최초작성
 *  1.1    2021. 01. 12.            이규한         주문상세 동일 출고처 주문상세ID리스트 조회 추가
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailDeliveryService {

    private final OrderDetailMapper orderDetailMapper;

    private final OrderDateMapper orderDateMapper;

    private final OrderStatusMapper orderStatusMapper;

    /**
     * 주문상세 상품 리스트 조회
     * @param odid
     * @return
     */
    public List<OrderDetailGoodsListDto> getOrderDetailGoodsList(String odid) {
        return orderDetailMapper.getOrderDetailGoodsList(odid);
    }

    /**
     * 주문상세 상품 리스트 조회
     * @param odid
     * @return
     */
    public List<OrderDetailGoodsListMallDto> getOrderDetailGoodsMallList(String odid) {
        return orderDetailMapper.getOrderDetailGoodsMallList(odid);
    }

    /**
     * 주문상세 동일 출고처 주문상세ID 리스트 조회
     * @param odOrderDetlId (주문상세 PK)
     * @return List<Long> (주문상세PK List)
     */
    protected List<Long> getUrWarehouseOdOrderDetlIdList(Long odOrderDetlId) {
        return orderDetailMapper.getUrWarehouseOdOrderDetlIdList(odOrderDetlId);
    }

    /**
     * 도착예정일 변경 처리
     * @param orderDetlVo
     * @return
     */
    protected int putOrderDetailDt(OrderDetlVo orderDetlVo) {
        return orderDateMapper.putOrderDetailDt(orderDetlVo);
    }

    /**
     * 도착예정일 변경 이력 등록
     * @param orderDetlHistVo
     * @return
     */
    protected int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) {
        return orderStatusMapper.putOrderDetailStatusHist(orderDetlHistVo);
    }

    /**
     * 주문상세 처리이력 메세지 정보 주문상세ID 조회
     * @param odOrderDetlId (주문상세 PK)
     * @return OrderDetlVo>
     */
    public OrderDetlVo getHistMsgOdOrderDetlId(Long odOrderDetlId) {
        return getHistMsgOdOrderDetlId(odOrderDetlId, null);
    }


    /**
     * 주문상세 처리이력 메세지 정보 주문상세ID 조회
     * @param odOrderDetlId (주문상세 PK)
     * @return OrderDetlVo>
     */
    public OrderDetlVo getHistMsgOdOrderDetlId(Long odOrderDetlId, String type) {
        return orderDetailMapper.getHistMsgOdOrderDetlId(odOrderDetlId, type);
    }
}
