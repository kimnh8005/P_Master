package kr.co.pulmuone.batch.erp.infra.mapper.user.master;

import kr.co.pulmuone.batch.erp.domain.model.base.dto.vo.UserVo;
import kr.co.pulmuone.batch.erp.domain.model.system.itgc.dto.ItgcRequestDto;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.EmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpEmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpOrganizationBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpRegalBatchVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface UserEmployeeByErpBatchMapper {

	List<ErpEmployeeBatchVo> getPulmuoneEmployeeList(@Param("code") String code);

	List<ErpEmployeeBatchVo> getSyncTargetPulmuoneEmployeeList();

	List<ErpEmployeeBatchVo> getSyncExceptionTargetPulmuoneEmployeeList();

	int addErpEmployee(ErpEmployeeBatchVo employeeBatchVo);

	int putEmployee(EmployeeBatchVo employeeVo);

	UserVo getUserInfo(@Param("loginId") String loginId);

	int putUserName(UserVo userVo);

	int addErpRegal(ErpRegalBatchVo erpRegalVo);

	int addErpOrganization(ErpOrganizationBatchVo erpOrganizationVo);

	int addItgcList(@Param("insertList") List<ItgcRequestDto> insertList);

}
