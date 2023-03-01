package kr.co.pulmuone.v1.comm.mappers.batch.master.promotion;

import kr.co.pulmuone.v1.batch.promotion.ad.dto.vo.SamsungAdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PromotionAdvertisingBatchMapper {

    List<SamsungAdVo> getAdExternalGoodsList(@Param("adId") String adId, @Param("apiDomain") String apiDomain, @Param("imageUrl") String imageUrl) throws Exception;

}