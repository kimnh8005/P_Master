package kr.co.pulmuone.v1.user.device.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDeviceBizImpl implements UserDeviceBiz {

    @Autowired
    private UserDeviceService userDeviceService;

    public int putMemberMapping(String deviceId, String urUserId) throws Exception {

        return userDeviceService.putMemberMapping(deviceId, urUserId);
    }
}
