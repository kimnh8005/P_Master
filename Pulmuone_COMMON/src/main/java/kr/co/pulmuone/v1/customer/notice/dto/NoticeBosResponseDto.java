package kr.co.pulmuone.v1.customer.notice.dto;

import java.util.List;

import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeBosResponseDto {

	 private long total;

	 private List<NoticeBosListVo> rows;

}
