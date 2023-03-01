package kr.co.pulmuone.v1.comm.mapper.calculate.collation;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallDetlListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > 외부몰 주문 대사 Mapper
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
public interface CalOutmallMapper {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
//	List<SettleOuMngVo> getOuIdAllList();

	/**
	 * 외부몰 주문 대사 리스트 카운트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	long getOutmallListCount(CalOutmallListRequestDto calOutmallListRequestDto);

	/**
	 * 외부몰 주문 대사 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	List<CalOutmallListDto> getOutmallList(CalOutmallListRequestDto calOutmallListRequestDto);

	CalOutmallUploadDto getSellerInfo(String sellersNm);

	int addOdOrderMaster(CalOutmallListRequestDto calOutmallListRequestDto);

	int addOdOrderUploadDetail(CalOutmallUploadDto calOutmallUploadDto);

	int putOutmallCountInfo(CalOutmallListRequestDto calOutmallListRequestDto);

	/**
	 * 외부몰 주문 대사 상ㅇ세내역 리스트 카운트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	CalOutmallDetlListDto getOutmallDetlListCount(CalOutmallListRequestDto calOutmallListRequestDto);

	/**
	 * 외부몰 주문 대사 상세내역 리스트 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	List<CalOutmallDetlListDto> getOutmallDetlList(CalOutmallListRequestDto calOutmallListRequestDto);

	/**
	 * BOS 클레임 사유 건수 조회
	 * @param psClaimBosId
	 * @return
	 */
	int getPsClaimBosCount(long psClaimBosId);

	/**
	 * 외부몰 주문 대사 엑셀업르도 실패내역 조회
	 * @param calOutmallListRequestDto
	 * @return
	 */
	List<CalOutmallDetlListDto> getCalOutmallUploadFailList(CalOutmallListRequestDto calOutmallListRequestDto);

}
