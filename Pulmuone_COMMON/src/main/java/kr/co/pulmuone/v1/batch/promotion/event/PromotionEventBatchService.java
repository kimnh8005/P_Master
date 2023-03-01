package kr.co.pulmuone.v1.batch.promotion.event;

import kr.co.pulmuone.v1.batch.order.front.OrderFrontBatchBiz;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromStampPurchaseBatchVo;
import kr.co.pulmuone.v1.batch.promotion.event.dto.EventStampPurchaseJoinRequestDto;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventStampPurchaseJoinVo;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventStampPurchaseVo;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventTimeOverVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.promotion.PromotionEventBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionEventBatchService {

    private final PromotionEventBatchMapper promotionEventBatchMapper;
    private final OrderFrontBatchBiz orderFrontBatchBiz;

    protected void runEventTimeOver() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<EventTimeOverVo> eventList = promotionEventBatchMapper.getEventTimeOver();
        for (EventTimeOverVo vo : eventList) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.parse(vo.getEndDateTime(), dateFormatter);
            // 종료일 지남 확인
            if (endDate.isBefore(now)) {
                promotionEventBatchMapper.putEventUseYn(vo.getEvEventId());
            }
        }
    }

    protected void runStampPurchaseEvent() {
        // BATCH 대상 조회
        List<EventStampPurchaseVo> stampJoinList = promotionEventBatchMapper.getStampPurchase();
        for (EventStampPurchaseVo vo : stampJoinList) {
            vo.setJoin(promotionEventBatchMapper.getStampPurchaseJoin(vo.getEvEventId()));
        }

        // 계산
        for (EventStampPurchaseVo vo : stampJoinList) {
            List<EventStampPurchaseJoinVo> joinList = vo.getJoin();
            List<Long> userList = joinList.stream()
                    .map(EventStampPurchaseJoinVo::getUrUserId)
                    .collect(Collectors.toList());

			if (userList.isEmpty()) {
				continue;
			}
			
            //주문 정보 조회
            List<OrderInfoFromStampPurchaseBatchVo> orderInfo = orderFrontBatchBiz.getOrderInfoFromStampPurchase(userList, vo.getStartDate(), vo.getEndDate(), vo.getOrderPrice());
            Map<Long, Integer> orderMaps = orderInfo.stream()
                    .collect(Collectors.toMap(OrderInfoFromStampPurchaseBatchVo::getUrUserId, OrderInfoFromStampPurchaseBatchVo::getOrderCount));
            List<EventStampPurchaseJoinRequestDto> joinRequestDtoList = new ArrayList<>();

            for (EventStampPurchaseJoinVo join : joinList) {
                // 스탬프 수량 계산
                int stampCount = orderMaps.getOrDefault(join.getUrUserId(), 0);
                if (stampCount > vo.getStampCount2()) {
                    stampCount = vo.getStampCount2();
                }

                joinRequestDtoList.add(EventStampPurchaseJoinRequestDto.builder()
                        .evEventStampJoinId(join.getEvEventStampJoinId())
                        .stampCount(stampCount)
                        .build());
            }

            // 스탬프 반영
            promotionEventBatchMapper.putEventStampJoin(joinRequestDtoList);

            //스탬프 반영 시간 반영
            promotionEventBatchMapper.putEventStamp(vo.getEvEventId());
        }
    }

}
