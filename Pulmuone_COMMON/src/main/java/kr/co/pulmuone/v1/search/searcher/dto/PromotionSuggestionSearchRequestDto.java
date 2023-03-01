package kr.co.pulmuone.v1.search.searcher.dto;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PromotionSuggestionSearchRequestDto {

    private String keyword;
    private Integer offset = 0;
    private Integer limit = 1;
    private GoodsEnums.DeviceType deviceType;
    private String typeCode = "EVENT";

    @Builder
    public PromotionSuggestionSearchRequestDto(String keyword, Integer offset, Integer limit, GoodsEnums.DeviceType deviceType, String typeCode) {
        this.keyword = keyword;
        this.offset = offset != null ? offset : this.offset;
        this.limit = limit != null ? limit : this.limit;
        this.deviceType = deviceType != null ? deviceType : DeviceUtil.getGoodsEnumDeviceTypeByUserDevice();
        this.typeCode = typeCode != null ? typeCode : this.typeCode;
    }

}
