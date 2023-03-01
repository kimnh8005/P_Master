package kr.co.pulmuone.v1.api.ecs.service;

import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;
import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;

public interface EcsBiz {

	String getEcsBoardSeq(String qnaType, Long urUserId, Long csQnaId, Long ilGoodsId) throws Exception;

	int addQnaToEcs(QnaToEcsVo QnaToEcsVo) throws Exception;

	int putQnaToEcs(QnaToEcsVo qnaToEcsVo) throws Exception;

	CsEcsCodeVo getEcsCode(String qnaType, String question, String goodsName) throws Exception;

	List<HashMap<String,String>> getEcsCodeList(String hdBcode, String hdScode) throws Exception;

	void putQnaAnswerToEcs(QnaBosRequestDto qnaBosRequestDto, QnaBosDetailVo qnaBosDetailResultVo) throws Exception;

	int addQnaAnswerToEcs(QnaToEcsVo QnaToEcsVo) throws Exception;
}
