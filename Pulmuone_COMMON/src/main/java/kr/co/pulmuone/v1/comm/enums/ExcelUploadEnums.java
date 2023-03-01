package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ExcelUploadEnums {


    // 업로드 유형
    @Getter
    @RequiredArgsConstructor
    public enum ExcelUploadType implements CodeCommEnum {
        EASYADMIN("E", "이지어드민", "EasyAdmin"),
        SABANGNET("S", "사방넷", "Sabangnet"),
        BOS_CREATE("B", "주문생성", "BOSCreate"),
        CAL_PG("PG", "PG거래내역대사", "CalculatePg"),
        CAL_OUTMALL("OUTMALL", "외부몰주문대사", "CalculateOutmall"),
        CAL_POV("POV", "POV I/F", "CalculatePov"),
        CAL_POV_VDC("POV_VDC", "POV VDC I/F", "CalculatePov"),
        TRACKING_NUMBER("TRACKING_NUMBER", "송장일괄업로드", "trackingNumber"),
        INTERFACE_DAY_CHANGE("INTERFACE_DAY_CHANGE", "I/F 일자 업데이트", "interfaceDayChange"),
        CLAIM_EXCEL_UPLOAD("CLAIM_EXCEL_UPLOAD", "클레임 엑셀 업로드", "claimExcelUpload"),
        SHIPPING_AREA_EXCEL_UPLOAD("SHIPPING_AREA_EXCEL_UPLOAD", "도서산간/배송불가 권역 엑셀 업로드", "shippingAreaExcelUpload")
        ;

        private final String code;
        private final String codeName;
        private final String codeFullName;
    }

    // 이지어드민 업로드 컬럼
    @Getter
    @RequiredArgsConstructor
    public enum EasyAdminCols {
        collectionMallId        (0, "합포번호"),
        collectionMallDetailId  (1, "관리번호"),
        omSellersId             (2, "외부몰코드(판매처코드)"),
        ilGoodsId               (3, "상품코드"),
        ilItemCd                (4, "ERP상품코드"),
        goodsName               (5, "상품명"),
        orderCount              (6, "수량"),
        paidPrice               (7, "상품총금액"),
        buyerName               (8, "주문자명"),
        buyerTel                (9, "주문자 연락처"),
        buyerMobile             (10, "주문자 휴대폰번호"),
        receiverName            (11, "수취인명"),
        receiverTel             (12, "수취인 연락처"),
        receiverMobile          (13, "수취인 휴대폰번호"),
        receiverZipCode         (14, "수취인 우편번호"),
        receiverAddress1        (15, "수취인 주소1"),
        receiverAddress2        (16, "수취인 주소2"),
        shippingPrice           (17, "배송비"),
        deliveryMessage         (18, "메세지"),
        deliLogisCd             (19, "택배사"),
        trackingNumber          (20, "송장번호"),
        outMallId               (21, "외부몰주문번호"),
        memo                    (22, "고객상담"),
        outmallOrderDt          (23, "외부몰주문일"),
        outmallGoodsNm          (24, "외부몰상품명"),
        outmallOptNm            (25, "외부몰옵션명"),
        ;


        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum SabangnetCols {
        collectionMallId        (0, "수집몰주문번호"),
        collectionMallDetailId  (1, "수집몰상세번호"),
        omSellersId             (2, "외부몰코드(판매처코드)"),
        ilGoodsId               (3, "상품코드"),
        ilItemCd                (4, "ERP상품코드"),
        goodsName               (5, "상품명"),
        orderCount              (6, "수량"),
        paidPrice               (7, "상품총금액"),
        buyerName               (8, "주문자명"),
        buyerTel                (9, "주문자 연락처"),
        buyerMobile             (10, "주문자 휴대폰번호"),
        receiverName            (11, "수취인명"),
        receiverTel             (12, "수취인 연락처"),
        receiverMobile          (13, "수취인 휴대폰번호"),
        receiverZipCode         (14, "수취인 우편번호"),
        receiverAddress1        (15, "수취인 주소1"),
        receiverAddress2        (16, "수취인 주소2"),
        shippingPrice           (17, "배송비"),
        deliveryMessage         (18, "메세지"),
        deliLogisCd             (19, "택배사"),
        trackingNumber          (20, "송장번호"),
        outMallId               (21, "외부몰주문번호"),
        memo                    (22, "고객상담"),
        outmallOrderDt          (23, "외부몰주문일"),
        outmallGoodsNm          (24, "외부몰상품명"),
        outmallOptNm            (25, "외부몰옵션명"),
        ;


        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum BosCreateCols {
        recvNm      (0, "수취인명"),
        recvHp      (1, "수취인연락처"),
        recvZipCd   (2, "우편번호"),
        recvAddr1   (3, "주소1"),
        recvAddr2   (4, "주소2"),
        ilGoodsId   (5, "상품코드"),
        orderCnt    (6, "수량"),
        salePrice   (7, "판매가"),
        orderStr    (8, "주문생성"),
        ;


        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CalculatePgCardCols {
    	giveDt   		(2, "giveDt"),
    	approvalDt      (3, "approvalDt"),
    	cardNm     		(5, "cardNm"),
    	cardAuthNum     (6, "cardAuthNum"),
    	cardQuota       (7, "cardQuota"),
    	odid    		(8, "odid"),
    	tid     		(9, "tid"),
    	transAmt     	(12, "transAmt"),
    	mPoint     		(13, "mPoint"),
    	commission     	(14, "commission"),
    	freeCommission  (15, "freeCommission"),
    	mPointCommission     (16, "mPointCommission"),
    	marketingCommission  (17, "marketingCommission"),
    	vat     		(18, "vat"),
    	mPointVat     	(19, "mPointVat"),
    	marketingVat    (20, "marketingVat"),
        ;

        private final int colNum;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum CalculatePgBankCols {
    	giveDt   		(2, "giveDt"),
    	approvalDt      (3, "approvalDt"),
    	bankNm     		(5, "cardNm"),
    	bankAccountNumber     (7, "cardAuthNum"),
    	odid    		(8, "odid"),
    	tid     		(9, "tid"),
    	transAmt     	(12, "transAmt"),
    	commission     	(13, "commission"),
    	certificationCommission  (14, "freeCommission"),
    	vat     		(15, "vat"),
    	certificationVat  (16, "mPointVat"),
        ;

        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CalculatePgVirrualBankCols {
    	giveDt   		(2, "giveDt"),
    	approvalDt      (3, "approvalDt"),
    	bankNm     		(5, "cardNm"),
    	bankAccountNumber     (6, "cardAuthNum"),
    	odid    		(8, "odid"),
    	tid     		(9, "tid"),
    	transAmt     	(12, "transAmt"),
    	commission     	(13, "commission"),
    	vat     		(14, "vat"),
        ;

        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CalculatePgKcpCols {
    	odid    		(1, "odid"),
    	transAmt     	(2, "transAmt"),
    	commission     	(3, "commission"),
    	vat     		(4, "vat"),
    	tid     		(5, "tid"),
    	approvalDt      (6, "approvalDt"),
    	accountDt     	(7, "accountDt"),
    	giveDt   		(8, "giveDt"),
    	escrowCommission     (9, "escrowCommission"),
    	escrowVat 		(10, "escrowVat"),
    	accountAmt 		(11, "accountAmt"),
        ;

        private final int colNum;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum CalculatePgCols {
    	pgService     	(0, "pgService"),
    	type   			(1, "type"),
    	odid    		(2, "odid"),
    	tid     		(3, "tid"),
    	approvalDt      (4, "approvalDt"),
    	transAmt     	(5, "transAmt"),
    	deductAmt     	(6, "deductAmt"),
    	accountAmt     	(7, "accountAmt"),
    	commission     	(8, "commission"),
    	vat     		(9, "vat"),
    	giveDt     		(10, "giveDt"),
    	escrowCommission     (11, "escrowCommission"),
    	escrowVat     	(12, "escrowVat"),
    	mPoint     		(13, "mPoint"),
    	mPointCommission     (14, "mPointCommission"),
    	mPointVat     	(15, "mPointVat"),
    	marketingCommission     (16, "marketingCommission"),
    	marketingVat    (17, "marketingVat"),
    	certificationCommission     (18, "certificationCommission"),
    	certificationVat     (19, "certificationVat"),
    	freeCommission     (20, "freeCommission"),
    	cardNm     (21, "cardNm"),
    	cardAuthNum     (22, "cardAuthNum"),
    	cardQuota     (23, "cardQuota"),
    	bankNm     (24, "bankNm"),
    	bankAccountNumber     (25, "bankAccountNumber"),
    	accountDt     (26, "accountDt"),
        ;


        private final int colNum;
        private final String codeName;
    }




    @Getter
    @RequiredArgsConstructor
    public enum CalculateOutmallCols {
    	outomallId      (0, "outomallId"),
    	sellersNm       (1, "sellersNm"),
    	orderAmt		(2, "orderAmt"),
    	orderCnt		(3, "orderCnt"),
    	icDt			(4, "icDt"),
    	orderIfDt		(5, "orderIfDt"),
    	settleDt		(6, "settleDt"),
    	goodsNm			(7, "goodsNm"),
    	discountPrice	(8, "discountPrice"),
    	couponPrice		(9, "couponPrice"),
    	etcDiscountPrice	(10, "etcDiscountPrice"),
    	settlePrice		(11, "settlePrice"),
    	settleItemCnt	(12, "settleItemCnt"),
        ;


        private final int colNum;
        private final String codeName;
    }

    // 이니시스 유형
    @Getter
    @RequiredArgsConstructor
    public enum InicisType implements CodeCommEnum {
        card("PAY_TP.CARD", "신용카드"),
        bank("PAY_TP.BANK", "실시간계좌이체"),
        virtualBank("PAY_TP.VIRTUAL_BANK", "가상계좌")
        ;

        private final String code;
        private final String codeName;
    }

    // 결제타입
    @Getter
    @RequiredArgsConstructor
    public enum PayType implements CodeCommEnum{
        G      ("G", "결제"),
        F      ("F", "환불"),
        ;


        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CalculatePovCols {
    	CORPORATION_CODE      	(2, "C"),
    	CHANNEL_CODE      		(4, "E"),
    	SKU_CODE      			(6, "G"),
    	ACCOUNT_CODE      		(8, "I"),
    	COST      				(10, "K"),
        ;


        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum CalculatePovVdcCols {
    	CORPORATION_CODE      	(4, "E"),
    	CHANNEL_CODE      		(6, "G"),
    	SKU_CODE      			(8, "I"),
    	ACCOUNT_CODE      		(10, "K"),
    	COST      				(12, "M"),
    	FACTORY_CODE      		(2, "C"),   //출고처 코드
        ;


        private final int colNum;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum TrackingNumberCols {
        odid                (0, "주문번호"),
        odOrderDetlSeq      (1, "주문상세순번"),
        psShippingCompId   (2, "택배사코드"),
        trackingNo          (3, "개별송장번호"),
        ;


        private final int colNum;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum InterfaceDayChangeCols {
        odid                (0, "주문번호"),
        odOrderDetlSeq      (1, "주문상세순번"),
        ifDay               (2, "I/F 변경 일자"),
        ;


        private final int colNum;
        private final String codeName;
    }


    @Getter
    @RequiredArgsConstructor
    public enum ClaimExcelUploadCols {
        odid                (0, "주문번호"),
        odOrderDetlSeq      (1, "주문상세순번"),
        claimCnt            (2, "클레임수량"),
        psClaimBosId        (3, "BOS 클레임사유 PK"),
        returnsYn           (4, "반품회수여부"),
        consultMsg          (5, "고객상담내용"),
        claimStatusTp       (6, "클레임상태구분"),
        ;


        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AdvertisingUploadCols {
        advertisingName (0, "광고명"),
        pmAdExternalCd  (1, "광고 ID"),
        source          (2, "매체(source)"),
        medium          (3, "구좌(medium)"),
        campaign        (4, "캠페인(campaign)"),
        term            (5, "키워드(term)"),
        content         (6, "콘텐츠(content)"),
        useYn           (7, "사용여부"),
        redirectUrl     (8, "Redirect URL")
        ;

        private final int colNum;
        private final String codeName;
    }

    //식단패턴
    @Getter
    @RequiredArgsConstructor
    public enum MealPatternUploadCols {
        patternNo(0, "패턴순번"),
        setNo(1, "세트순번"),
        setCd(2, "세트코드"),
        setNm(3, "세트명"),
        mealContsCd(4, "식단품목코드")
        ;

        private final int colNum;
        private final String codeName;
    }

    // 식단 컨텐츠 일괄 업로드
    @Getter
    @RequiredArgsConstructor
    public enum MealContsUploadCols {
        ilGoodsDailyMealContsCd (1, "식단품목코드(필수)"),
        ilGoodsDailyMealNm      (2, "식단품목명(필수)"),
        allergyYn               (3, "알러지식단여부"),
        mallDiv                 (4, "식단분류(필수)"),
        thumbnailImg            (5, "섬네일이미지url (필수)"),
        recommendedAge          (6, "연령"),
        totalCapacity           (7, "1회 제공량"),
        calorie                 (8, "칼로리정보"),
        eatsslimIndex           (9, "잇슬림지수"),
        description             (10, "상품설명"),
        allergyEgg              (11, "계란"),
        allergyMilk             (12, "우유"),
        allergySoybean          (13, "대두"),
        allergyBuckwheat        (14, "메밀"),
        allergyWheat            (15, "밀"),
        allergyTomato           (16, "토마토"),
        allergyPinenut          (17, "잣"),
        allergyWalnut           (18, "호두"),
        allergyPeanut           (19, "땅콩"),
        allergyPork             (20, "돼지고기"),
        allergyBeef             (21, "소고기"),
        allergyChicken          (22, "닭고기"),
        allergyCrab             (23, "게"),
        allergyMackerel         (24, "고등어"),
        allergySquid            (25, "오징어"),
        allergyShellfish        (26, "조개류"),
        allergyShrimp           (27, "새우"),
        allergySulfite          (28, "아황산류"),
        allergyPeach            (29, "복숭아"),
        nutritionTotalCarbohydrate      (30, "탄수화물"),
        nutritionTotalCarbohydrateRate  (31, "탄수화물 1일 기준치 비율(%)"),
        nutritionFiber          (32, "식이섬유"),
        nutritionFiberRate      (33, "식이섬유 1일 기준치 비율(%)"),
        nutritionSugars         (34, "당류"),
        nutritionSugarsRate     (35, "당류 1일 기준치 비율(%)"),
        nutritionTotalFat       (36, "지방"),
        nutritionTotalFatRate   (37, "지방 1일 기준치 비율(%)"),
        nutritionSaturatedFat   (38, "포화지방"),
        nutritionSaturatedFatRate  (39, "포화지방 1일 기준치 비율(%)"),
        nutritionTransFat       (40, "트렌스지방"),
        nutritionTransFatRate   (41, "트렌스지방 1일 기준치 비율(%)"),
        nutritionProtein        (42, "단백질"),
        nutritionProteinRate    (43, "단백질 1일 기준치 비율(%)"),
        nutritionCholesterol    (44, "콜레스테롤"),
        nutritionCholesterolRate(45, "콜레스테롤 1일 기준치 비율(%)"),
        nutritionSodium         (46, "나트륨"),
        nutritionSodiumRate     (47, "나트륨 1일 기준치 비율(%)"),
        specGoodsName           (48, "제품명"),
        specGoodsType           (49, "식품유형"),
        specProducerLocation    (50, "생산자 및 소재지"),
        specManufacturingDate   (51, "제조일자"),
        specExpirationDate      (52, "유통기한"),
        specStorageMethod       (53, "보관방법"),
        specOriginalMaterial    (54, "원재료 및 함량"),
        ;

        private final int colNum;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ShippingAreaExcelUploadCols {
        zipCd               (0, "우편번호"),
        useYn               (1, "사용여부(등록 또는 삭제)"),
        alternateDeliveryYn (2, "대체배송(Y)")
        ;

        private final int colNum;
        private final String codeName;
    }

}