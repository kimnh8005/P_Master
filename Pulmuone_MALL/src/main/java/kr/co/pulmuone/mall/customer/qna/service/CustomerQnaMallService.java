package kr.co.pulmuone.mall.customer.qna.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.qna.dto.OnetooneQnaByUserRequestDto;

public interface CustomerQnaMallService {

	ApiResult<?> getQnaInfoByCustomer() throws Exception;

	ApiResult<?> addQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception;

	ApiResult<?> putQnaByUser(OnetooneQnaByUserRequestDto onetooneQnaByUserRequestDto) throws Exception;

	ApiResult<?> getQnaDetailByUser(Long csQnaId) throws Exception;

	ApiResult<?> getOrderInfoPopupByQna(String searchPeriod) throws Exception;

}
