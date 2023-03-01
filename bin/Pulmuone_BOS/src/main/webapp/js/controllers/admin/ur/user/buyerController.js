﻿﻿﻿
/**-----------------------------------------------------------------------------
 * system 			 : 전체회원
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.01		천혜현          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var openPopup = false;

var pageParam = fnGetPageParam();

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', { flag: 'true' });

        fnPageInfo({
            PG_ID: 'buyer',
            callback: fnUI
        });
    }

    function fnUI() {

        fnInitButton(); //Initialize Button  ---------------------------------

        fnInitGrid(true); //Initialize Grid ------------------------------------

        //fnSearchControl();

        fnInitOptionBox(); //Initialize Option Box ------------------------------------

        fnInitDropList();

        fbTabChange();			// fbCommonController.js - fbTabChange 이벤트 호출

        setCalledIllegalParam();

    }

    //--------------------------------- Button Start---------------------------------
    function fnInitButton() {
        $('#fnSearch, #fnClear,#fnPwdClear,#fnExcelExport').kendoButton();
    }

    function fnSearch() {

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
			if($("#condiValue").val() == ""){
				fnInitGrid();
				fnKendoMessage({
		            message: '검색어를 입력해 주세요',
		            ok: function () {
		                $('#condiValue').focus();
		            }
		        });
				return false;
			}
		}else{
			if($('#mobile').val() == "" && $('#mail').val() == "" && $('#searchUserType').val() == "" && $('#searchUserGroup').val() =="" && $('#searchUserStatus').val() =="" && $('#startLastLoginDate').val() == "" &&
					$('#endLastLoginDate').val() =="" && $('#startCreateDate').val()=="" && $('#endCreateDate').val() == "" && $("input:radio[name='smsYn']:checked").val() == "" && $("input:radio[name='mailYn']:checked").val() == "" &&
					$("input:radio[name='marketingYn']:checked").val() == "" && $("input:radio[name='pushYn']:checked").val() == ""){
				fnInitGrid();
				return false;
			}
		}

        /*if (($('#condiType').val() == "USER_NAME" || $('#condiType').val() == "LOGIN_ID") && $("#condiTextareaValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }

        if (($('#condiType').val() == "MOBILE" || $('#condiType').val() == "MAIL") && $("#condiValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }*/

        if ($('#startCreateDate').val() == "" && $('#endCreateDate').val() != "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'startCreateDate');
        }

        if ($('#startCreateDate').val() != "" && $('#endCreateDate').val() == "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
        }

        if ($('#startLastLoginDate').val() == "" && $('#endLastLoginDate').val() != "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'startLastLoginDate');
        }

        if ($('#startLastLoginDate').val() != "" && $('#endLastLoginDate').val() == "") {
            return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'endLastLoginDate');
        }

        if ($('#startCreateDate').val() > $('#endCreateDate').val()) {
            return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'START_CREATED');
        }

        if ($('#startLastLoginDate').val() > $('#endLastLoginDate').val()) {
            return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'ADAY');
        }

        $('#inputForm').formClear(true);

        var data = $('#searchForm').formSerialize(true);



        var query = {
            page: 1,
            pageSize: PAGE_SIZE,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };
        aGridDs.query(query);
    }

    function valueCheck(key, nullMsg, ID) {
        fnKendoMessage({
            message: fnGetLangData({ key: key, nullMsg: nullMsg }),
            ok: function focusValue() {
                $('#' + ID).focus();
            }
        });

        return false;
    }

    function fnClear() {
        $('#searchForm').formClear(true);

        setDefaultDatePicker();

        //fnSearchControl();

    }

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    function fnInitGrid(isReset) {

        //------------------------------- 왼쪽그리드 S -------------------------------

        aGridDs = fnGetPagingDataSource({
            url: '/admin/ur/buyer/getBuyerList',
            pageSize: PAGE_SIZE
        });

        aGridOpt = {
            dataSource: aGridDs,
            pageable: {
                buttonCount: 10,
                pageSizes: [20, 30, 50]
            }
            ,
            navigatable: true,
            scrollable: false,
            columns: [
            	{ field: 'rowNumber'		, title: 'No.'		, width: '50px'	, attributes: { style: 'text-align:center' }, template: function (dataItem){
            		return fnKendoGridPagenation(aGrid.dataSource,dataItem);
            	}}
                , { field: 'userType'		, title: '회원유형'	, width: '60px'	, attributes :{ style: 'text-align:center' }, template: "#=(employeeYn=='Y') ? '임직원' : '일반'#"}
                , { field: 'userName'		, title: '회원명'	, width: '60px'	, attributes: { style: 'text-align:center;text-decoration: underline;' } }
                , { field: 'loginId'		, title: '회원ID'	, width: '90px'	, attributes: { style: 'text-align:center;text-decoration: underline;' } }
                , { field: 'mobile'			, title: '휴대폰'	, width: '100px', attributes: { style: 'text-align:center' }
                	, template: function(dataItem) {
						var mobile = kendo.htmlEncode(dataItem.mobile);
						return fnPhoneNumberHyphen(mobile);
                    }
                }
                , { field: 'mail'			, title: 'EMAIL'	, width: '100px', attributes: { style: 'text-align:center' } }

                , { field: 'genderView'		, title: '성별'	    , width: '50px', attributes: { style: 'text-align:center' } }  // 성별
                , { field: 'bdayView'		, title: '생년월일(나이)'	, width: '100px', attributes: { style: 'text-align:center' } }  // 생년월일

                , { field: 'createDate'		, title: '가입일자'	, width: '80px'	, attributes: { style: 'text-align:center' } }
                , { field: 'lastLoginDate'	, title: '최근방문일자', width: '80px'	, attributes: { style: 'text-align:center' } }
                , { field: 'marketingYn'	, title: '마케팅<br/> 활용동의' , width: '60px', attributes: { style: 'text-align:center' },template: "#=(marketingYn=='Y') ? '동의' : '거부'#"}
                , { title: '수신여부' 		, columns: [{ field: 'smsYn',  title: 'SMS'	,   width: '40px', attributes: { style: 'text-align:center' },template: "#=(smsYn=='Y') ? '수신' : '거부'#"}
                                                    ,   { field: 'mailYn', title: 'EMAIL',  width: '45px', attributes: { style: 'text-align:center' },template: "#=(mailYn=='Y') ? '수신' : '거부'#"}
                                                    ,   { field: 'pushYn', title: 'PUSH',   width: '45px', attributes: { style: 'text-align:center' },template: "#=(pushYn=='Y') ? '수신' : '거부'#"}]}
                , { field: 'groupName'		, title: '회원등급'	, width: '80px'	, attributes: { style: 'text-align:center' } }
                , { field: 'status'			, title: '회원상태'	, width: '80px'	, attributes: { style: 'text-align:center' } }
                , { title: '관리'			, width: "100px" , attributes: { style: 'text-align:center;padding:5px 4px', class: 'forbiz-cell-readonly' }
                            ,command: [{ name: 'cEdit2', text: fnGetLangData({ key: "5664", nullMsg: 'PW재발송' }), imageClass: "k-i-custom", className: "f-grid-setting btn-white btn-s", iconClass: "k-icon", click: fnPwdClear
                    		, visible: function(dataItem) { return dataItem.status !="정지" }}]}
                , { field: 'urUserId'		, hidden: true}

            ]
        };
        aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();
        //------------------------------- 왼쪽그리드 E -------------------------------

        $('.k-pager-sizes select').data("kendoDropDownList").bind("select",function (e){
        	if($("input[name=selectConditionType]:checked").val() == "multiSection") {
        		e.preventDefault();
        		this.enable(false);
        		PAGE_SIZE = e.dataItem.value;
        		this.value(PAGE_SIZE);
        		this.enable(true);
        		fnSearch();
        	}
    	});

        aGridDs.bind("requestStart", function(e) {
        	if($("input[name=selectConditionType]:checked").val() == "singleSection") {
    			if($("#condiValue").val() == ""){
    				e.preventDefault();
    			}
    		}
        });

    	aGrid.bind("dataBound", function(){

    		let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

    		$("#aGrid tbody > tr .row-number").each(function(index){
    			$(this).html(rowNum);
    			rowNum--;
    		});

    		$('#countTotalSpan').text(aGridDs._total);
    	});

    	if(aGridDs._total == undefined){
    		$('#countTotalSpan').text(0);
    	}

    	$("#aGrid").on("click", "tbody>tr>td", function() {
    		var index = $(this).index();

    		if($(this).closest('table').find('th').eq(index).text() == '회원명' || $(this).closest('table').find('th').eq(index).text() == '회원ID'){
    			var map = aGrid.dataItem(aGrid.select());

    			if(!openPopup){

    				openPopup = true;

    				fnKendoPopup({
    					id: 'buyerPopup',
    					title: fnGetLangData({ nullMsg: '회원상세' }),
    					param: { "urUserId": map.urUserId },
    					src: '#/buyerPopup',
    					width: '90%',
    					height: '90%',
    					success: function(id, data) {
    						openPopup = false;
    					},
    					error : function(){
    						openPopup = false;
    					},
    				});
    			}

    		}
    	});

    }
    //-------------------------------  Grid End  -------------------------------

    //-------------------------------  Common Function start -------------------------------
    function fnInitOptionBox() {
    	$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

    	fnTagMkRadio({
	        id: 'selectConditionType',
	        tagId: 'selectConditionType',
	        chkVal: 'singleSection',
	        tab: true,
	        data: [{
	            CODE: "singleSection",
	            NAME: "단일조건 검색",
	            TAB_CONTENT_NAME: "singleSection"
	        }, {
	            CODE: "multiSection",
	            NAME: "복수조건 검색",
	            TAB_CONTENT_NAME: "multiSection"
	        }],
	    });
	    //[공통] 탭 변경 이벤트
	    fbTabChange();

//    	fnTagMkRadio({
//			id    : 'condiType',
//			data  : [
//				{ "CODE" : "userName" , "NAME":"회원명"},
//				{ "CODE" : "loginId" , "NAME":"회원ID"}
//			],
//			tagId : 'condiType',
//			chkVal : "userName"
//		});
		/*var condiTypeDrop = fnKendoDropDownList({
			id    : 'condiType',
			data  : [
				{"CODE":"USER_NAME"	,"NAME":"회원명"},
				{"CODE":"LOGIN_ID"	,"NAME":"회원ID"},
				{"CODE":"MOBILE"	,"NAME":"휴대폰"},
				{"CODE":"MAIL"		,"NAME":"EMAIL"}
			],
			textField :"NAME",
			valueField : "CODE",
			blank : "선택"
		});

		condiTypeDrop.bind('change', function(e) {
			fnSearchControl();
        });*/

        var proStatus = [
            { "CODE": "", "NAME": fnGetLangData({ key: "4247", nullMsg: '전체' }) },
            { "CODE": "Y", "NAME": fnGetLangData({ key: "5737", nullMsg: '수신' }) },
            { "CODE": "N", "NAME": fnGetLangData({ key: "475", nullMsg: '거부' }) }
        ];
        fnTagMkRadio({
            id: "smsYn",
            data: proStatus,
            tagId: "smsYn",
            chkVal: ""
        });
        fnTagMkRadio({
            id: "mailYn",
            data: proStatus,
            tagId: "mailYn",
            chkVal: ""
        });
        fnTagMkRadio({
            id: "marketingYn",
            data: [
                { "CODE": "", "NAME": fnGetLangData({ key: "4247", nullMsg: '전체' }) },
                { "CODE": "Y", "NAME": fnGetLangData({ key: "5737", nullMsg: '동의' }) },
                { "CODE": "N", "NAME": fnGetLangData({ key: "475", nullMsg: '거부' }) }
            ],
            tagId: "marketingYn",
            chkVal: ""
        });
        fnTagMkRadio({
            id: "pushYn",
            data: proStatus,
            tagId: "pushYn",
            chkVal: ""
        });

        fnKendoDatePicker({
            id: 'startLastLoginDate',
            format: 'yyyy-MM-dd'
            //,defVal : fnGetDayMinus(fnGetToday(),6)
            //,defType : 'oneWeek'
        });

        fnKendoDatePicker({
            id: 'endLastLoginDate',
            format: 'yyyy-MM-dd',
            btnStyle: true,
            btnStartId: 'startLastLoginDate',
            btnEndId: 'endLastLoginDate'
            //,defVal : fnGetToday()
            //,defType : 'oneWeek'
//            ,change: function(e){
//        	   if ($('#startLastLoginDate').val() == "" ) {
//                   return valueCheck("6495", "시작일을 선택해주세요.", 'endLastLoginDate');
//               }
//			}
        });


        fnKendoDatePicker({
            id: "startCreateDate",
            format: "yyyy-MM-dd",
            defVal: fnGetDayMinus(fnGetToday(),30),
            defType : 'oneMonth'
//            change : function(e) {
//                fnStartCalChange("createDateStart", "createDateEnd");
//            }
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "endCreateDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startCreateDate",
            btnEndId: "endCreateDate",
            defVal: fnGetToday(),
            defType : 'oneMonth',
        });



		fnTagMkChkBox({
			id    : 'putPasswordType'
			,data  : [{ "CODE" : "1" , "NAME":'SMS'}
					 ,{ "CODE" : "2" , "NAME":'EMAIL'}]
			,tagId : 'putPasswordType'
		});

    };

    function fnInitDropList(){

		//전체회원 단일조건
		fnKendoDropDownList({
			id    : 'condiType',
			data  : [{ "CODE" : "userName" , "NAME":"회원명"},
				     { "CODE" : "loginId" , "NAME":"회원ID"}
					]
		});

    	fnKendoDropDownList({
			id  : 'searchUserType',
			data  : [
				{"CODE":"NORMAL"	,"NAME":"일반"},
				{"CODE":"EMPLOYEE"	,"NAME":"임직원"}
			],
			valueField : "CODE",
			textField :"NAME",
			blank : "전체"
		});

    	fnKendoDropDownList({
            id  : "searchUserGroup",
            url : "/admin/comn/getUserGroupList",
            params: {},
            valueField : "groupId",
            textField :"groupName",
            blank : "전체"
        });

		fnKendoDropDownList({
			id  : 'searchUserStatus',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "BUYER_STATUS", "useYn" :"Y"},
			blank : "전체"
		});
    }

    // 검색제어
    function fnSearchControl(){
        let condiValueYn = $("#condiType").val() != "" ? true : false;

        fnSearchConditionTypeControl( $("#condiType").val() );
        //fnSearchControl( condiValueYn );
    };

    //검색구분 제어
    function fnSearchConditionTypeControl(conditionTypeValue){

        // 검색구분이 전체일 경우
        if(conditionTypeValue == ""){
            $("#condiValue, #condiTextareaValue").val("");
            $("#condiValue").css("display", "");
            $("#condiTextareaValue").css("display", "none");
            $("#condiValue, #condiTextareaValue").attr("disabled", true);
        } // 검색구분이 회원ID, 회원명 일 경우
        else if(conditionTypeValue == "USER_NAME" || conditionTypeValue == "LOGIN_ID"){
            $("#condiValue").val("");
            $("#condiValue").css("display", "none");
            $("#condiValue").attr("disabled", true);
            $("#condiTextareaValue").css("display", "");
            $("#condiTextareaValue").attr("disabled", false);
        } // 검색구분이 모바일, 이메일 일 경우
        else if(conditionTypeValue == "MOBILE" || conditionTypeValue == "MAIL"){
            $("#condiTextareaValue").val("");
            $("#condiValue").css("display", "");
            $("#condiValue").attr("disabled", false);
            $("#condiTextareaValue").css("display", "none");
            $("#condiTextareaValue").attr("disabled", true);
        }
    };

    // 검색조건 제어
    // buyer.html에 맞게 수정
    /*function fnSearchConditionsControl(disableYn){
        $("#userType").data("kendoDropDownList").readonly( disableYn );
        $("#userLevel").data("kendoDropDownList").readonly( disableYn );
        $("#joinDateStart").data("kendoDatePicker").enable( !disableYn );
        $("#joinDateEnd").data("kendoDatePicker").enable( !disableYn );
        $("#lastVisitDateStart").data("kendoDatePicker").enable( !disableYn );
        $("#lastVisitDateEnd").data("kendoDatePicker").enable( !disableYn );
        $(".set-btn-type6").attr("disabled" , disableYn );
        $("input:radio[name=deviceType]").attr("disabled", disableYn );
        $("input:radio[name=pushReception]").attr("disabled", disableYn );
    };*/

    function fnPwdClear(e) {
        e.preventDefault();
        var aMap = aGrid.dataItem($(e.currentTarget).closest('tr'));
        aMap.userType = 'BUYER';

		fnKendoPopup({
            id: 'pwClearPopup',
            title: fnGetLangData({nullMsg:'PW 재발송'}),
            param  : { "data" : aMap},
            src: '#/pwClearPopup',
            width: '450px',
            height: '280px',
            success: function(id, data) {
            }
        });
    }

    function fnExcelExport(){
    	fnKendoPopup({
             id     : "excelDownloadReasonPopup",
             title  : "엑셀 다운로드 사유",
             src    : "#/excelDownloadReasonPopup",
             param  : {excelDownloadType : "EXCEL_DOWN_TP.MALL_MEMBER"},
             width  : "700px",
             height : "300px",
             success: function(id, data){

				if(data == 'EXCEL_DOWN_TP.MALL_MEMBER'){
					var data = $('#searchForm').formSerialize(true);
					fnExcelDownload('/admin/ur/buyer/getBuyerListExportExcel', data);
				}

             }
		});
    }

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("#createDateTd").find('button[data-id="fnDateBtn5"]').attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#startCreateDate").val(fnGetDayMinus(today, 30));
		$("#endCreateDate").data("kendoDatePicker").value(today);
	}

	function setCalledIllegalParam(){

        if(pageParam != undefined){

            $("#condiType").data('kendoDropDownList').value(pageParam.condiType);
            $('#condiValue').val(pageParam.condiValue);
        }

	}

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch (id) {
            case 'select':
                //form data binding
                $('#inputForm').bindingForm(data, 'rows', true);
                break;
            case 'insert':
                if (data.rows == "DUP_DATA") {
                    fnKendoMessage({ message: fnGetLangData({ key: "370", nullMsg: '중복입니다.' })});
                } else {
                    aGridDs.insert(data.rows);
                    fnKendoMessage({ message: fnGetLangData({ key: "369", nullMsg: '입력되었습니다.' })});
                }
                break;
            case 'save':
                fnKendoMessage({ message: fnGetLangData({ key: "368", nullMsg: '저장되었습니다.' }) });
                break;
            case 'update':
                fnUpdateGrid(data, $("#aGrid"), "rows");
                fnKendoMessage({ message: fnGetLangData({ key: "367", nullMsg: '수정되었습니다.' }) });
                break;
            case 'delete':
                aGridDs.remove(data);
                fnNew();
                fnKendoMessage({ message: fnGetLangData({ key: "366", nullMsg: '삭제되었습니다.' }) });
                break;

        }
    }

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function() { fnSearch(); };
    /** Common Clear*/
    $scope.fnClear = function() { fnClear(); };
    /** Common Close*/
    $scope.fnClose = function() { fnClose(); };

    $scope.fnPopupButton = function(data) { fnPopupButton(data); };

    $scope.fnPwdClear = function() { fnPwdClear(); };

    $scope.fnExcelExport = function() { fnExcelExport(); };

    //------------------------------- Html 버튼 바인딩  End -------------------------------
    //
}); // document ready - END