package kr.co.pulmuone.v1.comm.mapper.order.regular;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.regular.dto.MallRegularReqInfoResponseDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularReqGoodsListRequestDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultReqRoundGoodsListDto;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 Mall Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 02. 09.	김명진 		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface MallOrderRegularMapper {

	/**
	 * 정기배송 신청 정보 조회
	 * @param regularReqListRequestDto
	 * @return
	 * @throws Exception
	 */
	MallRegularReqInfoResponseDto getOrderRegularReqInfo(long urUserId) throws Exception;

	/**
	 * 정기배송 조회 회차 총 수 조회
	 * @param regularReqGoodsListRequestDto
	 * @return
	 * @throws Exception
	 */
	int getOrderRegularReqRoundGoodsListTotCnt(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception;

	/**
	 * 정기배송 회차별 상품 목록 조회
	 * @param urUserId
	 * @return
	 * @throws Exception
	 */
	List<RegularResultReqRoundGoodsListDto> getOrderRegularReqRoundGoodsList(RegularReqGoodsListRequestDto regularReqGoodsListRequestDto) throws Exception;
}
