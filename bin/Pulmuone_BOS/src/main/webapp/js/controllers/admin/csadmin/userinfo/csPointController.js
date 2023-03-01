﻿/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : CS검색 팝업
 *
 *
 * @ 수정일          수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.23    안치열        최초생성
 * @
 ******************************************************************************/
'use strict';

	var defaultActivateTab = "CS_POINT";
	var initializeFlag = true;
	var csData;

	$scope.$emit('fnIsMenu', { flag : true });


	importScript(
			"/js/controllers/admin/csadmin/csPlugin.js",
			function() {
				csPlugin.init();
				var csUserData = csPlugin.getUserData();

				csData = {"urUserId": csUserData.urUserId, "userName" : csUserData.userName , "loginId" : csUserData.loginId, "urErpEmployeeCd" : csUserData.urErpEmployeeCd, "bday" : csUserData.bday};

				// 비회원일 경우에는 이전 location.history.back();
				if(csUserData.urUserId != '' ){

				// html 컨텐츠 가지고 오기
				getContentsHtmlByPath(
						"/contents/views/admin/ur/user/buyerPopup.html",
						function() {
							// html 컨텐츠 가지고 온 이후 callback
							importScript("/js/controllers/admin/ur/user/buyerPopupController.js");
						}, $('#csPointContents'));


				} else {

					fnPageInfo({PG_ID  : 'csPoint'});

					$('#csPointContents').append(csPlugin.emptyContents());
					//$('#csPointContents').append(csPlugin.emptyMessageContents());
				}


			});
