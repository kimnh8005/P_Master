package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.comm.api.dto.basic.ErpIfGoodSearchResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MasterItemListVo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@ApiModel(description = "ERP 품목 검색 팝업의 데이터 조회용 response dto")
public class ErpItemPopupResponseDto {

    /*
     * ERP 품목 검색 팝업의 데이터 조회용 response dto
     */
    private List<ErpIfGoodSearchResponseDto> erpItemApiList; // ERP API 로 조회한 품목정보 목록

    /*
     * 이미 BOS 상에 등록된 품목 코드로 검색시 하단 필드 사용
     */
    @Builder.Default
    @JsonProperty("isRegisterdItemCode")
    private boolean isRegisterdItemCode = false; // 품목 코드로 조회시 BOS 상에 이미 등록된 코드인지 여부, 기본값 false ( 미등록 )

    private MasterItemListVo masterItem; // 이미 등록된 품목코드인 경우 해당 품목 정보

}
