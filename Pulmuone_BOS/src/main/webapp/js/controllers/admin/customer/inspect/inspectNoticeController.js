
/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description :  정기정검 설정
 * @
 * @ 수정일 			수정자 			수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.16 	최윤석 			최초생성 @
 ******************************************************************************/
'use strict';

var viewModel;  // Kendo viewModel
var fnTemplates   = {};
var gbIpGroupMaps = new Map();

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'inspectNotice',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitButton();
		fnDefaultSet();
		fnInitViewModel();

		// 접근 권한 설정 템플릿 초기화
		fnTemplates.commentTp = createTemplateItem({
			el      : '#exceptIpList',
			template: '#ul-template'
		});

		// IP 추가 버튼 클릭 이벤트
		$('#btnAddIpGroup').on('click', function(e) {
			e.stopPropagation();
			e.stopImmediatePropagation();
			fnBtnAddIpGroup(e);
		});

		// IP 삭제
		$('#ng-view').on('click', '.js__remove__ipGroupBtn', fnRemoveIpGroup);

	}

	// 기본 설정
	function fnDefaultSet(){

		fnAjax({
			url		: '/admin/customer/inspect/getInspectNotice',
			method: "GET",
			processData : false,
			isAction : 'select',
			success	:
				function( data ){
					if (data.inspectName == null || data.inspectName == undefined)
						return;

					viewModel.formInfo.set("inspectName", data.inspectName);
					viewModel.formInfo.set("mainTitle", data.mainTitle);
					viewModel.formInfo.set("subTitle", data.subTitle);
					viewModel.formInfo.set("startDt", data.startDt);
					viewModel.formInfo.set("startHour", data.startHour);
					viewModel.formInfo.set("startMin", data.startMin);
					viewModel.formInfo.set("endDt", data.endDt);
					viewModel.formInfo.set("endHour", data.endHour);
					viewModel.formInfo.set("endMin", data.endMin);
					viewModel.formInfo.set("createDt", data.createDt + " / " + data.createName);
					viewModel.formInfo.set("exceptIpList", data.exceptIpList);
				}
		});

		fnKendoDatePicker({
			id        : 'startDt'
			, format    : 'yyyy-MM-dd'
			, change    : fnOnChangeStartDt
		});
		fnKendoDatePicker({
			id        : 'endDt'
			, format    : 'yyyy-MM-dd'
			, btnStartId: 'startDt'
			, btnEndId  : 'endDt'
			, change    : fnOnChangeEndDt
			, minusCheck: true
			, nextDate  : true
		});
		fbMakeTimePicker('#startHour', 'start', 'hour', fnOnChangeStartDt);
		fbMakeTimePicker('#startMin' , 'start', 'min' , fnOnChangeStartDt);
		fbMakeTimePicker('#endHour'  , 'end'  , 'hour', fnOnChangeEndDt);
		fbMakeTimePicker('#endMin'   , 'end'  , 'min' , fnOnChangeEndDt);

		// 일자 선후 체크
		function fnOnChangeStartDt(e) {
			fnOnChangeDateTimePicker( e, 'start', 'startDt', 'startHour', 'startMin',  'endDt', 'endHour', 'endMin' );
		}
		function fnOnChangeEndDt(e) {
			fnOnChangeDateTimePicker( e, 'end'  , 'startDt', 'startHour', 'startMin',  'endDt', 'endHour', 'endMin' );
		}

	};

	// viewModel 초기화
	function fnInitViewModel(){
		viewModel = new kendo.data.ObservableObject({
			formInfo : {
				inspectName : null,
				mainTitle : "샾풀무원 정기점검 안내 \n",
				subTitle : "안녕하세요, 샵 풀무원 입니다. \n" +
							"쇼핑몰 이용에 불편을 드려 대단히 죄송합니다. \n" +
							"샵 풀무원은 보다 안정적인 서비스 제공을 위하여, \n" +
							"시스템 정기점건을 진행중입니다. 부득이하게 작업을 \n" +
							"진행하는 동안 샵 풀무원 접속이 불가합니다. \n" +
							"감사합니다.",
				startDt : null,
				startHour : null,
				startMin : null,
				endDt : null,
				endHour : null,
				endMin : null,
				exceptIpList : [],
				createDt : null,
				createId : null
			}
		});

		kendo.bind($("ul"), viewModel);
		kendo.bind($("#inputForm"), viewModel);
	}

	function fnBizCallback(cbId, data) {
		console.log("success");
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnClose, #fnSave').kendoButton();
	}

	// 초기화
	function fnSave(){
		let url  = '/admin/customer/inspect/setInspectNotice';
		let cbId= 'update';
		let data = $('#inputForm').formSerialize(true);
		let ulArray = new Array();

		viewModel.formInfo.exceptIpList.forEach(e => {
			if (e.ip != null) {
				ulArray.push(e.ip)
			} else {
				ulArray.push(e)
			}
		});
		data.exceptIpList = ulArray;

		if (data.mainTitle == "" || data.subTitle == "" || data.inspectName == "") {
			fnMessage("필수값 입력이 필요합니다.")
			return false;
		}

		if (data.startDt == "" || data.startHour == "" || data.startMin == ""
			|| data.endDt == "" || data.endHour == "" || data.endMin == "") {
			fnMessage("적용 기간 입력이 필요합니다.")
			return false;
		}

		fnAjax({
			url     : url,
			params    : data,
			contentType : 'application/json',
			processData : false,
			type    : "POST",
			success :
				function( data ){
					fnDefaultSet();
					fnMessage("정기점검 설정이 완료되었습니다.")
				},
			isAction : 'batch'
		});
	}

	// 팝업창 닫기
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// IP등록 추가 버튼
	function fnBtnAddIpGroup(e) {
		let inputVal = $('#ipGroup').val();

		//아이피 포맷 체크
		if (ValidateIPaddress(inputVal) == false) {
			return false;
		}

		//아이피 중복 체크
		let hasDuplicateIp = false;
		$('.ipGroupText').each(function(){
			if (inputVal == $(this).text()) {
				hasDuplicateIp = true;
				return false;
			}
		});

		if (hasDuplicateIp == true) {
			fnMessage('중복된 아이피 입니다. ' + inputVal);
			return false;
		}

		viewModel.formInfo.exceptIpList.push({ip:inputVal});

	}

	// IP 삭제 버튼
	function fnRemoveIpGroup(e) {
		e.stopPropagation();
		e.stopImmediatePropagation();

		let inputVal = $('#ipGroup').val();

		viewModel.formInfo.exceptIpList.pop({ip:inputVal});
	}

	function ValidateIPaddress(inputText) {
		var ipformat = /^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/;
		if (ipformat.test(inputText)) {
			return true;
		} else {
			fnMessage("입력하신 값은 IP형식이 아닙니다.");
			return false;
		}
	}

	//message
	function fnMessage(msg) {
		$("#forbizMessage").kendoDialog({
			closable: false,
			modal: true,
			minWidth: 400,
			title: 'Message',
			content: msg,
			actions: [{
				text: '확인', primary: true, action: function (e) {
					$("#forbizMessage").data("kendoDialog").close();
					fnKendoMessageInit();
				}
			}]
		});
	}

	/**
	 * 제이쿼리 템플릿 초기화 함수
	 * @param {object} el : 템플릿이 추가될 타겟, 제이쿼리 엘리먼트
	 * @param {string} template : 데이터 바인딩 시 사용할 제이쿼리 템플릿, html 태그 또는 제이쿼리 엘리먼트
	 */
	function createTemplateItem({el, template}) {
		if (!el || !template) {
			console.warn('error', el, template);
			return {};
		}

		const $el = $(el);
		const _template = $(template).html();

		return {
			el: $el,
			template: $.template(_template),
			render: function (data, callback) {
				const tmpl = $.tmpl(this.template, data);
				this.el.empty();
				tmpl.appendTo(this.el);

				if (typeof callback === 'function') {
					callback.call(this, data);
				}
			},
			add: function (data, callback) {
				const tmpl = $.tmpl(this.template, data);
				tmpl.appendTo(this.el);

				if (typeof callback === 'function') {
					callback.call(this, data);
				}
			},
		};
	}

	// --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------
	$scope.fnClose = function () {
		fnClose();
	};
	$scope.fnSave = function () {
		fnSave();
	};
	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
