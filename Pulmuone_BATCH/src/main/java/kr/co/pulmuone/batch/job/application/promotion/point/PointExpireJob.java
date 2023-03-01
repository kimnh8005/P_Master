package kr.co.pulmuone.batch.job.application.promotion.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.promotion.point.PointExpireBiz;
import lombok.extern.slf4j.Slf4j;

@Component("pointExpireJob")
@Slf4j
public class PointExpireJob implements BaseJob{

	@Autowired
	private PointExpireBiz pointExpireBiz;

	@Override
    public void run(String[] params) throws Exception {
		pointExpireBiz.runExpirePoint();
	}


}
