
/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 고객보상제 신청관리
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.15 	안치열 			최초생성 @
 ******************************************************************************/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();


var oldDisplayType;
var oldRewardPayCouponDetl;
var oldRewardPayPointDetl;
var oldRewardPayEtcDetl;
var oldAnswer;
var oldAdminCmt;
var csRewardApplyId;
var rewardPayTp;

var gbPageParam = '';
gbPageParam = fnGetPageParam();
var gbCsRewardId = '';
gbCsRewardId = gbPageParam.csRewardId;
$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'rewardApply',
			callback : fnUI
		});

		//지연건만 보기
		$('#ng-view').on("click", "#applyDelay" ,function(index){
			if($("#applyDelay").prop("checked")==true){
				$('#applyDelayView').val('Y');
				fnSearch();
			}else{
				$('#applyDelayView').val('N');
				fnSearch();
			}
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
		$('#fnSearch, #fnConfirm, #fnCancel, #fnClear, #fnClose, #fnShowImage, #fnApply, #fnShowImage, #fnExcel').kendoButton();
	}

	function fnSearch(){
		$('#inputForm').formClear(false);

		var data;
		data = $('#searchForm').formSerialize(true);

		if(!fnIsEmpty(gbCsRewardId)) { // csRewardId가 있으면 (고객보상제 리스트 > 신청내역)
			data.csRewardId = gbCsRewardId;
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
		$('input[name=applyDelay]').prop("checked",false);
		$('#applyDelayView').val('N');

        $("input[name=rewardStatus]").eq(0).prop("checked", true).trigger("change");
        setDefaultDatePicker();

	}

	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=rewardStatus]").eq(0).prop("checked", true).trigger("change");
		setDefaultDatePicker();

		if(gbCsRewardId != null){
		    $("#rewardNm").data('kendoDropDownList').value(gbCsRewardId);
            $("#rewardNm").data("kendoDropDownList").enable(false);
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


	// 확인 버튼 동작
	function fnConfirm(){

		if(fnDataCheck()){
			// 수정사항 발생 Case
			var rewardApplyResult;
            if($("#rewardApplyResult_0").prop("checked")){
                rewardApplyResult = 'REWARD_APPLY_STATUS.CONFIRM';
            }else if($("#rewardApplyResult_1").prop("checked")){
                rewardApplyResult = 'REWARD_APPLY_STATUS.COMPLETE';
            }else if($("#rewardApplyResult_2").prop("checked")){
                rewardApplyResult = 'REWARD_APPLY_STATUS.IMPOSSIBLE';
            }

            if(rewardApplyResult == 'REWARD_APPLY_STATUS.COMPLETE'){

                if(rewardPayTp == 'REWARD_PAY_TP.COUPON') {
                    if (fnIsEmpty(getValueItemForId('rewardPayCouponDetl')) == true) {
                          fnMessage('', '<font color="#FF1A1A">[지급내역 상세정보]</font> 필수 입력입니다.', 'rewardPayCouponDetl');
                          return false;
                    }else{
                        $('#rewardPayDetl').val($('#rewardPayCouponDetl').val());
                    }

                }else if(rewardPayTp == 'REWARD_PAY_TP.POINT') {
                    if (fnIsEmpty(getValueItemForId('rewardPayPointDetl')) == true) {
                          fnMessage('', '<font color="#FF1A1A">[지급내역 상세정보]</font> 필수 입력입니다.', 'rewardPayPointDetl');
                          return false;
                    }else{
                        $('#rewardPayDetl').val($('#rewardPayPointDetl').val());
                    }
                }else if(rewardPayTp == 'REWARD_PAY_TP.ETC') {
                    if (fnIsEmpty(getValueItemForId('rewardPayEtcDetl')) == true) {
                          fnMessage('', '<font color="#FF1A1A">[지급내역 상세정보]</font> 필수 입력입니다.', 'rewardPayEtcDetl');
                          return false;
                    }else{
                        $('#rewardPayDetl').val($('#rewardPayEtcDetl').val());
                    }
                }
            }

            if (($("#rewardApplyResult_1").prop("checked") || $("#rewardApplyResult_2").prop("checked") ) &&
                fnIsEmpty(getValueItemForId('answer')) == true) {
                  fnMessage('', '<font color="#FF1A1A">[처리사유]</font> 필수 입력입니다.', 'answer');
                  return false;
            }

			fnKendoMessage({message:'처리결과를 등록하시겠습니까? 처리하신 내용은 신청자에게 전송됩니다.', type : "confirm" ,ok : function(){ fnSave() } });
		}else{
			// 수정사항이 없는 Case
			fnClose();
		}
	}



	// 수정항목 변동사항 여부 확인
	function fnDataCheck(){

		var rewardApplyResult;
		if($("#rewardApplyResult_0").prop("checked")){
			rewardApplyResult = 'REWARD_APPLY_STATUS.CONFIRM';
		}else if($("#rewardApplyResult_1").prop("checked")){
			rewardApplyResult = 'REWARD_APPLY_STATUS.COMPLETE';
		}else if($("#rewardApplyResult_2").prop("checked")){
			rewardApplyResult = 'REWARD_APPLY_STATUS.IMPOSSIBLE';
		}
		if(oldDisplayType != rewardApplyResult){
			return true;
		}



		if(oldRewardPayCouponDetl != undefined && oldRewardPayCouponDetl != $('#rewardPayCouponDetl').val()){
		    return true;
		}

		if(oldRewardPayPointDetl != undefined && oldRewardPayPointDetl != $('#rewardPayPointDetl').val()){
            return true;
        }

        if(oldRewardPayEtcDetl != undefined && oldRewardPayEtcDetl != $('#rewardPayEtcDetl').val()){
		    return true;
		}

		if(oldAnswer != undefined && oldAnswer != $('#answer').val() && $('#answer').val() != ''){
			return true;
		}

		if( oldAdminCmt != undefined && oldAdminCmt != $('#adminCmt').val() && $('#adminCmt').val() != ''){
			return true;
		}

		return false
	}

	// 처리진행
	function fnApply(){

		fnKendoMessage({message:'고객에게 확인중으로 노출되며, 접수 상태로 되돌릴 수 없습니다. 신청건 처리를 진행하시겠습니까?', type : "confirm" ,ok : function(){ fnSetConfirmStatus() } });

	}

	// 확인중 상태 변경
	function fnSetConfirmStatus(){

		var url = '/admin/customer/reward/putRewardApplyConfirmStatus';
        var data = $('#inputForm').formSerialize(true);
		fnAjax({
			url     : url,
//			params  : {csRewardApplyId : $('#csRewardApplyId').val(), answerSmsYn : $('#answerSmsYn').val(), answerMailYn : $('#answerMailYn').val()},
			params  : data,
			success :
				function( data ){
                    $('#fnApply').hide();
                    $('#applyDiv').show();
                    $('#rewardPayDiv').hide();
                    $('#answerDiv').hide();
            //		$('#rewardApplyResult').val('REWARD_APPLY_STATUS.CONFIRM');
                    $("#rewardApplyResult_0").prop("checked",true);
                    $("#rewardApplyResult_0").attr("disabled",false);
                    $("#rewardApplyResult_1").attr("disabled",false);
                    $("#rewardApplyResult_2").attr("disabled",false);
                    $("#adminCmt").attr("disabled",false);
			},
			isAction : 'batch'
		});
	}

	function getValueItemForId(id) {
        return $('#'+id).val();
    }

    function fnMessage(key, nullMsg, ID){
        fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
          $('#'+ID).focus();
        }});
        return false;
    }

    // 보상불가 Case 확인중 변경 시
    function fnRewardConfirmChg(){
        fnKendoMessage({message:'이전 등록하신 처리 결과는 초기화 됩니다. 해당 신청 건 처리를 다시 진행하시겠습니까?', type : "confirm" ,
            ok : function(){
                $('#answer').val('');
                $('#adminCmt').val('');
                $('#rewardPayDetl').val('');
                $("#rewardPayCouponDetl").attr("disabled",false);
                $("#rewardPayPointDetl").attr("disabled",false);
                $("#rewardPayEtcDetl").attr("disabled",false);
                $("#answer").attr("disabled",false);
                $("#adminCmt").attr("disabled",false);
                $("#rewardApplyResult_2").attr("disabled",true);
                $('#answerDiv').hide();
         },
          cancel : function(){
                $("#rewardApplyResult_2").prop("checked",true);
          }});
    }

	// 확인 후 수정사항 저장
	function fnSave(){

		var url = '/admin/customer/reward/putRewardApplyInfo';

        if($('#rewardApplyResult').getRadioVal() == 'REWARD_APPLY_STATUS.IMPOSSIBLE'){
            $('#rewardPayDetl').val('');
        }

		var data = $('#inputForm').formSerialize(true);

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

    // 취소
    function fnCancel(){

        if(fnDataCheck()){
            // 수정사항 발생 Case
            fnKendoMessage({message:'입력하신 내용이 저장되지 않습니다. 취소하시겠습니까?', type : "confirm" ,ok : function(){ fnClose() } });
        }else{
            // 수정사항이 없는 Case
            fnClose();
        }
    }

	// 팝업창 닫기
	function fnClose(){

		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// 엑셀다운로드
    function fnExcel(){

        var data = $('#searchForm').formSerialize(true);
        fnExcelDownload('/admin/customer/reward/getRewardApplyListExportExcel', data);

    }


	// 이미지 팝업 호출
	function fnShowImage(imageUrl){

        fnKendoPopup({
              id      : 'rewardApplyPopup'
            , title   : '고객보상제 신청 첨부파일'
            , src     : '#/rewardApplyPopup'
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
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/customer/reward/getRewardApplyList",
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
                 { title:'No'	                                         , width:'50px' ,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'csRewardApplyId'	     ,title : '접수ID'        , width:'90px' ,attributes:{ style:'text-align:center' }}
				,{ field:'rewardNm'		         ,title : '보상제명'		 , width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'productInfo'		     ,title : '신청대상'		 , width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
				,{ field:'rewardApplyContent'	 ,title : '신청내용' 	 , width:'200px',attributes:{ style:'text-align:left;text-decoration: underline;color:blue;' }}
				,{ field:'userName'			     ,title : '회원명'		 , width:'70px' ,attributes:{ style:'text-align:center' }}
				,{ field:'loginId'			     ,title : '회원 ID'		 , width:'70px' ,attributes:{ style:'text-align:center' }}
				,{ field:'delayYn'			     ,title : '지연여부'		 , width:'70px' ,attributes:{ style:'text-align:center' },
					template : function(dataItem){
						let returnValue;
						if(dataItem.delayYn == 'N'){
							returnValue = "<span style='color:red;'>" + '지연' + "</span>";
						}else{
							returnValue = '정상';
						}
						return returnValue;
					}
				}
				,{ field:'createDate'            ,title : '신청일'		 , width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'            ,title : '처리날짜'		 , width:'100px',attributes:{ style:'text-align:center' }}
				,{ field:'rewardApplyStatusName' ,title : '처리상태'	     , width:'70px' ,attributes:{ style:'text-align:center' }}
				,{ field:'modifyUserName'	     ,title : '처리 담당자'	 , width:'90px' ,attributes:{ style:'text-align:center' },
				        template : function(dataItem){
                						let returnValue;
                							if(dataItem.rewardApplyStatus != 'REWARD_APPLY_STATUS.ACCEPT'){
                								returnValue =  dataItem.modifyUserName + "<br>" + "(" + dataItem.modifyId +")";
                							}else{
                								returnValue = '';
                							}
                						return returnValue;
                					}
				}
				,{ field:'rewardPayTpName'	     ,title : '지급유형'	     , width:'90px' ,attributes:{ style:'text-align:center' }}
				,{ field:'rewardPayDetl'	     ,title : '지급상세'	     , width:'90px' ,attributes:{ style:'text-align:center' },
				         template : function(dataItem){
                                        let returnValue;

                                        if(dataItem.rewardPayDetl=='' || dataItem.rewardPayDetl== null){
                                            returnValue = '';
                                        }else{
                                            if(dataItem.rewardPayTp == 'REWARD_PAY_TP.POINT'){
                                                returnValue =  fnNumberWithCommas(dataItem.rewardPayDetl) + "원";
                                            }else{
                                                returnValue = dataItem.rewardPayDetl;
                                            }
                                        }
                                        return returnValue;
                                    }
				}
				,{ field:'rewardApplyStatus', hidden:true}
				,{ field:'csRewardApplyId', hidden:true}
				,{ field:'rewardPayTp', hidden:true}
				,{ field:'ilGoodsId', hidden:true}
				,{ field:'odid', hidden:true}
				,{ field:'odOrderId', hidden:true}
				,{ field:'odOrderDetlId', hidden:true}
				,{ field:'rewardApplyStandard', hidden:true}

			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			// 신청대상 선택 (상품명 or 주문번호)
			if(index == 3){
				fnGridGoodsNameClick();
			}

			// 신청내용 선택
			if(index == 4){
				fnRewardClick();
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


    // 보상제명 선택 상세화면 호출
	function fnRewardNameClick(){
	    var aMap = aGrid.dataItem(aGrid.select());
	    window.open('#/rewardMgm?csRewardId=' + aMap.csRewardId + '&mode=update',"_blank","width=1800, height=1000, resizable=no, fullscreen=no");
	}

	// 상품명 선택 상세화면 호출
	function fnGridGoodsNameClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		var goodsUrl;

		if(aMap.rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_GOODS'){
		    if(aMap.ilGoodsId != '' && aMap.ilGoodsId != null){
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
		}else if(aMap.rewardApplyStandard == 'REWARD_APPLY_STANDARD.PACK_DELIVERY' || aMap.rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_NUMBER' ){
		    if(aMap.odid != '' && aMap.odid != null){
                let odid = aMap.odid
                var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
                window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);
		    }
		}

	}



	// 고객보상제 신청관리 상세화면 호출
	function fnRewardClick(){

		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/customer/reward/getRewardApplyDetail',
			params  : {csRewardApplyId : aMap.csRewardApplyId},
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

        // 보상제 리스트 조회
        fnKendoDropDownList({
            id    : 'rewardNm',
            url : "/admin/customer/reward/getRewardNmList",
            params : {"csRewardId" : gbCsRewardId},
            textField :"rewardNm",
            valueField : "csRewardId",
            blank : "전체",

        });


		// 처리상태
		fnTagMkChkBox({
            id : "rewardStatus",
            url : "/admin/comn/getCodeList",
            tagId : "rewardStatus",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "REWARD_APPLY_STATUS", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 보상구분
        fnTagMkChkBox({
            id : "qnaStatus",
            url : "/admin/comn/getCodeList",
            tagId : "qnaStatus",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "QNA_STATUS", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

		// 신청자 조회조건 단일조건
		fnKendoDropDownList({
			id    : 'searchRequestSelect',
			data  : [{"CODE":"SEARCH_SELECT.USER_NAME","NAME":'회원명'}
					,{"CODE":"SEARCH_SELECT.USER_ID","NAME":'회원ID'}
					],
			blank  : "선택"
		});


		// 처리담당자검색조건
		fnKendoDropDownList({
			id    : 'searchApplySelect',
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

	    // 처리결과:쿠폰
        fnTagMkRadio({
              id    : "rewardPayCopuon"
            , tagId : "rewardPayCopuon"
            , data  : [ { "CODE" : "REWARD_PAY_TP.COUPON",    "NAME" : "쿠폰" }]
        });

        // 처리결과:적립금
        fnTagMkRadio({
              id    : "rewardPayPoint"
            , tagId : "rewardPayPoint"
            , data  : [ { "CODE" : "REWARD_PAY_TP.POINT",    "NAME" : "적립금" }]
        });

        // 처리결과:기타
        fnTagMkRadio({
              id    : "rewardPayEtc"
            , tagId : "rewardPayEtc"
            , data  : [ { "CODE" : "REWARD_PAY_TP.ETC",    "NAME" : "기타" }]
        });

        // 처리결과
        fnTagMkRadio({
              id    : "rewardApplyResult"
            , tagId : "rewardApplyResult"
        	, data  : [ { "CODE" : "REWARD_APPLY_STATUS.CONFIRM",    "NAME" : "확인중" },
        	            { "CODE" : "REWARD_APPLY_STATUS.COMPLETE",   "NAME" : "보상완료" },
        	    	    { "CODE" : "REWARD_APPLY_STATUS.IMPOSSIBLE", "NAME" : "보상불가" }]
            , change : function(e){

                            var data = $("#rewardApplyResult").getRadioVal();

                            switch(data){
                                case "REWARD_APPLY_STATUS.CONFIRM" :
                                    if($('#rewardApplyStatus').val() == 'REWARD_APPLY_STATUS.IMPOSSIBLE'){
                                        fnRewardConfirmChg();
                                    }else{
                                        $("#rewardApplyResult_0").prop("checked",true);
                                        $("#rewardPayDiv").hide();
                                        $("#answerDiv").hide();
                                    }
                                    break;

                                case "REWARD_APPLY_STATUS.COMPLETE" :
                                    $("#rewardApplyResult_1").prop("checked",true);
                                    $("#rewardPayDiv").show();
                                    $("#answerDiv").show();
                                    $("#rewardPayCouponDetl").attr("disabled",false);
                                    $("#rewardPayPointDetl").attr("disabled",false);
                                    $("#rewardPayEtcDetl").attr("disabled",false);
                                    $("#answer").attr("disabled",false);
                                    $("#adminCmt").attr("disabled",false);
                                    break;

                                case "REWARD_APPLY_STATUS.IMPOSSIBLE" :
                                    $("#rewardApplyResult_2").prop("checked",true);
                                    $("#rewardPayDiv").hide();
                                    $("#answerDiv").show();
                                    $("#rewardPayCouponDetl").attr("disabled",false);
                                    $("#rewardPayPointDetl").attr("disabled",false);
                                    $("#rewardPayEtcDetl").attr("disabled",false);
                                    $("#answer").attr("disabled",false);
                                    $("#adminCmt").attr("disabled",false);
                                    break;
                            }

                        }
        });



		// 신청일 시작
        fnKendoDatePicker({
            id: "createDateStart",
            format: "yyyy-MM-dd",
            btnStartId: "createDateStart",
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "createDateEnd",
            defType : 'yesterday'
        });

        // 신청일 종료
        fnKendoDatePicker({
            id: "createDateEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createDateStart",
            defVal: fnGetDayMinus(fnGetToday(),1),
            btnEndId: "createDateEnd",
            defType : 'yesterday'
        });


        // 보상제 ID 페이지 호출
        $("#productLink").on("click", function(e){
            e.preventDefault();
        	window.open('#/rewardMgm?csRewardId=' + $('#csRewardId').val() + '&mode=update',"_blank","width=1800, height=1000, resizable=no, fullscreen=no");
        });


        // 보상신청대상
        $("#productInfoLink").on("click", function(e){
            e.preventDefault();
            var goodsTp = $('#goodsTp').val();
            var ilGoodsId = $('#ilGoodsId').val();
            var odid = $('#odid').val();
            var rewardApplyStandard = $('#rewardApplyStandard').val();

            var goodsUrl;

            if(rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_GOODS'){
                if(ilGoodsId != ''){
                    if(goodsTp == 'GOODS_TYPE.ADDITIONA'){
                        goodsUrl = "#/goodsAdditional?ilGoodsId="
                    }else if(goodsTp == 'GOODS_TYPE.DAILY'){
                        goodsUrl = "#/goodsDaily?ilGoodsId="
                    }else if(goodsTp == 'GOODS_TYPE.DISPOSAL'){
                        goodsUrl = "#/goodsDisposal?ilGoodsId="
                    }else if(goodsTp == 'GOODS_TYPE.GIFT' || goodsTp == 'GOODS_TYPE.GIFT_FOOD_MARKETING') {
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

                    window.open( goodsUrl + ilGoodsId , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
                }
            }else if(rewardApplyStandard == 'REWARD_APPLY_STANDARD.PACK_DELIVERY' || rewardApplyStandard == 'REWARD_APPLY_STANDARD.ORDER_NUMBER'){
                window.open("#/orderMgm?orderMenuId=lnb916&odid="+ odid , "_blank","width=1800, height=1000, resizable=no, fullscreen=no");
            }
        });

	}

	// ---------------Initialize Option Box End
	// ------------------------------------------------
	// ------------------------------- Common Function start
	// -------------------------------

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

				var mobile = kendo.htmlEncode(data.row.mobile);
				mobile = fnPhoneNumberHyphen(mobile);

				data.row.mobile = mobile;
				rewardPayTp = data.row.rewardPayTp;
				if(data.row.rewardPayTp == 'REWARD_PAY_TP.COUPON'){     //쿠폰
                    $('#rewardPayCopuon').show();
                    $('#rewardPayCouponDetl').show();
                    $('#rewardPayPoint').hide();
                    $('#rewardPayPointDetl').hide();
                    $('#rewardPayEtc').hide();
                    $('#rewardPayEtcDetl').hide();
                    $('#rewardPayCouponDetl').val(data.row.rewardPayDetl);
                }else if(data.row.rewardPayTp == 'REWARD_PAY_TP.POINT'){     //적립금
                    $('#rewardPayCopuon').hide();
                    $('#rewardPayCouponDetl').hide();
                    $('#rewardPayPoint').show();
                    $('#rewardPayPointDetl').show();
                    $('#rewardPayEtc').hide();
                    $('#rewardPayEtcDetl').hide();
                    $('#rewardPayPointDetl').val(data.row.rewardPayDetl);
                }else if(data.row.rewardPayTp == 'REWARD_PAY_TP.ETC'){     //기타
                    $('#rewardPayCopuon').hide();
                    $('#rewardPayCouponDetl').hide();
                    $('#rewardPayPoint').hide();
                    $('#rewardPayPointDetl').hide();
                    $('#rewardPayEtc').show();
                    $('#rewardPayEtcDetl').show();
                    $('#rewardPayEtcDetl').val(data.row.rewardPayDetl);
                }

				fnKendoInputPoup({height:"auto" ,width:"800px",title:{ nullMsg : '고객보상제 신청 상세'} });

				$('#kendoPopup').scrollTop(0);

				$('#inputForm').bindingForm(data, "row", true);

				$("#answerType_0").attr("disabled",true);
				$("#answerType_1").attr("disabled",true);



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

				if(data.row.delayYn == 'Y'){
				    $('#delayYn').val('정상');
				}else{
				    $('#delayYn').val('지연');
				}


				if(data.row.productInfo == null || data.row.productInfo == ''){
                    $('#productInfoLink').hide();
                }else{
                    $('#productInfoLink').show();
                }



                $('#rewardApplyStatus').val(data.row.rewardApplyStatus);
				if(data.row.rewardApplyStatus == 'REWARD_APPLY_STATUS.ACCEPT'){  				// 접수
					$('#fnApply').show();
					$('#applyDiv').hide();
				}else if(data.row.rewardApplyStatus == 'REWARD_APPLY_STATUS.CONFIRM'){			// 확인중
				    $("#rewardApplyResult_0").prop("checked",true);
				    $("#rewardApplyResult_0").attr("disabled",false);
				    $("#rewardApplyResult_1").attr("disabled",false);
				    $("#rewardApplyResult_2").attr("disabled",false);
				    $('#fnApply').hide();
                	$('#applyDiv').show();
                	$('#rewardPayDiv').hide();              // 지급내역
                	$('#answerDiv').hide();                 // 처리사유
				}else if(data.row.rewardApplyStatus == 'REWARD_APPLY_STATUS.COMPLETE'){		    // 보상완료
				    $("#rewardApplyResult_1").prop("checked",true);
				    $("#rewardApplyResult_0").attr("disabled",true);
				    $("#rewardApplyResult_2").attr("disabled",true);
					$('#fnApply').hide();
                    $('#applyDiv').show();
                    $('#rewardPayDiv').show();              // 지급내역
                    $('#answerDiv').show();                 // 처리사유
                    $("#rewardPayCouponDetl").attr("disabled",true);
                    $("#rewardPayPointDetl").attr("disabled",true);
                    $("#rewardPayEtcDetl").attr("disabled",true);
                    $("#answer").attr("disabled",true);
                    $("#adminCmt").attr("disabled",true);
				}else if(data.row.rewardApplyStatus == 'REWARD_APPLY_STATUS.IMPOSSIBLE'){		// 보상불가
				    $("#rewardApplyResult_2").prop("checked",true);
				    $("#rewardApplyResult_1").attr("disabled",true);
					$('#fnApply').hide();
                    $('#applyDiv').show();
                    $('#rewardPayDiv').hide();              // 지급내역
                    $('#answerDiv').show();                 // 처리사유
                    $("#rewardPayCouponDetl").attr("disabled",true);
                    $("#rewardPayPointDetl").attr("disabled",true);
                    $("#rewardPayEtcDetl").attr("disabled",true);
                    $("#answer").attr("disabled",true);
                    $("#adminCmt").attr("disabled",true);
				}

				oldDisplayType = data.row.rewardApplyStatus;
				if(data.row.rewardPayTp == 'REWARD_PAY_TP.COUPON'){
				    oldRewardPayCouponDetl = data.row.rewardPayDetl;
				}else if(data.row.rewardPayTp == 'REWARD_PAY_TP.POINT'){
				    oldRewardPayPointDetl = data.row.rewardPayDetl;
				}else if(data.row.rewardPayTp == 'REWARD_PAY_TP.ETC'){
                    oldRewardPayEtcDetl = data.row.rewardPayDetl;
                }
                oldAnswer = data.row.answer;
                oldAdminCmt = data.row.adminCmt;

				fnAjax({
					url		: '/admin/customer/reward/getImageList',
					params	: {csRewardApplyId : data.row.csRewardApplyId},
					isAction : 'select',
					success	:
						function( data ){
							var imageHtml = "";
							if(data.rows.length>0){
								imageHtml += "			<div style='position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;'>";
								for(var i=0; i < data.rows.length; i++){

									var imageUrl = publicStorageUrl + data.rows[i].imagePath + data.rows[i].imageName;
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

				var csRewardApplyIdInfo = document.getElementById("csRewardApplyId");
				var csRewardApplyIdResize = csRewardApplyIdInfo.value.length + 2;
				csRewardApplyIdInfo.setAttribute('size', csRewardApplyIdResize);

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
					fnAjax({
						url     : '/admin/customer/reward/getRewardApplyDetail',
						params  : {csRewardApplyId : csRewardApplyId},
						success :
							function( data ){
								fnBizCallback("select",data);
							},
						isAction : 'select'
					});

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
	/** Common Cancel */
	$scope.fnCancel = function(){	 fnCancel();};
	/** Common Close */
	$scope.fnClose = function( ){  fnClose();};
	/** Common ShowImage */
	$scope.fnShowImage = function(url){ fnShowImage(url);};
	/** Common Answer */
	$scope.fnApply = function(url){ fnApply();};
	/** Common ShowImage */
	$scope.fnShowImage = function(url){ fnShowImage(url);};

	$scope.fnExcel = function(url){ fnExcel();};


	$("#clear").click(function(){
	      $(".resultingarticles").empty();
	      $("#searchbox").val("");
    });

    //적립금 숫자만 입력 (rewardPayPointDetl)
    fnInputValidationByRegexp("rewardPayPointDetl", /[^0-9]/g);

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
