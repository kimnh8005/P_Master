/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 현금영수증발금내역
 *
 *
 * @ 수정일          수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.06.22    안치열        최초생성
 * @
 ******************************************************************************/
'use strict';

var defaultActivateTab = "CS_REFUNDAPPROVAL";
$scope.$emit('fnIsMenu', { flag : true });
var csData;

importScript(
		"/js/controllers/admin/csadmin/csPlugin.js",
		function() {
			csPlugin.init();
			var csUserData = csPlugin.getUserData();

			csData = {"urUserId": csUserData.urUserId};

			// html 컨텐츠 가지고 오기
			getContentsHtmlByPath(
					"/contents/views/admin/od/refund/csRefundApprovalList.html",
					function() {
						// html 컨텐츠 가지고 온 이후 callback
						// javascript import
						importScript("/js/controllers/admin/od/refund/csRefundApprovalListController.js");
					}, $('#csCsRefundApprovalListContents'));
		});


