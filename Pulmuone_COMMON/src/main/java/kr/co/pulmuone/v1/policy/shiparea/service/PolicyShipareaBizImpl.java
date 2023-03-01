package kr.co.pulmuone.v1.policy.shiparea.service;

import kr.co.pulmuone.v1.policy.shiparea.dto.vo.NonDeliveryAreaInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.policy.shiparea.dto.PolicyShipareaDto;

import kr.co.pulmuone.v1.comm.constants.Constants;

@Service
public class PolicyShipareaBizImpl implements PolicyShipareaBiz{

	@Autowired
    private PolicyShipareaService policyShipareaService;

	@Override
	public ApiResult<?> getBackCountryList(PolicyShipareaDto dto) {
		if ("JEJU".equals(dto.getSearchAreaType()))
			dto.setJejuYn("Y");
		else if ("ISLAND".equals(dto.getSearchAreaType()))
			dto.setIslandYn("Y");

		return ApiResult.success(policyShipareaService.getBackCountryList(dto));
	}

	@Override
	public ApiResult<?> addBackCountry(PolicyShipareaDto dto) {
        if(dto.getZipCode().isEmpty()) {
        	return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);
        } else if(policyShipareaService.duplicateBackCountryCount(dto) > 0) {
        	return ApiResult.result(BaseEnums.CommBase.DUPLICATE_DATA);
        } else {
        	String arryAreaTypes[] = dto.getAreaType().split(Constants.ARRAY_SEPARATORS);

    		for (String areaTypeVal : arryAreaTypes) {
    			if ("JEJU".equals(areaTypeVal))
    				dto.setJejuYn("Y");
    			else if ("ISLAND".equals(areaTypeVal))
    				dto.setIslandYn("Y");
    		}

    		return policyShipareaService.addBackCountry(dto);
        }
	}

	@Override
	public ApiResult<?> putBackCountry(PolicyShipareaDto dto) {
		String arryAreaTypes[] = dto.getAreaType().split(Constants.ARRAY_SEPARATORS);

		for (String areaTypeVal : arryAreaTypes) {
			if ("JEJU".equals(areaTypeVal))
				dto.setJejuYn("Y");
			else if ("ISLAND".equals(areaTypeVal))
				dto.setIslandYn("Y");
		}

        return policyShipareaService.putBackCountry(dto);
	}

	@Override
	public ApiResult<?> delBackCountry(PolicyShipareaDto dto) {
		if (dto.getZipCodeCsv() != null)
			dto.setZipCodes(dto.getZipCodeCsv().split(","));

		return policyShipareaService.delBackCountry(dto);
	}

	@Override
	public ApiResult<?> getBackCountry(PolicyShipareaDto dto) {
		return ApiResult.success(policyShipareaService.getBackCountry(dto));
	}

	@Override
	public ExcelDownloadDto getBackCountryExcelList(String[] zipCodes) {
		return policyShipareaService.getBackCountryExcelList(zipCodes);
	}

	@Override
	public boolean isUndeliverableArea(String undeliverableType, String zipCode) throws Exception {
		return policyShipareaService.isUndeliverableArea(undeliverableType, zipCode);
	}

	@Override
	public boolean isNonDeliveryArea(String[] undeliverableTypes, String zipCode) throws Exception {
		return policyShipareaService.isNonDeliveryArea(undeliverableTypes, zipCode);
	}

	@Override
	public NonDeliveryAreaInfoVo getNonDeliveryAreaInfo(String[] undeliverableTypes, String zipCode) throws Exception {
		return policyShipareaService.getNonDeliveryAreaInfo(undeliverableTypes, zipCode);
	}
}
