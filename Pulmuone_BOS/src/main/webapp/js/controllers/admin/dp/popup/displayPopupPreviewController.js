/**-----------------------------------------------------------------------------
 * description 		 : 전시관리 > 팝업관리 > 팝업 미리보기
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18		최성현          최초생성
 * @
 * **/
'use strict';

const paramData = parent.POP_PARAM["parameter"];

$(document).ready(
		function() {

			fnInitialize();

			function fnInitialize() {
				$scope.$emit('fnIsMenu', {
					flag : false
				});

				fnPageInfo({
					PG_ID : 'displayPopupPreview',
					callback : fnUI
				});
			}

			function fnUI() {

				fnInitButton();

				fnSearch();

			}

			 function fnInitButton() {
			        $('#fnClose').kendoButton();
			    }

			function fnSearch() {

					fnAjax({
						url : '/admin/display/popup/previewPopup',
						params : {
							displayFrontPopupId : paramData
						},
						success : function(data) {
							toggleTab(data)
						},
						isAction : 'select'
					});
			}

			function toggleTab(value) {

				$(".tab-content").hide();
				if (value.todayStopYn == "N") {
					$("#todayStopYn").hide();
				}
				if (value.popupImgPath === "") {
					$("[data-tab='DP_POPUP_TP.HTML']").show();
					$("#html").html(fnTagConvert(value.html));
				} else if (value.html === "") {
					$("[data-tab='DP_POPUP_TP.IMAGE']").show();
					$("#popupImgPath").attr("src",fnGetPublicStorageUrl() + value.popupImgPath);
				} else {
					return;
				}
			}


			function fnClose() {
				parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
				}

			$scope.fnClose = function() {
				fnClose();
			};
		});