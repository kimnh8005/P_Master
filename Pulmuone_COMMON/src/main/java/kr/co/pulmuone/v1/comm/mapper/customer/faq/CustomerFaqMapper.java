package kr.co.pulmuone.v1.comm.mapper.customer.faq;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosDetailVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosListVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.GetFaqListByUserResultVo;

@Mapper
public interface CustomerFaqMapper {

	Page<GetFaqListByUserResultVo> getFaqListByUser(GetFaqListByUserRequsetDto getFaqListByUserRequsetDto) throws Exception;

	int addFaqViews(Long csFaqId) throws Exception;

	Page<FaqBosListVo> getFaqList(FaqBosRequestDto faqBosRequestDto) throws Exception;

	int addFaqInfo(FaqBosRequestDto faqBosRequestDto);

	int putFaqInfo(FaqBosRequestDto faqBosRequestDto);

	int deleteFaqInfo(FaqBosRequestDto faqBosRequestDto);

	FaqBosDetailVo getDetailFaq (FaqBosRequestDto faqBosRequestDto) throws Exception;


}
