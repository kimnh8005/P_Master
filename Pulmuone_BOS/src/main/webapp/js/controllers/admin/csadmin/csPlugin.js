/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ @ 2020.12.28
 * 안치열 최초생성 @
 ******************************************************************************/
"use strict";

var globalCsData;

var csPlugin = {
	init : function() {
		var self = this;
		self._fnCallByParent();
		self._fnCsSearchAreaConvert();
	},
	getUserData: function (){
		var self = this;
		return self._fnGetUserData();
	},

	_csSessionStorageKey : 'CS_PLUGIN.USER_DATA',
	_csSearchTypeNoSerarch : 'NOSEARCH',
	_csSearchTypeSearchMember : 'MEMBER',
	_csSearchTypeSearchOrder : 'ORDER',
	_ObjUserData : function (){
		var self = this;
		return {csSearchType: self._csSearchTypeNoSerarch, userName : '', loginId: '', genderView : '',  urUserId : '', userTypeName : '', groupName : '', mail : '', mobile : '', accumulateCount : ''};
	},
	// CS 조회 영역 위치
	_$location : $('.wrap-location'),
	_$sect : $('.fb__sect'),
	emptyContents : function(){
		var self = this;
		var $html;
		self._$location.append($html);
		return $html;
	},
	// CS 조회 영역 HTML : 검색
	_$csAreaSearchHtml: $(
			'<div class="commonArea">'
			+ '<div class="commonArea__content commonArea__content--input">'
			+ '<input type="text" id="csCommonSearch" class="commonArea__input" placeholder="회원명으로 검색해 주세요.">'
			+ '<button id="fnCsSearchUser" type="button" class="commonArea__btn commonArea__btn--submit"></button>'
			+ '</div>'
			+ '<hr></hr>'
			+ '<hr></hr>'
			+ '</div>'
			+'<div style="text-align: center;padding: 100px 0;border-top: 0px solid #000;border-bottom: 2px solid #000;">'
		    + '<h2 style="font-size : 24px;">'
			+ '<span>'
			+ '회원 또는 주문을 검색하시면 상세 정보를 조회 할 수 있습니다.'
			+ '</span>'
			+ '</h2>'
			+ '</div>'
			),
	_$csOrderAreaSearchHtml: $(
					'<div class="commonArea">'
					+ '<div class="commonArea__content commonArea__content--input">'
					+ '<input type="text" id="csCommonSearch" class="commonArea__input" placeholder="회원명으로 검색해 주세요.">'
					+ '<button id="fnCsSearchUser" type="button" class="commonArea__btn commonArea__btn--submit"></button>'
					+ '</div>'
					+ '<hr></hr>'
					+ '<hr></hr>'
					+ '</div>'
					),
	// CS 조회 영역 HTML : 회원 선택
	_$csAreaMemberHtml: $(
			'<div class="commonArea">'
			+ '<div class="commonArea__content commonArea__content--info auth">'
			+ '<ul class="commonArea__userInfo">'
			+ '<li class="commonArea__userInfoItem userName">'
			+ '<span id="userName"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem userId">'
			+ '<span id="loginId"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem">'
			+ '<span id="genderView"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem">'
			+ '<span id="userTypeName"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem">'
			+ '<span id="groupName"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem userEmail">'
			+ '<span id="mail"></span>'
			+ '</li>'
			+ '<li class="commonArea__userInfoItem">'
			+ '<span id="mobile"></span>'
			+ '<span class="commonArea__userBadge blackList" id="blackListYn">블랙리스트</span>'
			+ '</li>'
			+ '</ul>'
			+ '<button id="fnCsSearchUser" type="button" class="commonArea__btn commonArea__btn--reload">검색</button>'
			+ '</div>'
			+ '</div>'
			),

	// CS 조회 영역 html 바인딩
	_fnAppendHtml : function($html) {
		var self = this;
		self._$location.append($html);
	},
	_fnSetUserData: function (csSearchType, userData){
		var self = this;

		var data = self._ObjUserData();
		data.csSearchType = csSearchType;

		if(csSearchType == self._csSearchTypeSearchMember){
			data.userName = userData.userName;
			data.loginId = userData.loginId;
			data.genderView = userData.genderView;
			data.urUserId = userData.urUserId;
			data.userTypeName = userData.userTypeName;
			data.groupName = userData.groupName;
			data.mail = userData.mail;
			data.mobile = userData.mobile;
			data.blackList = userData.blackList;
			data.urUserId = userData.urUserId;
		}
		window.sessionStorage.setItem(self._csSessionStorageKey, JSON.stringify(data));
	},
	_fnGetUserData: function (){
		var self = this;
		var userData = globalCsData; // JSON.parse(window.sessionStorage.getItem(self._csSessionStorageKey));
		if(!userData){
			userData = self._ObjUserData();
		}
		return userData;
	},
	_fnResetUserData : function (){
		var self = this;
		sessionStorage.setItem(self._csSessionStorageKey, self._ObjUserData());
	},
	_fnCallByParent: function() {
		let tmp = getCookie("csUserParamData")

		if (tmp !== undefined && tmp != '') {
			//setCookie("csUserParamData","");
			var getData = JSON.parse(tmp);
		}

		if(getData !== undefined) {
			var self = this;
			var data = self._ObjUserData();
			data.csSearchType = self._csSearchTypeSearchMember;
			data.urUserId = getData.urUserId;
			data.employeeYn = getData.employeeYn;
			data.userName = getData.userName;
			data.loginId = getData.loginId;
			data.genderView = getData.genderView;
			data.groupName = getData.groupName;
			data.mail = getData.mail;
			data.mobile = getData.mobile;
			data.blackList = getData.blackList;
			data.userTypeName = getData.userTypeName;

			globalCsData = data;
			window.sessionStorage.setItem(self._csSessionStorageKey, JSON.stringify(data));
		}
	},
	_fnCsSearchAreaConvert: function (){	// 회원 타입별 HTML 셋팅
		var self = this;

		var userData = self._fnGetUserData();

		var $html;
		var $desc;
		if(userData.csSearchType == self._csSearchTypeSearchMember){		// 회원
			$html = self._fnEventBindMember(self._$csAreaMemberHtml, userData);
		} else {
			if(defaultActivateTab == 'CS_ORDERLIST' ||
					defaultActivateTab == 'CS_ORDERDETAILLIST' ||
					defaultActivateTab == 'CS_CLAIMUNLESSORDER' ||
					defaultActivateTab == 'CS_CLAIMRETURNORDER' ||
					defaultActivateTab == 'CS_REFUNDDETAIL' ||
					defaultActivateTab == 'CS_REGULARDELIVERY' ||
					defaultActivateTab == 'CS_CANCELREQORDER' ||
					defaultActivateTab == 'CS_REFUNDAPPROVAL' ||
					defaultActivateTab == 'CS_CASHRECEIPT'){
				$html = self._fnEventBindSearch(self._$csOrderAreaSearchHtml);		// 초기 회원팝업 조회
			}else{
				$html = self._fnEventBindSearch(self._$csAreaSearchHtml);		// 초기 회원팝업 조회
			}
		}

		self._fnAppendHtml($html);

	},
	_fnEventBindSearch: function ($html){
		var self = this;
		$html.find('#fnCsSearchUser').click(function (){
			self._fnSearchUser();
		});
		return $html;
	},
	_fnEventBindMember: function ($html, userData){
		var self = this;
		var userTypeName;
		if(userData.employeeYn == 'Y'){
			userTypeName = '임직원';
		}else{
			userTypeName = '일반';
		}

		$html.find('#userName').text(userData.userName);
		$html.find('#loginId').text(userData.loginId);
		$html.find('#genderView').text(userData.genderView);
		$html.find('#userTypeName').text(userTypeName);
		$html.find('#groupName').text(userData.groupName);
		$html.find('#mail').text(userData.mail);
		$html.find('#mobile').text(userData.mobile);
		$html.find('#urUserId').text(userData.urUserId);
		if (userData.blackList == 'N' || userData.blackList == undefined) {
			 $html.find('#blackListYn').hide();
		}else{
			 $html.find('#blackListYn').show();
		}

		$html.find('#fnCsSearchUser').click(function (){
			location.reload();
			setCookie("csUserParamData","");
		});

		return $html;
	},
	_fnSearchUser : function() {
		var self = this;
		var csFindKeyword = $("#csCommonSearch").val();

		fnKendoPopup({
			id : 'csAdminSearchPopup',
			title : 'CS관리 검색',
			src : '#/csAdminSearchPopup',
			width : '1100px',
			height : '800px',
			param : {csFindKeyword : csFindKeyword},
			success : function(stMenuId, data) {

				if(data.urUserId != undefined){
					// 회원
//					if(data.searchType == '~~~' ||  true){

						parent.$('#csAdminSearchPopup' ).parent().data('kendoWindow').destroy();

						var defaultStr = Math.random().toString(16).substr(2,11);

						if(defaultActivateTab == 'CS_BUYER'){
							location.href = '/layout.html#/csBuyerInfo?buyer=' + defaultStr;
						}else if(defaultActivateTab == 'CS_REFUND'){
							location.href = '/layout.html#/csRefundBank?refund=' + defaultStr;
						}else if(defaultActivateTab == 'CS_SHIPPING_ADDRESS'){
							location.href = '/layout.html#/csShippingAddress?shipping=' + defaultStr;
						}else if(defaultActivateTab == 'CS_COUPON'){
							location.href = '/layout.html#/csCoupon?coupon=' + defaultStr;
						}else if(defaultActivateTab == 'CS_POINT'){
							location.href = '/layout.html#/csPoint?point=' + defaultStr;
						}else if(defaultActivateTab == 'CS_CUSTOMERQNA'){
							location.href = '/layout.html#/csCustomerQna?csCustomer='+ defaultStr;
						}else if(defaultActivateTab == 'CS_ORDERLIST'){
							location.href = '/layout.html#/csOrderList?csOrder='+ defaultStr;
						}else if(defaultActivateTab == 'CS_ORDERDETAILLIST'){
							location.href = '/layout.html#/csOrderDetailList?csOrderDetail='+ defaultStr;
						}else if(defaultActivateTab == 'CS_CLAIMRETURNORDER'){
							location.href = '/layout.html#/csReturnOrderDetailList?csReturnOrderDetail='+ defaultStr;
						}else if(defaultActivateTab == 'CS_CLAIMUNLESSORDER'){
							location.href = '/layout.html#/csUnreleasedOrderDetailList?csUnreleasedOrderDetail='+ defaultStr;
						}else if(defaultActivateTab == 'CS_REFUNDDETAIL'){
							location.href = '/layout.html#/csRefundOrderDetailList?csRefundOrderDetail='+ defaultStr;
						}else if(defaultActivateTab == 'CS_REGULARDELIVERY'){
                            location.href = '/layout.html#/csRegularDeliveryReqList?csRegularDeliveryReq='+ defaultStr;
						}else if(defaultActivateTab == 'CS_CANCELREQORDER'){
                            location.href = '/layout.html#/csCancelReqOrderDetailList?csCancelReqOrderDetail='+ defaultStr;
						}else if(defaultActivateTab == 'CS_REFUNDAPPROVAL'){
                            location.href = '/layout.html#/csCsRefundApprovalList?csCsRefundApproval='+ defaultStr;
						}else if(defaultActivateTab == 'CS_CASHRECEIPT'){
                            location.href = '/layout.html#/csCashReceiptIssuedList?csCashReceiptIssued='+ defaultStr;
                        }

						self._fnSetUserData(self._csSearchTypeSearchMember, data);
						data.csSearchType = self._csSearchTypeSearchMember;
						globalCsData = data;
//						parent.fnGoPage({url:'#/csBuyerInfo',menuId : 1061});
//					} else {
//						self._fnSetUserData(self._csSearchTypeSearchOrder, data);
////						fnGoPage(주문조회);
//					}
				} else {
					self._fnResetUserData();
					location.reload();
				}
			}
		});
	}
}

//쿠키값 Set
function setCookie(cookieName, value, exdays) {
	var exdate = new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var cookieValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
	document.cookie = cookieName + "=" + cookieValue;
}

//쿠키값 가져오기
function getCookie(cookie_name) {
	var x, y;
	var val = document.cookie.split(';');

	for (var i = 0; i < val.length; i++) {
		x = val[i].substr(0, val[i].indexOf('='));
		y = val[i].substr(val[i].indexOf('=') + 1);
		x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기

		if (x == cookie_name) {
			return unescape(y); // unescape로 디코딩 후 값 리턴
		}
	}
}

// =========================================================================
// # CS관리 검색 팝업 호출 이벤트 등록을 위한 1초 지연
// =========================================================================
setTimeout(function() {

	$('#csCommonSearch').on('keydown',function(e){
			if (e.keyCode == 13) {
				csPlugin._fnSearchUser();
				return false;
			}
		});

}, 1000)




