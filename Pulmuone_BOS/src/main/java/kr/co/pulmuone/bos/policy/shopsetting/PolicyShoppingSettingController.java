package kr.co.pulmuone.bos.policy.shopsetting;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.FileVo;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.GetPolicyShopSettingListRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.PutPolicyShopSettingRequestDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.dto.PutPolicyShopSettingRequestSaveDto;
import kr.co.pulmuone.v1.policy.shoppingsetting.service.PolicyShopSettingBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * <PRE>
 * Forbiz Korea
 * 쇼핑몰 설정 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    202001023    최성현              최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class PolicyShoppingSettingController {

    @Autowired
    private PolicyShopSettingBiz policyShopSettingBiz;


    @ApiOperation(value = "상점별 세부정책 리스트 조회")
    @PostMapping(value = "/admin/policy/shopsetting/getShopSettingList")
    public ApiResult<?> getShopConfigList(HttpServletRequest request, GetPolicyShopSettingListRequestDto getPolicyShopSettingListRequestDto) throws Exception {
            //getPolicyShopSettingListRequestDto = (GetPolicyShopSettingListRequestDto) BindUtil.convertRequestToObject(request, GetPolicyShopSettingListRequestDto.class);
            return policyShopSettingBiz.getPolicyShopSettingList(getPolicyShopSettingListRequestDto);


    }

    @PostMapping(value = "/admin/policy/shopsetting/putShopSetting")
    @ApiOperation(value = "상점별 세부정책 저장")
    public ApiResult<?> saveShopConfig(PutPolicyShopSettingRequestDto putPolicyShopSettingRequestDto) throws Exception {


            //binding data
            putPolicyShopSettingRequestDto.setUpdateRequestDtoList( BindUtil.convertJsonArrayToDtoList(putPolicyShopSettingRequestDto.getUpdateData(), PutPolicyShopSettingRequestSaveDto.class));

            putPolicyShopSettingRequestDto.setAddFileList(BindUtil.convertJsonArrayToDtoList(putPolicyShopSettingRequestDto.getAddFile(), FileVo.class));

            return policyShopSettingBiz.putPolicyShopSetting(putPolicyShopSettingRequestDto);

    }
}
