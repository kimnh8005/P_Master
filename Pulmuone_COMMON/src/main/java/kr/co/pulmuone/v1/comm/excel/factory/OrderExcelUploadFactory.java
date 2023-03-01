package kr.co.pulmuone.v1.comm.excel.factory;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovUploadDto;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelCollectionMallDetailIdListData;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelGoodsIdListData;
import kr.co.pulmuone.v1.comm.excel.util.OrderExcelSetData;
import kr.co.pulmuone.v1.comm.excel.validate.DefaultRowValidator;
import kr.co.pulmuone.v1.comm.excel.validate.OrderGoodsRowItemValidator;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderSellersDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Component
@Service
@RequiredArgsConstructor
public class OrderExcelUploadFactory {

    @Autowired
    private  OrderExcelSetData orderExcelSetData;

    @Autowired
    private  OrderExcelGoodsIdListData orderExcelGoodsIdListData;

    @Autowired
    private  OrderExcelCollectionMallDetailIdListData orderExcelCollectionMallDetailIdListData;

    @Autowired
    private  OrderGoodsRowItemValidator orderGoodsRowItemValidator;

    @Autowired
    private  DefaultRowValidator defaultRowValidator;



    /**
     * 업로드 데이터 Mapping
     * @param excelUploadType
     * @param uploadSheet
     * @return
     */
    public List<?> setExcelData(String excelUploadType, Sheet uploadSheet){
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode())){
            return orderExcelSetData.setBosCreateExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode())) {
            return orderExcelSetData.setEasyAdminExcelData(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode(), uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode())) {
            return orderExcelSetData.setSabangnetExcelData(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode(), uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_PG.getCode())) {
            return orderExcelSetData.setCalculatePgExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_OUTMALL.getCode())) {
            return orderExcelSetData.setCalculateOutmallExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_POV.getCode())) {
            return orderExcelSetData.setCalculatePovExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_POV_VDC.getCode())) {
            return orderExcelSetData.setCalculatePovVdcExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.TRACKING_NUMBER.getCode())) {
            return orderExcelSetData.setTrackingNumberExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.INTERFACE_DAY_CHANGE.getCode())) {
            return orderExcelSetData.setInterfaceDayChangeExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CLAIM_EXCEL_UPLOAD.getCode())) {
            return orderExcelSetData.setClaimExcelUploadExcelData(uploadSheet);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SHIPPING_AREA_EXCEL_UPLOAD.getCode())) {
            return orderExcelSetData.setShippingAreaExcelUploadExcelData(uploadSheet);
        }

        return null;
    }

    public List<OrderExcelResponseDto> getBoarCreateIlGoodsIdList(String excelUploadType, List<?> excelList) throws Exception {
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode())){
            return orderExcelGoodsIdListData.getBosCreateIlGoodsIdList((List<OrderExeclDto>) excelList);
        }

        return null;
    }


    /**
     * 검증용 상품코드 조회
     * @param excelUploadType
     * @param excelList
     * @return
     */
    public Map<Long, GoodsSearchOutMallVo> getIlGoodsIdList(String excelUploadType, List<?> excelList) throws Exception {
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode())) {
            return orderExcelGoodsIdListData.getEasyAdminIlGoodsIdList((List<OutMallOrderDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode())) {
            return orderExcelGoodsIdListData.getSabangnetIlGoodsIdList((List<OutMallOrderDto>) excelList);
        }

        return null;
    }



    /**
     * 검증용 수집몰 상세번호 조회
     * @param excelUploadType
     * @param excelList
     * @return
     */
    public Map<String, Object> getCollectionMallValidatorIdList(String excelUploadType, List<?> excelList) throws Exception {
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode())) {
            return orderExcelCollectionMallDetailIdListData.getEasyAdminCollectionMallValidatorIdList(excelUploadType, (List<OutMallOrderDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode())) {
            return orderExcelCollectionMallDetailIdListData.getSabangnetCollectionMallValidatorIdList(excelUploadType, (List<OutMallOrderDto>) excelList);
        }

        return null;
    }

    /**
     * 상품 항목 검증
     * @param excelUploadType
     * @param excelList
     * @return
     * @throws Exception
     */
    public List<?> getGoodsRowItemValidator(String excelUploadType, List<?> excelList, Map<Long, GoodsSearchOutMallVo> goodsMaps, Map<String, Object> collectionMallValidatorIdMaps, List<OutMallOrderSellersDto> sellersList) throws Exception {
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.BOS_CREATE.getCode())){
            return orderGoodsRowItemValidator.getBosCreatenGoodsRowItemValidator((List<OrderExcelResponseDto>) excelList, goodsMaps);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.EASYADMIN.getCode())) {
            return orderGoodsRowItemValidator.getEasyAdminRowItemValidator(excelUploadType, (List<OutMallOrderDto>) excelList, goodsMaps, collectionMallValidatorIdMaps, sellersList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SABANGNET.getCode())) {
            return orderGoodsRowItemValidator.getSabangnetRowItemValidator(excelUploadType, (List<OutMallOrderDto>) excelList, goodsMaps, collectionMallValidatorIdMaps, sellersList);
        }

        return null;
    }

    /**
     * 일반 업로드 항목 검증
     * @param excelUploadType
     * @param excelList
     * @return
     * @throws Exception
     */
    public List<?> getDefaultRowItemValidator(String excelUploadType, List<?> excelList) throws Exception {
        if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_PG.getCode())) {
            return defaultRowValidator.getCalculatePgRowItemValidator((List<CalPgUploadDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_OUTMALL.getCode())) {
            return defaultRowValidator.getCalculateOutmallRowItemValidator((List<CalOutmallUploadDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_POV.getCode())
        		|| excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CAL_POV_VDC.getCode())) {
            return defaultRowValidator.getCalculatePovRowItemValidator((List<CalPovUploadDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.INTERFACE_DAY_CHANGE.getCode())) {
            return defaultRowValidator.getIfDayChangeRowItemValidator((List<IfDayChangeDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.CLAIM_EXCEL_UPLOAD.getCode())) {
            return defaultRowValidator.getClaimExcelUploadRowItemValidator((List<ClaimInfoExcelUploadDto>) excelList);
        } else if (excelUploadType.equals(ExcelUploadEnums.ExcelUploadType.SHIPPING_AREA_EXCEL_UPLOAD.getCode())) {
            return defaultRowValidator.getShippingAreaExcelUploadRowItemValidator((List<ShippingAreaExcelUploadDto>) excelList);
        }
        return null;
    }
}
