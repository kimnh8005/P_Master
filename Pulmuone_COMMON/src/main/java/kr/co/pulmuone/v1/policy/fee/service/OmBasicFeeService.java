package kr.co.pulmuone.v1.policy.fee.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.policy.fee.OmBasicFeeMapper;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmBasicFeeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 기본 수수료 관리 Service
 * </PRE>
 *
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OmBasicFeeService {

	private final OmBasicFeeMapper omBasicFeeMapper;

    /**
     * 기본 수수료 목록 조회
     *
     * @param omBasicFeeListRequestDto
     * @return Page<OmBasicFeeListDto>
     * @throws Exception
     */
    protected Page<OmBasicFeeListDto> getOmBasicFeeList(OmBasicFeeListRequestDto omBasicFeeListRequestDto) throws Exception {

    	PageMethod.startPage(omBasicFeeListRequestDto.getPage(), omBasicFeeListRequestDto.getPageSize());

        return omBasicFeeMapper.getOmBasicFeeList(omBasicFeeListRequestDto);
    }

    /**
     * 기본 수수료 상세 조회
     *
     * @param omBasicFeeRequestDto
     * @return OmBasicFeeDto
     * @throws Exception
     */
    protected OmBasicFeeDto getOmBasicFee(OmBasicFeeRequestDto omBasicFeeRequestDto) throws Exception {
    	return omBasicFeeMapper.getOmBasicFee(omBasicFeeRequestDto);
    }

    /**
     * 기본 수수료 이력 목록 조회
     *
     * @param omBasicFeeHistListRequestDto
     * @return Page<OmBasicFeeListDto>
     * @throws Exception
     */
    protected Page<OmBasicFeeHistListDto> getOmBasicHistFeeList(OmBasicFeeHistListRequestDto omBasicFeeHistListRequestDto) throws Exception {

        PageMethod.startPage(omBasicFeeHistListRequestDto.getPage(), omBasicFeeHistListRequestDto.getPageSize());

        return omBasicFeeMapper.getOmBasicFeeHistList(omBasicFeeHistListRequestDto);
    }

    /**
     * 기본 수수료 등록
     *
     * @param omBasicFeeVoParam
     * @return int
     * @throws Exception
     */
    protected int addOmBasicFee(OmBasicFeeVo omBasicFeeVoParam) throws Exception {
        return omBasicFeeMapper.addOmBasicFee(omBasicFeeVoParam);
    }

    /**
     * 기본 수수료 이력 등록
     *
     * @param omBasicFeeHistVo
     * @return int
     * @throws Exception
     */
    protected int addOmBasicFeeHist(OmBasicFeeHistVo omBasicFeeHistVo) throws Exception {
        return omBasicFeeMapper.addOmBasicFeeHist(omBasicFeeHistVo);
    }

    /**
     * 기본 수수료 수정
     *
     * @param omBasicFeeVoParam
     * @return void
     * @throws Exception
     */
    protected int putOmBasicFee(OmBasicFeeVo omBasicFeeVoParam) throws Exception {
        return omBasicFeeMapper.putOmBasicFee(omBasicFeeVoParam);
    }

    /**
     * 판매처그룹수정
     *
     * @param omBasicFeeVoParam
     * @return void
     * @throws Exception
     */
    protected int putOmBasicFeeSellersGroup(OmBasicFeeVo omBasicFeeVoParam) throws Exception {
        return omBasicFeeMapper.putOmBasicFeeSellersGroup(omBasicFeeVoParam);
    }

}