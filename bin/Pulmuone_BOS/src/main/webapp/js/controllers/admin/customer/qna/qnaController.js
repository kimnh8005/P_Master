
/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 통합문의관리
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.15 	안치열 			최초생성 @
 ******************************************************************************/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();

var ecsCtgryStd1;
var ecsCtgryStd2;
var ecsCtgryStd3;

var oldDisplayType;
var oldEcsType1;
var oldEcsType2;
var oldEcsType3;
var oldFirstAnswer;
var oldSecondAnswer;
var oldoutmallQnaAnswer;

var deptCode;
var qnaTypeCode;

var csQnaId;
var csOutmallQnaId;

var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

if(defaultActivateTab == undefined){
	var defaultActivateTab;
	$('#customerQnaWarpDiv').show();
	$('#customerQnaNoneDiv').hide();
	$('#urUserId').val();
}else{
	$('#urUserId').val(csData.urUserId);
	$('#companyStandardType').hide();
	$('#customerQnaWarpDiv').hide();
	$('#customerQnaNoneDiv').show();
}

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : pageId,
			callback : fnUI
		});

		//답변지연건만 보기
		$('#ng-view').on("click", "#answerDelay" ,function(index){
			if($("#answerDelay").prop("checked")==true){
				$('#answerDelayView').val('Y');
				fnSearch();
			}else{
				$('#answerDelayView').val('N');
				fnSearch();
			}
		});

		// 탈퇴회원 문의 제외
		$('#ng-view').on("click", "#excludeDropUser" ,function(index){
			if($("#excludeDropUser").prop("checked")==true){
				$('#excludeDropUserView').val('Y');
				fnSearch();
			}else{
				$('#excludeDropUserView').val('N');
				fnSearch();
			}
		});

		// 상품, 1:1 문의 권한 존재할 경우 문의유형 제어처리
		$('#ng-view').on("click", "#qnaType_0" ,function(index){
			if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){
				$("#goodsDiv").show();
				$("#goodsType").data("kendoDropDownList").enable(false);
				$("#qnaDiv").hide();
			}
		});

		$('#ng-view').on("click", "#qnaType_1" ,function(index){
			if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){
				if($("#qnaType_1").prop("checked")==false && $("#qnaType_2").prop("checked")==true){
					$("#goodsDiv").hide();
					$("#qnaDiv").show();
				}else if($("#qnaType_1").prop("checked")==true && $("#qnaType_2").prop("checked")==false){
					$("#qnaDiv").hide();
					$("#goodsDiv").show();
					$("#goodsType").data("kendoDropDownList").enable(true);
				}else{
					$("#goodsDiv").show();
					$("#goodsType").data("kendoDropDownList").enable(false);
					$("#qnaDiv").hide();
				}
			}
		});

		$('#ng-view').on("click", "#qnaType_2" ,function(index){
			if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){
				if($("#qnaType_1").prop("checked")==false && $("#qnaType_2").prop("checked")==true){
					$("#goodsDiv").hide();
					$("#qnaDiv").show();
				}else if($("#qnaType_1").prop("checked")==true && $("#qnaType_2").prop("checked")==false){
					$("#qnaDiv").hide();
					$("#goodsDiv").show();
					$("#goodsType").data("kendoDropDownList").enable(true);
				}else{
					$("#goodsDiv").show();
					$("#goodsType").data("kendoDropDownList").enable(false);
					$("#qnaDiv").hide();
				}
			}
		});


	}

	function fnUI(){

		fnInitButton();	// Initialize Button ---------------------------------

		fnInitGrid();	// Initialize Grid ------------------------------------

		fnInitOptionBox();// Initialize Option Box

		fnDefaultSet();

		//레이어 팝업 통해서 들어왔을 경우 자동조회 않는다.
		//let tmp = getCookie("csUserParamData")
		//if (tmp == undefined || tmp == "") {
		//	fnSearch();
		//}

	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage, #fnAnswer, #fnShowImage').kendoButton();
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

		if(gbMallTp == 'MALL_TP.MALL') {
			if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){

			}else{

				if(fnIsProgramAuth("PRODUCTQNA")){
					data.qnaType = 'QNA_TP.PRODUCT';
				}

				if(fnIsProgramAuth("CUSTOMERQNA")){
					data.qnaType = 'QNA_TP.ONETOONE';
				}
			}
		} else if(gbMallTp == 'MALL_TP.OUTSOURCE') {
			data.saleChannelId = data.qnaChannel;
			data.qnaStatus = data.outmallQnaStatus;
			if(data.searchOutmallQnaProcYn.indexOf('ALL') != 0) {
				data.procYn = data.searchOutmallQnaProcYn;
			}
			data.collectionMallId = data.searchOutmallQnaCollectionMallId;
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

	// 초기화
	function fnClear(){

		$('#searchForm').formClear(true);
		$('input[name=answerDelay]').prop("checked",false);
		$('#answerDelayView').val('N');

		if(gbMallTp == 'MALL_TP.MALL') {
			$('input[name=excludeDropUser]').prop("checked",true);
			$('#excludeDropUserView').val('Y');
		}

		$('#companyStandardType').getRadioVal()

		// 단일조건, 복수조건 검색 제어
		if($("#companyStandardType_1").prop("checked")==true ){
			$("input[name=qnaStatus]").eq(0).prop("checked", true).trigger("change");
			$("input[name=qnaType]").eq(0).prop("checked", true).trigger("change");
			$("input[name=qnaChannel]").eq(0).prop("checked", true).trigger("change");

			setDefaultDatePicker();
		}
//		else{
//			$('#createDateStart').val('');
//			$('#createDateEnd').val('');
//			fnDefaultSet();
//			fnSearch();
//		}

		if(gbMallTp == 'MALL_TP.OUTSOURCE') { //외부몰 문의관리
			$("input[name=outmallQnaStatus]").eq(0).prop("checked", true).trigger("change");
			$("input[name=searchOutmallQnaProcYn]").eq(0).prop("checked", true).trigger("change");
			$('input[name=qnaChannel]').prop('checked', true);
			$('#outmallQnaType').data('kendoDropDownList').value('QNA_OUTMALL_TP.ORDER_SHIPPING'); //문의분류 > 주문/배송 고정값
		}


	}

	// 기본 설정
	function fnDefaultSet(){

		if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리

			$('#searchGoodsDiv').show();
			$('#searchQnaChannelDiv').hide();
			$('#outmallQnaTypeDiv').hide();
			$('#searchQnaTypeDiv').show();
			$('#searchStatusDiv').show();
			$('#searchOutmallQnaStatusDiv').hide();
			$('#outmallQnaCollectionMallIdDiv').hide();
			$('#excludeDropUserDiv').show();

			$("input[name=qnaStatus]").eq(0).prop("checked", true).trigger("change");
			$("input[name=qnaType]").eq(0).prop("checked", true).trigger("change");

			$('input[name=excludeDropUser]').prop("checked",true);
			$('#excludeDropUserView').val('Y');

		} else if(gbMallTp == 'MALL_TP.OUTSOURCE'){ //외부몰 문의관리

			$('#companyStandardType').hide();
			$('#searchGoodsDiv').hide();
			$('#searchQnaChannelDiv').show();
			$('#outmallQnaTypeDiv').show();
			$('#searchQnaTypeDiv').hide();
			$('#searchStatusDiv').hide();
			$('#searchOutmallQnaStatusDiv').show();
			$('#outmallQnaCollectionMallIdDiv').show();
			$('#excludeDropUserDiv').hide();

			$('input[name=qnaChannel]').prop('checked', true);
			$("input[name=outmallQnaStatus]").eq(0).prop("checked", true).trigger("change");
	    	$("input[name=searchOutmallQnaProcYn]").eq(0).prop("checked", true).trigger("change");

		}

	    $('#searchOneDiv').hide();
		$('#searchDateDiv').show();
		$('#searchManyDiv').show();
		$('#searchWordDiv').show();

		$("#companyStandardType_1").prop("checked",true);
		setDefaultDatePicker();

		if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){
			$("#goodsDiv").show();
			$("#goodsType").data("kendoDropDownList").enable(false);
			$("#qnaDiv").hide();
			$('#qnaDiv').hide();
		}


		//파라메타 있을때 조회 3개월 세팅
		let tmp = getCookie("csUserParamData")
		if (tmp !== undefined && tmp !== "") {
			$(".date-controller button").each(function() {
				$(this).attr("fb-btn-active", false);
			})

			$("button[data-id='fnDateBtn7']").attr("fb-btn-active", true);

			var today = fnGetToday();

			$("#createDateStart").val(fnGetDayMinus(today, 90));
			$("#createDateEnd").val(today);

			//$scope.fnSearch();
		}

	};

	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn2']").attr("fb-btn-active", true);

		var today = fnGetToday();

		$("#createDateStart").val(fnGetDayMinus(today, 1));
		$("#createDateEnd").val(today);
	}

	// 엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		var excelUrl = "";

		if(gbMallTp == 'MALL_TP.MALL') {
			excelUrl = "/admin/customer/qna/qnaExportExcel";

		} else if(gbMallTp == 'MALL_TP.OUTSOURCE') {
			excelUrl = "/admin/customer/qna/getOutmallQnaExportExcel";

			data.saleChannelId = data.qnaChannel;
			data.qnaStatus = data.outmallQnaStatus;
			if(data.searchOutmallQnaProcYn.indexOf('ALL') != 0) {
				data.procYn = data.searchOutmallQnaProcYn;
			}
			data.collectionMallId = data.searchOutmallQnaCollectionMallId;
		}

		fnExcelDownload(excelUrl, data);
	}

	// 확인 버튼 동작
	function fnConfirm(){

		if(fnDataCheck()){
			// 수정사항 발생 Case
			fnKendoMessage({message:'수정된 정보를 저장하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
		}else{
			// 수정사항이 없는 Case
			fnClose();
		}
	}



	// 수정항목 변동사항 여부 확인
	function fnDataCheck(){

		var secretType;
		if($("#secretType_0").prop("checked")){
			secretType = 'QNA_SECRET_TP.OPEN';
		}else if($("#secretType_1").prop("checked")){
			secretType = 'QNA_SECRET_TP.CLOSE_ADMIN';
		}else if($("#secretType_2").prop("checked")){
			secretType = 'QNA_SECRET_TP.CLOSE_CUSTOMER';
		}
		if(oldDisplayType != secretType){
			return true;
		}

		if((oldEcsType1 != $('#ecsCtgryStd1').val() && $('#ecsCtgryStd1').val() != '')
			|| (oldEcsType2 != $('#ecsCtgryStd2').val() && $('#ecsCtgryStd2').val() != '')
				|| (oldEcsType3 != $('#ecsCtgryStd3').val() && $('#ecsCtgryStd3').val() != '')){
			return true;
		}

		if(gbMallTp == 'MALL_TP.MALL') {
            if(oldFirstAnswer != $('#firstContent').val() && $('#firstContent').val() != ''){
                return true;
            }
            if( oldSecondAnswer != $('#secondContent').val() && $('#secondContent').val() != ''){
                return true;
            }
        } else if(gbMallTp == 'MALL_TP.OUTSOURCE') {
			if (oldoutmallQnaAnswer != $('#outmallQnaAnswerContent').val() && $('#outmallQnaAnswerContent').val() != '') {
				return true;
			}
		}

		return false
	}

	// 답변진행 상태변경 여부
	function fnAnswer(){

		fnKendoMessage({message:'답변확인중 상태로 전환됩니다. 답변등록을 진행하시겠습니까?', type : "confirm" ,ok : function(){ fnSetAnswerStatus() } });

	}

	// 답변진행 상태 변경
	function fnSetAnswerStatus(){

		var statusUrl = '';
		var statusParams = {};

		if(gbMallTp == 'MALL_TP.MALL') { // 통합몰 문의관리
			statusUrl = '/admin/customer/qna/putQnaAnswerStatus';
			statusParams = {csQnaId : $('#csQnaId').val()};
		}else if(gbMallTp == 'MALL_TP.OUTSOURCE') { // 외부몰 문의관리
			statusUrl = '/admin/customer/qna/putOutmallQnaAnswerStatus';
			statusParams = {csOutmallQnaId : $('#csOutmallQnaId').text()};
		}

		fnAjax({
			url     : statusUrl,
			params  : statusParams,
			success :
				function( data ){
			},
			isAction : 'batch'
		});


		$('#fnAnswer').hide();
		$('#ecsDiv').show();
		$('#answerDiv').show();
		$('#answer2StDiv').hide();
		$('#status').val('QNA_STATUS.ANSWER_CHECKING');

		var firstStr = document.getElementById("firstContent").value;
		var outmallQnaStr = document.getElementById("outmallQnaAnswerContent").value;

		if(gbMallTp == 'MALL_TP.MALL') { // 통합몰 문의관리
			$('#answer1StDiv').show();
			firstStr = replaceAll(firstStr, "<br/>", "\r\n");
			document.getElementById("firstContent").value = firstStr;
			$('#firstContent').attr("disabled",false);
			$('#firstAnswerDateTitle').hide();
			$('#firstUserInfoTitle').hide();

			$('#outmallQnaAnswerDiv').hide();
		}else if(gbMallTp == 'MALL_TP.OUTSOURCE') { // 외부몰 문의관리
			$('#answer1StDiv').hide();
			$('#outmallQnaAnswerDiv').show();
			outmallQnaStr = replaceAll(outmallQnaStr, "<br/>", "\r\n");
			document.getElementById("outmallQnaAnswerContent").value = outmallQnaStr;
			$('#outmallQnaAnswerContent').attr("disabled",false);
			$('#outmallQnaAnswerDateTitle').hide();
			$('#outmallQnaUserInfoTitle').hide();
		}
		$('#fnConfirm').prop("disabled", false);

	}

	// 확인 후 수정사항 저장
	function fnSave(){
		var url = '';
		var secretType;
		var data = $('#inputForm').formSerialize(true);
		data.status = $('#status').val();

		if(gbMallTp == 'MALL_TP.MALL') {
			url = '/admin/customer/qna/putQnaInfo';

			if($("#secretType_0").prop("checked")){
			secretType = 'QNA_SECRET_TP.OPEN';
			}else if($("#secretType_1").prop("checked")){
				secretType = 'QNA_SECRET_TP.CLOSE_ADMIN';
			}else if($("#secretType_2").prop("checked")){
				secretType = 'QNA_SECRET_TP.CLOSE_CUSTOMER';
			}

			if($('#status').val() == 'QNA_STATUS.RECEPTION' && secretType == 'QNA_SECRET_TP.CLOSE_ADMIN' && $('#productType').val() == 'QNA_PRODUCT_TP.ETC'){
				$('#ecsCtgryStd1').val('∀');
				$('#ecsCtgryStd2').val('∀');
				$('#ecsCtgryStd3').val('∀');
			}
			csQnaId = $('#csQnaId').val();
			data.firstContent = data.firstContent.replace(/\r\n|\r|\n/g,"<br />");
			data.secondContent = data.secondContent.replace(/\r\n|\r|\n/g,"<br />");

			data.secretType = secretType;
		} else if(gbMallTp == 'MALL_TP.OUTSOURCE'){
			url = '/admin/customer/qna/putOutmallQnaInfo';
			data.csOutmallQnaId = $('#csOutmallQnaId').text();
			data.outmallQnaAnswerContent = data.outmallQnaAnswerContent.replace(/&nbsp;/g, ' ').replace(/<br\s?\/?>|\/br|&lt;BR\/&gt;/g,"\n");
		}

		if($('#ecsCtgryStd1').val() == '∀'){
			$('#ecsCtgryStd1').val('');
			$('#ecsCtgryStd2').val('');
			$('#ecsCtgryStd3').val('');
			data.ecsCtgryStd1 = '';
			data.ecsCtgryStd2 = '';
			data.ecsCtgryStd3 = '';
		}

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback('confirm', data);
					},
				fail :
					function( data, resultcode ){
					resultcode.code;
                	resultcode.message;
                	resultcode.messageEnum;

                	fnKendoMessage({
                        message : resultcode.message,
                        ok : function(e) {
                        	fnBizCallback('fail', data);
                        }
                    });


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
              id      : 'customerQnaPopup'
            , title   : '통합몰관리 첨부파일'
            , src     : '#/customerQnaPopup'
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


	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		var gridSet = fnGridSetting(gbMallTp);

		aGridDs = fnGetPagingDataSource({
			url      : gridSet.url,
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : gridSet.optColumns
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			if(gbMallTp == 'MALL_TP.MALL') { // 통합몰 문의관리
				// 문의제목 선택
				if(index == 3){
					fnGridClick();
				}
				// 상품명 선택
				if(index == 4){
					fnGridGoodsNameClick();
				}
			} else if(gbMallTp == 'MALL_TP.OUTSOURCE') { // 외부몰 문의관리
				// 판매처 상품코드 선택
				if(index == 2 && !fnIsEmpty($(this).text())){
					fnGridShopProductIdClick();
				}
				if(index == 3 && !fnIsEmpty($(this).text())){
					fnGridOdOrderIdClick();
				}
				// 문의제목 선택
				if(index == 6){
					fnoutmallGridClick(); // 문의관리 상세화면
				}
			}




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

	function fnGridSetting(gbMallTp) {
		var url = "";
		var optColumns = [];

		if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리

			url = "/admin/customer/qna/getQnaList";
			optColumns = 	[
								 { title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
								,{ field:'qnaTypeName'	,title : '문의유형',width:'90px',attributes:{ style:'text-align:center' }}
								,{ field:'qnaDivisionName'	,title : '문의분류'	, width:'90px',attributes:{ style:'text-align:center' }}
								,{ field:'qnaTitle'			,title : '문의제목'		, width:'150px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
								,{ field:'goodsName'			,title : '상품명'		, width:'150px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
								,{ field:'userName'			,title : '회원명'	, width:'70px',attributes:{ style:'text-align:center' }}
								,{ field:'userId'			,title : '회원ID'		, width:'70px',attributes:{ style:'text-align:center' }}
								,{ field:'dropUserYn'			,title : '탈퇴<br>여부'	, width:'50px',attributes:{ style:'text-align:center' }}
								,{ field:'qnaStatusName'			,title : '처리상태'	, width:'70px',attributes:{ style:'text-align:center' }}
								,{ field:'delayYn'			,title : '답변지연여부'		, width:'70px',attributes:{ style:'text-align:center' },
									template : function(dataItem){
										let returnValue;
										if(dataItem.delayYn == '지연'){
											returnValue = "<span style='color:red;'>" + dataItem.delayYn + "</span>";
										}else{
											returnValue = dataItem.delayYn;
										}
										return returnValue;
									}
								}
								,{ field:'createDate'		,title : '등록일자'		, width:'100px',attributes:{ style:'text-align:center' }}
								,{ field:'answerDate',title : '처리날짜'		, width:'100px',attributes:{ style:'text-align:center' }}
								,{ field:'answerUserName'		,title : '답변담당자'	, width:'90px',attributes:{ style:'text-align:center' },
									template : function(dataItem){
										let returnValue;
											if(dataItem.status == 'QNA_STATUS.ANSWER_COMPLETED_1ST' || dataItem.status == 'QNA_STATUS.ANSWER_COMPLETED_2ND'){
												returnValue =  dataItem.answerUserName + "<br>" + "(" + dataItem.answerUserId +")";
											}else{
												returnValue = '';
											}
										return returnValue;
									}

								}
								,{ field:'csQnaId', hidden:true}
								,{ field:'qnaType', hidden:true}
							] ;
		} else if(gbMallTp == 'MALL_TP.OUTSOURCE') { //외부몰 문의관리
			//API 수정예정
			url = "/admin/customer/qna/getOutmallQnaList";
			optColumns = 	[
								 { title : 'No'	, width:'50px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
								,{ field:'saleChannelNm'		,title : '판매채널',width:'90px',attributes:{ style:'text-align:center' }}
								,{ field:'shopProductId'	,title : '판매처</br>상품코드'	, width:'90px',attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
								,{ field:'odOrderId'		,title : '외부몰</br>주문번호'		, width:'150px',attributes:{ style:'text-align:center;'},
									template : function(dataItem){
									return (dataItem.odOrderId == "0" || dataItem.odOrderId == "-") ? '' : '<span '+ ( dataItem.odid ? 'style="text-decoration: underline;color:blue;"' : '') +'>' + dataItem.odOrderId + '</span>';}
								 }
								,{ field:'collectionMallId'	,title : '수집몰</br>주문번호'	, width:'90px',attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
								,{ field:'outmallTypeName'	,title : '문의분류'	, width:'90px',attributes:{ style:'text-align:center' }}
								,{ field:'qnaTitle'			,title : '문의제목'		, width:'150px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
								,{ field:'easyadminStatus'	,title : '이지리플</br>상태', width:'70px',attributes:{ style:'text-align:center' },
									template : function(dataItem){
										return '['+dataItem.easyadminStatus+':'+dataItem.easyadminStatusText+']';
									}
								}
								,{ field:'qnaStatusName'	,title : '처리상태'	, width:'70px',attributes:{ style:'text-align:center' }}
								,{ field:'procYnText'			,title : '처리불가여부'		, width:'70px',attributes:{ style:'text-align:center' }}
								,{ field:'delayYn'			,title : '답변지연여부'		, width:'70px',attributes:{ style:'text-align:center' },
									template : function(dataItem){
										let returnValue;
										if(dataItem.delayYn == '지연'){
											returnValue = "<span style='color:red;'>" + dataItem.delayYn + "</span>";
										}else{
											returnValue = dataItem.delayYn;
										}
										return returnValue;
									}
								}
								,{ field:'createDate'		,title : '등록일자'		, width:'100px',attributes:{ style:'text-align:center' }}
								,{ field:'answerDate',title : '처리날짜'		, width:'100px',attributes:{ style:'text-align:center' }}
								,{ field:'answerUserName'		,title : '답변담당자'	, width:'90px',attributes:{ style:'text-align:center' },
									template : function(dataItem){
										let returnValue;
											if(dataItem.status == 'QNA_STATUS.ANSWER_COMPLETED' && !fnIsEmpty(dataItem.answerUserId)){ //답변완료
												returnValue =  dataItem.answerUserName + "<br>" + "(" + dataItem.answerUserId +")";
											}else{
												returnValue = '';
											}
										return returnValue;
									}
								}
								,{ field:'csOutmallQnaId', hidden:true}
								,{ field:'qnaType', hidden:true}
							] ;

		}
		return {url : url, optColumns : optColumns};

	}
	// 문의제목 선택
	function fnGridQnaTitleClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnGridClick(aMap.qanType);

	}


	// 상품명 선택 상세화면 호출
	function fnGridGoodsNameClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		var goodsUrl;

		if(aMap.ilGoodsId != '' && aMap.ilGoodsId != null && aMap.ilGoodsId != undefined){
            if(aMap.goodsTp == 'GOODS_TYPE.ADDITIONA'){
                goodsUrl = "#/goodsAdditional?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.DAILY'){
                goodsUrl = "#/goodsDaily?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.DISPOSAL'){
                goodsUrl = "#/goodsDisposal?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.GIFT' || aMap.goodsTp == 'GOODS_TYPE.GIFT_FOOD_MARKETING') {
                goodsUrl = "#/goodsAdditional?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.INCORPOREITY'){
                goodsUrl = "#/goodsIncorporeal?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.NORMAL'){
                goodsUrl = "#/goodsMgm?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.PACKAGE'){
                goodsUrl = "#/goodsPackage?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.RENTAL'){
                goodsUrl = "#/goodsRental?ilGoodsId="
            }else if(aMap.goodsTp == 'GOODS_TYPE.SHOP_ONLY'){
                goodsUrl = "#/goodsShopOnly?ilGoodsId="
            }

            window.open( goodsUrl + aMap.ilGoodsId , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
    	}
	}



	// 통합몰 문의관리 상세화면 호출
	function fnGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/qna/getQnaDetail',
			params  : {csQnaId : aMap.csQnaId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

	};

	// 외부몰 문의관리 판매처 상품상세페이지 호출
	function fnGridShopProductIdClick(){
		var aMap = aGrid.dataItem(aGrid.select());
        window.open( aMap.shopProductUrl, "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
	}

	// 외부몰 문의관리 외부몰 주문번호 주문상세페이지 호출
	function fnGridOdOrderIdClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		if(aMap.odid){
			window.open("#/orderMgm?orderMenuId=lnb916&odid="+ aMap.odid , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
		}
	}

	// 외부몰 문의관리 상세화면 호출
	function fnoutmallGridClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/qna/getOutmallQnaDetail',
			params  : {csOutmallQnaId : aMap.csOutmallQnaId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});

	};

	//외부몰문의 상세정보 팝업 내 판매처 상품코드 클릭 시
	$('#kendoPopup').on("click", "#outmallQnaShopProductUrl", function(e) {
		e.preventDefault();
		let shopProductUrl = $('#outmallQnaShopProductUrl').attr('value');
		window.open( shopProductUrl, "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
	});

	// ------------------------------- Grid End -------------------------------

	// ---------------Initialize Option Box Start
	// ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 문의유형
		if(fnIsProgramAuth("PRODUCTQNA") && fnIsProgramAuth("CUSTOMERQNA")){
			fnTagMkChkBox({
	            id : "qnaType",
	            url : "/admin/comn/getCodeList",
	            tagId : "qnaType",
	            async : false,
	            style : {},
	            params : {"stCommonCodeMasterCode" : "QNA_TP", "useYn" : "Y"},
	            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
	        });

			// 문의분류 (상품)
			fnKendoDropDownList({
				id  : 'goodsType',
				tagId : 'goodsType',
				url   : "/admin/comn/getCodeList",
				params : {"stCommonCodeMasterCode" : "QNA_PRODUCT_TP", "useYn" :"Y"},
				textField :"NAME",
				valueField : "CODE",
				value : "",
				blank : "전체"
			});


			// 문의분류 (1:1)
			fnKendoDropDownList({
				id  : 'qnaOneType',
				tagId : 'qnaOneType',
				url   : "/admin/comn/getCodeList",
				params : {"stCommonCodeMasterCode" : "QNA_ONETOONE_TP", "useYn" :"Y"},
				textField :"NAME",
				valueField : "CODE",
				value : "",
				blank : "전체"
			});


		} else if(fnIsProgramAuth("PRODUCTQNA") || fnIsProgramAuth("CUSTOMERQNA")){
			var _title, _value;
			if(fnIsProgramAuth("PRODUCTQNA")){
				_title = "상품문의";
				_value = "QNA_TP.PRODUCT";

				$('#goodsType').show();
				$('#qnaOneType').hide();

				$('#qnaType').val(_value);

				// 문의분류 (상품)
				fnKendoDropDownList({
					id  : 'goodsType',
					tagId : 'goodsType',
					url   : "/admin/comn/getCodeList",
					params : {"stCommonCodeMasterCode" : "QNA_PRODUCT_TP", "useYn" :"Y"},
					textField :"NAME",
					valueField : "CODE",
					value : "",
					blank : "전체"
				});

			} else {
				_title = "1:1문의";
				_value = "QNA_TP.ONETOONE";

				$('#goodsType').hide();
				$('#qnaOneType').show();

				$('#qnaType').val(_value);

				// 문의분류 (1:1)
				fnKendoDropDownList({
					id  : 'qnaOneType',
					tagId : 'qnaOneType',
					url   : "/admin/comn/getCodeList",
					params : {"stCommonCodeMasterCode" : "QNA_ONETOONE_TP", "useYn" :"Y"},
					textField :"NAME",
					valueField : "CODE",
					value : "",
					blank : "전체"
				});

			}
			$('#goodsTypeTitle').html("문의분류");
			$('#qnaType').html(_title + "<input type='hidden' name='qnaType' value='"+_value+"'>");
		}else if(!fnIsProgramAuth("PRODUCTQNA") && !fnIsProgramAuth("CUSTOMERQNA")){
			$('#searchQnaTypeDiv').hide();
		}

		if(gbMallTp == 'MALL_TP.MALL') {
			// 처리상태
			fnTagMkChkBox({
				id : "qnaStatus",
				url : "/admin/comn/getCodeList",
				tagId : "qnaStatus",
				async : false,
				style : {},
				params : {"stCommonCodeMasterCode" : "QNA_STATUS", "useYn" : "Y", "attr2" : "MALL"},
				beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
			});

			// 통합몰문의 복수검색조건
			fnKendoDropDownList({
				id    : 'searchManySelect',
				data  : [{"CODE":"SEARCH_SELECT.TITLE","NAME":'문의제목'}
						,{"CODE":"SEARCH_SELECT.QUESTION","NAME":'문의내용'}
						,{"CODE":"SEARCH_SELECT.ANSWER","NAME":'답변내용'}
						,{"CODE":"SEARCH_SELECT.USER_ID","NAME":'고객ID'}
						,{"CODE":"SEARCH_SELECT.USER_NAME","NAME":'고객명'}
						]
			});

		} else if(gbMallTp == 'MALL_TP.OUTSOURCE') {
			// 문의채널
			fnTagMkChkBox({
				id : "qnaChannel",
				url : "/admin/customer/qna/getOutmallNameList",
				tagId : "qnaChannel",
				async : false,
				style : {},
				textField :"NAME",
				valueField : "CODE",
				beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
			});

			// 전체선택 클릭
			$("#qnaChannel_0").click(function(){
				if($('#qnaChannel_0').prop('checked') == true) {
					$('input[name=qnaChannel]').prop('checked', true);
				} else {
					$('input[name=qnaChannel]').prop('checked', false);
				}
			});

			$("input[name=qnaChannel]").click(function() {
				if ($('#qnaChannel_0').prop('checked') == false && $("input[name^='qnaChannel']:checked").length >= $("input[name^='qnaChannel']").length -1) {
					$("input[name=qnaChannel]").eq(0).prop("checked", true);
				} else if($('#qnaChannel_0').prop('checked') == true && $("input[name^='qnaChannel']:checked").length <= $("input[name^='qnaChannel']").length -1){
					$("input[name=qnaChannel]").eq(0).prop("checked", false);
				}
			});

			// 외부몰문의 문의분류 (주문/배송 고정값)
			fnKendoDropDownList({
				id    : 'outmallQnaType',
				tagId : 'outmallQnaType',
				url   : "/admin/comn/getCodeList",
				params : {"stCommonCodeMasterCode" : "QNA_OUTMALL_TP", "useYn" :"Y"},
				textField :"NAME",
				valueField : "CODE",
				value : "QNA_OUTMALL_TP.ORDER_SHIPPING",
				blank : "전체"
			});

			// 처리상태
			fnTagMkChkBox({
				id : "outmallQnaStatus",
				url : "/admin/comn/getCodeList",
				tagId : "outmallQnaStatus",
				async : false,
				style : {},
				params : {"stCommonCodeMasterCode" : "QNA_STATUS", "useYn" : "Y", "attr1" : "outmall"},
				beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
			});

			// 처리불가여부
			fnTagMkChkBox({
				  id    : "searchOutmallQnaProcYn"
			    , tagId : "searchOutmallQnaProcYn"
			    , data  : [ { "CODE" : "ALL", "NAME" : "전체" },
			    			{ "CODE" : "Y", "NAME" : "처리가능" },
						  	{ "CODE" : "N", "NAME" : "처리불가" }
						  ]
			});

			// 외부몰문의 복수검색조건
			fnKendoDropDownList({
				id    : 'searchManySelect',
				data  : [{"CODE":"SEARCH_SELECT.TITLE","NAME":'문의제목'}
						,{"CODE":"SEARCH_SELECT.QUESTION","NAME":'문의내용'}
						,{"CODE":"SEARCH_SELECT.ANSWER","NAME":'답변내용'}
						]
			});
		}

		// 통합몰문의 단일조건
		fnKendoDropDownList({
			id    : 'searchSelect',
			data  : [{"CODE":"SEARCH_SELECT.USER_NAME","NAME":'회원명'}
					,{"CODE":"SEARCH_SELECT.USE_ID","NAME":'회원ID'}
					]
		});

		// 통합몰문의 답변담당자검색조건
		fnKendoDropDownList({
			id    : 'searchAnswerSelect',
			data  : [{"CODE":"SEARCH_ANSWER.USER_NAME","NAME":'담당자명'}
					,{"CODE":"SEARCH_ANSWER.USER_ID","NAME":'담당자ID'}
					],
			blank : "전체"
		});

		fbCheckboxChange();

		// 답변방법
	    fnTagMkChkBox({
	        id    : "answerType"
	      , tagId : "answerType"
	      , data  : [ { "CODE" : "SMS", "NAME" : "SMS" },
	    	  		  { "CODE" : "EMAIL", "NAME" : "EMAIL" }]
	    });


	    // 공개여부
	    fnTagMkRadio({
			id    :  'secretType',
			tagId : 'secretType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "QNA_SECRET_TP", "useYn" :"Y"},
			async : false,
			chkVal: "QNA_SECRET_TP.OPEN",
			style : {},
			change : function(e){


				var data = $("#secretType").getRadioVal();

				switch(data){
					case "QNA_SECRET_TP.OPEN" :
						$("#secretType_0").prop("checked",true);
						$("#secretComment").hide();
						break;

					case "QNA_SECRET_TP.CLOSE_ADMIN" :
						$("#secretType_1").prop("checked",true);
						$("#secretComment").show();
						if($("#secretComment").val() != null){
							$("#secretType").attr("disabled", false);
						}
						break;

					case "QNA_SECRET_TP.CLOSE_CUSTOMER" :
						$("#secretType_2").prop("checked",true);
						$("#secretType").attr("disabled", false);
						$("#secretComment").hide();
						break;
				}

            }
		});


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
					$('#searchGoodsDiv').hide();
					$('#searchDateDiv').hide();
					$('#searchQnaTypeDiv').hide();
					$('#searchStatusDiv').hide();
					$('#searchManyDiv').hide();
					$('#searchWordDiv').hide();
					$('#searchQnaChannelDiv').hide();
					$('#outmallQnaTypeDiv').hide();
					$("#companyStandardType_0").prop("checked",true);

					$('#createDateStart').val('');
					$('#createDateEnd').val('');
					//fnSearch();

					$('#aGrid').gridClear(true);

				}else if($('#companyStandardType').getRadioVal() == "N"){
					$('#searchForm').formClear(true);
					fnDefaultSet();
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
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "createDateEnd",
            defType : 'yesterday'
        });

        // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "createDateEnd",
            defType : 'yesterday'
        });



        // 품목코드 호출 시 품목 상세페이지 새창 호출
        $("#itemDetailLink").on("click", function(e){
        	e.preventDefault();
        	window.open("#/itemMgmModify?ilItemCode="+ $('#itemCode').val() +"&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON","_blank","width=1800, height=1000, resizable=no, fullscreen=no");
        });

        // 회원ID 선택 시 호출
        $("#userIdLink").on("click", function(e){
            var urUserId = $('#urUserIdInfo').val();

                fnKendoPopup({
                            id: 'buyerPopup',
                            title: fnGetLangData({nullMsg: '회원상세'}),
                            param: {"urUserId": urUserId},
                            src: '#/buyerPopup',
                            width: '1200px',
                            height: '640px',
                            success: function (id, data) {

                            }
                        });
         });


        fnKendoUpload({ // 1차답변 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
            id : "uploadImageOfEditor",
            select : function(e) {

                if (e.files && e.files[0]) { // 이미지 파일 선택시

                    // UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
                    if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
                        fnKendoMessage({
                            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
                            ok : function(e) {}
                        });
                        return;
                    }

                    let reader = new FileReader();

                    reader.onload = function(ele) {
                        fnUploadImageOfEditor('1st'); // 선택한 이미지 파일 업로드 함수 호출
                    };

                    reader.readAsDataURL(e.files[0].rawFile);
                }
            }
        });

        fnKendoUpload({ // 2차답변 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
            id : "uploadImageOfEditor2",
            select : function(e) {

                if (e.files && e.files[0]) { // 이미지 파일 선택시

                    // UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
                    if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
                        fnKendoMessage({
                            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
                            ok : function(e) {}
                        });
                        return;
                    }

                    let reader = new FileReader();

                    reader.onload = function(ele) {
                        fnUploadImageOfEditor('2st'); // 선택한 이미지 파일 업로드 함수 호출
                    };

                    reader.readAsDataURL(e.files[0].rawFile);
                }
            }
        });


	    fnKendoUpload({
	        id     : 'fileUpload',
	        template: "<div><span>파일명: #=name# &nbsp; (Size: #= kendo.toString(size, 'n0') # Bytes)</p>" +
			            "<strong class='k-upload-status'>" +
			            "<button type='button' class='k-upload-action'></button>" +
			            "<button type='button' class='k-upload-action'></button>" +
			            "</strong>" +
			            "</div>",
	        select : function(e){
	            var f = e.files[0];
	            var ext = f.extension.substring(1, f.extension.length);
	            if($.inArray(ext.toLowerCase(), ['js','html','htm','jsp','exe','cgi']) > -1){
	            	fnKendoMessage({ message : "js, html, htm, jsp, exe, cgi 확장자는 업로드 불가한 확장자입니다."});
	                e.preventDefault();
	                return false;
	            }
	            else{
	            	if (typeof(window.FileReader) == 'undefined'){
	                	//$('#photo').attr('src', e.sender.element.val());
	                }
	                else {
	                    if(f){
	                        var reader = new FileReader();
	                        reader.onload = function (ele) {
	                            //$('#photo').attr('src', ele.target.result);
	                        	$("ul.k-upload-files.k-reset").css({"display":"block"});
	                        	$("#csBbsAttcDelYn").val("");
	                        };
	                        reader.readAsDataURL(f.rawFile);
	                    }
	                }
	            }
	        },
	        localization : '파일첨부'
	    });

	    fnKendoDropDownList({
            id : "ecsCtgryStd1",
            tagId : "ecsCtgryStd1",
            url : "/admin/comn/getEcsCodeList",
            params : { "hdBcode" : "", "hdScode" : "" },
            textField : "NAME",
            valueField : "CODE",
            blank : "대분류",
            async : false
        });


	    fnKendoDropDownList({
	    	id : "ecsCtgryStd2",
            tagId : "ecsCtgryStd2",
            url : "/admin/comn/getEcsCodeList",
            textField : "NAME",
            valueField : "CODE",
            blank : "중분류",
            async : false,
            cscdId : "ecsCtgryStd1",
            cscdField : "hdBcode"
        });


	    $('#ecsCtgryStd1').unbind('change').on('change', function(){
			var ecsCtgryStd1DownList =$('#ecsCtgryStd1').data('kendoDropDownList');
			ecsCtgryStd1 = ecsCtgryStd1DownList._old;
			deptCode = ecsCtgryStd1DownList._old;

			let str = '/admin/comn/getEcsCodeList?';
			$("#ecsCtgryStd3").data("kendoDropDownList").dataSource.transport.options.read.url = str +  "hdBcode=" + deptCode;
		});

        $('#ecsCtgryStd2').unbind('change').on('change', function(){
			var ecsCtgryStd1DownList =$('#ecsCtgryStd2').data('kendoDropDownList');
			ecsCtgryStd2 = ecsCtgryStd1DownList._old;
		});

	    fnKendoDropDownList({
            id : "ecsCtgryStd3",
            tagId : "ecsCtgryStd3",
            url : "/admin/comn/getEcsCodeList?",
            textField : "NAME",
            valueField : "CODE",
            blank : "소분류",
            async : false,
            cscdId : "ecsCtgryStd2",
            cscdField : "hdScode"
        });


	    $('#ecsCtgryStd3').unbind('change').on('change', function(){
			var ecsCtgryStd1DownList =$('#ecsCtgryStd3').data('kendoDropDownList');
			ecsCtgryStd3 = ecsCtgryStd1DownList._oldText;
		});


	    //  상품문의 상세 페이지 호출
        $("#productLink").on("click", function(e){
        	e.preventDefault();
        	var goodsTp = $('#goodsTp').val();
        	var goodsUrl;
        	if(goodsTp == 'GOODS_TYPE.ADDITIONA'){
    			goodsUrl = "#/goodsAdditional?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.DAILY'){
    			goodsUrl = "#/goodsDaily?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.DISPOSAL'){
    			goodsUrl = "#/goodsDisposal?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.GIFT' || goodsTp == 'GOODS_TYPE.GIFT_FOOD_MARKETING'){
    			goodsUrl = "#/goodsAdditional?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.INCORPOREITY'){
                goodsUrl = "#/goodsIncorporeal?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.NORMAL'){
    			goodsUrl = "#/goodsMgm?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.PACKAGE'){
    			goodsUrl = "#/goodsPackage?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.RENTAL'){
    			goodsUrl = "#/goodsRental?ilGoodsId="
    		}else if(goodsTp == 'GOODS_TYPE.SHOP_ONLY'){
    			goodsUrl = "#/goodsShopOnly?ilGoodsId="
    		}

        	window.open(goodsUrl+ $('#ilGoodsId').val() , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
        });

        // 1:1문의 상세 페이지 호출
        $("#oneProductLink, #productNameLink").on("click", function(e){
        	e.preventDefault();
        	var goodsTp = $('#goodsTp').val();
            var goodsUrl;
            if(goodsTp == 'GOODS_TYPE.ADDITIONA'){
                goodsUrl = "#/goodsAdditional?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.DAILY'){
                goodsUrl = "#/goodsDaily?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.DISPOSAL'){
                goodsUrl = "#/goodsDisposal?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.GIFT' || goodsTp == 'GOODS_TYPE.GIFT_FOOD_MARKETING'){
                goodsUrl = "#/goodsAdditional?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.INCORPOREITY'){
                goodsUrl = "#/goodsIncorporeal?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.NORMAL'){
                goodsUrl = "#/goodsMgm?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.PACKAGE'){
                goodsUrl = "#/goodsPackage?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.RENTAL'){
                goodsUrl = "#/goodsRental?ilGoodsId="
            }else if(goodsTp == 'GOODS_TYPE.SHOP_ONLY'){
                goodsUrl = "#/goodsShopOnly?ilGoodsId="
            }
        	window.open( goodsUrl+ $('#oneIlGoodsId').val() , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
        });


        // 주문 상세 페이지 호출
        $("#orderDetailLink").on("click", function(e){

        	e.preventDefault();
        	window.open("#/orderMgm?orderMenuId=lnb916&odid="+ $('#odid').val() , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");

        });


	}

	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------

function fnUploadImageOfEditor(type) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

	var formName;
    var formData;
    if(type == '1st'){
    	formName = "uploadImageOfEditorForm";
    	formData = $('#uploadImageOfEditorForm').formSerialize(true);
    }else{
    	formName = "uploadImageOfEditorForm2"
    	formData = $('#uploadImageOfEditorForm2').formSerialize(true);
    }

    fnAjaxSubmit({
        form : formName,
        fileUrl : "/fileUpload",
        method : 'GET',
        url : '/comn/getPublicStorageUrl',
        storageType : "public",
        domain : "cs",
        params : formData,
        success : function(result) {

            var uploadResult = result['addFile'][0];
            var serverSubPath = uploadResult['serverSubPath'];
            var physicalFileName = uploadResult['physicalFileName'];
            var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

            var editor = $('#' + workindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
            editor.exec('inserthtml', {
            	 value : '<img src="' + imageSrcUrl + '"/>'
            });

        },
        isAction : 'insert'
    });

}

	function replaceAll(str, searchStr, replaceStr) {
	  return str.split(searchStr).join(replaceStr);
	}


	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':

				$('#inputForm').formClear(true);
				var winMsg;

				if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리
					data.row.qnaType == 'QNA_TP.PRODUCT' ? winMsg = '상품문의 상세 정보' : winMsg = '1:1문의 상세 정보';
				}else if(gbMallTp == 'MALL_TP.OUTSOURCE') { //외부몰 문의관리
					winMsg = '외부몰문의 상세 정보';
				}

				var mobile = kendo.htmlEncode(data.row.userMobile);
				mobile = fnPhoneNumberHyphen(mobile);

				data.row.userMobile = mobile;

				fnKendoInputPoup({height:"auto" ,width:"800px",title:{ nullMsg : winMsg} });

				if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리

					$('#csOutmallQnaIdDiv').hide();
					$("#outmallQnaInfoDiv").hide();
					$("#customerQnaInfoDiv1").show();
					$("#customerQnaInfoDiv2").show();
					$("#customerQnaInfoDiv3").show();
					$("#customerQnaInfoDiv3").show();

					//답변
					$('#answer1StDiv').show();
					$('#answer2StDiv').show();
					$('#outmallQnaAnswerDiv').hide();

					if(data.row.qnaType == 'QNA_TP.PRODUCT'){
						$("#fileDiv").hide();
						$("#productAnswerTitleDiv").show();
						$("#productAnswerDiv").show();
						$("#OnetoAnswerTitleDiv").hide();
						$("#productNameDiv").hide();
						$("#onetooneDiv").hide();
					}else{
						$("#fileDiv").show();
						$("#productAnswerTitleDiv").hide();
						$("#productAnswerDiv").hide();
						$("#OnetoAnswerTitleDiv").show();
						$("#productNameDiv").show();
						$("#onetooneDiv").show();
					}

				}else if(gbMallTp == 'MALL_TP.OUTSOURCE') { //외부몰 문의관리

						$('#csOutmallQnaIdDiv').show();
						$("#customerQnaInfoDiv1").hide();
						$("#customerQnaInfoDiv2").hide();
						$("#customerQnaInfoDiv3").hide();
						$("#outmallQnaInfoDiv").show();
						$("#fileDiv").hide();
						$("#productAnswerTitleDiv").hide();
						$("#productAnswerDiv").hide();
						$("#OnetoAnswerTitleDiv").hide();
						$("#onetooneDiv").hide();

						//답변
						$('#answer1StDiv').hide();
						$('#answer2StDiv').hide();
						$('#outmallQnaAnswerDiv').show();
				}


				$('#oneIlGoodsId').val(data.row.ilGoodsId);

				$('#kendoPopup').scrollTop(0);

				$('#inputForm').bindingForm(data, "row", true);

				if(data.row.secretType == 'QNA_SECRET_TP.OPEN'){
					$("#secretType_0").prop("checked",true);
					$("#secretComment").hide();
					$("input:radio[name='secretType']").attr('disabled',false);
					$("#secretComment").attr('disabled',false);
				}else if(data.row.secretType == 'QNA_SECRET_TP.CLOSE_ADMIN'){
					$("#secretType_1").prop("checked",true);
					$("#secretComment").show();

					if($("#secretComment").val() != null){
						$("input:radio[name='secretType']").attr('disabled',true);
						$("#secretComment").attr('disabled',true);
					}else{

					}
				}else if(data.row.secretType == 'QNA_SECRET_TP.CLOSE_CUSTOMER'){
					$("#secretType_2").prop("checked",true);
					$("#secretComment").hide();
					$("input:radio[name='secretType']").attr('disabled',true);
					$("#secretComment").attr('disabled',false);
				}

				$("#answerType_0").attr("disabled",true);
				$("#answerType_1").attr("disabled",true);

                $('#urUserIdInfo').val(data.row.urUserId);

				if(data.row.answerSmsYn == 'Y'){
					$("#answerType_0").prop("checked",true);
				}else{
					$("#answerType_0").prop("checked",false);
				}

				if(data.row.answerMailYn == 'Y'){
					$("#answerType_1").prop("checked",true);
				}else{
					$("#answerType_1").prop("checked",false);
				}


				if(data.row.odOrderId == null){
					$('#odOrderId').hide();
					$('#orderDetailLink').hide();
					$('#orOrderNone').html('정보없음');
				}else{
					$('#odOrderId').show();
					$('#orderDetailLink').show();
					$('#orOrderNone').hide();
					$('#orOrderNone').html('');
				}

				if(data.row.ilGoodsId == null){
					$('#oneIlGoodsId').hide();
					$('#oneProductLink').hide();
					$('#oneIlGoodsIdNone').html('정보없음');
				}else{
					$('#oneIlGoodsId').show();
					$('#oneProductLink').show();
					$('#oneIlGoodsIdNone').html('');
					$('#oneIlGoodsIdNone').hide();
				}



				if(data.row.status == 'QNA_STATUS.RECEPTION'){  					// 접수 (통합몰 & 외부몰 문의관리)
					$('#fnAnswer').show();
					$('#ecsDiv').hide();
					$('#answerDiv').hide();

					//외부몰문의관리 & 처리불가 시 답변진행 버튼 비활성화
					if(gbMallTp == 'MALL_TP.OUTSOURCE' && data.row.procYn == 'N') {
						$('#fnAnswer').prop("disabled", true);
					}
					$('#fnConfirm').prop("disabled", false);

				}else if(data.row.status == 'QNA_STATUS.ANSWER_CHECKING' && gbMallTp == 'MALL_TP.MALL'){	// 답변확인중 (통합몰 문의관리)
					$('#ecsDiv').show();
					$('#answerDiv').show();
					$('#answer1StDiv').show();
					$('#answer2StDiv').hide();
					$('#fnAnswer').hide();
					var firstStr = document.getElementById("firstContent").value;
					firstStr = replaceAll(firstStr, "<br/>", "\r\n");
					document.getElementById("firstContent").value = firstStr;
					$('#firstContent').attr("disabled",false);
					$('#firstAnswerDateTitle').hide();
					$('#firstUserInfoTitle').hide();

					// 외부몰문의관리
					$('#outmallQnaAnswerDiv').hide();
					$('#answerDiv > div').eq(0).text("답변 등록");

				}else if(data.row.status == 'QNA_STATUS.ANSWER_CHECKING' && gbMallTp == 'MALL_TP.OUTSOURCE'){	// 답변확인중 (외부몰 문의관리)
					$('#ecsDiv').show();
					$('#answerDiv').show();
					$('#answer2StDiv').hide();
					$('#fnAnswer').hide();
					$('#answer1StDiv').hide();
					$('#outmallQnaAnswerContent').val(data.row.outmallQnaAnswerContent.replace(/<br\s?\/?>/g,"\n"));
					$('#outmallQnaAnswerContent').attr("disabled",false);

					$('#outmallQnaAnswerDateTitle').hide();
					$('#outmallQnaUserInfoTitle').hide();

					// 외부몰문의관리
					$('#outmallQnaAnswerDiv').show();
					$('#answerDiv > div').eq(0).text("답변 등록");

					// 처리불가 시 수정 불가능
					if(data.row.procYn == 'N') {
						$('#fnConfirm').prop("disabled", true);
					}
					$('#fnConfirm').prop("disabled", false);

				}else if(data.row.status == 'QNA_STATUS.ANSWER_COMPLETED_1ST' && gbMallTp == 'MALL_TP.MALL'){		//1차 답변완료 (통합몰 문의관리)
					$('#ecsDiv').show();
					$('#answerDiv').show();
					$('#answer1StDiv').show();
					$('#answer1StWriteDiv').show();
					$('#firstContent').val(data.row.firstContent.replace(/<br\s?\/?>/g,"\n"));
					$('#firstContent').attr("disabled",true);
					$('#answer2StDiv').show();
					$('#fnAnswer').hide();

					$('#firstAnswerDateTitle').show();
					$('#firstUserInfoTitle').show();

					$('#secondAnswerDateTitle').hide();
					$('#secondUserInfoTitle').hide();

					// 외부몰문의관리
					$('#outmallQnaAnswerDiv').hide();
					$('#answerDiv > div').eq(0).text("답변 완료");

					var secondStr = document.getElementById("secondContent").value;
					secondStr = replaceAll(secondStr, "<br/>", "\r\n");
					document.getElementById("secondContent").value = secondStr;

				}else if(data.row.status == 'QNA_STATUS.ANSWER_COMPLETED_2ND' && gbMallTp == 'MALL_TP.MALL'){		//2차 답변완료 (통합몰 문의관리)
					$('#ecsDiv').show();
					$('#answerDiv').show();

					$('#answer1StDiv').show();
					$('#answer1StWriteDiv').show();
					$('#firstContent').val(data.row.firstContent.replace(/<br\s?\/?>/g,"\n"));
					$('#firstContent').attr("disabled",true);

					$('#firstAnswerDateTitle').show();
					$('#firstUserInfoTitle').show();

					$('#answer2StDiv').show();
					$('#answer2StWriteDiv').show();

					$('#secondContent').val(data.row.secondContent.replace(/<br\s?\/?>/g,"\n"));
					$('#secondContent').attr("disabled",true);

					$('#secondAnswerDateTitle').show();
					$('#secondUserInfoTitle').show();
					$('#fnAnswer').hide();

					// 외부몰문의관리
					$('#outmallQnaAnswerDiv').hide();
					$('#answerDiv > div').eq(0).text("답변 완료");

				} else if(data.row.status == 'QNA_STATUS.ANSWER_COMPLETED' && gbMallTp == 'MALL_TP.OUTSOURCE'){	  // 답변완료 (외부몰 문의관리)
					$('#ecsDiv').show();
					$('#answerDiv').show();
					$('#answer1StDiv').hide();
					$('#answer2StDiv').hide();

					let outmallQnaAnswerContent = data.row.outmallQnaAnswerContent;
					outmallQnaAnswerContent = outmallQnaAnswerContent.replace(/&nbsp;/g, ' ').replace(/<br\s?\/?>|\/br|&lt;BR\/&gt;/g,"\n");

					$('#outmallQnaAnswerContent').val(outmallQnaAnswerContent);
					$('#outmallQnaAnswerContent').attr("disabled",true);
					$('#outmallQnaAnswerDate').val(fnNvl(data.row.outmallQnaAnswerDate)).attr('readonly', true);
					$('#outmallQnaUserInfo').val(fnNvl(data.row.firstUserInfo)).attr('readonly', true);
					$('#fnAnswer').hide();
					$('#outmallQnaAnswerDateTitle').show();
					$('#outmallQnaUserInfoTitle').show();

					// 외부몰문의관리
					$('#outmallQnaAnswerDiv').show();
					$('#answerDiv > div').eq(0).text("답변 완료");

					// 처리불가 or 이지리플 상태가 전송완료(3) 시 수정 불가능
					if(data.row.procYn == 'N' || data.row.easyadminStatus == 3) {
						$('#fnConfirm').prop("disabled", true);
					}

					//답변날짜, 답변담당자 데이터가 없을 경우
					if(fnIsEmpty(data.row.outmallQnaAnswerDate) || fnIsEmpty(data.row.firstUserInfo)){
						$('#outmallQnaAnswerUserInfoDiv').hide();
					}
				}

				$('#secretType').val();
				oldDisplayType = data.row.secretType;
				oldEcsType1 = data.row.ecsCtgryStd1;
				oldEcsType2 = data.row.ecsCtgryStd2;
				oldEcsType3 = data.row.ecsCtgryStd3;
				oldFirstAnswer = data.row.firstContent;
				oldSecondAnswer = data.row.secondContent;
				oldoutmallQnaAnswer = fnNvl(data.row.outmallQnaAnswerContent);

				if(data.row.ecsCtgryStd3 != '' && data.row.ecsCtgryStd3 != null){
					$("#ecsCtgryStd3").data("kendoDropDownList").dataSource.transport.options.read.url = '/admin/comn/getEcsCodeList?hdBcode='+data.row.ecsCtgryStd1;
				}else{
					$("#ecsCtgryStd3").data("kendoDropDownList").dataSource.transport.options.read.url = '/admin/comn/getEcsCodeList?';
				}

				if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리
					fnAjax({
					url		: '/admin/customer/qna/getImageList',
					params	: {csQnaId : data.row.csQnaId},
					isAction : 'select',
					success	:
						function( data ){
							var imageHtml = "";
							if(data.rows.length>0){
								imageHtml += "			<div style='position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;'>";
								for(var i=0; i < data.rows.length; i++){

									var imageUrl = publicStorageUrl + data.rows[i].imagePath;
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
				}

				var qnaIdInfo = document.getElementById("csQnaId");
				var qnaIdResize = qnaIdInfo.value.length + 2;
				qnaIdInfo.setAttribute('size', qnaIdResize);

				var firstStuserInfo = document.getElementById("firstUserInfo");
				var firstResize = firstStuserInfo.value.length + 2;
				firstStuserInfo.setAttribute('size', firstResize);

				var secondStuserInfo = document.getElementById("secondUserInfo");
				var secondResize = secondStuserInfo.value.length + 2;
				secondStuserInfo.setAttribute('size', secondResize);

				// 외부몰 문의관리 상세정보 데이터 set
				if(gbMallTp == 'MALL_TP.MALL') { //통합몰 문의관리

					$('#csQnaId').val(data.row.csQnaId).attr('readonly', true);

				}else if(gbMallTp == 'MALL_TP.OUTSOURCE') { //외부몰 문의관리

					let outmallQnaShopProductIdStr =  "<div id='outmallQnaShopProductUrl' style='cursor: pointer; text-decoration: underline; color: #0000ff;' value='"+data.row.shopProductUrl+"'>"
													+data.row.shopProductId+ "</div>";
					let easyadminStatusStr = '['+data.row.easyadminStatus+':';
						switch(data.row.easyadminStatus){
							case 0 :
								easyadminStatusStr += '답변대기';
								break;
							case 1 :
								easyadminStatusStr += '답변입력';
								break;
							case 2 :
								easyadminStatusStr += '전송실패';
								break;
							case 3 :
								easyadminStatusStr += '전송완료';
								break;
						}
						easyadminStatusStr += ']';
					let procYn = data.row.procYn == 'Y' || data.row.procYn == '' ? '처리가능' : '처리불가';
					let question = data.row.question;
					question = question.replace(/&nbsp;/g, ' ').replace(/<br\s?\/?>|\/br|&lt;BR\/&gt;/g,"\n");
					let odOrderId = data.row.odOrderId;
					odOrderId = (odOrderId == "0" || odOrderId == "-") ? '' : odOrderId;

					$('#csOutmallQnaId').text(data.row.csOutmallQnaId).attr('readonly', true);
					$('#csOutmallQnaSeq').text(data.row.csOutmallQnaSeq).attr('readonly', true);
					$('#outmallQnaChannel').val(data.row.saleChannelNm).attr('readonly', true);
					$('#outmallQnaTp').val(data.row.outmallTypeName).attr('readonly', true);
					$('#outmallQnaOdOrderId').val(odOrderId).attr('readonly', true);
					$('#outmallQnaShopProductId').html(outmallQnaShopProductIdStr).attr('readonly', true);
					$('#outmallQnaTitle').val(data.row.title).attr('readonly', true);
					$('#outmallQnaContent').val(question).attr('readonly', true);
					$('#outmallQnaAnswerDelayYn').val(data.row.answerDelayYn).attr('readonly', true);
					$('#outmallQnaContentStatusName').val(data.row.qnaStatusName).attr('readonly', true);
					$('#outmallQnaQuestionCreateDate').val(data.row.questionCreateDate).attr('readonly', true);
					$('#outmallQnaAnsweredDate').val(fnNvl(data.row.answeredDate)).attr('readonly', true);
					$('#outmallQnaEasyadminStatus').val(easyadminStatusStr).attr('readonly', true);
					$('#outmallQnaEasyadminIfDate').val(fnNvl(data.row.easyadminIfDt)).attr('readonly', true);
					$('#outmallQnaCollectionMallId').val(fnNvl(data.row.collectionMallId)).attr('readonly', true);
					$('#outmallQnaProcYn').val(procYn).attr('readonly', true);

				}

				break;

			case 'confirm':
				fnKendoMessage({
					message:"저장되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
			});

				break;

			case 'fail':
					fnClose();
					if(gbMallTp == 'MALL_TP.MALL') {
						fnAjax({
							url     : '/admin/customer/qna/getQnaDetail',
							params  : {csQnaId : csQnaId},
							success :
								function( data ){
									fnBizCallback("select",data);
								},
							isAction : 'select'
						});
					} else if(gbMallTp == 'MALL_TP.OUTSOURCE') {
						fnAjax({
							url     : '/admin/customer/qna/getOutmallQnaDetail',
							params  : {csOutmallQnaId : $('#csOutmallQnaId').text()},
							success :
								function( data ){
									fnBizCallback("select",data);
								},
							isAction : 'select'
						});
					}


				break;
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
	/** Common Answer */
	$scope.fnAnswer = function(url){ fnAnswer();};
	/** Common ShowImage */
	$scope.fnShowImage = function(url){ fnShowImage(url);};


	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });

	// 주문 조회 클릭시 새창 호출
	$("#fnOrderSearch").on("click", function(e){
		e.preventDefault();

		fnGetBuyer();
		window.open("#/csOrderList","_blank","width=1800, height=1000, top=10, resizable=yes, fullscreen=no");
	});

	// 문의 조회 클릭시 새창 호출
	$("#fnQnaSearch").on("click", function(e){
		e.preventDefault();

		fnGetBuyer();
		window.open("#/csCustomerQna","_blank","width=1800, height=1000, top=10, resizable=yes, fullscreen=no");
	});

	function fnGetBuyer() {
		fnAjax({
			url		: '/admin/ur/buyer/getBuyer',
			params	: {'urUserId' : $('#urUserIdInfo').val()},
			isAction : 'select',
			success	:
				function( data ){

					let paramStr = new Object();
					paramStr.urUserId = data.rows.urUserId;
					paramStr.employeeYn = data.rows.employeeYn;
					paramStr.userName = data.rows.userName;
					paramStr.loginId = data.rows.loginId;
					paramStr.genderView = data.rows.genderView;
					paramStr.userTypeName = data.rows.userTypeName;
					paramStr.groupName = data.rows.groupName;
					paramStr.mail = data.rows.mail;
					paramStr.mobile = data.rows.mobile;
					paramStr.blackList = data.rows.blackList;

					let uri = JSON.stringify(paramStr);
					setCookie("csUserParamData", uri)
				}
		});
	}

	//쿠키값 Set
	function setCookie(cookieName, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
		document.cookie = cookieName + "=" + cookieValue;
	}


	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
