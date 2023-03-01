package kr.co.pulmuone.v1.api.ncp.service;

import com.google.gson.JsonObject;
import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NCPApiSmsBizImpl implements NCPApiSmsBiz {

    @Autowired
    private NCPApiSmsService ncpApiSmsService;

    @Override
    public JsonObject getJSONObject(List<NcpSmsRequestDto> smsInfoList, SendEnums.SendNcpSmsType smsType) {
        return ncpApiSmsService.getJSONObject(smsInfoList, smsType);
    }

    @Override
    public String sendMessage(JsonObject ncpMessage) throws Exception {
        return ncpApiSmsService.sendMessage(ncpMessage);
    }
}
