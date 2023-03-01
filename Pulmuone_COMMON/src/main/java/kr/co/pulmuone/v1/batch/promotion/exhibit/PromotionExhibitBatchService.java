package kr.co.pulmuone.v1.batch.promotion.exhibit;

import kr.co.pulmuone.v1.batch.promotion.exhibit.dto.vo.ExhibitTimeOverVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.promotion.PromotionExhibitBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromotionExhibitBatchService {

    private final PromotionExhibitBatchMapper promotionExhibitBatchMapper;

    protected void runExhibitTimeOver() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<ExhibitTimeOverVo> eventList = promotionExhibitBatchMapper.getExhibitTimeOver();
        for (ExhibitTimeOverVo vo : eventList) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endDate = LocalDateTime.parse(vo.getEndDateTime(), dateFormatter);
            // 종료일 지남 확인
            if (endDate.isBefore(now)) {
                promotionExhibitBatchMapper.putExhibitUseYn(vo.getEvExhibitId());
            }
        }
    }
}
