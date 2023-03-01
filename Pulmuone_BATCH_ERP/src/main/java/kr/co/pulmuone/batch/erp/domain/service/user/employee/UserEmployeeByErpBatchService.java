package kr.co.pulmuone.batch.erp.domain.service.user.employee;

import kr.co.pulmuone.batch.erp.domain.model.base.dto.vo.UserVo;
import kr.co.pulmuone.batch.erp.domain.model.system.itgc.dto.ItgcRequestDto;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.EmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpEmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpOrganizationBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpRegalBatchVo;
import kr.co.pulmuone.batch.erp.infra.mapper.user.master.UserEmployeeByErpBatchMapper;
import kr.co.pulmuone.batch.erp.infra.mapper.user.slaveErp.ErpEmployeeBatchMapper;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserEmployeeByErpBatchService {
    @Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private SqlSessionTemplate masterSqlSession;

    @Autowired
    private UserEmployeeByErpBatchMapper userEmployeeByErpBatchMapper;

    @Autowired
    private ErpEmployeeBatchMapper erpEmployeeBatchMapper;

    /**
     * @Desc 풀무원 임직원정보
     * @return
     * @return List<ErpEmployeeBatchVo>
     */
    public List<ErpEmployeeBatchVo> getPulmuoneEmployeeList(String code) {
    	return userEmployeeByErpBatchMapper.getPulmuoneEmployeeList(code);
    }


    /**
     * @Desc ERP 임직원정보
     * @return
     * @return List<ErpEmployeeBatchVo>
     */
    public List<ErpEmployeeBatchVo> getErpEmployeeList() {
    	return erpEmployeeBatchMapper.getErpEmployeeList();
    }



    /**
     * @Desc ERP 임직원 정보 변경
     * @param erpEmployeeBatchVo
     * @return
     * @return int
     */
    protected int addErpEmployee(ErpEmployeeBatchVo erpEmployeeBatchVo) {
    	return userEmployeeByErpBatchMapper.addErpEmployee(erpEmployeeBatchVo);
	}


    /**
     * @Desc 회원상세정보 상태 업데이트
     * @param resignEmployeeVo
     * @return
     * @return int
     */
    protected int putEmployee(EmployeeBatchVo resignEmployeeVo) {
    	return userEmployeeByErpBatchMapper.putEmployee(resignEmployeeVo);
    }


    /**
     * @Desc 회원기본정보 조회
     * @param loginId
     * @return
     * @return UserVo
     */
    protected UserVo getUserInfo(String loginId) {
        return userEmployeeByErpBatchMapper.getUserInfo(loginId);
    }


    /**
     * @Desc 회원기본정보 이름 수정
     * @param userVo
     * @return
     * @return int
     */
    protected int putUserName(UserVo userVo) {
        return userEmployeeByErpBatchMapper.putUserName(userVo);
    }



    /**
     * @Desc ERP 법인정보 (사용??? batch 설정 확인 필요)
     * @return
     * @return List<ErpRegalBatchVo>
     */
    public List<ErpRegalBatchVo> getErpRegalList() {
    	return erpEmployeeBatchMapper.getErpRegalList();

    }


    /**
     * @Desc ERP 법인 정보 등록/수정
     * @param erpRegalVo
     * @return
     * @return int
     */
    public int addErpRegal(ErpRegalBatchVo erpRegalVo) {
    	return userEmployeeByErpBatchMapper.addErpRegal(erpRegalVo);

    }


    /**
     * @Desc ERP 조직정보
     * @return
     * @return List<ErpOrganizationBatchVo>
     */
    public List<ErpOrganizationBatchVo> getErpOrganizationList() {
    	return erpEmployeeBatchMapper.getErpOrganizationList();

    }


    /**
     * @Desc ERP 조직  정보 등록/수정
     * @param erpOrganizationVo
     * @return
     * @return int
     */
    public int addErpOrganization(ErpOrganizationBatchVo erpOrganizationVo) {
    	return userEmployeeByErpBatchMapper.addErpOrganization(erpOrganizationVo);

    }

    /**
     * @Desc 풀무원 임직원 동기화 대상 조회 목록
     * @return
     * @return List<ErpEmployeeBatchVo>
     */
    public List<ErpEmployeeBatchVo> getSyncTargetPulmuoneEmployeeList() {
        return userEmployeeByErpBatchMapper.getSyncTargetPulmuoneEmployeeList();
    }

    /**
     * @Desc 풀무원 임직원 동기화 예외처리 대상 조회 목록
     * @return
     * @return List<ErpEmployeeBatchVo>
     */
    public List<ErpEmployeeBatchVo> getSyncExceptionTargetPulmuoneEmployeeList() {
        return userEmployeeByErpBatchMapper.getSyncExceptionTargetPulmuoneEmployeeList();
    }

    /**
     * itgc List 등록
     *
     * @param insertList List<itgcRequestDto>
     * @return int
     */
    protected int addItgcList(List<ItgcRequestDto> insertList) {
        if (insertList.isEmpty()) {
            return -1;
        }

        return userEmployeeByErpBatchMapper.addItgcList(insertList);
    }

}

