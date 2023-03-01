package kr.co.pulmuone.bos.goods.stock;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListRequestDto;
import kr.co.pulmuone.v1.goods.stock.dto.StockUploadListResponseDto;
import kr.co.pulmuone.v1.goods.stock.dto.vo.StockUploadListResultVo;
import kr.co.pulmuone.v1.goods.stock.service.GoodsStockExcelUploadListBiz;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 재고 엑셀 업로드 내역
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.11.20	    이성준            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
@RequiredArgsConstructor
public class GoodsStockExcelUploadListController {

	private final GoodsStockExcelUploadListBiz goodsStockExcelUploadListBiz;

	@Autowired(required=true)
	private HttpServletRequest request;

	@Autowired
    private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

	@ApiOperation(value = "ERP 재고 엑셀 업로드 내역 조회")
	@PostMapping(value = "/admin/goods/stock/getStockUploadList")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = StockUploadListResponseDto.class)
	})
	public ApiResult<?> getStockUploadList(StockUploadListRequestDto stockUploadListRequestDto) throws Exception {
		return goodsStockExcelUploadListBiz.getStockUploadList(BindUtil.bindDto(request, StockUploadListRequestDto.class));
	}

	@ApiOperation(value = "엑셀 선택 다운로드")
	@PostMapping(value = "/admin/goods/stock/exportExcelUploadList")
    public View getStockUploadListExcelDownload(@RequestBody StockUploadListRequestDto stockUploadListRequestDto, Model model) throws Exception {

        /*
         * 엑셀 파일 / 워크시트 정보
         *
         */

        String excelFileName = "item_stockList"; // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { 250, 250 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = {"center", "center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = {"ilItemCd", "msg" };

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "품목코드", "사유"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet); // 첫 번째 헤더 컬럼

        /*
         * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
         *
         */
        List<StockUploadListResultVo> itemStockList = goodsStockExcelUploadListBiz.getStockUploadListExcelDownload(stockUploadListRequestDto);

        firstWorkSheetDto.setExcelDataList(itemStockList);

        /*
         * excelDownloadDto 생성 후 workSheetDto 추가
         *
         */
        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder() //
                .excelFileName(excelFileName) //
                .build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        /*
         * model 객체에 excelDownloadDto 추가
         *
         */
        model.addAttribute(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

        /*
         * AbstractXlsxStreamingView 상속한 excelDownloadView return
         *
         */
       return excelDownloadView;
    }

}
