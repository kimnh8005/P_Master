package kr.co.pulmuone.v1.comm.mappers.batch.master.user.marketing;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.batch.user.marketing.dto.MarketingInfoBatchDto;


@Mapper
public interface MarketingInfoBatchMapper {

    List<MarketingInfoBatchDto> getTargetMarketingInfo();


}
