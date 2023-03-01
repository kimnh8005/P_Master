package kr.co.pulmuone.v1.comm.mapper.policy.shippingtemplate;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateManagementRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.ShippingTemplateRequestDto;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingConditionVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingTemplateVo;
import kr.co.pulmuone.v1.policy.shippingtemplate.dto.vo.ShippingWarehouseVo;

@Mapper
public interface PolicyShippingTemplateMapper {

    /**
     * @Desc 출고처기준 배송정책 설정 조회
     * @param shippingTemplateRequestDto
     * @return Page<ShippingTemplateVo>
     */
    Page<ShippingTemplateVo> getWarehousePeriodShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto);

    /**
     * @Desc 배송정책 설정 조회
     * @param shippingTemplateRequestDto
     * @return Page<ShippingTemplateVo>
     */
    Page<ShippingTemplateVo> getShippingTemplateList(ShippingTemplateRequestDto shippingTemplateRequestDto);

    /**
     * @Desc 배송비 탬플릿 상세 조회
     * @param shippingTemplateManagementRequestDto
     * @return ShippingTemplateVo
     */
    ShippingTemplateVo getShippingTemplateInfo(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);

    /**
     * @Desc 배송비 출고처 정보 조회
     * @param shippingTemplateManagementRequestDto
     * @return List<ShippingWarehouseVo>
     */
    List<ShippingWarehouseVo> getShippingWarehouseList(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);

    /**
     * @Desc 배송비 조건 정보 조회
     * @param shippingTemplateManagementRequestDto
     * @return List<ShippingConditionVo>
     */
    List<ShippingConditionVo> getShippingConditionList(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);

    /**
     * @Desc 배송정책 탬플릿 등록
     * @param shippingTemplateVo
     * @return int
     */
    int addShippingTemplate(ShippingTemplateVo shippingTemplateVo);

    /**
     * @Desc 배송비 탬플릿 원본 정책번호 업데이트
     * @param shippingTemplateVo
     * @return int
     */
    int putOriginalShippingTemplateId(ShippingTemplateVo shippingTemplateVo);

    /**
     * @Desc 배송비 출고처정보 동일한 출고처 기본유무 N 으로 변경
     * @param shippingWarehouseVo
     * @return int
     */
    int putShippingWarehouseBasicInitialization(ShippingWarehouseVo shippingWarehouseVo);

    /**
     * @Desc 배송비 출고처정보 등록
     * @param shippingWarehouseVo
     * @return int
     */
    int addShippingWarehouse(ShippingWarehouseVo shippingWarehouseVo);

    /**
     * @Desc 배송비 조건정보 등록
     * @param shippingConditionVo
     * @return int
     */
    int addShippingCondition(ShippingConditionVo shippingConditionVo);

    /**
     * @Desc 배송비 탬플릿 삭제여부 업데이트
     * @param shippingTemplateVo
     * @return int
     */
    int putShippingTemplateDeleteYn(ShippingTemplateVo shippingTemplateVo);

    /**
     * @Desc 상품 배송비 관계 출고처 유무 체크
     * @param shippingTemplateManagementRequestDto
     * @return Boolean
     */
    Boolean getGoodsShippingTemplateYn(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);

    /**
     * @Desc 배송정책 출고처 기본여부 체크
     * @param shippingTemplateManagementRequestDto
     * @return Boolean
     */
    Boolean getShippingWarehouseBasicYnCheck(ShippingTemplateManagementRequestDto shippingTemplateManagementRequestDto);
}
