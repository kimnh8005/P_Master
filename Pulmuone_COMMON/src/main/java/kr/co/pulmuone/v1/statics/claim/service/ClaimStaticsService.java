package kr.co.pulmuone.v1.statics.claim.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.statics.claim.ClaimStaticsMapper;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimReasonStaticsVo;
import kr.co.pulmuone.v1.statics.claim.dto.vo.ClaimStaticsVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0       2021.07.22.              이원호         최초작성
 * =======================================================================
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimStaticsService {

    private final ClaimStaticsMapper claimStaticsMapper;

    /**
     * 클레임 현황 통계 리스트 조회
     *
     * @param dto ClaimStaticsRequestDto
     * @return ClaimStaticsResponseDto
     */
    protected ClaimStaticsResponseDto getClaimStaticsList(ClaimStaticsRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ClaimStaticsVo> claimList = claimStaticsMapper.getClaimStaticsList(dto);

        return ClaimStaticsResponseDto.builder()
                .total(claimList.getTotal())
                .rows(claimList.getResult())
                .build();
    }

    /**
     * 클레임 사유 현황 통계 리스트 조회
     *
     * @param dto ClaimReasonStaticsRequestDto
     * @return ClaimReasonStaticsResponseDto
     */
    protected ClaimReasonStaticsResponseDto getClaimReasonStaticsList(ClaimReasonStaticsRequestDto dto) {
        PageMethod.startPage(dto.getPage(), dto.getPageSize());
        Page<ClaimReasonStaticsVo> claimList = claimStaticsMapper.getClaimReasonStaticsList(dto);

        return ClaimReasonStaticsResponseDto.builder()
                .total(claimList.getTotal())
                .rows(claimList.getResult())
                .build();
    }

}
