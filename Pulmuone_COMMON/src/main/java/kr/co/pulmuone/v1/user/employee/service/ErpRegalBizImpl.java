package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.user.employee.dto.ErpRegalRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.ErpRegalResponseDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpRegalVo;
import kr.co.pulmuone.v1.comm.constants.Constants;


/**
* <PRE>
* Forbiz Korea
* ERP 법인정보 BizImpl
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
public class ErpRegalBizImpl  implements ErpRegalBiz {

    @Autowired
    ErpRegalService erpRegalService;

    /**
     * @Desc ERP 법인 정보 조회
     * @param erpRegalRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPulmuoneRegalList(ErpRegalRequestDto erpRegalRequestDto){
        ErpRegalResponseDto erpRegalResponseDto = new ErpRegalResponseDto();

        String lastUpdateDate = erpRegalService.getLastUpdateDate();
        Page<ErpRegalVo> erpRegalList = erpRegalService.getPulmuoneRegalList(erpRegalRequestDto);

        erpRegalResponseDto.setLastUpdateDate(lastUpdateDate);
        erpRegalResponseDto.setTotal(erpRegalList.getTotal());
        erpRegalResponseDto.setRows(erpRegalList.getResult());

        return ApiResult.success(erpRegalResponseDto);
    }

    /**
     * @Desc ERP 법인 정보 전체 조회
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPulmuoneRegalListWithoutPaging(){
        ErpRegalResponseDto erpRegalResponseDto = new ErpRegalResponseDto();

        List<ErpRegalVo> erpRegalList = erpRegalService.getPulmuoneRegalAllList();
        erpRegalResponseDto.setRows(erpRegalList);

        return ApiResult.success(erpRegalResponseDto);
    }

    /**
     * @Desc ERP 법인정보 연동
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> erpRegalApiInterworking() throws Exception{
        boolean newRegalYn = true;

        List<ErpRegalVo> erpRegalList = erpRegalService.getPulmuoneRegalAllList();
        List<ErpRegalVo> erpRegalApiList = erpRegalService.getErpRegalList();

        for(ErpRegalVo erpRegalApiVo : erpRegalApiList) {

            newRegalYn = true;

            for(ErpRegalVo erpRegalVo : erpRegalList) {
                if( erpRegalApiVo.getErpRegalCode().equalsIgnoreCase(erpRegalVo.getErpRegalCode()) ) {
                    newRegalYn = false;
                    break;
                }
            }

            // 신규 법인
            if( newRegalYn ) {

                // UR_ERP_REGAL 등록
                erpRegalApiVo.setUseYn(BaseEnums.UseYn.Y.name());
                erpRegalApiVo.setEmployeeDiscountYn(BaseEnums.UseYn.N.name());
                erpRegalApiVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpRegalService.addErpRegal(erpRegalApiVo);
            }else { // 기존 법인

                // UR_ERP_REGAL 업데이트
                erpRegalApiVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpRegalService.putErpRegal(erpRegalApiVo);
            }
        }

        return ApiResult.success();
    }
}
