﻿/**-----------------------------------------------------------------------------
 * description 		 : 발주 유형관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.28		박영후          최초생성
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
			PG_ID  : 'poType',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

        //체크 박스 공통 이벤트
        fbCheckboxChange();

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave,  #fnClear, #fnChangeScheduleDate, #fnChangeMoveReqDate, #gnChangePoReqDate').kendoButton();
	}
	function fnSearch(){

		var data = $('#searchForm').formSerialize(true);
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

	function fnClear(){
		$('#searchForm').formClear(true);
		$("#searchPoType_0").prop("checked", true);

		$('input[name=searchScheduled]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

	}
	function fnNew(){
		$('#inputForm').formClear(true);
//		$("#poDateDiffByItemArea").hide();
		$("#poType_0").prop("checked", true);
//		$("#poPerItemYn").prop("checked", false);
		$("#poDate input[type=checkbox][name=poDate]").attr("disabled", false);

		$("#inputForm tr[modArea]").hide();
		$("#poReqAreaAll").hide();
		$("#poReqArea").hide();

		fnInitSelectBox();
		$("#erpPoTp").data("kendoDropDownList").enable(true);

		fnKendoInputPoup({height:"550px" ,width:"1200px", title:{ nullMsg :'발주유형' } });
	}

	function fnSave(){
		var url  = '/admin/item/potype/addItemPoType';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/item/potype/putItemPoType';
			cbId= 'update';
		}
		var data = $('#inputForm').formSerialize(true);

		var checkCount = $("input:checkbox[name^=check]:checked").length;

		if(checkCount == 0){
			fnKendoMessage({message:'발주일을 선택해 주세요.'});
			return
		}

		if($("#checkMon").prop("checked")==true){
			data.checkMon = "Y";
		}else {
			data.checkMon = "N";
		}

		if($("#checkTue").prop("checked")==true){
			data.checkTue = "Y";
		}else {
			data.checkTue = "N";
		}

		if($("#checkWed").prop("checked")==true){
			data.checkWed = "Y";
		}else {
			data.checkWed = "N";
		}

		if($("#checkThu").prop("checked")==true){
			data.checkThu = "Y";
		}else {
			data.checkThu = "N";
		}

		if($("#checkFri").prop("checked")==true){
			data.checkFri = "Y";
		}else {
			data.checkFri = "N";
		}

		if($("#checkSat").prop("checked")==true){
			data.checkSat = "Y";
		}else {
			data.checkSat = "N";
		}

		if($("#checkSun").prop("checked")==true){
			data.checkSun = "Y";
		}else {
			data.checkSun = "N";
		}

//		if($("#poPerItemYn").prop("checked")==true){
//			data.poPerItemYn = "Y";
//		}else {
//			data.poPerItemYn = "N";
//		}

		data.templateYn = "Y"; //발주유형 관라에서 생성한 발주유형은 모두 템플릿
		data.poPerItemYn = "N"; //발주유형 관라에서 생성한 발주유형은 품목별 상이가 아님.
		if( data.rtnValid ){

			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
		}
	}


	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/item/potype/getItemPoTypeList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 10
			}
			,navigatable: true
//			        ,height:550
			,columns   : [
				 {field : "rowNumber" , title : 'No'	, width:'100px', height : '50px',attributes:{ style:'text-align:center' }, template:"<span class='row-number'></span>"}
				,{field : "poTpNm" , title : '발주유형명'	, width:'150px',attributes:{ style:'text-align:left, text-decoration: underline;color:blue;' }}
				,{field : "compNm" , title : '공급업체'	, width:'150px',attributes:{ style:'text-align:left' }}
				,{field : "poTpName" , title : '발주유형구분<br>(ERP발주유형)'	, width:'150px',attributes:{ style:'text-align:left' }}
				,{field : "poDeadline" , title : '발주<br>마감시간'	, width:'150px',attributes:{ style:'text-align:left' }}
				,{title : "발주일",
				 columns: [
					 	{title : '구분'	, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
					 	,{title : '월'	, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '화'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '수'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '목'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '금'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '토'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
						,{title : '일'		, width:'100px', height : '50px',attributes:{ style:'text-align:center' }}
					]}

			],
			rowTemplate: function(dataItem){
					var template = "";

					if(dataItem.poPerItemYn == "N") {
						template += "<tr data-uid=" + dataItem.uid + ">" +
		      						"<td align='center'> " + dataItem.rowNumber +"</td>" +
		      						"<td align='center'><span style='text-decoration: underline;color:blue;'>" + dataItem.poTpNm +"</span></td>" +
		      						"<td align='center'>" + dataItem.compNm +"</td>" +
		      						"<td align='center'>" + dataItem.poTpName +" <br> (" + dataItem.erpPoTpName + ")</td>" +
		      						"<td align='center'>" + dataItem.poDeadline + "</td>" +
		      						"<td align='center'>" + dataItem.poType + "</td>" +
		      						"<td align='center'>" + dataItem.monValue + "</td>" +
		      						"<td align='center'>" + dataItem.tueValue + "</td>" +
		      						"<td align='center'>" + dataItem.wedValue + "</td>" +
		      						"<td align='center'>" + dataItem.thuValue + "</td>" +
		      						"<td align='center'>" + dataItem.friValue + "</td>" +
		      						"<td align='center'>" + dataItem.satValue + "</td>" +
		      						"<td align='center'>" + dataItem.sunValue + "</td>" +
		      						"</tr>";
					}else if(dataItem.poPerItemYn == "Y") {
						template += "<tr data-uid=" + dataItem.uid + ">" +
			  						"<td align='center'>" + dataItem.rowNumber +"</td>" +
			  						"<td align='center'><span style='text-decoration: underline;color:blue;'>" + dataItem.poTpNm +"</span></td>" +
			  						"<td align='center'>" + dataItem.compNm +"</td>" +
			  						"<td align='center'>" + dataItem.poTpName +" <br> (" + dataItem.erpPoTpName + ")</td>" +
			  						"<td align='center'>" + dataItem.poDeadline + "</td>" +
			  						"<td align='center'>" + dataItem.poType + "</td>" +
			  						"<td colspan='7' align='center'>품목별 상이</td>" +
			  						"</tr>";
					}
			      return template;
			}

			//,change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function() {

        	if(aGridDs._total > 0){
       		   //total count
               $('#countTotalSpan').text(aGridDs._data[0].rowNumber);
        	}else{
        	   //total count
        	   $('#countTotalSpan').text(aGridDs._total);
        	}

            // rowspan 로직 Start
            mergeGridRows(
                'aGrid' // div 로 지정한 그리드 ID
               , ['rowNumber', 'poTpNm', 'compNm', 'poTpName', 'poDeadline'] // 그리드에서 셀 머지할 컬럼들의 data-field 목록
               , ['rowNumber'] // group by 할 컬럼들의 data-field 목록
            );
            // rowspan 로직 End

        });

        $("#aGrid").on("click", "tbody>tr>td", function () {
			//fnGridClick();
			var index = $(this).index();

			if(index == 1) {
				var aMap = aGrid.dataItem(aGrid.select());

				fnAjax({
					url     : '/admin/item/potype/getItemPoType',
					params  : {ilPoTpId : aMap.ilPoTpId},
					success :
						function( data ){
							fnBizCallback("select",data);
						},
					isAction : 'select'
				});

			}


		});

		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true)
				$('INPUT[name=itemGridChk]').prop("checked",true);
			else
				$('INPUT[name=itemGridChk]').prop("checked",false);
		});
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		// 팝업 - 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'urSupplierId',
			//url : "/admin/ur/urCompany/getSupplierCompanyList",
			tagId : 'urSupplierId',
			//params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "선택",
			autoBind : false
		}).bind("change", function(e) {
				$("#erpPoTp").data("kendoDropDownList").enable(true);

				var value = e.sender.value();
				if(value == "2") {
					fnKendoDropDownList({
						id    : 'erpPoTp',
						url : "/admin/comn/getCodeList",
						params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y", "attr1":"OG"},
						textField :"NAME",
						valueField : "CODE",
						blank : "선택"
					});
				}else {
					fnKendoDropDownList({
						id    : 'erpPoTp',
						url : "/admin/comn/getCodeList",
						params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y", "attr1":"PF"},
						textField :"NAME",
						valueField : "CODE",
						blank : "선택"
					});
				}
//				if (value == 2 && $(':radio[name="poTp"]:checked').val() == "PO_TYPE.MOVING") {
//					$("#poDateDiffByItemArea").show();
//				}else {
//					$("#poDateDiffByItemArea").hide();
//					$("#poDate input[type=checkbox][name=poDate]").attr("disabled", false);
//					$("#poPerItemYn").attr("checked", false);
//				}
			});


		// 검색 - 공급업체 리스트 조회
		fnKendoDropDownList({
			id    : 'searchUrSupplierId',
			url : "/admin/ur/urCompany/getSupplierCompanyList",
			tagId : 'searchUrSupplierId',
			//params : {},
			textField :"supplierName",
			valueField : "urSupplierId",
			blank : "전체"
		}).dataSource.bind("requestEnd", function() {  // 팝업 공급업체 dropdown 데이타 설정
				$("#urSupplierId").data("kendoDropDownList").setDataSource(this);
			});


		// 검색 - 발주유형구분
		fnTagMkRadio({
			id    :  'searchPoType',
			tagId : 'searchPoType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "PO_TYPE", "useYn" :"Y"},
			beforeData : [
				{"CODE":"", "NAME":"전체"},
			],
			chkVal: "",
			async : false,
			style : {}
		});


		// 팝업 - 발주유형구분
		fnTagMkRadio({
			id    :  'poTp',
			tagId : 'poTp',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "PO_TYPE", "useYn" :"Y"},
			chkVal: "PO_TYPE.MOVING",
			async : false,
			style : {}
		});

		$('#poTp').bind("change", onPoTypeChange);


		// 팝업 - ERP 발주유형
		fnKendoDropDownList({
			id    : 'erpPoTp',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y", "attr1":"PF"},
			textField :"NAME",
			valueField : "CODE",
			blank : "선택",
//			cscdId : "urSupplierId",
		});

		$("#erpPoTp").data("kendoDropDownList").enable(false);

		// ERP 발주유형
		fnKendoDropDownList({
			id    : 'searchErpTotp',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			blank : "선택"
		});

		// 발주일
		fnKendoDropDownList({
       		id: "searchScType",
            data: [
            	{
            		CODE: "OR",
            		NAME: "포함",
            	},
            	{
            		CODE: "AND",
            		NAME: "동일조건",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });

		// 발주일 - 요일
		fnTagMkChkBox({
			id    : 'searchScheduled',
			data  : [
				{ "CODE" : 'ALL' , "NAME":"전체" },
				{ "CODE" : 'SUN' , "NAME":"일" },
				{ "CODE" : 'MON' , "NAME":"월" },
				{ "CODE" : 'TUE' , "NAME":"화" },
				{ "CODE" : 'WED' , "NAME":"수" },
				{ "CODE" : 'THU' , "NAME":"목" },
				{ "CODE" : 'FRI' , "NAME":"금" },
				{ "CODE" : 'SAT' , "NAME":"토" }
			],
			tagId : 'searchScheduled',
			chkVal: '',
			style : {}
		});

		// 최초 조회시에는 마스터 품목 유형 '전체' 체크
		$('input[name=searchScheduled]').each(function(idx, element) {
		    $(element).prop('checked', true);
		});

		// 팝업 - 발주일
		fnTagMkChkBox({
			id    : 'poDate',
			data  : [
				{ "CODE" : 'SUN' , "NAME":"일" },
				{ "CODE" : 'MON' , "NAME":"월" },
				{ "CODE" : 'TUE' , "NAME":"화" },
				{ "CODE" : 'WED' , "NAME":"수" },
				{ "CODE" : 'THU' , "NAME":"목" },
				{ "CODE" : 'FRI' , "NAME":"금" },
				{ "CODE" : 'SAT' , "NAME":"토" }
			],
			tagId : 'poDate',
			chkVal: '',
			style : {}
		});


		// 팝업 - 발주마감시간(시)
		var poDeadlineHourDatas = [];
		for (var i = 0; i < 24; i++) {
			var hour = i;
			if (i < 10)
				hour = "0" + i;
			poDeadlineHourDatas.push({"NAME":hour, "CODE":hour});
		}

		fnKendoDropDownList({
			id    : 'poDeadlineHour',
			tagId : 'poDeadlineHour',
			blank : "선택"
		}).setDataSource(poDeadlineHourDatas);

		// 팝업 - 발주마감시간(분)
		var poDeadlineMinDatas = [];
		for (var i = 0; i < 60; i++) {
			var min = i;
			if (i < 10)
				min = "0" + i;
			poDeadlineMinDatas.push({"NAME":min, "CODE":min});
		}

		fnKendoDropDownList({
			id    : 'poDeadlineMin',
			tagId : 'poDeadlineMin',
			blank : "선택"
		}).setDataSource(poDeadlineMinDatas);


		// 품목별 상이 checkbox 선택/해제 시
//		$("#poPerItemYn").on("change", function() {
//			if (this.checked)
//				$("#poDate input[type=checkbox][name=poDate]").attr("checked", false).attr("disabled", true);
//			else
//				$("#poDate input[type=checkbox][name=poDate]").attr("disabled", false);
//		});


		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnAjax({
			url   : "/admin/comn/getPostCodeList",
			params : {"stCommonCodeMasterCode" : "ARRIVED_SCHEDULE", "useYn" :"Y"},
			success :
				function( data ){
					fnScheduledInit(data);
				},
				isAction : 'batch'
		});

		//배송패턴 등록 설정요일 설정
		$('#ng-view').on("click", "#checkAllWeek" ,function(index){

			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==true){
				$('input[name^=check]').prop("checked",true);

				$("#scheduledMon").data("kendoDropDownList").enable(true);
				$("#scheduledTue").data("kendoDropDownList").enable(true);
				$("#scheduledWed").data("kendoDropDownList").enable(true);
				$("#scheduledThu").data("kendoDropDownList").enable(true);
				$("#scheduledFri").data("kendoDropDownList").enable(true);
				$("#scheduledSat").data("kendoDropDownList").enable(true);
				$("#scheduledSun").data("kendoDropDownList").enable(true);

				$("#moveReqMon").data("kendoDropDownList").enable(true);
				$("#moveReqTue").data("kendoDropDownList").enable(true);
				$("#moveReqWed").data("kendoDropDownList").enable(true);
				$("#moveReqThu").data("kendoDropDownList").enable(true);
				$("#moveReqFri").data("kendoDropDownList").enable(true);
				$("#moveReqSat").data("kendoDropDownList").enable(true);
				$("#moveReqSun").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqMon").data("kendoDropDownList").enable(true);
					$("#poReqTue").data("kendoDropDownList").enable(true);
					$("#poReqWed").data("kendoDropDownList").enable(true);
					$("#poReqThu").data("kendoDropDownList").enable(true);
					$("#poReqFri").data("kendoDropDownList").enable(true);
					$("#poReqSat").data("kendoDropDownList").enable(true);
					$("#poReqSun").data("kendoDropDownList").enable(true);
				}

			}else{
				$('input[name^=check]').prop("checked",false);

				$("#scheduledMon").data("kendoDropDownList").enable(false);
				$("#scheduledTue").data("kendoDropDownList").enable(false);
				$("#scheduledWed").data("kendoDropDownList").enable(false);
				$("#scheduledThu").data("kendoDropDownList").enable(false);
				$("#scheduledFri").data("kendoDropDownList").enable(false);
				$("#scheduledSat").data("kendoDropDownList").enable(false);
				$("#scheduledSun").data("kendoDropDownList").enable(false);

				$("#moveReqMon").data("kendoDropDownList").enable(false);
				$("#moveReqTue").data("kendoDropDownList").enable(false);
				$("#moveReqWed").data("kendoDropDownList").enable(false);
				$("#moveReqThu").data("kendoDropDownList").enable(false);
				$("#moveReqFri").data("kendoDropDownList").enable(false);
				$("#moveReqSat").data("kendoDropDownList").enable(false);
				$("#moveReqSun").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqMon").data("kendoDropDownList").enable(false);
					$("#poReqTue").data("kendoDropDownList").enable(false);
					$("#poReqWed").data("kendoDropDownList").enable(false);
					$("#poReqThu").data("kendoDropDownList").enable(false);
					$("#poReqFri").data("kendoDropDownList").enable(false);
					$("#poReqSat").data("kendoDropDownList").enable(false);
					$("#poReqSun").data("kendoDropDownList").enable(false);
				}


			}
		});

		$('#ng-view').on("click", "#checkMon" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkMon").prop("checked")==true){
				$("#scheduledMon").data("kendoDropDownList").enable(true);
				$("#moveReqMon").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqMon").data("kendoDropDownList").enable(true);
				}
			}else{
				$("#scheduledMon").data("kendoDropDownList").enable(false);
				$("#moveReqMon").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqMon").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkTue" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkTue").prop("checked")==true){
				$("#scheduledTue").data("kendoDropDownList").enable(true);
				$("#moveReqTue").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqTue").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledTue").data("kendoDropDownList").enable(false);
				$("#moveReqTue").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqTue").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkWed" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkWed").prop("checked")==true){
				$("#scheduledWed").data("kendoDropDownList").enable(true);
				$("#moveReqWed").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqWed").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledWed").data("kendoDropDownList").enable(false);
				$("#moveReqWed").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqWed").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkThu" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkThu").prop("checked")==true){
				$("#scheduledThu").data("kendoDropDownList").enable(true);
				$("#moveReqThu").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqThu").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledThu").data("kendoDropDownList").enable(false);
				$("#moveReqThu").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqThu").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkFri" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkFri").prop("checked")==true){
				$("#scheduledFri").data("kendoDropDownList").enable(true);
				$("#moveReqFri").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqFri").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledFri").data("kendoDropDownList").enable(false);
				$("#moveReqFri").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqFri").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkSat" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkSat").prop("checked")==true){
				$("#scheduledSat").data("kendoDropDownList").enable(true);
				$("#moveReqSat").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqSat").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledSat").data("kendoDropDownList").enable(false);
				$("#moveReqSat").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqSat").data("kendoDropDownList").enable(false);
				}

			}
		});

		$('#ng-view').on("click", "#checkSun" ,function(index){
			var poTypeVal = $(':radio[name="poTp"]:checked').val();

			if($("#checkAllWeek").prop("checked")==false && $("input[name^='check']:checked").length == 7){
				$("#checkAllWeek").prop("checked",true);
			}else if($("#checkAllWeek").prop("checked")==true &&  $("input[name^='check']:checked").length < 8){
				$("#checkAllWeek").prop("checked",false);
			}

			if($("#checkSun").prop("checked")==true){
				$("#scheduledSun").data("kendoDropDownList").enable(true);
				$("#moveReqSun").data("kendoDropDownList").enable(true);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqSun").data("kendoDropDownList").enable(true);
				}

			}else{
				$("#scheduledSun").data("kendoDropDownList").enable(false);
				$("#moveReqSun").data("kendoDropDownList").enable(false);

				if(poTypeVal == "PO_TYPE.PRODUCTION") {
					$("#poReqSun").data("kendoDropDownList").enable(false);
				}

			}
		});

	}

	function fnScheduledInit(codeData) {
		fnKendoDropDownList({
			id    : "scheduleAll",
			data  : codeData.rows,
			tagId : 'scheduleAll',
			blank : "선택해주세요"

		});


		fnKendoDropDownList({
			id    : "scheduledMon",
			data  : codeData.rows,
			tagId : 'scheduledMon'

		});

		fnKendoDropDownList({
			id    : "scheduledTue",
			data  : codeData.rows,
			tagId : 'scheduledTue'
		});

		fnKendoDropDownList({
			id    : "scheduledWed",
			data  : codeData.rows,
			tagId : 'scheduledWed'
		});

		fnKendoDropDownList({
			id    : "scheduledThu",
			data  : codeData.rows,
			tagId : 'scheduledThu'
		});

		fnKendoDropDownList({
			id    : "scheduledFri",
			data  : codeData.rows,
			tagId : 'scheduledFri'
		});

		fnKendoDropDownList({
			id    : "scheduledSat",
			data  : codeData.rows,
			tagId : 'scheduledSat'
		});

		fnKendoDropDownList({
			id    : "scheduledSun",
			data  : codeData.rows,
			tagId : 'scheduledSun'
		});


		fnKendoDropDownList({
			id    : "moveReqAll",
			data  : codeData.rows,
			tagId : 'moveReqAll',
			blank : "선택해주세요"

		});

		fnKendoDropDownList({
			id    : "moveReqMon",
			data  : codeData.rows,
			tagId : 'moveReqMon',

		});

		fnKendoDropDownList({
			id    : "moveReqTue",
			data  : codeData.rows,
			tagId : 'moveReqTue'
		});

		fnKendoDropDownList({
			id    : "moveReqWed",
			data  : codeData.rows,
			tagId : 'moveReqWed'
		});

		fnKendoDropDownList({
			id    : "moveReqThu",
			data  : codeData.rows,
			tagId : 'moveReqThu'
		});

		fnKendoDropDownList({
			id    : "moveReqFri",
			data  : codeData.rows,
			tagId : 'moveReqFri'
		});

		fnKendoDropDownList({
			id    : "moveReqSat",
			data  : codeData.rows,
			tagId : 'moveReqSat'
		});

		fnKendoDropDownList({
			id    : "moveReqSun",
			data  : codeData.rows,
			tagId : 'moveReqSun'
		});

		fnKendoDropDownList({
			id    : "poReqAll",
			data  : codeData.rows,
			tagId : 'poReqAll',
			blank : "선택해주세요"

		});

		fnKendoDropDownList({
			id    : "poReqMon",
			data  : codeData.rows,
			tagId : 'poReqMon',

		});

		fnKendoDropDownList({
			id    : "poReqTue",
			data  : codeData.rows,
			tagId : 'poReqTue'
		});

		fnKendoDropDownList({
			id    : "poReqWed",
			data  : codeData.rows,
			tagId : 'poReqWed'
		});

		fnKendoDropDownList({
			id    : "poReqThu",
			data  : codeData.rows,
			tagId : 'poReqThu'
		});

		fnKendoDropDownList({
			id    : "poReqFri",
			data  : codeData.rows,
			tagId : 'poReqFri'
		});

		fnKendoDropDownList({
			id    : "poReqSat",
			data  :   codeData.rows,
			tagId : 'poReqSat'
		});

		fnKendoDropDownList({
			id    : "poReqSun",
			data  : codeData.rows,
			tagId : 'poReqSun'
		});

		fnInitSelectBox();

	};

	function onPoTypeChange(e) {

		if(e.target.value == "PO_TYPE.PRODUCTION") {

			$("#poReqAreaAll").show();
			$("#poReqArea").show();
			$("#poDateDiffByItemArea").hide();

			if( $("#checkMon").prop("checked") == true) {
				$("#poReqMon").data("kendoDropDownList").enable(true);
			}

			if( $("#checkTue").prop("checked") == true) {
				$("#poReqTue").data("kendoDropDownList").enable(true);
			}

			if( $("#checkWed").prop("checked") == true) {
				$("#poReqWed").data("kendoDropDownList").enable(true);
			}

			if( $("#checkThu").prop("checked") == true) {
				$("#poReqThu").data("kendoDropDownList").enable(true);
			}

			if( $("#checkFri").prop("checked") == true) {
				$("#poReqFri").data("kendoDropDownList").enable(true);
			}

			if( $("#checkSat").prop("checked") == true) {
				$("#poReqSat").data("kendoDropDownList").enable(true);
			}

			if( $("#checkSun").prop("checked") == true) {
				$("#poReqSun").data("kendoDropDownList").enable(true);
			}
		}else {

			if($('#urSupplierId').data('kendoDropDownList').value() == "2") {
				$("#poDateDiffByItemArea").show();
			}
			$("#poReqAreaAll").hide();
			$("#poReqArea").hide();
		}
	};

	// 입고예정일 일괄변경
	function fnChangeScheduleDate(){

		if($("#checkMon").prop("checked")==false &&
			$("#checkTue").prop("checked")==false &&
			$("#checkWed").prop("checked")==false &&
			$("#checkThu").prop("checked")==false &&
			$("#checkFri").prop("checked")==false &&
			$("#checkSat").prop("checked")==false &&
			$("#checkSun").prop("checked")==false
			){
			fnKendoMessage({message:'입고예정일 선택 후 변경바랍니다.'});
			return;
		}

		var scheduleDate = $('#scheduleAll').val();
		if($("#checkMon").prop("checked")==true){
			$('#scheduledMon').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkTue").prop("checked")==true){
			$('#scheduledTue').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkWed").prop("checked")==true){
			$('#scheduledWed').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkThu").prop("checked")==true){
			$('#scheduledThu').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkFri").prop("checked")==true){
			$('#scheduledFri').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkSat").prop("checked")==true){
			$('#scheduledSat').data('kendoDropDownList').value(scheduleDate);
		}
		if($("#checkSun").prop("checked")==true){
			$('#scheduledSun').data('kendoDropDownList').value(scheduleDate);
		}
	}

	// 이동요청일 일괄변경
	function fnChangeMoveReqDate(){

		if($("#checkMon").prop("checked")==false &&
				$("#checkTue").prop("checked")==false &&
				$("#checkWed").prop("checked")==false &&
				$("#checkThu").prop("checked")==false &&
				$("#checkFri").prop("checked")==false &&
				$("#checkSat").prop("checked")==false &&
				$("#checkSun").prop("checked")==false
				){
				fnKendoMessage({message:'이동요청일  선택 후 변경바랍니다.'});
				return;
			}

		var moveReqDate = $('#moveReqAll').val();

		if($("#checkMon").prop("checked")==true){
			$('#moveReqMon').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkTue").prop("checked")==true){
			$('#moveReqTue').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkWed").prop("checked")==true){
			$('#moveReqWed').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkThu").prop("checked")==true){
			$('#moveReqThu').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkFri").prop("checked")==true){
			$('#moveReqFri').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkSat").prop("checked")==true){
			$('#moveReqSat').data('kendoDropDownList').value(moveReqDate);
		}
		if($("#checkSun").prop("checked")==true){
			$('#moveReqSun').data('kendoDropDownList').value(moveReqDate);
		}
	}

	// PO요청일 일괄변경
	function gnChangePoReqDate(){

		if($("#checkMon").prop("checked")==false &&
				$("#checkTue").prop("checked")==false &&
				$("#checkWed").prop("checked")==false &&
				$("#checkThu").prop("checked")==false &&
				$("#checkFri").prop("checked")==false &&
				$("#checkSat").prop("checked")==false &&
				$("#checkSun").prop("checked")==false
				){
				fnKendoMessage({message:'PO요청일 선택 후 변경바랍니다.'});
				return;
			}

		var poReqAll = $('#poReqAll').val();

		if($("#checkMon").prop("checked")==true){
			$('#poReqMon').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkTue").prop("checked")==true){
			$('#poReqTue').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkWed").prop("checked")==true){
			$('#poReqWed').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkThu").prop("checked")==true){
			$('#poReqThu').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkFri").prop("checked")==true){
			$('#poReqFri').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkSat").prop("checked")==true){
			$('#poReqSat').data('kendoDropDownList').value(poReqAll);
		}
		if($("#checkSun").prop("checked")==true){
			$('#poReqSun').data('kendoDropDownList').value(poReqAll);
		}
	}



	function fnInitSelectBox(){
		$("#scheduledMon").data("kendoDropDownList").enable(false);
		$("#scheduledTue").data("kendoDropDownList").enable(false);
		$("#scheduledWed").data("kendoDropDownList").enable(false);
		$("#scheduledThu").data("kendoDropDownList").enable(false);
		$("#scheduledFri").data("kendoDropDownList").enable(false);
		$("#scheduledSat").data("kendoDropDownList").enable(false);
		$("#scheduledSun").data("kendoDropDownList").enable(false);

		$("#moveReqMon").data("kendoDropDownList").enable(false);
		$("#moveReqTue").data("kendoDropDownList").enable(false);
		$("#moveReqWed").data("kendoDropDownList").enable(false);
		$("#moveReqThu").data("kendoDropDownList").enable(false);
		$("#moveReqFri").data("kendoDropDownList").enable(false);
		$("#moveReqSat").data("kendoDropDownList").enable(false);
		$("#moveReqSun").data("kendoDropDownList").enable(false);

		$("#poReqMon").data("kendoDropDownList").enable(false);
		$("#poReqTue").data("kendoDropDownList").enable(false);
		$("#poReqWed").data("kendoDropDownList").enable(false);
		$("#poReqThu").data("kendoDropDownList").enable(false);
		$("#poReqFri").data("kendoDropDownList").enable(false);
		$("#poReqSat").data("kendoDropDownList").enable(false);
		$("#poReqSun").data("kendoDropDownList").enable(false);

	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);

				fnInitSelectBox();

				var rowData = data.rows;

				$("#erpPoTp").data("kendoDropDownList").enable(true);

				if (rowData.supplierCd == "OG" ) {
					fnKendoDropDownList({
						id    : 'erpPoTp',
						url : "/admin/comn/getCodeList",
						params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y", "attr1":rowData.supplierCd},
						textField :"NAME",
						valueField : "CODE",
						blank : "선택"
					});
				}else {
					fnKendoDropDownList({
						id    : 'erpPoTp',
						url : "/admin/comn/getCodeList",
						params : {"stCommonCodeMasterCode" : "ERP_PO_TP", "useYn" :"Y", "attr1":rowData.supplierCd},
						textField :"NAME",
						valueField : "CODE",
						blank : "선택"
					});
				}

				$("#erpPoTp").data("kendoDropDownList").value(rowData.erpPoTp);

//				if (rowData.urSupplierId == 2 && rowData.poTp == "PO_TYPE.MOVING") {
//					$("#poDateDiffByItemArea").show();
//					if (rowData.poPerItemYn == "Y") {
//						$("#poPerItemYn").prop("checked", true);
//						$("#poDate input[type=checkbox][name=poDate]").attr("disabled", true);
//					} else {
//						$("#poPerItemYn").prop("checked", false);
//						$("#poDate input[type=checkbox][name=poDate]").attr("disabled", false);
//					}
//				} else {
//					$("#poDateDiffByItemArea").hide();
//					$("#poDate input[type=checkbox][name=poDate]").attr("disabled", false);
//				}

				if(rowData.poTp == "PO_TYPE.PRODUCTION") {
					$("#poReqAreaAll").show();
					$("#poReqArea").show();
				}else {
					$("#poReqAreaAll").hide();
					$("#poReqArea").hide();
				}

				if (rowData.checkSun == "Y") {
					$("#checkSun").prop("checked", true);

					$("#scheduledSun").data("kendoDropDownList").enable(true);
					$("#scheduledSun").data("kendoDropDownList").select(rowData.scheduledSun);

					$("#moveReqSun").data("kendoDropDownList").enable(true);
					$("#moveReqSun").data("kendoDropDownList").select(rowData.moveReqSun);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqSun").data("kendoDropDownList").enable(true);
						$("#poReqSun").data("kendoDropDownList").select(rowData.poReqSun);
					}
				}else {
					$("#checkSun").prop("checked", false);
				}

				if (rowData.checkMon == "Y") {
					$("#checkMon").prop("checked", true);

					$("#scheduledMon").data("kendoDropDownList").enable(true);
					$("#scheduledMon").data("kendoDropDownList").select(rowData.scheduledMon);

					$("#moveReqMon").data("kendoDropDownList").enable(true);
					$("#moveReqMon").data("kendoDropDownList").select(rowData.moveReqMon);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqMon").data("kendoDropDownList").enable(true);
						$("#poReqMon").data("kendoDropDownList").select(rowData.poReqMon);
					}

				}else {
					$("#checkMon").prop("checked", false);
				}

				if (rowData.checkTue == "Y") {
					$("#checkTue").prop("checked", true);

					$("#scheduledTue").data("kendoDropDownList").enable(true);
					$("#scheduledTue").data("kendoDropDownList").select(rowData.scheduledTue);

					$("#moveReqTue").data("kendoDropDownList").enable(true);
					$("#moveReqTue").data("kendoDropDownList").select(rowData.moveReqTue);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqTue").data("kendoDropDownList").enable(true);
						$("#poReqTue").data("kendoDropDownList").select(rowData.poReqTue);
					}
				}else {
					$("#checkTue").prop("checked", false);
				}

				if (rowData.checkWed == "Y") {
					$("#checkWed").prop("checked", true);

					$("#scheduledWed").data("kendoDropDownList").enable(true);
					$("#scheduledWed").data("kendoDropDownList").select(rowData.scheduledWed);

					$("#moveReqWed").data("kendoDropDownList").enable(true);
					$("#moveReqWed").data("kendoDropDownList").select(rowData.moveReqWed);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqWed").data("kendoDropDownList").enable(true);
						$("#poReqWed").data("kendoDropDownList").select(rowData.poReqWed);
					}
				}else {
					$("#checkWed").prop("checked", false);
				}

				if (rowData.checkThu == "Y") {
					$("#checkThu").prop("checked", true);

					$("#scheduledThu").data("kendoDropDownList").enable(true);
					$("#scheduledThu").data("kendoDropDownList").select(rowData.scheduledThu);

					$("#moveReqThu").data("kendoDropDownList").enable(true);
					$("#moveReqThu").data("kendoDropDownList").select(rowData.moveReqThu);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqThu").data("kendoDropDownList").enable(true);
						$("#poReqThu").data("kendoDropDownList").select(rowData.poReqThu);
					}

				}else {
					$("#checkThu").prop("checked", false);
				}

				if (rowData.checkFri == "Y") {
					$("#checkFri").prop("checked", true);

					$("#scheduledFri").data("kendoDropDownList").enable(true);
					$("#scheduledFri").data("kendoDropDownList").select(rowData.scheduledFri);

					$("#moveReqFri").data("kendoDropDownList").enable(true);
					$("#moveReqFri").data("kendoDropDownList").select(rowData.moveReqFri);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqFri").data("kendoDropDownList").enable(true);
						$("#poReqFri").data("kendoDropDownList").select(rowData.poReqFri);
					}

				}else {
					$("#checkFri").prop("checked", false);
				}

				if (rowData.checkSat == "Y") {
					$("#checkSat").prop("checked", true);

					$("#scheduledSat").data("kendoDropDownList").enable(true);
					$("#scheduledSat").data("kendoDropDownList").select(rowData.scheduledSat);

					$("#moveReqSat").data("kendoDropDownList").enable(true);
					$("#moveReqSat").data("kendoDropDownList").select(rowData.moveReqSat);

					if(rowData.poTp == "PO_TYPE.PRODUCTION") {
						$("#poReqSat").data("kendoDropDownList").enable(true);
						$("#poReqSat").data("kendoDropDownList").select(rowData.poReqSat);
					}
				}else {
					$("#checkSat").prop("checked", false);
				}

				var createDate = (data.rows.createDate != null && data.rows.createDate != '') ? data.rows.createDate : '-';
				var createNm = (data.rows.createNm != null && data.rows.createNm != '') ? data.rows.createNm : '-';
				var createLoginId = (data.rows.createLoginId != null && data.rows.createLoginId != '') ? data.rows.createLoginId : '-';
				var modifyStr = '';
				if (data.rows.modifyDate != null && data.rows.modifyDate != '') {
					var modifyDate = (data.rows.modifyDate != null && data.rows.modifyDate != '') ? data.rows.modifyDate : '-';
					var modifyNm = (data.rows.modifyNm != null && data.rows.modifyNm != '') ? data.rows.modifyNm : '-';
					var modifyLoginId = (data.rows.modifyLoginId != null && data.rows.modifyLoginId != '') ? data.rows.modifyLoginId : '-';
					modifyStr = modifyDate + ' / ' + modifyNm + '(' + modifyLoginId + ')';
				}
				$("#createDate").html(createDate + ' / ' + createNm + '(' + createLoginId + ')');
				$("#modifyDate").html(modifyStr);

				$("#inputForm tr[modArea]").show();

				fnKendoInputPoup({height:"600px" ,width:"1220px", title:{ nullMsg :'발주유형' } });
				break;
			case 'insert':
				fnSearch();
				$('#kendoPopup').data('kendoWindow').close();
				$('#searchForm').formClear(true);
				fnKendoMessage({message : '저장되었습니다.'});
				break;
//			case 'save':
//				fnKendoMessage({message : '저장되었습니다.'});
//				break;
			case 'update':
				aGridDs.query();
				fnKendoMessage({message : '수정되었습니다.'});
				fnClose();
				break;
			case 'delete':
				aGridDs.query();
				fnClose();
				fnKendoMessage({message : '삭제되었습니다.'});
				break;

		}  // switch(id){
	}  // function fnBizCallback( id, data ){

	/*
     * Kendo Grid 전용 rowSpan 메서드
     *
     * @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
     * @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
     * @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
     *
     */
    function mergeGridRows(gridId, mergeColumns, groupByColumns) {

        if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) { // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
            return;
        }

        var groupByColumnIndexArray = [];  // group by 할 컬럼들의 th 헤더 내 column index 목록
        var tdArray = [];  // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
        var groupNoArray = [];  // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

        var groupNo;  // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
        var beforeTr = null; // 이전 tr
        var beforeTd = null; // 이전 td
        var rowspan = null; // rowspan 할 개수, 1 인경우 rowspan 하지 않음

        var thRow = $('#' + gridId + ' > table > thead > tr')[0];  // 해당 그리드의 th 헤더 row

        // 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
        if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

            $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

                // groupByColumns => groupByColumnIndexArray 로 변환
                if( groupByColumns.includes( $(th).attr('data-field') ) ) {
                    groupByColumnIndexArray.push(thIndex);
                }

            });

        } // if 문 끝

        $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
            beforeTr = $(this).prev();  // 이전 tr

            if( beforeTr.length == 0 ) {  // 첫번째 tr 인 경우 : 이전 tr 없음

                groupNo = 0;  // 그룹번호는 0 부터 시작
                groupNoArray.push(groupNo); // 첫번째 tr 의 그룹번호 push

            } else {

                var sameGroupFlag = true;  // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

                for( var i in groupByColumnIndexArray ) {

                    var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index

                    // 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
                    if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
                        sameGroupFlag = false;
                    }

                }

                if( ! sameGroupFlag ) {  // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
                    groupNo++;
                }

                groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push

            }

        });  // tbody 내 tr 반복문 끝

        $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복

            if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
                return true;   // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
            }

            tdArray = [];  // 값 초기화
            beforeTd = null;
            rowspan = null;

            var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

            $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작

                var td = $(this).children().eq(colIdx);
                tdArray.push(td);

            });  // tbody 내 tr 반복문 끝

            for( var i in tdArray ) {  // 해당 컬럼의 td 배열 반복문 시작

                var td = tdArray[i];

                if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {

                    rowspan = $(beforeTd).attr("rowSpan");

                    if ( rowspan == null || rowspan == undefined ) {
                        $(beforeTd).attr("rowSpan", 1);
                        rowspan = $(beforeTd).attr("rowSpan");
                    }

                    rowspan = Number(rowspan) + 1;

                    $(beforeTd).attr("rowSpan",rowspan);
                    $(td).hide(); // .remove(); // do your action for the old cell here

                } else {

                    beforeTd = td;

                }

                beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set

            }  // 해당 컬럼의 td 배열 반복문 끝

        });  // thead 의 th 반복문 끝

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
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnChangeScheduleDate = function( ){  fnChangeScheduleDate();};

	$scope.fnChangeMoveReqDate = function( ){  fnChangeMoveReqDate();};

	$scope.gnChangePoReqDate = function( ){  gnChangePoReqDate();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
