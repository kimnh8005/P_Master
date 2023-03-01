package kr.co.pulmuone.v1.user.device.service;

import kr.co.pulmuone.v1.comm.mapper.user.device.UserDeviceMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserDeviceService {

    @Autowired
    private UserDeviceMapper userDeviceMapper;

    protected int putMemberMapping(String deviceId, String urUserId) throws Exception {

        return userDeviceMapper.putMemberMapping(deviceId, urUserId);
    }
}
