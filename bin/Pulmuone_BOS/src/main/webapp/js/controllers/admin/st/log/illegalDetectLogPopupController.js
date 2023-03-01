/**-----------------------------------------------------------------------------
 * system 			 : 부정거래탐지 상세정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.24		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var stIllegalLogId;


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------
    stIllegalLogId = parent.POP_PARAM["parameter"].stIllegalLogId;
	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'illegalDetectLogPopup',
			callback : fnUI
		});

		// ------------------------------------------------------------------------
	}

	function fnUI(){


		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

        initUI();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave', '#fnOrderExcelExport', '#fnIdExcelExport').kendoButton();
	}

    // ==========================================================================
    // # 회원 ID 엑셀다운로드 로그 등록
    // ==========================================================================
    function fnIdExcelExport() {
        var formData = $('#inputForm').formSerialize(true);
        var url = '/admin/st/log/illegalDetectUserIdxportExcel';
        fnKendoPopup({
            id      : 'excelDownloadReasonPopup'
          , title   : '엑셀 다운로드 사유'
          , src     : '#/excelDownloadReasonPopup'
          , param   : {excelDownloadType : 'EXCEL_DOWN_TP.MALL_MEMBER'}
          , width   : '700px'
          , height  : '300px'
          , success : function(id, data){
                        if(data == 'EXCEL_DOWN_TP.MALL_MEMBER'){
                          fnExcelDownload(url, formData);
                        }
                      }
        });
    }

    // ==========================================================================
    // # 주문번호 엑셀다운로드 로그 등록
    // ==========================================================================
    function fnOrderExcelExport() {
        var formData = $('#inputForm').formSerialize(true);
        var url = '/admin/st/log/illegalDetectOrderExportExcel';
        fnKendoPopup({
            id      : 'excelDownloadReasonPopup'
          , title   : '엑셀 다운로드 사유'
          , src     : '#/excelDownloadReasonPopup'
          , param   : {excelDownloadType : 'EXCEL_DOWN_TP.ORDER'}
          , width   : '700px'
          , height  : '300px'
          , success : function(id, data){
                        if(data == 'EXCEL_DOWN_TP.ORDER'){
                          fnExcelDownload(url, formData);
                        }
                      }
        });
    }


	function initUI(){

    		var url  = '/admin/st/log/getIllegalDetectLogDetail';
    		var data = {
    				'stIllegalLogId' : stIllegalLogId
    				};
    		fnAjax({
    				url     : url,
    				params  : data,
    				success :
    				function(result) {

                        $('#inputForm').bindingForm(result, 'rows', true);



                        if(result.rows.loginIdGroup != null && result.rows.loginIdGroup != ''){
                            let loginIdGroup = result.rows.loginIdGroup;
                            loginIdGroup = replaceAll(loginIdGroup, ",", "\r\n");
                            $('#userId').val(loginIdGroup);
                        }

                        if(result.rows.odidGroup != null && result.rows.odidGroup != ''){
                            let odidGroup = result.rows.odidGroup;
                            odidGroup = replaceAll(odidGroup, ",", "\r\n");
                            $('#odid').val(odidGroup);
                        }

                        if(result.rows.illegalStatusType == 'ILLEGAL_STATUS_TYPE.DETECT'){
                            $('#modifyInfoDiv').hide();
                        }else{
                            $('#modifyInfoDiv').show();
                        }

                        $("#illegalTypeName").html(result.rows.illegalTypeName);
                        $("#illegalDetailTypeName").html(result.rows.illegalDetailTypeName);
                        $("#urPcidCd").html(result.rows.urPcidCd);
                        $("#userYn").html(result.rows.userYn);
                        $("#detectDate").html(result.rows.detectDate);
                        $("#modifyInfo").html(result.rows.modifyInfo);

                        var userCnt = result.rows.userCnt;
                        $("#userCnt").html('(총 ' + '<span style="color:red;">' + userCnt + '</span>'  + '개 ID)');

                        var orderCnt = result.rows.orderCnt;
                        $("#orderCnt").html('(총 ' + '<span style="color:red;">' + orderCnt + '</span>'  + '개 주문)');

                        var illegalDetectCmtFront = '';
                        var illegalDetectCmtEnd = '';
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.USER_JOIN'){          // 비정상 회원가입
                            illegalDetectCmtFront = '동일한 Device ID에서 로그인 실패가 60분 이내에 ';
                            illegalDetectCmtEnd = '회 완료 ';
                            $("#illegalDetectCmt").html(illegalDetectCmtFront +'<span style="color:red;">'+  result.rows.illegalDetect + '</span>' + illegalDetectCmtEnd);
                            $('#loginIdDiv').show();
                            $('#odidDiv').hide();
                        }
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.LOGIN_FAIL'){          // 비정상 로그인
                            illegalDetectCmtFront = '동일한 Device ID에서 로그인 실패가 60분 이내에 ';
                            illegalDetectCmtEnd = '회 진행 ';

                            $("#illegalDetectCmt").html(illegalDetectCmtFront +'<span style="color:red;">'+  result.rows.illegalDetect + '</span>' + illegalDetectCmtEnd);
                            $('#userYnDiv').hide();
                            $('#loginIdDiv').hide();
                            $('#odidDiv').hide();
                        }
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.STOLEN_LOST_CARD'){          // 도난분실카드
                            illegalDetectCmtFront = '도난분실카드로 결제 시도 (오류코드 120026)';
                            illegalDetectCmtEnd = '';

                            $("#illegalDetectCmt").html(illegalDetectCmtFront);
                            $('#illegalDetect').val('');
                            $('#odidDiv').hide();
                        }
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.TRANSACTION_NOT_CARD'){          // 거래불가카드
                            illegalDetectCmtFront = '거래불가카드로 결제 시도 (오류코드 120027)';
                            illegalDetectCmtEnd = '';

                            $("#illegalDetectCmt").html(illegalDetectCmtFront);
                            $('#illegalDetect').val('');
                            $('#odidDiv').hide();
                        }
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.ORDER_COUNT'){          // 비정상주문결제1. 결제횟수
                            illegalDetectCmtFront = '동일한 Device ID에서 로그인 실패가 60분 이내에 ';
                            illegalDetectCmtEnd = '회 진행 ';
                            $("#illegalDetectCmt").html(illegalDetectCmtFront +'<span style="color:red;">'+  result.rows.illegalDetect + '</span>' + illegalDetectCmtEnd);
                            $('#illegalDetectCmtFront').val(illegalDetectCmtFront);
                            $('#illegalDetectCmtEnd').val(illegalDetectCmtEnd);
                        }
                        if(result.rows.illegalDetailType == 'ILLEGAL_DETAIL_TYPE.ORDER_PRICE'){          // 비정상주문결제1. 결제금액
                            illegalDetectCmtFront = '동일한 Device ID에서 로그인 실패가 60분 이내에 ';
                            illegalDetectCmtEnd = '원 진행 ';
                            let illegalDetect = result.rows.illegalDetect;
                            illegalDetect = priceToString(illegalDetect);

                            $("#illegalDetectCmt").html(illegalDetectCmtFront +'<span style="color:red;">'+  illegalDetect + '</span>' + illegalDetectCmtEnd);
                            $('#illegalDetectCmtFront').val(illegalDetectCmtFront);
                            $('#illegalDetectCmtEnd').val(illegalDetectCmtEnd);
                        }


    				},

    			isAction : 'select'
    		});

    	}

    function priceToString(price) {
        return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

    function replaceAll(str, searchStr, replaceStr) {
      return str.split(searchStr).join(replaceStr);
    }

	function fnClose(params){

		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	// 확인 버튼
	function fnSave(){
	    fnKendoMessage({message:'등록한 정보로 저장 하시겠습니까?', type : "confirm" ,ok : function(){ fnConfirm() } });
	}
	// 변경내용 저장
	function fnConfirm(){

        var data;
        data = $('#inputForm').formSerialize(true);
        var cbId = 'update';
        fnAjax({
            url     : "/admin/st/log/putIllegalDetectDetailInfo",
            params  : data,
            success : function( data ){
                fnKendoMessage({  message : "저장 되었습니다."
                    , ok : function(){
                       fnClose();
                    }
                });
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "update"
        });
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnSelectClient(param){

		fnClose(param);
	}

	//-------------------------------  Grid End  -------------------------------
	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

        $('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 진행상태
        fnTagMkRadio({
            id : "illegalStatusType",
            url : "/admin/comn/getCodeList",
            tagId : "illegalStatusType",
            async : false,
            style : {},
            params : {"stCommonCodeMasterCode" : "ILLEGAL_STATUS_TYPE", "useYn" :"Y"},
            chkVal: 'ILLEGAL_STATUS_TYPE.DETECT'
        });


	}
	//---------------Initialize Option Box End ------------------------------------------------



	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common fnSave*/
	$scope.fnSave = function( ) {	fnSave();	};
	$scope.fnClose = function( ) {	fnClose();	};
	$scope.fnIdExcelExport = function( ) {	fnIdExcelExport();	};
	$scope.fnOrderExcelExport = function( ) {	fnOrderExcelExport();	};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
