package kr.co.pulmuone.v1.batch.goods.etc;

import kr.co.pulmuone.v1.goods.goods.dto.vo.FlyerGoodsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import kr.co.pulmuone.v1.comm.mappers.batch.master.goods.FlyerGoodsBatchMapper;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlyerGoodsBatchService {
    private final FlyerGoodsBatchMapper flyerGoodsBatchMapper;

    /**
     *  전단행사 미노출시 노출할 상품 리스트 조회(할인율 순 20개)
     */
    protected List<FlyerGoodsVo> getFlyerGoodsList(String execTime, String priceType) {
        return flyerGoodsBatchMapper.getFlyerGoodsList(execTime, priceType);
    }

    /**
     *  전단행사 대신 보여줄 상품데이터 삭제
     */
    protected int deleteFlyerGoods() {
        return flyerGoodsBatchMapper.deleteFlyerGoods();
    }

    /**
     *  전단행사 대체 상품데이터 추가
     */
    protected void addFlyerGoodsList(List<FlyerGoodsVo> flyerGoodsList) {
        flyerGoodsBatchMapper.addFlyerGoodsList(flyerGoodsList);
    }
}
