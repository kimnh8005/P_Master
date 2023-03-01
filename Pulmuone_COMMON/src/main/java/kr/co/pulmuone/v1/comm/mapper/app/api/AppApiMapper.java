package kr.co.pulmuone.v1.comm.mapper.app.api;

import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AppApiMapper {

    void deletePushToken(@Param("deviceId") String deviceId);

    void insertPushToken(PushDeviceVo pushDeviceVo);

    void updateOpenYn(@Param("deviceId") String deviceId, @Param("pushId") int pushId);

    PushDeviceVo findBySettingInfo(@Param("deviceId") String deviceId);

    void updatePushAllowed(@Param("deviceId") String deviceId, @Param("pushAllowed") String pushAllowed);

    void updateNightAllowed(@Param("deviceId") String deviceId, @Param("nightAllowed") String nightAllowed);
}
