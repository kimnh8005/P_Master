package kr.co.pulmuone.v1.policy.shippingtemplate.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementResponseDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateResponseDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;


/**
* <PRE>
* Forbiz Korea
* 배송정책설정 BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 9. 28.                손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class PolicyShippingTemplateBizImpl  implements PolicyShippingTemplateBiz {

    @Autowired
    PolicyShippingTemplateService policyShippingTemplateService;

    /**
     * @Desc 배송정책 설정 조회
     * @param shippingTemplateRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    public ApiResult<?> getShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto) throws Exception{
        ShippingTemplateResponseDto shippingTemplateResponseDto = new ShippingTemplateResponseDto();
        Page<ShippingTemplateVo> shippingTemplateList;

        if(BaseEnums.UseYn.Y.name().equalsIgnoreCase(shippingTemplateRequestDto.getWarehouseViewYn()) ) {
            shippingTemplateList = policyShippingTemplateService.getWarehousePeriodShippingTemplateList(shippingTemplateRequestDto);
        }else {
            shippingTemplateList = policyShippingTemplateService.getShippingTemplateList(shippingTemplateRequestDto);
        }

        shippingTemplateResponseDto.setTotal(shippingTemplateList.getTotal());
        shippingTemplateResponseDto.setRows(shippingTemplateList.getResult());

        return ApiResult.success(shippingTemplateResponseDto);
    }

    /**
     * @Desc 배송정책 상세 정보 조회
     * @param shippingTemplateManagementRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        ShippingTemplateManagementResponseDto shippingTemplateManagementResponseDto = new ShippingTemplateManagementResponseDto();

        ShippingTemplateVo shippingTemplateInfo = policyShippingTemplateService.getShippingTemplateInfo(shippingTemplateManagementRequestDto);
        List<ShippingWarehouseVo> shippingWarehouseList = policyShippingTemplateService.getShippingWarehouseList(shippingTemplateManagementRequestDto);
        List<ShippingConditionVo> shippingConditionList = policyShippingTemplateService.getShippingConditionList(shippingTemplateManagementRequestDto);

        shippingTemplateManagementResponseDto.setShippingTemplateInfo(shippingTemplateInfo);
        shippingTemplateManagementResponseDto.setShippingWarehouseList(shippingWarehouseList);
        shippingTemplateManagementResponseDto.setShippingConditionList(shippingConditionList);

        return ApiResult.success(shippingTemplateManagementResponseDto);
    }

    /**
     * @Desc  배송정책 등록
     *        1. 배송비조건정보 조건값 기준으로 ASC 정렬
     *        2. 배송비 탬플릿 등록
     *        3. 배송비 탬플릿 원본 정책번호 업데이트
     *        4. 배송비 출고처 정보 등록
     *        5. 배송비 조건정보 등록 ( 조건 배송비 구분이 결제금액별 배송비 일 경우만 등록 )
     * @param shippingTemplateManagementRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception{

        // 배송비조건정보 조건값 기준으로 ASC 정렬
        List<ShippingConditionVo> shippingConditionList = shippingTemplateManagementRequestDto.getShippingConditionList();
        shippingConditionList.sort(Comparator.comparing(ShippingConditionVo::getConditionValue));

        // 배송정책 탬플릿 등록
        ShippingTemplateVo shippingTemplateInfoVo = this.setShippingTemplateParamVo(shippingTemplateManagementRequestDto, shippingConditionList);
        policyShippingTemplateService.addShippingTemplate(shippingTemplateInfoVo);

        // 배송비 탬플릿 원본 정책번호 업데이트
        ShippingTemplateVo putOriginalShippingTemplateVo = new ShippingTemplateVo();
        putOriginalShippingTemplateVo.setShippingTemplateId(shippingTemplateInfoVo.getShippingTemplateId());
        putOriginalShippingTemplateVo.setOriginalShippingTemplateId(shippingTemplateInfoVo.getShippingTemplateId());
        putOriginalShippingTemplateVo.setCreateId(shippingTemplateInfoVo.getCreateId());
        policyShippingTemplateService.putOriginalShippingTemplateId(putOriginalShippingTemplateVo);

        // 배송비 출고처정보 등록
        this.addShippingWarehouseList(shippingTemplateInfoVo, shippingTemplateManagementRequestDto.getShippingWarehouseList());

        // 배송비 조건정보 등록
        if( GoodsEnums.ConditionType.TYPE3.getCode().equals(shippingTemplateManagementRequestDto.getConditionTypeCode()) ) {
            List<ShippingConditionVo> settingShippingConditionList = policyShippingTemplateService.shippingConditionListSetting(shippingTemplateInfoVo, shippingConditionList);

            for(ShippingConditionVo settingShippingConditionVo : settingShippingConditionList) {
                policyShippingTemplateService.addShippingCondition(settingShippingConditionVo);
            }
        }

        return ApiResult.success();
    }

    /**
     * @Desc  배송정책 수정
     *        1. 배송비조건정보 조건값 기준으로 ASC 정렬
     *        2. 배송비 탬플릿 삭제여부 업데이트
     *        3. 배송비 탬플릿 등록
     *        4. 배송비 출고처 정보 등록
     *        5. 배송비 조건정보 등록 ( 조건 배송비 구분이 결제금액별 배송비 일 경우만 등록 )
     * @param shippingTemplateManagementRequestDto
     * @return ApiResult
     * @throws Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putShippingTemplate(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) throws Exception{

        // 배송비조건정보 조건값 기준으로 ASC 정렬
        List<ShippingConditionVo> shippingConditionList = shippingTemplateManagementRequestDto.getShippingConditionList();
        shippingConditionList.sort(Comparator.comparing(ShippingConditionVo::getConditionValue));

        // 배송비 탬플릿 삭제여부 업데이트
        ShippingTemplateVo putShippingTemplateDeleteVo = new ShippingTemplateVo();
        putShippingTemplateDeleteVo.setShippingTemplateId(shippingTemplateManagementRequestDto.getShippingTemplateId());
        putShippingTemplateDeleteVo.setCreateId(shippingTemplateManagementRequestDto.getUserVo().getUserId());
        policyShippingTemplateService.putShippingTemplateDeleteYn(putShippingTemplateDeleteVo);

        // 배송정책 탬플릿 등록
        ShippingTemplateVo shippingTemplateInfoVo = this.setShippingTemplateParamVo(shippingTemplateManagementRequestDto, shippingConditionList);
        policyShippingTemplateService.addShippingTemplate(shippingTemplateInfoVo);

        // 배송비 출고처정보 등록
        this.addShippingWarehouseList(shippingTemplateInfoVo, shippingTemplateManagementRequestDto.getShippingWarehouseList());

        // 배송비 조건정보 등록
        if( GoodsEnums.ConditionType.TYPE3.getCode().equals(shippingTemplateManagementRequestDto.getConditionTypeCode()) ) {
            List<ShippingConditionVo> settingShippingConditionList = policyShippingTemplateService.shippingConditionListSetting(shippingTemplateInfoVo, shippingConditionList);

            for(ShippingConditionVo settingShippingConditionVo : settingShippingConditionList) {
                policyShippingTemplateService.addShippingCondition(settingShippingConditionVo);
            }
        }

        return ApiResult.success();
    }

    /**
     * @Desc 배송정책 탬플릿 파라미터 셋팅
     * @param shippingTemplateManagementRequestDto
     * @param shippingConditionList
     * @return ShippingTemplateVo
     */
    private ShippingTemplateVo setShippingTemplateParamVo(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto, List<ShippingConditionVo>  shippingConditionList) {
        ShippingTemplateVo shippingTemplateVo = new ShippingTemplateVo();
        shippingTemplateVo.setOriginalShippingTemplateId(shippingTemplateManagementRequestDto.getOriginalShippingTemplateId());
        shippingTemplateVo.setShippingTemplateName(shippingTemplateManagementRequestDto.getShippingTemplateName());
        shippingTemplateVo.setPaymentMethodType(shippingTemplateManagementRequestDto.getPaymentMethodType());
        shippingTemplateVo.setBundleYn(shippingTemplateManagementRequestDto.getBundleYn());
        shippingTemplateVo.setConditionTypeCode(shippingTemplateManagementRequestDto.getConditionTypeCode());
        shippingTemplateVo.setShippingPrice(shippingConditionList.get(0).getShippingPrice());
        shippingTemplateVo.setClaimShippingPrice(shippingTemplateManagementRequestDto.getClaimShippingPrice());
        shippingTemplateVo.setUndeliverableAreaTp(shippingTemplateManagementRequestDto.getUndeliverableAreaTp());
        shippingTemplateVo.setAreaShippingDeliveryYn(shippingTemplateManagementRequestDto.getAreaShippingDeliveryYn());
        shippingTemplateVo.setAreaShippingYn(shippingTemplateManagementRequestDto.getAreaShippingYn());
        shippingTemplateVo.setJejuShippingPrice(shippingTemplateManagementRequestDto.getJejuShippingPrice());
        shippingTemplateVo.setIslandShippingPrice(shippingTemplateManagementRequestDto.getIslandShippingPrice());
        shippingTemplateVo.setCreateId(shippingTemplateManagementRequestDto.getUserVo().getUserId());

        return shippingTemplateVo;
    }

    /**
     * @Desc 배송비 출고처정보 등록
     *       1. 기본여부가 Y 일 경우 동일한 출고처번호에 해당하는 기본여부 N 으로 변경
     *       2. 배송비 출고처정보 등록
     * @param shippingTemplateInfo
     * @param shippingWarehouseList
     * @return void
     */
    private void addShippingWarehouseList(ShippingTemplateVo shippingTemplateInfo, List<ShippingWarehouseVo>  shippingWarehouseList) {
        Long shippingWarehouseId = Long.valueOf(1);

        for(int i = 0, rowCnt = shippingWarehouseList.size(); i < rowCnt; i++) {
            ShippingWarehouseVo shippingWarehouseVo = new ShippingWarehouseVo();
            shippingWarehouseVo.setShippingTemplateId(shippingTemplateInfo.getShippingTemplateId());
            shippingWarehouseVo.setShippingWarehouseId(shippingWarehouseId);
            shippingWarehouseVo.setWarehouseId(shippingWarehouseList.get(i).getWarehouseId());
            shippingWarehouseVo.setBasicYn( shippingWarehouseList.get(i).isBasicYnCheck() ? BaseEnums.UseYn.Y.name() : BaseEnums.UseYn.N.name() );
            shippingWarehouseVo.setCreateId(shippingTemplateInfo.getCreateId());

            // 기본여부가 Y 일 경우 동일한 출고처번호에 해당하는 기본여부 N 으로 변경
            if( BaseEnums.UseYn.Y.name().equalsIgnoreCase(shippingWarehouseVo.getBasicYn()) ) {

                policyShippingTemplateService.putShippingWarehouseBasicInitialization(shippingWarehouseVo);
            }

            // 배송비 출고처정보 등록
            policyShippingTemplateService.addShippingWarehouse(shippingWarehouseVo);

            shippingWarehouseId++;
        }
    }

    /**
     * @Desc 상품 배송비 관계 출고처 유무 체크
     * @param shippingTemplateManagementRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getGoodsShippingTemplateYn(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        ShippingTemplateManagementResponseDto shippingTemplateManagementResponseDto = new ShippingTemplateManagementResponseDto();

        Boolean goodsShippingTemplateUseYn = policyShippingTemplateService.getGoodsShippingTemplateYn(shippingTemplateManagementRequestDto);

        shippingTemplateManagementResponseDto.setGoodsShippingTemplateUseYn(goodsShippingTemplateUseYn);

        return ApiResult.success(shippingTemplateManagementResponseDto);
    }

    /**
     * @Desc 배송정책 출고처 기본여부 체크
     * @param shippingTemplateManagementRequestDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getShippingWarehouseBasicYnCheck(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        ShippingTemplateManagementResponseDto shippingTemplateManagementResponseDto = new ShippingTemplateManagementResponseDto();

        Boolean otherShippingWarehouseBasicYn = policyShippingTemplateService.getShippingWarehouseBasicYnCheck(shippingTemplateManagementRequestDto);

        shippingTemplateManagementResponseDto.setOtherShippingWarehouseBasicYn(otherShippingWarehouseBasicYn);

        return ApiResult.success(shippingTemplateManagementResponseDto);
    }
}
