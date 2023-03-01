package kr.co.pulmuone.v1.order.schedule.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBabymealBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBindBiz;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleEatsslimBizImpl;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleGreenJuiceBizImpl;

@Component
@Service
public class OrderScheduleFactory {

	@Autowired
	private OrderScheduleGreenJuiceBizImpl orderScheduleGreenJuiceBizImpl;

	@Autowired
	private OrderScheduleEatsslimBizImpl orderScheduleEatsslimBizImpl;

	@Autowired
	private OrderScheduleBabymealBizImpl orderScheduleBabymealBizImpl;

    public OrderScheduleBindBiz getOrderScheduleBind(String bindType){
        if (bindType.equals(OrderScheduleEnums.ScheduleCd .GREENJUICE.getCode())){
            return orderScheduleGreenJuiceBizImpl;
        } else if (bindType.equals(OrderScheduleEnums.ScheduleCd .EATSSLIM.getCode())) {
            return orderScheduleEatsslimBizImpl;
        } else if (bindType.equals(OrderScheduleEnums.ScheduleCd .BABYMEAL.getCode())) {
            return orderScheduleBabymealBizImpl;
        }

        return null;
    }
}
