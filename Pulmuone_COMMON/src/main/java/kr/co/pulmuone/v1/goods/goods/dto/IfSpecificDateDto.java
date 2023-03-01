package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "일일배송 도착일자/IF일자 변경 Dto")
public class IfSpecificDateDto {
	private LocalDate goodsDailyTpIfDate;
	private List<ArrivalScheduledDateDto> scheduledDateList;
}
