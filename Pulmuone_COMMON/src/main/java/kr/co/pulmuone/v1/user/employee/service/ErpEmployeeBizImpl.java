package kr.co.pulmuone.v1.user.employee.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeRequestDto;
import kr.co.pulmuone.v1.user.employee.dto.ErpEmployeeResponseDto;
import kr.co.pulmuone.v1.user.employee.dto.vo.EmployeeVo;
import kr.co.pulmuone.v1.user.employee.dto.vo.ErpEmployeeVo;
import kr.co.pulmuone.v1.comm.constants.Constants;


/**
* <PRE>
* Forbiz Korea
* ERP 임직원 BizImpl
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
public class ErpEmployeeBizImpl  implements ErpEmployeeBiz {

    @Autowired
    ErpEmployeeService erpEmployeeService;

    @Autowired
    UserEmployeeBiz userEmployeeBiz;

    @Autowired
    ErpOrganizationBiz erpOrganizationBiz;

    @Autowired
    ErpRegalBiz erpRegalBiz;

    /**
     * @Desc ERP 임직원 정보 조회
     * @param erpEmployeeRequestDto
     * @return ApiResult<?>
     * @throws Exception
     */
    @Override
    public ApiResult<?> getPulmuoneEmployeeList(ErpEmployeeRequestDto erpEmployeeRequestDto) throws Exception{
        ErpEmployeeResponseDto erpEmployeeResponseDto = new ErpEmployeeResponseDto();

        String lastUpdateDate = erpEmployeeService.getLastUpdateDate();
        Page<ErpEmployeeVo> erpEmployeeList = erpEmployeeService.getPulmuoneEmployeeList(erpEmployeeRequestDto);

        erpEmployeeResponseDto.setLastUpdateDate(lastUpdateDate);
        erpEmployeeResponseDto.setTotal(erpEmployeeList.getTotal());
        erpEmployeeResponseDto.setRows(erpEmployeeList.getResult());

        return ApiResult.success(erpEmployeeResponseDto);
    }

    /**
     * @Desc ERP 임직원정보 AND 조직정보 연동
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> getErpEmployeeAndErpOrganization() throws Exception{
        this.erpEmployeeApiInterworking();
        erpOrganizationBiz.erpOrganizationApiInterworking();
        erpRegalBiz.erpRegalApiInterworking();

        return ApiResult.success();
    }

    /**
     * @Desc ERP 임직원정보 연동
     * @return ApiResult<?>
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> erpEmployeeApiInterworking() throws Exception{
        boolean newEmployeeYn = true;

        List<ErpEmployeeVo> erpEmployeeAllList = erpEmployeeService.getPulmuoneEmployeeAllList();
        List<ErpEmployeeVo> erpEmployeeApiList = erpEmployeeService.getErpEmployeeList();
        List<ErpEmployeeVo> resignEmployeeList = erpEmployeeAllList.stream()
                                                                   .filter(e -> !erpEmployeeApiList.stream()
                                                                                                   .map(ErpEmployeeVo::getErpEmployeeNumber)
                                                                                                   .collect(Collectors.toList())
                                                                                                   .contains( e.getErpEmployeeNumber() ))
                                                                   .collect(Collectors.toList());

        for(int i=0, resignCount=resignEmployeeList.size(); i < resignCount; i++) {

            // UR_ERP_EMPLOYEE 상태 퇴사로 업데이트
            ErpEmployeeVo resignErpEmployeeVo = new ErpEmployeeVo();
            resignErpEmployeeVo.setErpEmployeeNumber(resignEmployeeList.get(i).getErpEmployeeNumber());
            resignErpEmployeeVo.setErpStatusName(UserEnums.EmployeeStatus.RESIGN.getCodeName());
            resignErpEmployeeVo.setCreateId(Constants.BATCH_CREATE_ID);
            erpEmployeeService.putErpEmployee(resignErpEmployeeVo);

            // UR_EMPLOYEE 상태 퇴사로 업데이트
            EmployeeVo resignEmployeeVo = new EmployeeVo();
            resignEmployeeVo.setEmployeeNumber(resignEmployeeList.get(i).getErpEmployeeNumber());
            resignEmployeeVo.setUserStatus(UserEnums.EmployeeStatus.RESIGN.getCode());
            resignEmployeeVo.setCreateId(String.valueOf(Constants.BATCH_CREATE_ID));
            userEmployeeBiz.putEmployee(resignEmployeeVo);
        }

        for(ErpEmployeeVo erpApiEmployeeVo : erpEmployeeApiList) {

            newEmployeeYn = true;

            for(ErpEmployeeVo erpEmployeeVo : erpEmployeeAllList) {
                if( erpApiEmployeeVo.getErpEmployeeNumber().equalsIgnoreCase(erpEmployeeVo.getErpEmployeeNumber()) ) {
                    newEmployeeYn = false;
                    break;
                }
            }

            // 신규 임직원
            if( newEmployeeYn ) {

                // UR_ERP_EMPLOYEE 등록
                erpApiEmployeeVo.setUseYn(BaseEnums.UseYn.Y.name());
                erpApiEmployeeVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpEmployeeService.addErpEmployee(erpApiEmployeeVo);
            }else { // 기존 임직원

                // UR_ERP_EMPLOYEE 업데이트
                erpApiEmployeeVo.setCreateId(Constants.BATCH_CREATE_ID);
                erpEmployeeService.putErpEmployee(erpApiEmployeeVo);

                // 상태가 휴직 일 경우 UR_EMPLOYEE 회원상태 휴직으로 업데이트
                if( UserEnums.EmployeeStatus.ADMINISTRATIVE_LEAVE.getCodeName().equalsIgnoreCase(erpApiEmployeeVo.getErpStatusName()) ) {
                    EmployeeVo administrativeLeaveEmployeeVo = new EmployeeVo();
                    administrativeLeaveEmployeeVo.setEmployeeNumber(erpApiEmployeeVo.getErpEmployeeNumber());
                    administrativeLeaveEmployeeVo.setUserStatus(UserEnums.EmployeeStatus.ADMINISTRATIVE_LEAVE.getCode());
                    administrativeLeaveEmployeeVo.setCreateId(String.valueOf(Constants.BATCH_CREATE_ID));
                    userEmployeeBiz.putEmployee(administrativeLeaveEmployeeVo);
                }
            }
        }

        return ApiResult.success();
    }


}
