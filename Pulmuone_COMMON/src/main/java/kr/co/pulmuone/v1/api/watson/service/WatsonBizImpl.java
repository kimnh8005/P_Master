package kr.co.pulmuone.v1.api.watson.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.CsEcsCodeVo;


/**
* <PRE>
* Forbiz Korea
* watson BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 12. 16.		천혜현          최초작성
* =======================================================================
* </PRE>
*/
@Slf4j
@Service
public class WatsonBizImpl implements WatsonBiz {

    @Autowired
    WatsonService watsonService;

    /**
     * @Desc watson classifier_id 호출 API
     * @param question
     * @param goodsName
     * @return CsEcsCodeVo
     */
    @Override
    public CsEcsCodeVo getClassifierIdCall(String question, String goodsName) throws Exception{
        CsEcsCodeVo csEcsCodeVo = new CsEcsCodeVo();
        try{
            csEcsCodeVo = watsonService.getClassifierIdCall(question, goodsName);
        }catch(Exception e){
            log.error("Watson API Call Error == "+ e.getMessage());
        }
        return csEcsCodeVo;
    }

}
