package kr.co.pulmuone.v1.comm.excel.util;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.calculate.pov.dto.CalPovUploadDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.ExcelUploadEnums;
import kr.co.pulmuone.v1.comm.enums.PovEnums.Corporation;
import kr.co.pulmuone.v1.comm.util.BigDecimalUtils;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.ClaimInfoExcelUploadDto;
import kr.co.pulmuone.v1.order.create.dto.OrderExeclDto;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import kr.co.pulmuone.v1.order.ifday.dto.IfDayChangeDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.policy.shippingarea.dto.ShippingAreaExcelUploadDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class OrderExcelSetData {

    private int addRowNum = 1;
    private int addRowSpanNum = 2;

    public List<OutMallOrderDto> setEasyAdminExcelData(String excelUploadType, Sheet sheet) {
        List<OutMallOrderDto> excelList = new ArrayList<>();
        String address, address2, addr1, addr2 = "";
        String buyerName, receiverName = "";
        String buyerTel         = "";
        String buyerMobile      = "";
        String receiverTel      = "";
        String receiverMobile   = "";
        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row == null ||StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0)).trim()) && StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)).trim())) {
                continue;
            }
            if("필수값".equals(ExcelUploadUtil.cellValue(row.getCell(0)).trim())) {
            	break;
            }
            //if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))) {
                address         = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.receiverAddress1.getColNum()));
                address2        = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.receiverAddress2.getColNum()));

                addr1           = StringUtil.nvl(address);
                addr2           = StringUtil.nvl(address2);
                buyerName       = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.buyerName.getColNum()));
                buyerTel        = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.buyerTel.getColNum()));
                buyerMobile     = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.buyerMobile.getColNum()));
                receiverName    = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.receiverName.getColNum()));
                receiverTel     = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverTel.getColNum()));
                receiverMobile  = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverMobile.getColNum()));


                if(StringUtils.isEmpty(addr2)){
                    if(ExcelUploadUtil.isSplitAdress(address)){
                        addr1 = ExcelUploadUtil.splitAdress(address, 1);
                        addr2 = ExcelUploadUtil.splitAdress(address, 2);
                    }
                }



                excelList.add(OutMallOrderDto.builder()
                        .outmallType(               excelUploadType)
                        .collectionMallId(          ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.collectionMallId.getColNum())))
                        .collectionMallDetailId(    ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.collectionMallDetailId.getColNum())))
                        .omSellersId(               ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.omSellersId.getColNum())))
                        .ilGoodsId(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.ilGoodsId.getColNum())))
                        .ilItemCd(                  StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.ilItemCd.getColNum()))))
                        .goodsName(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.goodsName.getColNum())))
                        .orderCount(                StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(StringUtil.digitsOnly(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.orderCount.getColNum()))), "0")), "0"))
                        .paidPrice(                 StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(StringUtil.digitsOnly(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.paidPrice.getColNum()))), "0")), "0"))
                        .buyerName(                 (StringUtils.isEmpty(buyerName) ? receiverName : buyerName))
                        .buyerTel(                  ExcelUploadUtil.defaultPhoneNumber(buyerTel, buyerMobile, receiverTel, receiverMobile))
                        .buyerMobile(               ExcelUploadUtil.defaultPhoneNumber(buyerMobile, buyerTel, receiverMobile, receiverTel))
                        .receiverName(              (StringUtils.isEmpty(receiverName) ? buyerName : receiverName))
                        .receiverTel(               ExcelUploadUtil.defaultPhoneNumber(receiverTel, receiverMobile, buyerTel, buyerMobile))
                        .receiverMobile(            ExcelUploadUtil.defaultPhoneNumber(receiverMobile, receiverTel, buyerMobile, buyerTel))
                        .receiverZipCode(           ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.receiverZipCode.getColNum())).replaceAll("-", ""))
                        .receiverAddress1(          addr1)
                        .receiverAddress2(          addr2)
                        .shippingPrice(             StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.shippingPrice.getColNum())), "0")), "0"))
                        .deliveryMessage(           ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.deliveryMessage.getColNum())))
                        .logisticsCd(               ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.deliLogisCd.getColNum())))
                        .trackingNo(                ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.trackingNumber.getColNum())))
                        .outMallId(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.outMallId.getColNum())))
                        .memo(                      ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.memo.getColNum())))
                        .outmallOrderDt(            ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.outmallOrderDt.getColNum())))
                        .outmallGoodsNm(            ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.outmallGoodsNm.getColNum())))
                        .outmallOptNm(              ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.EasyAdminCols.outmallOptNm.getColNum())))
                        .success(true)
                        .build());
            //}
        }

        return excelList;
    }

    public List<OutMallOrderDto> setSabangnetExcelData(String excelUploadType, Sheet sheet) {
        List<OutMallOrderDto> excelList = new ArrayList<>();
        String buyerTel         = "";
        String buyerMobile      = "";
        String receiverTel      = "";
        String receiverMobile   = "";
        String oldOutmallId     = "";
        String outmallId        = "";
        String omSellersId      = "";
        String collectionMallId = "";
        String oldCollectionMallId = "";
        String collectionMallDetailId = "";
        String receiverName = "";
        String receiverZipCode = "";
        String receiverAddress1 = "";
        String receiverAddress2 = "";
        String collectionMallKey = "";
        String address, address2, addr1, addr2 = "";
        Map<String, String> collectionMap = new HashMap<>();


        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row == null || StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)).trim()) && StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(21)).trim())) {
                continue;
            }
            if("필수값".equals(ExcelUploadUtil.cellValue(row.getCell(0)).trim())) {
            	break;
            }

            collectionMallDetailId      = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.collectionMallDetailId.getColNum()));
            outmallId                   = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.outMallId.getColNum()));
            omSellersId                 = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.omSellersId.getColNum()));
            receiverName                = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverName.getColNum()));
            receiverZipCode             = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverZipCode.getColNum()));
            address                     = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverAddress1.getColNum()));
            address2                    = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverAddress2.getColNum()));
            receiverAddress1            = StringUtil.nvl(address);
            receiverAddress2            = StringUtil.nvl(address2);

            if(StringUtils.isEmpty(receiverAddress2)){
                if(ExcelUploadUtil.isSplitAdress(address)){
                    receiverAddress1 = ExcelUploadUtil.splitAdress(address, 1);
                    receiverAddress2 = (ExcelUploadUtil.splitAdress(address, 2));
                }
            }

            //if(!"".equals(collectionMallDetailId)) {
                collectionMallKey = omSellersId+"_"+receiverName+"_"+receiverZipCode+"_"+receiverAddress1+"_"+receiverAddress2+"_"+outmallId;
                if (collectionMap.containsKey(collectionMallKey) != true){
                    collectionMap.put(collectionMallKey, collectionMallDetailId);
                }

                collectionMallId = collectionMap.get(collectionMallKey);
                //if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))) {

                buyerTel = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.buyerTel.getColNum()));
                buyerMobile = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.buyerMobile.getColNum()));
                receiverTel = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverTel.getColNum()));
                receiverMobile = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.receiverMobile.getColNum()));

                excelList.add(OutMallOrderDto.builder()
                        .outmallType(excelUploadType)
                        //.collectionMallId(          ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.collectionMallId.getColNum())))
                        .collectionMallId(          collectionMallId)
                        .collectionMallDetailId(    collectionMallDetailId)
                        .omSellersId(               omSellersId)
                        .ilGoodsId(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.ilGoodsId.getColNum())))
                        .ilItemCd(                  StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.ilItemCd.getColNum()))))
                        .goodsName(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.goodsName.getColNum())))
                        //.orderCount(                StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.orderCount.getColNum())), "0")), "0"))
                        //.paidPrice(                 StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.paidPrice.getColNum())), "0")), "0"))
                        .orderCount(                ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.orderCount.getColNum())))
                        .paidPrice(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.paidPrice.getColNum())))
                        .buyerName(                 ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.buyerName.getColNum())))
                        .buyerTel(                  ExcelUploadUtil.defaultPhoneNumber(buyerTel, buyerMobile, receiverTel, receiverMobile))
                        .buyerMobile(               ExcelUploadUtil.defaultPhoneNumber(buyerMobile, buyerTel, receiverMobile, receiverTel))
                        .receiverName(              receiverName)
                        .receiverTel(               ExcelUploadUtil.defaultPhoneNumber(receiverTel, receiverMobile, buyerTel, buyerMobile))
                        .receiverMobile(            ExcelUploadUtil.defaultPhoneNumber(receiverMobile, receiverTel, buyerMobile, buyerTel))
                        .receiverZipCode(           receiverZipCode.replaceAll("-", ""))
                        .receiverAddress1(          receiverAddress1)
                        .receiverAddress2(          receiverAddress2)
                        .shippingPrice(             StringUtil.nvl(StringUtil.nvlInt(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.shippingPrice.getColNum())), "0")), "0"))
                        .deliveryMessage(           ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.deliveryMessage.getColNum())))
                        .logisticsCd(               ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.deliLogisCd.getColNum())))
                        .trackingNo(                ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.trackingNumber.getColNum())))
                        .outMallId(                 outmallId)
                        .memo(                      ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.memo.getColNum())))
                        .outmallOrderDt(            ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.outmallOrderDt.getColNum())))
                        .outmallGoodsNm(            ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.outmallGoodsNm.getColNum())))
                        .outmallOptNm(              ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.SabangnetCols.outmallOptNm.getColNum())))
                        .success(true)
                        .build());

                oldOutmallId = outmallId;
            //}
        }
        //}


        return excelList;
    }

    public List<OrderExeclDto> setBosCreateExcelData(Sheet sheet) {
        List<OrderExeclDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if(row != null){
                Iterator cellItr = row.cellIterator();

                while ( cellItr.hasNext() ) {
                    XSSFCell cell = (XSSFCell) cellItr.next();
                }

                // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
                if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                        || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
                ) {
                    excelList.add(OrderExeclDto.builder()
                            .recvNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.recvNm.getColNum())))
                            .recvHp(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.recvHp.getColNum())).replaceAll("-", ""))
                            .recvZipCd(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.recvZipCd.getColNum())).replaceAll("-", ""))
                            .recvAddr1(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.recvAddr1.getColNum())))
                            .recvAddr2(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.recvAddr2.getColNum())))
                            .ilGoodsId(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.ilGoodsId.getColNum())), "0"))
                            .orderCnt(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.orderCnt.getColNum())), "0"))
                            .salePrice(StringUtil.nvl(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.salePrice.getColNum())), "0"))
                            .orderStr(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.BosCreateCols.orderStr.getColNum())))
                            .success(true)
                            .build());
                }
            }
        }
        return excelList;
    }

    /**
     * 정산관리 > 대사관리 > PG 거래내역대사
     * @param sheet
     * @return
     */
    public List<CalPgUploadDto> setCalculatePgExcelData(Sheet sheet) {
    	List<CalPgUploadDto> excelList = new ArrayList<>();


        Row rowTitle = sheet.getRow(0);
        String inicisType ="";

        if(ExcelUploadUtil.cellValue(rowTitle.getCell(5)).equals("카드")) {
        	inicisType = ExcelUploadEnums.InicisType.card.getCode();
        }else if(ExcelUploadUtil.cellValue(rowTitle.getCell(6)).equals("거래유형")) {
        	inicisType = ExcelUploadEnums.InicisType.bank.getCode();
        }else if(ExcelUploadUtil.cellValue(rowTitle.getCell(6)).equals("계좌번호")) {
        	inicisType = ExcelUploadEnums.InicisType.virtualBank.getCode();
        }

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum()-1; i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while ( cellItr.hasNext() ) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
            ) {

            	if(ExcelUploadEnums.InicisType.card.getCode().equals(inicisType)) {         // PG사 이니시스 - 신용카드
            		excelList.add(CalPgUploadDto.builder()
                            .giveDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.giveDt.getColNum())))
                            .approvalDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.approvalDt.getColNum())))
                            .cardNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.cardNm.getColNum())))
                            .cardAuthNum(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.cardAuthNum.getColNum())))
                            .cardQuota(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.cardQuota.getColNum())))
                            .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.odid.getColNum())))
                            .tid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.tid.getColNum())))
                            .transAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.transAmt.getColNum())))
                            .mPoint(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.mPoint.getColNum())))
                            .commission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.commission.getColNum())))
                            .freeCommission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.freeCommission.getColNum())))
                            .mPointCommission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.mPointCommission.getColNum())))
                            .marketingCommission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.marketingCommission.getColNum())))
                            .vat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.vat.getColNum())))
                            .mPointVat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.mPointVat.getColNum())))
                            .marketingVat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgCardCols.marketingVat.getColNum())))
                            .inicisType(inicisType)
                            .success(true)
                            .build());
            	}else if(ExcelUploadEnums.InicisType.bank.getCode().equals(inicisType)) {       // PG사 이니시스 - 계좌이체
            		excelList.add(CalPgUploadDto.builder()
                            .giveDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.giveDt.getColNum())))
                            .approvalDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.approvalDt.getColNum())))
                            .bankNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.bankNm.getColNum())))
                            .bankAccountNumber(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.bankAccountNumber.getColNum())))
                            .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.odid.getColNum())))
                            .tid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.tid.getColNum())))
                            .transAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.transAmt.getColNum())))
                            .commission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.commission.getColNum())))
                            .certificationCommission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.certificationCommission.getColNum())))
                            .vat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.vat.getColNum())))
                            .certificationVat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgBankCols.certificationVat.getColNum())))
                            .inicisType(inicisType)
                            .success(true)
                            .build());
            	}else if(ExcelUploadEnums.InicisType.virtualBank.getCode().equals(inicisType)) {        // PG사 이니시스 - 가상계좌
            		excelList.add(CalPgUploadDto.builder()
                            .giveDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.giveDt.getColNum())))
                            .approvalDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.approvalDt.getColNum())))
                            .bankNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.bankNm.getColNum())))
                            .bankAccountNumber(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.bankAccountNumber.getColNum())))
                            .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.odid.getColNum())))
                            .tid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.tid.getColNum())))
                            .transAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.transAmt.getColNum())))
                            .commission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.commission.getColNum())))
                            .vat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgVirrualBankCols.vat.getColNum())))
                            .inicisType(inicisType)
                            .success(true)
                            .build());
            	}else {                                                                                // PG사 KCP
            		excelList.add(CalPgUploadDto.builder()
            				.odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.odid.getColNum())))
            				.transAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.transAmt.getColNum())))
            				.commission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.commission.getColNum())))
            				.vat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.vat.getColNum())))
            				.tid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.tid.getColNum())))
            				.approvalDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.approvalDt.getColNum())))
            				.accountDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.accountDt.getColNum())))
                            .giveDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.giveDt.getColNum())))
                            .escrowCommission(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.escrowCommission.getColNum())))
                            .escrowVat(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.escrowVat.getColNum())))
                            .accountAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePgKcpCols.accountAmt.getColNum())))
                            .success(true)
                            .build());
            	}
            }
        }
        return excelList;
    }

    /**
     * 정산관리 > 대사관리 > 외부몰 주문 대사
     * @param sheet
     * @return
     */
    public List<CalOutmallUploadDto> setCalculateOutmallExcelData(Sheet sheet) {
    	List<CalOutmallUploadDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while ( cellItr.hasNext() ) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
            ) {
                excelList.add(CalOutmallUploadDto.builder()
                		.outmallId(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.outomallId.getColNum())))
                        .sellersNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.sellersNm.getColNum())))
                        .orderAmt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.orderAmt.getColNum())))
                        .orderCnt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.orderCnt.getColNum())))
                        .icDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.icDt.getColNum())))
                        .orderIfDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.orderIfDt.getColNum())))
                        .settleDt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.settleDt.getColNum())))
                        .goodsNm(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.goodsNm.getColNum())))
                        .discountPrice(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.discountPrice.getColNum())))
                        .couponPrice(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.couponPrice.getColNum())))
                        .etcDiscountPrice(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.etcDiscountPrice.getColNum())))
                        .settlePrice(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.settlePrice.getColNum())))
                        .settleItemCnt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculateOutmallCols.settleItemCnt.getColNum())))
                        .success(true)
                        .build());
            }
        }
        return excelList;
    }

    /**
     * 정산관리 > POV I/F > POV I/F
     * @param sheet
     * @return
     */
    public List<CalPovUploadDto> setCalculatePovExcelData(Sheet sheet) {
        List<CalPovUploadDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            if(row != null) {

	            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
				if (Corporation.findByCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.CORPORATION_CODE.getColNum()))) != null) {
	                excelList.add(CalPovUploadDto.builder()
	                        .corporationCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.CORPORATION_CODE.getColNum())))
	                        .channelCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.CHANNEL_CODE.getColNum())))
	                        .skuCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.SKU_CODE.getColNum())))
	                        .accountCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.ACCOUNT_CODE.getColNum())))
	                        .cost(BigDecimalUtils.to(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovCols.COST.getColNum()))))
	                        .factoryCode(null)
	                        .build());
	            }
            }
        }
        return excelList;
    }

    public List<CalPovUploadDto> setCalculatePovVdcExcelData(Sheet sheet) {
        List<CalPovUploadDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row != null) {
	            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            	if (Corporation.findByCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.CORPORATION_CODE.getColNum()))) != null) {
	                excelList.add(CalPovUploadDto.builder()
	                		 .corporationCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.CORPORATION_CODE.getColNum())))
	                         .channelCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.CHANNEL_CODE.getColNum())))
	                         .skuCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.SKU_CODE.getColNum())))
	                         .accountCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.ACCOUNT_CODE.getColNum())))
	                         .cost(BigDecimalUtils.to(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.COST.getColNum()))))
	                         .factoryCode(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.CalculatePovVdcCols.FACTORY_CODE.getColNum())))
	                        .build());
	            }
            }
        }
        return excelList;
    }


    /**
     *
     * @param sheet
     * @return
     */
    public List<OrderTrackingNumberVo> setTrackingNumberExcelData(Sheet sheet) {
        List<OrderTrackingNumberVo> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while ( cellItr.hasNext() ) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
            ) {
                excelList.add(OrderTrackingNumberVo.builder()
                        .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.TrackingNumberCols.odid.getColNum())))
                        .odOrderDetlSeq(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.TrackingNumberCols.odOrderDetlSeq.getColNum()))))
                        .psShippingCompId(StringUtil.nvlLong(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.TrackingNumberCols.psShippingCompId.getColNum()))))
                        .trackingNo(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.TrackingNumberCols.trackingNo.getColNum())))
                        .successYn("Y")
                        .build());
            }
        }
        return excelList;
    }

    public List<IfDayChangeDto> setInterfaceDayChangeExcelData(Sheet sheet) {
        List<IfDayChangeDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while ( cellItr.hasNext() ) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
            ) {
                excelList.add(IfDayChangeDto.builder()
                        .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.InterfaceDayChangeCols.odid.getColNum())))
                        .odOrderDetlSeq(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.InterfaceDayChangeCols.odOrderDetlSeq.getColNum()))))
                        .ifDay(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.InterfaceDayChangeCols.ifDay.getColNum())))
                        .success(true)
                        .failMessage("")
                        .build());
            }
        }
        return excelList;
    }

    public List<ClaimInfoExcelUploadDto> setClaimExcelUploadExcelData(Sheet sheet) {
        List<ClaimInfoExcelUploadDto> excelList = new ArrayList<>();

        for (int i = sheet.getFirstRowNum()+addRowNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while ( cellItr.hasNext() ) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }

            try {
                long claimBosId = Long.parseLong(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.psClaimBosId.getColNum())));
                // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
                if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                        && StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
                ) {
                    excelList.add(ClaimInfoExcelUploadDto.builder()
                            .odid(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.odid.getColNum())))
                            .odOrderDetlSeq(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.odOrderDetlSeq.getColNum()))))
                            .claimCnt(StringUtil.nvlInt(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.claimCnt.getColNum()))))
                            .psClaimBosId(claimBosId)
                            .returnsYn(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.returnsYn.getColNum())))
                            .consultMsg(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.consultMsg.getColNum())))
                            .claimStatusTp(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ClaimExcelUploadCols.claimStatusTp.getColNum())))
                            .success(true)
                            .failMessage("")
                            .build());
                }
            } catch (NumberFormatException ignored) { }
        }
        return excelList;
    }

    public List<ShippingAreaExcelUploadDto> setShippingAreaExcelUploadExcelData(Sheet sheet) {
        List<ShippingAreaExcelUploadDto> excelList = new ArrayList<>();
        String alternateDeliveryTp = "";

        // 엑셀파일 3열부터 인식한다.
        for (int i = sheet.getFirstRowNum() + addRowSpanNum; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);

            Iterator cellItr = row.cellIterator();

            while (cellItr.hasNext()) {
                XSSFCell cell = (XSSFCell) cellItr.next();
            }
            alternateDeliveryTp = ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ShippingAreaExcelUploadCols.alternateDeliveryYn.getColNum()));

            // 대체배송가능 Y인경우 Enum 코드값으로 변경한다.
            if("Y".equals(alternateDeliveryTp)){
                alternateDeliveryTp = BaseEnums.AlternateDeliveryType.Y.getCode();
            }

            // 첫번째 두번째가 Null이 아닌경우만 데이터 생성
            if (StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))
                    || StringUtils.isNotEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))
            ) {
                excelList.add(ShippingAreaExcelUploadDto.builder()
                            .zipCd(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ShippingAreaExcelUploadCols.zipCd.getColNum())))
                            .useYn(ExcelUploadUtil.cellValue(row.getCell(ExcelUploadEnums.ShippingAreaExcelUploadCols.useYn.getColNum())))
                            .alternateDeliveryTp(alternateDeliveryTp)
                            .build());
            }
        }
        return excelList;
    }
}
