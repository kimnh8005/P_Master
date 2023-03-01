package kr.co.pulmuone.batch.eon.domain.service.email.eon;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailEonBizImpl implements EmailEonBiz {

  @Autowired
  private EmailEonService emailEonService;

  @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
  public void runEmailEon() {
    emailEonService.runEmailEon();
  }

}
