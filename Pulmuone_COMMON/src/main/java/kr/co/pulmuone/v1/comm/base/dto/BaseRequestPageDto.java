package kr.co.pulmuone.v1.comm.base.dto;


import kr.co.pulmuone.v1.comm.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 최상위 RequestDto 객체 (페이지 관련 값 존재. 검색 리스트용 )
 *
 * @author 오영민
 *
 */
@Getter
@Setter
public class BaseRequestPageDto extends BaseRequestDto {

	private int page;

	private int sPage = 0;

	private int ePage = 15;

	private int pageSize;

	public int getsPage() {
		if(StringUtil.nvlInt(this.page) > 1) {
			this.sPage = ( StringUtil.nvlInt(this.page) - 1 ) * StringUtil.nvlInt(this.pageSize);
		}
		return sPage;
	}
	public int getePage() {
		this.ePage = StringUtil.nvlInt(this.pageSize);
		return ePage;
	}

	private int baseRowCount;
	private int groupStartPage;
	private int groupEndPage = 3;

	public int getGroupStartPage() {
		if(StringUtil.nvlInt(this.page) > 1) {
			this.groupStartPage = ( StringUtil.nvlInt(this.page) - 1 ) * StringUtil.nvlInt(this.pageSize) * this.baseRowCount;
		}
		return groupStartPage;
	}
	public int getGroupEndPage() {
		this.groupEndPage = StringUtil.nvlInt(this.pageSize) * this.baseRowCount;
		return groupEndPage;
	}
}
