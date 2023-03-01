package kr.co.pulmuone.v1.user.warehouse.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.DeliveryEnums;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternDetailResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternListVo;

@Service
public class DeliveryPatternServiceImpl implements DeliveryPatternBiz{

    @Autowired
    DeliveryPatternService deliveryPatternService;

    @Override
    public ApiResult<?> getDeliveryPatternList(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();

    	ArrayList<String> selectWeekList = deliveryPatternRequestDto.getSelectWeekList();
		Iterator<String> weekIterator = selectWeekList.iterator();
		while (weekIterator.hasNext()) {
			String checkDay = weekIterator.next();
			if(checkDay.equals(DeliveryEnums.WeekType.MON.getCode())) {
				deliveryPatternRequestDto.setCheckMon(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.TUE.getCode())) {
				deliveryPatternRequestDto.setCheckTue(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.WED.getCode())) {
				deliveryPatternRequestDto.setCheckWed(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.THU.getCode())) {
				deliveryPatternRequestDto.setCheckThu(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.FRI.getCode())) {
				deliveryPatternRequestDto.setCheckFri(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.SAT.getCode())) {
				deliveryPatternRequestDto.setCheckSat(checkDay);
			}
			if(checkDay.equals(DeliveryEnums.WeekType.SUN.getCode())) {
				deliveryPatternRequestDto.setCheckSun(checkDay);
			}
		}

        Page<DeliveryPatternListVo> voList = deliveryPatternService.getDeliveryPatternList(deliveryPatternRequestDto);

        result.setRows(voList.getResult());
        result.setTotal(voList.getTotal());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getShippingPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternDetailResponseDto result = new DeliveryPatternDetailResponseDto();
    	result = deliveryPatternService.getShippingPattern(deliveryPatternRequestDto);
    	return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> addDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{

    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();

		//배송패턴 Insert
    	deliveryPatternService.addDeliveryPattern(deliveryPatternRequestDto);

		if(deliveryPatternRequestDto.getCheckMon().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseMon());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedMon());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.MON.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckTue().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseTue());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedTue());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.TUE.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckWed().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseWed());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedWed());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.WED.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckThu().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseThu());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedThu());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.THU.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckFri().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseFri());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedFri());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.FRI.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckSat().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseSat());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedSat());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.SAT.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckSun().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseSun());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedSun());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.SUN.getCode());
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		DeliveryPatternListVo vo = new DeliveryPatternListVo();
		List<DeliveryPatternListVo> rows = new ArrayList<DeliveryPatternListVo>();
		vo.setPsShippingPatternId(deliveryPatternRequestDto.getPsShippingPatternId());
		vo.setTitle(deliveryPatternRequestDto.getDeliveryPatternName());
		rows.add(vo);
		result.setRows(rows);

    	return ApiResult.success();
    }

    @Override
    public ApiResult<?> putDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();

    	deliveryPatternService.putDeliveryPattern(deliveryPatternRequestDto);

    	deliveryPatternService.removeShippingPatternDay(deliveryPatternRequestDto);
    	String updateShippingPatternId = deliveryPatternRequestDto.getPsShippingPatternId();

		if(deliveryPatternRequestDto.getCheckMon().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseMon());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedMon());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.MON.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckTue().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseTue());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedTue());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.TUE.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckWed().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseWed());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedWed());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.WED.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckThu().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseThu());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedThu());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.THU.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckFri().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseFri());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedFri());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.FRI.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckSat().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseSat());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedSat());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.SAT.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

		if(deliveryPatternRequestDto.getCheckSun().equals(BaseEnums.UseYn.Y.toString())) {
			deliveryPatternRequestDto.setForwardingScheduledDay(deliveryPatternRequestDto.getWarehouseSun());
			deliveryPatternRequestDto.setArrivalScheduledDay(deliveryPatternRequestDto.getArrivedSun());
			deliveryPatternRequestDto.setWeekCode(DeliveryEnums.WeekType.SUN.getCode());
			deliveryPatternRequestDto.setPsShippingPatternId(updateShippingPatternId);
			deliveryPatternService.addShippingPatternDay(deliveryPatternRequestDto);
		}

    	return ApiResult.success();
    }

}
