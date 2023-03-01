package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.ErpOrganizationResponseDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpOrganizationVo;
import kr.co.pulmuone.v1.comm.constants.Constants;


/**
* <PRE>
* Forbiz Korea
* ERP 조직정보 BizImpl
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
public class ErpOrganizationBizImpl  implements ErpOrganizationBiz {

    @Autowired
    ErpOrganizationService erpOrganizationService;

    /**
     * @Desc ERP 조직정보 조회
     * @param erpOrganizationRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPulmuoneOrganizationList(ErpOrganizationRequestDto erpOrganizationRequestDto){
        ErpOrganizationResponseDto erpOrganizationResponseDto = new ErpOrganizationResponseDto();

        List<ErpOrganizationVo> erpOrganizationList = erpOrganizationService.getPulmuoneOrganizationList(erpOrganizationRequestDto);

        erpOrganizationResponseDto.setRows(erpOrganizationList);

        return ApiResult.success(erpOrganizationResponseDto);
    }

    /**
     * @Desc ERP 조직정보 연동
     * @return ApiResult<?>
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> erpOrganizationApiInterworking() throws Exception{
        boolean newOrganizationYn = true;
        ErpOrganizationRequestDto erpOrganizationRequestDto = new ErpOrganizationRequestDto();

        List<ErpOrganizationVo> erpOrganizationAllList = erpOrganizationService.getPulmuoneOrganizationList(erpOrganizationRequestDto);
        List<ErpOrganizationVo> erpOrganizationApiList = erpOrganizationService.getErpOrganizationList();

        for(ErpOrganizationVo erpOrganizationApiVo : erpOrganizationApiList) {

            newOrganizationYn = true;

            for(ErpOrganizationVo erpOrganizationVo : erpOrganizationAllList) {
                if( erpOrganizationApiVo.getErpOrganizationCode().equalsIgnoreCase(erpOrganizationVo.getErpOrganizationCode()) ) {
                    newOrganizationYn = false;
                    break;
                }
            }

            // 신규 조직
            if( newOrganizationYn ) {

                // UR_ERP_ORGANIZATION 등록
                erpOrganizationApiVo.setUseYn(BaseEnums.UseYn.Y.name());
                erpOrganizationApiVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpOrganizationService.addErpOrganization(erpOrganizationApiVo);
            }else { // 기존 조직

                // UR_ERP_ORGANIZATION 업데이트
                erpOrganizationApiVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpOrganizationService.putErpOrganization(erpOrganizationApiVo);
            }
        }

        return ApiResult.success();
    }
}
