package kr.co.pulmuone.v1.policy.shippingtemplate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.policy.shippingtemplate.PolicyShippingTemplateMapper;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;
import lombok.RequiredArgsConstructor;


/**
* <PRE>
* Forbiz Korea
* 배송정책설정 Service
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
@RequiredArgsConstructor
public class PolicyShippingTemplateService {

    private final PolicyShippingTemplateMapper policyShippingTemplateMapper;

    /**
     * @Desc 출고처기준 배송정책 설정 조회
     * @param shippingTemplateRequestDto
     * @return Page<ShippingTemplateVo>
     */
    protected Page<ShippingTemplateVo> getWarehousePeriodShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto){
        PageMethod.startPage(shippingTemplateRequestDto.getPage(), shippingTemplateRequestDto.getPageSize());
        return policyShippingTemplateMapper.getWarehousePeriodShippingTemplateList(shippingTemplateRequestDto);
    }

    /**
     * @Desc 배송정책 설정 조회
     * @param shippingTemplateRequestDto
     * @return Page<ShippingTemplateVo>
     */
    protected Page<ShippingTemplateVo> getShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto){
        PageMethod.startPage(shippingTemplateRequestDto.getPage(), shippingTemplateRequestDto.getPageSize());
        return policyShippingTemplateMapper.getShippingTemplateList(shippingTemplateRequestDto);
    }

    /**
     * @Desc 배송비 탬플릿 상세 조회
     * @param shippingTemplateManagementRequestDto
     * @return ShippingTemplateVo
     */
    protected ShippingTemplateVo getShippingTemplateInfo(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) {
        return policyShippingTemplateMapper.getShippingTemplateInfo(shippingTemplateManagementRequestDto);
    }

    /**
     * @Desc 배송비 출고처 정보 조회
     * @param shippingTemplateManagementRequestDto
     * @return List<ShippingWarehouseVo>
     */
    protected List<ShippingWarehouseVo> getShippingWarehouseList(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        return policyShippingTemplateMapper.getShippingWarehouseList(shippingTemplateManagementRequestDto);
    }

    /**
     * @Desc 배송비 조건 정보 조회
     * @param shippingTemplateManagementRequestDto
     * @return List<ShippingConditionVo>
     */
    protected List<ShippingConditionVo> getShippingConditionList(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto){
        return policyShippingTemplateMapper.getShippingConditionList(shippingTemplateManagementRequestDto);
    }

    /**
     * @Desc 배송정책 탬플릿 등록
     * @param shippingTemplateVo
     * @return int
     */
    protected int addShippingTemplate(ShippingTemplateVo shippingTemplateVo) {
        return policyShippingTemplateMapper.addShippingTemplate(shippingTemplateVo);
    }

    /**
     * @Desc 배송비 탬플릿 원본 정책번호 업데이트
     * @param shippingTemplateVo
     * @return int
     */
    protected int putOriginalShippingTemplateId(ShippingTemplateVo shippingTemplateVo) {
        return policyShippingTemplateMapper.putOriginalShippingTemplateId(shippingTemplateVo);
    }

    /**
     * @Desc 배송비 출고처정보 동일한 출고처 기본유무 N 으로 변경
     * @param shippingWarehouseVo
     * @return int
     */
    protected int putShippingWarehouseBasicInitialization(ShippingWarehouseVo shippingWarehouseVo) {
        return policyShippingTemplateMapper.putShippingWarehouseBasicInitialization(shippingWarehouseVo);
    }

    /**
     * @Desc 배송비 출고처정보 등록
     * @param shippingWarehouseVo
     * @return int
     */
    protected int addShippingWarehouse(ShippingWarehouseVo shippingWarehouseVo) {
        return policyShippingTemplateMapper.addShippingWarehouse(shippingWarehouseVo);
    }

    /**
     * @Desc 배송비 조건정보 등록
     * @param shippingConditionVo
     * @return int
     */
    protected int addShippingCondition(ShippingConditionVo shippingConditionVo) {
        return policyShippingTemplateMapper.addShippingCondition(shippingConditionVo);
    }

    /**
     * @Desc 배송비 탬플릿 삭제여부 업데이트
     * @param shippingTemplateVo
     * @return int
     */
    protected int putShippingTemplateDeleteYn(ShippingTemplateVo shippingTemplateVo) {
        return policyShippingTemplateMapper.putShippingTemplateDeleteYn(shippingTemplateVo);
    }

    /**
     * @Desc 상품 배송비 관계 출고처 유무 체크
     * @param shippingTemplateManagementRequestDto
     * @return Boolean
     */
    protected Boolean getGoodsShippingTemplateYn(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) {
        return policyShippingTemplateMapper.getGoodsShippingTemplateYn(shippingTemplateManagementRequestDto);
    }

    /**
     * @Desc 배송정책 출고처 기본여부 체크
     * @param shippingTemplateManagementRequestDto
     * @return Boolean
     */
    protected Boolean getShippingWarehouseBasicYnCheck(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto) {
        return policyShippingTemplateMapper.getShippingWarehouseBasicYnCheck(shippingTemplateManagementRequestDto);
    }

    /**
     * @Desc 배송비 조건정보 목록 셋팅
     *       배송비 조건정보에 배송비 금액은 해당 조건을 만족했을때에 금액이 들어간다.
     *       EX) 화면에서     조건        배송비
     *                        10000 미만   3000 원
     *                        20000 미만   2000 원
     *           이런 형태로 값이 넘어오면 DB 에는
     *                        10000       2000
     *                        20000       0
     *           이렇게 저장되어야 한다. 3000 원은 IL_SHIPPING_TEMPLATE.SHIPPING_PRICE 에 저장
     * @param shippingTemplateVo
     * @param shippingConditionList
     * @return List<ShippingConditionVo>
     */
    protected List<ShippingConditionVo> shippingConditionListSetting(ShippingTemplateVo shippingTemplateVo, List<ShippingConditionVo>  shippingConditionList) {
        List<ShippingConditionVo> returnShippingConditionList = new ArrayList<ShippingConditionVo>();
        Long shippingConditionId = Long.valueOf(1);
        long nextShippingPrice = 0;

        for(int i = 0, rowCnt = shippingConditionList.size(); i < rowCnt; i++) {

            if( shippingConditionId.compareTo( Long.valueOf(rowCnt) ) == 0 ) {

                nextShippingPrice = 0;
            }else {

                nextShippingPrice = shippingConditionList.get( shippingConditionId.intValue() ).getShippingPrice();
            }

            ShippingConditionVo shippingConditionVo = new ShippingConditionVo();
            shippingConditionVo.setShippingTemplateId(shippingTemplateVo.getShippingTemplateId());
            shippingConditionVo.setShippingConditionId(shippingConditionId);
            shippingConditionVo.setShippingPrice(nextShippingPrice);
            shippingConditionVo.setConditionValue(shippingConditionList.get(i).getConditionValue());
            shippingConditionVo.setCreateId(shippingTemplateVo.getCreateId());

            returnShippingConditionList.add(shippingConditionVo);

            shippingConditionId++;
        }

        return returnShippingConditionList;
    }

}
