/**-----------------------------------------------------------------------------
 * description 		 : 엑셀양식관리 - 상품양식관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.25		박승현          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid, bGridDs, bGridOpt, bGrid, tmpltList, delYn;

$(document).ready(function() {
	//Initialize Page Call
	fnInitialize();

	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'policyExcelGoodsTmplt',
			callback : fnUI
		});
	};

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave, #fnClear, #fnGridAdd, #fnGridDel, #fnGridAddNull, #fnDataUp, #fnDataDown, #fnDelInit').kendoButton();
		//$('#fnDelInit').kendoButton({ enable: false });
		delYn = false;
	};

	function fnSearch(psExcelTemplateId){
		if( psExcelTemplateId.length > 0 ){
			var url = '/admin/policy/excel/getPolicyExcelTmpltInfo';
			var cbId = 'select';

			fnAjax({
				url		:	url,
				params 	:	{ 'psExcelTemplateId' : psExcelTemplateId },
				success	:
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : 'select'
			});
		}else{
			fnClear();
		};
	};

	function fnClear(){
		$('#searchForm').formClear(true);
		$('#aGrid').gridClear(true);
		aGridDs.read({"stCommonCodeMasterId" : "EXCEL_TEMPLATE_GOODS", "useYn" : "Y"});
		$('#bGrid').gridClear(true);
		fnInitButton();
	};

	function fnSave(){
		//11.26 SPEC OUT
		/*
		var $val = $('#startLine').val().replace(/[(\s*)]/g,"");

		if((new RegExp(/[^1-9]/gi)).test($val)){
			fnKendoMessage({message : '엑셀 시작열의 최소 값은 1 입니다', ok : fnFocus});
			return false;
		}
		*/
		var url  = '';
		var cbId = '';

		var listChk = $('#psExcelTemplateId').data('kendoDropDownList').value();
		var isAction="";
		if( listChk == null || listChk == ""  ){
			url = "/admin/policy/excel/addPolicyExcelTmplt",
			cbId = 'insert';
			isAction = 'insert';
		} else {
			url = "/admin/policy/excel/putPolicyExcelTmplt",
			cbId = 'update';
			isAction = 'update';
		}
		var data = $('#searchForm').formSerialize(true);
		var jsonData = $('#bGrid').data("kendoGrid").dataSource.data();
		var jsonDataAGrid = $('#aGrid').data("kendoGrid").dataSource.data();
		data["templateData"] =  kendo.stringify(jsonData);
		data["urCompanyId"] =  "1";
		data["excelTemplateTp"] =  "EXCEL_TEMPLATE_TP.GOODS";
		if(data["personalUseYn"] == "")	data["personalUseYn"] = "N";

		if( data["templateData"].length < 1 || data["templateData"] == "[]"){
    		fnKendoMessage({ message : "필수값을 입력해주세요"});
    		return false;
    	}

		// 입력 필수 항목 체크
		var aGridAttributeCnt = 0;
		var bGridAttributeCnt = 0;
	    for (var i = 0; i < jsonData.length; i++) {
	    	if(jsonData[i].ATTRIBUTE1 == 'Y') bGridAttributeCnt += 1;
	    }
	    for (var i = 0; i < jsonDataAGrid.length; i++) {
	    	if(jsonDataAGrid[i].attribute1 == 'Y') aGridAttributeCnt += 1;
	    }

	    if(aGridAttributeCnt != bGridAttributeCnt){
	    	fnKendoMessage({ message : "입력 필수 항목을 모두 포함해주세요."});
    		return false;
	    }

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : isAction
			});
		};
	};

	function fnFocus(){
		//11.26 SPEC OUT
		//$('#startLine').focus();
		//$('#startLine').val('');
	}

	function fnDelInit(){
		if(delYn)	fnKendoMessage({message:'삭제하시겠습니까?', type : "confirm" ,ok :fnDelApply  });
		else	fnClear();
    }

    function fnDelApply(){
		var url  = "/admin/policy/excel/delPolicyExcelTmplt";
		var cbId = 'delete';
		var data = $('#searchForm').formSerialize(true);
		data["psExcelTemplateId"] = $('#psExcelTemplateId').data('kendoDropDownList').value();

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : 'delete'
			});
		};
	};

	function fnBizCallback( id, data ){
		switch(id){
			case 'select':

				$('input:radio[name="excelTemplateUseTp"]:input[value="' + data.excelTemplateUseTp + '"]').prop("checked", true);
				$('#excelTemplateUseTp').data('pre', data.excelTemplateUseTp);
				$('input:checkbox[name=personalUseYn]').prop("checked",false);
				$('input:checkbox[name="personalUseYn"]:input[value="' + data.personalUseYn + '"]').prop("checked", true);
				$("#templateNm").val(data.templateNm);
				//11.26 SPEC OUT
				//$("#startLine").val(data.startLine);
				$('#searchForm').bindingForm(data, 'rows', true);
				var targetGrid = $("#bGrid").data("kendoGrid");
				var strData = data.templateData;
				strData = strData.replace(/&quot;/g,'\"');
				var jsonData = JSON.parse(strData);
				bGridDs = targetGrid.dataSource.data(jsonData);
				//$('#fnDel').kendoButton().data("kendoButton").enable(true);
				delYn = true;
				break;
			case 'insert':
				fnKendoMessage({message : '저장이 완료되었습니다 ' ,ok :fnClear()});
				tmpltList.dataSource.read();
				fnClear();
				break;
			case 'update':
				fnKendoMessage({message : '저장이 완료되었습니다 ' ,ok :fnClear()});
				tmpltList.dataSource.read();
				fnClear();
				break;
			case 'delete':
				//삭제 완료시 문구 없음
				//fnKendoMessage({message : '삭제되었습니다.' ,ok :fnClear()});
				tmpltList.dataSource.read();
				fnClear();
				break;
		};
	};

	function fnInitOptionBox(){
		fnTagMkChkBox({
	        id    : 'personalUseYn',
	        data  : [{ "CODE" : 'Y' , "NAME" : "개인 사용" }],
	        tagId : 'personalUseYn',
	        chkVal: 'N',
	        style : {}
	    });

		fnTagMkRadio({
			id    :  'excelTemplateUseTp',
			tagId : 'excelTemplateUseTp',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "EXCEL_TEMPLATE_USE_TP", "useYn" :"Y"},
			async : false,
			chkVal: "EXCEL_TEMPLATE_USE_TP.DOWNLOAD",
			style : {}
		});

		// 엑셀 양식 유형
        fnKendoDropDownList({
        	id : "psExcelTemplateId",
        	url : "/admin/policy/excel/getPolicyExcelTmpltList",
        	tagId : "psExcelTemplateId",
        	params : { "excelTemplateTp" : "EXCEL_TEMPLATE_TP.GOODS"},
        	textField : "templateNm",
            valueField : "psExcelTemplateId",
        	blank : "새 데이터 입력",
        	async : false
        });

		tmpltList = $('#psExcelTemplateId').data('kendoDropDownList');
		$('#psExcelTemplateId').on("change",  function(index){
			var param = tmpltList.value();
			fnSearch(param);
		});
		$('#excelTemplateUseTp').data('pre', $(this).val());
		$('#excelTemplateUseTp').on("change",  function(index){
			if(delYn){
				fnKendoMessage({message : '기등록된 양식은 사용타입 변경이 불가능합니다.' });
				var before_change = $(this).data('pre');
				$(this).val(before_change);
				$('input:radio[name="excelTemplateUseTp"]:input[value="' + before_change + '"]').prop("checked", true);
			}else{
				fnKendoMessage({
					//2020.11.13 메세지 미정의 된 상태. 임의 처리
					message: "타입변경 시 선택한 양식 항목정보가 초기화 됩니다, 변경하시겠습니까?",
					type: "confirm",
					ok: function (event) {
						$('#bGrid').gridClear(true);
						var useTp = $(':radio[name="excelTemplateUseTp"]:checked').val();
						if(useTp == "EXCEL_TEMPLATE_USE_TP.UPLOAD" || useTp == "EXCEL_TEMPLATE_USE_TP.ALL") fnGridAddMandatory();
						return true;
					},
					cancel: function (event) {
						return false;
					}
				})
			}
		});
		fnInputValidationForHangulAlphabetNumber("templateNm");
		//11.26 SPEC OUT
		//fnInputValidationByRegexp("startLine", /[^0-9]/g);
	};

	function fnInitGrid(){

		aGridDs = fnGetDataSource({
			url : '/admin/st/code/getCodeList'
		});

		aGridOpt = {
			dataSource : aGridDs
			,autoBind : false
			,height : 600
			,scrollable: true
			,selectable: "multiple, row"
			,columns : [{ field:'dictionaryMasterName'		,title : '항목명'	, width:'50%', attributes:{ style:'text-align:left' }}
						,{ field : "attribute1", title : "입력필수항목", width : '25%', attributes:{ style:'text-align:center' }
							, template : function(dataItem){
								if( dataItem.attribute1 == "Y" ){
									return "●";
								}else{
									return "";
								}
							}
						}
						,{ field : "attribute2", title : "입력가능", width : '25%', attributes:{ style:'text-align:center' }
							, template : function(dataItem){
								if( dataItem.attribute2 == "Y" ){
									return "●";
								}else{
									return "";
								}
							}
						}
						]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		bGridDs = fnGetEditDataSource({
			model_fields : {
					EXCEL_COL        : { editable: false , type: 'string', validation: { required: true  }}
				,CODE             : { editable: false , type: 'string', validation: { required: true  }}
				,NAME			  : { editable: true , type: 'string', validation: { required: false  }}
			}
		});
		bGridOpt = {
			dataSource : bGridDs
			,editable : false
			,height : 600
			,scrollable: true
			,columns   : [
							{ field:'EXCEL_COL', title : '열', width:'20%', attributes : {style:'text-align:center'}, defaultValue:'CODE'}
							,{ field:'NAME', title : '선택항목(Title)', width:'60%', attributes : {style:'text-align:left'}}
							,{ field : 'ATTRIBUTE1'		, title : '입력필수항목'		, width : '15%', attributes : { style:'text-align:center' }
								, template : function(dataItem){ return dataItem.ATTRIBUTE1 == "Y" ? "●": "";}
							}
						]
			,noRecordMsg : '선택항목이 없습니다'
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();
		bGrid.table.kendoSortable({
			filter: ">tbody >tr",
			hint: function hintElement(element) { // Customize the hint

				var table = bGrid.table.clone(), // Clone Grid's table
					wrapperWidth = bGrid.wrapper.width(), //get Grid's width
					wrapper = $("<div class='k-grid k-widget'></div>").width(wrapperWidth),
					hint;

				table.find("thead").remove(); // Remove Grid's header from the hint
				table.find("tbody").empty(); // Remove the existing rows from the hint
				table.wrap(wrapper); // Wrap the table
				table.append(element.clone().removeAttr("uid")); // Append the dragged element

				hint = table.parent(); // Get the wrapper

				return hint; // Return the hint element
			},
			cursor: "move",
			placeholder: function(element) {
				return element.clone().addClass("k-state-hover").css("opacity", 0.65);
			},
			container: "#bGrid tbody",
			change: function(e) {
				var oldIndex = e.oldIndex,
				newIndex = e.newIndex,
				dataItem = bGrid.dataSource.getByUid(e.item.data("uid"));

				bGrid.dataSource.remove(dataItem);
				bGrid.dataSource.insert(newIndex, dataItem);

				fnAllExcelColUpdate();
				bGrid.refresh();
			}
		});
		aGridDs.read({"stCommonCodeMasterId" : "EXCEL_TEMPLATE_GOODS", "useYn" : "Y"});
	};

	function fnGridAddMandatory(){
		var flag = true;
		var jObj = {};
		var targetGrid = $('#bGrid').data('kendoGrid');
		var allDataA = aGrid.dataSource.data();
		var allDataB = bGrid.dataSource.data();

		for (var i=0; i < allDataA.length; i++) {
			if(allDataA[i].attribute1 == "Y"){
				flag = true;
				for (var j=0; j < allDataB.length; j++) {
					if(allDataB[j].CODE == allDataA[i].stCommonCodeId){
						flag = false;
						break;
					}
				}
				if(flag){
					jObj['EXCEL_COL'] = chkExcelCol(null);
					jObj['CODE'] = allDataA[i].stCommonCodeId;
					jObj['NAME'] = allDataA[i].dictionaryMasterName;
					targetGrid.dataSource.add(jObj);
				}
			};
		};
	};
	function fnGridAdd(){
		for (var i=0; i < aGrid.select().length; i++) {
			fnGridAddItem(i);
		}
	}
	function fnGridAddItem(idx){
		var flag = true;
		var jObj = {};
		var targetGrid = $('#bGrid').data('kendoGrid');

		var data = aGrid.dataItem(aGrid.select()[idx]);

		var allDatas = bGrid.dataSource.data();
		for (var i=0; i < allDatas.length; i++) {
			if(allDatas[i].CODE == data.stCommonCodeId){
				flag = false;
			};
		};

		if( data == null || data == undefined){
			flag = false;
		}else{
			jObj['EXCEL_COL'] = chkExcelCol(null);
			jObj['CODE'] = data.stCommonCodeId;
			jObj['NAME'] = data.dictionaryMasterName;
			jObj['ATTRIBUTE1'] = data.attribute1;
		}

		if(flag) {
			targetGrid.dataSource.add(jObj);
		}
	};

	function fnGridAddNull(){
		var flag = true;
		var jObj = {};
		var data = bGrid.dataItem(bGrid.select());
		var targetGrid = $('#bGrid').data('kendoGrid');
		var idx = fnGridRowNumRtn(bGrid);
		jObj['CODE'] = '';
		jObj['NAME'] = '';

		if(flag) {
			if(idx > -1){
				jObj['EXCEL_COL'] = chkExcelCol(idx+1);
				targetGrid.dataSource.insert(idx+1, jObj);
			}else{
				jObj['EXCEL_COL'] = chkExcelCol(null);
				targetGrid.dataSource.add(jObj);
			}
		};

		if(idx > -1){
			fnAllExcelColUpdate();
			bGrid.refresh();
		}
	};

	function chkExcelCol(index){
		var idx;
		var idxDiv;
		var idxMod;
		var data;
		if(index==null||index==undefined){
			idx = $('#bGrid').data("kendoGrid").dataSource.data().length;
		}else{
			idx = index;
		};
		idxDiv = parseInt(idx/26);
		idxMod = idx%26;
		data = String.fromCharCode(idx+65);

		if(idxDiv > 0){
			data = String.fromCharCode((idxDiv-1)+65) + String.fromCharCode(idxMod+65);
		};
		return data;
	};

	function fnGridDel(){
		var flag = true;
		var data = bGrid.dataItem(bGrid.select());
		var targetGrid = $('#bGrid').data('kendoGrid');
		if( data == null || data == undefined){
			return;
		};

//		var useTp = $(':radio[name="excelTemplateUseTp"]:checked').val();
//		if(useTp == "EXCEL_TEMPLATE_USE_TP.UPLOAD" || useTp == "EXCEL_TEMPLATE_USE_TP.ALL"){
//			var allDatas = aGrid.dataSource.data();
//			for (var i=0; i < allDatas.length; i++) {
//				if(allDatas[i].attribute1 == "Y" && allDatas[i].stCommonCodeId == data.CODE){
//					fnKendoMessage({message : '필수항목은 제외하실 수 없습니다.' });
//					flag = false;
//				};
//			};
//		}

		if(flag) {
			bGrid.dataSource.remove(data);
			fnAllExcelColUpdate();
			bGrid.refresh();
		}
	};

	function fnAllExcelColUpdate(){
		var datas = bGrid.dataSource.data();
		for (var i=0; i < datas.length; i++) {
			datas[i].EXCEL_COL = chkExcelCol(i);
		};
	}


	function fnGridRowNumRtn(targetGrid){
		var targetGrid = targetGrid;
		var row = targetGrid.select().closest("tr");
		var rowIdx = $("tr", targetGrid.tbody).index(row);

		return rowIdx;
	};

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Clear*/
	$scope.fnClear =function(){	fnClear();};
	/** Common Save*/
	$scope.fnSave = function(){	fnSave();};
	/** Common Delete*/
	$scope.fnDelInit = function(){	fnDelInit();};

	$scope.fnGridAdd = function(){	fnGridAdd();};
	$scope.fnGridAddNull = function(){	fnGridAddNull();};
	$scope.fnGridDel = function(){	fnGridDel();};
	$scope.fnDataUp = function(){	fnDataUp();};
	$scope.fnDataDown = function(){	fnDataDown();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
