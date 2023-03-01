package kr.co.pulmuone.batch.erp.infra.mapper.user.slaveErp;

import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpEmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpOrganizationBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpRegalBatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface ErpEmployeeBatchMapper {

    /**
     * @Desc ERP 임직원정보 연동 조회
     * @return List<EmployeeBatchVo>
     */
    List<ErpEmployeeBatchVo> getErpEmployeeList();

    /**
     * @Desc ERP 조직정보 연동 조회
     * @return List<ErpOrganizationVo>
     */
    List<ErpOrganizationBatchVo> getErpOrganizationList();

    /**
     * @Desc ERP 법인정보 연동 조회
     * @return List<ErpRegalVo>
     */
    List<ErpRegalBatchVo> getErpRegalList();
}
