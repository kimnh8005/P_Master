package kr.co.pulmuone.v1.comm.mapper.calculate.order;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListDto;
import kr.co.pulmuone.v1.calculate.order.dto.CalOrderListRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 주문정산 > 주문 정산 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 *  1.0		2021. 03. 05.	이명수		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface CalOrderMapper {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	List<SettleOuMngVo> getOuIdAllList();

	/**
	 * 상품 정산 리스트 카운트 조회
	 * @param calOrderListRequestDto
	 * @return
	 */
	long getOrderListCount(CalOrderListRequestDto calOrderListRequestDto);

	/**
	 * 주문 정산 리스트 조회
	 * @param calOrderListRequestDto
	 * @return
	 */
	List<CalOrderListDto> getOrderList(CalOrderListRequestDto calOrderListRequestDto);

	List<CalOrderListDto> getOrderListExportExcel(CalOrderListRequestDto calOrderListRequestDto);
}
