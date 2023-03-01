package kr.co.pulmuone.v1.calculate.pov.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.calculate.pov.dto.CalPovCostSummaryDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovListDto;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovAllocationVo;
import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovProcessVo;
import kr.co.pulmuone.v1.comm.enums.PovEnums.Corporation;
import kr.co.pulmuone.v1.comm.enums.PovEnums.PovAllocationType;
import kr.co.pulmuone.v1.comm.enums.PovEnums.ProcessStatus;
import kr.co.pulmuone.v1.comm.enums.PovEnums.Scenario;
import kr.co.pulmuone.v1.comm.mapper.calculate.pov.CalPovMapper;
import kr.co.pulmuone.v1.comm.mappers.slavePov.SlavePovMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 정산관리 > POV I/F > POV I/F Service
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

@Service
@RequiredArgsConstructor
public class CalPovService {

	private final CalPovMapper calPovMapper;

	private final SlavePovMapper slavePovMapper;

	/**
	 * POV I/F 리스트 조회
	 *
	 * @param calPovListRequestDto
	 * @return
	 */
	protected CalPovListDto getPovList(String year, String month) {
		return new CalPovListDto(calPovMapper.getPovList(Scenario.TEMPORARY.getCode(), year, month),
				calPovMapper.getPovList(Scenario.FINAL.getCode(), year, month));
	}

	protected List<CalPovCostSummaryDto> mapSummaryDTO(CalPovListDto summary) {
		List<CalPovCostSummaryDto> list = new ArrayList<>();
		List<Corporation> corporations = Arrays.asList(Corporation.PULMUONE, Corporation.PULMUONE_FOOD,
				Corporation.ORGA, Corporation.GREEN_JUICE, Corporation.FOODMERCE);

		BigDecimal zero = BigDecimal.ZERO;

		String me = PovAllocationType.ME.getCode();
		String ov = PovAllocationType.OV.getCode();
		String vdc = PovAllocationType.VDC.getCode();
		String moge = PovAllocationType.MOGE.getCode();

		for (Corporation corporation : corporations) {
			CalPovCostSummaryDto dto = new CalPovCostSummaryDto();
			String code = corporation.getCode();
			dto.setCorporationCode(code);
			dto.setCorporationName(corporation.getCodeName());
			Map<String, BigDecimal> tempMap = summary.getTemporaryAllocationSummary().get(code);
			dto.setTempMeCost(zero);
			dto.setTempOvCost(zero);
			dto.setTempVdcCost(zero);
			dto.setTempMogeCost(zero);
			if (tempMap != null) {
				if(tempMap.get(me) != null) dto.setTempMeCost(tempMap.get(me));
				if(tempMap.get(ov) != null) dto.setTempOvCost(tempMap.get(ov));
				if(tempMap.get(vdc) != null) dto.setTempVdcCost(tempMap.get(vdc));
				if(tempMap.get(moge) != null) dto.setTempMogeCost(tempMap.get(moge));
			}
			Map<String, BigDecimal> finalMap = summary.getFinalAllocationSummary().get(code);
			dto.setFinalMeCost(zero);
			dto.setFinalOvCost(zero);
			dto.setFinalVdcCost(zero);
			dto.setFinalMogeCost(zero);
			if (finalMap != null) {
				if(finalMap.get(me) != null) dto.setFinalMeCost(finalMap.get(me));
				if(finalMap.get(ov) != null) dto.setFinalOvCost(finalMap.get(ov));
				if(finalMap.get(vdc) != null) dto.setFinalVdcCost(finalMap.get(vdc));
				if(finalMap.get(moge) != null) dto.setFinalMogeCost(finalMap.get(moge));
			}
			Map<String, BigDecimal> diffMap = summary.getDifferenceSummary().get(code);
			dto.setDiffMeCost(zero);
			dto.setDiffOvCost(zero);
			dto.setDiffVdcCost(zero);
			dto.setDiffMogeCost(zero);
			if (diffMap != null) {
				if(diffMap.get(me) != null) dto.setDiffMeCost(diffMap.get(me));
				if(diffMap.get(ov) != null) dto.setDiffOvCost(diffMap.get(ov));
				if(diffMap.get(vdc) != null) dto.setDiffVdcCost(diffMap.get(vdc));
				if(diffMap.get(moge) != null) dto.setDiffMogeCost(diffMap.get(moge));
			}
			list.add(dto);
		}

		return list;
	}

	protected Map<String, CalPovProcessVo> getPovProcessList(String year, String month) {
		Map<String, CalPovProcessVo> map = new HashMap<>();
		map.put(Scenario.TEMPORARY.getCode(),
				calPovMapper.getPovProcessList(Scenario.TEMPORARY.getCode(), year, month));
		map.put(Scenario.FINAL.getCode(), calPovMapper.getPovProcessList(Scenario.FINAL.getCode(), year, month));
		return map;
	}

	protected CalPovProcessVo getPovProcessList(String scenario, String year, String month) {
		return calPovMapper.getPovProcessList(scenario, year, month);
	}

	protected boolean validateUploadExcel(CalPovProcessVo calPovProcessVo) {
		ProcessStatus status = getPovProcessStatus(calPovProcessVo.getYear(), calPovProcessVo.getMonth());
		if (!ProcessStatus.canExcelUpload(calPovProcessVo.getScenario(), status.getCode())) {
			return false;
		}
		return true;
	}

	protected ProcessStatus getPovProcessStatus(String year, String month) {
		return ProcessStatus.getStatus(getPovProcessList(year, month));
	}

	protected void deletePovProcessAndAllocation(CalPovProcessVo calPovProcessVo) {
		calPovMapper.deletePovProcessByScenarioAndYearAndMonth(calPovProcessVo.getScenario(), calPovProcessVo.getYear(), calPovProcessVo.getMonth());
		calPovMapper.deletePovAllocationByScenarioAndYearAndMonth(calPovProcessVo.getScenario(), calPovProcessVo.getYear(), calPovProcessVo.getMonth());
		slavePovMapper.deleteRemotePovProcessByScenarioAndYearAndMonth(calPovProcessVo.getScenario(), calPovProcessVo.getYear(), calPovProcessVo.getMonth());
	}

	protected void savePovProcess(CalPovProcessVo calPovProcessVo) {
		calPovMapper.insertPovProcess(calPovProcessVo);
		slavePovMapper.insertRemotePovProcess(calPovProcessVo);
	}

	protected void insertPovAllocation(CalPovAllocationVo calPovAllocationVo) {
		calPovMapper.insertPovAllocation(calPovAllocationVo);
	}

	protected boolean validateInterface(String scenario, String year, String month, String user) {
		ProcessStatus status = getPovProcessStatus(year, month);
		if (!ProcessStatus.canInterface(scenario, status.getCode())) {
			return false;
		}
		return true;
	}

	protected CalPovProcessVo findRemotePovProcessByScenarioAndYearAndMonth(String scenario, String year, String month) {
		return slavePovMapper.findRemotePovProcessByScenarioAndYearAndMonth(scenario, year, month);
	}

	protected void updatePovProcessWhenInterfaced(CalPovProcessVo calPovProcessVo) {
		calPovMapper.updatePovProcessWhenInterfaced(calPovProcessVo);
	}

	protected void doRemoteInterface(CalPovProcessVo calPovProcessVo) {
		String scenario = calPovProcessVo.getScenario();
        String year = calPovProcessVo.getYear();
        String month = calPovProcessVo.getMonth();

		slavePovMapper.updateRemotePovProcessWhenInterfaced(calPovProcessVo);
		deleteRemotePovAllocations(scenario, year, month);
		insertRemotePovAllocations(scenario, year, month);
	}

	private void deleteRemotePovAllocations(String scenario, String year, String month) {
		slavePovMapper.deleteRemotePovAllocationByScenarioAndYearAndMonth(scenario, year, month);
		slavePovMapper.deleteRemotePovAllocationByScenarioAndYearAndMonthVDC(scenario, year, month);
	}

	private void insertRemotePovAllocations(String scenario, String year, String month) {
		List<CalPovAllocationVo> allocations = calPovMapper.getPovList(scenario, year, month);

        for (CalPovAllocationVo allocation : allocations) {
			if (PovAllocationType.ME.getCode().equals(allocation.getAllocationType())
					|| PovAllocationType.OV.getCode().equals(allocation.getAllocationType())
					|| PovAllocationType.MOGE.getCode().equals(allocation.getAllocationType())) {
				slavePovMapper.insertRemotePovAllocation(allocation);
			} else if(PovAllocationType.VDC.getCode().equals(allocation.getAllocationType())) {
				slavePovMapper.insertRemotePovAllocationVDC(allocation);
			}
        }
    }
}