package kr.co.pulmuone.batch.erp.domain.service.user.employee;

import kr.co.pulmuone.batch.erp.common.Constants;
import kr.co.pulmuone.batch.erp.common.enums.BaseEnums;
import kr.co.pulmuone.batch.erp.common.enums.SystemEnums;
import kr.co.pulmuone.batch.erp.common.enums.UserEnums;
import kr.co.pulmuone.batch.erp.domain.model.base.dto.vo.UserVo;
import kr.co.pulmuone.batch.erp.domain.model.system.itgc.dto.ItgcRequestDto;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.EmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpEmployeeBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpOrganizationBatchVo;
import kr.co.pulmuone.batch.erp.domain.model.user.employee.dto.vo.ErpRegalBatchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserEmployeeByErpBatchBizImpl implements UserEmployeeByErpBatchBiz {

    @Autowired
    private UserEmployeeByErpBatchService userEmployeeByErpBatchService;


    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { Exception.class })
    public void synchEmployeeByErp() {

		sysncEmployeeInfoByErp();	//풀무원 임직원 정보 동기화(By 풀무원 임직원 )
		sysncRegalInfoByErp();		//풀무원 법인 정보 동기화(By 풀무원 법인 )
		sysncOrganizationInfoByErp();	//풀무원 조직 정보 동기화(By 풀무원 조직)

    }


    /**
     * @Desc 풀무원 정보 동기화(By ERP)
     * @return void
     */
    private void sysncEmployeeInfoByErp() {

    	List<ErpEmployeeBatchVo> pulmuoneEmployeeList = userEmployeeByErpBatchService.getSyncTargetPulmuoneEmployeeList();	//풀무원 동기화 대상 임직원정보
    	List<ErpEmployeeBatchVo> erpEmployeeList = userEmployeeByErpBatchService.getErpEmployeeList();	//ERP 임직원정보

    	updateResign(pulmuoneEmployeeList, erpEmployeeList);	//퇴사처리

    	synchEmployeeInfo(erpEmployeeList); //회원정보 동기화 처리
    }


    /**
     * @Desc 풀무원 임직원 퇴사 처리
     * @param pulmuoneEmployeeList
     * @param erpEmployeeList
     * @return void
     */
    private void updateResign(List<ErpEmployeeBatchVo> pulmuoneEmployeeList, List<ErpEmployeeBatchVo> erpEmployeeList) {
    	//풀무원 존재 && ERP 비존재(ERP 에서 삭제된 경우)
    	List<ErpEmployeeBatchVo> resignEmployeeList = pulmuoneEmployeeList.stream()
    								                .filter(e -> !erpEmployeeList.stream()
    								                                                .map(ErpEmployeeBatchVo::getErpEmployeeNumber)
    								                                                .collect(Collectors.toList())
    								                                                .contains( e.getErpEmployeeNumber() ) )
    								                .collect(Collectors.toList());

    	//퇴사 처리
    	for(ErpEmployeeBatchVo employeeBatchVo : resignEmployeeList) {

            //ERP 임직원 정보(UR_ERP_EMPLOYEE) 상태만 (퇴사로) 업데이트
    		ErpEmployeeBatchVo resignErpEmployeeBatchVo = new ErpEmployeeBatchVo();
    		resignErpEmployeeBatchVo.setErpEmployeeNumber(employeeBatchVo.getErpEmployeeNumber());
    		resignErpEmployeeBatchVo.setErpStatusName(UserEnums.EmployeeStatus.RESIGN.getCode());
    		resignErpEmployeeBatchVo.setCreateId(Constants.BATCH_CREATE_ID);
    		resignErpEmployeeBatchVo.setUseYn(BaseEnums.UseYn.N.name());
    		userEmployeeByErpBatchService.addErpEmployee(resignErpEmployeeBatchVo);

    		//회원상세정보(UR_EMPLOYEE) 상태 퇴사로 업데이트
    		EmployeeBatchVo resignEmployeeVo = new EmployeeBatchVo();
    		resignEmployeeVo.setUserStatus(UserEnums.EmployeeStatus.RESIGN.getCode());
    		resignEmployeeVo.setEmployeeNumber(employeeBatchVo.getErpEmployeeNumber());
    		resignEmployeeVo.setCreateId(String.valueOf(Constants.BATCH_CREATE_ID));
    		userEmployeeByErpBatchService.putEmployee(resignEmployeeVo);
    	}

    	// ITGC 등록 - 퇴사정보
		List<ItgcRequestDto> itgcList = resignEmployeeList.stream()
				.map(vo -> ItgcRequestDto.builder()
					.stMenuId(SystemEnums.ItgcMenu.BATCH.getStMenuId())
					.itgcType(SystemEnums.ItgcType.ADMIN_DROP.getCode())
					.itgcDetail(SystemEnums.ItgcDetailType.ADMIN_DROP.getCodeName())
					.itgcAddType(SystemEnums.ItgcAddType.DEL.getCode())
					.targetInfo(vo.getErpEmployeeNumber() + " : " + vo.getErpEmployeeName())
					.createId(Constants.BATCH_CREATE_ID)
					.build())
				.collect(Collectors.toList());
    	if(!itgcList.isEmpty()){
    		userEmployeeByErpBatchService.addItgcList(itgcList);
		}
    	
    }



    /**
     * @Desc 회원정보 동기화 처리
     * @param erpEmployeeList
     * @return void
     */
    private void synchEmployeeInfo(List<ErpEmployeeBatchVo> erpEmployeeList) {

    	List<ErpEmployeeBatchVo> pulmuoneEmployeeList = userEmployeeByErpBatchService.getSyncExceptionTargetPulmuoneEmployeeList();	//풀무원 임직원정보(퇴사자, 수동관리 임직원)

    	//ERP 데이터중 풀무원 퇴사자 및 수동처리 대상인 임직원 정보 제외
    	List<ErpEmployeeBatchVo> erpNonResignEmployeeList = erpEmployeeList.stream()
    								                .filter(e -> !pulmuoneEmployeeList.stream()
    								                                                .map(ErpEmployeeBatchVo::getErpEmployeeNumber)
    								                                                .collect(Collectors.toList())
    								                                                .contains( e.getErpEmployeeNumber() ))
    								                .collect(Collectors.toList());

    	for(ErpEmployeeBatchVo erpEmployeeVo : erpNonResignEmployeeList) {
    	    //ERP 상태 명 풀무원 코드값으로 받기
    	    String statusCode = getErpUserStatus(erpEmployeeVo.getErpStatusName());

    	    //임직원 정보 회원정보 동기화
    	    erpEmployeeVo.setErpStatusName(statusCode);
    	    erpEmployeeVo.setUseYn(BaseEnums.UseYn.Y.name());
    		userEmployeeByErpBatchService.addErpEmployee(erpEmployeeVo);

    		//회원상세정보(UR_EMPLOYEE) 상태 동기화
    		EmployeeBatchVo resignEmployeeVo = new EmployeeBatchVo();
    		resignEmployeeVo.setEmployeeNumber(erpEmployeeVo.getErpEmployeeNumber());
    		resignEmployeeVo.setCreateId(String.valueOf(Constants.BATCH_CREATE_ID));
    		resignEmployeeVo.setRegalName(erpEmployeeVo.getErpRegalName()); 		//법인명
			resignEmployeeVo.setPositionName(erpEmployeeVo.getErpPositionName()); 	//직책명
			resignEmployeeVo.setOrganizationName(erpEmployeeVo.getErpOrganizationName()); //조직명
    		//임직원 상태가 정상이 아닌 경우만 update(휴직||퇴사)
    		if(!UserEnums.EmployeeStatus.NORMAL.getCode().equals(statusCode)) {
    			resignEmployeeVo.setUserStatus(statusCode);
    		}
			int rtn = userEmployeeByErpBatchService.putEmployee(resignEmployeeVo);

			//존재할 경우
			if(rtn == 1) {
				//이름(UR_USER) 동기화
	            UserVo userVo = userEmployeeByErpBatchService.getUserInfo(erpEmployeeVo.getErpEmployeeNumber()); // 회원 기본정보 조회
	            if(!erpEmployeeVo.getErpEmployeeName().equals(userVo.getLoginName()) ) {	//존재할 경우
	                // 회원 기본정보 이름 수정
	                UserVo userParamVo = new UserVo();
	                userParamVo.setUserId(userVo.getUserId());
	                userParamVo.setLoginName(erpEmployeeVo.getErpEmployeeName());

	                userEmployeeByErpBatchService.putUserName(userParamVo);
	            }
			}
     	}
    }


    /**
     * @Desc ERP 회원상태 값 -> Pulmuone 회원상태값 받기
     * @param userStatus
     * @return
     * @return String
     */
    private static String getErpUserStatus(String userStatus) {
    	String code = "";

    	if(UserEnums.ErpEmployeeStatus.NORMAL1.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.NORMAL1.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.NORMAL2.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.NORMAL2.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.RESIGN1.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.RESIGN1.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.RESIGN2.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.RESIGN2.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.RESIGN3.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.RESIGN3.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.RESIGN4.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.RESIGN4.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.ADMINISTRATIVE_LEAVE1.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.ADMINISTRATIVE_LEAVE1.getCode();
    	} else if(UserEnums.ErpEmployeeStatus.ADMINISTRATIVE_LEAVE2.getCodeName().equalsIgnoreCase(userStatus)) {
    		code = UserEnums.ErpEmployeeStatus.ADMINISTRATIVE_LEAVE2.getCode();
    	}

    	return code;
    }



    /**
     * @Desc 풀무원 법인 정보 동기화(By 풀무원 법인 )
     * @return void
     */
    private void sysncRegalInfoByErp() {
    	List<ErpRegalBatchVo> erpRegalApiList = userEmployeeByErpBatchService.getErpRegalList();

    	for(ErpRegalBatchVo erpRegalVo : erpRegalApiList) {
    		erpRegalVo.setCreateId(Constants.BATCH_CREATE_ID);
    		userEmployeeByErpBatchService.addErpRegal(erpRegalVo);
    	}
    }



    /**
     * @Desc 풀무원 조직 정보 동기화(By 풀무원 조직 )
     * @return void
     */
    private void sysncOrganizationInfoByErp() {
    	List<ErpOrganizationBatchVo> erpOrganizationApiList = userEmployeeByErpBatchService.getErpOrganizationList();

        for(ErpOrganizationBatchVo erpOrganizationVo : erpOrganizationApiList) {
        	erpOrganizationVo.setCreateId(Constants.BATCH_CREATE_ID);
        	userEmployeeByErpBatchService.addErpOrganization(erpOrganizationVo);
        }
    }

}
