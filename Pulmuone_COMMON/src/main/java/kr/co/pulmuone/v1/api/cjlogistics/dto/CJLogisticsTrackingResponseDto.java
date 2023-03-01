package kr.co.pulmuone.v1.api.cjlogistics.dto;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.api.cjlogistics.dto.vo.CJLogisticsMasterVo;
import kr.co.pulmuone.v1.api.cjlogistics.dto.vo.CJLogisticsTrackingVo;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString(exclude = { "responseCode", "responseMessage", "master" })
@ApiModel(description = "CJ 송장번호 트래킹 API Response")
public class CJLogisticsTrackingResponseDto{

    @Getter
    @ApiModelProperty(value = "CJ 송장번호 트래킹 응답 코드")
    private String responseCode;

    @Getter
    @ApiModelProperty(value = "CJ 송장번호 트래킹 응답 메시지")
    private String responseMessage;

    @Getter
    @ApiModelProperty(value = "CJ 송장 마스터 정보")
    private CJLogisticsMasterVo master;

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse cJLogisticsTrackingApiResponse) {
        this.responseCode = cJLogisticsTrackingApiResponse.getCode();
        this.responseMessage = cJLogisticsTrackingApiResponse.getMessage();
    }

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    // 트래킹 데이터 셋팅
    public void setTrackingData(String trackingJsonData, JsonNode jsonNode) {
        try
        {
        	master = new CJLogisticsMasterVo();
        	this.master.setInsuernm(jsonNode.get("insuernm").asText());		// 인수자명
            this.master.setQty(jsonNode.get("qty").asText());				// 수량
            this.master.setRcvernm(jsonNode.get("rcvernm").asText());		// 수신자명
            this.master.setSendernm(jsonNode.get("sendernm").asText());		// 송화인명

            log.info("master :::: \n <{}>", master);

            List<CJLogisticsTrackingVo> trackingTemp = JsonUtil.deserializeJsonArray(trackingJsonData, CJLogisticsTrackingVo.class);
            log.info("trackingTemp :::: \n <{}>", trackingTemp);
            log.info("CollectionUtils.isNotEmpty(trackingTemp) :::: \n <{}>", CollectionUtils.isNotEmpty(trackingTemp));
            if( CollectionUtils.isNotEmpty(trackingTemp) ) {
                this.master.setTracking(trackingTemp.stream().map(trackingInfo -> {
                                                          trackingInfo.setScanDate(trackingInfo.getScanDate().replace("-", ""));
                                                          trackingInfo.setScanTime(trackingInfo.getScanTime().replace(":", ""));
                                                          return trackingInfo;
                                                          })
                                                    .sorted(Comparator.comparing(CJLogisticsTrackingVo::getScanDate)
                                                    .thenComparing(CJLogisticsTrackingVo::getScanTime))
                                                    .collect(Collectors.toList()));
            }

            this.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.SUCCESS);
        } catch (IOException e) {
            this.setResponseCodeAndMessage(ApiEnums.CJLogisticsTrackingApiResponse.JSON_PARSING_ERROR);
        }
    }

}
