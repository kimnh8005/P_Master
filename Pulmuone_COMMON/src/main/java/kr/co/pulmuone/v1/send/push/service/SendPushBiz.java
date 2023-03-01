package kr.co.pulmuone.v1.send.push.service;

import kr.co.pulmuone.v1.send.push.dto.*;
import org.springframework.stereotype.Service;

public interface SendPushBiz {

    GetPushSendListResponseDto getPushSendList(GetPushSendListRequestDto dto);

    AddPushIssueSelectResponseDto addPushIssueSelect(AddPushIssueSelectRequestDto dto);

    AddPushIssueSearchResponseDto addPushIssueSearch(AddPushIssueSearchRequestDto dto) throws Exception;

    AddPushIssueAllResponseDto addPushIssueAll(AddPushIssueAllRequestDto dto);
}
