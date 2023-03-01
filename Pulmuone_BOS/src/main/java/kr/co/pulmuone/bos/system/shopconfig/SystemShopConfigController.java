package kr.co.pulmuone.bos.system.shopconfig;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.config.SiteConfig;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestSaveDto;
import kr.co.pulmuone.v1.system.shopconfig.service.SystemShopConfigBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 상점별 세부정책 Controller
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20201029    최성현                 최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class SystemShopConfigController {

    @Autowired
    private SystemShopConfigBiz systemShopConfigBiz;

    @ApiOperation(value = "상점별 세부정책 리스트 조회")
    @PostMapping(value = "/admin/system/shopconfig/getShopConfigList")
    public ApiResult<?> getShopConfigList(HttpServletRequest request, GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto) throws Exception{

        getSystemShopConfigListRequestDto = (GetSystemShopConfigListRequestDto) BindUtil.convertRequestToObject(request, GetSystemShopConfigListRequestDto.class);

        return systemShopConfigBiz.getSystemShopConfigList(getSystemShopConfigListRequestDto);
    }

    @ApiOperation(value = "상점별 세부정책 저장")
    @PostMapping(value = "/admin/system/shopconfig/saveShopConfig")
     public ApiResult<?> saveShopConfig(SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto)throws Exception{

        //binding data
        saveSystemShopConfigRequestDto.setInsertRequestDtoList((List<SaveSystemShopConfigRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(saveSystemShopConfigRequestDto.getInsertData(), SaveSystemShopConfigRequestSaveDto.class));
        saveSystemShopConfigRequestDto.setUpdateRequestDtoList((List<SaveSystemShopConfigRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(saveSystemShopConfigRequestDto.getUpdateData(), SaveSystemShopConfigRequestSaveDto.class));
        saveSystemShopConfigRequestDto.setDeleteRequestDtoList((List<SaveSystemShopConfigRequestSaveDto>) BindUtil.convertJsonArrayToDtoList(saveSystemShopConfigRequestDto.getDeleteData(), SaveSystemShopConfigRequestSaveDto.class));

        // 캐쉬를 갱신한다.
        SiteConfig.reload();

        return systemShopConfigBiz.saveSystemShopConfig(saveSystemShopConfigRequestDto);
    }

    /**
     * 상점별 세부정책 그룹 리스트 조회
     * @param getSystemShopConfigListRequestDto
     * @return
     */
    @ApiOperation(value = "상점별 세부정책 그룹 리스트 조회")
    @PostMapping(value = "/admin/system/shopconfig/getGroupShopConfigList")
    public ApiResult<?> getGroupShopConfigList(GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto) {
        return systemShopConfigBiz.getSystemShopConfigList(getSystemShopConfigListRequestDto);
    }
}
