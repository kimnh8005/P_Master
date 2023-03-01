package kr.co.pulmuone.v1.api.lotteglogis.dto;

import java.io.IOException;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.api.lotteglogis.dto.vo.LotteGlogisClientOrderVo;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import lombok.Getter;
import lombok.ToString;

@ToString(exclude = { "responseCode", "responseMessage", "returnList" })
@ApiModel(description = "롯데 거래처 주문 API Response")
public class LotteGlogisClientOrderResponseDto{

    @Getter
    @ApiModelProperty(value = "롯데 거래처 주문 결과 코드")
    private String responseCode;

    @Getter
    @ApiModelProperty(value = "롯데 거래처 주문 결과 메시지")
    private String responseMessage;

    @Getter
    @ApiModelProperty(value = "롯데 거래처 주문 결과 목록")
	private	List<LotteGlogisClientOrderVo> returnList;

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse lotteGlogisClientOrderApiResponse) {
        this.responseCode = lotteGlogisClientOrderApiResponse.getCode();
        this.responseMessage = lotteGlogisClientOrderApiResponse.getMessage();
    }

    // 응답코드, 메시지 셋팅
    public void setResponseCodeAndMessage(String responseCode, String responseMessage) {
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    // 거래처 주문 결과 데이터 셋팅
    public void setClientOrderReturnData(String clientOrderReturnJsonData) {
        try
        {
            this.returnList = JsonUtil.deserializeJsonArray(clientOrderReturnJsonData, LotteGlogisClientOrderVo.class);
            this.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.SUCCESS);
        } catch (IOException e) {
            this.setResponseCodeAndMessage(ApiEnums.LotteGlogisClientOrderApiResponse.JSON_PARSING_ERROR);
        }
    }
}
