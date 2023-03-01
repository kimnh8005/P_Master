package kr.co.pulmuone.v1.customer.faq.service;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosDetailResponseDto;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosResponseDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserResponseDto;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosDetailVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosListVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.GetFaqListByUserResultVo;

@Service
public class CustomerFaqBizImpl implements CustomerFaqBiz{

    @Autowired
    private CustomerFaqService customerFaqService;

	/**
	 * @Desc  FAQ 게시판 리스트 조회
	 * @param getFaqListByUserRequsetDto
	 * @throws Exception
	 */
	@Override
	public GetFaqListByUserResponseDto getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception {
		GetFaqListByUserResponseDto getFaqListByUserResponseDto = new GetFaqListByUserResponseDto();

		Page<GetFaqListByUserResultVo> faq = customerFaqService.getFaqListByUser(getFaqListByUserRequsetDto);
		getFaqListByUserResponseDto.setTotal(faq.getTotal());
		getFaqListByUserResponseDto.setFaq(faq.getResult());

		return getFaqListByUserResponseDto;
	}

	/**
	 * FAQ 게시글 조회수 증가
	 */
	@Override
	public int addFaqViews(Long csFaqId) throws Exception {
		return customerFaqService.addFaqViews(csFaqId);
	}


	 /**
     * FAQ 관리 목록조회
     *
     * @throws Exception
     */
    @Override
    public  ApiResult<?>  getFaqList(FaqBosRequestDto faqBosRequestDto) throws Exception {

    	FaqBosResponseDto result = new FaqBosResponseDto();

    	if (!faqBosRequestDto.getFindKeyword().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(faqBosRequestDto.getFindKeyword(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			faqBosRequestDto.setFindKeywordArray(array);
		}


        Page<FaqBosListVo> voList = customerFaqService.getFaqList(faqBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }


    /**
     * FAQ 신규 등록
     */
    @Override
    public ApiResult<?> addFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
        return customerFaqService.addFaqInfo(faqBosRequestDto);
    }

    /**
     * FAQ 수정
     */
    @Override
    public ApiResult<?> putFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
        return customerFaqService.putFaqInfo(faqBosRequestDto);
    }

    /**
     * FAQ 삭제
     */
    @Override
    public ApiResult<?> deleteFaqInfo(FaqBosRequestDto faqBosRequestDto) throws Exception {
        return customerFaqService.deleteFaqInfo(faqBosRequestDto);
    }

    /**
     * FAQ 관리 상세조회
     */
	@Override
    public ApiResult<?> getDetailFaq(FaqBosRequestDto faqBosRequestDto) throws Exception{
		FaqBosDetailResponseDto result = new FaqBosDetailResponseDto();
		FaqBosDetailVo vo = new FaqBosDetailVo();
		vo = customerFaqService.getDetailFaq(faqBosRequestDto);

		result.setRow(vo);

    	return ApiResult.success(result);
    }

}
