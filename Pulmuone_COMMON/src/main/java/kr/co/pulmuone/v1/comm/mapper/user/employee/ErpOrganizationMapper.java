package kr.co.pulmuone.v1.comm.mapper.user.employee;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpOrganizationVo;

@Mapper
public interface ErpOrganizationMapper {

    /**
     * @Desc ERP 조직정보 조회
     * @param erpOrganizationRequestDto
     * @return List<ErpOrganizationVo>
     */
    List<ErpOrganizationVo> getPulmuoneOrganizationList(ErpOrganizationRequestDto erpOrganizationRequestDto);

    /**
     * @Desc ERP 조직정보 등록
     * @param erpOrganizationVo
     * @return int
     */
    int addErpOrganization(ErpOrganizationVo erpOrganizationVo);

    /**
     * @Desc ERP 조직정보 수정
     * @param erpOrganizationVo
     * @return int
     */
    int putErpOrganization(ErpOrganizationVo erpOrganizationVo);
}
