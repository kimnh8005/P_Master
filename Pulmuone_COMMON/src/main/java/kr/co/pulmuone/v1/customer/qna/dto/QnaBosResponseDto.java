package kr.co.pulmuone.v1.customer.qna.dto;

import java.util.List;

import kr.co.pulmuone.v1.customer.qna.dto.vo.QnaBosListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QnaBosResponseDto {

	 private long total;

	 private List<QnaBosListVo> rows;
}
