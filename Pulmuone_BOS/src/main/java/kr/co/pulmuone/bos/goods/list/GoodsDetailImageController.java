package kr.co.pulmuone.bos.goods.list;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDetailImageListResponseDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsDetailImageBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class GoodsDetailImageController {

    @Autowired
    private GoodsDetailImageBiz goodsDetailImageBiz;

    @Autowired
    private ExcelDownloadView excelDownloadView;

    @PostMapping(value = "/admin/goods/list/getGoodsDetailImageList")
    @ApiOperation(value = "상품 상세 이미지 다운로드 목록 조회", httpMethod = "POST")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data List<>", response = GoodsDetailImageListResponseDto.class)
    })
    public ApiResult<?> getGoodsDetailImageList(HttpServletRequest request, GoodsDetailImageListRequestDto goodsDetailImageListRequestDto) throws Exception{
        return goodsDetailImageBiz.getGoodsDetailImageList(BindUtil.bindDto(request, GoodsDetailImageListRequestDto.class));
    }


    /**
     * 상품 상세 이미지 다운로드 목록 엑셀 다운로드
     *
     * @param goodsDetailImageListRequestDto
     * @return ModelAndView
     */
    @ApiOperation(value = "상품 상세 이미지 다운로드 목록 엑셀 다운로드")
    @PostMapping(value = "/admin/goods/list/getGoodsDetailImageListExportExcel")
    public ModelAndView goodsChangeLogListExportExcel(@RequestBody GoodsDetailImageListRequestDto goodsDetailImageListRequestDto) {

        ExcelDownloadDto excelDownloadDto = goodsDetailImageBiz.getGoodsDetailImageListExportExcel(goodsDetailImageListRequestDto);

        ModelAndView modelAndView = new ModelAndView(excelDownloadView);
        modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        return modelAndView;

    }

}
