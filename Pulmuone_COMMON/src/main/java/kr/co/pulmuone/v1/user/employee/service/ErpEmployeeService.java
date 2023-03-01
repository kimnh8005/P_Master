package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.employee.ErpEmployeeMapper;
import kr.co.pulmuone.v1.comm.mappers.slaveErp.user.employee.PulmuoneEmployeeMapper;
import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* ERP 임직원 Service
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
public class ErpEmployeeService {

    @Autowired
    private final ErpEmployeeMapper erpEmployeeMapper;

    @Autowired
    private final PulmuoneEmployeeMapper pulmuoneEmployeeMapper;

    /**
     * @Desc ERP 임직원정보 최근 업데이트 일자
     * @return String
     */
    protected String getLastUpdateDate() {
        return erpEmployeeMapper.getLastUpdateDate();
    }

    /**
     * @Desc ERP 임직원 정보 조회
     * @param erpEmployeeRequestDto
     * @return Page<ErpEmployeeVo>
     */
    protected Page<ErpEmployeeVo> getPulmuoneEmployeeList(ErpEmployeeRequestDto erpEmployeeRequestDto) {
        PageMethod.startPage(erpEmployeeRequestDto.getPage(), erpEmployeeRequestDto.getPageSize());
        return erpEmployeeMapper.getPulmuoneEmployeeList(erpEmployeeRequestDto);
    }

    /**
     * @Desc ERP 임직원 정보 전체 조회
     * @param erpEmployeeRequestDto
     * @return List<ErpRegalVo>
     */
    protected List<ErpEmployeeVo> getPulmuoneEmployeeAllList() {
        ErpEmployeeRequestDto erpEmployeeRequestDto = new ErpEmployeeRequestDto();
        return erpEmployeeMapper.getPulmuoneEmployeeList(erpEmployeeRequestDto);
    }

    /**
     * @Desc ERP 임직원정보 연동 조회
     * @return List<ErpEmployeeVo>
     */
    protected List<ErpEmployeeVo> getErpEmployeeList(){
        return pulmuoneEmployeeMapper.getErpEmployeeList();
    }

    /**
     * @Desc ERP 임직원정보 등록
     * @param erpEmployeeVo
     * @return int
     */
    protected int addErpEmployee(ErpEmployeeVo erpEmployeeVo) {
        return erpEmployeeMapper.addErpEmployee(erpEmployeeVo);
    }

    /**
     * @Desc ERP 임직원정보 수정
     * @param erpEmployeeVo
     * @return int
     */
    protected int putErpEmployee(ErpEmployeeVo erpEmployeeVo) {
        return erpEmployeeMapper.putErpEmployee(erpEmployeeVo);
    }
}
