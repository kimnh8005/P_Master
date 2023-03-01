package kr.co.pulmuone.batch.esl.job.application.sample;

import kr.co.pulmuone.batch.esl.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.esl.domain.service.sample.SampleMasterService;
import kr.co.pulmuone.batch.esl.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component("sampleEslJob")
@Slf4j
@RequiredArgsConstructor
public class SampleEslJob implements BaseJob {

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