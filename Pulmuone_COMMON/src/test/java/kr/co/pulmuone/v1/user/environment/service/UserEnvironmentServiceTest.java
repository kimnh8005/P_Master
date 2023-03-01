package kr.co.pulmuone.v1.user.environment.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserEnvironmentServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserEnvironmentService userEnvironmentService;

    @Test
    void addPCID() {

        try {
            userEnvironmentService.addPCID("test", "test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}