/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : CS 통합몰 문의관리
 *
 *
 * @ 수정일          수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.24    안치열        최초생성
 * @
 ******************************************************************************/
'use strict';

var defaultActivateTab = "CS_CUSTOMERQNA";
$scope.$emit('fnIsMenu', { flag : true });
var csData;
var gbMallTp = 'MALL_TP.MALL';
var pageId = 'customerQna';
importScript(
		"/js/controllers/admin/csadmin/csPlugin.js",
		function() {
			csPlugin.init();
			var csUserData = csPlugin.getUserData();

			csData = {"urUserId": csUserData.urUserId};

			// 비회원일 경우에는 이전 location.history.back();
			if(csUserData.urUserId != '' ){

			// html 컨텐츠 가지고 오기
			getContentsHtmlByPath(
					"/contents/views/admin/customer/qna/qna.html",
					function() {
						// html 컨텐츠 가지고 온 이후 callback
						// javascript import
						importScript("/js/controllers/admin/customer/qna/qnaController.js");
					}, $('#csCustomerQnaContents'));
			} else {
				fnPageInfo({PG_ID  : 'csCustomerQna'});

				$('#csCustomerQnaContents').append(csPlugin.emptyContents());
			}
		});


