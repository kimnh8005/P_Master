package kr.co.pulmuone.v1.api.ecs.service;

import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;
import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.api.watson.service.WatsonBiz;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.QnaEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.system.code.dto.vo.GetCodeListResultVo;
import kr.co.pulmuone.v1.system.code.service.SystemCodeBiz;


/**
* <PRE>
* Forbiz Korea
* ECS 연동 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 11     천혜현          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class EcsBizImpl implements EcsBiz {

    @Autowired
    EcsService ecsService;

    @Autowired
    SystemCodeBiz systemCodeBiz;

    @Autowired
    WatsonBiz watsonBiz;

    /**
     * @Desc ECS boardSeq 생성
     * @param qnaType
     */
    @Override
    public String getEcsBoardSeq(String qnaType, Long urUserId, Long csQnaId, Long ilGoodsId) throws Exception{
        return ecsService.getEcsBoardSeq(qnaType, urUserId, csQnaId, ilGoodsId);
    }


    /**
     * @Desc ECS 문의 등록
     * @param qnaToEcsVo
     */
    @Override
    public int addQnaToEcs(QnaToEcsVo qnaToEcsVo) throws Exception{
        return ecsService.addQnaToEcs(qnaToEcsVo);
    }

    /**
     * @Desc ECS 문의 수정
     * @param qnaToEcsVo
     */
    @Override
    public int putQnaToEcs(QnaToEcsVo qnaToEcsVo) throws Exception{
        return ecsService.putQnaToEcs(qnaToEcsVo);
    }

    /**
     * @Desc ECS 문의 분류값 watson_VOC API 조회
     * @param qnaType
     * @return CsEcsCodeVo
     */
    @Override
    public CsEcsCodeVo getEcsCode(String qnaType, String question, String goodsName) throws Exception{
    	CsEcsCodeVo csEcsCode = new CsEcsCodeVo();

    	// watson_VOC 자동분류 API 호출
    	csEcsCode = watsonBiz.getClassifierIdCall(question,goodsName);

    	if(StringUtil.isEmpty(csEcsCode.getHdBcode())) {
    		GetCodeListResultVo getCodeResult = systemCodeBiz.getCode(qnaType);
			csEcsCode.setHdBcode(getCodeResult.getAttribute1());
			csEcsCode.setHdScode(getCodeResult.getAttribute2());
			csEcsCode.setClaimGubun(getCodeResult.getAttribute3());
    	}

    	return csEcsCode;
    }


    /**
     * @Desc ECS 문의 분류목록 조회
     * @param hdBcode
     * @Param hdScode
     * @return List<HashMap<String,String>>>
     */
    @Override
    public List<HashMap<String,String>> getEcsCodeList(String hdBcode, String hdScode) throws Exception{
    	return ecsService.getEcsCodeList(hdBcode,hdScode);
    }

    /**
     * @Desc ECS 문의 답변 업데이트
     * @param qnaBosRequestDto
     * @Param qnaBosDetailResultVo
     */
    @Override
    public void putQnaAnswerToEcs(QnaBosRequestDto qnaBosRequestDto, QnaBosDetailVo qnaBosDetailResultVo) throws Exception{
        ecsService.putQnaAnswerToEcs(qnaBosRequestDto, qnaBosDetailResultVo);
    }

    /**
     * @Desc ECS 문의-답변 등록
     * @param qnaToEcsVo
     */
    @Override
    public int addQnaAnswerToEcs(QnaToEcsVo qnaToEcsVo) throws Exception{
        return ecsService.addQnaAnswerToEcs(qnaToEcsVo);
    }


}
