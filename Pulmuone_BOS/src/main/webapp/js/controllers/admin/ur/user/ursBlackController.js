/**-----------------------------------------------------------------------------
 * description 		 : 블랙리스트회원
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.25		박영후          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var gFileTagId;
var gFile;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'ursBlack',
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

		fnInitKendoUpload();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}
	function fnSearch(){

		if (($('#condiType').val() == "USER_NAME" || $('#condiType').val() == "LOGIN_ID") && $("#condiTextareaValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }

        if (($('#condiType').val() == "MOBILE" || $('#condiType').val() == "MAIL") && $("#condiValue").val() == "") {
            return valueCheck("", "조회 하시고자 하는 검색조건을 선택 후 검색해 주세요.", 'condiType');
        }

		if( $('#startCreateDate').val() == "" && $('#endCreateDate').val() != ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요.."});
			return;
		}
		if( $('#startCreateDate').val() != "" && $('#endCreateDate').val() == ""){
			fnKendoMessage({ message : "시작일 또는 종료일을 입력해주세요."});
			return;
		}

		if( $('#startCreateDate').val() > $('#endCreateDate').val()){
			fnKendoMessage({ message : "시작일을 종료일보다 뒤로 설정할 수 없습니다."});
			return;
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
	function fnClear(){
		$('#searchForm').formClear(true);

		$(".set-btn-type6").attr("fb-btn-active" , false );
        //fnSearchControl();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userBlack/getBlackListUserList'
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
					{title : 'No.'		, width:'50px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
					,{ field:'userType'			,title : '회원유형'	, width:'60px'	,attributes:{ style:'text-align:center' }, template: "#=(employeeYn=='Y') ? '임직원' : '일반'#"}
					,{ field:'userName'			,title : '회원명'		, width:'80px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
					,{ field:'loginId'			,title : '회원ID'		, width:'100px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
					,{ field:'mobile'			,title : '휴대폰'		, width:'100px'	,attributes:{ style:'text-align:center' }
						, template: function(dataItem) {
							var mobile = kendo.htmlEncode(dataItem.mobile);
							return fnPhoneNumberHyphen(mobile);
	                    }
					}
					,{ field:'mail'				,title : 'EMAIL'	, width:'140px'	,attributes:{ style:'text-align:center' }}
					,{ field:'eventLimitTp'		,title : '이벤트 제한 여부'	, width:'50px'	,attributes:{ style:'text-align:center' }}
					,{ field:'accumulateCount'	,title : '누적횟수'	, width:'40px'	,attributes:{ style:'text-align:center;text-decoration: underline;' }}
					,{ field:'lastCreateDate'	,title : '최근등록일'	, width:'90px'	,attributes:{ style:'text-align:center' }}
					,{ field:'urUserId'			,hidden: true}
			]
			,selectable : false
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		// cell 에 링크 걸기 시작
		var detailTargetPosition = [];
		var blackHistTargetPosition = "";

		for ( var i in aGrid.columns) {
			var gridColumn = aGrid.columns[i];

			// 팝업 링크 칼럼 추가/제거시 아래 조건에 field 명을 추가/제거하면 된다.
			if (gridColumn.field == "userName" || gridColumn.field == "loginId")	// 회원명, 회원 ID 칼럼 id 조회 (회원상세 팝업 링크를 위해서)
				detailTargetPosition.push(i*1 + 1);
			if (gridColumn.field == "accumulateCount")								// 누적횟수 칼럼 id 조회 (블랙리스트 이력 팝업 링크를 위해서)
				blackHistTargetPosition = i*1 + 1;
		}

		aGrid.bind("dataBound", function() {
			for ( var i in detailTargetPosition) {
				// 회원상세 팝업 링크
				$("#aGrid tbody tr td:nth-child(" + detailTargetPosition[i] + ")").on("click", function () {
					fnDetailClick(this);
				}).css("cursor", "pointer");
			}

			// 블랙리스트 이력 팝업 링크
			$("#aGrid tbody tr td:nth-child(" + blackHistTargetPosition + ")").on("click", function () {
				fnBlackHistClick(this);
			}).css("cursor", "pointer");

			let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			$("#countTotalSpan").text(aGridDs._total);
		});
		// cell 에 링크 걸기 끝
	}


	// 회원상세 팝업
	function fnDetailClick(td){
		var dataItem = aGrid.dataItem(td.closest('tr'));
		//console.log(dataItem.urUserId);

		fnKendoPopup({
            id: 'buyerPopup',
            title: fnGetLangData({ key: "5223", nullMsg: '회원수정' }),
            param: { "urUserId": dataItem.urUserId },
            src: '#/buyerPopup',
            width: '1200px',
            height: '700px',
            success: function(id, data) {
                //fnSearch();
            }
        });
	};


	// 블랙리스트 이력 팝업
	function fnBlackHistClick(td){
		var dataItem = aGrid.dataItem(td.closest('tr'));
		//console.log(dataItem.urUserId);

		fnKendoPopup({
            id: 'blackHistPopup',
            title: '블랙리스트 이력',
            param: { "urUserId": dataItem.urUserId },
            src: '#/ursBlackHist',
            width: '800px',
            height: '450px',
            success: function(id, data) {
                //fnSearch();
            }
        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

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
		/*var condiTypeDrop = fnKendoDropDownList({
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

		fnKendoDropDownList({
			id  : 'eventLimitTp',
			data  : [
				{"CODE":"Y"	,"NAME":"예"},
				{"CODE":"N"	,"NAME":"아니오"}
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

		// 켄도팝업 초기화
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
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

	// 목록 리스트 엑셀다운로드
	function fnBlackListUserListExcelDownload() {
		var url = "/admin/ur/userBlack/getBlackListUserListExportExcel";
		var grid = $("#aGrid").data("kendoGrid");
		var gridDs = grid.dataSource;

		if(gridDs._pristineData.length <= 0){
			fnKendoMessage({ message : "조회 후 다운로드 가능합니다." });
			return;
		}
		var form = $("#searchForm");
		var data = form.formSerialize(true);

		fnExcelDownload(url, data);
	}

	// 신규 팝업
	function fnNew(){

		$('#inputForm').formClear(true);

		$(".fileText").html("");

		$("#uploadUserFile").prop("required", true);

		fnKendoInputPoup({height:"150px" ,width:"700px", title:{ nullMsg :'블랙리스트 엑셀업로드'} });
	};

	// 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
	function fnInitKendoUpload() {
		var uploadFileTagIdList = ['excelFile'];

		var selectFunction = function(e) {
			if (e.files && e.files[0]) {
				// 엑셀 파일 선택시
				$("#fileInfoDiv").text(e.files[0].name);

				if(!validateExtension(e)) {
					fnKendoMessage({
						message : '허용되지 않는 확장자 입니다.',
						ok : function(e) {}
					});
					return;
				}

				gFileTagId = e.sender.element[0].id;
				let reader = new FileReader();

				reader.onload = function(ele) {
					gFile = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체
				};

				reader.readAsDataURL(e.files[0].rawFile);

			} // End of if (e.files && e.files[0])
		} // End of var selectFunction = function(e)

		for (var i = 0; i < uploadFileTagIdList.length; i++) {
			fnKendoUpload({
				id : uploadFileTagIdList[i]
				, select : selectFunction
			});
		} // End of for (var i = 0; i < uploadFileTagIdList.length; i++)
	}

	// 파일업로드-validateExtension
	function validateExtension(e) {

		var allowedExt = '';
		var ext = e.files[0].extension;
		var $el = e.sender.element;

		if( !$el.length ) return;

		if( $el[0].accept && $el[0].accept.length ) {
			// 공백 제거
			allowedExt = $el[0].accept.replace(/\s/g, '');
			allowedExt = allowedExt.split(',');
		} else {
			allowedExt = allowedImageExtensionList;
		}

		return allowedExt.includes(ext);
	}

	// 파일업로드 처리
	function fnExcelUploadRun(){

		fnKendoMessage({
			message: "블랙리스트로 지정 시, 취소할 수 없습니다.<BR>업로드하신 회원 정보를 다시 한번 확인 해주세요.<BR><BR>정말 진행하시겠습니까?",
			type: "confirm",
			ok: function (event) {
				if(gFile == undefined || gFile == ""){
					fnKendoMessage({
						message : "엑셀파일을 등록해주세요.",
						ok : function(e) {
						}
					});
					return;
				}
				fnExcelUpload(gFile, gFileTagId);
				return true;
			},
			cancel: function (event) {
				return false;
			}
		});
	}

	// NOTE 파일 업로드 이벤트
	function fnExcelUpload(file) {

		var formData = new FormData();
		formData.append('bannerImage', file);

		$.ajax({
			url         : '/admin/ur/userBlack/addBlackListUserExcelUpload'
			, data        : formData
			, type        : 'POST'
			, contentType : false
			, processData : false
			, async       : false
			, beforeSend : function(xhr) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			}
			, success     : function(data) {

				fnSearch();
				gFile = "";
				$("#fileInfoDiv").empty();

				if (data.code == '0000') {
					fnKendoMessage({message: data.data.successCnt+"명이 블랙리스트에 추가되었습니다."});
				} else {
					fnKendoMessage({message: data.message});
				}

				$('#kendoPopup').data('kendoWindow').close();
			}
		});
	}

	// 샘플 엑셀 다운로드
	function fnSampleDownload(){
		document.location.href = "/contents/excelsample/usermessage/ursBlackExcelUploadSample.xlsx"
	}

	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------
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

	$scope.fnNew = function( ){	fnNew();};

	$scope.fnBtnExcelSelect = function(fileType) { $('#' + fileType).trigger('click'); };

	$scope.fnExcelDownload = function() { fnBlackListUserListExcelDownload(); };

	$scope.fnExcelUploadRun = function() { fnExcelUploadRun(); };

	$scope.fnSampleDownload = function() { fnSampleDownload(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
