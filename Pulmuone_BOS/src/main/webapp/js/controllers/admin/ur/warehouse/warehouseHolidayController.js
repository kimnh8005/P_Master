/**-----------------------------------------------------------------------------
 * description 		 : 출고처 휴일 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.09		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var cGridDs, cGridOpt, cGrid;
var putUrWarehouseId;
var putHoliday;
var putGroupYn;
var putDawnYn;

//Constants
// 캘린더 이벤트 소스 아이디 => 이 아이디를 가지고 이벤트소스를 삭제 후 새로운 데이터로 계속 갱신
var CALENDARPAGE_SIZE = 10000;
var SOURCE_ID = "test";
var FULL_DATE_FORMAT = "yyyy-MM-dd"
var DATE_FORMAT = "yyyy-MM";
var VIEW_TYPE = {
	month: "dayGridMonth",
	week: "dayGridWeek"
}

// 날짜 변수 세팅
var todayDate = new Date();

// 캘린더 초기 날짜 설정 변수
var initialDate = todayDate.oFormat(FULL_DATE_FORMAT);

// 다음달
var nextDate = todayDate.getNextMonth();
// 이전달
var prevDate = todayDate.getPrevMonth();
var datePicker = null;
var popupPicker = null;

// 출고처 개수 담을 객체
var counts = {
	next: 0,
	current: 0,
	prev: 0
}


$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'warehouseHoliday',
			callback : fnUI
		});


	}

	$("#holidaysYn").click(function(){

		if($("#holidaysYn").prop("checked")==false){
			$('#holidaysDiv').hide();
			$('#holidayDiv').show();
			$("#holidaysYn").val('N');
		}else{
			$('#holidaysDiv').show();
			$('#holidayDiv').hide();
			$("#holidaysYn").val('Y');
			$("#startSetHolidays").val('');
			$("#endSetHolidays").val('');

			if($('#holidayDate').val() != null){
				$('#startSetHolidays').val($('#holidayDate').val());
			}
		}
	});


	function fnUI(){

		initCalendar();
		initDatePicker();
		bindEvents();
		render();
		$scope.fnCalendarSearch = fnCalendarSearch;

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------
		fnInitPopupGrid();
		initList();						// 출고처 휴일 설정 리스트 화면 숨기기
		fnInitOptionBox();//Initialize Option Box ------------------------------------

		chkHolidayInit();

		fnCalendarSearch();

	}

	function initList(){

		$('#searchForm').hide();
		$("#listDiv").hide();
	}


	function chkHolidayInit(){

		$('#holidaysDiv').hide();
		$('#holidayDiv').show();
		$("#holidaysYn").val('N');
	}

	//Functions..
	// 화면 렌더링
	function render() {
		renderDate();
		renderCount();

		// destroy를 안하면 dayCellDidMount훅을 타지 않는다.
		var type = calendar.getViewType();

		calendar.destroy().render();
		nextCalendar.destroy().render();

		// 타입이 목록으로 보기면 Calendar에서 제공하는 기본 헤더를 보여준다.
		if (type === "dayGridWeek") {
			toggleHeader(true);
		} else {
			toggleHeader(false);
		}
	}

	// 데이트피커 초기화
	function initDatePicker() {
		datePicker = fnKendoDatePicker({
			id: "datePicker",
			format: FULL_DATE_FORMAT,
			change: changeDatePicker
		});

		popupPicker = fnKendoDatePicker({
			id: "popupPicker",
			format: FULL_DATE_FORMAT,
			defVal: initialDate
		});
	}

	// 켄도데이트피커 클릭 이벤트
	function changeDatePicker(e) {

		var pickedDate = e.sender.value();

		if (!pickedDate instanceof Date || !pickedDate) return;

		// 선택된 날짜를 기준으로 todayDate, prevDate, nextDate 값 변경
		todayDate = pickedDate;
		prevDate = todayDate.getPrevMonth();
		nextDate = todayDate.getNextMonth();

		// 선택된 날짜를 기준으로 출고처 개수를 변경
		counts.current = getWarehouseCount(todayDate.getYear(), todayDate.getMonth());
		counts.prev = getWarehouseCount(prevDate.getYear(), prevDate.getMonth());
		counts.next = getWarehouseCount(nextDate.getYear(), nextDate.getMonth());

		// 선택된 날짜로 달력 날짜 변경
		calendar.setDate(todayDate);
		nextCalendar.setDate(nextDate);

		// 화면 전체 다시 렌더링
		render();
	}

	// 이벤트 바인딩
	function bindEvents() {
		// 달력 네비게이션 버튼
		var $nextBtn = $("#calendar-next");
		var $prevBtn = $("#calendar-prev");

		//목록으로 보기 버튼
		var calendarToggleView = $("#calendarToggleView");
		var toggleView = $("#toggleView");

		// 휴일 등록 버튼
		var openPopupBtn = $("#openPopupBtn");

		// 조회 버튼
		var searchBtn = $("#searchBtn");
		var clearBtn = $("#clearBtn")

		// 팝업 버튼
		var popupCancelBtn = $("#cancelHoliday");
		var popupSaveBtn = $("#saveHoliday");

		// 팝업 Form
		var $popupForm = $("#popupForm");


		// 목록으로 보기 상단 버튼
		var $topTodayBtn = $("#calendar .fc-today-button");
		var $topPrevBtn = $("#calendar .fc-prev-button");
		var $topNextBtn = $("#calendar .fc-next-button");



		// 다음 달로 이동
		$nextBtn.on("click", function (e) {
			// 날짜 변경
			prevDate = todayDate;
			todayDate = nextDate;
			nextDate = todayDate.getNextMonth();

			// 출고처 개수 변경
			counts.prev = counts.current;
			counts.current = counts.next;
			counts.next = getWarehouseCount(nextDate.getYear(), nextDate.getMonth());

			$("#datePicker").data("kendoDatePicker").value(todayDate);
			calendar.next();
			nextCalendar.next();
			renderDate();
			renderCount();
		})

		// 이전 달로 이동
		$prevBtn.on("click", function (e) {
			// 날짜 변경
			nextDate = todayDate;
			todayDate = prevDate;
			prevDate = todayDate.getPrevMonth();

			// 출고처 개수 변경
			counts.next = counts.current;
			counts.current = counts.prev;
			counts.prev = getWarehouseCount(prevDate.getYear(), prevDate.getMonth());

			$("#datePicker").data("kendoDatePicker").value(todayDate);
			calendar.prev();
			nextCalendar.prev();
			renderDate();
			renderCount();
		})

		// 목록으로 보기 클릭 이벤트 => 리스트 뷰 타입 변경
		calendarToggleView.on("change", function (e) {

			var checked = e.target.checked;
			if (checked) {
				$("#calendarDiv").hide();
				$("#calendarUnderDiv").hide();
				$('#calendarSearchForm').hide();
				$("#searchForm").show();
				$("#listDiv").show();
				fnClear();
                $("#listYn").val('Y');
				$('input:checkbox[name="toggleView"]').prop("checked", true);
				fnSearch();		// 출고처 휴일 리스트 목록 조회
			}
		})

		// 목록으로 보기 클릭 이벤트 => 달력 뷰 타입 변경
		toggleView.on("change", function (e) {

			var checked = e.target.checked;
			if (!checked) {
				$("#calendarDiv").show();
				$("#calendarUnderDiv").show();
				$('#calendarSearchForm').show();
				$("#searchForm").hide();
				$("#listDiv").hide();
				 $("#listYn").val('N');
				fnCalendarClear();
				fnCalendarSearch();		// 출고처 휴일 Calendar 조회
				$('input:checkbox[name="calendarToggleView"]').prop("checked", false);
			}
		})

		// 휴일 등록 이벤트
		openPopupBtn.on("click", function (e) {

			$('#holidayDate').val('');
			$('input:checkbox[name="holidaysYn"]').prop("checked", false);
			fnNew();
		})

		// 조회 버튼 이벤트
		searchBtn.on("click", function (e) {
			fnCalendarSearch();
		})

		// 초기화 버튼
		clearBtn.on("click", function (e) {
			fnCalendarClear();
		})


		// 팝업 취소 버튼
		popupCancelBtn.on("click", function (e) {
			$popupForm.formClear(true);
		})

		// 팝업 저장 버튼
		popupSaveBtn.on("click", function (e) {
			e.preventDefault();

			var _date = $("#popupPicker").val();
			var _title = $("#holidayName").val();

			if (!_date || !_title) {
				fnKendoMessage({
					message: "날짜 혹은 출고처 이름을 입력해주세요.",
				})
				return;
			}

			// 달력에 출고처 데이터 변경 => ajax통신으로 변경
			calendar.setEvent(_title, _date, SOURCE_ID);
			nextCalendar.setEvent(_title, _date, SOURCE_ID);

			// 출고처 개수 갱신
			counts.prev = getWarehouseCount(prevDate.getYear(), prevDate.getMonth());
			counts.current = getWarehouseCount(todayDate.getYear(), todayDate.getMonth());
			counts.next = getWarehouseCount(nextDate.getYear(), nextDate.getMonth());

			render();

			fnKendoMessage({
				message: "저장되었습니다.",
				ok: function (e) {
					fnClose();
				}
			})
		})


	}

	/******************* FullCalendar Functions *******************/
	// FullCalendar 생성
	function initCalendar() {
		// 이번달 달력
		var calendarEl = $("#calendar")[0];
		var calendarOpt = {
			// events를 기본적으로 빈 배열로 초기화해야 달력이 그려진다.
			eventSources: [{
				id: SOURCE_ID,
				events: [],
			}],
			eventContent: eventContent,
			eventDidMount: eventDidMount,
			dayCellDidMount: dayCellDidMount,
			eventClassNames: "fb__calendar__event",
			height: 860,
			// 달력 셀 선택시 발생하는 이벤트
			select: onSelectCell,
			// 이벤트 => 출고처 제목 클릭시 발생하는 이벤트
			eventClick: onClickEventCell,
			// 초기 설정 날짜
			initialDate: initialDate ? initialDate : null,
			headerToolbar : null,
		}

		var calendar = new FbCalendar({ target: calendarEl, options: calendarOpt });
		window.calendar = calendar;


		//다음달 달력
		var nextCalendarEl = $("#calendar--next")[0];
		var nextCalendarOpt = {
			eventSources: [{
				id: SOURCE_ID,
				events: [],
			}],
			eventContent: eventContent,
			eventDidMount: eventDidMount,
			dayCellDidMount: dayCellDidMount,
			eventClassNames: "fb__calendar__event",
			height: 860,
			select: onSelectCell,
			eventClick: onClickEventCell,
			initialDate: nextDate ? nextDate : null,
			headerToolbar : null,
		}

		var nextCalendar = new FbCalendar({ target: nextCalendarEl, options: nextCalendarOpt });
		window.nextCalendar = nextCalendar;
	}

	// 지난달, 이번달, 다음달 출력
	function renderDate() {
		// 상단 달력 헤더
		var $current = $("#currentDate");
		var $prev = $("#prevDate");
		var $next = $("#nextDate")

		// 하단 달력 헤더
		var $nextCurrent = $("#next-currentDate");

		$prev.html(prevDate.oFormat(DATE_FORMAT));
		$next.html(nextDate.oFormat(DATE_FORMAT));
		$current.html(todayDate.oFormat(DATE_FORMAT));
		$nextCurrent.html(nextDate.oFormat(DATE_FORMAT));
	}

	// 달 별 출고처 개수 출력
	function renderCount() {
		var $next = $("#nextCount");
		var $prev = $("#prevCount");
		var $current = $("#currentCount");

		var $nextCurrent = $("#next-currentCount");

		$next.html("(" + counts.next + ")");
		$prev.html("(" + counts.prev + ")");
		$current.html("(" + counts.current + ")");
		$nextCurrent.html("(" + counts.next + ")");
	}



	// 달력 리스트로 표현 시 헤더 노출
	function toggleHeader(toggle) {
		var $el = $(".fc-header-toolbar");

		$el.each(function () {
			var self = $(this);
			var _visibility = toggle ? "visible" : "hidden";

			self.css("visibility", _visibility);
		})
	}

	// Calendar 이벤트(타이틀)가 매핑되는 템플릿
	function eventContent(arg) {
		const extendedProps = arg.event.extendedProps;
		const groupsKeys = Object.keys(extendedProps);

		// 캘린더 셀에 추가할 노드 배열
		const arrayOfDomNodes = [];

		// 출고처 목록 데이터가 있을 경우
		if(groupsKeys.length) {
			// groupId: 그룹 구분 키 값
			// holidayList: 그룹 별 휴일 목록
			for(const [groupId, holidayList] of Object.entries(extendedProps)) {
				const el = document.createElement("ul");
				el.classList.add("event-list");

				if(groupId !== 'no-group') {
					el.setAttribute('data-group-id', groupId);
					el.classList.add("group-list");
				}

				el.innerHTML = holidayList.length ? holidayList.map((item) => {
					const id = `${item.urWarehouseId},${item.groupYn},${item.dawnDlvryYn}`;

					// 그룹이 있을 경우
					if(item.groupYn === 'Y'){
						return `<li class="event-item group" data-id="${id}">${item.warehouseName}</li>`;
					}else{
						return `<li class="event-item" data-id="${id}">${item.warehouseName}</li>`;
					}
				}).join('') : '';

				arrayOfDomNodes.push(el);
			}
		}

		if (fnIsProgramAuth("SAVE")) {
            var button = document.createElement("button");
            button.className = "btn-s btn-white add-btn";
            button.innerHTML = "추가"
            arrayOfDomNodes.push(button);
        }

		return { domNodes: arrayOfDomNodes };
	}

	// 캘린더 이벤트가 모두 렌더링되고 실행되는 함수
	function eventDidMount(arg) {
		var $td = arg.el.closest("td");
		var buttonOverlay = $td.querySelector(".button-overlay");

		// 이벤트가 있으면, 추가 버튼을 display : none 처리한다.
		if (buttonOverlay) {
			buttonOverlay.style.display = "none";
		}

		return true;
	}

	// 캘린더 각 셀이 렌더링되고 실행되는 함수
	function dayCellDidMount(arg) {
		var el = arg.el;
		var cell = el.querySelector(".fc-daygrid-day-frame");

		var div = document.createElement("div");
		div.className = "button-overlay";

		if (fnIsProgramAuth("SAVE")) {
			var button = document.createElement("button");
			button.className = "btn-s btn-white";
			button.innerHTML = "추가"

			div.appendChild(button);
		}

		cell.appendChild(div);
		return true;
	}

	// 달력 셀 클릭 이벤트
	function onSelectCell(info) {
		console.log("달력 셀 클릭 이벤트");
		var start = info.startStr;
//		var $el = this.el;
//		var _list = null;

		// 해당 셀이 포함되어있는 달력 객체를 가져온다.
//		var _calendar = $el === calendar.el ? calendar : nextCalendar;

		// 해당 날짜에 출고처 리스트가 있는지 체크한다.
//		var _list = _calendar.getEventsByDate(start);

//		console.log("_list : " + _list);
		// 출고처 리스트가 없으면 등록 팝업
//		if (!_list) {
			chkHolidayInit();
			$('input:checkbox[name="holidaysYn"]').prop("checked", false);
			fnNew(start);
//		}
	}

	// 달력 출고처(이벤트) 클릭 이벤트
	function onClickEventCell(info) {

		var start = info.event.startStr;
		var clickWarehouseId = info.jsEvent.target;
        if($(clickWarehouseId).hasClass('event-item')) {
            var infoStr = clickWarehouseId.dataset.id.split(",");

            var urWarehouseId = infoStr[0];
            var groupYn = infoStr[1];
            var dawnYn = infoStr[2];

            console.log("id : " + urWarehouseId);

            putUrWarehouseId = urWarehouseId;
            putHoliday = start;
            putGroupYn = groupYn;
            putDawnYn = dawnYn;

            // 출고처휴일 수정 팝업 호출
            fnPutWarehouseHoliday(start, urWarehouseId, groupYn, dawnYn);

        } else if($(clickWarehouseId).hasClass('add-btn')) {
            chkHolidayInit();
            $('input:checkbox[name="holidaysYn"]').prop("checked", false);
            fnNew(start);
        }

	}


	/******************* FullCalendar Functions End *******************/


	/******************* Form Functions *******************/

	// 조회 버튼 이벤트
	function fnCalendarSearch() {
		var data = null;
		data = $('#calendarSearchForm').formSerialize(true);

		var query = {
			page: 1,
			pageSize: CALENDARPAGE_SIZE,
			filterLength: fnSearchData(data).length,
			filter: {
				filters: fnSearchData(data)
			}
		};



		fnAjax({
			url     : "/admin/ur/warehouse/getScheduleWarehouseHolidayList",
			params  : query,
			success :
				function( data ){
				var rows = data.rows;

				// rows의 데이터를 Calendar에 eventSources의 events : []에 넣어주기 위한 데이터로 변경합니다.
				// 달력에 정상적으로 렌더링하기 위해서 events는 title : 제목, start : 해당 날짜로 구성되어야 합니다.
				// (수정)title대신 extendedProps에 출고처 목록을 추가합니다.
				// (참고)https://fullcalendar.io/docs/event-object
				var newRows = rows.map(item => {
					return Object.assign(item, {
						start: new Date(item.holiday).oFormat(FULL_DATE_FORMAT),
					});
				})

				// groupBy를 통해서 각 날짜별, 그룹별로 모아진 데이터를 생성한다.
				newRows = groupBy(newRows, "start");

				// 기존의 이벤트 소스를 삭제합니다.
				calendar.removeEventSource(SOURCE_ID);
				nextCalendar.removeEventSource(SOURCE_ID);

				// 두 캘린더에 새로운 이벤트 소스를 추가한다,
				calendar.addEventSource(newRows, SOURCE_ID);
				nextCalendar.addEventSource(newRows, SOURCE_ID);

				// 월 별 출고처 개수 설정
				counts.current = getWarehouseCount(todayDate.getYear(), todayDate.getMonth());
				counts.next = getWarehouseCount(nextDate.getYear(), nextDate.getMonth());
				counts.prev = getWarehouseCount(prevDate.getYear(), prevDate.getMonth());

				// 다시 화면을 렌더링한다.
				render();
				},
				isAction : 'batch'
		});

	}

	// 출고처 달력 검색 정보 초기화
	function fnCalendarClear() {
		// $("#popupForm").formClear(true);
		$("#calendarSearchForm").formClear(true);
	}

	function fnClose() {
		var kendoWindow = $('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	/******************* Form Functions End *******************/


	/*
	* list : 정렬시킬 배열
	* keyGetter : 기준으로 정렬시킬 키 값
	*/
	function groupBy(list, keyGetter) {
		// 배열을 생성
		const map = [];
		list.forEach((item) => {
			// item의 키값을 가져온다.
			var key = item[keyGetter];

			// map안에서 현재 item의 start값과 같은 start값을 가진 데이터의 인덱스 값을 가져온다.
			var idx = map.findIndex(function (m) { return m.start === item.start });

			// 같은 값이 있을 경우 extendedProps의 해당되는 그룹에 item을 추가합니다.
			if (idx >= 0) {
				const currentItem = map[idx];

				// 그룹이 있을 경우
				if(item.groupYn === 'Y' && item.holidayGroupKey) {
					// 속한 그룹이 extendedProps에 생성되어있지 않을 경우
					if(!currentItem.extendedProps[item.holidayGroupKey]) {
						currentItem.extendedProps[item.holidayGroupKey] = [];
					} 

					currentItem.extendedProps[item.holidayGroupKey].push(item);
				} else {
					// 그룹이 없을 경우 'no-group'이라는 그룹을 생성합니다.
					if(!currentItem.extendedProps['no-group']) {
						currentItem.extendedProps['no-group'] = [];
					} 

					currentItem.extendedProps['no-group'].push(item);
				}
			} else {
				// 없을 경우 map 배열에 새로운 데이터를 추가한다.
				const newItem = {
					title: '',
					start: item.start,
					extendedProps: {},
				};

				// 그룹이 있을 경우
				if(item.groupYn === 'Y' && item.holidayGroupKey) {
					newItem.extendedProps[item.holidayGroupKey] = [];
					newItem.extendedProps[item.holidayGroupKey].push(item);
				} else {
					newItem.extendedProps['no-group'] = [];
					newItem.extendedProps['no-group'].push(item);
				}

				map.push(newItem);
			}
		});

		return map;
	}

	// 해당 월 출고처 개수
	function getWarehouseCount(year, month) {
		var count = 0;

		// 인자로 전달받은 month에 존재하는 events 데이터를 가져옵니다.
//		var events = calendar.getEventsByMonth(month);
		var events = calendar.getEventsByYear(year, month);

		// events의 title값이 출고처 이름에 해당하므로 count에 모두 더해줍니다.
		/*for (var i = 0; i < events.length; i++) {
			count += events[i].title.split(",").length;
		}*/

		return events.length;
	}

	/******************* Search Functions End *******************/



	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose,#fnRightGridDblClick, #fnGridDblClick').kendoButton();
	}
	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

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


	// 출고처 목록 검색 초기화
	function fnClear(){
		$('#searchForm').formClear(true);
		$("span#tpCode input:radio").eq(0).click();
		//chkInit();
	}

	// 휴일 등록 (신규등록) 팝업 호출
	function fnNew(start){

		$('#holidayDate').val('');

		$("#startSetHolidays").val('');
		$("#endSetHolidays").val('');

		chkHolidayInit();
		$('#holidaysYn').attr('disabled', false);
		$('input:checkbox[name="holidaysYn"]').prop("checked", false);
		fnInitPopupGrid();

		var data;
		data = $('#searchForm').formSerialize(true);
		bGridDs.read(data);

		if(start != null){
			$('#holidayDate').val(start);
		}

		$('#createInfoDiv').hide();
		$('#createInfo').val('');
		fnKendoInputPoup({height:"700px" ,width:"1200px", title:{ nullMsg :'출고처 휴일 설정' } } );
	}


	// 휴일등록(신규) Grid 초기화
	function fnInitPopupGrid(){


		bGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/warehouse/getWarehouseSetList'
		});

		bGridOpt = {
			dataSource: bGridDs
				,filterable: {
						mode: "row",
						showOperators: false,
						operators: {
						string: {
							contains: "Contains"
							}
						}
			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				{ field:'chk'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'36px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='bGridCheckbox' name='BGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll1' />"
					,filterable: false
					}
				,{ field:'warehouseName'
				  ,title : { key : '4560'		, nullMsg :'츨고처명'}
				  ,width:'100px'
				  ,attributes:{ style:'text-align:left' }
				  ,filterable: {
					  			cell: {	 delay : 1500
					  					,suggestionOperator: "contains"
					  					,showOperators: false
//					  					,template: function(e){
//																e.element.addClass("k-textbox");
//																e.element.css("width","80px");
//					  					}
					  			}
				  }
		     }
				/*,{ field:'warehouseGroupName'		,title : { key : '4343'		, nullMsg :'출고처그룹'}		, width:'100px',attributes:{ style:'text-align:left' }, filterable: {	cell: {	 suggestionOperator: "contains",showOperators: false,template: function(e){
			e.element.addClass("k-textbox");
			e.element.css("width","80px");
		}     }}}
				,{ field:'dawnYnName'		,title : { key : '2839'		, nullMsg :'구분'}		, width:'80px',attributes:{ style:'text-align:left' }, filterable: {	cell: {	 suggestionOperator: "contains",showOperators: false,template: function(e){
			e.element.addClass("k-textbox");
			e.element.css("width","120px");
		}     }}}*/
				,{ field:'warehouseGroupName',
					   title: { key : '4343'		, nullMsg :'출고처그룹'},
					   width:'100px',attributes:{ style:'text-align:left' },
				  filterable: {
					cell: {
							suggestionOperator: "contains"
								,showOperators: false
								,template: function(e){
									e.element.kendoDropDownList({
										dataSource: {
											serverFiltering: true,
											transport: {
												read: {
													url   : "/admin/comn/getCodeList?stCommonCodeMasterCode=WAREHOUSE_GROUP&useYn=Y"
												}
											},
											schema: {
												data: function (response) {
													return response.data.rows
												}
											}
										},
										dataTextField: "NAME",
										dataValueField: "CODE",
										valuePrimitive: true,
										optionLabel: "All"
											});
								}
							}
						}
					}
					,{ field:'dawnYnName',
					   title: { key : '2839'		, nullMsg :'구분'},
					   width:'120px',attributes:{ style:'text-align:left' },
				  filterable: {
					  cell: {
						  	suggestionOperator: "contains",
						  	showOperators: false,
						  	template: function(e){
								e.element.kendoDropDownList({
									dataSource: [{
											dawnYnValue : "0",
											dawnYnName : "새벽"
										},{
											dawnYnValue : "1",
											dawnYnName : "-"
										}]
									,
									dataTextField: "dawnYnName",
									dataValueField: "dawnYnName",
									valuePrimitive: true,
									optionLabel: "All"
										});
						  	//		e.element.css("width","120px");
						  	}
					   }}}
				,{ field :'dawnYn'  ,hidden:true}
			]
		};


		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		bGrid.bind("dataBound", function(){
			if(bGrid.dataSource && bGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('bGridCheckbox','checkBoxAll1');
				$('input[name=BGRID]').on("change", function (){
					fnSetAllCheckbox('bGridCheckbox','checkBoxAll1');
				});
			}
		});
		$(bGrid.tbody).on("click", "td", function (e) {
				var row = $(this).closest("tr");
				var rowIdx = $("tr", aGrid.tbody).index(row);
				var colIdx = $("td", row).index(this);
				if(colIdx>0){
					fnBGridClick($(e.target).closest('tr'));
				}
		});


		cGridDs = fnGetPagingDataSource({
		url      : '/admin/ur/warehouse/getConfirmWarehouseHolidayList',
		filter :  {
			filters: [
				{ field: "stRoleTypeId", operator: "eq", value: function(e){
				var aMap= aGrid.dataItem(aGrid.select());
				if(aMap){
					return aMap.stRoleTypeId;
				}else{
					return null;
				}
				}}
			]
			}
		});

		cGridOpt = {
			dataSource: cGridDs
			,navigatable: true
			,scrollable: true
			,filterable: {
						mode: "row",
						showOperators: false,
						operators: {
						string: {
							contains: "Contains"
						}
						}
			}
			,columns   : [
					{ field:'chk'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'36px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='cGridCheckbox' name='CGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll2' />"
					,filterable: false}
					,{ field:'warehouseName'		,title : '출고처명'		, width:'100px',attributes:{ style:'text-align:center' }, filterable: false }
					,{ field:'warehouseGroupName'		,title : '출고처그룹'		, width:'100px',attributes:{ style:'text-align:center' }, filterable: false }
					,{ field:'dawnYnName'		,title : '구분'		, width:'80px',attributes:{ style:'text-align:center' }, filterable: false}
					,{ field :'dawnYn'  ,hidden:true}
			]
			,noRecordMsg : fnGetLangData({key : '5920', nullMsg : '검색결과가 없습니다.' })
		};


		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();

		cGrid.bind("dataBound", function(){
			if(cGrid.dataSource && cGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('cGridCheckbox','checkBoxAll2');
				$('input[name=CGRID]').on("change", function (){
					fnSetAllCheckbox('cGridCheckbox','checkBoxAll2');
				});

			}
		});
		$(cGrid.tbody).on("click", "td", function (e) {
				var row = $(this).closest("tr");
				var rowIdx = $("tr", aGrid.tbody).index(row);
				var colIdx = $("td", row).index(this);
				if(colIdx>0){
					fncGridClick($(e.target).closest('tr'));
				}
			});

	}


	function fnSave(){

	/*	var selectRows 	= bGrid.tbody.find('input[class=bGridCheckbox]:checked').closest('tr');
		var insertArray = new Array();
		for(var i =0; i< selectRows.length;i++){			//등록되는 출고처목록
			var dataRow = bGrid.dataItem($(selectRows[i]));
			insertArray.push(dataRow);
		}*/

		if(cGrid._data == undefined){
			fnKendoMessage({message:'출고처 휴일 확정을 등록해주세요'});
			return;
		}


		var fromDate;
		var toDate;
		var oldFromDate;
		var oldToDate;


		if($("#holidaysYn").val() == 'Y'){				//연휴 선택 시 휴일 지정
			fromDate = $("#startSetHolidays").val();
			toDate   = $("#endSetHolidays").val();
			oldFromDate = $("#oldFromDate").val();
			if($("#oldToDate").val() == null){
				oldToDate   = $("#oldFromDate").val();
			}else{
				oldToDate   = $("#oldToDate").val();
			}
		}else{											//연휴 선택 안할 경우  휴일 지정

//		    putGroupYn = 'N';
            fromDate = $("#holidayDate").val();
            toDate   = $("#holidayDate").val();
            oldFromDate = $("#oldFromDate").val();
            oldToDate   = $("#oldFromDate").val();



		}


		if($('#holidaysYn').val() == 'Y'){
			if($('#startSetHolidays').val() == '' || $('#endSetHolidays').val() == ''){
				fnKendoMessage({message:'휴일을 선택해주세요'});
				return;
			}
		}else{
			if($('#holidayDate').val() == '' || $('#holidayDate').val().length == 0){
				fnKendoMessage({message:'휴일을 선택해주세요'});
				return;
			}
		}



		var url  = '/admin/ur/warehouse/addWarehouseHoliday';
		var cbId = 'insert';

		if( $("#createInfo").val().length > 0   ){
			url  = '/admin/ur/warehouse/putWarehouseHoliday';
			cbId= 'update';
		}


		fnAjax({
			url     : url,
			params  : {insertData :kendo.stringify(cGridDs._data), fromDate: fromDate, toDate: toDate, oldFromDate : oldFromDate, oldToDate : oldToDate, urWarehouseId : putUrWarehouseId, holiday : putHoliday , groupYn : putGroupYn, dawnYn : putDawnYn},
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
		});


	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/ur/warehouse/getWarehouseHolidayList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				 { field:'no'	,title : 'No'	, width:'80px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'holiday'	,title : '휴일'	, width:'200px',attributes:{ style:'text-align:center' }}
				,{ field:'warehouseName'	,title : '출고처명'		, width:'550px',attributes:{ style:'text-align:center' }}
				,   { command: [{ text: '수정', click: fnPutWarehouseHoliday, className:'btn-s btn-white',
					click: function(e) {
					            e.preventDefault();
					            var tr = $(e.target).closest("tr"); // get the current table row (tr)
					            var data = this.dataItem(tr);
					            var holiday;

					            var dawnYn = 'N';
					            var warehouseHolidayName = data.warehouseName.split(',');
					            var warehouseName = warehouseHolidayName[0];
					            if(warehouseName.includes('(새벽)')){
					                dawnYn = 'Y';
					            }
					            var groupYn = 'N';
					            if(data.fromDate == data.toDate){
                                    groupYn = 'N';
                                    holiday = data.holiday;
                                }else{
                                    groupYn = 'Y';
                                    holiday = data.fromDate;
                                }
                                var urWarehouseHolidayId = data.urWarehouseHolidayId.split(',');
                                var urWarehouseId = urWarehouseHolidayId[0];
                                putUrWarehouseId = urWarehouseId;
                                putGroupYn = groupYn;
                                putDawnYn = dawnYn;
                                putHoliday = holiday;
					            fnPutWarehouseHoliday(holiday, urWarehouseId , groupYn, dawnYn);
							} }]
				, title: '관리', width: "150px", attributes:{ style:'text-align:center'  , class:'forbiz-cell-readonly #:#' }}


			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr>td", function () {
		});

		aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}



	// 휴일등록(수정 Case) Grid 초기화
	function fnInitConfirmHolidayGrid(){


		bGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/warehouse/getWarehouseHolidayDetail'
		});

		bGridOpt = {
			dataSource: bGridDs
				,filterable: {
						mode: "row",
						showOperators: false,
						operators: {
						string: {
							contains: "Contains"
						}
						}
			}
			,navigatable: true
			,scrollable: true
			,columns   : [
				{ field:'chk'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'36px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='bGridCheckbox' name='BGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll1' />"
					, filterable: false
					}
				,{ field:'warehouseName'		,title : { key : '4560'		, nullMsg :'츨고처명'}		, width:'100px',attributes:{ style:'text-align:left' }
				, filterable: {
								cell: {
										suggestionOperator: "contains"
									   ,showOperators: false
//									   ,template: function(e){
//																e.element.addClass("k-textbox");
//																e.element.css("width","80px");
//									   }
								}
				}
		     }
				,{ field:'warehouseGroupName',
				   title: { key : '4343'		, nullMsg :'출고처그룹'},
				   width:'100px',attributes:{ style:'text-align:left' },
			  filterable: {
				cell: {
						suggestionOperator: "contains"
							,showOperators: false
							,template: function(e){
								e.element.kendoDropDownList({
									dataSource: {
										serverFiltering: true,
										transport: {
											read: {
												url   : "/admin/comn/getCodeList?stCommonCodeMasterCode=WAREHOUSE_GROUP&useYn=Y"
											}
										},
										schema: {
											data: function (response) {
												return response.data.rows
											}
										}
									},
									dataTextField: "NAME",
									dataValueField: "CODE",
									valuePrimitive: true,
									optionLabel: "All"
										});
							}
						}
					}
				}
				,{ field:'dawnYnName',
				   title: { key : '2839'		, nullMsg :'구분'},
				   width:'120px',attributes:{ style:'text-align:left' },
			  filterable: {
				  cell: {
					  	suggestionOperator: "contains",
					  	showOperators: false,
					  	template: function(e){
							e.element.kendoDropDownList({
								dataSource: [{
										dawnYnValue : "0",
										dawnYnName : "새벽",
										dawnYn : "Y"
									},{
										dawnYnValue : "1",
										dawnYnName : "-",
										dawnYn : "N"
									}]
								,
								dataTextField: "dawnYnName",
								dataValueField: "dawnYnName",
								valuePrimitive: true,
								optionLabel: "All"
									});
					  	//		e.element.css("width","120px");
					  	}
				   }}}
//				,{ field :'dawnYn'  ,hidden:true}
			]
		};


		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		bGrid.bind("dataBound", function(){
			if(bGrid.dataSource && bGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('bGridCheckbox','checkBoxAll1');
				$('input[name=BGRID]').on("change", function (){
					fnSetAllCheckbox('bGridCheckbox','checkBoxAll1');
				});
			}
		});
		$(bGrid.tbody).on("click", "td", function (e) {
				var row = $(this).closest("tr");
				var rowIdx = $("tr", aGrid.tbody).index(row);
				var colIdx = $("td", row).index(this);
				if(colIdx>0){
					fnBGridClick($(e.target).closest('tr'));
				}
		});


		cGridDs = fnGetEditDataSource({
		url      : '/admin/ur/warehouse/getConfirmWarehouseHolidayList',
		requestEnd : function(e){
		                        if(e.response.data.rows[0] != undefined){
                                    $("#createInfo").val(e.response.data.rows[0].createInfo);

                                    if(e.response.data.rows[0].fromDate == e.response.data.rows[0].toDate){
                                        $('input:checkbox[name="holidaysYn"]').prop("checked", false);
                                        $("#holidaysDiv").hide();
                                        $("#holidayDiv").show();
                                        $("#holidaysYn").val('N');
                            			$("#holidayDate").val(e.response.data.rows[0].fromDate);
                            			$("#oldFromDate").val(e.response.data.rows[0].fromDate);
                                    }else{
                                        $('input:checkbox[name="holidaysYn"]').prop("checked", true);
                                        $("#holidaysDiv").show();
                                        $("#holidayDiv").hide();
                                        $("#holidaysYn").val('Y');
                            			$("#startSetHolidays").val(e.response.data.rows[0].fromDate);
                            			$("#endSetHolidays").val(e.response.data.rows[0].toDate);
                            			$("#oldFromDate").val(e.response.data.rows[0].fromDate);
                            			$("#oldToDate").val(e.response.data.rows[0].toDate);
                                    }
		                        }
                            },
		model_fields : {
			warehouseName : {
				editable : false,
				type : 'string',
				validation : {
					required : true
				}
			},
			warehouseGroupName : {
				editable : false,
				type : 'string',
				validation : {
					required : true
				}
			},
			dawnYnName : {
				editable : false,
				type : 'string',
				validation : {
					required : true
				}
			},
			dawnYn : {
				editable : false,
				type : 'string',
				validation : {
					required : true
				}
			}
		}
		});

		cGridOpt = {
			dataSource: cGridDs
			,navigatable: true
			,scrollable: true
			,filterable: {
						mode: "row",
						showOperators: false,
						operators: {
						string: {
							contains: "Contains"
						}
						}
			}
			,columns   : [
					{ field:'chk'			,title : { key : '4346'		, nullMsg :'체크박스'}		, width:'36px',attributes:{ style:'text-align:center' }
					,template : "<input type='checkbox' class='cGridCheckbox' name='CGRID'/>"
					,headerTemplate : "<input type='checkbox' id='checkBoxAll2' />"
					,filterable: false
					},{ field:'warehouseName'		,title : '출고처명'		, width:'100px',attributes:{ style:'text-align:center' }, filterable: false
					}
					,{ field:'warehouseGroupName'		,title : '출고처그룹'		, width:'100px',attributes:{ style:'text-align:center' }, filterable: false}
					,{ field:'dawnYnName'		,title : '구분'		, width:'80px',attributes:{ style:'text-align:center' }, filterable: false}
					,{ field :'dawnYn'  ,hidden:true}
			]
			,noRecordMsg : fnGetLangData({key : '5920', nullMsg : '검색결과가 없습니다.' })
		};


		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();

		cGrid.bind("dataBound", function(){
			if(cGrid.dataSource && cGrid.dataSource._view.length > 0){
				fnSetAllCheckbox('cGridCheckbox','checkBoxAll2');
				$('input[name=CGRID]').on("change", function (){
					fnSetAllCheckbox('cGridCheckbox','checkBoxAll2');
				});

			}
		});
		$(cGrid.tbody).on("click", "td", function (e) {
				var row = $(this).closest("tr");
				var rowIdx = $("tr", aGrid.tbody).index(row);
				var colIdx = $("td", row).index(this);
				if(colIdx>0){
					fncGridClick($(e.target).closest('tr'));
				}
			});


	}

	// 출고처 휴일 설정 팝업 호출(수정팝업)
	function fnPutWarehouseHoliday(holiday, urWarehouseId, groupYn, dawnYn){

		// 수정팝업 초기화
		fnInitConfirmHolidayGrid();

		var data ={"holiday": holiday, "urWarehouseId": urWarehouseId, "groupYn" : groupYn, "dawnYn" : dawnYn};

		bGridDs.read(data);
		cGridDs.read(data);

		// 등록정보 표시
		$('#createInfoDiv').show();
        $('#holidaysYn').attr('disabled', true);
		fnKendoInputPoup({height:"700px" ,width:"1200px", title:{ nullMsg :'출고처 휴일 설정' } } );
	}

	// 휴일 설정 대상 리스트에서 선택
	function fnBGridClick(param){
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if(clickRowCheckBox.prop('checked')){
			clickRowCheckBox.prop('checked', false);
		}else{
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('bGridCheckbox','checkBoxAll1');
	}

	// 휴일 설정 된 대상 리스트에서 선택
	function fncGridClick(param){
		var clickRowCheckBox = param.find('input[type=checkbox]');
		if(clickRowCheckBox.prop('checked')){
			clickRowCheckBox.prop('checked', false);
		}else{
			clickRowCheckBox.prop('checked', true);
		}
		fnSetAllCheckbox('cGridCheckbox','checkBoxAll2');
	}



	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 출고처 그룹
		fnKendoDropDownList({
			id    : "warehouseGroup",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "전체"

		});

		// 출고처 그룹
		fnKendoDropDownList({
			id    : "calendarWarehouseGroup",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" :"Y"},
			tagId : 'warehouseGroupCode',
			blank : "전체"

		});


		fnKendoDatePicker({
			id    : 'startHoliday',
			format: 'yyyy-MM-dd'
		});
		fnKendoDatePicker({
			id    : 'endHoliday',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startHoliday',
			btnEndId : 'endHoliday',
		    change: function(e){
	    	   if ($('#startHoliday').val() == "" ) {
	               return valueCheck("6495", "시작일을 선택해주세요.", 'endHoliday');
	           }
			}
		});


		// 연휴선택구분
		fnTagMkRadio({
			id    :  "holidaysYn",
			data  : [	{ "CODE" : "Y" , "NAME":"예"}
					, 	{ "CODE" : "N" , "NAME":"아니오"}],
			tagId : "holidaysYn",
			chkVal: 'N',
			style : {},
			change : function(e){

				}
		});

		// 연휴기간
		fnKendoDatePicker({
			id          : 'startSetHolidays',
			format		: 'yyyy-MM-dd',
			change      : function(e){
				dateTimeChk('startSetHolidays','endSetHolidays','연휴시작일');
			}
		});
		fnKendoDatePicker({
			id          : 'endSetHolidays',
			format		: 'yyyy-MM-dd',
			change      : function(e){
				dateTimeChk('startSetHolidays','endSetHolidays','연휴종료일');
			}
		});

		// 휴일
		fnKendoDatePicker({
			id          : 'holidayDate',
			format		: 'yyyy-MM-dd',
			change      : function(e){
				dateTimeChk('holidayDate','holidayDate','유효기간');
			}
		});



	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------



	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnCalendarSearch();
							fnClose();
						}
				});
			break;

		}
	}

	// 출고처 휴일 설정 대상으로 이동
	function fnGridDblClick(){
		var selectRows 	= bGrid.tbody.find('input[class=bGridCheckbox]:checked').closest('tr');
		if(selectRows){
			for(var i =0; i< selectRows.length;i++){
				var bMap = bGrid.dataItem($(selectRows[i]));
				bMap.each
				var insertData = {"urWarehouseId": bMap.urWarehouseId, "warehouseName": bMap.warehouseName, "warehouseGroupName": bMap.warehouseGroupName, "warehouseGroupCd": bMap.warehouseGroupCd, "dawnYnName": bMap.dawnYnName, "dawnYn" : bMap.dawnYn};
				var dupChk = false;
				for(var k=0 ;k< cGridDs._data.length;k++){
				    if(cGridDs._data[k].urWarehouseId == insertData.urWarehouseId && cGridDs._data[k].dawnYn == insertData.dawnYn){
				        dupChk = true;
				    }
				}
				if(!dupChk){
				    cGridDs.add(insertData);
				}
			}

			for(var i =0; i< selectRows.length;i++){
				bGrid.removeRow($(selectRows[i]));
			}
		}
	};

	// 출고처 휴일 설정 대상에서 제외
	function fnRightGridDblClick(){
		var selectRows 	= cGrid.tbody.find('input[class=cGridCheckbox]:checked').closest('tr');
		if(selectRows){
			for(var i =0; i< selectRows.length;i++){
				var cMap = cGrid.dataItem($(selectRows[i]));
				var insertData = {"urWarehouseId": cMap.urWarehouseId, "warehouseName": cMap.warehouseName, "warehouseGroupName": cMap.warehouseGroupName, "warehouseGroupCd": cMap.warehouseGroupCd, "dawnYnName": cMap.dawnYnName, "dawnYn" : cMap.dawnYn};
				bGridDs.add(insertData);
			}
			for(var i =0; i< selectRows.length;i++){
				cGrid.removeRow($(selectRows[i]));
			}
		}
	};



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
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnDeliveryPatternPopupButton = function( ){  fnDeliveryPatternPopupButton();};

	$scope.fnDawnDeliveryPatternPopupButton = function( ){  fnDawnDeliveryPatternPopupButton();};

	$scope.fnGridDblClick = function( ) {fnGridDblClick();};

	$scope.fnRightGridDblClick = function( ) {fnRightGridDblClick();};



	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
