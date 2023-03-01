package kr.co.pulmuone.v1.batch.goods.etc;

import kr.co.pulmuone.v1.goods.goods.dto.vo.FlyerGoodsVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FlyerGoodsBatchBizImpl implements FlyerGoodsBatchBiz {

    private final FlyerGoodsBatchService flyerGoodsBatchService;

    private static final String NORMAL = "NORMAL";
    private static final String EMPLOYEE = "EMPLOYEE";

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runFlyerGoods() {
        // 1. 익일 00:00 ~ 09:00 까지 전단행사가 비노출되는 경우에 노출시킬 상품리스트를 조회한다.
        // 실행시간 - 23:40   / 익일 00:00 기준 할인율 검색
        // 2. 익일부터 적용되는 상품가격 조회. 일반 할인율 우선 , 임직원 할인율 우선 각 20개씩 조회 및 저장한다.
        // - 일반 : 일반할인율 순 20개
        // - 임직원 : 임직원할인율 순 20개

        // 집계
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();  // 오늘날짜
        String execTime = localDate.plusDays(1).format(dateTimeFormatter);  // 익일 00:00

        // 회원할인순 상품 조회
        List<FlyerGoodsVo> getNormalGoodsList = flyerGoodsBatchService.getFlyerGoodsList(execTime, NORMAL);

        // 임직원할인순 상품 조회
        List<FlyerGoodsVo> getEmployeeGoodsList = flyerGoodsBatchService.getFlyerGoodsList(execTime, EMPLOYEE);

        // 위 데이터 추가
        List<FlyerGoodsVo> flyerGoodsList = new ArrayList<>();
        flyerGoodsList.addAll(getNormalGoodsList);
        flyerGoodsList.addAll(getEmployeeGoodsList);

        // 테이블 삭제 (IL_GOODS_FLYER)
        flyerGoodsBatchService.deleteFlyerGoods();

        // 상품 추가
        flyerGoodsBatchService.addFlyerGoodsList(flyerGoodsList);
    }
}
