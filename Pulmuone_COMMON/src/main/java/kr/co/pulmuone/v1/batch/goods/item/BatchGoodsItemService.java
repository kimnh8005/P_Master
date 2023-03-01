package kr.co.pulmuone.v1.batch.goods.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.item.BatchGoodsItemMapper;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * ERP API 조회 후 ERP 연동 품목 정보 일괄 업데이트 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 09.               박주형         최초작성
* =======================================================================
 * </PRE>
 */

@Service
@RequiredArgsConstructor
public class BatchGoodsItemService {

    @Autowired
    private final BatchGoodsItemMapper batchGoodsItemMapper;

    /**
     * @desc ERP 연동 품목의 품목정보 수정
     *
     * @param MasterItemVo : 마스터 품목 Vo
     *
     * @return int
     */
    protected int modifyErpItem(MasterItemVo masterItemVo) {

        return batchGoodsItemMapper.modifyErpItem(masterItemVo);

    }

    protected MasterItemVo getItemByItemCd(String ilItemCode) {
        return batchGoodsItemMapper.getItemByItemCd(ilItemCode);
    }

    protected String getPoTpIdByErpPoTp(String erpPoTp) {
        return batchGoodsItemMapper.getPoTpIdByErpPoTp(erpPoTp);
    }

    protected int updatePoInfo(String erpPoTp, String ilPoTpId, String preOrderYn, String preOrderTp) {
        return batchGoodsItemMapper.updatePoInfo(erpPoTp, ilPoTpId, preOrderYn, preOrderTp);
    }

}
