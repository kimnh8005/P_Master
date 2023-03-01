package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.user.employee.ErpOrganizationMapper;
import kr.co.pulmuone.v1.comm.mappers.slaveErp.user.employee.PulmuoneEmployeeMapper;
import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpOrganizationVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* ERP 조직정보 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 16.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class ErpOrganizationService {

    @Autowired
    private final ErpOrganizationMapper erpOrganizationMapper;

    @Autowired
    private final PulmuoneEmployeeMapper pulmuoneEmployeeMapper;

    /**
     * @Desc ERP 조직정보 조회
     * @param erpOrganizationRequestDto
     * @return List<ErpOrganizationVo>
     */
    protected List<ErpOrganizationVo> getPulmuoneOrganizationList(ErpOrganizationRequestDto erpOrganizationRequestDto){
        return erpOrganizationMapper.getPulmuoneOrganizationList(erpOrganizationRequestDto);
    }

    /**
     * @Desc ERP 조직정보 연동 조회
     * @return List<ErpOrganizationVo>
     */
    protected List<ErpOrganizationVo> getErpOrganizationList(){
        return pulmuoneEmployeeMapper.getErpOrganizationList();
    }

    /**
     * @Desc ERP 조직정보 등록
     * @param erpOrganizationVo
     * @return int
     */
    protected int addErpOrganization(ErpOrganizationVo erpOrganizationVo){
        return erpOrganizationMapper.addErpOrganization(erpOrganizationVo);
    }

    /**
     * @Desc ERP 조직정보 수정
     * @param erpOrganizationVo
     * @return int
     */
    protected int putErpOrganization(ErpOrganizationVo erpOrganizationVo){
        return erpOrganizationMapper.putErpOrganization(erpOrganizationVo);
    }
}
