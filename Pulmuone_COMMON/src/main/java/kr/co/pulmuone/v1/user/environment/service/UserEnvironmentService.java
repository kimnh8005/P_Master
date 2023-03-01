package kr.co.pulmuone.v1.user.environment.service;

import kr.co.pulmuone.v1.comm.mapper.user.environment.UserEnvironmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserEnvironmentService {

    @Autowired
    private UserEnvironmentMapper userEnvironmentMapper;

    /**
     * PCID 추가
     * @param	agent
     * @return
     * @throws Exception
     */
    protected void addPCID(String urPcidCd, String agent) throws Exception{
        userEnvironmentMapper.addPCID(urPcidCd, agent);
    }
}