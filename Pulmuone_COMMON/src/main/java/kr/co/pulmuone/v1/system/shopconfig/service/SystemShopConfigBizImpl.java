package kr.co.pulmuone.v1.system.shopconfig.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.GetSystemShopConfigListResponseDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.basic.SaveSystemShopConfigRequestSaveDto;
import kr.co.pulmuone.v1.system.shopconfig.dto.vo.GetSystemShopConfigListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 상점별세부정책 BizImpl
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 30.              최성현          최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class SystemShopConfigBizImpl implements SystemShopConfigBiz {

    @Autowired
    private SystemShopConfigService systemShopConfigService;

    /**
     * @Desc 상점별 세부정책 조회
     * @param getSystemShopConfigListRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getSystemShopConfigList(GetSystemShopConfigListRequestDto getSystemShopConfigListRequestDto) {


        GetSystemShopConfigListResponseDto getSystemShopConfigListResponseDto = new GetSystemShopConfigListResponseDto();
        Page<GetSystemShopConfigListResultVo> getSystemShopConfigListResultVo = systemShopConfigService.getSystemShopConfigList(getSystemShopConfigListRequestDto);

        getSystemShopConfigListResponseDto.setTotal(getSystemShopConfigListResultVo.getTotal());
        getSystemShopConfigListResponseDto.setRows(getSystemShopConfigListResultVo.getResult());

        return ApiResult.success(getSystemShopConfigListResponseDto);
    }

    /**
     * @Desc 상점별 세부정책 저장,수정,삭제
     * @param saveSystemShopConfigRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> saveSystemShopConfig(SaveSystemShopConfigRequestDto saveSystemShopConfigRequestDto) {

        SaveSystemShopConfigRequestSaveDto result = new SaveSystemShopConfigRequestSaveDto();
        
        List<SaveSystemShopConfigRequestSaveDto> insertRequestDtoList = saveSystemShopConfigRequestDto.getInsertRequestDtoList();
        List<SaveSystemShopConfigRequestSaveDto> updateRequestDtoList = saveSystemShopConfigRequestDto.getUpdateRequestDtoList();
        List<SaveSystemShopConfigRequestSaveDto> deleteRequestDtoList = saveSystemShopConfigRequestDto.getDeleteRequestDtoList();

        //데이터 저장
        if (!insertRequestDtoList.isEmpty()) {
           systemShopConfigService.addShopConfig(insertRequestDtoList);
        }
        //데이터 수정
        if (!updateRequestDtoList.isEmpty()) {
           systemShopConfigService.putShopConfig(updateRequestDtoList);
        }
        //데이터 삭제
        if (!deleteRequestDtoList.isEmpty()) {
           systemShopConfigService.delShopConfig(deleteRequestDtoList);
        }

        return ApiResult.success(result);
    }
    
}
