package kr.co.pulmuone.v1.order.delivery.service;

import java.util.List;

import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberUploadDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.order.delivery.OrderBulkTrackingNumberMapper;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberDetlListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.OrderBulkTrackingNumberListRequestDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberFailVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberSuccVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderBulkTrackingNumberVo;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.RequiredArgsConstructor;

/**
* <PRE>
* Forbiz Korea
* 일괄송장 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일				:  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 24.        이규한          	  최초작성
* =======================================================================
* </PRE>
*/

@Service
@RequiredArgsConstructor
public class OrderBulkTrackingNumberService {

    @Autowired
    private OrderBulkTrackingNumberMapper orderBulkTrackingNumberMapper;

    /**
     * @Desc 주문상세 번호 존재여부 조회
     * @param odOrderDetlId
     * @throws Exception
     * @return int
     */
    protected Integer getOdOrderDetlIdCount(String odid, int odOrderDetlSeq) {
    	return orderBulkTrackingNumberMapper.getOdOrderDetlIdCount(odid, odOrderDetlSeq);
    }

    /**
     * @Desc 택배사 존재여부 조회
     * @param psShippingCompId
     * @throws Exception
     * @return int
     */
    protected int getPsShippingCompIdCount(Long psShippingCompId) {
    	return orderBulkTrackingNumberMapper.getPsShippingCompIdCount(psShippingCompId);
    }

    /**
     * @Desc 일괄송장정보 등록
     * @param OrderBulkTrackingNumberVo
     * @throws Exception
     * @return
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOrderBulkTrackingNumber(OrderBulkTrackingNumberVo paramVo) {
   		return orderBulkTrackingNumberMapper.addOrderBulkTrackingNumber(paramVo);
   	}

    /**
     * @Desc 주문상세 송장번호 등록여부 조회
     * @param OrderBulkTrackingNumberVo
     * @throws Exception
     * @return int
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int getOrderTrackingNumberCnt(Long odOrderDetlId) {
   		return orderBulkTrackingNumberMapper.getOrderTrackingNumberCnt(odOrderDetlId);
   	}

    /**
     * @Desc 주문상세 송장번호 등록
     * @param OrderTrackingNumberVo
     * @throws Exception
     * @return int
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int addOrderTrackingNumber(OrderTrackingNumberVo odTrackingNumberVo) {
   		return orderBulkTrackingNumberMapper.addOrderTrackingNumber(odTrackingNumberVo);
	}

    /**
     * @Desc 주문상세 송장번호 수정
     * @param OrderTrackingNumberVo
     * @throws Exception
     * @return int
     */
   	@Transactional(rollbackFor = Exception.class)
   	protected int putOrderTrackingNumber(OrderTrackingNumberVo odTrackingNumberVo) {
   		return orderBulkTrackingNumberMapper.putOrderTrackingNumber(odTrackingNumberVo);
	}

    /**
     * @Desc 일괄송장 성공내역 테이블 등록
     * @param OrderBulkTrackingNumberSuccVo
     * @throws Exception
     * @return int
     */
   	protected int addOrderBulkTrackingNumberSucc(OrderBulkTrackingNumberSuccVo odBulkTrackingNumberSuccVo) {
		return orderBulkTrackingNumberMapper.addOrderBulkTrackingNumberSucc(odBulkTrackingNumberSuccVo);

	}

    /**
     * @Desc 일괄송장 실패내역 테이블 등록
     * @param OrderBulkTrackingNumberFailVo
     * @throws Exception
     * @return int
     */
   	protected int addOrderBulkTrackingNumberFail(OrderBulkTrackingNumberFailVo odBulkTrackingNumberFailVo) {
		return orderBulkTrackingNumberMapper.addOrderBulkTrackingNumberFail(odBulkTrackingNumberFailVo);

	}

    /**
     * @Desc 일괄송장정보 수정(성공/실패 건수 업데이트)
     * @param OrderBulkTrackingNumberVo
     * @throws Exception
     * @return int
     */
   	protected int putOrderBulkTrackingNumber(OrderBulkTrackingNumberVo odBulkTrackingNumberVo) {
		return orderBulkTrackingNumberMapper.putOrderBulkTrackingNumber(odBulkTrackingNumberVo);
	}

    /**
     * @Desc 일괄송장 입력 내역 목록 조회
     * @param OrderBulkTrackingNumberListRequestDto
     * @throws Exception
     * @return Page<OdBulkTrackingNumberVo>
     */
   	protected Page<OrderBulkTrackingNumberVo> getOrderBulkTrackingNumberList(OrderBulkTrackingNumberListRequestDto paramDto) {
   		PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return orderBulkTrackingNumberMapper.getOrderBulkTrackingNumberList(paramDto);
	}

   	/**
     * @Desc 일괄송장 입력 실패내역 목록 조회
     * @param OrderBulkTrackingNumberListRequestDto
     * @throws Exception
     * @return List<OdBulkTrackingNumberFailVo>
     */
	protected List<OrderBulkTrackingNumberFailVo> getOrderBulkTrackingNumberFailList(OrderBulkTrackingNumberListRequestDto paramDto) {
		return orderBulkTrackingNumberMapper.getOrderBulkTrackingNumberFailList(paramDto);
	}

    /**
     * @Desc 일괄 송장 입력 내역 상세 목록 조회
     * @param OrderBulkTrackingNumberDetlListRequestDto
     * @throws Exception
     * @return Page<OdBulkTrackingNumberDetlDto>
     */
	protected Page<OrderBulkTrackingNumberDetlDto> getOrderBulkTrackingNumberDetlList(OrderBulkTrackingNumberDetlListRequestDto paramDto) {
   		PageMethod.startPage(paramDto.getPage(), paramDto.getPageSize());
        return orderBulkTrackingNumberMapper.getOrderBulkTrackingNumberDetlList(paramDto);
	}

   	/**
     * @Desc 일괄 송장 입력 내역 상세 목록 조회 (엑셀다운로드)
     * @param OrderBulkTrackingNumberDetlListRequestDto
     * @throws Exception
     * @return List<OdBulkTrackingNumberFailVo>
     */
	protected List<OrderBulkTrackingNumberDetlDto> getOrderBulkTrackingNumberDetlExcelList(OrderBulkTrackingNumberDetlListRequestDto paramDto) {
		return orderBulkTrackingNumberMapper.getOrderBulkTrackingNumberDetlList(paramDto);
	}

	/**
	 * @Desc 일괄송장 입력 가능 여부 조회(주문상태가 배송준비중,배송중이고 클레임주문건이 아닌경우만 송장 입력 가능)
	 * @param odOrderDetlId
	 * @return int
	 */
	protected int getOrderBulkTrackingNumberOrderStatus(Long odOrderDetlId){
		return orderBulkTrackingNumberMapper.getOrderBulkTrackingNumberOrderStatus(odOrderDetlId);
	}
}