package kr.co.pulmuone.mall.customer.notice.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;

public interface CustomerNoticeMallService {

	ApiResult<?> getNoticeListByUser(GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto) throws Exception;

	ApiResult<?> getNoticeByUser(Long csNoticeId) throws Exception;

}
