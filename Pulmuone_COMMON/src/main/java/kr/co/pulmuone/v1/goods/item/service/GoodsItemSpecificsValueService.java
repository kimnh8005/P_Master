package kr.co.pulmuone.v1.goods.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemSpecificsValueMapper;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemSpecificsValueVo;
import lombok.RequiredArgsConstructor;


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
@RequiredArgsConstructor
public class GoodsItemSpecificsValueService {

    @Autowired
    private GoodsItemSpecificsValueMapper goodsItemSpecificsValueMapper;

    /**
     * @Desc 품목별 상품정보제공고시 값 삭제
     * @param itemSpecificsValueVo
     * @return int
     */
    protected int delItemSpecificsValue(ItemSpecificsValueVo itemSpecificsValueVo) {
        return goodsItemSpecificsValueMapper.delItemSpecificsValue(itemSpecificsValueVo);
    }
}
