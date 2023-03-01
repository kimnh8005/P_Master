package kr.co.pulmuone.v1.user.environment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserEnvironmentBizImpl implements UserEnvironmentBiz {

    @Autowired
    private UserEnvironmentService userEnvironmentService;

    public void addPCID(String urPcidCd, String agent) throws Exception {

        userEnvironmentService.addPCID(urPcidCd, agent);
    }
}
