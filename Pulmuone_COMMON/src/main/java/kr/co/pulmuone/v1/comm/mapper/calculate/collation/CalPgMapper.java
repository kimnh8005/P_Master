package kr.co.pulmuone.v1.comm.mapper.calculate.collation;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgFailListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.calculate.employee.dto.vo.SettleOuMngVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 거래 내역 대사 Mapper
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
public interface CalPgMapper {

	/**
	 * 부문 구문 전체 조회
	 * @return
	 */
	List<SettleOuMngVo> getOuIdAllList();

	/**
	 * PG 거래 내역 대사 리스트 카운트 조회
	 * @param calPgListRequestDto
	 * @return
	 */
	long getPgListCount(CalPgListRequestDto calPgListRequestDto);

	/**
	 * PG 거래 내역 대사 리스트 조회
	 * @param calPgListRequestDto
	 * @return
	 */
	List<CalPgListDto> getPgList(CalPgListRequestDto calPgListRequestDto);


	int addOdOrderMaster(CalPgListRequestDto calPgListRequestDto);

	int addOdPgCompareUploadDetail(CalPgUploadDto calPgUploadDto);

	int addOdPgCompareUploadDetailInfo(CalPgUploadDto calPgUploadDto);

	int putPgCountInfo(CalPgListRequestDto calPgListRequestDto);

	List<CalPgFailListDto> getCalPgUploadFailList(CalPgListRequestDto calPgListRequestDto);

}
