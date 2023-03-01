package kr.co.pulmuone.batch.eon.job.application.sample;

import kr.co.pulmuone.batch.eon.domain.model.sample.SampleMasterVo;
import kr.co.pulmuone.batch.eon.domain.service.sample.SampleMasterService;
import kr.co.pulmuone.batch.eon.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Component("sampleEonJob")
@Slf4j
@RequiredArgsConstructor
public class SampleEonJob implements BaseJob {

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
