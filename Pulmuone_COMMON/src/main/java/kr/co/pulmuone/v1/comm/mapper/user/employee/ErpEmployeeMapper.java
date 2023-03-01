package kr.co.pulmuone.v1.comm.mapper.user.employee;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;

@Mapper
public interface ErpEmployeeMapper {

    /**
     * @Desc ERP 임직원정보 최근 업데이트 일자
     * @return String
     */
    String getLastUpdateDate();

    /**
     * @Desc ERP 임직원 정보 조회
     * @param erpEmployeeRequestDto
     * @return Page<ErpEmployeeVo>
     */
    Page<ErpEmployeeVo> getPulmuoneEmployeeList(ErpEmployeeRequestDto erpEmployeeRequestDto);

    /**
     * @Desc ERP 임직원정보 등록
     * @param erpEmployeeVo
     * @return int
     */
    int addErpEmployee(ErpEmployeeVo erpEmployeeVo);

    /**
     * @Desc ERP 임직원정보 수정
     * @param erpEmployeeVo
     * @return int
     */
    int putErpEmployee(ErpEmployeeVo erpEmployeeVo);
}
