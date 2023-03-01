package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsApprovalResultVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ErpLinkMasterItemVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ErpNotLinkMasterItemVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.ItemApprovalResultVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "마스터 품목 정보 response dto")
public class MasterItemResponseDto {

    /*
     * 마스터 품목 등록 화면에서 사용
     */

    // ERP 연동 마스터 품목
    private ErpLinkMasterItemVo getErpLinkMasterItemVo;

    // ERP 미연동 마스터 품목
    private ErpNotLinkMasterItemVo getErpNotLinkMasterItemVo;

    /*
     * 마스터 품목 수정 화면에서 사용
     */

    // 마스터 품목 정보
    private MasterItemVo rows;

    // 배송 불가지역 정보
    private UndeliverableAreaResponseDto undeliverableAreaResponseDto;

    // 배송 불가지역 정보
	private List<ItemApprovalResultVo> itemApprStatusList;


}
