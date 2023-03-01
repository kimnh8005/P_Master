package kr.co.pulmuone.v1.policy.fee.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.policy.fee.OmLogisticsFeeMapper;
import kr.co.pulmuone.v1.policy.fee.dto.*;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeHistVo;
import kr.co.pulmuone.v1.policy.fee.dto.vo.OmLogisticsFeeVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 물류 수수료 관리 Service
 * </PRE>
 *
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class OmLogisticsFeeService {

	private final OmLogisticsFeeMapper omLogisticsFeeMapper;

    /**
     * 물류 수수료 목록 조회
     *
     * @param omLogisticsFeeListRequestDto
     * @return Page<OmLogisticsFeeListDto>
     * @throws Exception
     */
    protected Page<OmLogisticsFeeListDto> getOmLogisticsFeeList(OmLogisticsFeeListRequestDto omLogisticsFeeListRequestDto) throws Exception {

    	PageMethod.startPage(omLogisticsFeeListRequestDto.getPage(), omLogisticsFeeListRequestDto.getPageSize());

        return omLogisticsFeeMapper.getOmLogisticsFeeList(omLogisticsFeeListRequestDto);
    }

    /**
     * 물류 수수료 상세 조회
     *
     * @param omLogisticsFeeRequestDto
     * @return OmLogisticsFeeDto
     * @throws Exception
     */
    protected OmLogisticsFeeDto getOmLogisticsFee(OmLogisticsFeeRequestDto omLogisticsFeeRequestDto) throws Exception {
    	return omLogisticsFeeMapper.getOmLogisticsFee(omLogisticsFeeRequestDto);
    }

    /**
     * 물류 수수료 이력 목록 조회
     *
     * @param omLogisticsFeeHistListRequestDto
     * @return Page<OmLogisticsFeeListDto>
     * @throws Exception
     */
    protected Page<OmLogisticsFeeHistListDto> getOmLogisticsFeeHistList(OmLogisticsFeeHistListRequestDto omLogisticsFeeHistListRequestDto) throws Exception {

        PageMethod.startPage(omLogisticsFeeHistListRequestDto.getPage(), omLogisticsFeeHistListRequestDto.getPageSize());

        return omLogisticsFeeMapper.getOmLogisticsFeeHistList(omLogisticsFeeHistListRequestDto);
    }

    /**
     * 물류 수수료 등록
     *
     * @param omLogisticsFeeVoParam
     * @return int
     * @throws Exception
     */
    protected int addOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVoParam) throws Exception {
        return omLogisticsFeeMapper.addOmLogisticsFee(omLogisticsFeeVoParam);
    }

    /**
     * 물류 수수료 이력 등록
     *
     * @param omLogisticsFeeHistVo
     * @return int
     * @throws Exception
     */
    protected int addOmLogisticsFeeHist(OmLogisticsFeeHistVo omLogisticsFeeHistVo) throws Exception {
        return omLogisticsFeeMapper.addOmLogisticsFeeHist(omLogisticsFeeHistVo);
    }

    /**
     * 물류 수수료 수정
     *
     * @param omLogisticsFeeVoParam
     * @return void
     * @throws Exception
     */
    protected int putOmLogisticsFee(OmLogisticsFeeVo omLogisticsFeeVoParam) throws Exception {
        return omLogisticsFeeMapper.putOmLogisticsFee(omLogisticsFeeVoParam);
    }

}