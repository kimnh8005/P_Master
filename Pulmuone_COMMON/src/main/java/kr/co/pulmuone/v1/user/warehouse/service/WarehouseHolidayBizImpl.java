package kr.co.pulmuone.v1.user.warehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.warehouse.dto.SaveWarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseHolidayResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.WarehouseTemplateResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseHolidayResultVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.WarehouseResultVo;

@Service
public class WarehouseHolidayBizImpl implements WarehouseHolidayBiz{

	@Autowired
	WarehouseHolidayService warehouseHolidayService;

	@Override
	public ApiResult<?> getWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getWarehouseHolidayList(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);
    }


	@Override
	public ApiResult<?> getWarehouseSetList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getWarehouseSetList(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getWarehouseHolidayDetail(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getWarehouseHolidayDetail(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);
	}

	@Override
	public ApiResult<?> getConfirmWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
		WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getConfirmWarehouseHolidayList(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);
	}

    @Override
    public ApiResult<?> addWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception{
        return warehouseHolidayService.addWarehouseHoliday(saveWarehouseHolidayRequestDto);
    }

    @Override
    public ApiResult<?> putWarehouseHoliday(SaveWarehouseHolidayRequestDto saveWarehouseHolidayRequestDto) throws Exception{
        return warehouseHolidayService.putWarehouseHoliday(saveWarehouseHolidayRequestDto);
    }


    @Override
	public ApiResult<?> getScheduleWarehouseHolidayList(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception{
    	WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getScheduleWarehouseHolidayList(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);

    }

    @Override
    public ApiResult<?> getHolidayWarehouseInfo(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception{
    	WarehouseTemplateResponseDto result = new WarehouseTemplateResponseDto();

    	WarehouseResultVo vo = new WarehouseResultVo();
    	vo = warehouseHolidayService.getHolidayWarehouseInfo(warehouseHolidayRequestDto);

    	result.setWarehouseResultTemplate(vo);

    	return ApiResult.success(result);
    }

	@Override
	public ApiResult<?> getWarehouseHolidayListById(WarehouseHolidayRequestDto warehouseHolidayRequestDto) throws Exception {
    	WarehouseHolidayResponseDto result = new WarehouseHolidayResponseDto();

        Page<WarehouseHolidayResultVo> warehouseHolidayResultVoList = warehouseHolidayService.getWarehouseHolidayListById(warehouseHolidayRequestDto);

        result.setRows(warehouseHolidayResultVoList.getResult());
        result.setTotal(warehouseHolidayResultVoList.getTotal());

        return ApiResult.success(result);
    }

}
