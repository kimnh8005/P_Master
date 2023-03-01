package kr.co.pulmuone.v1.order.schedule.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBabymealBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleEatsslimBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleGreenJuiceBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBabymealBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleBindBiz;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleEatsslimBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.mall.MallOrderScheduleGreenJuiceBizImpl;

@Component
@Service
public class MallOrderScheduleFactory {

	@Autowired
	private MallOrderScheduleGreenJuiceBizImpl mallOrderScheduleGreenJuiceBizImpl;

	@Autowired
	private MallOrderScheduleEatsslimBizImpl mallOrderScheduleEatsslimBizImpl;

	@Autowired
	private MallOrderScheduleBabymealBizImpl mallOrderScheduleBabymealBizImpl;

    public MallOrderScheduleBindBiz getOrderScheduleBind(String bindType){
        if (bindType.equals(OrderScheduleEnums.ScheduleCd .GREENJUICE.getCode())){
            return mallOrderScheduleGreenJuiceBizImpl;
        } else if (bindType.equals(OrderScheduleEnums.ScheduleCd .EATSSLIM.getCode())) {
            return mallOrderScheduleEatsslimBizImpl;
        } else if (bindType.equals(OrderScheduleEnums.ScheduleCd .BABYMEAL.getCode())) {
            return mallOrderScheduleBabymealBizImpl;
        }

        return null;
    }
}
