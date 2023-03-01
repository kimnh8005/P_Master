package kr.co.pulmuone.v1.batch.promotion.notissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PointNotIssueBizImpl implements PointNotIssueBiz{

	 @Autowired
	 private PointNotIssueService pointNotIssueService;

	 @Override
	 @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
	 public void runPointNotIssue() throws Exception {
		 pointNotIssueService.runPointNotIssue();
	 }

}
