package kr.co.pulmuone.v1.policy.accessibleip.service;


import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.policy.accessibleIp.PolicyAccessibleIpMapper;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.*;

import kr.co.pulmuone.v1.policy.accessibleip.dto.vo.GetPolicyAccessibleIpListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * BOS 접근 IP 설정 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 21.                최성현          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class PolicyAccessibleIpService {

    @Autowired
    private PolicyAccessibleIpMapper policyAccessibleIpMapper;

    /**
     * @Desc BOS 접근 IP 조회
     * @param getPolicyAccessibleIpListRequestDto
     * @return Page<GetPolicyAccessibleIpListResultVo>
     */
    protected Page<GetPolicyAccessibleIpListResultVo> getPolicyAccessibleIpList(GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto) {
        PageMethod.startPage(getPolicyAccessibleIpListRequestDto.getPage(), getPolicyAccessibleIpListRequestDto.getPageSize());
        return policyAccessibleIpMapper.getPolicyAccessibleIpList(getPolicyAccessibleIpListRequestDto);
    }

    /**
     * @Desc BOS 접근 IP 저장
     * @param insertRequestDtoList
     * @return int
     */
    protected int addPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList) {
       return policyAccessibleIpMapper.addPolicyAccessibleIp(insertRequestDtoList);
    }

    /**
     * @Desc BOS 접근 IP 수정
     * @param updateRequestDtoList
     * @return int
     */
    protected int putPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> updateRequestDtoList) {
        return policyAccessibleIpMapper.putPolicyAccessibleIp(updateRequestDtoList);
    }

    /**
     * @Desc BOS 접근 IP 삭제
     * @param deleteRequestDtoList
     * @return int
     */
    protected int delPolicyAccessibleIp(List<SavePolicyAccessibleIpRequestSaveDto> deleteRequestDtoList) {
        return policyAccessibleIpMapper.delPolicyAccessibleIp(deleteRequestDtoList);
    }

    /**
     * @Desc 중복 데이터 체크
     * @param voList
     * @return int
     */
    protected int checkPolicyAccessibleIpDuplicate(List<SavePolicyAccessibleIpRequestSaveDto> voList) {
        return policyAccessibleIpMapper.checkPolicyAccessibleIpDuplicate(voList);
    }
}
