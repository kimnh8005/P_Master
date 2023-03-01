/**-----------------------------------------------------------------------------
 * description 		 : 통합 대시보드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.03.31		choo   최초생성
 * @ 2021.04.16		김승우	대시보드 기능 적용
 * **/
 'use strict';

 var $document = $(document);
 var aGridDs1, aGridOpt1, aGrid1, aGridOpt1_2, aGrid1_2;
 var aGridDs1_2, aGridOpt1_2, aGrid1_2;
 var aGridDs2, aGridOpt2, aGrid2;
 var bGridDs, bGridOpt, bGrid;
 var cGridDs1, cGridOpt1, cGrid1;
 var cGridDs2, cGridOpt2, cGrid2;
 var cGridDs3, cGridOpt3, cGrid3;
 var dGridDs, dGridOpt, dGrid;
 var eGridDs, eGridOpt, eGrid;
 var fGridDs, fGridOpt, fGrid;

 // 라이브러리 로딩 여부
 var isPackeryLoaded = false;
 var isDraggabillyLoaded = false;

 var $layout = null;
 var packery = null;
 var $timestamp = null;

 // 버튼
 var $rollBackBtn = null;
 var $initBtn = null;
 var $editBtn = null;
 var $hideBtn = null;

 //  대시보드 수정 여부
 var isEdit = false;

 //  update용 데이터
 var clonedData = [];

 // 대시보드 모듈 기본 높이
 var cardHeight = 400;

 //  대시보드 모듈 간격 사이즈
 var gutterSize = 10;

 //  판매 채널 ( 통합몰, 외부몰 )
 var mallTypes = [];

 $(document).ready(function() {
	//  모듈 별 기본 정보
	const modulesInfo = {
		// 관리자 공지사항
		"bos_notice": {
			id: "bos_notice",
			url: "/admin/cs/notice/getDashboardNoticeList",
			params: null,
			options: {},
		},
		// 클레임 현황
		"order_claim": {
			id: "order_claim",
			url: "/admin/dashboard/getDashboardClaimStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 주문/매출 현황
		"order_delivery": {
			id: "order_delivery",
			url: "/admin/dashboard/getDashboardOrderSaleStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 회원가입 현황
		"sign_up": {
			id: "sign_up",
			url: "/admin/dashboard/getDashboardSignUpStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 고객 문의 처리현황
		"customer_qna": {
			id: "customer_qna",
			url: "/admin/dashboard/getDashboardCustomerQnaStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 보상제 처리현황
		"reward_apply": {
			id: "reward_apply",
			url: "/admin/dashboard/getDashboardRewardApplyStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 부정거래 탐지
		"illegal_detect": {
			id: "illegal_detect",
			url: "/admin/dashboard/getDashboardIllegalDetectStatics",
			params: {
				searchPeriodSe: "TD",
				mallTp: "",
			},
			options: {
				contentType: "application/json",
			},
		},
		// 2021.08.11 승인 관련 대시보드 화면 작업, 추후 수정 필요
		//,
		// 내 승인요청 현황
		"my_approval_request": {
			id: "my_approval_request",
			url: "/admin/dashboard/getDashboardMyApprovalRequestStatics",
			params: null,
			options: {
				contentType: "application/json",
			},
		},
		// 내 승인처리 목록
		"my_approval_accept": {
			id: "my_approval_accept",
			url: "/admin/dashboard/getDashboardMyApprovalAcceptStatics",
			params: null,
			options: {
				contentType: "application/json",
			},
		},
		// 담당자별 승인 처리 현황
		"tot_approval_accept": {
			id: "tot_approval_accept",
			url: "/admin/dashboard/getDashboardTotalApprovalAcceptStatics",
			params: null,
			options: {
				contentType: "application/json",
			},
		}

	};
	// ==========================================================================
	// # Initialize Page Call
	// ==========================================================================
	fnInitialize();

	// ==========================================================================
	// # Initialize PageR
	// ==========================================================================
	function fnInitialize(){
		// ------------------------------------------------------------------------
		// 화면기본설정
		// ------------------------------------------------------------------------
		$scope.$emit('fnIsMenu', { flag : true });

		$("#lnb, #lnb-closeBtn").css({"display": "none"});

		fnPageInfo({
			PG_ID  : 'dashboardTot',
			callback : fnUI
		});
	}

	// ==========================================================================
	// # 초기화 - 버튼
	// ==========================================================================
	function fnInitButton(){
		$('#fnOk').kendoButton();
	};

	// 확인
	function fnOk(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// ==========================================================================
	// # fnUI
	// ==========================================================================
	function fnUI(){
		fnTranslate(); // 다국어 변환--------------------------------------------
		fnLoadLibrary(); // 대시보드 라이브러리 로딩 --------------------------------------------
		fnInitRecentlyLoginNoti(); // 최근 접속정보 알림
		fnInitPopup(); // 공지사항 팝업
	}

	// ==========================================================================
	// # fnInitOptionBox
	// ==========================================================================
	function fnInitOptionBox() {
		// ------------------------------------------------------------------------
		// 주문/매출현황 판매채널
		// ------------------------------------------------------------------------
		fnTagMkRadio({
			  id      : ''
			, tagId   : ''
			, url     : '/admin/comn/getCodeList'
			, params  : {'stCommonCodeMasterCode' : 'MALL_TP', 'useYn' : 'Y'}
			, async   : true
			, isDupUrl: 'Y'
			, chkVal  : !!val ? val : 'EVENT_SURVEY_TP.SINGLE'
			, style   : {}
			, success : function(data) {},
		});
	}

	// ==========================================================================
	// # fnLoadLibrary
	// ==========================================================================
	/**
	 * Packery & Draggabilly 라이브러리 스크립트 파일 추가
	 */
	 function fnLoadLibrary() {
		_importScript("/js/lib/packery/packery.pkgd.min.js", "packery-lib", function() {
			isPackeryLoaded = true;

			if( isDraggabillyLoaded && isPackeryLoaded ) {
			   fnInitPage();
			}
		});

		_importScript("/js/lib/packery/draggabilly.pkgd.min.js", "draggabilly-lib", function() {
			isDraggabillyLoaded = true;

			if( isDraggabillyLoaded && isPackeryLoaded ) {
				fnInitPage();
			}
		});
	}
	// Library End =========================================

	// ==========================================================================
	// # fnInitPage : 대시보드 레이아웃 초기 설정
	// ==========================================================================
	function fnInitPage() {
		// 주문/매출현황 판매채널 데이터 조회 후 아래에 있는 함수들 실행
		fetchMallTypes();

		// 대시보드 정보 API 조회
		fetchDashboardInfo();

		// 업데이트 시간 초기 설정
		fnInitTimeStamp();

		// 이벤트 설정
		fnInitEvents();
	}

	// Template functions =========================================
	// ==========================================================================
	// # renderModuleTemplate : 대시보드 각 카드별 템플릿 생성 함수
	// ==========================================================================
	/**
	 * @param {String} moduleId : 해당 모듈/카드 아이디
	 * @param {Object} result : API 조회 결과 result
	 * @returns jquery template html
	 */
	function renderModuleTemplate(moduleId, result) {
		try {
			// 데이터가 없을 경우
			if( !result ) throw new Error("no template Module data : " + moduleId);

			const templateEl = $("#" + moduleId + "_contents_template");

			// 템플릿이 없을 경우
			if( !templateEl.length ) throw new Error("no template Module Id : " + moduleId);

			const template = $.template(templateEl.html());

			return $.tmpl(template, result);
		} catch (error) {
			throw error;
		}
	}

	/**
	 *
	 * @param {String} moduleId : 모듈 아이디
	 * @returns 모듈 컨텐츠 템플릿 아이디
	 */
	function getContentsTemplateId(moduleId) {
		return moduleId ? moduleId + "_contents_template" : null;
	}

	// ==========================================================================
	// # createTemplateHtml : 대시보드 각 카드별 템플릿 생성 함수
	// ==========================================================================
	/**
	 * @param {String} templateId : 템플릿 아이디
	 * @param {*} data : 템플릿에 바인딩할 데이터
	 * @returns 제이쿼리 템플릿
	 */
	function createTemplateHtml(templateId, data) {
		try {
			// 데이터가 없을 경우
			if( !data ) throw new Error("no template data : " + templateId);

			const templateEl = $("#" + templateId);

			// 템플릿이 없을 경우
			if( !templateEl.length ) throw new Error("no template element : " + templateId);

			const template = $.template(templateEl.html());

			return $.tmpl(template, data);
		} catch (error) {
			throw error;
		}
	}

	// 관리자 공지사항 createDate yyyy-mm-dd로 변경
	function subStringDate(dateString) {
		if( !dateString ) return "";

		return new Date(dateString).oFormat("yyyy-MM-dd");
	}
	// Template functions End =========================================

	// Layouts =========================================
	/**
	 * Packery 라이브러리 실행
	 * @param {Object} el : HTMLDivElement
	 * @returns Packery Object
	 */
	function initPackery(el) {
		const packery = el.packery({
			itemSelector: '.grid-item',
			columnWidth: '.grid-sizer',
			initLayout: false,
			gutter: '.gutter-sizer',
			percentPosition: true,
			resize: false,
		}).data("packery");

		packery.on("dragItemPositioned", function (draggedItem) {
			onDragEnd(draggedItem);
		});

		return packery;
	}

	// ==========================================================================
	// # initDashboardLayout : 대시보드 화면 초기 설정
	// ==========================================================================
	function initDashboardLayout(data) {
		try {
			// cloneData에 기존 데이터 복사본 저장
			clonedData = JSON.parse(JSON.stringify(data));

			$layout = $("#gridLayout");

			// Packery 라이브러리 실행
			packery = initPackery($layout);

			// 대시보드 레이아웃 렌더링
			drawDashboard($layout, data);

			// 최초 페이지 진입 시 높이 값 설정
			setDefaultHeight(packery);
		} catch (error) {
			console.log(error);
		}
	}

	// ==========================================================================
	// # drawDashboard : 대시보드 렌더링
	// ==========================================================================
	function drawDashboard($target, data) {
		$target || ($target = $("#gridLayout"));

		// 간격, 그리드 행 너비를 위해 필수
		const defaultHTML = "<div class='grid-sizer'></div><div class='gutter-sizer'></div>";

		$target.html(defaultHTML);

		data.forEach(function(item) {
			// 각 모듈 렌더링
			drawModule($target, item);
		});

		const packery = $target.data().packery;

		if( !packery ) return;

		// 각 모듈이 모두 화면에 그려지면 packery 아이템 세팅
		packery.reloadItems();

		// 각 모듈 드래그 앤 드롭 설정
		bindDraggableEvents();

		// 레이아웃 포지셔닝 설정( data-top, data-left )
		positioningDashboard();
	}

	// ==========================================================================
	// # drawModule : 모듈 렌더링
	// ==========================================================================
	/**
	 * @param {Object} $target : 모듈을 추가할 jQuery 엘리먼트
	 * @param {Object} data : 모듈 데이터
	 */
	function drawModule($target, data) {
		$target || ($target = $("#gridLayout"));

		// 모듈 엘리먼트 생성
		const $el = $("<div></div>");

		try {
			// 모듈 클래스 설정
			const _className = getModuleClassName(data.cardWidthRatio);

			// 템플릿 찾기위해서 소문자로 변환
			const dashboardCardCd = data.dashboardCardCd.toLowerCase();
			data.dashboardCardCd = dashboardCardCd;

			// 모듈 별 기본 정보
			// const moduleInfo = modulesInfo[dashboardCardCd];

			// 엘리먼트 속성 설정
			$el.attr("id", dashboardCardCd);
			$el.attr("data-id", dashboardCardCd);
			$el.attr("data-module-id", dashboardCardCd);
			$el.attr("data-sort", data.sort);
			$el.addClass(_className);

			// yaxis, xaxis 체크
			const isAxisValid = validateAxis(data);

			// 최초로 대시보드를 렌더링하거나 xaxis, yaxis값이 올바르지 않을 경우
			if( !isAxisValid || !data.modifyDt ) {
				$el.attr("data-no-axis", true);
			} else {
				// yaxis, xaxis 값이 있고, 수정한 이력이 있을 경우
				$el.attr("data-top", data.yaxis);
				$el.attr("data-left", data.xaxis);
			}

			// if( data.yaxis === null || data.xaxis === null ) {
			// 	$el.attr("data-no-axis", true);
			// } else {
			// 	// yaxis, xaxis 값이 있을 경우
			// 	$el.attr("data-top", data.yaxis);
			// 	$el.attr("data-left", data.xaxis);
			// }

			// 템플릿
			const templateEl = $("#" + dashboardCardCd + "_template");

			// 템플릿이 없을 경우
			if( !templateEl.length ) {
				throw new Error("No template. Card Id : " + dashboardCardCd);
			}

			// 모듈 기본 템플릿 렌더링
			const template = $.template(templateEl.html());
			if(data.linkUrl) data.linkUrl = location.origin + location.pathname + "#" + data.linkUrl;

			const templateString = $.tmpl(template, data);

			$el.html(templateString);
			$el.appendTo($target);

			// 모듈 > 판매 채널 탭 이벤트
			const $tabs = $el.find(".js__module__tabs");
			if( $tabs.length ) {
				// 판매 채널 라디오 탭 렌더링
				renderMallTypes(dashboardCardCd, $tabs.attr("id"));

				// 정의된 이벤트 핸들러가 있을 경우
				$tabs.on("change", "input", function(e) {
					// id
					// modulesInfo[id];
					// $(e.target).data('params-id')
					onChangeModuleItems(e);
				});
			}

			// 모듈 > 기간 설정 이벤트
			const $searchPeriodSe = $el.find("input[name='" + dashboardCardCd + "_searchPeriodSe']");

			if( $searchPeriodSe.length ) {
				const _id = $searchPeriodSe.attr("id");

				// 켄도 드롭다운 리스트 렌더링
				renderSearchPeriodList(_id);

				// 정의된 이벤트 핸들러가 있을 경우
				$searchPeriodSe.on("change", function(e) {
					onChangeModuleItems(e);
				});
			}

			// dataUrl이 있으면 모듈 API 조회
			if( data.dataUrl ) {
				// 모듈 컨텐츠 최초 설정
				initModuleContent(data.dashboardCardCd, data.dataUrl);
			}

			$hideBtn = $('.hideBtn');

			$hideBtn.on("click", function(e) {
			let dashboardCardNm = e.target.dataset.moduleNm;
			let urUserDashboardId = e.target.dataset.uruserdashboardid;

				fnKendoMessage({
					message: fnGetLangData({key: '', nullMsg: dashboardCardNm+" 대시보드를 더 이상 안 보이게 숨기기 처리 하시겠습니까?"}),
					type: "confirm",
					ok: function () {
						fnHideDashBoardModule(dashboardCardNm, urUserDashboardId);
					},
				});
		});


			fnInitTimeStamp();
		} catch (error) {
			console.error(error.message);
		}
	}

	// ==========================================================================
	// # saveLayoutData : 대시보드 현재 상태 저장
	// ==========================================================================
	function saveLayoutData() {
		const data = clonedData || [];

		if( !data.length ) return;

		try {
			const _packery = packery || $("#gridLayout").data("packery");

			const gridWidth = _packery.packer.width;
			const packeryItems = _packery.items;

			// 복사한 데이터를 기준으로 xaxis, yaxis 데이터 변경
			const newData = data.map(function(dataItem) {
				const selectedItem = packeryItems.find(function(p) {
					return p.element.dataset.id === dataItem.dashboardCardCd.toLowerCase();
				});

				const newItem = $.extend(true, {}, dataItem);

				if( selectedItem ) {
					const $element = $(selectedItem.element);
					const xaxis = selectedItem.rect.x / gridWidth;
					const _top = selectedItem.rect.y;

					const yaxis = _top%cardHeight > 0 ? (_top - _top%cardHeight)/cardHeight : _top / cardHeight;

					$element.attr("data-top", yaxis);
					$element.attr("data-left", xaxis);

					newItem.xaxis = xaxis;
					newItem.yaxis = yaxis;
				}

				return newItem;
			});

			// 서버에 데이터 전송
			updateDashboardInfo(newData);

			// localStorage.setItem("layoutData", JSON.stringify(newData));
		} catch (error) {
			console.error(error);
		}
	}

	// ==========================================================================
	// # getModuleClassName : 모듈 클래스 네임 설정 ( 비율 )
	// ==========================================================================
	function getModuleClassName(cardWidthRatio) {
		const classNames = {
			"DASHBOARD_WIDTH_TP.WIDTH_PER_25": "w25",
			"DASHBOARD_WIDTH_TP.WIDTH_PER_50": "w50",
			"DASHBOARD_WIDTH_TP.WIDTH_PER_100": "w100",
		};

		const _className =  classNames[cardWidthRatio] || "";

		return "grid-item" + " " + _className;
	}

	// ==========================================================================
	// # bindDraggableEvents : 대시보드 모듈 드래그 앤 드롭 설정
	// ==========================================================================
	function bindDraggableEvents() {
		const _packery = packery || $("#gridLayout").data("packery");

		// Packery가 없을 경우
		if( !_packery ) return;

		const $items = $(_packery.element).find(".grid-item");
		$(_packery.element).packery('bindUIDraggableEvents', $items);
	}

	// ==========================================================================
	// # positioningDashboard : 대시보드 모듈 최초 포지션 설정
	// ==========================================================================
	function positioningDashboard() {
		const _packery = packery || $("#gridLayout").data("packery");

		// Packery가 없을 경우
		if( !_packery ) return;

		_packery._resetLayout();

		const $items = _packery.getItemElements();

		// grid-item이 없을 경우
		if( !$items.length ) return;

		try {
			$items.forEach(function(item) {
				const packeryItem = _packery.getItem(item);

				try {
					const $item = $(item);

					const noAxis = $item.data("noAxis");

					//   x, y축 좌표가 null일 경우
					if ( noAxis ) {
						throw new Error("no xaxis and yaxis. Card id : " + $item.data("id"));
					}

					const left = Number($item.data("left"));
					const top = Number($item.data("top"));

					packeryItem.rect.x = left * _packery.packer.width;
					packeryItem.rect.y = top > 0 ? top * cardHeight + (gutterSize * top) : top;

					packeryItem.element.style.top = packeryItem.rect.y + "px";
					packeryItem.element.style.left = packeryItem.rect.x + "px";
				} catch (error) {
					throw error;
				}
			});

			// packery.shiftLayout();
		} catch (error) {
			console.error(error.message);

			// 기본 레이아웃 함수 실행
			packery.layout();
		}

	}

	// ==========================================================================
	// # setDefaultHeight : 대시보드 레이아웃 최초 그릴때 높이 값 설정
	// ==========================================================================
	function setDefaultHeight(packery) {
		packery || (packery = $("#gridLayout").data("packery"));

		// Packery가 없을 경우
		if( !packery ) return;

		const lastItem = packery.items.reduce(function(prev, cur) {
			return prev.rect.y > cur.rect.y ? prev : cur;
		});

		$(packery.element).height(lastItem.rect.y + $(lastItem.element).height());
	}

	// ==========================================================================
	// # startDraggable : 대시보드 모듈 드래그 앤 드롭 시작/중지
	// ==========================================================================
	function startDraggable(draggable = true) {
		const _packery = packery || $("#gridLayout").data("packery");

		const $items = $(_packery.element).find(".grid-item");

		if(draggable) {
			$items.draggable();
		}
		else {
			$items.draggable("destroy");
		}
	}

	// ==========================================================================
	// # onDragEnd : 대시보드 모듈 드롭 이벤트 핸들러
	// ==========================================================================
	function onDragEnd(item) {
		console.log(item);
	}

	window.startDraggable = startDraggable;

	function fnInitTimeStamp() {
		$timestamp = $(".timeStamp").html(new Date().oFormat("yyyy-MM-dd hh:mm:ss"));
	}

	// ------------------------------------------------------------------------
	// 주문/매출현황 조회기간 리스트 렌더링
	// ------------------------------------------------------------------------
	/**
	 * @param {String} id : 엘리먼트 아이디
	 */
	function renderSearchPeriodList(id) {
		if( !id ) return;

		fnKendoDropDownList({
			id    : id,
			tagId : id,
			data  : [
					{ CODE: 'TD', NAME: '오늘' },
					{ CODE: '1H', NAME: '1시간' },
					{ CODE: '3H', NAME: '3시간' },
					{ CODE: '6H', NAME: '6시간' },
					{ CODE: '12H', NAME: '12시간' },
					{ CODE: '1D', NAME: '1일' }
			],
			chkVal: 'TD'
		});
	}

	// ------------------------------------------------------------------------
	// 판매채널 탭 렌더링
	// ------------------------------------------------------------------------
	/**
	 * @param {String} id : 엘리먼트 아이디
	 * @param {String} moduleId : 포함된 모듈 아이디
	 */
	function renderMallTypes(moduleId, id) {
		if( !id ) return;

		// 판매 채널 데이터가 없을 경우
		if( !mallTypes ) return;

		const _mallTypes = [{
			ATTR1: "",
			ATTR2: "",
			ATTR3: "",
			CODE: "",
			COMMENT: "전체",
			NAME: "전체",
		}].concat(mallTypes);

		const $target = $("#" + id);

		if( !$target.length ) return;

		const htmlString =  _mallTypes.map(function(mallTp) {
			let tag = '<li class="module__tab"><label>';

			if( !mallTp.CODE ) {
				tag += '<input type="radio" name="' + moduleId + '_types" value="' + mallTp.CODE + '" data-param-key="mallTp" class="js__params" checked>';
			} else {
				tag += '<input type="radio" name="' + moduleId + '_types" value="' + mallTp.CODE + '" data-param-key="mallTp" class="js__params">';
			}

			tag += '<span>' + mallTp.NAME + '</span></label></li>';

			return tag;
		}).join("");

		$target.html(htmlString);
	}

	// Layouts End =========================================

	// ==========================================================================
	// # fnInitEvents : 대시보드 이벤트 설정
	// ==========================================================================
	function fnInitEvents() {

		// 대시보드 > 전체 초기화 버튼
		$initBtn = $("#init");

		$initBtn.on("click", function(e) {
				fnKendoMessage({
					message: fnGetLangData({key: '', nullMsg: "전체 대시보드를 초기화 시키겠습니까?"}),
					type: "confirm",
					ok: function () {
						fnInitDashBoardModule();
					},
				});
		});

		// 대시보드 > 모듈 설정 버튼
		$editBtn = $("#edit");

		$editBtn.on("click", function(e) {
			isEdit = !isEdit;
			
			if( isEdit ) { // 수정모드 O
				$(this).html("저장");
				$("#gridLayout").addClass("isEditing");
				$initBtn.prop("disabled", true); // 전체 초기화 버튼 비활성화
				$hideBtn.prop("disabled", true); // 숨기기 버튼 비활성화

			} else { // 수정모드 X
				$(this).html("모듈 설정");
				$("#gridLayout").removeClass("isEditing");
				$initBtn.prop("disabled", false); // 전체 초기화 버튼 활성화
				$hideBtn.prop("disabled", false); // 숨기기 버튼 활성화

				fnKendoMessage({
					message : fnGetLangData({key : '', nullMsg : "변경 사항을 저장 하시겠습니까?"}),
					type: "confirm",
					ok: function() {
						fnSave();
					},
				});
			}

			startDraggable(isEdit);
		});

		// 대시보드 > 되돌리기 버튼
		$rollBackBtn = $("#rollBack");

		$rollBackBtn.on("click", function(e) {
			if( isEdit ) {	// 모듈 설정 상태일 때는 모듈 rollBack
				isEdit = false;
				$("#edit").html("모듈 설정");
				$("#gridLayout").removeClass("isEditing");
				startDraggable(false);

				positioningDashboard();
			} else { // 전체 데이터 refresh
				// 주문/매출현황 판매채널 데이터 조회 후 아래에 있는 함수들 실행
				fetchMallTypes();

				// 대시보드 정보 API 조회
				fetchDashboardInfo();

				// 업데이트 시간 초기 설정
				fnInitTimeStamp();
			}
		});

		// 모듈 > 링크(more) 버튼 이벤트
		// $document.on("click", ".js__module__link",function(e) {
		// 	e.preventDefault();
		// 	const $target = $(e.target);

		// 	const _href = $target.attr("href");

		// 	if( !_href ) return;

		// 	const url = location.pathname + "#" + _href;

		// 	location.href = url;
		// });


		// 윈도우 리사이즈 이벤트 발생 시 포지션 재 조정
		var timeOutFn = null;

		$(window).on("resize", function(e) {
			// 디바운스
			clearTimeout(timeOutFn);

			timeOutFn = setTimeout(function() {
				positioningDashboard();
			}, 0);
		});
	}
	// Events End =========================================

	function fnInitDashBoardModule(){
		const option = {
			url: "/admin/dashboard/initDashboardList",
			// method: "POST",
			contentType: "application/json",
			async: true,
			params: {
				dashboardList: [],
			},
			success: function(data, code) {
				fnKendoMessage({
					message : fnGetLangData({key : '', nullMsg : "모든 대시보드의 데이터가 초기화 및 업데이트 되었습니다." }),
					ok: function() {
						fetchDashboardInfo();
					},
				});
			},
		};

		fnAjax(option);
	};

	function fnHideDashBoardModule(dashboardCardNm, urUserDashboardId){
		const option = {
			url: "/admin/dashboard/delDashboardCard",
			// method: "POST",
			contentType: "application/json",
			async: true,
			params: {
				urUserDashboardId: urUserDashboardId,
			},
			success: function(data, code) {
				fnKendoMessage({
					message : dashboardCardNm + " 대시보드가 숨기기 처리되었습니다.</br> 전체 초기화 버튼으로 다시 불러올 수 있습니다.",
					ok: function() {
						fetchDashboardInfo();
					},
				});
			},
		};

		fnAjax(option);
	};

	function fnInitRecentlyLoginNoti(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true,
			activate: function() {
				const self = this;

				$('.k-overlay').on('click', function(e) {
					self.close();
				});
			},
			deactivate: function() {
				$('.k-overlay').off('click');
			},
		});

		//최근 접속 정보
		$.ajax({
			url: "/admin/comn/popup/getRecentlyLoginData",
			type: 'POST',
			dataType: 'json',
			success:
				function (data) {
					var chkVal = data.code;
					switch (chkVal) {
						case "0000":
							fnKendoPopup(data.data.recentlyLoginResultVo); // 최근 접속 정보 알림 팝업
							break;

						case "LOGIN_NO_DATA": //회원 데이터 없음
							fnKendoPopup(data.data);
							break;

						default :
							fnMessage(data.message);
							break;
					}
				}
			});
	}

	function fnInitPopup(){
		// 공지사항 리스트 팝업
		$.ajax({
			url: "/admin/comn/popup/getNoticePopupList",
			type: 'POST',
			dataType: 'json',
			success:
				function (data) {
					var chkVal = data.code;
					switch (chkVal) {
						case "0000":
							for(var i=0; i<data.data.getNoticePopupListResultVo.length; i++) {
								fnDashboardNoticePopupList(data.data.getNoticePopupListResultVo[i]);
							}
							break;

						default :
							fnMessage(data.message);
							break;
				}
			}
		});
	}


	// API ------------------------------------------------------------------------
	// ------------------------------------------------------------------------
	// 주문/매출현황 판매채널 데이터 조회
	// ------------------------------------------------------------------------
	function fetchMallTypes(onSuccess) {
		fnAjax({
			url: '/admin/comn/getCodeList',
			method: "GET",
			async: false,
			params: {'stCommonCodeMasterCode' : 'MALL_TP', 'useYn' : 'Y'},
			success: function(data, code) {
				mallTypes = data.rows;

				if( typeof onSuccess === "function" ) {
					onSuccess(data, code);
				}
			},
		})
	}

	// ==========================================================================
	// 주문/매출현황
	// ==========================================================================
	function fnInitOrderSaleStatics() {
    // ----------------------------------------------------------------------
    // 파라미터
    // 몰구분(mallTp)               : 전체('')/통합몰(MALL_TP.MALL)/외부몰(MALL_TP.OUTSOURCE)
    // 조회기간구분(searchPeriodSe) : 오늘(TD), 1시간(1H), 3시간(3H), 6시간(6H), 12시간(12H), 1일(1D)
    // ----------------------------------------------------------------------
    var data;

    $.ajax({
        url     : '/admin/dashboard/getDashboardOrderSaleStatics'
      , type    : 'POST'
      , dataType: 'json'
      , data    : data
      , success : function (data) {
                    //var chkVal = data.code;
                    //switch (chkVal) {
                    //  case "0000":
                    //                for(var i=0; i<data.data.getNoticePopupListResultVo.length; i++) {
                    //                  fnDashboardNoticePopupList(data.data.getNoticePopupListResultVo[i]);
                    //                }
                    //    break;
                    //  default :
                    //                fnMessage(data.message);
                    //    break;
                    //}
                  }
    });

	}

	// ==========================================================================
	// 클레임현황
	// ==========================================================================
	function fnInitClaimStatics() {

		// ----------------------------------------------------------------------
		// 파라미터
		// 몰구분(mallTp)               : 전체('')/통합몰(MALL_TP.MALL)/외부몰(MALL_TP.OUTSOURCE)
		// 조회기간구분(searchPeriodSe) : 오늘(TD), 1시간(1H), 3시간(3H), 6시간(6H), 12시간(12H), 1일(1D)
		// ----------------------------------------------------------------------
		var data;

		$.ajax({
			url     : '/admin/dashboard/getDashboardClaimStatics'
		, type    : 'POST'
		, dataType: 'json'
		, data    : data
		, success : function (data) {
						//var chkVal = data.code;
						//switch (chkVal) {
						//  case "0000":
						//                for(var i=0; i<data.data.getNoticePopupListResultVo.length; i++) {
						//                  fnDashboardNoticePopupList(data.data.getNoticePopupListResultVo[i]);
						//                }
						//    break;
						//  default :
						//                fnMessage(data.message);
						//    break;
						//}
					}
		});
	}
	// API End ------------------------------------------------------------------------

	//쿠키 가져옴
	function getCookiePop(name){
		var cName = name + "=";
		var x = 0;

		while(x <= document.cookie.length)
		{
			var y = (x+cName.length);

			if(document.cookie.substring(x, y) == cName)
			{
				var endOfCookie = document.cookie.indexOf(";", y);

				if(endOfCookie == -1){
					endOfCookie = document.cookie.length;
				}

				return unescape(document.cookie.substring(y, endOfCookie));
			}
			x = document.cookie.indexOf(" ", x ) + 1;

			if (x == 0)
			break;
		}
		return "";
	}

	//최근 접속 정보 알림 팝업
	function fnKendoPopup(params){

		if( params == null ){
			$("#inputForm").css("display", "none");
		}
		else {
			$("label[for = 'loginName']").text(params.loginName == null  ? '' : params.loginName ); // 회원명
			$("#loginDate").val(params.loginDate == null ? '' : params.loginDate); // 로그인 시간
			$("#addressIp").val(params.addressIp == null  ? '' : params.addressIp); // 접속 IP
			$("#firstConnect").css("display", "none");
		}
		fnKendoInputPoup({id:"kendoPopup",height:"250px" ,width:"640px", param  : params, title:{nullMsg :'최근 접속 정보 알림'} });
	}

	function fnOk(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//공지사항 팝업
	function fnDashboardNoticePopupList(params){
		var popupCoordinateX = params.popupCoordinateX; // 좌측
		var popupCoordinateY = params.popupCoordinateY; // 상단
		var csCompanyBbsId = getCookiePop("csCompanyBbsId_"+params.csCompanyBbsId); //csCompanyBbsId 쿠키 get

		if(csCompanyBbsId != params.csCompanyBbsId){
			window.open("#/dashboardNoticePopup?csCompanyBbsId="+params.csCompanyBbsId,"_blank","width=400, height=400, resizable=1, fullscreen=no, left="+popupCoordinateX+", top="+popupCoordinateY+"");
		}
	}

	// 자바스크립트 파일 로드
	function _importScript(path, id, callback) {
		var script = document.createElement("script");

		script.type = "text/javascript";
		script.src = path;
		script.id = id || "";
		if(typeof callback == "function"){
			script.onload = function() {
				callback();
			}
		}

		document.getElementsByTagName("head")[0].appendChild(script);
	}

	// ==========================================================================
	// # 저장처리
	// ==========================================================================
	function fnSave() {
		saveLayoutData();
	}

	// ==========================================================================
	// # API
	// ==========================================================================
	/**
	 *
	 * @param {String} url : 카드/모듈의 dataUrl
	 * @param {Function} callback : API 조회 성공 시 실행할 함수
	 */
	function _fetchModuleData(url, callback) {
		const option = {
			url: url,
			// method: "POST",
			async: true,
			success: function(data, code) {
				if( "function" === typeof callback ) {
					callback(data.rows, code);
				}
			}
		};

		fnAjax(option);
	}

	// ==========================================================================
	// # initModuleContent : 대시보드 모듈 컨텐츠 최초 렌더링
	// ==========================================================================
	/**
	 * @param {Object} moduleData : 대시보드 모듈 데이터
	 */
	function initModuleContent(moduleId, dataUrl) {
		try {
			moduleId = moduleId.toLowerCase();

			// 모듈 아이디가 없을 경우
			if( !moduleId ) throw new Error("initModuleContent, 모듈 아이디가 없습니다. Module : " + JSON.stringify(moduleData));

			const moduleOptions = modulesInfo[moduleId];

			// 정의된 모듈 정보가 없을 경우
			if( !moduleOptions ) throw new Error("initModuleContent, 정의된 모듈 정보가 없습니다. Module Id : " + moduleId);

			if( !moduleOptions.url ) {
				moduleOptions.url = dataUrl;
			}

			// ajax 요청 option 생성
			const requestOption = $.extend({}, moduleOptions.options, {
				url: moduleOptions.url,
			});

			// 파라미터가 있을 경우 파라미터 추가
			if( moduleOptions.params ) {
				requestOption.params = moduleOptions.params;
			}

			// 요청 success 함수
			requestOption.success = function(data) {
				// 컨텐츠 렌더링
				renderModuleContent(moduleId, data, requestOption.params);
			};

			fnAjax(requestOption);
		} catch (error) {
			console.error(error.message);
		}
	}

	// ==========================================================================
	// # onSuccessFetchModule :
	// ==========================================================================
	function onSuccessFetchModule(data) {

	}

	// ==========================================================================
	// # checkIsModuleElement : 렌더링할 모듈 및 모듈 컨텐츠 영역이 있는지 체크
	// ==========================================================================
	function checkIsModuleElement(moduleId) {
		const $module = $("#" + moduleId);
		const $contentTarget = $("#" + moduleId + "_content");

		// 모듈이나 컨텐츠를 그릴 영역이 없는 경우
		if( !$module.length || !$contentTarget.length ) {
			throw new Error("모듈 또는 모듈 컨텐츠 영역이 없습니다. Module Id : " + moduleId);
		}
	}

	// ==========================================================================
	// # renderModuleContent : 모듈 컨텐츠 렌더링 함수
	// ==========================================================================
	/**
	 * @param {String} moduleId : 모듈 아이디
	 * @param {Object} data : 모듈 별 API 요청 결과
	 * @param {Object} params : 모듈 별 API 요청 파라미터 또는 추가 변수
	 */
	function renderModuleContent(moduleId, data, params) {
		// 각 모듈 별 렌더링 함수
		const renderFns = {
			"bos_notice": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.rows);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"order_claim": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.claimStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"order_delivery": function(id, result, parameter) {
				const htmlString = renderModuleTemplate(id, result.orderSaleStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);

				if( !parameter ) return;

				// 통합몰/외부몰일 경우
				if( parameter.mallTp ) {
					// 해당하는 열을 제외한 열 hide
					const $tr = $contentTarget.find("tbody tr").not("[data-mall-tp='" + parameter.mallTp +"']");
					$tr.hide();
				}
			},
			"sign_up": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.signUpStatics);
				const $contentTarget = $("#" + moduleId + "_content");
				$contentTarget.html(htmlString);
			},
			"customer_qna": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.customerQnaStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"reward_apply": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.rewardApplyStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"illegal_detect": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.illegalDetectStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			// 2021.08.11 승인 관련 대시보드 화면 작업, 추후 수정 필요
			// ,
			"my_approval_request": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.myApprovalRequestStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"my_approval_accept": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.myApprovalAcceptStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			},
			"tot_approval_accept": function(id, result) {
				const htmlString = renderModuleTemplate(id, result.totApprovalAcceptStatics);
				const $contentTarget = $("#" + moduleId + "_content");

				$contentTarget.html(htmlString);
			}
		}
		try {
			const args = arguments;

			checkIsModuleElement(moduleId);

			// 모듈 별 렌더링 함수
			const renderFunction = renderFns[moduleId];

			if( !renderFunction ) throw new Error("렌더링 함수가 정의되어 있지 않습니다. Module Id :" + moduleId);

			// args
			renderFunction(moduleId, data, params);
		} catch (error) {
			console.error(error.message);
		}
	}

	// ==========================================================================
	// # onChangeModuleItems : 모듈 내 change 이벤트 핸들러
	// ==========================================================================
	/**
	 * @param {Object} e : Event 객체
	 */
	function onChangeModuleItems(e) {
		const $target = $(e.target);

		const $module = $target.closest(".grid-item");

		if( !$module.length ) return;

		const moduleId = $module.attr("data-module-id");

		const moduleOptions = modulesInfo[moduleId];

		// 정의된 모듈 정보가 없을 경우
		if( !moduleOptions ) return;

		const $paramElements = $module.find("[data-param-key]");

		if( !$paramElements.length ) return;

		const newParams = createModuleParams($paramElements);

		const requestOption = $.extend(moduleOptions.options, {
			url: moduleOptions.url,
			params: moduleOptions.params,
		});

		if( Object.keys(newParams).length ) {
			requestOption.params = $.extend({}, requestOption.params, newParams);
		}

		requestOption.success = function(data) {
			// 여기에 렌더링함수
			renderModuleContent(moduleId, data, requestOption.params);
		}

		fnAjax(requestOption);
	}

	// ==========================================================================
	// # createModuleParams : 모듈 API 조회에 사용할 파라미터 생성
	// ==========================================================================
	/**
	 * @param {Array} $elements : jquery 엘리먼트 배열
	 * @returns
	 */
	function createModuleParams($elements) {
		if( !$elements.length ) return null;

		const newParams = {};

		$elements.each(function() {
			// this는 각 input 엘리먼트
			const $el = $(this);
			const _type = $el.attr("type");
			const _key = $el.attr("data-param-key");
			const _value = $el.val();

			switch(_type) {
				case "radio":
					if( $el.is(":checked") ) {
						newParams[_key] = _value;
					}
					break;
				case "checkbox":
					if( $el.is(":checked") ) {
						if( !newParams[_key] ) {
							newParams[_key] = [];
						}

						newParams[_key].push(_value);
					}
					break;
				default:
					newParams[_key] = _value;
					break;
			}
		});

		return newParams;
	}

	/**
	 * 대시보드 모듈 리스트 API 조회
	 */
	 function fetchDashboardInfo() {
		const option = {
			url: "/admin/dashboard/selectDashboardList",
			method: "POST",
			async: true,
			success: function(data, code) {
				initDashboardLayout(data.rows);
			}
		};

		fnAjax(option);
	}

	/**
	 * 대시모드 모듈 리스트 업데이트 API
	 * @param {Array} list : 전송할 배열
	 */
	function updateDashboardInfo(list) {
		const option = {
			url: "/admin/dashboard/putDashboardList",
			// method: "POST",
			contentType: "application/json",
			async: true,
			params: {
				dashboardList: list,
			},
			success: function(data, code) {
				fnKendoMessage({
					message : fnGetLangData({key : '', nullMsg : "저장되었습니다." }),
					ok: function() {
						fetchDashboardInfo();
					},
				});
			},
		};

		fnAjax(option);
	}
	// ==========================================================================
	// # API End
	// ==========================================================================

	function commafy(value) {
		if( !isNumber(value) ) return "";

		return value.toLocaleString();
	}

	/**
	 * 대시보드 xaxis, yaxis validation 체크
	 * @param {Object} moduleData : 대시보드 정보
	 * @returns Boolean
	 */
	function validateAxis(moduleData) {
		if( !moduleData ) return false;

		return isNumber(moduleData.xaxis) && isNumber(moduleData.yaxis);
	}

	function isNumber(value) {
		if( value === null || typeof value === "boolean" || value === undefined || Array.isArray(value) || value === "" ) return false;

		return (typeof value === "number" || typeof Number(value) === "number") && isFinite(value);
	}

	// ==========================================================================
	// # JQuery 템플릿 초기화 함수
	//  @param {object} el : 템플릿이 추가될 타겟, 제이쿼리 엘리먼트
	//  @param {string} template : 데이터 바인딩 시 사용할 제이쿼리 템플릿, html 태그 또는 제이쿼리 엘리먼트
	// ==========================================================================
	function createTemplateItem({ el, template }) {
		if (!el || !template) {
			console.warn('error', el, template);
			return {};
		}

		const $el = $(el);
		const _template = $(template).html();

		return {
			el      : $el
			, template: $.template(_template)
			, render  : function (data, callback) {
							const tmpl = $.tmpl(this.template, data);
							this.el.empty();
							tmpl.appendTo(this.el);

							if (typeof callback === 'function') {
								callback.call(this, data);
							}
						}
			, add     : function (data, callback) {
							const tmpl = $.tmpl(this.template, data);
							tmpl.appendTo(this.el);

							if (typeof callback === 'function') {
								callback.call(this, data);
							}
						}
		};
	}


	 //------------------------------- Html 버튼 바인딩  Start -------------------------------

	 $scope.fnOk = function( ){  fnOk();}; // 확인
	 $scope.fnSave = function() {fnSave();} //저장
	 $scope.commafy = function(value) {
		return commafy(value);
	 }
	 $scope.subStringDate = function(dateString) {
		return subStringDate(dateString);
	 }

	 //------------------------------- Html 버튼 바인딩  End -------------------------------

 }); // document ready - END
