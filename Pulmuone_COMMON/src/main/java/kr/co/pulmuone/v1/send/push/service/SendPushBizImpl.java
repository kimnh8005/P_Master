package kr.co.pulmuone.v1.send.push.service;

import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.vo.GetBuyerDeviceListParamVo;
import kr.co.pulmuone.v1.send.device.service.SendDeviceBiz;
import kr.co.pulmuone.v1.send.push.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SendPushBizImpl implements SendPushBiz {

    @Autowired
    private SendPushService sendPushService;

    @Autowired
    private SendDeviceBiz sendDeviceBiz;

    @Override
    public GetPushSendListResponseDto getPushSendList(GetPushSendListRequestDto dto) {
        return sendPushService.getPushSendList(dto);
    }

    @Override
    public AddPushIssueSelectResponseDto addPushIssueSelect(AddPushIssueSelectRequestDto dto) {
        return sendPushService.addPushIssueSelect(dto);
    }

    @Override
    public AddPushIssueSearchResponseDto addPushIssueSearch(AddPushIssueSearchRequestDto dto) throws Exception {

        if(dto != null && dto.getSearchForm() != null) {

            GetBuyerDeviceListRequestDto searchDto = new GetBuyerDeviceListRequestDto();
            searchDto.setCondiType(dto.getSearchForm().getCondiType());
            searchDto.setCondiValue(dto.getSearchForm().getCondiValue());
            searchDto.setUserLevel(dto.getSearchForm().getUserLevel());
            searchDto.setJoinDateStart(dto.getSearchForm().getJoinDateStart());
            searchDto.setJoinDateEnd(dto.getSearchForm().getJoinDateEnd());
            searchDto.setLastVisitDateStart(dto.getSearchForm().getLastVisitDateStart());
            searchDto.setLastVisitDateEnd(dto.getSearchForm().getLastVisitDateEnd());

            dto.setBuyerDeviceList(sendDeviceBiz.getBuyerDeviceSearchAllList(searchDto));

        }

            return sendPushService.addPushIssueSearch(dto);
    }

    @Override
    public AddPushIssueAllResponseDto addPushIssueAll(AddPushIssueAllRequestDto dto) {
        return sendPushService.addPushIssueAll(dto);
    }
}
