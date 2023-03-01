package kr.co.pulmuone.v1.batch.shopping.recently;

import kr.co.pulmuone.v1.batch.policy.config.PolicyConfigBatchBiz;
import kr.co.pulmuone.v1.comm.enums.PolicyEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.shopping.ShoppingRecentlyBatchMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShoppingRecentlyBatchService {

    private final PolicyConfigBatchBiz policyConfigBatchBiz;

    private final ShoppingRecentlyBatchMapper shoppingRecentlyBatchMapper;

    /**
     * 최근 본 상품 정리
     */
    public void runShoppingRecentlyClear() {

        //1. 특정기간동안 미접속 비회원 데이터
        clearNonMemberRecently();

        //2. 상품정보 조회불가인 상품 리스트
        clearDoNotSearchGoodsRecently();

    }

    /**
     * 특정기간동안 미접속 비회원 데이터 삭제
     */
    protected void clearNonMemberRecently() {
        // 삭제기간 검색
        String cartMaintenancePeriod = policyConfigBatchBiz.getConfigValue(PolicyEnums.PolicyKey.OD_CART_MAINTENANCE_PERIOD.getCode());
        if (!StringUtil.isNumeric(cartMaintenancePeriod)) return;

        // 삭제 대상 조회
        List<Long> recentlyIdResponseList = shoppingRecentlyBatchMapper.getNonMemberRecently(Integer.parseInt(cartMaintenancePeriod));
        if (recentlyIdResponseList == null || recentlyIdResponseList.size() == 0) return;

        // 삭제 진행
        List<Long> recentlyIdList = new ArrayList<>();
        int limitCount = 1000;
        int batchCount = 0;
        for (Long id : recentlyIdResponseList) {
            recentlyIdList.add(id);
            batchCount++;
            if (batchCount == limitCount) {
                shoppingRecentlyBatchMapper.delRecentlyByIdList(recentlyIdList);
                batchCount = 0;
                recentlyIdList.clear();
            }
        }
        if (recentlyIdList.size() > 0) {
            shoppingRecentlyBatchMapper.delRecentlyByIdList(recentlyIdList);
        }
    }

    /**
     * 상품정보 조회불가인 상품 리스트 삭제
     */
    protected void clearDoNotSearchGoodsRecently() {
        // 삭제 대상 조회
        List<Long> goodsIdResponseList = shoppingRecentlyBatchMapper.getDoNotSearchGoodsId();
        if (goodsIdResponseList == null || goodsIdResponseList.size() == 0) return;

        // 삭제 진행
        List<Long> goodsIdList = new ArrayList<>();
        int limitCount = 1000;
        int batchCount = 0;
        for (Long id : goodsIdResponseList) {
            goodsIdList.add(id);
            batchCount++;
            if (batchCount == limitCount) {
                shoppingRecentlyBatchMapper.delRecentlyByGoodsIdList(goodsIdList);
                batchCount = 0;
                goodsIdList.clear();
            }
        }
        if (goodsIdList.size() > 0) {
            shoppingRecentlyBatchMapper.delRecentlyByGoodsIdList(goodsIdList);
        }
    }

}
