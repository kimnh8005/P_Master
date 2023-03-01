package kr.co.pulmuone.v1.comm.mappers.batch.master.promotion;

import kr.co.pulmuone.v1.batch.promotion.exhibit.dto.vo.ExhibitTimeOverVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionExhibitBatchMapper {

    List<ExhibitTimeOverVo> getExhibitTimeOver();

    void putExhibitUseYn(@Param("evExhibitId") Long evExhibitId);

}
