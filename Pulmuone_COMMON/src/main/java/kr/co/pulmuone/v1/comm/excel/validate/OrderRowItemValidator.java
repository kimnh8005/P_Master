package kr.co.pulmuone.v1.comm.excel.validate;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.util.PhoneUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.goods.search.dto.vo.GoodsSearchOutMallVo;
import kr.co.pulmuone.v1.order.create.dto.OrderExcelResponseDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderSellersDto;
import kr.co.pulmuone.v1.policy.fee.dto.OmBasicFeeListDto;
import kr.co.pulmuone.v1.store.delivery.dto.WarehouseUnDeliveryableInfoDto;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderRowItemValidator {

    @Autowired
    private StoreWarehouseBiz storeWarehouseBiz;

    @Autowired
    private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

    /**
     * BOS Create 내역 Validation
     *
     * @param dto OutMallOrderDto
     * @return MessageCommEnum
     */
    public MessageCommEnum bosCreateValidateOrder(OrderExcelResponseDto dto, Map<Long, GoodsSearchOutMallVo> goodsMaps) {

        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getRecvNm())) {
            dto.setErrRecvNm(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvNm.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvNm.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_NAME_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getRecvHp())) {
            dto.setErrRecvHp(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_MOBILE_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getRecvZipCd())) {
            dto.setErrRecvZipCd(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_ZIP_CODE_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getRecvAddr1())) {
            dto.setErrRecvAddr1(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr1.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr1.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_ADDRESS1_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getRecvAddr2())) {
            dto.setErrRecvAddr2(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr2.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr2.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_ADDRESS2_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getIlGoodsId())) {
            dto.setErrIlGoodsId(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.ilGoodsId.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.ilGoodsId.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.IL_GOODS_ID_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getOrderCnt())) {
            dto.setErrOrderCnt(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
            //return ExcelUploadValidateEnums.ValidateType.ORDER_COUNT_EMPTY;
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 바이트 길이 확인
        if (StringUtil.getByteLength(dto.getRecvNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
            dto.setRecvNm(StringUtil.cutString(dto.getRecvNm(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
            dto.setErrRecvNm(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvNm.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvNm.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_NAME_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getRecvHp()) > ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()) {
            dto.setRecvHp(StringUtil.cutString(dto.getRecvHp(), ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength(), ""));
            dto.setErrRecvHp(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_MOBILE_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getRecvZipCd()) > ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()) {
            dto.setRecvZipCd(StringUtil.cutString(dto.getRecvZipCd(), ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength(), ""));
            dto.setErrRecvZipCd(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_5.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_ZIP_CODE_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getRecvAddr1()) > ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()) {
            dto.setRecvAddr1(StringUtil.cutString(dto.getRecvAddr1(), ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength(), ""));
            dto.setErrRecvAddr1(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr1.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr1.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.RECEIVER_ADDRESS1_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getRecvAddr2()) > ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()) {
            dto.setRecvAddr2(StringUtil.cutString(dto.getRecvAddr2(), ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength(), ""));
            dto.setErrRecvAddr2(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr2.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()));
            dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.BosCreateCols.recvAddr2.getCodeName(),  ExcelUploadValidateEnums.ByteLength.BYTE_60.getByteLength()));
            //return ExcelUploadValidateEnums.ValidateType.BUYER_MOBILE_LENGTH_OVER;
            returnStatus = BaseEnums.Default.FAIL;
        }


        //문자 포함여부 확인
        if (!StringUtil.isEmpty(dto.getRecvHp())) {
            if (!StringUtil.isNumeric(dto.getRecvHp())) {
                dto.setErrIlGoodsId(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName()));
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.recvHp.getCodeName()));
                //return ExcelUploadValidateEnums.ValidateType.GOODS_ID_STRING;
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        if (!StringUtil.isEmpty(dto.getRecvZipCd())) {
            if (!StringUtil.isNumeric(dto.getRecvZipCd())) {
                dto.setErrIlGoodsId(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName()));
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.recvZipCd.getCodeName()));
                //return ExcelUploadValidateEnums.ValidateType.GOODS_ID_STRING;
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        if (!StringUtil.isEmpty(dto.getIlGoodsId())) {
            if (!StringUtil.isNumeric(dto.getIlGoodsId())) {
                dto.setErrIlGoodsId(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.ilGoodsId.getCodeName()));
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.ilGoodsId.getCodeName()));
                //return ExcelUploadValidateEnums.ValidateType.GOODS_ID_STRING;
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        if (!StringUtil.isEmpty(dto.getOrderCnt())) {
            if (!StringUtil.isNumeric(dto.getOrderCnt())) {
                dto.setErrOrderCnt(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
                //return ExcelUploadValidateEnums.ValidateType.ORDER_COUNT_STRING;
                returnStatus = BaseEnums.Default.FAIL;
            } else {
                if (Integer.parseInt(dto.getOrderCnt()) <= 0) {
                    dto.setErrOrderCnt(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
                    dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.BosCreateCols.orderCnt.getCodeName()));
                    //return ExcelUploadValidateEnums.ValidateType.ORDER_COUNT_ERROR;
                    returnStatus = BaseEnums.Default.FAIL;
                }
            }
        }
        if (!StringUtil.isEmpty(dto.getSalePrice())) {
            if (!StringUtil.isNumeric(Integer.toString(dto.getSalePrice()))) {
                dto.setErrSalePrice(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.salePrice.getCodeName()));
                dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.BosCreateCols.salePrice.getCodeName()));
                //return ExcelUploadValidateEnums.ValidateType.PAID_PRICE_STRING;
                returnStatus = BaseEnums.Default.FAIL;
            } else {
                // 증정품, 식품마케팅 증정품은 가격 0원이여도 가능
                if (Integer.parseInt(Integer.toString(dto.getSalePrice())) <= 0
                        && !GoodsEnums.GoodsType.GIFT.getCode().equals(dto.getGoodsTp()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(dto.getGoodsTp())) {
                    dto.setErrSalePrice(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.BosCreateCols.salePrice.getCodeName()));
                    dto.setFailMessage(MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.BosCreateCols.salePrice.getCodeName()));
                    //return ExcelUploadValidateEnums.ValidateType.ORDER_COUNT_ERROR;
                    returnStatus = BaseEnums.Default.FAIL;
                }
            }
        }


        //상품수량, 판매가 확인


        //상품상태 확인 및 품목코드 설정
        if (dto.getSaleStatus() == null) {
            dto.setErrSaleStatus("{"+dto.getIlGoodsId()+"} "+ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR.getMessage());
            dto.setFailMessage("{"+dto.getIlGoodsId()+"} "+ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR.getMessage());
            //return ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR;
            returnStatus = BaseEnums.Default.FAIL;
        } else {
        	if (GoodsEnums.SaleStatus.STOP_SALE.getCode().equals(dto.getSaleStatus())) {
                dto.setErrSaleStatus("{"+dto.getIlGoodsId()+"} "+ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE.getMessage());
                dto.setFailMessage("{"+dto.getIlGoodsId()+"} "+ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE.getMessage());
                //return ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR;
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        return returnStatus;
    }

    public MessageCommEnum easyAdminValidateOrder(OutMallOrderDto dto, Map<Long, GoodsSearchOutMallVo> goodsMaps, Map<String, Object> collectionMallValidatorIdMaps, List<OutMallOrderSellersDto> sellersList) {
        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        String chkValue = "";
        dto.setFailMessage("");

        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getCollectionMallId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.collectionMallId.getCodeName() ));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getCollectionMallDetailId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.collectionMallDetailId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }


        if (StringUtil.isEmpty(dto.getGoodsName())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.goodsName.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        // 상품코드만 체크..  품목코드는 사용 안함.
//        if (StringUtil.isEmpty(dto.getIlGoodsId()) && StringUtil.isEmpty(dto.getIlItemCd())){
//            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.COLUMN_GOODS_ID_EMPTY.getMessage());
//            returnStatus = BaseEnums.Default.FAIL;
//        }
        if (StringUtil.isEmpty(dto.getBuyerName())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerName.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isEmpty(dto.getReceiverMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverMobile.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getIlGoodsId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.ilGoodsId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;

            if (!StringUtil.isDigits(dto.getIlGoodsId())) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.ilGoodsId.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        if (StringUtil.isEmpty(dto.getOrderCount())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getPaidPrice())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getOutMallId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.outMallId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getReceiverZipCode())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverZipCode.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getReceiverAddress1())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverAddress1.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 바이트 길이 확인
        if (StringUtil.getByteLength(dto.getCollectionMallId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setCollectionMallId(StringUtil.cutString(dto.getCollectionMallId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.collectionMallId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getCollectionMallDetailId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setCollectionMallDetailId(StringUtil.cutString(dto.getCollectionMallDetailId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.collectionMallDetailId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getIlGoodsId()) > ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()) {
            dto.setIlGoodsId(StringUtil.cutString(dto.getIlGoodsId(), ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.ilGoodsId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getGoodsName()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setGoodsName(StringUtil.cutString(dto.getGoodsName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.goodsName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
            //return ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_LENGTH_OVER;
        }
        if (StringUtil.getByteLength(dto.getBuyerName()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
            dto.setBuyerName(StringUtil.cutString(dto.getBuyerName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
//        if (StringUtil.getByteLength(dto.getBuyerTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
//            dto.setBuyerTel(StringUtil.cutString(dto.getBuyerTel(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
//            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
//            returnStatus = BaseEnums.Default.FAIL;
//        }
//
//        if (StringUtil.getByteLength(dto.getBuyerMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
//            dto.setBuyerMobile(StringUtil.cutString(dto.getBuyerMobile(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
//            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerMobile.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
//            returnStatus = BaseEnums.Default.FAIL;
//        }
        if (StringUtil.getByteLength(dto.getReceiverName()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
            dto.setReceiverName(StringUtil.cutString(dto.getReceiverName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isEmpty(dto.getReceiverTel())) {
            if (StringUtil.getByteLength(dto.getReceiverTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setReceiverTel(StringUtil.cutString(dto.getReceiverTel(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getBuyerTel());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverTel.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }
        if (!StringUtil.isEmpty(dto.getReceiverMobile())) {
            if (StringUtil.getByteLength(dto.getReceiverMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setReceiverMobile(StringUtil.cutString(dto.getReceiverMobile(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverMobile.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getReceiverMobile());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverMobile.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }

        if (!StringUtil.isEmpty(dto.getBuyerMobile())) {
            if (StringUtil.getByteLength(dto.getBuyerMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setReceiverMobile(StringUtil.cutString(dto.getBuyerMobile(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerMobile.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getBuyerMobile());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerMobile.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }

        if (!StringUtil.isEmpty(dto.getBuyerTel())) {
            if (StringUtil.getByteLength(dto.getBuyerTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setReceiverMobile(StringUtil.cutString(dto.getBuyerTel(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getBuyerTel());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.buyerTel.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }
        if (StringUtil.getByteLength(dto.getReceiverZipCode()) > ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()) {
            dto.setReceiverZipCode(StringUtil.cutString(dto.getReceiverZipCode(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverZipCode.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverAddress1()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setReceiverAddress1(StringUtil.cutString(dto.getReceiverAddress1(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverAddress1.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverAddress2()) > ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength()) {
            dto.setReceiverAddress2(StringUtil.cutString(dto.getReceiverAddress2(), ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverAddress2.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getShippingPrice()) > ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()) {
            dto.setShippingPrice(StringUtil.cutString(dto.getShippingPrice(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.shippingPrice.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }


        if (StringUtil.getByteLength(dto.getOutMallId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setOutMallId(StringUtil.cutString(dto.getOutMallId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.outMallId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getMemo()) > ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()) {
            dto.setMemo(StringUtil.cutString(dto.getMemo(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.memo.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallOrderDt()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setOutmallOrderDt(StringUtil.cutString(dto.getOutmallOrderDt(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.outmallOrderDt.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallGoodsNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setOutmallGoodsNm(StringUtil.cutString(dto.getOutmallGoodsNm(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.outmallGoodsNm.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallOptNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()) {
            dto.setOutmallOptNm(StringUtil.cutString(dto.getOutmallOptNm(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.outmallOptNm.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (!StringUtil.isEmpty(dto.getLogisticsCd())) {
            if (StringUtil.getByteLength(dto.getLogisticsCd()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
                dto.setLogisticsCd(StringUtil.cutString(dto.getLogisticsCd(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.deliLogisCd.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        if (!StringUtil.isEmpty(dto.getTrackingNo())) {
            if (StringUtil.getByteLength(dto.getTrackingNo()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
                dto.setTrackingNo(StringUtil.cutString(dto.getTrackingNo(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.trackingNumber.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        if (!StringUtil.isEmpty(dto.getDeliveryMessage())) {
            if (StringUtil.getByteLength(dto.getDeliveryMessage()) > ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength()) {
                dto.setDeliveryMessage(StringUtil.cutString(dto.getTrackingNo(), ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.deliveryMessage.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        /*
        if (StringUtil.getByteLength(dto.getOutMallId()) > 40) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.EasyAdminCols.outMallId.getCodeName(), 40));
            returnStatus = BaseEnums.Default.FAIL;
            //return ExcelUploadValidateEnums.ValidateType.OUT_MALL_ID_LENGTH_OVER;
        }
         */


        //문자 포함여부 확인
        if (!StringUtil.isDigits(dto.getIlGoodsId())) {
            dto.setIlGoodsId("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.ilGoodsId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isDigits(dto.getOrderCount())) {
            dto.setOrderCount("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (dto.getOrderCount().contains(".")) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_DECIMAL_POINT.getMessage(), ExcelUploadEnums.EasyAdminCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (!StringUtil.isDigits(dto.getPaidPrice())) {
            dto.setPaidPrice("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.EasyAdminCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (dto.getPaidPrice().contains(".")) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_DECIMAL_POINT.getMessage(), ExcelUploadEnums.EasyAdminCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }


        //상품수량, 판매가 확인
        // TODO 추 후 상품 타입 체크하여 증정품일 경우 0원 처리 수정 필요 - KMJ
        // TODO 수량은 무조건 이었어야해서 다시 풀었음. - 최용호
        if (!StringUtil.isEmpty(dto.getOrderCount())) {
            if (Integer.parseInt(dto.getOrderCount()) <= 0) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.EasyAdminCols.orderCount.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        /*
        if (Integer.parseInt(dto.getPaidPrice()) <= 0) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.EasyAdminCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        */

        // 수집몰 주문 번호 체크
        /*
        if (overlapCollectionMallId.containsKey(Long.parseLong(dto.getCollectionMallId()))) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_DETAIL_ID_UPLOAD_OVERLAP.getMessage(), dto.getCollectionMallId()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        /*
        */

        Map<String, String> collectionMallIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("collectionMallIdList");

        if (collectionMallIdMaps.containsKey(dto.getCollectionMallId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.EASYADMIN_COLLECTION_MALL_ID_OVERLAP.getMessage(), dto.getCollectionMallId()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        Map<String, String> collectionMallDetailIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("collectionMallDetailIdList");

        if (collectionMallDetailIdMaps.containsKey(dto.getCollectionMallDetailId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.EASYADMIN_COLLECTION_MALL_DETAIL_ID_OVERLAP.getMessage(), dto.getCollectionMallDetailId()));
            returnStatus = BaseEnums.Default.FAIL;
        }
/*

        Map<String, String> outmallIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("outmallIdList");

        if (outmallIdMaps.containsKey(dto.getOutMallId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_OUTMALL_ID_OVERLAP.getMessage(), dto.getOutMallId()));
            returnStatus = BaseEnums.Default.FAIL;
        }
*/




        //상품상태 확인 및 품목코드 설정
        GoodsSearchOutMallVo goodsSearchOutMallVo = goodsMaps.get(dto.getIlGoodsId());
        Long omSellersId = Long.parseLong(dto.getOmSellersId());    // 판매처
        
        if (goodsSearchOutMallVo == null) {

            boolean checkSearchFlag = false;
            for(long key : goodsMaps.keySet()) {
                goodsSearchOutMallVo = goodsMaps.get(key);

                // 풀샵 상품코드 세팅
                dto.setGoodsNo(Long.toString(goodsSearchOutMallVo.getGoodsNo()));

                if(goodsSearchOutMallVo.getGoodsNo() == 0L) {


                    if (dto.getIlGoodsId().equals(Long.toString(goodsSearchOutMallVo.getGoodsId()))) {

                        dto.setIlGoodsId(Long.toString(goodsSearchOutMallVo.getGoodsId()));
                        checkSearchFlag = true;
                        // 허용 상품 상태
                        /* 외부몰 판매상태는 체크 하지 않음
                        if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                            checkSearchFlag = false;
                        }
                        */


                        if (!GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                        ) {
                            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName()));
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 출고처 배송불가지역 체크
                        try{
                            // 배송불가지역 체크
                            boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                            if(isUndeliveableArea){
                                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                                returnStatus = BaseEnums.Default.FAIL;
                            }

                        }catch(Exception e){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 판매처에 등록된 공급업체의 상품인지 확인
                        long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
                        for(OutMallOrderSellersDto sellerDto : sellersList){
                            if(omSellersId.equals(sellerDto.getOmSellersId())){
                                boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                                if(!isExistSupplierGoods){
                                    dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                                    returnStatus = BaseEnums.Default.FAIL;
                                }
                            }
                        }

                        // 품목코드 체크
                        dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
                        break;
                    }


                }
                else {


                    // 매핑했을경우
                    if (dto.getIlGoodsId().equals(Long.toString(goodsSearchOutMallVo.getGoodsNo()))){
                        dto.setIlGoodsId(Long.toString(goodsSearchOutMallVo.getGoodsId()));
                        checkSearchFlag = true;

                        /*if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                            checkSearchFlag = false;
                        }*/

                        // 허용 상품 상태
                        if (       !GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                        ) {
                            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName()));
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 출고처 배송불가지역 체크
                        try{
                            // 배송불가지역 체크
                            boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                            if(isUndeliveableArea){
                                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                                returnStatus = BaseEnums.Default.FAIL;
                            }

                        }catch(Exception e){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 판매처에 등록된 공급업체의 상품인지 확인
                        long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
                        for(OutMallOrderSellersDto sellerDto : sellersList){
                            if(omSellersId.equals(sellerDto.getOmSellersId())){
                                boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                                if(!isExistSupplierGoods){
                                    dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                                    returnStatus = BaseEnums.Default.FAIL;
                                }
                            }
                        }

                        // 품목코드 체크
                        dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
                        break;
                    }



                }
            }
            if (checkSearchFlag == false) {
                dto.setGoodsNo(dto.getIlGoodsId());
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }
        } else {

            // 풀샵 상품코드 세팅
            dto.setGoodsNo(Long.toString(goodsSearchOutMallVo.getGoodsNo()));

            /*if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }*/
            // 허용 상품 상태
            if (       !GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                    && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                    && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
            ) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 출고처 배송불가지역 체크
            try{
                // 배송불가지역 체크
                boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                if(isUndeliveableArea){
                    dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                    returnStatus = BaseEnums.Default.FAIL;
                }

            }catch(Exception e){
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 판매처에 등록된 공급업체의 상품인지 확인
            long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
            for(OutMallOrderSellersDto sellerDto : sellersList){
                if(omSellersId.equals(sellerDto.getOmSellersId())){
                    boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                    if(!isExistSupplierGoods){
                        dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                        returnStatus = BaseEnums.Default.FAIL;
                    }
                }
            }

            /*
            if (goodsSearchOutMallVo.getSaleStatus().equals(GoodsEnums.SaleStatus.STOP_SALE.getCode())) {
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }
            if (!goodsSearchOutMallVo.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }
             */

            // 품목코드 체크
            dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
        }



        //주문자 정보 확인
        /*
        if (!PhoneUtil.isLandlineValidate(dto.getBuyerTel()) || !PhoneUtil.isCellValidate(dto.getBuyerMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.BUYER_INFO_ERROR.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
        }
        //수취인 정보 확인
        if (!PhoneUtil.isLandlineValidate(dto.getReceiverTel()) || !PhoneUtil.isCellValidate(dto.getReceiverMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.RECEIVER_INFO_ERROR.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
        }
        */
        //returnStatus = BaseEnums.Default.SUCCESS;



        return returnStatus;
    }

    /**
     * 배송불가권역 여부
     *  1.출고처
     *  2.품목
     *  3.배송정책
     * @param urWarehouseId
     * @param ilGoodsId
     * @param zipCode
     * @param goodsUndeliverableAreaTp
     * @return
     * @throws Exception
     */
    public boolean isUndeliverableArea(Long urWarehouseId, Long ilGoodsId, String zipCode, String goodsUndeliverableAreaTp) throws Exception{
        // 출고처 배송불가지역 체크
        WarehouseUnDeliveryableInfoDto warehouseUnDeliveryableInfoDto = storeWarehouseBiz.getUnDeliverableInfo(urWarehouseId, zipCode, false);
        boolean isShippingPossibility = warehouseUnDeliveryableInfoDto.isShippingPossibility();

        if(!isShippingPossibility){
            return true;
        }

        // 도서산관 및 제주 배송 정보 조회
        ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz.getShippingArea(zipCode);
        if (shippingAreaVo == null) {
            return false;
        }

        // 품목 도서산간 제주 배송불가 여부
        isShippingPossibility = !goodsShippingTemplateBiz.isUnDeliverableArea(goodsUndeliverableAreaTp, shippingAreaVo);

        if(!isShippingPossibility){
            return true;
        }

        // 주소기반 배송 정책으로 인한 도서산관,제주 지역 배송 가능여부 체크
        if (shippingAreaVo != null) {
            ShippingDataResultVo shippingDataResultVo = goodsShippingTemplateBiz.getShippingUndeliveryInfo(ilGoodsId, urWarehouseId);
            isShippingPossibility = !goodsShippingTemplateBiz.isUnDeliverableArea(shippingDataResultVo.getUndeliverableAreaType(), shippingAreaVo);
        }

        return isShippingPossibility ? false : true;
    }

    public MessageCommEnum sabangnetValidateOrder(OutMallOrderDto dto, Map<Long, GoodsSearchOutMallVo> goodsMaps, Map<String, Object> collectionMallValidatorIdMaps,List<OutMallOrderSellersDto> sellersList) {
        String chkValue = "";
        BaseEnums.Default returnStatus = BaseEnums.Default.SUCCESS;
        dto.setFailMessage("");
        // 필수 값 확인
        if (StringUtil.isEmpty(dto.getCollectionMallDetailId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.collectionMallDetailId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getBuyerName())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.buyerName.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isEmpty(dto.getGoodsName())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.goodsName.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.isEmpty(dto.getReceiverMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.EasyAdminCols.receiverMobile.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (!StringUtil.isEmpty(dto.getBuyerTel())) {
            if (StringUtil.getByteLength(dto.getBuyerTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.buyerTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
            chkValue = StringUtil.getPhoneStringReplace(dto.getBuyerTel());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.receiverTel.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        if (!StringUtil.isEmpty(dto.getBuyerMobile())) {
            if (StringUtil.getByteLength(dto.getBuyerMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.buyerMobile.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getReceiverMobile());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.receiverMobile.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }


        if (StringUtil.isEmpty(dto.getReceiverName())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.receiverName.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isEmpty(dto.getReceiverTel())) {
            if (StringUtil.getByteLength(dto.getReceiverTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getBuyerTel());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.receiverTel.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }
        if (!StringUtil.isEmpty(dto.getReceiverMobile())) {
            if (StringUtil.getByteLength(dto.getReceiverMobile()) > 40) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverMobile.getCodeName(), 40));
                returnStatus = BaseEnums.Default.FAIL;
            }

            chkValue = StringUtil.getPhoneStringReplace(dto.getReceiverMobile());
            if (!StringUtil.isDigits(chkValue)) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.receiverMobile.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }

        }
        if (StringUtil.isEmpty(dto.getReceiverZipCode())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.receiverZipCode.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getReceiverAddress1())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.receiverAddress1.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        /*
        if (StringUtil.isEmpty(dto.getReceiverAddress2())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.receiverAddress2.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
         */
        if (StringUtil.isEmpty(dto.getIlGoodsId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.ilGoodsId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;

            if (!StringUtil.isDigits(dto.getIlGoodsId())) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.ilGoodsId.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        if (StringUtil.isEmpty(dto.getOrderCount())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getPaidPrice())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.isEmpty(dto.getOutMallId())) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_EMPTY.getMessage(), ExcelUploadEnums.SabangnetCols.outMallId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        // 바이트 길이 확인
        if (StringUtil.getByteLength(dto.getCollectionMallId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setCollectionMallId(StringUtil.cutString(dto.getCollectionMallId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.collectionMallId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getCollectionMallDetailId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setCollectionMallDetailId(StringUtil.cutString(dto.getCollectionMallDetailId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.collectionMallDetailId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getIlGoodsId()) > ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()) {
            dto.setIlGoodsId(StringUtil.cutString(dto.getIlGoodsId(), ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.ilGoodsId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_12.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getGoodsName()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setGoodsName(StringUtil.cutString(dto.getGoodsName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.goodsName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getBuyerName()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
            dto.setBuyerName(StringUtil.cutString(dto.getBuyerName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.buyerName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getBuyerTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
            dto.setBuyerTel(StringUtil.cutString(dto.getBuyerTel(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.buyerTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getBuyerMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
            dto.setBuyerMobile(StringUtil.cutString(dto.getBuyerMobile(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.buyerMobile.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverName()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
            dto.setReceiverName(StringUtil.cutString(dto.getReceiverName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverName.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getReceiverTel()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
            dto.setReceiverTel(StringUtil.cutString(dto.getReceiverTel(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverTel.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverMobile()) > ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()) {
            dto.setReceiverMobile(StringUtil.cutString(dto.getReceiverMobile(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverZipCode.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_40.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverZipCode()) > ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()) {
            dto.setReceiverZipCode(StringUtil.cutString(dto.getReceiverZipCode(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverZipCode.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverAddress1()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setReceiverAddress1(StringUtil.cutString(dto.getReceiverAddress1(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverAddress1.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getReceiverAddress2()) > ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength()) {
            dto.setReceiverAddress2(StringUtil.cutString(dto.getReceiverAddress2(), ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.receiverAddress2.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_140.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getShippingPrice()) > ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength()) {
            dto.setShippingPrice(StringUtil.cutString(dto.getShippingPrice(), ExcelUploadValidateEnums.ByteLength.BYTE_10.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.shippingPrice.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (StringUtil.getByteLength(dto.getOutMallId()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setOutMallId(StringUtil.cutString(dto.getOutMallId(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.outMallId.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getMemo()) > ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()) {
            dto.setMemo(StringUtil.cutString(dto.getMemo(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.memo.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallOrderDt()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
            dto.setOutmallOrderDt(StringUtil.cutString(dto.getOutmallOrderDt(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.outmallOrderDt.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallGoodsNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()) {
            dto.setOutmallGoodsNm(StringUtil.cutString(dto.getOutmallGoodsNm(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.outmallGoodsNm.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_200.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (StringUtil.getByteLength(dto.getOutmallOptNm()) > ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()) {
            dto.setOutmallOptNm(StringUtil.cutString(dto.getOutmallOptNm(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength(), ""));
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.outmallOptNm.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_500.getByteLength()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isEmpty(dto.getLogisticsCd())) {
            if (StringUtil.getByteLength(dto.getLogisticsCd()) > ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()) {
                dto.setLogisticsCd(StringUtil.cutString(dto.getLogisticsCd(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.deliLogisCd.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_50.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        if (!StringUtil.isEmpty(dto.getTrackingNo())) {
            if (StringUtil.getByteLength(dto.getTrackingNo()) > ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()) {
                dto.setTrackingNo(StringUtil.cutString(dto.getTrackingNo(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.trackingNumber.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_30.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }

        if (!StringUtil.isEmpty(dto.getDeliveryMessage())) {
            if (StringUtil.getByteLength(dto.getDeliveryMessage()) > ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength()) {
                dto.setDeliveryMessage(StringUtil.cutString(dto.getTrackingNo(), ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength(), ""));
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_LENGTH_OVER.getMessage(), ExcelUploadEnums.SabangnetCols.deliveryMessage.getCodeName(), ExcelUploadValidateEnums.ByteLength.BYTE_150.getByteLength()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }



        //문자 포함여부 확인
        if (!StringUtil.isDigits(dto.getIlGoodsId())) {
            dto.setIlGoodsId("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.ilGoodsId.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (!StringUtil.isDigits(dto.getOrderCount())) {
            dto.setOrderCount("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (dto.getOrderCount().contains(".")) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_DECIMAL_POINT.getMessage(), ExcelUploadEnums.SabangnetCols.orderCount.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        if (!StringUtil.isDigits(dto.getPaidPrice())) {
            dto.setPaidPrice("0");
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_STRING.getMessage(), ExcelUploadEnums.SabangnetCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        if (dto.getPaidPrice().contains(".")) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_DECIMAL_POINT.getMessage(), ExcelUploadEnums.SabangnetCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        //상품수량, 판매가 확인
        // 수량은 무조건 있어야해서 다시 풀었음. -- 최용호
        if (!StringUtil.isEmpty(dto.getOrderCount())) {
            if (Integer.parseInt(dto.getOrderCount()) <= 0) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.SabangnetCols.orderCount.getCodeName()));
                returnStatus = BaseEnums.Default.FAIL;
            }
        }
        /*
        if (Integer.parseInt(dto.getPaidPrice()) <= 0) {
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLUMN_NUMBER_ZERO.getMessage(), ExcelUploadEnums.SabangnetCols.paidPrice.getCodeName()));
            returnStatus = BaseEnums.Default.FAIL;
        }
        */

        // 수집몰 상세 번호 체크
        Map<String, String> collectionMallIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("collectionMallIdList");

        if (collectionMallIdMaps.containsKey(dto.getCollectionMallId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.SABANGNET_COLLECTION_MALL_ID_OVERLAP.getMessage(), dto.getCollectionMallId()));
            returnStatus = BaseEnums.Default.FAIL;
        }

        Map<String, String> collectionMallDetailIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("collectionMallDetailIdList");

        if (collectionMallDetailIdMaps.containsKey(dto.getCollectionMallDetailId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.SABANGNET_COLLECTION_MALL_DETAIL_ID_OVERLAP.getMessage(), dto.getCollectionMallDetailId()));
            returnStatus = BaseEnums.Default.FAIL;
        }
/*

        Map<String, String> outmallIdMaps  = (Map<String, String>) collectionMallValidatorIdMaps.get("outmallIdList");

        if (outmallIdMaps.containsKey(dto.getOutMallId())){
            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.COLLECTION_MALL_OUTMALL_ID_OVERLAP.getMessage(), dto.getOutMallId()));
            returnStatus = BaseEnums.Default.FAIL;
        }
*/



        //상품상태 확인 및 품목코드 설정
        GoodsSearchOutMallVo goodsSearchOutMallVo = goodsMaps.get(dto.getIlGoodsId());
        Long omSellersId = Long.parseLong(dto.getOmSellersId());
        
        if (goodsSearchOutMallVo == null) {
            boolean checkSearchFlag = false;
            for(long key : goodsMaps.keySet()) {

                goodsSearchOutMallVo = goodsMaps.get(key);

                // 풀샵 상품코드 세팅
                dto.setGoodsNo(Long.toString(goodsSearchOutMallVo.getGoodsNo()));

                if(goodsSearchOutMallVo.getGoodsNo() == 0L) {
                    if (dto.getIlGoodsId().equals(Long.toString(goodsSearchOutMallVo.getGoodsId()))) {
                        dto.setIlGoodsId(Long.toString(goodsSearchOutMallVo.getGoodsId()));
                        checkSearchFlag = true;

                        /*if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                            checkSearchFlag = false;
                        }*/

                        // 허용 상품 상태
                        if (!GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                        ) {
                            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName()));
                            returnStatus = BaseEnums.Default.FAIL;
                            //checkSearchFlag = false;
                        }

                        // 출고처 배송불가지역 체크
                        try{
                            // 배송불가지역 체크
                            boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                            if(isUndeliveableArea){
                                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                                returnStatus = BaseEnums.Default.FAIL;
                            }

                        }catch(Exception e){
                            dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 판매처에 등록된 공급업체의 상품인지 확인
                        long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
                        for(OutMallOrderSellersDto sellerDto : sellersList){
                            if(omSellersId.equals(sellerDto.getOmSellersId())){
                                boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                                if(!isExistSupplierGoods){
                                    dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                                    returnStatus = BaseEnums.Default.FAIL;
                                }
                            }
                        }

                        // 품목코드 체크
                        dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
                        break;
                    }
                }
                else {
                    // 매핑했을경우
                    if (dto.getIlGoodsId().equals(Long.toString(goodsSearchOutMallVo.getGoodsNo()))){
                        dto.setIlGoodsId(Long.toString(goodsSearchOutMallVo.getGoodsId()));
                        checkSearchFlag = true;

                        /*if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                            checkSearchFlag = false;
                        }*/

                        // 허용 상품 상태
                        if (       !GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                                && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                        ) {
                            dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName()));
                            returnStatus = BaseEnums.Default.FAIL;
                            //checkSearchFlag = false;
                        }

                        // 출고처 배송불가지역 체크
                        try{
                            // 배송불가지역 체크
                            boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                            if(isUndeliveableArea){
                                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                                returnStatus = BaseEnums.Default.FAIL;
                            }

                        }catch(Exception e){
                            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                            returnStatus = BaseEnums.Default.FAIL;
                        }

                        // 판매처에 등록된 공급업체의 상품인지 확인
                        long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
                        for(OutMallOrderSellersDto sellerDto : sellersList){
                            if(omSellersId.equals(sellerDto.getOmSellersId())){
                                boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                                if(!isExistSupplierGoods){
                                    dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                                    returnStatus = BaseEnums.Default.FAIL;
                                }
                            }
                        }

                        // 품목코드 체크
                        dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
                        break;
                    }
                }
            }
            if (checkSearchFlag == false) {
                dto.setGoodsNo(dto.getIlGoodsId());
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_ID_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }
        } else {

            // 풀샵 상품코드 세팅
            dto.setGoodsNo(Long.toString(goodsSearchOutMallVo.getGoodsNo()));

            /*if (!GoodsEnums.GoodsOutmallSaleStat.ON_SALE.getCode().equals(goodsSearchOutMallVo.getGoodsOutmallSaleStat())){
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_OUTMALL_STATUS_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }*/

            // 허용 상품 상태
            if (       !GoodsEnums.SaleStatus.WAIT.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                    && !GoodsEnums.SaleStatus.ON_SALE.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
                    && !GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_MANAGER.getCode().equals(goodsSearchOutMallVo.getSaleStatus())
            ) {
                dto.setFailMessage(dto.getFailMessage() +  MessageFormat.format(ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage(), GoodsEnums.SaleStatus.findByCode(goodsSearchOutMallVo.getSaleStatus()).getCodeName() ));
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 출고처 배송불가지역 체크
            try{
                // 배송불가지역 체크
                boolean isUndeliveableArea = isUndeliverableArea(Long.parseLong(goodsSearchOutMallVo.getUrWarehouseId()), goodsSearchOutMallVo.getGoodsId(), dto.getReceiverZipCode(), goodsSearchOutMallVo.getUndeliverableAreaTp());

                if(isUndeliveableArea){
                    dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                    returnStatus = BaseEnums.Default.FAIL;
                }

            }catch(Exception e){
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.WAREHOUSE_UNDELIVERABLE_AREA.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }

            // 판매처에 등록된 공급업체의 상품인지 확인
            long goodsUrSupplierId = goodsSearchOutMallVo.getUrSupplierId();
            for(OutMallOrderSellersDto sellerDto : sellersList){
                if(omSellersId.equals(sellerDto.getOmSellersId())){
                    boolean isExistSupplierGoods = sellerDto.getSupplierList().stream().anyMatch(f->f.getUrSupplierId() == goodsUrSupplierId);
                    if(!isExistSupplierGoods){
                        dto.setFailMessage(dto.getFailMessage() + ExcelUploadValidateEnums.ValidateType.NOT_REGISTRATION_GOODS_IN_SELLER.getMessage());
                        returnStatus = BaseEnums.Default.FAIL;
                    }
                }
            }

            /*
            if (goodsSearchOutMallVo.getSaleStatus().equals(GoodsEnums.SaleStatus.STOP_SALE.getCode())) {
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_STOP_SALE.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }
            if (!goodsSearchOutMallVo.getSaleStatus().equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
                dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.GOODS_STATUS_ERROR.getMessage());
                returnStatus = BaseEnums.Default.FAIL;
            }

             */

            // 품목코드 체크
            dto.setIlItemCd(goodsSearchOutMallVo.getIlItemCd());
        }

        //주문자 정보 확인
        /*
        if (!PhoneUtil.isLandlineValidate(dto.getBuyerTel()) || !PhoneUtil.isCellValidate(dto.getBuyerMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.BUYER_INFO_ERROR.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
            //return ExcelUploadValidateEums.ValidateType.BUYER_INFO_ERROR;
        }
         */
        //수취인 정보 확인
        /*
        if (!PhoneUtil.isLandlineValidate(dto.getReceiverTel()) || !PhoneUtil.isCellValidate(dto.getReceiverMobile())) {
            dto.setFailMessage(dto.getFailMessage() +  ExcelUploadValidateEnums.ValidateType.RECEIVER_INFO_ERROR.getMessage());
            returnStatus = BaseEnums.Default.FAIL;
        }
         */

        return returnStatus;
    }

}
