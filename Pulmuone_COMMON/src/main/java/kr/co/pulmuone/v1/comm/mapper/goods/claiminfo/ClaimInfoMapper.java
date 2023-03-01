package kr.co.pulmuone.v1.comm.mapper.goods.claiminfo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;

@Mapper
public interface ClaimInfoMapper {

	List<ClaimInfoVo> getClaimInfoList(ClaimInfoRequestDto claimInfoRequestDto);

	ClaimInfoVo getClaimInfo(String ilClaimInfoId);

    int putClaimInfo(ClaimInfoRequestDto claimInfoRequestDto);
}
