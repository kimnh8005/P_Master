package app.api;

import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import kr.co.pulmuone.v1.comm.mapper.app.api.AppApiMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class AppApiServiceUnitTest {

    @Autowired
    AppApiMapper appApiMapper;

    @Test
    @Commit
    void insertPushToken() {
        PushDeviceVo vo = new PushDeviceVo();
        vo.setOsType("i");
        vo.setPushKey("@@e8IokTE2rxA:APA91bH0MKNO3eZS7A3-0hjpFyPQNbfseCBnH0pTzGIgtZXg4Y4Y4yuuLbZxTBKOtX7HHxIi0D7ZxsxbMG1rqTZ_5u6ETtuRMo4PVqrtG6329BnXWX2Rf8bAIr-M2VP50ACb3L");
        vo.setDeviceId("411111");
        vo.setPushAllowed("Y");
        appApiMapper.insertPushToken(vo);
    }
}
