package kr.co.pulmuone.batch.cj.infra.mapper.claim.master;

import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 클레임 회수 송장 API 배치 Mapper
 * </PRE>
 */

@Mapper
public interface ReturnTrackingNumberMapper {


    List<String> selectTargetList();

    int addReturnTrackingNumber(HashMap<String, String> params);
}
