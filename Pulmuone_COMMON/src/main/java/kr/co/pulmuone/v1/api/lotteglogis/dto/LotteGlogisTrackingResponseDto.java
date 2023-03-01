package kr.co.pulmuone.v1.api.lotteglogis.dto;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.api.cjlogistics.dto.vo.CJLogisticsTrackingVo;
import kr.co.pulmuone.v1.api.lotteglogis.dto.vo.LotteGlogisTrackingVo;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = { "responseCode", "responseMessage", "tracking" })
@ApiModel(description = "롯데 송장번호 트래킹 API Response")
public class LotteGlogisTrackingResponseDto{

    @Getter
    @ApiModelProperty(value = "롯데 송장번호 트래킹 응답 코드")
    private String responseCode;

    @Getter
    @ApiModelProperty(value = "롯데 송장번호 트래킹 응답 메시지")
    private String responseMessage;

    @Getter
    @ApiModelProperty(value = "롯데 트캐링 목록")
    private List<LotteGlogisTrackingVo> tracking;

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse lotteGlogisTrackingApiResponse) {
        this.responseCode = lotteGlogisTrackingApiResponse.getCode();
        this.responseMessage = lotteGlogisTrackingApiResponse.getMessage();
    }

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    // 트래킹 데이터 셋팅
    public void setTrackingData(String trackingJsonData) {
        try
        {
            List<LotteGlogisTrackingVo> trackingTemp = JsonUtil.deserializeJsonArray(trackingJsonData, LotteGlogisTrackingVo.class);

            if( CollectionUtils.isNotEmpty(trackingTemp) ) {
                this.tracking = trackingTemp.stream()//.filter(t -> !ApiEnums.LotteGlogisTrackingStatus.CONSIGNEE_REGISTRATION.getCode().equalsIgnoreCase(t.getTrackingStatusCode()))
                                                     .sorted(Comparator.comparing(LotteGlogisTrackingVo::getTrackingStatusCode)
                                                                       .thenComparing(LotteGlogisTrackingVo::getScanDate)
                                                                       .thenComparing(LotteGlogisTrackingVo::getScanTime))
                                                     .collect(Collectors.toList());
            }

            this.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.SUCCESS);
        } catch (IOException e) {
            this.setResponseCodeAndMessage(ApiEnums.LotteGlogisTrackingApiResponse.JSON_PARSING_ERROR);
        }
    }
}
