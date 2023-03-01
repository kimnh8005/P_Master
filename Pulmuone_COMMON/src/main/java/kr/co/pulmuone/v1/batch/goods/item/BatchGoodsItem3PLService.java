package kr.co.pulmuone.v1.batch.goods.item;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.goods.item.vo.ErpBaekamGoodsItemResultVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item.BatchGoodsItem3PLMapper;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 *
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 02.08    정형진         최초작성
* =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class BatchGoodsItem3PLService {

    @Autowired
    private BatchGoodsItem3PLMapper batchGoodsItem3PLMapper;

	/**
     * @Desc 품목정보 - 연동 , 백암물류 품목, 신규 데이터
     * @param
     * @return List<ItemErpStock3PlSearchResultVo>
     */
	protected List<ErpBaekamGoodsItemResultVo> getInsertTargetItemList(){
		return batchGoodsItem3PLMapper.getInsertTargetItemList();
	}

	/**
     * @Desc 품목정보 - 연동 , 백암물류 품목, 수정 데이터
     * @param
     * @return List<ItemErpStock3PlSearchResultVo>
     */
	protected List<ErpBaekamGoodsItemResultVo> getUpdateTargetItemList(){
		return batchGoodsItem3PLMapper.getUpdateTargetItemList();
	}


}
