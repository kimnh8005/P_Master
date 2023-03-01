package kr.co.pulmuone.v1.batch.legacysync.purchase;

import kr.co.pulmuone.v1.batch.legacysync.purchase.dto.CreateLegacyPurchaseOrderDto;
import kr.co.pulmuone.v1.batch.legacysync.purchase.dto.vo.PurchaseOrderVo;
import kr.co.pulmuone.v1.comm.enums.StockEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.legacysync.LegacySyncBatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegacySyncPurchaseBatchService {

    @Autowired
    private LegacySyncBatchMapper legacySyncBatchMapper;

    /**
     * 통합몰 발주 > 풀무원샵 발주 정보를 전달하기 위한 I/F 서비스
     *
     */
    protected int purchaseOrderInterface(List<String> erpPoTypeList) {
        int result = 0;

        List<PurchaseOrderVo> purchaseOrderList = legacySyncBatchMapper.getPurchaseOrderList(erpPoTypeList);

        for (PurchaseOrderVo purchaseOrderVo : purchaseOrderList){
            // 풀무원샵 출고처 정보 - 용인: 61, 백암: 201
            CreateLegacyPurchaseOrderDto createLegacyPurchaseOrderDto = CreateLegacyPurchaseOrderDto.builder()
                    .poDt(purchaseOrderVo.getBaseDt())
                    .pdSeq(StockEnums.UrWarehouseId.WAREHOUSE_YONGIN_ID.getCode()
                                    .equals(purchaseOrderVo.getWarehouseCd()) ?  61 : 201)
                    .itemCd(purchaseOrderVo.getItemCd())
                    .goodsNm(purchaseOrderVo.getItemName())
                    .orderDt(purchaseOrderVo.getPoIfQty())
                    .addOrderDt(0)
                    .requestDate(purchaseOrderVo.getReqDt())
                    .poTime(purchaseOrderVo.getPoTime())
                    .foodmerceResponseDate(purchaseOrderVo.getFoodmerceResponseDate())
                    .build();

            result += legacySyncBatchMapper.insertLegacyPurchaseOrder(createLegacyPurchaseOrderDto);
        }

        return result;
    }
}
