﻿/**-----------------------------------------------------------------------------
 * system            : 상품공통공지사항 등록/수정 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.03     박승현          최초생성
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel

$(document).ready(function() {

	const FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	var todayDate = new Date();
	todayDate.setHours(0,0,0,0);
	var todayDateTime = todayDate.oFormat(FULL_DATE_FORMAT);

	var lastDate = new Date(2999,11,31,23,59,0);
	var lastDateTime = lastDate.oFormat(FULL_DATE_FORMAT);

	var startDateTime;
	var endDateTime;

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "goodsNoticeMgm",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnViewModelInit();
        fnDefaultValue();
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSave").kendoButton();
    };

    // 팝업 닫기
	function fnClose(){
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	};

    // 저장
    function fnSave(){
        let paramData = $("#inputForm").formSerialize(true);
        if( paramData.rtnValid ){
            if( fnSaveValid() ){
                let url = "";
                paramData['noticeStartDt'] = paramData['noticeStartDt'] + $('#noticeStartHour').val() + $('#noticeStartMin').val() + "00";
                paramData['noticeEndDt'] = paramData['noticeEndDt'] + $('#noticeEndHour').val() + $('#noticeEndMin').val() + "59";
                if( viewModel.newCreate ){
                    url = "/admin/goods/notice/addGoodsNotice";
                }else{
                    url = "/admin/goods/notice/putGoodsNotice";
                    paramData['ilNoticeId'] = viewModel.get("goodsNoticeInfo").ilNoticeId;
                }
                fnAjax({
                    url     : url,
                    params  : paramData,
                    success : function( data ){
                        if( viewModel.get("newCreate") ){
                            fnKendoMessage({  message : "저장이 완료되었습니다."
                                            , ok : function(){
                                                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                              }
                                           });
                        }else{
                            fnKendoMessage({  message : "저장이 완료되었습니다."
                                            , ok : function(){
                                                  parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                                              }
                                           });
                        }
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "insert"
                });
            }
        }
    };
    //--------------------------------- Button End---------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){

    	fnTagMkChkBox({
	        id    : 'alwaysYn',
	        data  : [{ "CODE" : 'Y' , "NAME" : "상시노출" }],
	        tagId : 'alwaysYn',
	        chkVal: 'N',
	        style : {}
	    });

    	fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			data  : [   { "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
			],
			chkVal: "",
			style : {}
		});
    	fnTagMkRadio({
    		id    :  'dispAllYn',
    		tagId : 'dispAllYn',
    		data  : [   { "CODE" : "Y"	, "NAME":'전체' },
    			{ "CODE" : "N"	, "NAME":'출고처별' }
    		],
    		chkVal: "",
    		style : {},
    		change : function(e) {
    			fnDisplaySetting();
            }
    	});

    	// 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 선택",
            async : false
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "urWarehouseId",
            tagId : "urWarehouseId",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 전체",
            async : false,
            cscdId     : "warehouseGroup",
            cscdField  : "warehouseGroupCode"
        });

    	fnKendoDropDownList({
            id : "goodsNoticeTp",
            url : "/admin/comn/getCodeList",
            tagId : "goodsNoticeTp",
            params : { "stCommonCodeMasterCode" : "GOODS_NOTICE_TP", "useYn" : "Y" },
            chkVal: "",
            blank : "공지구분 선택",
            async : false
        });

    	// 등록일 시작
        fnKendoDatePicker({
            id: "noticeStartDt",
            format: "yyyy-MM-dd",
            btnStartId: "noticeStartDt",
            btnEndId: "noticeEndDt",
            defVal: fnGetToday(),
            change : function(e) {
                fnStartCalChange("noticeStartDt", "noticeEndDt");
            }
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "noticeEndDt",
            format: "yyyy-MM-dd",
            btnStartId: "noticeStartDt",
            btnEndId: "noticeEndDt",
            defVal: fnGetDayAdd(fnGetToday(),6,'yyyy-MM-dd'),
			min: $("#noticeStartDt").val(),
            change : function(e) {
            	fnEndCalChange("noticeStartDt", "noticeEndDt");
            }
        });
//        fbMakeTimePicker("#noticeStartHour", "hour");
//        fbMakeTimePicker("#noticeStartMin" , "min");
//        fbMakeTimePicker("#noticeEndHour"  , "hour");
//        fbMakeTimePicker("#noticeEndMin"   , "min");
        fbMakeTimePicker("#noticeStartHour", "start", "hour");
        fbMakeTimePicker("#noticeStartMin" , "start", "min");
        fbMakeTimePicker("#noticeEndHour"  , "end"  , "hour");
        fbMakeTimePicker("#noticeEndMin"   , "end"  , "min");

        // 종료 시/분 기본값 Set
        $("#noticeStartHour").data("kendoDropDownList").select(0);
        $("#noticeEndHour").data("kendoDropDownList").select(23);
        $("#noticeEndMin").data("kendoDropDownList").select(59);

        fnDescriptionKendoEditor({ // Editor
            id : 'detlDesc',
        });

    	fnInputValidationForHangulAlphabetNumberSpace("noticeNm");

    	$('input:checkbox[name=alwaysYn]').on("click", function(index){
    		isAlways();
        });
//    	fnStartCalChange("noticeStartDt", "noticeEndDt");
//    	fnEndCalChange("noticeStartDt", "noticeEndDt");
    	if( viewModel.newCreate ){
    		$('#noticeStartDt').data('kendoDatePicker').min(new Date(fnGetToday()));
    	}
    	fnDisplaySetting();
    };

    function fnDescriptionKendoEditor(opt) { // Editor

        if  ( $('#' + opt.id).data("kendoEditor") ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

            $('#' + opt.id + 'Td').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화

            var textAreaHtml = '<textarea id="' + opt.id + '" ';
            textAreaHtml += 'style="width: 100%; height: 250px" ';
            textAreaHtml += '></textarea>"'

            $('#' + opt.id + 'Td').append(textAreaHtml);  // 새로운 editor TextArea 를 추가

        }

        $('#' + opt.id).kendoEditor({
            tools : [ {
                name : 'viewHtml',
                tooltip : 'HTML 소스보기'
            },
            // { name : 'pdf', tooltip : 'PDF 저장' },  // PDF 저장시 한글 깨짐 현상 있어 주석 처리
            //------------------- 구분선 ----------------------
            {
                name : 'bold',
                tooltip : '진하게'
            }, {
                name : 'italic',
                tooltip : '이탤릭'
            }, {
                name : 'underline',
                tooltip : '밑줄'
            }, {
                name : 'strikethrough',
                tooltip : '취소선'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'foreColor',
                tooltip : '글자색상'
            }, {
                name : 'backColor',
                tooltip : '배경색상'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'justifyLeft',
                tooltip : '왼쪽 정렬'
            }, {
                name : 'justifyCenter',
                tooltip : '가운데 정렬'
            }, {
                name : 'justifyRight',
                tooltip : '오른쪽 정렬'
            }, {
                name : 'justifyFull',
                tooltip : '양쪽 맞춤'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'insertUnorderedList',
                tooltip : '글머리기호'
            }, {
                name : 'insertOrderedList',
                tooltip : '번호매기기'
            }, {
                name : 'indent',
                tooltip : '들여쓰기'
            }, {
                name : 'outdent',
                tooltip : '내어쓰기'
            },
            //------------------- 구분선 ----------------------
            {
                name : 'createLink',
                tooltip : '하이퍼링크 연결'
            }, {
                name : 'unlink',
                tooltip : '하이퍼링크 제거'
            }, {
                name : 'insertImage',
                tooltip : '이미지 URL 첨부'
            }, {
                name : 'file-image',
                tooltip : '이미지 파일 첨부',
                exec : function(e) {
                    e.preventDefault();
                    workindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
                    $('#uploadImageOfEditor').trigger('click'); // 파일 input Tag 클릭 이벤트 호출

                },
            },
            //------------------- 구분선 ----------------------
            {
                name : 'subscript',
                tooltip : '아래 첨자'
            }, {
                name : 'superscript',
                tooltip : '위 첨자'
            },
            //------------------- 구분선 ----------------------
            { name : 'tableWizard'    , tooltip : '표 수정' },
            {
                name : 'createTable',
                tooltip : '표 만들기'
            }, {
                name : 'addRowAbove',
                tooltip : '위 행 추가'
            }, {
                name : 'addRowBelow',
                tooltip : '아래 행 추가'
            }, {
                name : 'addColumnLeft',
                tooltip : '왼쪽 열 추가'
            }, {
                name : 'addColumnRight',
                tooltip : '오른쪽 열 추가'
            }, {
                name : 'deleteRow',
                tooltip : '행 삭제'
            }, {
                name : 'deleteColumn',
                tooltip : '열 삭제'
            },
            { name : 'mergeCellsHorizontally'   , tooltip : '수평으로 셀 병합' },
	        { name : 'mergeCellsVertically'   , tooltip : '수직으로 셀 병합' },
	        { name : 'splitCellHorizontally'   , tooltip : '수평으로 셀 분할' },
	        { name : 'splitCellVertically'   , tooltip : '수직으로 셀 분할' },
            //------------------- 구분선 ----------------------
            'formatting', 'fontName', {
                name : 'fontSize',
                items : [ {
                    text : '8px',
                    value : '8px'
                }, {
                    text : '9px',
                    value : '9px'
                }, {
                    text : '10px',
                    value : '10px'
                }, {
                    text : '11px',
                    value : '11px'
                }, {
                    text : '12px',
                    value : '12px'
                }, {
                    text : '13px',
                    value : '13px'
                }, {
                    text : '14px',
                    value : '14px'
                }, {
                    text : '16px',
                    value : '16px'
                }, {
                    text : '18px',
                    value : '18px'
                }, {
                    text : '20px',
                    value : '20px'
                }, {
                    text : '22px',
                    value : '22px'
                }, {
                    text : '24px',
                    value : '24px'
                }, {
                    text : '26px',
                    value : '26px'
                }, {
                    text : '28px',
                    value : '28px'
                }, {
                    text : '36px',
                    value : '36px'
                }, {
                    text : '48px',
                    value : '48px'
                }, {
                    text : '72px',
                    value : '72px'
                }, ]
            }
            //  ,'print'
            ],
            //          pdf : {
            //                title : "상품상세 기본정보",
            //                fileName : "상품상세 기본정보.pdf",
            //                paperSize : 'A4',
            //                margin : {
            //                    bottom : '20 mm',
            //                    left : '20 mm',
            //                    right : '20 mm',
            //                    top : '20 mm'
            //                }
            //            },
            messages : {
                formatting : '포맷',
                formatBlock : '포맷을 선택하세요.',
                fontNameInherit : '폰트',
                fontName : '글자 폰트를 선택하세요.',
                fontSizeInherit : '글자크기',
                fontSize : '글자 크기를 선택하세요.',
                //------------------- 구분선 ----------------------
                print : '출력',
                //------------------- 구분선 ----------------------

                //------------------- 구분선 ----------------------
                imageWebAddress : '웹 주소',
                imageAltText : '대체 문구', //Alternate text
                //------------------- 구분선 ----------------------
                fileWebAddress : '웹 주소',
                fileTitle : '링크 문구',
                //------------------- 구분선 ----------------------
                linkWebAddress : '웹 주소',
                linkText : '선택 문구',
                linkToolTip : '풍선 도움말',
                linkOpenInNewWindow : '새 창에서 열기', //Open link in new window
                //------------------- 구분선 ----------------------
                dialogInsert : '적용',
                dialogUpdate : 'Update',
                dialogCancel : '닫기',
            //-----------------------------
            }
        });
	}

    function isAlways(){
    	if($('input:checkbox[name=alwaysYn]').is(":checked") ){
        	$('#noticeEndDt').data("kendoDatePicker").value('2999-12-31');
            $("#noticeEndHour").data("kendoDropDownList").select(23);
            $("#noticeEndMin").data("kendoDropDownList").select(59);
            $("#noticeEndDt").data("kendoDatePicker").enable(false);
            $("#noticeEndHour").data("kendoDropDownList").enable(false);
            $("#noticeEndMin").data("kendoDropDownList").enable(false);
        }else{
            $("#noticeEndDt").data("kendoDatePicker").enable(true);
        	$('#noticeEndDt').data("kendoDatePicker").value(fnGetDayAdd(fnGetToday(),6,'yyyy-MM-dd'));
        }

    }
    function fnDisplaySetting(){
    	if($("#dispAllYn").getRadioVal() == "N"){
    		$("#warehouseGroup").data("kendoDropDownList").enable(true);
        }else{
        	$("#warehouseGroup").data("kendoDropDownList").select("");
        	$("#urWarehouseId").data("kendoDropDownList").select("");
        	$("#warehouseGroup").data("kendoDropDownList").enable(false);
        	$("#urWarehouseId").data("kendoDropDownList").enable(false);
        }
    }

    //---------------Initialize Option Box End ------------------------------------------------

    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
    	viewModel = new kendo.data.ObservableObject({
    		goodsNoticeInfo : { // 상품공통공지사항 템플릿
    			ilNoticeId : null, // 상품공통공지사항.SEQ
    			goodsNoticeTp : null, // 상품 공지 구분 공통코드
    			noticeNm : "", // 공지제목
    			dispAllYn : "", // 노출범위
    			warehouseGroup : "", // 출고처 그룹 공통코드
    			urWarehouseId : "", // 출고처 FK
    			noticeStartDt : "", // 노출시작일
    			noticeEndDt : "", // 노출종료일
    			useYn : "", // 사용여부
    			detlDesc : "" // 상세정보
    		},
    		newCreate : true, // 신규 여부
    		fnGetGoodsNoticeInfo : function( dataItem ){ // 카드혜택 상세정보 조회
    			fnAjax({
    				url     : "/admin/goods/notice/getGoodsNoticeInfo",
    				params  : dataItem,
    				method : "GET",
    				success : function( data ){
    					viewModel.set("goodsNoticeInfo", data);
						$('#inputForm').bindingForm( {'rows':data},'rows', true);
						fnDisplaySetting();

						var sDt = kendo.parseDate(data.noticeStartDt, "yyyy-MM-dd HH:mm");
						var eDt = kendo.parseDate(data.noticeEndDt, "yyyy-MM-dd HH:mm");
						if(sDt != null && sDt != ""){
					    	$("#noticeStartHour").data("kendoDropDownList").value(sDt.oFormat("HH"));
					    	$("#noticeStartMin").data("kendoDropDownList").value(sDt.oFormat("mm"));

						}
						if(eDt != null && eDt != ""){
							$("#noticeEndHour").data("kendoDropDownList").value(eDt.oFormat("HH"));
							$("#noticeEndMin").data("kendoDropDownList").value(eDt.oFormat("mm"));
							if(eDt.oFormat("yyyy-MM-dd HH:mm") == "2999-12-31 23:59") {
								$('input:checkbox[name=alwaysYn]').prop("checked", true);
								isAlways();
							}
						}
						var createInfo = data.createDt + " / " + data.createUserName + "(" + data.createId + ")";
						if(data.modifyDt != null && data.modifyDt != "") createInfo += "<br>" + data.modifyDt + " / " + data.modifyUserName + "(" + data.modifyId + ")";
						$("#createDt").html(createInfo);
    				},
    				isAction : "select"
    			});
    		}
    	});
    	kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue(){
        if( !paramData.ilNoticeId ){
            viewModel.set("newCreate", true);
        }else{
            viewModel.set("newCreate", false);
            viewModel.fnGetGoodsNoticeInfo( paramData );
        }
    };

    // 입력값 검증
    function fnSaveValid(){
    	startDateTime = kendo.parseDate(($('#noticeStartDt').val() + " " + $('#noticeStartHour').val() + ":" + $('#noticeStartMin').val()), "yyyy-MM-dd HH:mm");
    	endDateTime = kendo.parseDate(($('#noticeEndDt').val() + " " + $('#noticeEndHour').val() + ":" + $('#noticeEndMin').val()), "yyyy-MM-dd HH:mm");

    	if ($("input[name='useYn']:checked").val() != "Y" && $("input[name='useYn']:checked").val() != "N" ){
			fnKendoMessage({ message : "사용여부를 선택해주세요." });
			return false;
		}

    	if ($("input[name='dispAllYn']:checked").val() != "Y" && $("input[name='dispAllYn']:checked").val() != "N" ){
			fnKendoMessage({ message : "노출범위를 선택해주세요." });
			return false;
		}else if($("input[name='dispAllYn']:checked").val() == "N"){
			if($("#warehouseGroup").val() == ""){
				fnKendoMessage({ message : "출고처 그룹을 선택해주세요." });
				return false;
			}
		}

    	if( startDateTime.getTime() >= endDateTime.getTime() ){
			fnKendoMessage({ message : "기간을 정확히 입력해주세요" });
			return false;
		}

    	//오늘 날짜와 비교
    	//!paramData.ilNoticeId
    	if(paramData.ilNoticeId == undefined) {
    		if( todayDate.getTime() > startDateTime.getTime() ) {
    			fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요." });
    			return false;
    		}
    	}


		if( startDateTime.getTime() > endDateTime.getTime() ) {
			fnKendoMessage({ message : "종료 날짜 이전을 선택해주세요." });
			return false;
		}
		if( $("#detlDesc").val().length < 1 ){
    		fnKendoMessage({ message : "상세정보를 입력해주세요",
    			ok : function(e) { $("#detlDesc").focus(); }
    		});
    		return false;
    	}

    	return true;
    };

    function fnDateClear(){
    	$('input:checkbox[name=alwaysYn]').prop("checked", false);

        $("#noticeEndDt").data("kendoDatePicker").enable(true);
        $("#noticeEndHour").data("kendoDropDownList").enable(true);
        $("#noticeEndMin").data("kendoDropDownList").enable(true);

    	$('#noticeStartDt').data("kendoDatePicker").value(fnGetToday());
    	$("#noticeStartHour").data("kendoDropDownList").select(0);
    	$("#noticeStartMin").data("kendoDropDownList").select(0);

    	$('#noticeEndDt').data("kendoDatePicker").value(fnGetDayAdd(fnGetToday(),6,'yyyy-MM-dd'));
        $("#noticeEndHour").data("kendoDropDownList").select(23);
        $("#noticeEndMin").data("kendoDropDownList").select(59);
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function(){ fnSave(); }; // 저장
    $scope.fnClose = function(){ fnClose(); }; // 닫기
    $scope.fnDateClear = function(){ fnDateClear(); }; // 초기화

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
