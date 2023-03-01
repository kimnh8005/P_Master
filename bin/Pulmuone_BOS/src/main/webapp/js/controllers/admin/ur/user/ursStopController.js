﻿/**-----------------------------------------------------------------------------
 * description 		 : 휴면회원 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13		최봉석          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ursStop',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		//fnSearchControl();

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

		//탭 변경 이벤트
        fbTabChange();			// fbCommonController.js - fbTabChange 이벤트 호출

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){

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

        if ($('#startCreateDate').val() > $('#endCreateDate').val()) {
            return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'START_CREATED');
        }

		var data;
		data = $('#searchForm').formSerialize(true);
		$('#inputForm').formClear(false);
		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}
	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});

		return false;
	}
	function fnClear(){
		$('#searchForm').formClear(true);

		$(".set-btn-type6").attr("fb-btn-active" , false );
        //fnSearchControl();
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
	}
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/buyerStatus/getBuyerStopList'
			,pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			//,height:550
			,columns   : [
					{title : 'No.'		, width:'80px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
					,{ field:'userType'			,title : '회원유형'	, width:'60px'	,attributes:{ style:'text-align:center' }, template: "#=(employeeYn=='Y') ? '임직원' : '일반'#"}
					,{ field:'userName'			,title : '회원명'		, width:'60px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
					,{ field:'loginId'			,title : '회원ID'		, width:'90px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
					,{ field:'mobile'			,title : '휴대폰'		, width:'100px'	,attributes:{ style:'text-align:center' }
						, template: function(dataItem) {
							var mobile = kendo.htmlEncode(dataItem.mobile);
							return fnPhoneNumberHyphen(mobile);
		                }
					}
					,{ field:'mail'				,title : 'EMAIL'	, width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ field:'lastLoginDate'	,title : '최근방문일'	, width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ field:'reason'			,title : '정지사유'	, width:'90px'	,attributes:{ style:'text-align:center;white-space:nowrap;text-decoration: underline;' }}
					,{ field:'createDate'		,title : '정지일'		, width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ field:'urUserId'			,hidden: true}
					,{ field:'urBuyerStatusLogId',hidden: true}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function(){
			let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			$("#countTotalSpan").text(aGridDs._total);
		});

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});

		$("#aGrid").on("click", "tbody > tr > td", function () {
			var index = $(this).index();
			var map = aGrid.dataItem(aGrid.select());

			if($(this).closest('table').find('th').eq(index).text() == '회원명' || $(this).closest('table').find('th').eq(index).text() == '회원ID'){
				if(map != null){
					fnKendoPopup({
						id: 'buyerPopup',
						title: fnGetLangData({ nullMsg: '회원상세' }),
						param: { "urUserId": map.urUserId ,"type": "userStop" },
						src: '#/buyerPopup',
						width: '1200px',
						height: '700px',
						success: function(id, data) {
							//fnSearch();
						}
					});
				}
			}

			if($(this).closest('table').find('th').eq(index).text() == '정지사유'){
				if(map !=null){
					fnAjax({
						url     : '/admin/ur/buyerStatus/getBuyerStopLog',
						params  : {urBuyerStatusLogId : map.urBuyerStatusLogId},
						success :
							function( data ){
								$('#inputForm').bindingForm( {'rows':data.rows},'rows', true);
								fnKendoInputPoup({height:"310px" ,width:"450px",title:{nullMsg :'정지사유'} });
							},
						isAction : 'select'
					});
				}

			}
		});

	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		//$('#inputForm').bindingForm( {'rows':map},'rows', true);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
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

	  //전체회원 단일조건
		fnKendoDropDownList({
			id    : 'condiType',
			data  : [{ "CODE" : "userName" , "NAME":"회원명"},
				     { "CODE" : "loginId" , "NAME":"회원ID"}
					]
		});

		/*var condiTypeDrop =  fnKendoDropDownList({
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

		fnKendoDropDownList({
			id  : 'userType',
			data  : [
				{"CODE":"NORMAL"	,"NAME":"일반"},
				{"CODE":"EMPLOYEE"	,"NAME":"임직원"}
			],
			valueField : "CODE",
			textField :"NAME",
			blank : "전체"
		});

		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd'
			//,defVal : fnGetDayMinus(fnGetToday(),7)
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate'
			//,defVal : fnGetToday()
			,change: function(e){
        	   if ($('#startCreateDate').val() == "" ) {
                   return valueCheck("6495", "시작일을 선택해주세요.", 'endCreateDate');
               }
			}
		});

		var $this = $('.modify-form header');
		$this.on('click',function() {
			$this.next().toggleClass('active');
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

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#inputSHOP_NAME').focus();
	};

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				//$('#searchForm').bindingForm(data, 'rows', true);
				//break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				//fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"",nullMsg :'휴면해제 되었습니다.' }) ,ok :fnSearch});
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				break;

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
