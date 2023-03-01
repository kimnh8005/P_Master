package kr.co.pulmuone.v1.comm.mapper.send.push;

import kr.co.pulmuone.v1.send.push.dto.GetPushSendListRequestDto;
import kr.co.pulmuone.v1.send.push.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import java.util.List;

@Mapper
public interface SendPushMapper {

    Page<GetPushSendListResultVo> getPushSendList(GetPushSendListRequestDto vo);

    int addPushManual(AddPushManualParamVo vo);

    int addPushIssue(AddPushIssueParamVo vo);

    List<GetSendUserDeviceListResultVo> getSendUserDeviceList(GetSendUserDeviceListParamVo vo);
}
