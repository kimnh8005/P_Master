package kr.co.pulmuone.v1.batch.promotion.point;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PointExpireBizImpl implements PointExpireBiz{

	 @Autowired
	 private PointExpireService pointExpireService;


	 @Override
	 @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	 public void runExpirePoint() throws Exception {
		 pointExpireService.runExpirePoint();
	 }
}
