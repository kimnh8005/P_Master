/**-----------------------------------------------------------------------------
 * description 		 : 전시관리 > 팝업관리
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.25		최성현          최초생성
 * @
 * **/
'use strict';

const paramData = parent.POP_PARAM["parameter"];

$(document).ready(
		function() {

			var workKindEditorId; // 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id

			fnInitialize();

			function fnInitialize() {
				$scope.$emit('fnIsMenu', {
					flag : false
				});

				fnPageInfo({
					PG_ID : 'displayPopupMgmPopup',
					callback : fnUI
				});
			}

			function fnUI() {

				initDateTimePicker();

				fnInitButton();

				fnInitOptionBox();

				bindEvent();

				fnSearch();

				fnInputValidationForHangulAlphabetNumberSpace("popupSubject");

			}


			function fnInitButton() {
				$('#fnSave').kendoButton();

				fnDescriptionKendoEditor({ // Editor
					id : 'html',
				});

				fnKendoUpload({ // 상품 상세 기본 정보 / 주요정보 이미지 첨부 File Tag 를 kendoUpload 로 초기화
					id : "uploadImageOfEditor",
					select : function(e) {

						if (e.files && e.files[0]) { // 이미지 파일 선택시

							// UPLOAD_IMAGE_SIZE : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
							if (UPLOAD_IMAGE_SIZE < e.files[0].size) { // 용량 체크
								fnKendoMessage({
									message : '이미지 업로드 허용 최대 용량은 ' + parseInt(UPLOAD_IMAGE_SIZE / 1048576) + ' MB 입니다.',
									ok : function(e) {}
								});
								return;
							}

							let reader = new FileReader();

							reader.onload = function(ele) {
								fnUploadImageOfEditor(); // 선택한 이미지 파일 업로드 함수 호출
							};

							reader.readAsDataURL(e.files[0].rawFile);
						}

						function fnUploadImageOfEditor(opt) { // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가

							var formData = $('#uploadImageOfEditorForm').formSerialize(true);

							fnAjaxSubmit({
								form: "uploadImageOfEditorForm",
								fileUrl: "/fileUpload",
								method: 'GET',
								url: '/comn/getPublicStorageUrl',
								storageType: "public",
								domain: "dp",
								params: formData,
								success: function (result) {

									var uploadResult = result['addFile'][0];
									var serverSubPath = uploadResult['serverSubPath'];
									var physicalFileName = uploadResult['physicalFileName'];
									var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url

									var editor = $('#' + workKindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
									editor.exec('inserthtml', {
										value: '<img src="' + imageSrcUrl + '"/>'
									});

								},
								isAction: 'insert'
							});
						}

					}
				});
			}

			function fnSearch() {
				if (paramData !== undefined) {

					fnAjax({
						url : '/admin/display/popup/putPopupDetail',
						params : {
							displayFrontPopupId : paramData
						},
						success : function(data) {

							$('#inputForm').bindingForm({
								data : data
							}, "data");

							toggleTab(data.popupType);

							var startdate = new Date(data.displayPopupStartDate);
							var enddate = new Date(data.displayPopupEndDate);

							var startDateMinute = startdate.getMinutes()<10? '0'+startdate.getMinutes():startdate.getMinutes();
                            var endDateMinute = enddate.getMinutes()<10? '0'+enddate.getMinutes():enddate.getMinutes();

							$("#displayStartDate").data("kendoDatePicker").value(
									startdate.oFormat("yyyy-MM-dd"))

							$("#displayStartHour").data("kendoDropDownList").select(startdate.getHours())

							$("#displayStartMin").data("kendoDropDownList").value(startDateMinute)

							$("#displayEndDate").data("kendoDatePicker").value(
									enddate.oFormat("yyyy-MM-dd"))

							$("#displayEndHour").data("kendoDropDownList").select(enddate.getHours())

							$("#displayEndMin").data("kendoDropDownList").value(endDateMinute)

							},
									isAction : 'select'
					});
				} else {
					$("input[name='popupType']").trigger("change");
				}

			}

			fnKendoUpload({
				id : "uploadFile",
				select : function(e) {
					let f = e.files[0];
					let ext = f.extension.toLowerCase(); // 확장자
					let kind = [ ".png", ".jpg", ".jpeg", ".gif" ]

					if (!kind.includes(ext)) {
						  fnKendoMessage({
						                    message : "사진 파일만 첨부가능합니다."
						  });
						  e.preventDefault();
					} else {
						if (e.files && e.files[0]) {
							let reader = new FileReader();

							reader.onload = function(ele) {
								// fnGetExcelUploadUser();
								// $("#uploadUserViewControl").show();
								$("#uploadUserLink").text(f.name);
								$("#popupImageOriginName").val(f.name);

								// 파일첨부 버튼 숨김처리
								// $("#fnFileUpload").css('display', 'none');
								$("#uploadFileDeleteYn").val('');
							};

							reader.readAsDataURL(f.rawFile);
						}
				}},
				localization : "파일첨부"
			});

			function fnSave() {

				var data = $('#inputForm').formSerialize(true);

				fnInputValidationForHangulAlphabetNumberSpace("popupSubject");
				var url = '/admin/display/popup/addPopup';
				var cbId = 'insert';

				if (OPER_TP_CODE == 'U') {
					url = '/admin/display/popup/putPopup';
					cbId = 'update';
				}

				var data = $('#inputForm').formSerialize(true);

				if (data.rtnValid) {

					data.displayStartDate = data.displayStartDate.slice(0, 4)
							+ "-" + data.displayStartDate.slice(4, 6) + "-"
							+ data.displayStartDate.slice(6, 8) + " "
							+ data.displayStartHour + ":"
							+ data.displayStartMin + ":" + "00"
					data.displayEndDate = data.displayEndDate.slice(0, 4) + "-"
							+ data.displayEndDate.slice(4, 6) + "-"
							+ data.displayEndDate.slice(6, 8) + " "
							+ data.displayEndHour + ":" + data.displayEndMin
							+ ":" + "59"

					// 노출기간 시작일,종료일 비교
					if(!fnValidateDatePicker(data.displayStartDate, data.displayEndDate)){
						fnKendoMessage({message : "노출기간을 확인해주세요."});
						return false;
					}

					fnAjaxSubmit({
						form : 'inputForm',
						fileUrl : '/fileUpload',
						url : url,
						storageType : "public", // 추가
						domain : "dp", // 추가
						params : data,
						success : function(data) {
							fnBizCallback(cbId, data);
						},
						isAction : 'batch'
					});
				}
			}


			function fnInitOptionBox() {
				$('#kendoPopup').kendoWindow({
					visible : false,
					modal : true
				});



				fnTagMkRadio({
					id : 'displayTargetType',
					tagId : 'displayTargetType',
					url : "/admin/comn/getCodeList",
					params : {
						"stCommonCodeMasterCode" : 'DP_TARGET_TP',
						'useYn' : "Y"
					},
					async : false,
					chkVal : 'DP_TARGET_TP.ALL',
					style : {}
				});

				fnTagMkRadio({
					id : 'displayRangeType',
					tagId : 'displayRangeType',
					url : '/admin/comn/getCodeList',
					params : {
						'stCommonCodeMasterCode' : 'DP_RANGE_TP',
						'useYn' : 'Y'
					},
					async : false,
					chkVal : 'DP_RANGE_TP.ALL',
					style : {}
				});

				fnTagMkRadio({
					id : 'todayStopYn',
					tagId : 'todayStopYn',
					data : [ {
						'CODE' : 'Y',
						'NAME' : '예'
					}, {
						'CODE' : 'N',
						'NAME' : '아니오'
					} ],
					chkVal : 'Y',
					style : {}
				});

				fnTagMkRadio({
					id : 'useYn',
					tagId : 'useYn',
					data : [ {
						'CODE' : 'Y',
						'NAME' : '예'
					}, {
						'CODE' : 'N',
						'NAME' : '아니오'
					} ],
					chkVal : 'Y',
					style : {}
				});

				fnTagMkRadio({
					id : 'popupType',
					tagId : 'popupType',
					url : '/admin/comn/getCodeList',
					params : {
						'stCommonCodeMasterCode' : 'DP_POPUP_TP',
						'useYn' : 'Y'
					},
					async : false,
					chkVal : 'DP_POPUP_TP.IMAGE',
					style : {}
				});

			}
			;

			function initDateTimePicker() {
				fnKendoDatePicker({
					id : 'displayStartDate',
					format : 'yyyy-MM-dd',
					defVal : fnGetToday(),
					defType : "oneWeek",
					change : fnOnChangeStartBeginDt
				});

				fnKendoDatePicker({
					id : 'displayEndDate',
					format : 'yyyy-MM-dd',
					btnStyle : false,
					btnStartId : 'displayStartDate',
					btnEndId : 'displayEndDate',
					defType : "oneWeek",
					defVal : fnGetDayAdd(fnGetToday(), 6),
					change : fnOnChangeStartFinishDt
				});

				fbMakeTimePicker("#displayStartHour", "start", "hour");
				fbMakeTimePicker("#displayStartMin", "start", "min");
				fbMakeTimePicker("#displayEndHour", "end", "hour");
				fbMakeTimePicker("#displayEndMin", "end", "min");

				$("#displayEndHour").data("kendoDropDownList").select(23);
				$("#displayEndMin").data("kendoDropDownList").select(59);

			}

			function fnOnChangeStartBeginDt(e) {
				fnOnChangeDatePicker(e, 'start', 'displayStartDate', 'displayEndDate');
			}
			function fnOnChangeStartFinishDt(e) {
				fnOnChangeDatePicker(e, 'end'  , 'displayStartDate', 'displayEndDate');
			}



			function bindEvent() {
				$("input[name='popupType']").on("change", function(e) {

					var checked = $(this).is(":checked");

					if (!checked)
						return;

					var _value = $(this).val();

					toggleTab(_value);

				})
			}

			$("#uploadUserDelete").on("click", function(e){
				e.preventDefault();
				$("#uploadUserViewControl").hide();
				$("#uploadUserLink").text("");
				$("#uploadFileDeleteYn").val("Y");
				$("#fnFileUpload").css('display','');
			});

			function toggleTab(value) {

				$(".tab-content").hide();

				if (value === "DP_POPUP_TP.HTML") {
					$("[data-tab='DP_POPUP_TP.HTML']").show();
					$("#html").prop("required", true);
					$("#uploadUserLink").prop("required", false);
				} else if (value === "DP_POPUP_TP.IMAGE") {
					$("#html").prop("required", false);
					$("#uploadUserLink").prop("required", true);
					$("[data-tab='DP_POPUP_TP.IMAGE']").show();
					$("#uploadUserViewControl").hide();
					$("#uploadUserLink").text("");
					$("#uploadFileDeleteYn").val("Y");
					$("#fnFileUpload").css('display','');
					if (!paramData) {
						$("#html").data("kendoEditor").value("");
					}
				} else {
					return;
				}
			}

			// 정상설정 첨부파일 버튼 클릭
			function fnFileUpload() {
				$("#uploadFile").trigger("click");
			}
			;

			function fnClose() {
				parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
			}


			/**
			 * 콜백합수
			 */
			function fnBizCallback(id, data) {
				switch (id) {
				case 'insert':
					fnKendoMessage({
						message : "저장되었습니다.",
						ok : function(e) {
							fnClose();
						}
					});
					break;

				case 'update':
					fnKendoMessage({
						message : "수정되었습니다.",
						ok : function() {
							fnClose();
						}
					});
					break;
				}
			}

			function fnDescriptionKendoEditor(opt) { // Editor

				if  ( $('#' + opt.id).data("kendoEditor") ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

					$('#' + opt.id + 'Td').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화

					var textAreaHtml = '<textarea id="' + opt.id + '" ';
					textAreaHtml += 'style="width: 100%; height: 400px" ';
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
								workKindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
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

			// ------------------------------- Common Function end
			// -------------------------------

			// ------------------------------- Html 버튼 바인딩 Start
			// -------------------------------

			$scope.fnSave = function() {
				fnSave();
			};

			$scope.fnClose = function() {
				parent.fnSearch();
			};

			$scope.fnFileUpload = function() {
				fnFileUpload();
			};// 정상설정 파일 업로드
			// ------------------------------- Html 버튼 바인딩 End
			// -------------------------------
			//
		}); // document ready - END
