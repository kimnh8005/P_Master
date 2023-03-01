package kr.co.pulmuone.v1.comm.mappers.slaveErp.user.employee;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpOrganizationVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpRegalVo;

@Mapper
@Repository
public interface PulmuoneEmployeeMapper {

    /**
     * @Desc ERP 임직원정보 연동 조회
     * @return List<ErpEmployeeVo>
     */
    List<ErpEmployeeVo> getErpEmployeeList();

    /**
     * @Desc ERP 조직정보 연동 조회
     * @return List<ErpOrganizationVo>
     */
    List<ErpOrganizationVo> getErpOrganizationList();

    /**
     * @Desc ERP 법인정보 연동 조회
     * @return List<ErpRegalVo>
     */
    List<ErpRegalVo> getErpRegalList();
}
