package kr.co.pulmuone.v1.policy.excel.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.policy.excel.dto.PolicyExcelTmpltDto;
import kr.co.pulmuone.v1.policy.excel.dto.vo.PolicyExcelTmpltVo;

@Service
public class PolicyExcelTmpltBizImpl implements PolicyExcelTmpltBiz {

    @Autowired
    private PolicyExcelTmpltService policyExcelTmpltService;

    /**
     * 엑셀양식관리 설정 조회
     *
     * @param psExcelTemplateId
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPolicyExcelTmpltInfo(String psExcelTemplateId) {
    	return ApiResult.success(policyExcelTmpltService.getPolicyExcelTmpltInfo(psExcelTemplateId));
    }
    /**
     * 엑셀양식관리 양식목록 조회
     *
     * @param excelTemplateTp
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getPolicyExcelTmpltList(PolicyExcelTmpltVo vo) {
    	return ApiResult.success(policyExcelTmpltService.getPolicyExcelTmpltList(vo));
    }
    /**
     * 엑셀양식관리 설정 신규 등록
     *
     * @param PolicyExcelTmpltDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> addPolicyExcelTmplt(PolicyExcelTmpltDto dto) {
    	policyExcelTmpltService.addPolicyExcelTmplt(dto);

        return ApiResult.success();
    }
    /**
     * 엑셀양식관리 설정 수정
     *
     * @param PolicyExcelTmpltDto
     * @return ApiResult
     */
    @Override
    public ApiResult<?> putPolicyExcelTmplt(PolicyExcelTmpltDto dto) {
    	policyExcelTmpltService.putPolicyExcelTmplt(dto);
    	return ApiResult.success();
    }
    /**
     * 엑셀양식관리 설정 삭제
     *
     * @param psExcelTemplateId
     * @return ApiResult
     */
    @Override
    public ApiResult<?> delPolicyExcelTmplt(String psExcelTemplateId) {
    	policyExcelTmpltService.delPolicyExcelTmplt(psExcelTemplateId);
    	return ApiResult.success();
    }
    /**
     * 생성된 엑셀양식 별 다운로드 컬럼 설정
     *
     * @param psExcelTemplateId
     * @return ExcelWorkSheetDto
     */
    @Override
    public ExcelWorkSheetDto getCommonDownloadExcelTmplt(String psExcelTemplateId){
    	PolicyExcelTmpltVo vo = policyExcelTmpltService.getPolicyExcelTmpltInfo(psExcelTemplateId);
    	if(vo == null) vo = new PolicyExcelTmpltVo();

    	// file명 생성 - 텔플릿명_날짜
    	LocalDate now = LocalDate.now();//현재날짜
        String baseDt = now.toString().replaceAll("-", "");//현재날짜
    	String excelFileName = vo.getTemplateNm();
    	if (excelFileName != null && excelFileName != "") {
        	excelFileName = excelFileName + "_" + baseDt;
    	} else {
    		excelFileName = "";
    	}

    	String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름
        String replaceStr = "";

        if("EXCEL_TEMPLATE_TP.MASTER_ITEM".equals(vo.getExcelTemplateTp())){
        	replaceStr = "EXCEL_TEMPLATE_ITEM.";
        }else if("EXCEL_TEMPLATE_TP.ORDER".equals(vo.getExcelTemplateTp())){
        	replaceStr = "EXCEL_TEMPLATE_ORDER.";
        }else if("EXCEL_TEMPLATE_TP.GOODS".equals(vo.getExcelTemplateTp())){
        	replaceStr = "EXCEL_TEMPLATE_GOODS.";
        }
        
        //주문양식관리 숫자타입
        String[] numberList = {"odOrderDetlSeq", "orderPrice", "shippingPrice", "couponPrice", "directPrice", "paidPrice", "orderCnt", "cancelCnt", "cancelAbleCnt", "warehouseReqCnt",
                               "standardPrice", "recommendedPrice", "salePrice", "discountEmployeePrice", "finalGoodsPriceVat", "finalGoodsPriceNotVat"};

        ExcelWorkSheetDto dto = ExcelWorkSheetDto.builder()
                .excelFileName(excelFileName)
                .workSheetName(excelSheetName)
                .build();

        JsonArray array = JsonParser.parseString(StringUtil.nvl(vo.getTemplateData())).getAsJsonArray();

        String[] codeList = new String[array.size()];
        String[] codeNameList = new String[array.size()];
        String[] codeCellTypeList = new String[array.size()];
        int index = 0;

        if("EXCEL_TEMPLATE_TP.ORDER".equals(vo.getExcelTemplateTp())) { // 주문양식관리인 경우
            for(final JsonElement json: array){
        	codeList[index] = json.getAsJsonObject().get("CODE").toString().replaceAll(replaceStr, "").replaceAll("\"", "");
                if(Arrays.stream(numberList).anyMatch(codeList[index]::equals)) {
                    codeCellTypeList[index] = "int";
                } else {
                    codeCellTypeList[index] = "String";
                }
        	codeNameList[index++] = json.getAsJsonObject().get("NAME").toString().replaceAll("\"", "");
            }
            dto.setCellTypeList(codeCellTypeList);
        } else {
            for(final JsonElement json: array){
        	codeList[index] = json.getAsJsonObject().get("CODE").toString().replaceAll(replaceStr, "").replaceAll("\"", "");
        	codeNameList[index++] = json.getAsJsonObject().get("NAME").toString().replaceAll("\"", "");
            }
        }

        dto.setHeaderList(0, codeNameList); // 첫 번째 헤더 컬럼
        dto.setPropertyList(codeList);

    	return dto;
    }
}


