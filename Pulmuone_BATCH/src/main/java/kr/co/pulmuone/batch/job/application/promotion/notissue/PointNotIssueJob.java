package kr.co.pulmuone.batch.job.application.promotion.notissue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.promotion.notissue.PointNotIssueBiz;
import lombok.extern.slf4j.Slf4j;

@Component("pointNotIssueJob")
@Slf4j
public class PointNotIssueJob implements BaseJob{

	@Autowired
    private PointNotIssueBiz pointNotIssueBiz;

    @Override
    public void run(String[] params) throws Exception {

    	// 미적립된 환불 적립금 재적립 배치
    	pointNotIssueBiz.runPointNotIssue();

    }
}
