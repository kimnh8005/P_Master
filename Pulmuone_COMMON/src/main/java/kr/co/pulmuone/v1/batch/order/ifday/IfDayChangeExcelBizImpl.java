package kr.co.pulmuone.v1.batch.order.ifday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IfDayChangeExcelBizImpl implements IfDayChangeExcelBiz {

    @Autowired
    private IfDayChangeExcelService ifDayChangeExcelService;



	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	public void putIfDayChange() throws Exception {
		ifDayChangeExcelService.putIfDayChange();
	}
}