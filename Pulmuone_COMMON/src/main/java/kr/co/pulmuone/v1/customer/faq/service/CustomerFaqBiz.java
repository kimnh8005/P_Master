package kr.co.pulmuone.v1.customer.faq.service;

import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;

public interface CustomerFaqBiz {

	//고객센터 FAQ 리스트 조회
	GetFaqListByUserResponseDto getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception;

	// FAQ 게시글 조회수 증가
	int addFaqViews(Long csFaqId) throws Exception;

	// FAQ관리 목록조회
	ApiResult<?> getFaqList(FaqBosRequestDto faqBosRequestDto) throws Exception;

	// FAQ 신규 등록
    ApiResult<?> addFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception;

	// FAQ 수정
    ApiResult<?> putFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception;

	// FAQ 삭제
    ApiResult<?> deleteFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception;

	// FAQ 관리 상세조회
	ApiResult<?> getDetailFaq(FaqBosRequestDto faqBosRequestDto) throws Exception;


}
