package kr.co.pulmuone.v1.comm.mappers.batch.master.promotion;

import kr.co.pulmuone.v1.batch.promotion.event.dto.EventStampPurchaseJoinRequestDto;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventStampPurchaseJoinVo;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventStampPurchaseVo;
import kr.co.pulmuone.v1.batch.promotion.event.dto.vo.EventTimeOverVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionEventBatchMapper {

    List<EventTimeOverVo> getEventTimeOver();

    void putEventUseYn(@Param("evEventId") Long evEventId);

    List<EventStampPurchaseVo> getStampPurchase();

    List<EventStampPurchaseJoinVo> getStampPurchaseJoin(@Param("evEventId") Long evEventId);

    void putEventStamp(@Param("evEventId") Long evEventId);

    void putEventStampJoin(@Param("list") List<EventStampPurchaseJoinRequestDto> list);

}
