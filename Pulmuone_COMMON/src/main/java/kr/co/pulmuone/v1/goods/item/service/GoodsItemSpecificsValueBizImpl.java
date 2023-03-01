package kr.co.pulmuone.v1.goods.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecificsValueVo;


/**
* <PRE>
* Forbiz Korea
* 품목별 상품정보제공고시
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 07. 17.               박영후          최초작성
*  1.0    2020. 10. 12.               손진구          NEW 변경
* =======================================================================
* </PRE>
*/
@Service
public class GoodsItemSpecificsValueBizImpl  implements GoodsItemSpecificsValueBiz {

    @Autowired
    GoodsItemSpecificsValueService goodsItemSpecificsValueService;

    /**
     * @Desc 품목별 상품정보제공고시 값 삭제
     * @param itemSpecificsValueVo
     * @return int
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public int delItemSpecificsValue(ItemSpecificsValueVo itemSpecificsValueVo) throws Exception{
        return goodsItemSpecificsValueService.delItemSpecificsValue(itemSpecificsValueVo);
    }
}
