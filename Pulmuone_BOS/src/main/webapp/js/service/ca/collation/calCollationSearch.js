/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 관련 공통 검색
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * **/
var calCollationSearchUtil = {

	/** 검색어 조건 */
    searchTypeKeyword: function(){
        fnKendoDropDownList(
            {
                id : "searchMultiType",
                data : calCollationOptionUtil.searchTypeData(),
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    searchTypeKeywordPgDetl: function(){
        fnKendoDropDownList(
            {
                id : "searchMultiType",
                data : calCollationOptionUtil.searchTypeDataPgDetl(),
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    searchTypeKeywordOutmallDetl: function(){
        fnKendoDropDownList(
            {
                id : "outmallSearchMultiType",
                data : calCollationOptionUtil.searchTypeDataOutmallDetl(),
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    dateSearchType: function(data){
        fnKendoDropDownList(
            {
                id : "dateSearchType",
                data : data,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    pgUploadGubun: function(data){
        fnKendoDropDownList(
            {
                id : "pgUploadGubun",
                data : data,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    findPgGubn: function(data){
        fnKendoDropDownList(
            {
                id : "findPgGubn",
                data : data,
                valueField : "CODE",
                textField : "NAME",
                blank : "전체"
            });
    },
    findCollectionMallType: function(data){
        fnKendoDropDownList(
            {
                id : "findCollectionMallType",
                data : data,
                valueField : "CODE",
                textField : "NAME",
                //blank : "선택해주세요."
            });
    },
    dateSearch: function(){

        // 시작일
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            defVal : fnGetDayAdd(fnGetToday(),-30),
            defType : 'oneMonth',
            change : function(e) {
            	fnStartCalChange("dateSearchStart", "dateSearchEnd");
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
            defType : 'oneMonth',
            minusCheck: true,
            nextDate: false,
            change : function(e) {
            	fnEndCalChange("dateSearchStart", "dateSearchEnd");
           		fnValidateCal(e)
            }
        });

    },
    getDropDownCommCd: function(optId, optText, optValue, optBlank, grpCd, useYn, attr1, attr2, attr3){
        fnKendoDropDownList({
            id          : optId,
            url         : "/admin/comn/getCodeList",
            params      : {"stCommonCodeMasterCode" : grpCd, "useYn" : useYn, "attr1" : attr1, "attr2" : attr2, "attr3" : attr3},
            textField   : optText,
            valueField  : optValue,
            blank       : optBlank
        });
    },
    getDropDownUrl: function(optId, tagId, optUrl, optText, optValue, optBlank, optCscdId, optCscdField){
        fnKendoDropDownList({
            id : optId,
            tagId : tagId,
            url : optUrl,
            textField : optText,
            valueField : optValue,
            blank : optBlank,
            async : false,
            cscdId: optCscdId ? optCscdId : undefined,
            cscdField: optCscdField ? optCscdField : undefined,
        });
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
    getCheckBoxCommUrl: function({optId, optText, optValue, optBlank, grpCd, useYn, attr1, attr2, attr3, chkVal}) {
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
    getRadioLocalData: function({ id, data, change, chkVal, style }) {
        fnTagMkRadio({
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
    getDropDownLocalData : function(id, chkVal, data ) {
        fnKendoDropDownList({
            id :        id,
            tagId :     id,
            value :     chkVal,
            data :      data
        });
    },
    getCalculateMonth: function(){

        let startAddMonth	= -3;
        let endAddMonth		= 0;
        // 정산 시작 년도
        calCollationSearchUtil.getDropDownLocalData("findStartYear", calCollationOptionUtil.optNowYear(startAddMonth), calCollationOptionUtil.optFindYear(startAddMonth));
        // 정산 시작 월
        calCollationSearchUtil.getDropDownLocalData("findStartMonth", calCollationOptionUtil.optNowMonth(startAddMonth), calCollationOptionUtil.optFindMonth(startAddMonth));

        // 정산 종료 년도
        calCollationSearchUtil.getDropDownLocalData("findEndYear", calCollationOptionUtil.optNowYear(endAddMonth), calCollationOptionUtil.optFindYear(startAddMonth));
        // 정산 종료 월
        calCollationSearchUtil.getDropDownLocalData("findEndMonth", calCollationOptionUtil.optNowMonth(endAddMonth), calCollationOptionUtil.optFindMonth(endAddMonth));
    },
    setFindKeyword: function(){
        $('#findKeyword').on('keydown',function(e){
            if (e.keyCode == 13) {
                if ($.trim($(this).val()) == ""){
                    fnKendoMessage({message: "검색어를 입력하세요."});
                } else {
                    employeeSubmitUtil.search();
                }
                return false;
            }
        });
    },
	fnInitFirstDateAndLastDate: function(startId, endId){
		var firstDay = fnGetToday("yyyy-MM-01");
		var dayArray = firstDay.split("-");
		var lastDay = new Date(dayArray[0], dayArray[1], 0).oFormat("yyyy-MM-dd");
		$("#"+startId).data("kendoDatePicker").value( firstDay ); // 시작 기본날짜
		$("#"+endId).data("kendoDatePicker").value( lastDay ); // 종료 기본날짜
		$("#"+startId).closest('.complex-condition').find(".date-controller button[fb-btn-active=true]").attr("fb-btn-active", false);
	}

}

var calCollationOptionUtil = {
    dateSearchGoodsData: function(){return [
        { "CODE" : "ORDER_DT", "NAME" : "주문일자" },
        { "CODE" : "IC_DT", "NAME" : "결제일자" },
    ]
    },
    searchTypeData: function(){return [
        { "CODE" : "OD_ORDER_ID", "NAME" : "주문번호" },
        { "CODE" : "ERP_ORDER_ID", "NAME" : "ERP 주문번호" },
        { "CODE" : "OUTMALL_ID", "NAME" : "외부몰 주문번호" },
        { "CODE" : "COLLECTION_MALL_ID", "NAME" : "수집몰 주문번호" },
        { "CODE" : "GOODS_NM", "NAME" : "상품명" },
        { "CODE" : "IL_GOODS_ID", "NAME" : "상품코드" },
        { "CODE" : "IL_ITEM_CD", "NAME" : "품목코드" },
        { "CODE" : "ITEM_BARCODE", "NAME" : "품목바코드" },
    ]
    },
    searchTypeDataPgDetl: function(){return [
        { "CODE" : "OD_ORDER_ID", "NAME" : "주문번호" },
        { "CODE" : "TID", "NAME" : "TID" },
    ]
    },
    searchTypeDataOutmallDetl: function(){return [
        { "CODE" : "OD_ORDER_ID", "NAME" : "주문번호" },
        { "CODE" : "IL_ITEM_CD", "NAME" : "품목코드" },
        { "CODE" : "ITEM_BATCODE", "NAME" : "품목바코드" },
        { "CODE" : "IL_GOODS_ID", "NAME" : "상품코드" },
        { "CODE" : "GOODS_NAME", "NAME" : "상품명" },
    ]
    },
    findCollectionMallTypeData: function(){return [
        { "CODE" : "ALL", "NAME" : "전체" },
        { "CODE" : "E", "NAME" : "이지어드민" },
        { "CODE" : "S", "NAME" : "샤방넷" },
    ]
    },
    dateSearchPgDetl: function(){return [
        { "CODE" : "ORDER_DT", "NAME" : "주문일자" },
        { "CODE" : "IC_DT", "NAME" : "결제일자" },
        { "CODE" : "CREATE_DT", "NAME" : "등록일자" },
    ]
    },
    // 통합몰 매출 대사 내역 - 구분
    optSalesGubun:{
        id :        "salesGubun",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "S", "NAME" : "매출" },
            { "CODE" : "R", "NAME" : "반품" }
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // PG 대사 상세내역 - 구분
    optSalesOrderGubun:{
        id :        "salesOrderGubun",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "S", "NAME" : "결제" },
            { "CODE" : "R", "NAME" : "환불" },
            { "CODE" : "A", "NAME" : "추가" },
            { "CODE" : "C", "NAME" : "CS환불" },
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // 주문자유형
    buyerType: {
        optId: "buyerTypeCode",
        grpCd: "BUYER_TYPE",
        useYn: "Y",
        chkVal: "ALL",
    },
    // 매칭여부
    optSalesMatchingType:{
        id : "findMatchingType",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "CAL_NOT", "NAME" : "대사내역 상이" },
            { "CODE" : "ERP_NOT", "NAME" : "통합ERP 미매칭" },
            { "CODE" : "BOS_NOT", "NAME" : "BOS 주문 미매칭" }
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // 매칭여부
    optPgDetlMatchingType:{
        id : "findMatchingType",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "PG_NOT", "NAME" : "PG 내역 미매칭" },
            { "CODE" : "BOS_NOT", "NAME" : "BOS 주문 내역 미매칭" },
            { "CODE" : "PRICE_NOT", "NAME" : "금액 상이" }
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // 외부몰 매칭여부
    outmallMatchingType:{
        id :        "findOutmallMatchingType",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "ORDER_NOT", "NAME" : "주문내역 상이" },
            { "CODE" : "OUTMALL_NOT", "NAME" : "외부몰 미매칭" },
            { "CODE" : "BOS_NOT", "NAME" : "BOS 주문 미매칭" },
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // 결제수단
    paymentMethod: {
        optId: "paymentMethodCode",
        grpCd: "PAY_TP",
        useYn: "Y",
        chkVal: "ALL",
    },
    // ERP I/F 여부
    optErpSendYn:{
        id :        "erpSendYn",
        data :  [
            { "CODE" : "ALL", "NAME" : "전체" },
            { "CODE" : "Y", "NAME" : "연동" },
            { "CODE" : "N", "NAME" : "미연동" }
        ],
        change :    "",
        chkVal :    "ALL",
        style :     ""
    },
    // 주문상태
    orderStatus: function(optId, searchGrp){
        return {optId: optId , url: "/admin/statusMgr/getSearchCode", params: {"searchGrp": searchGrp }, textField: "statusNm", valueField: "statusCd", chkVal: "ALL"}
    },
    optNowYear: function(addMonth){
        let thisDate    = new Date();
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let thisYear    = thisDate.getFullYear();

        return thisYear;
    },
    optNowMonth: function(addMonth){
        let thisDate    = new Date();
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let thisMonth   = (thisDate.getMonth()+1).zf(2);
        return thisMonth;
    },
    optFindYear: function(addMonth){
        let startYear   = 2021;

        let thisDate    = new Date();
        let thisYear    = thisDate.getFullYear();
        thisDate.setMonth(thisDate.getMonth() + addMonth);
        let checkYear    = thisDate.getFullYear();
        if (startYear > checkYear){
            startYear = checkYear;
        }

        let data = [];
        for (let i=startYear;i<=thisYear;i++){
            let item = new Object();
            item.CODE = i.zf(4);
            item.NAME = i.zf(4) + "년";
            data.push(item);
        }
        return data;
    },
    optFindMonth: function(addMonth){
        let data = [];
        for (let i=1;i<=12;i++){
            let item = new Object();
            item.CODE = i.zf(2);
            item.NAME = i.zf(2) + "월";
            data.push(item);
        }
        return data;
    },
    // 판매처(외부몰) 그룹별 조회
    sellersGroup: function(optId, findSellersGroupCd){
        return {optId: optId , url: "/admin/outmall/sellers/getSellersGroupList", params: {"findSellersGroupCd": findSellersGroupCd }, textField: "NAME", valueField: "CODE", chkVal: "ALL"}
    },
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

function fnInputValidate() {
    let findStartYear   = $.trim($("#findStartYear").val());
    let findStartMonth  = $.trim($("#findStartMonth").val());
    let findEndYear     = $.trim($("#findEndYear").val());
    let findEndMonth    = $.trim($("#findEndMonth").val());

    let startDt     = new Date(findStartYear,findStartMonth-1, 1);
    let endDt       = new Date(findEndYear,findEndMonth-1, 1);
    let interval    = endDt - startDt;
    let day         = 1000 * 60 * 60 * 24;
    let month       = day * 30;
    let diffMonth   = parseInt(interval/month);

	// 조회기간 체크
	if(diffMonth > 3) {
		fnKendoMessage({message : "검색 기간은 최대 3개월, 일수로는 99일을 초과 할 수 없습니다.", ok : function() {
			$("#findStartYear").focus();
		}});
		return false;
	}
	//달력 시작일 종료일 체크
	if(diffMonth < 0) {
		fnKendoMessage({message : "조회 시작월보다 과거로 설정할 수는 없습니다.", ok : function() {
			$("#findEndYear").focus();
		}});
		return false;
    }


    return true;
}

function fnInputValidateUse() {
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
    if(omSellersId == sellersGroupDirectMng) { // 직관리일경우
        calCollationSearchUtil.getCheckBoxUrl(calCollationOptionUtil.sellersGroup("omSellersId", omSellersId));
        $("#omSellersId").show();
    }
}


function fnGrantAuthEmployeeSearch(){ // 담당자 검색 팝업

    fnKendoPopup({
        id     : "grantAuthEmployeeSearchPopup",
        title  : "담당자 검색",
        src    : "#/grantAuthEmployeeSearchPopup",
        param  : {},
        width  : "700px",
        height : "600px",
        success: function( id, data ){

            if( data.employeeNumber != undefined ){
                $('#grantAuthEmployeeNumber').val(data.employeeNumber);
                $('#grantAuthEmployeeName').val(data.employeeName);
            }else{
                $('#grantAuthEmployeeNumber').val('');
                $('#grantAuthEmployeeName').val('');
            }
        }
    });
}