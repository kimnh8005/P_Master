package kr.co.pulmuone.v1.customer.faq.dto;

import java.util.List;

import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FaqBosResponseDto {

	 private long total;

	 private List<FaqBosListVo> rows;

}
