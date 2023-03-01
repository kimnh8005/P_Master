package kr.co.pulmuone.mall.display.layout.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import kr.co.pulmuone.v1.goods.brand.dto.vo.DpBrandListResultVo;
import kr.co.pulmuone.v1.goods.brand.dto.vo.UrBrandListResultVo;
import kr.co.pulmuone.v1.user.company.dto.vo.CommmonHeadQuartersCompanyVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " CommonDataDto")
public class CommonDataDto {

    private List<UrBrandListResultVo> brand;

    private List<DpBrandListResultVo> dpBrand;

    private CommmonHeadQuartersCompanyVo company;

    private LogoDto logo;
}
