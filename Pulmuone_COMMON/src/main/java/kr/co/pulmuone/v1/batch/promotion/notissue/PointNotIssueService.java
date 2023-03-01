package kr.co.pulmuone.v1.batch.promotion.notissue;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointNotIssueService {


	private final PointBiz pointBiz;


    public void runPointNotIssue() throws Exception {


        pointBiz.depositRefundOrderNotIssuePoints();

    }
}
