package kr.co.pulmuone.v1.api.ezadmin.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "이지어드민 API 기타정보 조회 Response")
public class EZAdminResponseEtcInfoDto extends EZAdminResponseDefaultDto{

    private List<?> data;

    public EZAdminResponseEtcInfoDto (List<?> list) {
    	this.data = list;
    }

}
