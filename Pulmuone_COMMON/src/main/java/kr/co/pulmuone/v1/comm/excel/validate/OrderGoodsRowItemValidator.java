package kr.co.pulmuone.v1.comm.excel.validate;

import kr.co.pulmuone.v1.batch.collectionmall.ezadmin.dto.vo.EZAdminOrderInfoOrderVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadValidateEnums;
import kr.co.pulmuone.v1.comm.util.BeanUtils;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderSellersDto;
import kr.co.pulmuone.v1.outmall.order.service.OutmallOrderBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderGoodsRowItemValidator {

    //@Autowired
    private OutmallOrderBiz outmallOrderBiz;

    private final OrderRowItemValidator orderRowItemValidator;

    public List<OrderExcelResponseDto> getBosCreatenGoodsRowItemValidator(List<OrderExcelResponseDto> excelList, Map<Long, GoodsSearchOutMallVo> goodsMaps) throws UnsupportedEncodingException {
        for (OrderExcelResponseDto dto : excelList) {
            MessageCommEnum responseCommEnum = orderRowItemValidator.bosCreateValidateOrder(dto, goodsMaps);

            dto.setSuccess(true);
            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
               /* dto.setFailMessage(responseCommEnum.getMessage());
                if (responseCommEnum.equals(ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE)) {
                    excelList.stream()
                            .filter(vo -> !dto.getIlGoodsId().equals(vo.getIlGoodsId()))
                            .forEach(vo -> {
                                dto.setSuccess(false);
                                dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE_EXIST.getMessage());
                            });
                    break;
                }
                if (responseCommEnum.equals(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR)) {
                    excelList.stream()
                            .filter(vo -> !dto.getIlGoodsId().equals(vo.getIlGoodsId()))
                            .forEach(vo -> {
                                dto.setSuccess(false);
                                dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR_EXIST.getMessage());
                            });
                    break;
                }
                if (responseCommEnum.equals(ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR)) {
                    excelList.stream()
                            .filter(vo -> !dto.getIlGoodsId().equals(vo.getIlGoodsId()))
                            .forEach(vo -> {
                                dto.setSuccess(false);
                                dto.setFailMessage(ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR_EXIST.getMessage());
                            });
                    break;
                }*/
            }
        }
        return excelList;
    }

    /**
     * 이지어드민 입력 데이터 가공
     * @param outmallOrder
     * @throws Exception
     */
    private void validOutmallOrderInfo(OutMallOrderDto outmallOrder) {

		// 주소 확인
//		String address = outmallOrder.getReceiverAddress1();
//		outmallOrder.setReceiverAddress1(ExcelUploadUtil.splitAdress(address, 1));
//		outmallOrder.setReceiverAddress2(ExcelUploadUtil.splitAdress(address, 2));

		// 주문자 확인
		outmallOrder.setBuyerName(StringUtil.nvl(outmallOrder.getBuyerName(), outmallOrder.getReceiverName()));
		// 수취인 연락처 확인
		outmallOrder.setReceiverTel(ExcelUploadUtil.defaultPhoneNumber(outmallOrder.getReceiverTel(), outmallOrder.getBuyerTel(), outmallOrder.getReceiverMobile(), outmallOrder.getBuyerMobile()));
		// 주문자 전화번호 확인
		outmallOrder.setBuyerTel(ExcelUploadUtil.defaultPhoneNumber(outmallOrder.getBuyerTel(), outmallOrder.getReceiverTel(), outmallOrder.getBuyerMobile(), outmallOrder.getReceiverMobile()));
		// 수취인 휴대폰 확인
		outmallOrder.setReceiverMobile(ExcelUploadUtil.defaultPhoneNumber(outmallOrder.getReceiverMobile(), outmallOrder.getBuyerMobile(), outmallOrder.getReceiverTel(), outmallOrder.getBuyerTel()));
		// 주문자 휴대폰 확인
		outmallOrder.setBuyerMobile(ExcelUploadUtil.defaultPhoneNumber(outmallOrder.getBuyerMobile(), outmallOrder.getBuyerTel(), outmallOrder.getReceiverTel()));

		// 주문번호 하이픈 replace
        outmallOrder.setReceiverTel(outmallOrder.getReceiverTel().replaceAll("-",""));
        outmallOrder.setReceiverMobile(outmallOrder.getReceiverMobile().replaceAll("-",""));
        outmallOrder.setBuyerTel(outmallOrder.getBuyerTel().replaceAll("-",""));
        outmallOrder.setBuyerMobile(outmallOrder.getBuyerMobile().replaceAll("-",""));
    }

    public List<OutMallOrderDto> getEasyAdminRowItemValidator(String outmallType, List<OutMallOrderDto> excelList, Map<Long, GoodsSearchOutMallVo> goodsMaps, Map<String, Object> collectionMallValidatorIdMaps, List<OutMallOrderSellersDto> sellersList) throws UnsupportedEncodingException {

        Map<Long, List<OutMallOrderSellersDto>> sellersInfo = sellersList.stream().collect(Collectors.groupingBy(OutMallOrderSellersDto::getOmSellersId,LinkedHashMap::new,Collectors.toList()));
        Map<String, Integer> overlapCollectionMallDetailId = new HashMap<>();
        String collectionMallDetailId   = "";
        int checkNum = 0;
        for (OutMallOrderDto dto : excelList) {
            MessageCommEnum responseCommEnum = orderRowItemValidator.easyAdminValidateOrder(dto, goodsMaps, collectionMallValidatorIdMaps, sellersList);


            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
            }
            // 판매처 코드가 존재하지 않을 경우
            if(StringUtil.isEmpty(dto.getOmSellersId()) || !sellersInfo.containsKey(Long.parseLong(dto.getOmSellersId()))) {
                dto.setOmSellersId("0");
                dto.setFailMessage(dto.getFailMessage() + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.omSellersId.getCodeName()) );
            	dto.setSuccess(false);
            }
            if (overlapCollectionMallDetailId.containsKey(dto.getCollectionMallDetailId())) {
                checkNum = overlapCollectionMallDetailId.get(dto.getCollectionMallDetailId());
                overlapCollectionMallDetailId.put(dto.getCollectionMallDetailId(), checkNum+1);
            } else {
                overlapCollectionMallDetailId.put(dto.getCollectionMallDetailId(), 1);
            }
            this.validOutmallOrderInfo(dto);
        }

        for (OutMallOrderDto dto : excelList) {
            collectionMallDetailId  = dto.getCollectionMallDetailId();
            checkNum = overlapCollectionMallDetailId.get(collectionMallDetailId);
            if (checkNum > 1){
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.EASYADMIN_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP.getMessage(), collectionMallDetailId));
            }
        }

/*

        Map<String, Integer> maps = new HashMap<>();
        for (OutMallOrderDto dto : excelList) {
            maps.put(dto.getCollectionMallDetailId(), maps.getOrDefault(dto.getCollectionMallDetailId(), 0) + 1);
        }

        outmallOrderBiz = BeanUtils.getBeanByClass(OutmallOrderBiz.class);

        // DB 데이터 검증 용

        List<String> detailIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallDetailId)
                .collect(Collectors.toList());
        Set<String> responseSet = new HashSet<>(outmallOrderBiz.getOutMallExcelDetailId(outmallType, detailIdList));

        // 검증 및 결과 값 설정

        for (OutMallOrderDto dto : excelList) {
            String detailId = dto.getCollectionMallDetailId();
            if (!StringUtil.isEmpty(detailId) && maps.get(detailId) > 1) {
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + Constants.ARRAY_SEPARATORS +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP.getMessage(), "관리번호", detailId));
            }
            if (responseSet.contains(detailId)) {
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + Constants.ARRAY_SEPARATORS +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_OVERLAP.getMessage(), "관리번호", detailId));
            }
        }
*/

        return excelList;
    }

    public List<OutMallOrderDto> getSabangnetRowItemValidator(String outmallType, List<OutMallOrderDto> excelList, Map<Long, GoodsSearchOutMallVo> goodsMaps, Map<String, Object> collectionMallValidatorIdMaps, List<OutMallOrderSellersDto> sellersList) {
        Map<Long, List<OutMallOrderSellersDto>> sellersInfo = sellersList.stream().collect(Collectors.groupingBy(OutMallOrderSellersDto::getOmSellersId,LinkedHashMap::new,Collectors.toList()));
        Map<String, Integer> overlapCollectionMallDetailId = new HashMap<>();
        String collectionMallDetailId   = "";
        int checkNum = 0;
        for (OutMallOrderDto dto : excelList) {
            MessageCommEnum responseCommEnum = orderRowItemValidator.sabangnetValidateOrder(dto, goodsMaps, collectionMallValidatorIdMaps, sellersList);
            if (!responseCommEnum.equals(BaseEnums.Default.SUCCESS)) {
                dto.setSuccess(false);
            }

            // 판매처 코드가 존재하지 않을 경우
            if(StringUtil.isEmpty(dto.getOmSellersId()) || !sellersInfo.containsKey(Long.parseLong(dto.getOmSellersId()))) {
                dto.setOmSellersId("0");
                dto.setFailMessage(dto.getFailMessage() + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.omSellersId.getCodeName()) );
                dto.setSuccess(false);
            }

            if (overlapCollectionMallDetailId.containsKey(dto.getCollectionMallDetailId())) {
                checkNum = overlapCollectionMallDetailId.get(dto.getCollectionMallDetailId());
                overlapCollectionMallDetailId.put(dto.getCollectionMallDetailId(), checkNum+1);
            } else {
                overlapCollectionMallDetailId.put(dto.getCollectionMallDetailId(), 1);
            }

            this.validOutmallOrderInfo(dto);
        }

        for (OutMallOrderDto dto : excelList) {
            collectionMallDetailId  = dto.getCollectionMallDetailId();
            checkNum = overlapCollectionMallDetailId.get(collectionMallDetailId);
            if (checkNum > 1){
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + MessageFormat.format(ExcelUploadValidateEnums.ValidateType.SABANGNET_COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP.getMessage(), collectionMallDetailId));
            }
        }


/*
        // 입력내용 검증 용
        Map<String, Integer> maps = new HashMap<>();
        for (OutMallOrderDto dto : excelList) {
            maps.put(dto.getCollectionMallDetailId(), maps.getOrDefault(dto.getCollectionMallDetailId(), 0) + 1);
        }

        outmallOrderBiz = BeanUtils.getBeanByClass(OutmallOrderBiz.class);
        // DB 데이터 검증 용
        List<String> detailIdList = excelList.stream()
                .map(OutMallOrderDto::getCollectionMallDetailId)
                .collect(Collectors.toList());
        Set<String> responseSet = new HashSet<>(outmallOrderBiz.getOutMallExcelDetailId(outmallType, detailIdList));

        // 검증 및 결과 값 설정

        for (OutMallOrderDto dto : excelList) {
            String detailId = dto.getCollectionMallDetailId();
            if (maps.get(detailId) > 1) {
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + Constants.ARRAY_SEPARATORS +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP.getMessage(), "수집몰상세번호", detailId));
            }
            if (responseSet.contains(detailId)) {
                dto.setSuccess(false);
                dto.setFailMessage(dto.getFailMessage() + Constants.ARRAY_SEPARATORS +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_OVERLAP.getMessage(), "수집몰상세번호", detailId));
            }
        }
*/
        return excelList;
    }

}
