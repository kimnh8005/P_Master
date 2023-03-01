package kr.co.pulmuone.v1.comm.mapper.customer.qna;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OutmallQnaMapper {

	Page<QnaBosListVo> getOutmallQnaList(QnaBosRequestDto qnaBosRequestDto) throws Exception;

	QnaBosDetailVo getOutmallQnaDetail(QnaBosRequestDto qnaBosRequestDto) throws Exception;

	int putOutmallQnaAnswerStatus(QnaBosRequestDto qnaBosRequestDto);

	int putOutmallQnaInfo(QnaBosRequestDto qnaBosRequestDto);

	int addOutmallQnaAnswer(QnaBosRequestDto qnaBosRequestDto);

	QnaBosDetailVo getOutmallQnaAnswerInfo(String csOutMallQnaId);

    List<GetCodeListResultVo> getOutmallNameList();

	int putOutmallQnaAnswer(QnaBosRequestDto qnaBosRequestDto);
}
