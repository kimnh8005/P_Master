/**-----------------------------------------------------------------------------
 * description : 거래처 관리 > 거래처 공지/문의 > 공지사항
 * @ 수정일				수정자			수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.21		김아란			최초생성
 * @
 * -----------------------------------------------------------------------------**/

'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var pageParam = fnGetPageParam();
var csId = pageParam.csId;
var bbsConfigInfo;
var gridTitleArray = new Array();

// ----------------------------------------------------------------------------
// noticeViewController 에서 진입 여부 확인
// ----------------------------------------------------------------------------
var gbPageTp = '';

try {
  if (gbPageTp == undefined) {
    //console.log('# gbPageTp is Null');
  }
  else {
    if (gbPageId == 'noticeView') {
      gbPageTp = gbPageId;
    }
    else {
      //console.log('# gbPageTp is Not noticeView');
    }
  }
}
catch (error) {
  //console.log('# gbPageTp is Null 2');
}
//console.log('# gbPageTp :: ', gbPageTp);

$(document).ready(function() {
	fnInitialize();		//Initialize Page Call ---------------------------------

	function fnInitialize(){		//Initialize PageR
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({ PG_ID  : 'notice', callback : fnUI });
	}

	function fnUI(){
		fnTranslate();		//다국어 변환--------------------------------------------
		fnInitButton();		//Initialize Button  ---------------------------------
		fnInitOptionBox();		//Initialize Option Box ------------------------------------
		fnInitGrid();		//Initialize Grid ------------------------------------

		var option2 = new Object();
		option2 = fnGetPageParam();

		$('#searchForm').bindingForm({ 'rows':option2 }, 'rows', false);
		$('#fnDel, #fnNotiSet').kendoButton({ enable: false });

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnClear').kendoButton();
		$('#fnDel, #fnNotiSet, #fnNotiSet2').kendoButton({ enable: false });
	}

	function fnSearch(){
//		$('input[type=checkbox][id=checkBoxAll]').prop("checked", false);


		//달력 시작일 종료일 체크
		if($("#startCreateDate").val() > $("#endCreateDate").val()) {
			fnKendoMessage({message : "시작일을 종료일보다 뒤로 설정할 수 없습니다.", ok : function() {
					$("#endCreateDate").focus();
				}});
			return false;
		}


		var data = $('#searchForm').formSerialize(true);

//		if(data.rtnValid){
			var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
			};

			aGridDs.query( query );
//		}
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		//$('#aGrid').gridClear(true);
	}

	function fnNew(){
		var sData = $('#searchForm').formSerialize(true);
		var option = new Object();
		option.url = "#/noticeAdd";
		option.menuId = 201;
		option.data = {
						editMode		:"I",
						companyBbsType	:sData.companyBbsType,
						useYn			:sData.useYn,
						popupYn			:sData.popupYn,
						conditionType	:sData.conditionType,
						conditionValue	:sData.conditionValue,
						startCreateDate	:sData.startCreateDate,
						endCreateDate	:sData.endCreateDate
						};
		$scope.$emit('goPage', option);
	}
	function fnDel(){
		var selectRows 	= aGrid.tbody.find('input[name=itemGridChk]:checked').closest('tr');
		var deleteArray = new Array();
		for(var i =0; i< selectRows.length;i++){
			var dataRow = aGrid.dataItem($(selectRows[i]));
			deleteArray.push(dataRow);
		}
		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :selectRows.length + '개의 게시물을 삭제 하시겠습니까?' }), type : "confirm" ,ok : function(){ fnDelApply(deleteArray) }  });
	}
	function fnDelApply(deleteArray){
		var url  = '/admin/cs/notice/delNotice';
		var cbId = 'delete';
		var map = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : url,
			params  : {deleteData :kendo.stringify(deleteArray)},
			success :
				function( data ){
					fnBizCallback(cbId, map);
				},
			isAction : 'delete'
		});
	}
	function fnNotiSet(notiYn){
		var selectRows 	= aGrid.tbody.find('input[name=itemGridChk]:checked').closest('tr');
		if(selectRows.length < 1){
			fnKendoMessage({message : fnGetLangData({key :"5062",nullMsg :'게시글을 먼저 선택해 주세요.' })});
			return;
		}
		var updateArray = new Array();
		for(var i =0; i< selectRows.length;i++){
			var dataRow = aGrid.dataItem($(selectRows[i]));
			updateArray.push(dataRow);
		}
		var msg="";
		var langKey="";
		if(notiYn == "Y"){
			langKey = "5060";
			msg = selectRows.length + "개의 게시물을 공지설정 하시겠습니까?";
		}else{
			langKey = "5061";
			msg = selectRows.length + "개의 게시물을 공지해제 하시겠습니까?";
		}
		fnKendoMessage({message:fnGetLangData({key :langKey,nullMsg :msg }), type : "confirm" ,ok : function(){ fnNotiSetApply(notiYn, updateArray) }  });
	}
	function fnNotiSetApply(notiYn, updateArray){
		var url  = '/admin/cs/notice/putNoticeSet';
		var cbId = 'update';
		var map = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : url,
			params  : {updateData :kendo.stringify(updateArray), notificationYn:notiYn},
			success :
				function( data ){
					fnBizCallback(cbId, map);
				},
			isAction : 'update'
		});
	}

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url : '/admin/cs/notice/getNoticeList'
			,pageSize : PAGE_SIZE
		});


		aGridOpt = {
			dataSource : aGridDs
			,pageable : { pageSizes : [20, 30, 50], buttonCount : 5}
			,navigatable: true
			//,height:550
			,columns : [
				{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />", template : '<input type="checkbox" name="itemGridChk" class="itemGridChk" />', width:'50px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }}
				,{ field : 'no', title : 'NO', width:'50px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }, filterable : { cell : { showOperators: false }}}
				,{ field : 'companyBbsType', title : '공지 구분', width:'80px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }}
				,{ field : 'popupYn', title : '팝업노출', width:'80px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }, template: "#=(popupYn=='Y') ? '예' : '아니오'#"}
				,{ field : 'title', title : '제목', width:'350px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				,{ field : 'createName', title : '작성자', width:'120px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				,{ field : 'createDate', title : '등록일자', width:'150px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				,{ field : 'physicalAttachment', title : '첨부', width:'200px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				,{ field : 'useYn', title : '사용여부', width:'80px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;" }, template: "#=(useYn=='Y') ? '예' : '아니오'#"}
				,{ field : 'views', title : '조회수', width:'50px', attributes : { "class" : "#=(notificationModeYn=='Y')?'bgColorGreen':''#", style : "text-align:center;"}}
				,{ field : 'csCompanyBbsId', hidden:true }
				]
			,change : fnGridClick};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#checkBoxAll").on("click",function(index){
			if($("#checkBoxAll").prop("checked")==true){
				$('INPUT[name=itemGridChk]').prop("checked",true);
				$('#fnDel').data('kendoButton').enable(true);
				$('#fnNotiSet').data('kendoButton').enable(true);
				$('#fnNotiSet2').data('kendoButton').enable(true);

			}else{
				$('INPUT[name=itemGridChk]').prop("checked",false);
				$('#fnDel').data('kendoButton').enable(false);
				$('#fnNotiSet').data('kendoButton').enable(false);
				$('#fnNotiSet2').data('kendoButton').enable(false);
			}
		});

		aGrid.bind("dataBound", function(){
			$('#totalCnt').text(aGridDs._total);

			$('INPUT[name=itemGridChk]').prop("checked",false);
			$('input[type=checkbox][class=itemGridChk]').on("change", function (){
				if($('input[type=checkbox][class=itemGridChk]:checked').length > 0){
					$('#fnDel').data('kendoButton').enable(true);
					$('#fnNotiSet').data('kendoButton').enable(true);
					$('#fnNotiSet2').data('kendoButton').enable(true);
				}
				if($('input[type=checkbox][class=itemGridChk]:checked').length == 0){
					$('#fnDel').data('kendoButton').enable(false);
					$('#fnNotiSet').data('kendoButton').enable(false);
					$('#fnNotiSet2').data('kendoButton').enable(false);
				}
			});

			// ----------------------------------------------------------------------
      // # 버튼 노출/숨김 제어
      // ----------------------------------------------------------------------
      fnButtonDisplay();
		});
	}

	function fontcolorhandler(COMP_CATEGORY_ID){
		var returnVal = "";

		if(COMP_CATEGORY_ID == "2"){
			returnVal = 'text-align:left;color:red';
		}else{
			returnVal = 'text-align:left';
		}

		return returnVal;
	}

	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		var sData = $('#searchForm').formSerialize(true);
		var option = new Object();
		option.menuId = 200;
		option.data = {
				csCompanyBbsId	:map.csCompanyBbsId,
				companyBbsType	:sData.companyBbsType,
				useYn			:sData.useYn,
				popupYn			:sData.popupYn,
				conditionType	:sData.conditionType,
				conditionValue	:sData.conditionValue,
				startCreateDate	:sData.startCreateDate,
				endCreateDate	:sData.endCreateDate
				};
		// $scope.goPage(option);


		if (gbPageTp == 'noticeView') {
		  // 대시보드에서의 호출 : 읽기모드
		  option.url = "#/noticePopupView";
		}
		else {
		  option.url = "#/noticePopup";
		}
		option.target = '_blank';
		fnGoNewPage(option);  // 새페이지(탭)
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnTagMkRadio({
			id    :  'inputCompanyBbsType',
			tagId : 'companyBbsType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "BOS_BBS_TYPE", "useYn" :"Y"},
			beforeData : [
				{"CODE":"", "NAME":"전체"},
			],
			async : false,
			chkVal: "",
			style : {}
		});
		fnTagMkRadio({
			id    :  'inputUseYn',
			tagId : 'useYn',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
			],
			chkVal: "",
			style : {}
		});
		fnTagMkRadio({
			id    :  'inputPopupYn',
			tagId : 'popupYn',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
			],
			chkVal: "",
			style : {}
		});
		fnKendoDropDownList({
			id    : 'conditionType',
			data  : [
				{"CODE":"CREATE_NAME"	,"NAME":"작성자"},
				{"CODE":"TITLE"		,"NAME":"제목"}
			],
			textField :"NAME",
			valueField : "CODE"
		});
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			change: function() {
				$("#endCreateDate").data("kendoDatePicker").min($("#startCreateDate").val());
			}
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			change: function() {
				$("#startCreateDate").data("kendoDatePicker").max($("#endCreateDate").val());
			}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------

	// ==========================================================================
  // # 버튼 노출/숨김 제어
  // ==========================================================================
  function fnButtonDisplay() {
    // 대시보드 버튼 제어 처리 영역
    if (gbPageTp == 'noticeView') {
      // 대시보드에서 호출 : 읽기모드
      $('#editButtonDiv').hide();
      $('#newButtonSpan').hide();
    }
    else {
      $('#editButtonDiv').show();
      $('#newButtonSpan').show();
    }
  }

	//-------------------------------  Common Function start -------------------------------

		/**
		* 콜백합수
		*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.', ok :fnSearch});
				break;
			case 'update':
				fnKendoMessage({message : '수정되었습니다.', ok :fnSearch});
				break;
		}
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ){	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function( ){	fnClear();};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** Common Del*/
	$scope.fnDel = function( ){  	fnDel();};
	/** Common NotiSet*/
	$scope.fnNotiSet = function(gubun){  fnNotiSet(gubun);};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
