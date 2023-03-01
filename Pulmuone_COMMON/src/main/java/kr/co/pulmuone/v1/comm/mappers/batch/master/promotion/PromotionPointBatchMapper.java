package kr.co.pulmuone.v1.comm.mappers.batch.master.promotion;

import kr.co.pulmuone.v1.batch.promotion.point.dto.vo.PointExpiredListVo;
import kr.co.pulmuone.v1.batch.promotion.point.dto.vo.PointExpiredVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionPointBatchMapper {

    List<PointExpiredVo> getPointExpectExpired(@Param("startDate") String startDate, @Param("endDate") String endDate);

    List<PointExpiredListVo> getPointExpectExpireList(@Param("list") List<PointExpiredVo> list, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
