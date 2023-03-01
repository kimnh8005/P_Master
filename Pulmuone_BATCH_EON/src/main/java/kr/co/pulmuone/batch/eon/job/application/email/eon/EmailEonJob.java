package kr.co.pulmuone.batch.eon.job.application.email.eon;

import kr.co.pulmuone.batch.eon.domain.service.email.eon.EmailEonBiz;
import kr.co.pulmuone.batch.eon.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("emailEonJob")
@Slf4j
@RequiredArgsConstructor
public class EmailEonJob implements BaseJob {

    @Autowired
    private EmailEonBiz emailEonBiz;

    @Override
    public void run(String[] params) {

        emailEonBiz.runEmailEon();
    }
}
