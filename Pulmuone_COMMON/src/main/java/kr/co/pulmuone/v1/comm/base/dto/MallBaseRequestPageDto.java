package kr.co.pulmuone.v1.comm.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MallBaseRequestPageDto {

	@ApiModelProperty(value = "page")
    private int page;

	@ApiModelProperty(value = "limit")
    private int limit;

	@ApiModelProperty(hidden = true)
    private int sPage = 0;

	@ApiModelProperty(hidden = true)
    private int ePage = 15;

    public int getsPage() {
        return page;
    }

    public int getePage() {
        return limit;
    }

    public int getSkipPage() {
        if (page > 1) {
            return (page - 1) * limit;
        }
        return 0;
    }

}
