package kr.co.pulmuone.batch.eon.domain.service.email.eon;

import kr.co.pulmuone.batch.eon.domain.model.email.eon.dto.vo.EmailBosVo;
import kr.co.pulmuone.batch.eon.infra.mapper.email.master.EmailBosMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailEonService {
    @Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private final SqlSessionTemplate masterSqlSession;

    @Autowired
    @Qualifier("slaveEonSqlSessionTemplate")
    private final SqlSessionTemplate eonSqlSession;

    @Value("${eon.sender.mail}")
    private String senderMail;

    @Value("${eon.sender.nm}")
    private String senderNm;

    @Value("${eon.return.mail}")
    private String returnMail;

    protected void runEmailEon() {

        EmailBosMapper emailBosMapper = masterSqlSession.getMapper(EmailBosMapper.class);

        List<EmailBosVo> emailBosVos = emailBosMapper.getList(senderMail, senderNm, returnMail);

        insertEonMail(emailBosVos);

        updateBosMail(emailBosMapper, emailBosVos);
    }


    protected void insertEonMail(List<EmailBosVo> emailBosVos) {
        IntStream.range(0, emailBosVos.size()).forEach(i -> {
            eonSqlSession.insert("kr.co.pulmuone.batch.eon.infra.mapper.email.slaveEon.EmailEonMapper.insertForm", emailBosVos.get(i));
            eonSqlSession.flushStatements();
            eonSqlSession.insert("kr.co.pulmuone.batch.eon.infra.mapper.email.slaveEon.EmailEonMapper.insertList", emailBosVos.get(i));
            eonSqlSession.update("kr.co.pulmuone.batch.eon.infra.mapper.email.slaveEon.EmailEonMapper.updateForm", emailBosVos.get(i));

            log.info("========= count-{} =========\n", i);
        });
    }

    protected void updateBosMail(EmailBosMapper emailBosMapper, List<EmailBosVo> emailBosVos) {

        for (EmailBosVo vo : emailBosVos) {
            emailBosMapper.update(vo);
        }
    }
}
