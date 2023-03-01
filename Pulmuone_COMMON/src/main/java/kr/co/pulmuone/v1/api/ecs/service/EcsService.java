package kr.co.pulmuone.v1.api.ecs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.comm.mapper.customer.qna.CustomerQnaMapper;
import kr.co.pulmuone.v1.comm.mappers.slaveEcs.EcsMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* ECS 연동 service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 11    천혜현          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class EcsService {

	@Autowired
	EcsMapper ecsMapper;

	@Autowired
	CustomerQnaMapper customerQnaMapper;

    /**
     * @Desc ECS 문의 등록
     * @param qnaToEcsVo
     */
    protected int addQnaToEcs(QnaToEcsVo qnaToEcsVo) {
    	return ecsMapper.addQnaToEcs(qnaToEcsVo);
    }


    /**
     * @Desc ECS 문의 수정
     * @param qnaToEcsVo
     */
    protected int putQnaToEcs(QnaToEcsVo qnaToEcsVo) {
    	return ecsMapper.putQnaToEcs(qnaToEcsVo);
    }


    /**
     * @Desc ECS 문의 분류목록 조회
     * @param hdBcode
     * @param hdScode
     * @return List<HashMap<String,String>>
     */
    protected List<HashMap<String,String>> getEcsCodeList(String hdBcode, String hdScode) {
    	List<HashMap<String,String>> result = new ArrayList<>();

    	//상담대분류 조회
    	if(StringUtil.isEmpty(hdBcode) && StringUtil.isEmpty(hdScode)) {
    		result = customerQnaMapper.getEcsHdBcodeList();
    	//상담중분류 조회
    	}else if(StringUtil.isNotEmpty(hdBcode) && StringUtil.isEmpty(hdScode)) {
    		result = customerQnaMapper.getEcsHdScodeList(hdBcode);
    	//상담소분류 조회
    	}else if(StringUtil.isNotEmpty(hdBcode) && StringUtil.isNotEmpty(hdScode)){
    		result = customerQnaMapper.getEcsClaimGubunList(hdBcode,hdScode);
    	}

    	return result;
    }

	/**
	 * @Desc ECS boardSeq 생성
	 * @param qnaType
	 */
    protected String getEcsBoardSeq(String qnaType, Long urUserId, Long csQnaId, Long ilGoodsId) throws Exception{
		//1:1문의  boardSeq
		String boardSeq = "c_" + csQnaId;

		//상품문의 boardSeq
		if(qnaType.equals(QnaEnums.QnaType.PRODUCT.getCode()) && ilGoodsId != null) {
			boardSeq += "_" + ilGoodsId;
		}

		//외부몰문의
		if(qnaType.equals(QnaEnums.QnaType.OUTMALL.getCode()) && ilGoodsId == null) {
			boardSeq = "ez_" + csQnaId;
		}
		
		return boardSeq;
	}

	/**
	 * @Desc ECS 문의 답변 업데이트
	 * @param qnaBosRequestDto
	 * @Param qnaBosDetailResultVo
	 */
    protected void putQnaAnswerToEcs(QnaBosRequestDto qnaBosRequestDto, QnaBosDetailVo qnaBosDetailResultVo) throws Exception{
		String boardSeq = getEcsBoardSeq(QnaEnums.QnaType.ONETOONE.getCode(), Long.parseLong(qnaBosDetailResultVo.getUrUserId()), Long.parseLong(qnaBosDetailResultVo.getCsQnaId()), null);
		String reply = StringUtil.htmlSingToText("내용:" + qnaBosRequestDto.getContent() + "\n답변자:"+ qnaBosRequestDto.getUserVo().getUserId());
		// 상품문의
		if(QnaEnums.QnaType.PRODUCT.getCode().equals(qnaBosDetailResultVo.getQnaType())) {
			boardSeq = getEcsBoardSeq(QnaEnums.QnaType.PRODUCT.getCode(), Long.parseLong(qnaBosDetailResultVo.getUrUserId()), Long.parseLong(qnaBosDetailResultVo.getCsQnaId()), Long.parseLong(qnaBosDetailResultVo.getIlGoodsId()));
		}
		// 외부몰문의
		if(QnaEnums.QnaType.OUTMALL.getCode().equals(qnaBosDetailResultVo.getQnaType())) {
			boardSeq = getEcsBoardSeq(QnaEnums.QnaType.OUTMALL.getCode(), null, Long.valueOf(qnaBosDetailResultVo.getCsOutmallQnaSeq()), null);
		}
		QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
				.receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
				.boardSeq(boardSeq)
				.reply(reply)
				.counseler(qnaBosRequestDto.getUserVo().getUserId())
				.hdBcode(qnaBosRequestDto.getEcsCtgryStd1())
				.hdScode(qnaBosRequestDto.getEcsCtgryStd2())
				.claimGubun(qnaBosRequestDto.getEcsCtgryStd3())
				.build();
		putQnaToEcs(qnaToEcsVo);
	}


    /**
     * @Desc ECS 문의-답변 등록
     * @param qnaToEcsVo
     */
    protected int addQnaAnswerToEcs(QnaToEcsVo qnaToEcsVo) {
        return ecsMapper.addQnaAnswerToEcs(qnaToEcsVo);
    }
}
