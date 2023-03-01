package kr.co.pulmuone.v1.customer.faq.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.mapper.customer.faq.CustomerFaqMapper;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosResponseDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosDetailVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosListVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.GetFaqListByUserResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerFaqService {

	 private final CustomerFaqMapper customerFaqMapper;

		/**
		 * @Desc FAQ 게시판 리스트 조회
		 * @param getFaqListByUserRequsetDto
		 * @throws Exception
		 * @return Page<GetFaqListByUserResponseDto>
		 */
		protected Page<GetFaqListByUserResultVo> getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception {

			//FAQ 결과 페이지
			PageMethod.startPage(getFaqListByUserRequsetDto.getPage(), getFaqListByUserRequsetDto.getLimit());
			return customerFaqMapper.getFaqListByUser(getFaqListByUserRequsetDto);
			}

		/**
		 * @param csFaqId
		 * @Desc FAQ 게시글 조회수 증가
		 * @return ApiResult<?>
		 */
		protected int addFaqViews(Long csFaqId) throws Exception {
			return customerFaqMapper.addFaqViews(csFaqId);
		}

	    /**
	     * FAQ 관리 목록조회
	     *
	     * @param FaqBosRequestDto
	     * @return Page<FeedbackBosListVo>
	     * @throws Exception Exception
	     */
	 	@UserMaskingRun(system="BOS")
	    protected Page<FaqBosListVo> getFaqList(FaqBosRequestDto faqBosRequestDto) throws Exception {
	    	PageMethod.startPage(faqBosRequestDto.getPage(), faqBosRequestDto.getPageSize());
	        return customerFaqMapper.getFaqList(faqBosRequestDto);
	    }


	 	/**
	 	 * FAQ 신규 등록
	 	 * @param dto
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	protected ApiResult<?> addFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
	 		FaqBosResponseDto result = new FaqBosResponseDto();

	 		faqBosRequestDto.setViewSort(CustomerEnums.ViewSOrt.VIEW_SORT.getCode());
	 		customerFaqMapper.addFaqInfo(faqBosRequestDto);

	        return ApiResult.success(result);
	    }


	 	/**
	 	 * FAQ 수정
	 	 * @param dto
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	protected ApiResult<?> putFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
	 		FaqBosResponseDto result = new FaqBosResponseDto();

	 		customerFaqMapper.putFaqInfo(faqBosRequestDto);

	        return ApiResult.success(result);
	    }


	 	/**
	 	 * FAQ 삭제
	 	 * @param dto
	 	 * @return
	 	 * @throws Exception
	 	 */
	 	protected ApiResult<?> deleteFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
	 		FaqBosResponseDto result = new FaqBosResponseDto();

	 		customerFaqMapper.deleteFaqInfo(faqBosRequestDto);

	        return ApiResult.success(result);
	    }



	    /**
	     * FAQ 관리 상세조회
	     * @param FeedbackBosRequestDto
	     * @return
	     * @throws Exception
	     */
	    protected FaqBosDetailVo getDetailFaq(FaqBosRequestDto faqBosRequestDto)throws Exception {
	    	return customerFaqMapper.getDetailFaq(faqBosRequestDto);
	    }

}
