package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ErpEmployeeServiceTest extends CommonServiceTestBaseForJunit5{

    @Autowired
    private ErpEmployeeService erpEmployeeService;

    @Test
    void ERP_임직원정보_연동_조회_성공() {

        List<ErpEmployeeVo> erpEmployeeVoList = erpEmployeeService.getErpEmployeeList();

        log.info(erpEmployeeVoList.toString());
    }

}
