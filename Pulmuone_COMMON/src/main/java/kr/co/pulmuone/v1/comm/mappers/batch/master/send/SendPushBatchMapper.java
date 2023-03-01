package kr.co.pulmuone.v1.comm.mappers.batch.master.send;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendDeviceInfoVo;
import kr.co.pulmuone.v1.batch.send.push.dto.vo.SendManualPushVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SendPushBatchMapper {

    List<SendManualPushVo> getManualPush();

    void putManualPush(@Param("sendYn") String sendYn, @Param("snManualPushId") Long snManualPushId);

    Page<SendDeviceInfoVo> getDeviceInfo(@Param("snManualPushId") Long snManualPushId, @Param("osType") String osType);

    void putPushSend(@Param("snPushSendIdList") List<Long> snPushSendIdList);

}
