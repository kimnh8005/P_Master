package kr.co.pulmuone.v1.comm.mappers.slavePov;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
 *  1.0		2021. 04. 28.	홍진영		최초작성
 * =======================================================================
 * </PRE>
 */
@Repository
@Mapper
public interface SlavePovMapper {

	void deleteRemotePovProcessByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void insertRemotePovProcess(CalPovProcessVo calPovProcessVo);

	CalPovProcessVo findRemotePovProcessByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void updateRemotePovProcessWhenInterfaced(CalPovProcessVo calPovProcessVo);

	void deleteRemotePovAllocationByScenarioAndYearAndMonth(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void deleteRemotePovAllocationByScenarioAndYearAndMonthVDC(@Param("scenario") String scenario, @Param("year") String year, @Param("month") String month);

	void insertRemotePovAllocation(CalPovAllocationVo calPovAllocationVo);

	void insertRemotePovAllocationVDC(CalPovAllocationVo calPovAllocationVo);
}