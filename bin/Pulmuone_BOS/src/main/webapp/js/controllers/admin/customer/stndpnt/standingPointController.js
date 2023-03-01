/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 상품입점 문의 관리
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.18 	안치열 			최초생성 @
 ******************************************************************************/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var message;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'standingPoint',
			callback : fnUI
		});
	}


	function fnUI(){

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitOptionBox();// Initialize Option Box

		fnDefaultSet();

		fnSearch();


	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelExport').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

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
		$("input[name=apprSearchStat]").eq(0).prop("checked", true).trigger("change");
		setDefaultDatePicker();
		fnSearch();

	}

	// 기본 설정
	function fnDefaultSet(){

		$("input[name=apprSearchStat]").eq(0).prop("checked", true).trigger("change");
		setDefaultDatePicker();
	};

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#createDateStart").data("kendoDatePicker").value(fnGetDayMinus(today, 6));
		$("#createDateEnd").data("kendoDatePicker").value(today);
	}


	function fnConfirm(){

		if($("#apprStat_0").prop("checked")==true){
			message = '승인';
		}else{
			message = '반려';
		}

		if($('#questionStat').val() == 'STAND_PNT_STAT.APPROVED' &&  ($("#apprStat_0").prop("checked")==true)){
			fnClose();
		}else if($('#questionStat').val() == 'STAND_PNT_STAT.DENIED' &&  ($("#apprStat_1").prop("checked")==true)){
			fnClose();
		}else{
			fnKendoMessage({message: message +'처리 하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
		}

	}


	// 승인 상태 저장
	function fnSave(){

		var url = '/admin/customer/stndpnt/putStandingPointStatus';
		var data = $('#inputForm').formSerialize(true);
		var cbId = 'update';

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


	// 팝업창 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}



	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/customer/stndpnt/getStandingPointList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : [
				 { title : 'No',width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'compNm'			,title : '회사명'		,width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'question'			,title : '문의내용'	, width:'200px',attributes:{ style:'text-align:left; text-decoration: underline;color:blue;' }}
				,{ field:'managerUserName'	,title : '담당자명'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'address'			,title : '주소'		, width:'150px',attributes:{ style:'text-align:left' }}
				,{ field:'mobile'			,title : '휴대폰번호'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'tel'				,title : '연락처'		, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'email'			,title : '이메일'		, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'questionStatName'	,title : '문의상태'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'apprUserName'		,title : '승인담당자'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'createDt'			,title : '등록일자'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'questionStat', hidden:true}
				,{ field:'apprUserId', hidden:true}
				,{ field:'csStandPntId', hidden:true}

			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

//		$("#aGrid").on("click", "tbody>tr", function () {
//			fnGridClick();
//		});

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });

		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			if(index == 2){
				fnGridClick();
			}
		});

	}


	// 상품입점 문의 상세화면 호출
	function fnGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/stndpnt/getDetailStandingPoint',
			params  : {csStandPntId : aMap.csStandPntId, questionStat : aMap.questionStat},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};



	// 엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/customer/stndpnt/getStandingPointExportExcel', data);
	}

	/**
	 * 첨부파일 다운로드
	 */
	function attchFileDownload(filePath, physicalFileName, originalFileName) {
		var opt = {
				filePath: filePath,
				physicalFileName: physicalFileName,
			    originalFileName: originalFileName
		}

		fnDownloadPublicFile(opt);
	}



	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, "row", true);

				fnKendoInputPoup({height:"900px" ,width:"800px",title:{key :"5876",nullMsg :'상품입점 상담 문의 상세 정보' } });

				if(data.row.questionStat == 'STAND_PNT_STAT.DENIED'){
					$("#apprStat_1").prop("checked",true);
				}else{
					$("#apprStat_0").prop("checked",true);
				}

				// 첨부파일 정보
				if(data.rowsFile != null && data.rowsFile != null){
					var fileList = "";
					fileList = fileList + data.rowsFile.realFileNm+"\n";
					$("#inputAttcId").val(data.rowsFile.csStandPntAttcId);
					$("span#STND_PNT_ATTC").html(fileList);
					$('#attcDown').attr('href', "javascript:$scope.attchFileDownload('" + data.rowsFile.filePath + "', '" + data.rowsFile.fileNm + "', '" + data.rowsFile.realFileNm + "')");
					$('#attcDown').css("display" ,"");
				}else{
					$("span#STND_PNT_ATTC").html('첨부한 파일이 없습니다.');
					$('#attcDown').css("display" ,"");
				}

				break;
			case 'update':
				fnKendoMessage({
						message:message + "되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}


	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});


		// 검색 조건
		fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{"CODE":"SEARCH_SELECT.COMPANY","NAME":'회사명'}
					,{"CODE":"SEARCH_SELECT.MANAGER","NAME":'담당자명'}
					,{"CODE":"SEARCH_SELECT.QUESTION","NAME":'문의내용'}
					,{"CODE":"SEARCH_SELECT.APPRUSER","NAME":'승인담당자명'}
					]
		});


		// 등록일 시작
        fnKendoDatePicker({
            id: "createDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
            defVal: fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
            defVal: fnGetToday(),
            defType : 'oneWeek'
        });

        fbCheckboxChange();


		// 승인상태
		fnTagMkChkBox({
            id : "apprSearchStat",
            url : "/admin/comn/getCodeList",
            tagId : "apprSearchStat",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "STAND_PNT_STAT", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

		 // 승인처리
	    fnTagMkRadio({
			id    :  'apprStat',
			tagId : 'apprStat',
			data  : [
				{ "CODE" : "STAND_PNT_STAT.APPROVED"	, "NAME":'승인' },
				{ "CODE" : "STAND_PNT_STAT.DENIED"	, "NAME":'반려' }
			],
			chkVal: "STAND_PNT_STAT.APPROVED",
			style : {}
	    });



	}
	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------


	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
	/** Common ExcelDownload */
	$scope.fnExcelExport = function( ){	fnExcelExport();};
	/** Common Confirm */
	$scope.fnConfirm = function(){	 fnConfirm();};
	/** Common Close */
	$scope.fnClose = function( ){  fnClose();};

	$scope.attchFileDownload = function(filePath, physicalFileName, originalFileName){  attchFileDownload(filePath, physicalFileName, originalFileName);};

	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });



	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
