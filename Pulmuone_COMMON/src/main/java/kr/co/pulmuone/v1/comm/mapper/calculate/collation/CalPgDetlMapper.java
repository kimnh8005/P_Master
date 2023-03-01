package kr.co.pulmuone.v1.comm.mapper.calculate.collation;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > 대사관리 > PG 대사 상세내역 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :	작성일		:	작성자	:	작성내역
 * -----------------------------------------------------------------------
 * 1.0		2021. 04. 26.	이원호		최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface CalPgDetlMapper {

    List<CalPgDetlListDto> getPgDetailList(CalPgDetlListRequestDto dto);

    CalPgUploadResponseDto getPgDetailListCount(CalPgDetlListRequestDto dto);

}
