package kr.co.pulmuone.v1.policy.accessibleip.service;


import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.accessibleip.dto.basic.*;
import kr.co.pulmuone.v1.policy.accessibleip.dto.vo.GetPolicyAccessibleIpListResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * BOS 접근 IP 설정 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    2020. 10. 21.                최성현          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class PolicyAccessibleIpBizImpl implements PolicyAccessibleIpBiz {

    @Autowired
    private PolicyAccessibleIpService policyAccessibleIpService;

    /**
     * @Desc BOS 접근가능 IP 설정 조회
     * @param getPolicyAccessibleIpListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPolicyAccessibleIpList(GetPolicyAccessibleIpListRequestDto getPolicyAccessibleIpListRequestDto) {

        GetPolicyAccessibleIpListResponseDto getPolicyAccessibleIpListResponseDto = new GetPolicyAccessibleIpListResponseDto();
        Page<GetPolicyAccessibleIpListResultVo> getPolicyAccessibleIpListResultList = policyAccessibleIpService.getPolicyAccessibleIpList(getPolicyAccessibleIpListRequestDto);

        getPolicyAccessibleIpListResponseDto.setTotal(getPolicyAccessibleIpListResultList.getTotal());
        getPolicyAccessibleIpListResponseDto.setRows(getPolicyAccessibleIpListResultList.getResult());

        return ApiResult.success(getPolicyAccessibleIpListResponseDto);
    }


    /**
     * @Desc BOS 접근가능 IP 설정 저장, 수정, 삭제
     * @param savePolicyAccessibleIpRequestDto
     * @return ApiResult
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> savePolicyAccessibleIp(SavePolicyAccessibleIpRequestDto savePolicyAccessibleIpRequestDto) {

        SavePolicyAccessibleIpResponseDto result = new SavePolicyAccessibleIpResponseDto();

        List<SavePolicyAccessibleIpRequestSaveDto> insertRequestDtoList = savePolicyAccessibleIpRequestDto.getInsertRequestDtoList();
        List<SavePolicyAccessibleIpRequestSaveDto> updateRequestDtoList = savePolicyAccessibleIpRequestDto.getUpdateRequestDtoList();
        List<SavePolicyAccessibleIpRequestSaveDto> deleteRequestDtoList = savePolicyAccessibleIpRequestDto.getDeleteRequestDtoList();

        MessageCommEnum returnCode = null;
        if (!insertRequestDtoList.isEmpty()) {
            returnCode = this.checkPolicyAccessibleIpDuplicate(insertRequestDtoList);

            if (BaseEnums.Default.SUCCESS != returnCode) {
                return ApiResult.result(returnCode);
            }
        }

        if (!updateRequestDtoList.isEmpty()) {
            returnCode = this.checkPolicyAccessibleIpDuplicate(updateRequestDtoList);

            if (BaseEnums.Default.SUCCESS != returnCode) {
                return ApiResult.result(returnCode);
            }
        }

        if (!deleteRequestDtoList.isEmpty()) {
            returnCode = this.checkPolicyAccessibleIpDuplicate(deleteRequestDtoList);

            if (BaseEnums.Default.SUCCESS != returnCode) {
                return ApiResult.result(returnCode);
            }
        }


        if (BaseEnums.Default.SUCCESS == returnCode) {
            //삽입
            if (!CollectionUtils.isEmpty(insertRequestDtoList)) {
                policyAccessibleIpService.addPolicyAccessibleIp(insertRequestDtoList);
            }
            //수정
            if (!CollectionUtils.isEmpty(updateRequestDtoList)) {
                policyAccessibleIpService.putPolicyAccessibleIp(updateRequestDtoList);
            }
            //삭제
            if (!CollectionUtils.isEmpty(deleteRequestDtoList)) {
                policyAccessibleIpService.delPolicyAccessibleIp(deleteRequestDtoList);
            }
        }


        return ApiResult.success(result);
    }

    /**
     * @Desc 중복 데이터 확인
     * @param SavePolicyAccessibleIpRequestSaveDto
     * @return MessageCommEnum
     */
    private MessageCommEnum checkPolicyAccessibleIpDuplicate(List<SavePolicyAccessibleIpRequestSaveDto> voList) {

        MessageCommEnum returnCode = BaseEnums.Default.SUCCESS;
        int count = 0;
        if (!CollectionUtils.isEmpty(voList)) {
            count = policyAccessibleIpService.checkPolicyAccessibleIpDuplicate(voList);
            if (count > 0) {
                returnCode = BaseEnums.CommBase.DUPLICATE_DATA_DO_REFRESH;
                return returnCode;
            }
        }

        return returnCode;
    }
}
