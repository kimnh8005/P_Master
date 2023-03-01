package kr.co.pulmuone.v1.api.ncp.service;

import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.comm.enums.SendEnums;

import java.util.List;

public interface NCPApiSmsBiz {

    JsonObject getJSONObject(List<NcpSmsRequestDto> smsInfoList, SendEnums.SendNcpSmsType smsType);

    String sendMessage(JsonObject ncpMessage) throws Exception;

}
