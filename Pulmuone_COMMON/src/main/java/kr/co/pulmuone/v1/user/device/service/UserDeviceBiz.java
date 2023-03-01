package kr.co.pulmuone.v1.user.device.service;

public interface UserDeviceBiz {

    int putMemberMapping(String deviceId, String urUserId) throws Exception;
}
