package kr.co.pulmuone.v1.comm.mapper.order.claim;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadFailRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadListRequestDto;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadFailVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadInfoVo;
import kr.co.pulmuone.v1.order.claim.dto.vo.ClaimInfoExcelUploadSuccessVo;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelFailRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayExcelListRequestDto;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelFailVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelInfoVo;
import kr.co.pulmuone.v1.order.ifday.dto.vo.IfDayExcelSuccessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ClaimInfoExcelUploadMapper {

    void addClaimExcelInfo(ClaimInfoExcelUploadInfoVo vo);

    void putClaimExcelInfo(ClaimInfoExcelUploadInfoVo vo);

    void addClaimExcelSuccess(@Param("ifClaimExcelInfoId") Long ifClaimExcelInfoId, @Param("voList") List<ClaimInfoExcelUploadSuccessVo> voList);

    void addClaimExcelFail(@Param("ifClaimExcelInfoId") Long ifClaimExcelInfoId, @Param("voList") List<ClaimInfoExcelUploadFailVo> voList);

    ClaimInfoExcelUploadInfoVo getClaimExcelInfo(ClaimInfoExcelUploadFailRequestDto claimInfoExcelUploadFailRequestDto);

    Page<ClaimInfoExcelUploadInfoVo> getClaimExcelInfoList(ClaimInfoExcelUploadListRequestDto dto);

    List<ClaimInfoExcelUploadFailVo> getClaimFailExcelDownload(ClaimInfoExcelUploadFailRequestDto dto);

    List<ClaimInfoExcelUploadFailVo> getClaimUpdateFailExcelDownload(ClaimInfoExcelUploadFailRequestDto claimInfoExcelUploadFailRequestDto);
}
