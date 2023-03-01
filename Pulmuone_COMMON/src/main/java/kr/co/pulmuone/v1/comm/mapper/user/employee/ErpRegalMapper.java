package kr.co.pulmuone.v1.comm.mapper.user.employee;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.user.employee.dto.ErpRegalRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpRegalVo;

@Mapper
public interface ErpRegalMapper {

    /**
     * @Desc ERP 법인정보 최근 업데이트 일자
     * @return String
     */
    String getLastUpdateDate();

    /**
     * @Desc ERP 법인정보 조회
     * @param erpRegalRequestDto
     * @return Page<ErpRegalVo>
     */
    Page<ErpRegalVo> getPulmuoneRegalList(ErpRegalRequestDto erpRegalRequestDto);

    /**
     * @Desc ERP 법인정보 등록
     * @param erpRegalVo
     * @return int
     */
    int addErpRegal(ErpRegalVo erpRegalVo);

    /**
     * @Desc ERP 법인정보 수정
     * @param erpRegalVo
     * @return int
     */
    int putErpRegal(ErpRegalVo erpRegalVo);
}
