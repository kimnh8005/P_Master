package kr.co.pulmuone.v1.comm.mapper.statics.claim;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimReasonStaticsVo;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimStaticsVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClaimStaticsMapper {

    Page<ClaimStaticsVo> getClaimStaticsList(ClaimStaticsRequestDto dto);

    Page<ClaimReasonStaticsVo> getClaimReasonStaticsList(ClaimReasonStaticsRequestDto dto);

}