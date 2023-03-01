package kr.co.pulmuone.v1.comm.mapper.calculate.pov;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovAllocationVo;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > POV I/F > POV I/F Mapper
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
public interface CalPovMapper {

	/**
	 * POV I/F 리스트 조회
	 * @param calPovListRequestDto
	 * @return
	 */
	List<CalPovAllocationVo> getPovList(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	CalPovProcessVo getPovProcessList(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void deletePovProcessByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void deletePovAllocationByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void deleteRemotePovProcessByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void insertPovProcess(CalPovProcessVo calPovProcessVo);

	void insertPovAllocation(CalPovAllocationVo calPovAllocationVo);

	void updatePovProcessWhenInterfaced(CalPovProcessVo calPovProcessVo);
}

