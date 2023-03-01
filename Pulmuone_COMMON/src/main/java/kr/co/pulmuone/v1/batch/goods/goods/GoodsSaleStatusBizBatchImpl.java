package kr.co.pulmuone.v1.batch.goods.goods;

import kr.co.pulmuone.v1.batch.goods.goods.dto.GoodsSaleStatusBatchRequestDto;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsChangeLogVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class GoodsSaleStatusBizBatchImpl implements GoodsSaleStatusBizBatch {

    @Autowired
    private final GoodsSaleStatusService goodsSaleStatusService;

    /**
     * 판매중지 D+30 > 영구판매중지
     * @param
     * @return void
     * @throws BaseException
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
    public void runGoodsSaleStatusSetUp() {
        try {
            List<Long> goodsIdList = goodsSaleStatusService.getGoodsIdListByGoodsBatch(
                    GoodsSaleStatusBatchRequestDto.builder()
                            .saleStatus(GoodsEnums.SaleStatus.STOP_SALE.getCode())
                            .goodsType(GoodsEnums.GoodsType.PACKAGE.getCode())  // package 상품 제외
                            .searchDate(LocalDate.now().plusDays(-30))  // SALE_STATUS.STOP_SALE D+30
                            .build()
            );

            if(goodsIdList != null && !goodsIdList.isEmpty()) {

                // 묶음 구성 상품이 '영구판매중지' 가 되면, 해당 묶음 상품도 '영구판매중지'
                List<Long> packageGoodsIdList = goodsSaleStatusService.getPackageGoodsIdListByGoodsBatch(goodsIdList);
                goodsIdList.addAll(packageGoodsIdList);

                goodsSaleStatusService.updateGoodsSaleStatusByGoodsIds(goodsIdList);

                // CHANGE_LOG
                goodsIdList.forEach(id -> {
                    goodsSaleStatusService.addGoodsChangeLog(GoodsChangeLogVo.builder()
                            .ilGoodsId(id.toString())
                            .tableNm("IL_GOODS")
                            .tableIdOrig(id.toString())
                            .tableIdNew(id.toString())
                            .beforeData(GoodsEnums.SaleStatus.STOP_SALE.getCode())
                            .afterData(GoodsEnums.SaleStatus.STOP_PERMANENT_SALE.getCode())
                            .columnNm(GoodsEnums.GoodsColumnComment.SALE_STATUS.getCode())
                            .columnLabel(GoodsEnums.GoodsColumnComment.SALE_STATUS.getCodeName())
                            .createId("1")
                            .createDate(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"))
                            .build());
                });
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
