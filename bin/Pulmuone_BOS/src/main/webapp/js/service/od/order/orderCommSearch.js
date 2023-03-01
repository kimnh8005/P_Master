/**-----------------------------------------------------------------------------
 * description 		 : 주문리스트 검색 항목
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18		강윤경   최초생성
 * @ 2020.11.30     김승우   수정
 * **/
var orderSearchUtil = {

    //dropDown
    searchTypeKeyword: function(searchData,searchDataSingle){
        //복수검색 dropDown
        let searchMultiTypeDropDown =  fnKendoDropDownList(
            {
                id : "searchMultiType",
                data : searchData,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
        /*
        // 검색구분 DropDown 변경 이벤트
        searchConditionDropDown.bind("change", function() {
            if( $("#searchCondition").val() == "" ){
                $("#findKeyword").val("").attr("disabled", true);
            }else{
                $("#findKeyword").attr("disabled", false);
            }
        });

        // 입력제한
        fnInputValidationForHangulAlphabetNumber("findKeyword"); // 검색어
		*/

        //단일검색 dropDown
        let searchSingleTypeDropDown =  fnKendoDropDownList(
            {
                id : "searchSingleType",
                data : searchDataSingle,
                valueField : "CODE",
                textField : "NAME",
                blank : "선택해주세요."
            });
    },

    searchTypeKeywordForOrderDetail: function(id,searchDataForOrderDetail){
        //복수검색 dropDown
        let searchMultiTypeDropDownForOrder =  fnKendoDropDownList(
            {
                id : id,
                data : searchDataForOrderDetail,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },

    searchTypeKeywordSingle: function(searchDataSingle){
        //단일검색 dropDown
        let searchSingleTypeDropDown =  fnKendoDropDownList(
            {
                id : "searchSingleType",
                data : searchDataSingle,
                valueField : "CODE",
                textField : "NAME",
                blank : "선택해주세요."
            });
    },

    dateSearch: function(dateSearchData, minusDay){
        // 주문관리 메뉴에서만 기간검색 1주일 적용
        minusDay = stringUtil.getInt(minusDay, -30);
        let defType;
        if(minusDay == -7) {
            defType = 'oneWeek';
        } else if(minusDay == -30) {
            defType = 'oneMonth';
        }

        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : dateSearchData
        });
        // 시작일
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetDayAdd(fnGetToday(),minusDay),
            defType : defType,
            change : function(e) {
                fnDateValidation(e, "start", "dateSearchStart", "dateSearchEnd", minusDay);
                 fnValidateCal(e)
            }
        });
        // 종료일
        fnKendoDatePicker({
            id: "dateSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetToday(),
            defType : defType,
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "dateSearchStart", "dateSearchEnd");
                fnValidateCal(e)
            }
        });


        if (minusDay == -15) {
            $('[data-id="fnDateBtn4"]').mousedown();
        }

        $("#dateSearchStart, #dateSearchEnd").off().on('keydown', function(e) {
            if (e.keyCode == 13) {
                return false;
            }
        });


    },
    shopOrderDateSearch: function(dateSearchData, minusDay){
        minusDay = stringUtil.getInt(minusDay, -1);

        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : dateSearchData
        });
        // 시작일
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetToday(),
            change : function(e) {
                fnDateValidation(e, "start", "dateSearchStart", "dateSearchEnd", minusDay);
                 fnValidateCal(e)
            }
        });
        // 종료일
        fnKendoDatePicker({
            id: "dateSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetDayAdd(fnGetToday(),1),
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "dateSearchStart", "dateSearchEnd");
                fnValidateCal(e)
            }
        });


        if (minusDay == -15) {
            $('[data-id="fnDateBtn4"]').mousedown();
        }

        $("#dateSearchStart, #dateSearchEnd").off().on('keydown', function(e) {
            if (e.keyCode == 13) {
                return false;
            }
        });

    },
    dateMutilSearch: function(dateSearchData, minusDay){
        // 주문관리 메뉴에서만 기간검색 1주일 적용
        minusDay = stringUtil.getInt(minusDay,-30);
        let defType;
        if(minusDay == -7) {
            defType = 'oneWeek';
        } else if(minusDay == -30) {
            defType = 'oneMonth';
        }

        fnKendoDropDownList({
            id : "dateMutilSearchType",
            tagId : "dateMutilSearchType",
            data : dateSearchData
        });
        // 시작일
        fnKendoDatePicker({
            id: "dateMutilSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateMutilSearchStart",
            btnEndId: "dateMutilSearchEnd",
            defVal : fnGetDayAdd(fnGetToday(),minusDay),
            defType : defType,
            change : function(e) {
                fnDateValidation(e, "start", "dateMutilSearchStart", "dateMutilSearchEnd", minusDay);
                fnValidateCal(e)
            }
        });
        // 종료일
        fnKendoDatePicker({
            id: "dateMutilSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "dateMutilSearchStart",
            btnEndId: "dateMutilSearchEnd",
            defVal : fnGetToday(),
            defType : defType,
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnDateValidation(e, "end", "dateMutilSearchStart", "dateMutilSearchEnd");
                fnValidateCal(e)
            }
        });


        if (minusDay == -15) {
            $('[data-id="fnDateBtn4"]').mousedown();
        }

        $("#dateSearchStart, #dateSearchEnd").off().on('keydown', function(e) {
            if (e.keyCode == 13) {
                return false;
            }
        });


    },

    // dropDown
    getDropDownComm: function(searchData, id){
        // dropDown
        let dropDownComm =  fnKendoDropDownList(
            {
                id : id,
                data : searchData,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },

    //radio
    searchInterfaceData: function(){
        return [
            { "CODE" : ""	, "NAME":'전체' },
            { "CODE" : "Y"	, "NAME":'예' },
            { "CODE" : "N"	, "NAME":'아니오' }
        ];
    },

    //radio
    getRadioCommData : function({ optId, grpCd, useYn, attr1, attr2, attr3, chkVal, isAll }){

        var opt = {
            id          : optId,
            tagId       : optId,
            url         : "/admin/comn/getCodeList",
            params      : {"stCommonCodeMasterCode" : grpCd, "useYn" : useYn, "attr1" : attr1 || "", "attr2" : attr2 || "", "attr3" : attr3 || ""},
            chkVal      : chkVal ? chkVal : "",
            async       : false,
        };

        if(isAll) {
            opt['beforeData'] = [{ "CODE" : "ALL", "NAME" : "전체" }];
        }

        fnTagMkRadio(opt);
    },

    getCheckBoxLocalData: function({ id, data, change, chkVal, style }) {
        fnTagMkChkBox({
            id    : id,
            tagId : id,
            data  : data ? data : [],
            chkVal: chkVal ? chkVal : null,
            style : style ? style : "",
            change: change ? change : function() {},
        });

        // 전체가 체크 되었을 경우
        if( chkVal && chkVal === "ALL" ) {
            checkAll(id);
        }
    },

    /* 체크박스 공통코드 데이터 조회 */
    getCheckBoxCommCd: function({ optId, grpCd, useYn, attr1, attr2, attr3, chkVal }){

        fnTagMkChkBox({
            id          : optId,
            tagId       : optId,
            url         : "/admin/comn/getCodeList",
            params      : {"stCommonCodeMasterCode" : grpCd, "useYn" : useYn, "attr1" : attr1 || "", "attr2" : attr2 || "", "attr3" : attr3 || ""},
            beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }],
            chkVal      : chkVal ? chkVal : "",
            async       : false,
        });

        // 전체가 체크 되었을 경우
        if( chkVal && chkVal === "ALL" ) {
            checkAll(optId);
        }
    },
    /* 체크박스 특정 URL 데이터 조회 */
    getCheckBoxUrl: function({optId, url, params, textField, valueField, chkVal}){
        fnTagMkChkBox({
            id          : optId,
            tagId       : optId,
            url         : url,
            params      : params,
            textField   : textField ? textField : "NAME",
            valueField  : valueField ? valueField : "CODE",
            beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }],
            chkVal      : chkVal ? chkVal : "",
            async       : false,
        });

        // 전체가 체크 되었을 경우
        if( chkVal && chkVal === "ALL" ) {
            checkAll(optId);
        }
    },

    excelDownList: function(){

        fnAjax({
            url     : "/admin/policy/excel/getPolicyExcelTmpltList",
            params  : { "excelTemplateTp" : "EXCEL_TEMPLATE_TP.ORDER", "excelTemplateUseTp" : "DOWNLOAD"},
            method : "GET",
            async : false,
            success :
                function( data ){
                    var searchData = [];
                    $.each(data.rows, function (i, item) {

                        var obj = new Object();
                        obj.psExcelTemplateId = item.psExcelTemplateId;
                        obj.templateNm = item.templateNm;
                        searchData.push(obj);
                        $("#privacyIncludeYn_"+item.psExcelTemplateId).remove();
                        $("body").append("<input type='hidden' id='privacyIncludeYn_"+item.psExcelTemplateId+"' value='"+ item.privacyIncludeYn +"'/>")
                    });

                    // 엑셀 양식 유형 - 엑셀다운로드 양식을 위한 공통
                    fnKendoDropDownList({
                        id : "psExcelTemplateId",
                        //url : "/admin/policy/excel/getPolicyExcelTmpltList",
                        data : searchData,
                        tagId : "psExcelTemplateId",
                        params : { "excelTemplateTp" : "EXCEL_TEMPLATE_TP.ORDER", "excelTemplateUseTp" : "DOWNLOAD"},
                        textField : "templateNm",
                        valueField : "psExcelTemplateId",
                        blank : "선택",
                        async : false
                    });
                }
        });
    },

    // 단일조건
    searchDataSingle: function(){
        return [
            { "CODE" : "ORDER_ID"			, "NAME" : "주문번호" },
            { "CODE" : "OUTMALL_ID"			, "NAME" : "외부몰 주문번호" },
            { "CODE" : "COLLECTION_MALL_ID"	, "NAME" : "수집몰 주문번호" }
        ];
    },
    // 멀티조건
    searchDataMutil: function() {
        return [
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "ORDER_ID"				, "NAME": "주문번호"},
            {"CODE": "OUTMALL_ID"			, "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"	, "NAME": "수집몰 주문번호"},
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },
    // 주문상세리스트 전용
    searchDataDetailMutil: function() {
        return [
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "ORDER_ID"				, "NAME": "주문번호"},
            {"CODE": "TRACKING_NO"			, "NAME": "송장번호"},
            {"CODE": "OUTMALL_ID"			, "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"	, "NAME": "수집몰 주문번호"},
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },
    // 주문리스트 전용 > 고객명 검색(고객명 검색 : 주문자ID,주문자명,수취인명 )
    searchDataForCustomerMulti: function() {
        return [
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "RECEIVER_NAME"		, "NAME": "수취인명"}
        ];
    },
    // 주문리스트 전용 > 연락처 검색(연락처 검색 : 주문자연락처, 수취인연락처)
    searchDataForContactMulti: function() {
        return [
            {"CODE": "BUYER_HP"				, "NAME": "주문자연락처"},
            {"CODE": "RECEIVER_HP"			, "NAME": "수취인연락처"}
        ];
    },
    // 주문상세리스트 전용 > 고객검색
    searchDataDetailForBuyerMutil: function() {
        return [
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "RECEIVER_NAME"		, "NAME": "수취인명"},
            {"CODE": "BUYER_HP"		        , "NAME": "주문자연락처"},
            {"CODE": "RECEIVER_HP"          , "NAME": "수취인연락처"}
        ];
    },
    // 주문상세리스트 전용 > 주문검색
    searchDataDetailForOrderMutil: function() {
        return [
            {"CODE": "ORDER_ID"			    , "NAME": "주문번호"},
            {"CODE": "TRACKING_NO"		    , "NAME": "송장번호"},
            {"CODE": "OUTMALL_ID"		    , "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"   , "NAME": "수집몰 주문번호"}
        ];
    },
    
    // 주문상세리스트 전용 > 상품검색
    searchDataDetailForGoodsMutil: function() {
        return [
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },

    // 멀티조건 (결제완료, 출고예정, 배송중 주문 상세리스트)
    searchDataMutilOhter: function() {
        return [
            {"CODE": "ORDER_ID"				, "NAME": "주문번호"},
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "OUTMALL_ID"			, "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"	, "NAME": "수집몰 주문번호"},
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },

    // cs 관리 - 주문리스트 티조건
    csSearchDataMutil: function() {
        return [
            {"CODE": "ORDER_ID"				, "NAME": "주문번호"},
            {"CODE": "OUTMALL_ID"			, "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"	, "NAME": "수집몰 주문번호"},
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },
    // 주문리스트 전용 > 고객명 검색(고객명 검색 : 주문자ID,주문자명,수취인명 )
    csSearchDataForCustomerMulti: function() {
        return [
            {"CODE": "BUYER_ID"				, "NAME": "주문자ID"},
            {"CODE": "BUYER_NAME"			, "NAME": "주문자명"},
            {"CODE": "RECEIVER_NAME"		, "NAME": "수취인명"}
        ];
    },
    // 주문리스트 전용 > 연락처 검색(연락처 검색 : 주문자연락처, 수취인연락처)
    csSearchDataForContactMulti: function() {
        return [
            {"CODE": "BUYER_HP"				, "NAME": "주문자연락처"},
            {"CODE": "RECEIVER_HP"			, "NAME": "수취인연락처"}
        ];
    },    // cs 관리 - 주문상세리스트 티조건 전용 > 고객검색
    csSearchDataDetailForBuyerMutil: function() {
        return [
            {"CODE": "RECEIVER_NAME"		, "NAME": "수취인명"},
            {"CODE": "BUYER_HP"		        , "NAME": "주문자연락처"},
            {"CODE": "RECEIVER_HP"          , "NAME": "수취인연락처"}
        ];
    },
    // cs 관리 - 주문상세리스트 전용 > 주문검색
    csSearchDataDetailForOrderMutil: function() {
        return [
            {"CODE": "ORDER_ID"			    , "NAME": "주문번호"},
            {"CODE": "TRACKING_NO"		    , "NAME": "송장번호"},
            {"CODE": "OUTMALL_ID"		    , "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"   , "NAME": "수집몰 주문번호"}
        ];
    },

    // cs 관리 - 주문상세리스트 전용 > 상품검색
    csSearchDataDetailForGoodsMutil: function() {
        return [
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },

    // 멀티조건 (결제완료, 출고예정, 배송중 주문 상세리스트)
    csSearchDataMutilOhter: function() {
        return [
            {"CODE": "ORDER_ID"				, "NAME": "주문번호"},
            {"CODE": "OUTMALL_ID"			, "NAME": "외부몰 주문번호"},
            {"CODE": "COLLECTION_MALL_ID"	, "NAME": "수집몰 주문번호"},
            {"CODE": "GOODS_NAME"			, "NAME": "상품명"},
            {"CODE": "GOODS_ID"				, "NAME": "상품코드"},
            {"CODE": "ITEM_CODE"			, "NAME": "품목코드"},
            {"CODE": "ITEM_BARCODE"			, "NAME": "품목바코드"}
        ];
    },
    // 연동여부
    searchrecallTypeData: function() {
        return [
            { "CODE" : "RECALL_SUCCESS"	, "NAME":'연동 성공' },
            { "CODE" : "RECALL_FAIL"	, "NAME":'연동 실패' },
            { "CODE" : "RECALL_NONE"	, "NAME":'연동 안함' }
        ];
    }
}

var orderOptionUtil = {
    // 주문상태
    orderStatus: function(optId, searchGrp){
        return {optId: optId , url: "/admin/statusMgr/getSearchCode", params: {"searchGrp": searchGrp }, textField: "statusNm", valueField: "statusCd", chkVal: "ALL"}
    },
    // 판매처(외부몰) 그룹별 조회
    sellersGroup: function(optId, findSellersGroupCd){
        return {optId: optId , url: "/admin/outmall/sellers/getSellersGroupList", params: {"findSellersGroupCd": findSellersGroupCd }, textField: "NAME", valueField: "CODE", chkVal: "ALL"}
    },
    sellersGroup2: function(optId, findSellersGroupCd){
        return {optId: optId , url: "/admin/outmall/sellers/getSellersGroupList", params: {"findSellersGroupCd": findSellersGroupCd }, textField: "NAME", valueField: "CODE", chkVal: "ALL"}
    },
    // 배송유형
    delivType: {
        optId: "searchDelivType",
        grpCd: "GOODS_DELIVERY_TYPE",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 결제수단
    paymentMethod: {
        optId: "paymentMethodCode",
        grpCd: "PAY_TP",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 주문자유형
    buyerType: {
        optId: "buyerTypeCode",
        grpCd: "BUYER_TYPE",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 주문유형
    orderType: {
        optId: "orderTypeCode",
        grpCd: "SALE_TYPE",
        useYn: "Y",
        chkVal: "ALL",
        attr1: "ORDER"
    },
    // 유형
    agentType: {
        optId: "agentTypeCode",
        grpCd: "AGENT_TYPE",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 정기배송유형
    regularAgentType: {
        optId: "agentTypeCode",
        grpCd: "AGENT_TYPE",
        useYn: "Y",
        chkVal: "ALL",
        attr1: "AGENT"
    },
    // 정기배송신청기간
    regularTermType: {
        optId: "regularTerm",
        grpCd: "REGULAR_CYCLE_TERM_TYPE",
        useYn: "Y",
        chkVal: "ALL"
    },
    // 정기배송요일정보
    regularWeekCd: {
        optId: "weekCd",
        grpCd: "WEEK_CD",
        useYn: "Y",
        attr1: "R",
        isAll: false
    },
    // 정기배송신청상태
    regularStatus: {
        optId: "regularReqStatus",
        grpCd: "REGULAR_STATUS_CD",
        useYn: "Y",
        chkVal: "ALL"
    },
    // 정기배송상세신청상태
    regularStatusDetl: {
        optId: "regularReqDetailStatus",
        grpCd: "REGULAR_DETL_STATUS_CD",
        useYn: "Y",
        chkVal: "ALL"
    },
    // 미출상태
    missStockStatus: {
        optId: "missStockStatus",
        grpCd: "MISS_STOCK_STATUS",
        useYn: "Y",
        isAll: true,
        chkVal: "ALL"
    },
    // 미출사유
    missStockReason: {
        optId: "missStockReason",
        grpCd: "MISS_STOCK_REASON",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 반품사유
    returnOrderCause: {
        id: "returnOrderCause",
        data: [ { "CODE" : "ALL"		, "NAME":' 전체 ' },
            { "CODE" : "1"		, "NAME":' 클레임 요청일자 ' },
            { "CODE" : "2"		, "NAME":' 클레임 승인일자 ' } ],
        chkVal: "ALL",
    },

    //회수여부
    recallYN: {
        id : 'recallYN',
        tagId : 'recallYN',
        async : false,
        style : {},
        data  : [
            { "CODE" : ""   , "NAME" : "전체" },
            { "CODE" : "Y"	, "NAME":'회수' },
            { "CODE" : "N"	, "NAME":'회수 안함' }
        ],
        chkVal : ''

    },
    // CS환불승인상태
    csRefundApproveCd: {
    	optId 	: "csRefundApproveCd",
    	grpCd 	: "CS_REFUND_APPR_CD",
    	useYn 	: "Y",
    	chkVal	: "ALL",
    },
    // CS환불구분
    csRefundTp: {
        id 		: "csRefundTp",
        change 	: "",
        style 	: "",
        data  	: [
			{ "CODE" : "ALL"   								, "NAME" : "전체"			},
            { "CODE" : "CS_REFUND_TP.PAYMENT_PRICE_REFUND"	, "NAME" : "예치금 환불" 	},
            { "CODE" : "CS_REFUND_TP.POINT_PRICE_REFUND"	, "NAME" : "적립금 환불" 	}
		],
        chkVal 	: "ALL"
    },
    // PROD제외 주문생성 구분
    orderCreateTp	: {
        id 		: 'orderCreateTp',
        tagId 	: 'orderCreateTp',
        data  	: [
        	{ "CODE" : "MEMBER"			, "NAME" : "회원"		},
            { "CODE" : "NON_MEMBER"		, "NAME" : "비회원" 		},
            { "CODE" : "EXCEL_UPLOAD"	, "NAME" : "엑셀 업로드" } // 엑셀업로드 -> 임시주석처리
        ],
        chkVal : 'MEMBER'
    },
    // PROD 주문생성 구분
    prodOrderCreateTp	: {
        id 		: 'orderCreateTp',
        tagId 	: 'orderCreateTp',
        data  	: [
            { "CODE" : "MEMBER"			, "NAME" : "회원"		},
            { "CODE" : "NON_MEMBER"		, "NAME" : "비회원" 		}
        ],
        chkVal : 'MEMBER'
    },
    // 주소입력 구분
    addressCreateTp	: {
        id 		: 'recvAddressCreateTp',
        tagId 	: 'recvAddressCreateTp',
        data  	: [
        	{ "CODE" : "DEFAULT_ADDRESS", "NAME" : "회원 기본 주소"	},
            { "CODE" : "DIRECT"			, "NAME" : "직접입력" 		}
        ],
        chkVal : 'DEFAULT_ADDRESS'
    }
    ,
    // 주문생성 - 결제방식 (회원/비회원)
    orderPayTp1	: {
        id 		: 'payTp',
        tagId 	: 'payTp',
        data  	: [
        	{'CODE':'PAY_TP.DIRECT'	, 'NAME': '직접결제'},
            {'CODE':'PAY_TP.CARD'	, 'NAME': '신용카드'},
            {'CODE':'PAY_TP.VIRTUAL_BANK'	, 'NAME': '가상계좌'}
        ],
        chkVal : 'PAY_TP.DIRECT'
    },
    // 주문생성 - 결제방식 (엑셀 업로드)
    orderPayTp2	: {
        id 		: 'payTp',
        tagId 	: 'payTp',
        data  	: [
            {'CODE':'PAY_TP.CARD'			, 'NAME': '신용카드'},
        	{'CODE':'PAY_TP.VIRTUAL_BANK'	, 'NAME': '가상계좌'}
        ],
        chkVal : 'PAY_TP.CARD'
    },
    trackingNoYn	: {
        id 		: 'trackingNoYn',
        tagId 	: 'trackingNoYn',
        data  	: [
            { "CODE" : "Y", "NAME" : "송장없음"	},
        ],
        chkVal : ''
    },
    settleInfoYn	: {
        id 		: 'settleInfoYn',
        tagId 	: 'settleInfoYn',
        data  	: [
            { "CODE" : "Y", "NAME" : "정산일 없음"	},
        ],
        chkVal : ''
    },
    orderCntYn	: {
        id 		: 'orderCntYn',
        tagId 	: 'orderCntYn',
        data  	: [
            { "CODE" : "Y", "NAME" : "취소주문제외"	},
        ],
        chkVal : ''
    },
    selectGreenjuice	: {
        id 		: 'selectGreenjuice',
        tagId 	: 'selectGreenjuice',
        data  	: [
            { "CODE" : "Y", "NAME" : "녹즙, 내 맘대로 주문"	},
        ],
        chkVal : ''
    }
}

var orderSubmitUtil = {
    search: function(shopStockStatus) {
        if( !fnInputValidate() ) return;

        var grid = $("#orderGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);

        var selectConditionType = $('input[name=selectConditionType]:checked').val();

        if(shopStockStatus){
            data['shopStockStatus'] = 'shopStockStatus';
        }

        if ($("input[name='orderState']").length > 0 && $("input[name='claimState']").length > 0) {
            var orderStatusCnt = $("input[name='orderState']:checked").length;
            var claimStatusCnt = $("input[name='claimState']:checked").length;


            if (orderStatusCnt <= 0 && claimStatusCnt <= 0) {
                fnKendoMessage({message: "주문 상태를 선택해주세요."});
                return false;
            }
        }
        //
        if(selectConditionType == "singleSection" && $.trim($("#searchSingleType").val()) == ""){
            fnKendoMessage({ message : "단일 조건 항목을 선택해주세요." });
            return false;
        }
        if(selectConditionType == "singleSection" && $.trim($("#codeSearch").val()) == ""){
        	fnKendoMessage({ message : "검색어를 입력 해주세요." });
        	return false;
        }

        if($('input[name=selectConditionType]:checked').length > 0) {
            data['selectConditionType'] = selectConditionType;
        }


        if($("#checkBoxMutilDateYn").length){
            if($("#checkBoxMutilDateYn").is(":checked") == true){
                if($("#dateMutilSearchType").val() == $("#dateSearchType").val()){
                    fnKendoMessage({ message : "동일한 기간 조건은 조회가 불가능합니다." });
                    return false;
                }
            }
        }

            //data = {"categoryType":""}; //url 호출 위해 임시
        const _pageSize = grid && grid.dataSource ? grid.dataSource.pageSize() : PAGE_SIZE;

        var query = {
            page: 1,
            pageSize: _pageSize,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };

        gridDs.query(query);
    },
    clear: function(minusDay) {
        minusDay = stringUtil.getInt(minusDay, -30);
        var form = $("#searchForm");
        form.formClear(true);
        form.find("input[type='checkbox']").prop("checked", true);

        $("#dateSearchStart").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), minusDay) ); // 시작 기본날짜
        $("#dateSearchEnd").data("kendoDatePicker").value( fnGetToday() ); // 종료 기본날짜
        $("#sellersDetail").html("");
        $("#omSellersId").html("").hide();
        if (minusDay == -15){
            $('[data-id="fnDateBtn4"]').mousedown();
        } else {
            $('[data-id="fnDateBtnC"]').mousedown();
        }

    },
    shopOrderListclear: function() {
        var form = $("#searchForm");
        form.formClear(true);
        form.find("input[type='checkbox']").prop("checked", true);

        $("#dateSearchStart").data("kendoDatePicker").value( fnGetToday() ); // 시작 기본날짜
        $("#dateSearchEnd").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), 1) ); // 종료 기본날짜
        $("#sellersDetail").html("");
        $("#omSellersId").html("").hide();
        $(".date-controller button[fb-btn-active=true]").attr("fb-btn-active", false);

        if($('#urStoreId').val() != undefined && $('#urStoreId').val() != null) {
            $("#deliveryType").data("kendoDropDownList").value("");
            $("#deliveryType").data("kendoDropDownList").enable(false);
            $("#scheduleNo").data("kendoDropDownList").value("");
            $("#scheduleNo").data("kendoDropDownList").enable(false);
        }

    },
    orderDetailListClear: function() {
        var form = $("#searchForm");
        form.formClear(true);
        form.find("input[type='checkbox']").prop("checked", true);

        $("#dateSearchStart").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), -30) ); // 시작 기본날짜
        $("#dateSearchEnd").data("kendoDatePicker").value( fnGetToday() ); // 종료 기본날짜
        $("#sellersDetail").html("");
        $("#omSellersId").html("").hide();
        $('[data-id="fnDateBtnC"]').mousedown();
        searchCommonUtil.getDropDownUrl("warehouseId","warehouseId", "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");

        $("#dateMutilSearchStart").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), -30) ); // 시작 기본날짜
        $("#dateMutilSearchEnd").data("kendoDatePicker").value( fnGetToday() ); // 종료 기본날짜

        form.find("input[name='trackingNoYn']").prop("checked", false);
        form.find("input[name='settleInfoYn']").prop("checked", false);
        form.find("input[name='orderCntYn']").prop("checked", false);
        form.find("input[name='checkBoxMutilDateYn']").prop("checked", false);
        form.find("input[name='selectGreenjuice']").prop("checked", false);
    },
    excelExportDown: function(downUrl, btnObj) {
        var grid = $("#orderGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        if(gridDs._pristineData.length <= 0){
            fnKendoMessage({ message : "조회 후 다운로드 가능합니다." });
            return;
        }

        var psExcelTemplateId = stringUtil.getString($("input[name='psExcelTemplateId']").val(), "0");
        var privacyIncludeYn = stringUtil.getString($("#privacyIncludeYn_"+psExcelTemplateId).val(), "N");

        if (privacyIncludeYn == "Y") {
            fnKendoPopup({
                id: "excelDownloadReasonPopup",
                title: "엑셀 다운로드 사유",
                src: "#/excelDownloadReasonPopup",
                param: {excelDownloadType: "EXCEL_DOWN_TP.ORDER"},
                width: "700px",
                height: "300px",
                success: function (id, response) {
                    if (response == 'EXCEL_DOWN_TP.ORDER') {
                        var form = $("#searchForm");
                        var data = form.formSerialize(true);

                        if ($("#psExcelTemplateId").length > 0) {
                            if ($("#psExcelTemplateId").val().length < 1) {
                                fnKendoMessage({message: "다운로드 양식을 선택해주세요."});
                                return;
                            }
                            data['psExcelTemplateId'] = $("#psExcelTemplateId").val();
                        }

                        var selectConditionType = $('input[name=selectConditionType]:checked').val();

                        if ($('input[name=selectConditionType]:checked').length > 0) {
                            data['selectConditionType'] = selectConditionType;
                        }
                        fnExcelDownload(downUrl, data, btnObj);
                    }
                }
            });
        } else {
            var form = $("#searchForm");
            var data = form.formSerialize(true);

            if ($("#psExcelTemplateId").length > 0) {
                if ($("#psExcelTemplateId").val().length < 1) {
                    fnKendoMessage({message: "다운로드 양식을 선택해주세요."});
                    return;
                }
                data['psExcelTemplateId'] = $("#psExcelTemplateId").val();
            }

            var selectConditionType = $('input[name=selectConditionType]:checked').val();

            if ($('input[name=selectConditionType]:checked').length > 0) {
                data['selectConditionType'] = selectConditionType;
            }
            fnExcelDownload(downUrl, data, btnObj);
        }


    },
    changeStatus: function(url, btnMsg, statusCd, key) {
        let selectRows  = $("#orderGrid").find("input[name=rowCheckbox]:checked").closest("tr");

        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "상태 변경 할 주문 상세 내역을 선택해주세요." });
            return;
        }

        fnKendoMessage({
            type    : "confirm",
            message : btnMsg + " 일괄 변경하시겠습니까?",
            ok      : function(){


                let submitFlag = true;
                let params = {};
                params.detlIdList           = [];
                params.psShippingCompIdList = [];
                params.trackingNoList       = [];

                if(fnIsEmpty(key)) { key = 'odOrderDetlId'; }

                for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){

                    let dataItem = orderGrid.dataItem($(selectRows[i]));

                    params.detlIdList[i] = dataItem[key];
// var trNum = $(this).closest('tr').prevAll().length;
                    if ("DI" == statusCd) { // 배송중으로 변경시
                        let trNum               = $(selectRows[i]).prevAll().length;
                        let psShippingCompId    = $("select[name='psShippingCompId']:eq("+ trNum +")").val(); // $.trim(dataItem["psShippingCompId"]);
                        let trackingNo          = $("input[name='trackingNo']:eq("+ trNum +")").val(); // $.trim(dataItem["trackingNo"]);

                        if (psShippingCompId != "" && trackingNo != "") {
                            params.psShippingCompIdList[i]  = psShippingCompId;
                            params.trackingNoList[i]        = trackingNo;
                        } else {
                            submitFlag = false;
                            break;
                        }
                    }
                }

                if (submitFlag != true) {
                    fnKendoMessage({message: "택배사/송장번호를 확인해주세요."});
                    return false;
                }

                params.statusCd = statusCd;

                fnAjax({
                    url     : url,
                    params  : params,
                    success : function( data ){

                        var msg = "[총 건수] : " + data.totalCount + "<BR>" +
                            " [성공 건수] : " + data.successCount + "<BR>" +
                            " [실패 건수] : " + data.failCount + "<BR><BR>" ;

                        fnKendoMessage({ message : msg + "저장이 완료되었습니다.", ok : orderSubmitUtil.search() });
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "update"
                });
            },
            cancel  : function(){

            }
        });
    },
    totalCountView: function() {


        var grid = $("#orderGrid").data("kendoGrid");
        var gridDs = grid.dataSource;

        if(gridDs._pristineData.length <= 0){
            fnKendoMessage({ message : "조회 후 확인 가능합니다." });
            return;
        }

        var form = $("#searchForm");

        // form.formClear(false);
        var data = form.formSerialize(true);

        if($('input[name=selectConditionType]:checked').length > 0) {
            data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
        }

        //data = {"categoryType":""}; //url 호출 위해 임시

        var params = {
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };
        fnAjax({
            url     : '/admin/order/getOrderTotalCountInfo',
            params  : params,
            success :
                function( data ){
                    let message = "주문번호 : " + kendo.toString(data.rows.totalCnt, "n0") + "건 / 주문 상품 " + kendo.toString(data.rows.lineCnt, "n0") + "라인 / 주문 상품 " + kendo.toString(data.rows.orderGoodsCnt-data.rows.cancelGoodsCnt, "n0") + "개";
                    fnKendoMessage({ message : message });
                },
            error : function (xhr){
                fnKendoMessage({message : fnGetLangData({nullMsg :'' })});
            },
            isAction : 'batch'
        });
    },
    changeClaimStatusCCPopup: function() {
    	// 일괄 취소 팝업 오픈

        let selectRows = $("#orderGrid").find('.rowCheckbox:checked').closest('tr');
        if(selectRows.length == 0) {
            return false;
        }

        let dataItemList = new Array();
        for (let selectRow of selectRows){
            let dataItem = orderGrid.dataItem($(selectRow));
            dataItemList.push(dataItem);
        }

        fnKendoPopup({
             id     : "changeClaimStatusCCPopup",
             title  : "일괄 취소완료",
             src    : "#/changeClaimStatusCCPopup",
             param  : {dataItemList : dataItemList},
             width  : "800px",
            // height : "auto",
             success: function(id, data){

             }
        });
    },
    changeClaimStatusCCNext: function() {
    	// 일괄 취소 완료 다음 btn
    	var isNext = true;

    	$('.searchLClaimCtgryItem').each(function() {
    		if($(this).val() == ""){
    			fnKendoMessage({message : fnGetLangData({nullMsg :'클레임 사유를 선택해주세요.' })});
    			isNext = false;
    			return false;
    		}
    	});

    	$('.searchMClaimCtgryItem').each(function() {
    		if($(this).val() == ""){
    			fnKendoMessage({message : fnGetLangData({nullMsg :'클레임 사유를 선택해주세요.' })});
    			isNext = false;
    			return false;
    		}
    	});

    	$('.searchSClaimCtgryItem').each(function() {
    		if($(this).val() == ""){
    			fnKendoMessage({message : fnGetLangData({nullMsg :'클레임 사유를 선택해주세요.' })});
    			isNext = false;
    			return false;
    		}
    	});

    	if(isNext){
    		$("#checkBoxAll").prop("checked", false);
    		$("input[name=rowCheckbox]").prop("checked", false);
    		$("#claimForm").css("display","none");
    		$("#fnChange").css("display","none");
    		$("#fnNext").css("display","none");
    		$("#fnSave").css("display","");
    		$("select[name=searchLClaimCtgryId]").css("display","none");
    		$("select[name=searchMClaimCtgryId]").css("display","none");
    		$("select[name=searchSClaimCtgryId]").css("display","none");
    		$("input[name=claimPriceInput]").css("display","");
    	}

    },
    changeClaimStatusCCSave: function() {
	    // 일괄 취소완료 팝업 저장 btn
    	var data = new Object();
    	var claimDataList = [];

    	for(var i = 0; i < paramData.dataItemList.length ; i++){
    		var ifUnreleasedInfoId =  paramData.dataItemList[i].ifUnreleasedInfoId;
    		var claimData = new Object();
    		claimData = paramData.dataItemList[i];
    		claimData.searchLClaimCtgryId = $("#searchLClaimCtgryId_"+ifUnreleasedInfoId).val();
    		claimData.searchMClaimCtgryId = $("#searchMClaimCtgryId_"+ifUnreleasedInfoId).val();
    		claimData.searchSClaimCtgryId = $("#searchSClaimCtgryId_"+ifUnreleasedInfoId).val();
    		claimData.claimCnt = claimData.orderCnt;
    		claimData.ifUnreleasedInfoId = ifUnreleasedInfoId;

    		claimDataList.push(claimData);
    	}

    	data.claimDataList = claimDataList;

    	fnAjax({
            url: "/admin/order/putChangeClaimStatusCC",
            params: data,
            contentType: "application/json",
            success:
                function (data) {

                },
            isAction: 'insert'
        });

	}
    ,changeClaimStatusCCChange: function() {
	    // 일괄 취소완료 팝업 > 클레임 사유 일괄 변경 btn
    	var isChange = true;

    	var searchLClaimCtgryIdValue = $("#searchLClaimCtgryId_form option:selected").val();
    	var searchMClaimCtgryIdValue = $("#searchMClaimCtgryId_form option:selected").val();
    	var searchSClaimCtgryIdValue = $("#searchSClaimCtgryId_form option:selected").val();

    	if(searchLClaimCtgryIdValue == '' || searchMClaimCtgryIdValue == '' || searchSClaimCtgryIdValue == ''){
    		fnKendoMessage({message : fnGetLangData({nullMsg :'클레임 사유를 선택해주세요.' })});
    		isChange = false;
			return false;
    	}

    	if(isChange){
    		// 체크박스 선택된 값만 일괄변경
    		// 체크박스 전체 선택시
    		if($("#checkBoxAll").prop("checked")){
    			var searchMClaimCtgryForm = $("#searchMClaimCtgryId_form").clone();
    			$(".searchMClaimCtgryItem").html(searchMClaimCtgryForm.html());
    			var searchSClaimCtgryForm = $("#searchSClaimCtgryId_form").clone();
    			$(".searchSClaimCtgryItem").html(searchSClaimCtgryForm.html());

    			$(".searchLClaimCtgryId").val(searchLClaimCtgryIdValue).attr("selected","selected");
    			$(".searchMClaimCtgryId").val(searchMClaimCtgryIdValue).attr("selected","selected");
    			$(".searchSClaimCtgryId").val(searchSClaimCtgryIdValue).attr("selected","selected");
    		}else{

    			$('input:checkbox[name="rowCheckbox"]').each(function() {
    				if(this.checked){
    					var ifUnreleasedInfoId = $(this).data('ifunreleasedinfoid');

    					var searchMClaimCtgryForm = $("#searchMClaimCtgryId_form").clone();
    					$("#searchMClaimCtgryId_"+ifUnreleasedInfoId).html(searchMClaimCtgryForm.html());
    					var searchSClaimCtgryForm = $("#searchSClaimCtgryId_form").clone();
    					$("#searchSClaimCtgryId_"+ifUnreleasedInfoId).html(searchSClaimCtgryForm.html());

    					$("#searchLClaimCtgryId_"+ifUnreleasedInfoId).val(searchLClaimCtgryIdValue).attr("selected","selected");
    					$("#searchMClaimCtgryId_"+ifUnreleasedInfoId).val(searchMClaimCtgryIdValue).attr("selected","selected");
    					$("#searchSClaimCtgryId_"+ifUnreleasedInfoId).val(searchSClaimCtgryIdValue).attr("selected","selected");

    				}
    			});
    		}
    	}

	},
	changeClaimStatusECPopup: function() {
		// 일괄 재배송 팝업 오픈

        let selectRows = $("#orderGrid").find('.rowCheckbox:checked').closest('tr');
        if(selectRows.length == 0) {
            return false;
        }

        let dataItemList = new Array();
        for (let selectRow of selectRows){
            let dataItem = orderGrid.dataItem($(selectRow));
            dataItemList.push(dataItem);
        }

        fnKendoPopup({
             id     : "changeClaimStatusECPopup",
             title  : "일괄 재배송",
             src    : "#/changeClaimStatusECPopup",
             param  : {dataItemList : dataItemList},
             width  : "1800px",
            // height : "auto",
             success: function(id, data){

             }
        });
    },
    changeClaimStatusECChange: function() {
	    // 일괄 재배송 팝업 > 상품 일괄 변경 btn

    	var searchGoodsName = $("#searchGoodsInfo").val();
    	var searchGoodsId = $("#searchGoodsInfo").data('goodsId');

    	// 체크박스 선택된 값만 일괄변경
    	// 체크박스 전체 선택시
    	if($("#checkBoxAll").prop("checked")){

    		$('input[name="gridGoodsInfo"]').each(function() {
    			var ifUnreleasedInfoId = $(this).data('ifunreleasedinfoid');
    			var claimCnt = $(this).data('claimcnt');
    			var changeGoodsInfo = " ㄴ " + searchGoodsName + " / 수량 : " + claimCnt + "개";

    			$(this).css("display","");
    			$(this).val(changeGoodsInfo);
    			$(this).data('goodsId',searchGoodsId);
    		});

    	}else{

    		$('input:checkbox[name="rowCheckbox"]').each(function() {
    			if(this.checked){
    				var ifUnreleasedInfoId = $(this).data('ifunreleasedinfoid');
    				var claimCnt = $(this).data('claimcnt');
        			var changeGoodsInfo = " ㄴ " + searchGoodsName + " / 수량 : " + claimCnt + "개";

    				$("#gridGoodsInfo_"+ifUnreleasedInfoId).css("display","");
    				$("#gridGoodsInfo_"+ifUnreleasedInfoId).val(changeGoodsInfo);
    				$("#gridGoodsInfo_"+ifUnreleasedInfoId).data('goodsId',searchGoodsId);
    			}
    		});
    	}
	},
    changeClaimStatusECNext: function() {
    	// 일괄 재배송 다음 btn
    	var isNext = true;

    	$('input[name="gridGoodsInfo"]').each(function() {
    		if($(this).val() == ""){
    			fnKendoMessage({message : fnGetLangData({nullMsg :'재배송 상품을 선택해주세요' })});
    			isNext = false;
    			return false;
    		}
		});

    	if(isNext){
    		$("#checkBoxAll").prop("checked", false);
    		$("input[name=rowCheckbox]").prop("checked", false);
    		$("#claimForm").css("display","none");
    		$("#fnChange").css("display","none");
    		$("#fnNext").css("display","none");
    		$("#fnSave").css("display","");
    		$("button[name='gridSearchGoodsPopupButton']").css("display","none");
    	}

    },
    changeClaimStatusECSave: function() {
	    // 일괄 재배송 팝업 저장 btn
    	var data = new Object();
    	var claimDataList = [];

    	console.log("paramData.dataItemList :: " , paramData.dataItemList);

    	for(var i = 0; i < paramData.dataItemList.length ; i++){
    		var ifUnreleasedInfoId =  paramData.dataItemList[i].ifUnreleasedInfoId;
    		var claimData = new Object();
    		claimData = paramData.dataItemList[i];
    		claimData.changeIlGoodsId = $("#gridGoodsInfo_"+ifUnreleasedInfoId).data('goodsId');
    		claimData.claimCnt = claimData.orderCnt;
    		claimData.ifUnreleasedInfoId = $("#gridGoodsInfo_"+ifUnreleasedInfoId).data('ifunreleasedinfoid');

    		claimDataList.push(claimData);
    	}

    	data.claimDataList = claimDataList;

    	fnAjax({
            url: "/admin/order/putChangeClaimStatusEC",
            params: data,
            contentType: "application/json",
            success:
                function (data) {

                },
            isAction: 'insert'
        });

	}
}

/* Order functions */
//날짜 validate
function fnValidateCal(e) {
    const _sender = e.sender;
    const formatDate = fnFormatDate(_sender.element.val(), _sender.options.format)
    if(formatDate == "") {
        fnKendoMessage({message : "날짜를 확인해주세요.", ok : function() {
                $("#"+e.sender.dateView.options.id).focus();
            }});
    }
}

//등록 validate
function fnInputValidate() {

    if($("#dateSearchStart").val() == "" || $("#dateSearchEnd").val() == "") {
        fnKendoMessage({message : "날짜를 확인해주세요", ok : function() {
                $("#dateSearchStart").focus();
            }});
        return false;
    }

    //달력 조회 기간 체크
    if(fnGetDayMinus($("#dateSearchEnd").val(), 99) > $("#dateSearchStart").val() ) {
        fnKendoMessage({message : "검색 기간은 최대 3개월, 일수로는 99일을 초과 할 수 없습니다.", ok : function() {
                $("#dateSearchEnd").focus();
            }});
        return false;
    }
    //달력 시작일 종료일 체크
    if($("#dateSearchStart").val() > $("#dateSearchEnd").val()) {
        fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
                $("#dateSearchEnd").focus();
            }});
        return false;
    }


    return true;
}

// 체크 박스 전체 선택
function checkAll(id) {
    $(`#${id}`).find("input[type='checkbox']").prop("checked", true);
}

//판매처 상세
function onPurchaseTargetType(){
    //초기화
    $("#omSellersId").html("").hide();

    //$(this).val() 값에 따라 결과값 변경 필요
    //임시
    var omSellersId =  $(this).val();

    if(omSellersId != 'SELLERS_GROUP.MALL') { // 통합몰 제외
        orderSearchUtil.getCheckBoxUrl(orderOptionUtil.sellersGroup("omSellersId", omSellersId));
        $("#omSellersId").show();
    }
}

function onPurchaseTargetType2(){
    //초기화
    $("#omSellersId2").html("").hide();

    //$(this).val() 값에 따라 결과값 변경 필요
    //임시
    var omSellersId =  $(this).val();

    if(omSellersId == 'SELLERS_GROUP.DIRECT_MNG' || omSellersId == 'SELLERS_GROUP.VENDOR') { // 직관리 or 벤더인 경우
        orderSearchUtil.getCheckBoxUrl(orderOptionUtil.sellersGroup2("omSellersId2", omSellersId));
        $("#omSellersId2").show();
    }
}

function popOnPurchaseTargetType(){
    //초기화
    $("#popupOmSellersId").html("").hide();

    //$(this).val() 값에 따라 결과값 변경 필요
    //임시
    var omSellersId =  $(this).val();
    if(omSellersId == sellersGroupDirectMng || omSellersId == sellersGroupVendor) { // 직관리일경우
        orderSearchUtil.getCheckBoxUrl(orderOptionUtil.sellersGroup("popupOmSellersId", omSellersId));
        $("#popupOmSellersId").show();
    }
}

//리스트 내 기간검색 validation
function fnDateValidation(e, type, sId, eId, minusDay) {
    var self = e.sender.element;
    var sd = $('#' + sId).data('kendoDatePicker');
    var ed = $('#' + eId).data('kendoDatePicker');
    var sDate = sd.value();
    var eDate   = ed.value();
    var isValid = true;

    // sDate, eDate 둘중 하나라도 없으면 검사할 필요가 없다.
    if( sDate && eDate ) {
        isValid = fnValidateDatePicker(sDate, eDate);
    }

    if( !isValid ) {
        if( type === "start" ) {
            fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
                    $('#' + sId).focus();
                    $('#' + sId).val(fnGetDayAdd(fnGetToday(),minusDay));
                }});
        } else {
            fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
                    $('#' + eId).focus();
                    $('#' + eId).val(fnGetToday());
                }});
        }
        return;
    }
    fnClearDateController(eId);
}
