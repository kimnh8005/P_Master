package kr.co.pulmuone.batch.erp.job.application.sample;

import kr.co.pulmuone.batch.erp.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.erp.domain.service.sample.SampleMasterService;
import kr.co.pulmuone.batch.erp.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("sampleErpJob")
@Slf4j
@RequiredArgsConstructor
public class SampleErpJob implements BaseJob {

    private final SampleMasterService sampleMasterService;

    @Override
    @Transactional
    public void run(String[] params){
        masterDbTest();
    }

    private void masterDbTest() {

        log.info("masterDbTest {}", "[조회 시작]");
        List<SampleMasterVo> list = sampleMasterService.getList();
        list.stream().forEach(System.out::println);
        log.info("masterDbTest {}", "[조회 끝]\n\n\n\n");
    }
}