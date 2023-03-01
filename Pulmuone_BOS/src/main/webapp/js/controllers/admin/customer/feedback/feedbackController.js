﻿/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 후기관리 @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ @ 2020.11.23
 * 안치열 최초생성 @
 ******************************************************************************/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'feedback',
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
		$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

		if($('#companyStandardType').getRadioVal() == "Y"){
			if($('#findKeyword').val() == '' ){
				fnKendoMessage({ message : "검색 조건을 확인해 주세요."});
				return
			}
		}

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

		// 단일조건, 복수조건 검색 제어
		if($("#companyStandardType_1").prop("checked")==true ){
			$("input[name=feedbackFilter]").eq(0).prop("checked", false).trigger("change");
			$("input[name=displayYn]").eq(0).prop("checked", true).trigger("change");
			setDefaultDatePicker();
		}
//		else{
//			$('#createDateStart').val('');
//			$('#createDateEnd').val('');
//			$("input[name=feedbackFilter]").eq(0).prop("checked", false).trigger("change");
//			$("input[name=displayYn]").eq(0).prop("checked", true).trigger("change");
//			fnDefaultSet();
//			fnSearch();
//		}


	}

	// 기본 설정
	function fnDefaultSet(){
		$("input[name=feedbackFilter]").eq(0).prop("checked", false).trigger("change");
	    $("input[name=displayYn]").eq(0).prop("checked", true).trigger("change");
	    $('#searchOneDiv').hide();
	    $('#searchProductDiv').show();
		$('#searchScoreDiv').show();
		$('#searchFeedbackDiv').show();
		$('#searchDateDiv').show();
		$("#companyStandardType_1").prop("checked",true);

		setDefaultDatePicker();
	};

	// 엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/customer/feedback/feedbackExportExcel', data);
	}

	function fnConfirm(psShippingPattern){
		fnKendoMessage({message:'수정된 정보를 저장하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });

	}

	// 확인 후 수정사항 저장
	function fnSave(){

		var url = '/admin/customer/feedback/putFeedbackInfo';
		var data = $('#inputForm').formSerialize(true);

		if(data.adminExcellentYnCheck == 'on'){
			data.adminExcellentYnCheck = 'Y';
		}

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback('confirm', data);
					},
					isAction : 'batch'
				});
		}
	}

	// 팝업창 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}


	// 이미지 팝업 호출
	function fnShowImage(imageUrl){

        fnKendoPopup({
              id      : 'feedbackPopup'
            , title   : '후기첨부파일'
            , src     : '#/feedbackPopup'
            , width   : '600px'
            , height  : '600px'
            , param   : { "LOGO_URL" : imageUrl }
            , success : function (id, data) {
                            if (data.id) {
                                $('#baseName').val(data.baseName);
                            }
                        }
        });
	}

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#createDateStart").data("kendoDatePicker").value(fnGetDayMinus(today, 6));
		$("#createDateEnd").data("kendoDatePicker").value(today);
	}


	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/customer/feedback/getFeedbackList",
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
				 { title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'feedbackProductTypeName'	,title : '후기구분',width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'feedbackTypeName'	,title : '후기작성유형'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'itemName'			,title : '품목명'		, width:'150px',attributes:{ style:'text-align:left' }}
				,{ field:'itemCode'			,title : '품목코드'	, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'comment'			,title : '후기내용'	, width:'170px',attributes:{ style:'text-align:left' }}
				,{ field:'userName'			,title : '회원명'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'userId'			,title : '회원ID'		, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'satisfactionScore',title : '만족도'		, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일'		, width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'displayYn'		,title : '노출여부'	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'feedbackId', hidden:true}
				,{ field:'ilGoodsId', hidden:true}

			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr", function () {
			fnGridClick();
		});

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });
	}

	// 후기관리 상세화면 호출
	function fnGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/feedback/getDetailFeedback',
			params  : {feedbackId : aMap.feedbackId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};


	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});


		// 상품유형
		fnTagMkRadio({
			id : 'feedbackProductType',
			url : "/admin/comn/getCodeList",
			tagId : 'feedbackProductType',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "FEEDBACK_PRODUCT_TP", "useYn" :"Y"},
			beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
			chkVal : 'ALL'

		});

		// 후기단일조건
		fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{"CODE":"SEARCH_SELECT.ITEM","NAME":'품목명'}
					,{"CODE":"SEARCH_SELECT.ITEM_CODE","NAME":'품목코드'}
					,{"CODE":"SEARCH_SELECT.USER_NAME","NAME":'회원명'}
					,{"CODE":"SEARCH_SELECT.USE_ID","NAME":'회원ID'}
					,{"CODE":"SEARCH_SELECT.EVENT_CODE","NAME":'이벤트코드'}
					]
		});

		// 만족도
		fnKendoDropDownList({
			id    : 'satisfactionScore',
			tagId : 'satisfactionScore',
			data  : [{"CODE":"5","NAME":'5점 ★★★★★'}
					,{"CODE":"4","NAME":'4점 ★★★★'}
					,{"CODE":"3","NAME":'3점 ★★★'}
					,{"CODE":"2","NAME":'2점 ★★'}
					,{"CODE":"1","NAME":'1점 ★'}
//					,{"CODE":"0","NAME":'0점 ☆'}
					],
			blank : "전체"
		});


		// 후기유형
		fnKendoDropDownList({
			id  : 'feedbackType',
			tagId : 'feedbackType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "FEEDBACK_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "",
			blank : "전체"
		});


		// 후기필터
		fnTagMkChkBox({
            id : "feedbackFilter",
            url : "/admin/comn/getCodeList",
            tagId : "feedbackFilter",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "FEEDBACK_FILTER", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "우수후기 전체" }]
        });


		// 공개여부
		fnTagMkChkBox({
            id : "displayYn",
            url : "/admin/comn/getCodeList",
            tagId : "displayYn",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "DISPLAY_YN", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

		// 공개여부
		fnTagMkRadio({
			id : 'popupDisplayYn',
			tagId : 'popupDisplayYn',
			async : false,
			style : {},
			data  : [
				{ "CODE" : "Y"	, "NAME":'노출' },
				{ "CODE" : "N"	, "NAME":'비노출' }
			]


		});

		fbCheckboxChange();

		// 단일/복수조건 검색 구분
		fnTagMkRadio({
			id    :  'companyStandardType',
			tagId : 'companyStandardType',
			chkVal: 'Y',
			data  : [
						{ "CODE" : "Y"	, "NAME":'단일조건 검색' },
						{ "CODE" : "N"	, "NAME":'복수조건 검색' }
					],
			style : {},
			change : function(e){
				if($('#companyStandardType').getRadioVal() == "Y"){
					$('#searchForm').formClear(true);
					$('#searchOneDiv').show();
					$('#searchProductDiv').hide();
					$('#searchScoreDiv').hide();
					$('#searchFeedbackDiv').hide();
					$('#searchDateDiv').hide();
					$("#companyStandardType_0").prop("checked",true);

					$('#createDateStart').val('');
					$('#createDateEnd').val('');
					$("input[name=reviewFilter]").eq(0).prop("checked", false).trigger("change");
					$("input[name=displayYn]").eq(0).prop("checked", true).trigger("change");


					$('#aGrid').gridClear(true);

				}else if($('#companyStandardType').getRadioVal() == "N"){
					$('#searchForm').formClear(true);
					fnDefaultSet();
					$('#searchOneDiv').hide();
					$('#searchProductDiv').show();
					$('#searchScoreDiv').show();
					$('#searchFeedbackDiv').show();
					$('#searchDateDiv').show();
					$("#companyStandardType_1").prop("checked",true);

					$('#aGrid').gridClear(true);
					fnSearch();

				}
			}
		});

		// 베스트후기(관리자)
		fnTagMkRadio({
			id : 'adminBestYn',
			tagId : 'adminBestYn',
			data  : [
				{ "CODE" : "Y"	, "NAME":'예' },
				{ "CODE" : "N"	, "NAME":'아니오' }
			]

		});

		// 등록(가입)일 시작
        fnKendoDatePicker({
            id: "createDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
            defVal: fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
//            change : function(e) {
//                fnStartCalChange("createDateStart", "createDateEnd");
//            }
        });

        // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
            btnEndId: "createDateEnd",
            defVal: fnGetToday(),
            defType : 'oneWeek'
//            change : function(e) {
//                fnEndCalChange("createDateStart", "createDateEnd");
//            }
        });


        // 품목코드 호출 시 품목 상세페이지 새창 호출
        $("#itemDetailLink").on("click", function(e){
        	e.preventDefault();
        	window.open("#/itemMgmModify?ilItemCode="+ $('#itemCode').val() +"&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON","_blank","width=1800, height=1000, resizable=no, fullscreen=no");
        });


	}
	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------


	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':

				fnKendoInputPoup({height:"auto" ,width:"800px",title:{ nullMsg : '후기 상세 정보'} });

				$('#kendoPopup').scrollTop(0);

				$('#inputForm').bindingForm(data, "row", true);

				if(data.row.evEventId != null){
					$('#adminExcellentYnDiv').show();
					$('#adminExcellentYn').show();
					$('#adminExcellentView').show();
				}else{
					$('#adminExcellentYnDiv').hide();
					$('#adminExcellentYn').hide();
					$('#adminExcellentView').hide();
				}


				// 베스트후기(자동)
				if(data.row.bestCnt > 10){
					$('#bestCntYn').val('예');
				}else{
					$('#bestCntYn').val('아니오');
				}

				// 베스트후기(관리자)
				if(data.row.adminBestYn == 'Y'){
					$("#adminBestYn_0").prop("checked",true);
				}else{
					$("#adminBestYn_1").prop("checked",true);
				}

				// 공개설정
				if(data.row.displayYn == 'Y'){
					$("#popupDisplayYn_0").prop("checked",true);
				}else{
					$("#popupDisplayYn_1").prop("checked",true);
				}

				// 우수후기선정 체크시 표시처리
				if(data.row.adminExcellentYn == 'Y'){
					$('#adminExcellentYnCheckDiv').hide();
					$('#adminExcellentYnView').show();
					$('#adminExcellentYnView').val('우수후기');
					$('#adminExcellentYn').val('우수후기');
				}else{
					$('#adminExcellentYnCheckDiv').show();
					$('#adminExcellentYnView').hide();
				}


				fnAjax({
					url		: '/admin/customer/feedback/getImageList',
					params	: {feedbackId : data.row.feedbackId},
					isAction : 'select',
					success	:
						function( data ){
							var imageHtml = "";
							if(data.rows.length>0){
								imageHtml += "			<div style='position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;'>";
								for(var i=0; i < data.rows.length; i++){

									var imageUrl = publicStorageUrl + data.rows[i].imageName;
									imageHtml += "      <span style='width:150px; height:150px; margin-bottom:10px; margin-right:10px; border-color:#a9a9a9; border-style:solid; border-width:1px'>";
									imageHtml += "          <img src='"+ imageUrl +"' style='max-width: 100%; max-height: 100%;' onclick='$scope.fnShowImage(\""+imageUrl+"\")'>";
									imageHtml += "      </span>";
								}
								imageHtml += "			</div>";

								$("#imageContent").html(imageHtml);
							}
							else{
								$("#imageContent").html("");
							}
						}
				});


				break;

			case 'confirm':
				fnKendoMessage({
					message:"저장되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
			});

		}
	}
	// ------------------------------- Common Function end
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
	/** Common ShowImage */
	$scope.fnShowImage = function(url){ fnShowImage(url);};


	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });



	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
