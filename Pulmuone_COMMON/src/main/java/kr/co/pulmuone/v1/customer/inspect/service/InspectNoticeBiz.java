package kr.co.pulmuone.v1.customer.inspect.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.inspect.dto.InspectNoticeRequestDto;

public interface InspectNoticeBiz {

    ApiResult<?> getInspectNotice() throws Exception;
    ApiResult<?> setInspectNotice(InspectNoticeRequestDto requestDto) throws Exception;
    boolean checkInspectAllowed(String ipAddress) throws Exception;

}
