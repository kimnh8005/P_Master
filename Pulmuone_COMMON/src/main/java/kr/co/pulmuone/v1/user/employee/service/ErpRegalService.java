package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.employee.ErpRegalMapper;
import kr.co.pulmuone.v1.comm.mappers.slaveErp.user.employee.PulmuoneEmployeeMapper;
import kr.co.pulmuone.v1.user.employee.dto.ErpRegalRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpRegalVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* ERP 법인정보 Service
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
public class ErpRegalService {

    @Autowired
    private final ErpRegalMapper erpRegalMapper;

    @Autowired
    private final PulmuoneEmployeeMapper pulmuoneEmployeeMapper;

    /**
     * @Desc ERP 법인정보 최근 업데이트 일자
     * @return String
     */
    protected String getLastUpdateDate() {
        return erpRegalMapper.getLastUpdateDate();
    }

    /**
     * @Desc ERP 법인정보 조회
     * @param erpRegalRequestDto
     * @return Page<ErpRegalVo>
     */
    protected Page<ErpRegalVo> getPulmuoneRegalList(ErpRegalRequestDto erpRegalRequestDto) {
        PageMethod.startPage(erpRegalRequestDto.getPage(), erpRegalRequestDto.getPageSize());
        return erpRegalMapper.getPulmuoneRegalList(erpRegalRequestDto);
    }

    /**
     * @Desc ERP 법인정보 전체 조회
     * @return List<ErpRegalVo>
     */
    protected List<ErpRegalVo> getPulmuoneRegalAllList() {
        ErpRegalRequestDto erpRegalRequestDto = new ErpRegalRequestDto();
        return erpRegalMapper.getPulmuoneRegalList(erpRegalRequestDto);
    }

    /**
     * @Desc ERP 법인정보 연동 조회
     * @return List<ErpRegalVo>
     */
    protected List<ErpRegalVo> getErpRegalList(){
        return pulmuoneEmployeeMapper.getErpRegalList();
    }

    /**
     * @Desc ERP 법인정보 등록
     * @param erpRegalVo
     * @return int
     */
    protected int addErpRegal(ErpRegalVo erpRegalVo){
        return erpRegalMapper.addErpRegal(erpRegalVo);
    }

    /**
     * @Desc ERP 법인정보 수정
     * @param erpRegalVo
     * @return int
     */
    protected int putErpRegal(ErpRegalVo erpRegalVo){
        return erpRegalMapper.putErpRegal(erpRegalVo);
    }
}
