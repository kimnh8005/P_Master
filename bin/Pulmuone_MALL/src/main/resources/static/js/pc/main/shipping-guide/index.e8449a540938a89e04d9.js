(function(t){function s(s){for(var a,r,l=s[0],o=s[1],_=s[2],c=0,d=[];c<l.length;c++)r=l[c],Object.prototype.hasOwnProperty.call(i,r)&&i[r]&&d.push(i[r][0]),i[r]=0;for(a in o)Object.prototype.hasOwnProperty.call(o,a)&&(t[a]=o[a]);p&&p(s);while(d.length)d.shift()();return n.push.apply(n,_||[]),e()}function e(){for(var t,s=0;s<n.length;s++){for(var e=n[s],a=!0,l=1;l<e.length;l++){var o=e[l];0!==i[o]&&(a=!1)}a&&(n.splice(s--,1),t=r(r.s=e[0]))}return t}var a={},i={"pc/main/shipping-guide/index":0},n=[];function r(s){if(a[s])return a[s].exports;var e=a[s]={i:s,l:!1,exports:{}};return t[s].call(e.exports,e,e.exports,r),e.l=!0,e.exports}r.m=t,r.c=a,r.d=function(t,s,e){r.o(t,s)||Object.defineProperty(t,s,{enumerable:!0,get:e})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,s){if(1&s&&(t=r(t)),8&s)return t;if(4&s&&"object"===typeof t&&t&&t.__esModule)return t;var e=Object.create(null);if(r.r(e),Object.defineProperty(e,"default",{enumerable:!0,value:t}),2&s&&"string"!=typeof t)for(var a in t)r.d(e,a,function(s){return t[s]}.bind(null,a));return e},r.n=function(t){var s=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(s,"a",s),s},r.o=function(t,s){return Object.prototype.hasOwnProperty.call(t,s)},r.p="/";var l=window["webpackJsonp"]=window["webpackJsonp"]||[],o=l.push.bind(l);l.push=s,l=l.slice();for(var _=0;_<l.length;_++)s(l[_]);var p=o;n.push([129,"chunk-commons"]),e()})({129:function(t,s,e){t.exports=e("db1e")},"843c":function(t,s,e){},"962c":function(t,s,e){"use strict";var a=e("843c"),i=e.n(a);i.a},a16f:function(t,s,e){},ab90:function(t,s,e){"use strict";var a=e("a16f"),i=e.n(a);i.a},d50d:function(t,s,e){},d74b:function(t,s,e){"use strict";var a=e("d50d"),i=e.n(a);i.a},db1e:function(t,s,e){"use strict";e.r(s);var a=e("5530"),i=(e("e260"),e("e6cf"),e("cca6"),e("a79d"),function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("main",{staticClass:"fb__shipping-guide"},[e("fb-header",{attrs:{pageType:"sub"}}),t.isPageError&&!t.isReloading?[e("error-layout",{staticClass:"fb__error--main",attrs:{"error-type":"default"}},[[e("div",{staticClass:"fb__btn-wrap margin"},[e("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:t.reloadPage}},[t._v("새로고침")]),e("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(s){return t.goToRoute(t.$APP_CONFIG.PAGE_URL.ROOT)}}},[t._v("홈으로")])])]],2)]:t._e(),t.$FB_CODES.FETCHES.WAIT===t.fetches.shippingAddress||t.isReloading?e("div",{staticClass:"fb__loading"},[e("div",{staticClass:"fb__fetching"})]):t.$FB_CODES.FETCHES.SUCCESS===t.fetches.shippingAddress?[e("div",{staticClass:"fb__shipping-guide__inner"},[e("div",{staticClass:"fb__shipping-guide__contents"},[e("section",{staticClass:"fb__section search-type__area"},[e("section",{staticClass:"user-info"},[e("div",{staticClass:"user-info__inner"},[t.isLogin?[e("span",{staticClass:"user-info__text"},[t.isDeliveryAvailable?t._m(0):e("span",[t._v("회원님의 배송지는 배송불가 지역입니다.")]),e("span",{staticClass:"address"},[t._v(t._s(t.userAddresss1)+" "+t._s(t.userAddresss2))])])]:[e("span",{staticClass:"user-info__text"},[e("a",{attrs:{href:"#로그인"},on:{click:function(s){return s.preventDefault(),t.goToRoute(t.$APP_CONFIG.PAGE_URL.SIGN_IN)}}},[e("em",{staticClass:"point"},[t._v("로그인")])]),t._v(" 하시면, 해당하는 배송유형을 확인하실 수 있습니다. ")])]],2),e("button",{staticClass:"btn__find-address",attrs:{type:"button"},on:{click:function(s){return t.findAddress(s)}}},[t._v(" 다른 주소지는 어떤 배송이 가능할지 궁금하다면?")])]),e("section",{staticClass:"shipping-type shipping-type__dawndelivery"},[e("div",{staticClass:"shipping-type__left"},[t._m(1),e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("dawnDelivery")}}},[t._v("배송지역 보기")])]),t._m(2)]),t._m(3),t._m(4),e("section",{staticClass:"shipping-type shipping-type__store"},[e("div",{staticClass:"shipping-type__left"},[t._m(5),e("div",{staticClass:"btns"},[e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("pickupStore")}}},[t._v("배송지역 보기")]),e("button",{staticClass:"btn__find-store",attrs:{type:"button"},on:{click:function(s){return t.openModal("searchOrgaStore")}}},[t._v("올가 매장 찾기")])])]),t._m(6)]),t._m(7),e("section",{staticClass:"shipping-type shipping-type__pickup"},[e("div",{staticClass:"shipping-type__left"},[t._m(8),e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("availableStore")}}},[t._v("픽업가능 매장보기")])]),t._m(9)])])])])]:t._e(),e("fb-footer"),e("fb-modal",{attrs:{classes:t.modals.daumPost.classes,width:t.modals.daumPost.width="540px",height:t.modals.daumPost.height="600px",open:t.modals.daumPost.open},on:{"close-modal":function(s){return t.closeModal("daumPost")}}},[e("header",[e("h2",[t._v("주소검색")])]),e("div",{ref:"daum-post-api-wrapper",staticClass:"daum-post",class:t.modals.daumPost.open?"open":null})]),e("fb-modal",{attrs:{classes:t.modals.dawnDelivery.classes,width:t.modals.dawnDelivery.width="540px",height:t.modals.dawnDelivery.height="auto",open:t.modals.dawnDelivery.open},on:{"close-modal":function(s){return t.closeModal("dawnDelivery")}}},[[e("dawn-delivery-modal")]],2),e("fb-modal",{attrs:{classes:t.modals.searchOrgaStore.classes,width:t.modals.searchOrgaStore.width="432px",open:t.modals.searchOrgaStore.open,isBackgroundClose:!1},on:{"close-modal":function(s){return t.closeModal("searchOrgaStore")}}},[[e("search-orga-store-modal")]],2),e("fb-modal",{attrs:{classes:t.modals.shippingType.classes,width:t.modals.shippingType.width="432px",height:t.modals.shippingType.height="auto",open:t.modals.shippingType.open},on:{"close-modal":function(s){return t.closeModal("shippingType")}}},[[e("div",[e("h2",{staticClass:"fb__title--hidden"},[t._v("배송 유형 검색 결과")]),e("div",{staticClass:"shipping-type__contents"},[e("span",{staticClass:"result__title"},[t.addressInfo.shippingCompDelivery?e("em",[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("em",[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("em",[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("em",[t._v("매장배송")]):t._e(),e("br"),t._v("가능지역입니다. ")]),e("div",{staticClass:"result__icons"},[t.addressInfo.shippingCompDelivery?e("span",{staticClass:"icon__list normal"},[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("span",{staticClass:"icon__list dawn"},[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("span",{staticClass:"icon__list daily"},[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("span",{staticClass:"icon__list store"},[t._v("매장배송")]):t._e()]),e("div",{staticClass:"result__address__area"},[e("span",{staticClass:"result__address__title"},[t._v("선택한 주소지")]),e("span",{staticClass:"result__address"},[t._v(t._s(t.addressInfo.address1)+" "+t._s(t.addressInfo.address2))])]),e("button",{staticClass:"shipping-type__btn__ok fb__btn-full--green",attrs:{type:"button"},on:{click:function(s){return t.closeShippingTypeModal(s)}}},[t._v("확인")])])])]],2),t.modals.availableStore.open?e("available-store",{attrs:{options:t.modals.availableStore},on:{close:function(s){t.modals.availableStore.open=!1}}}):t._e(),e("pickup-store",{attrs:{options:t.modals.pickupStore},on:{close:function(s){t.modals.pickupStore.open=!1}}}),e("fb-alert",{attrs:{open:t.alert.open,message:t.alert.message},on:{"close-alert":function(s){return t.closeAlert(s)}}})],2)}),n=[function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("span",[t._v(" 회원님의 배송지는"),e("br"),t.addressInfo.shippingCompDelivery?e("em",{staticClass:"delivery-type normal"},[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("em",{staticClass:"delivery-type dawn"},[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("em",{staticClass:"delivery-type daily"},[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("em",{staticClass:"delivery-type store"},[t._v("매장배송")]):t._e(),t._v(" 가능지역입니다. ")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("h4",{staticClass:"shipping-type__title"},[e("em",[t._v("새벽배송")]),t._v("가장 신선한 배송으로 시작하는 하루 ")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__right"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 7시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start-dawn"},[e("em",[t._v("배송출발")]),t._v("밤 10시 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")]),t._v("아침 7시까지 도착 ")])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("배송가능 지역")]),e("td",{staticClass:"shipping-zone"},[e("strong",[t._v("서울 전지역, 경기/인천 지역")]),t._v(" (일부 지역 제외) "),e("br"),e("span",{attrs:{color:"gray"}},[t._v(" 타 지역은 일반 택배 배송으로 진행됩니다. ")]),e("br")])]),e("tr",[e("th",[t._v("배송 안내")]),e("td",[e("strong",[t._v("오후 7시 주문마감되며, 오후 7시까지 주문 시 당일 저녁"),e("br"),t._v("12시부터 다음날 오전 7시 사이에 도착 (월~토)")]),e("br"),e("span",{attrs:{color:"gray"}},[t._v(" ※ 주문 물량 일시 증가 시 오후 7시 이전 주문 시에도 당일"),e("br"),t._v("출고가 불가할 수 있습니다. ")]),e("br")])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type shipping-type__normaldelivery"},[e("div",{staticClass:"shipping-type__left"},[e("h4",{staticClass:"shipping-type__title"},[e("em",[t._v("택배배송")]),t._v("오늘 주문하면 내일 배송 시작! ")])]),e("div",{staticClass:"shipping-type__right"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 6시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start"},[e("em",[t._v("배송출발")]),t._v("다음날 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("배송가능 지역")]),e("td",{staticClass:"shipping-zone"},[e("strong",[t._v("국내 전 지역")]),t._v(" (단, 옹진군 / 울릉도 제외)"),e("br")])]),e("tr",[e("th",[t._v("배송 안내")]),e("td",[e("strong",[t._v("오후 6시까지 주문 완료 시, 다음날 배송 진행")])])])]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 출고처에 따라 마감 시간은 다를 수 있습니다.")])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type shipping-type__dailydelivery"},[e("div",{staticClass:"shipping-type__left"},[e("h4",{staticClass:"shipping-type__title"},[e("em",[t._v("일일배송")]),t._v("매일 아침, 건강함을 만나보세요. ")])]),e("div",{staticClass:"shipping-type__right"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")])]),e("span",{staticClass:"delivery-step delivery-start"},[e("em",[t._v("배송출발")]),t._v("주문 시 지정한"),e("br"),t._v("배송예정일 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th"),e("td",[e("span",{attrs:{color:"gray"}},[t._v("가정집 배송 시간")])]),e("td",[e("span",{attrs:{color:"gray"}},[t._v("기타 배송 시간 (회사/사무실)")])])]),e("tr",[e("th",[e("strong",[t._v("잇슬림")])]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 7시 전")])]),e("tr",[e("th",[e("strong",[t._v("베이비밀")])]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 7시 전")])]),e("tr",[e("th",[e("strong",[t._v("풀무원 녹즙")])]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 8시 ~ 11시")])])]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 배송가능 권역의 일부지역은 매장/가맹점 사정에 따라 배송이 불가 할 수 있습니다.")]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 일부 가맹점 사정에 따라 배송 시간이 다소 변경 될 수 있습니다.")])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("h4",{staticClass:"shipping-type__title"},[e("em",[t._v("매장배송")]),t._v("올가 매장에서 신선함을 만나보세요. ")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__right"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 6시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start delivery-start-store"},[e("em",[t._v("배송출발")])]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type shipping-type__store__detail"},[e("div",{staticClass:"inner-table__wrap"},[e("h5",{staticClass:"inner-table__title inner-table__title--center"},[t._v(" 매장과 "),e("em",[t._v("가까워요!")]),t._v(" "),e("span",[t._v("2시간 내")])]),e("div",{staticClass:"icon__wrap"},[e("span",{staticClass:"icon five-times"},[t._v("일 5회 배송")]),e("span",{staticClass:"icon within2hours"},[t._v("2시간 이내 배송")]),e("span",{staticClass:"icon free"},[t._v("무료배송")])]),e("table",{staticClass:"inner-table type1"},[e("tr",[e("th",[t._v("주문마감 시간")]),e("th",[t._v("배송 시간 안내")])]),e("tr",[e("td",[t._v(" 10:00"),e("br"),t._v(" 12:00"),e("br"),t._v(" 14:00"),e("br"),t._v(" 16:00"),e("br"),t._v(" 18:00 ")]),e("td",[t._v(" 11:00 ~ 13:00"),e("br"),t._v(" 13:00 ~ 15:00"),e("br"),t._v(" 15:00 ~ 17:00"),e("br"),t._v(" 17:00 ~ 19:00"),e("br"),t._v(" 19:00 ~ 21:00 ")])])])]),e("div",{staticClass:"inner-table__wrap"},[e("h5",{staticClass:"inner-table__title inner-table__title--center"},[t._v(" 매장과 "),e("em",[t._v("조금 멀어요~")]),t._v(" "),e("span",[t._v("3시간 내")])]),e("div",{staticClass:"icon__wrap"},[e("span",{staticClass:"icon three-times"},[t._v("일 3회 배송")]),e("span",{staticClass:"icon within3hours"},[t._v("3시간 이내 배송")]),e("span",{staticClass:"icon free"},[t._v("무료배송")])]),e("table",{staticClass:"inner-table type1"},[e("tr",[e("th",[t._v("주문마감 시간")]),e("th",[t._v("배송 시간 안내")])]),e("tr",[e("td",[t._v(" 10:00"),e("br"),t._v(" 14:00"),e("br"),t._v(" 18:00 ")]),e("td",[t._v(" 11:00 ~ 12:30"),e("br"),t._v(" 15:00 ~ 16:30"),e("br"),t._v(" 19:00 ~ 20:30 ")])])])]),e("div",{staticClass:"inner-table__wrap"},[e("h5",{staticClass:"inner-table__title"},[t._v(" 배송가능 지역 ")]),e("table",{staticClass:"inner-table type2"},[e("tr",[e("th",[t._v("송파구")]),e("td",[t._v(" 잠실동, 방이동, 문정동, 가락동, 신천동, 오금동, "),e("br"),t._v(" 송파동, 풍납동, 장지동, 거여동, 석촌동, 마천동, "),e("br"),t._v(" 삼전동 ")])]),e("tr",[e("th",[t._v("강동구")]),e("td",[t._v(" 성내동, 둔촌동 ")])]),e("tr",[e("th",[t._v("서초구")]),e("td",[t._v(" 서초동, 반포동, 잠원동, 양재동, 방배동, 우면동 ")])]),e("tr",[e("th",[t._v("동작구")]),e("td",[t._v(" 사당동 ")])]),e("tr",[e("th",[t._v("성동구")]),e("td",[t._v(" 금호동, 옥수동, 성수동 ")])]),e("tr",[e("th",[t._v("용산구")]),e("td",[t._v(" 한남동 ")])]),e("tr",[e("th",[t._v("분당구")]),e("td",[t._v(" 야탑동, 백현동, 삼평동, 서현동, 수내동, 분당동, "),e("br"),t._v(" 정자동 ")])])])]),e("div",{staticClass:"inner-table__wrap"},[e("h5",{staticClass:"inner-table__title"},[t._v(" 배송가능 지역 ")]),e("table",{staticClass:"inner-table type2"},[e("tr",{staticClass:"only"},[e("th",{attrs:{colspan:"2"}},[e("strong",[t._v("강남구 전체")]),t._v(" (추후 배송 구역 확대 예정) ")])])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("h4",{staticClass:"shipping-type__title"},[e("em",[t._v("매장픽업")]),t._v("올가 매장에서 신선함을 만나보세요. ")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__right"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")])]),e("span",{staticClass:"delivery-step delivery-start-pickup-store"},[e("em",[t._v("매장방문")])]),e("span",{staticClass:"delivery-step delivery-completed-pickup-customer"},[e("em",[t._v("고객수령")])])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("픽업 안내")]),e("td",[e("div",{staticClass:"icon__wrap"},[e("span",{staticClass:"icon pickup-five-times"},[t._v("일 5회 픽업")]),e("span",{staticClass:"icon pickup-within30m"},[t._v("30분 이내 픽업")]),e("span",{staticClass:"icon pickup-free"},[t._v("픽업서비스 무료")])])])]),e("tr",[e("th",[t._v("픽업 가능 매장")]),e("td",{staticClass:"shipping-zone"},[e("strong",[t._v("방이점, 방배점, 반포점, 압구정점")])])])])])}],r=e("0bf4"),l=e("fc84"),o=e("e412"),_=e("dcbe"),p=e("4402"),c=e("3af9"),d=e("863d"),v=e("9d0b"),h=e("eabb"),u=e("4d98"),f=function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("fb-modal",{attrs:{open:t.options.open,width:t.options.width,height:t.options.height,classes:t.options.classes,"is-scroll-lock":!0},on:{"close-modal":t.closeModal}},[e("section",{staticClass:"pickup-store"},[e("div",{staticClass:"modal__header"},[e("h2",{staticClass:"modal__title"},[t._v(" 매장배송지역 "),e("button",{staticClass:"modal__close",attrs:{type:"button",tabindex:"0"},on:{click:t.closeModal}})])]),e("simplebar",{staticClass:"modal__body",attrs:{"data-simplebar-auto-hide":"false"}},[e("section",{staticClass:"pickup-store__contents"},[e("h3",{staticClass:"fb__title--hidden"},[t._v("매장 배송 지역 정보")]),e("section",{staticClass:"pickup-store__maps"},[e("h4",{staticClass:"fb__title--hidden"},[t._v("매장 정보 지역 지도")]),e("div",{staticClass:"pickup-store__map"},[e("span",{staticClass:"pickup-store__map__title"},[t._v("서울특별시")]),e("figure",{staticClass:"store-map"},[e("img",{attrs:{src:"/assets/pc/images/main/img-map-400-1.svg",alt:"서울특별시 지도"}})])]),e("div",{staticClass:"pickup-store__map"},[e("span",{staticClass:"pickup-store__map__title"},[t._v("경기도")]),e("figure",{staticClass:"store-map"},[e("img",{attrs:{src:"/assets/pc/images/main/img-map-400-2.svg",alt:"경기도 지도"}})])])]),e("section",{staticClass:"pickup-store__message"},[e("span",{staticClass:"pickup-store__message__icon"}),e("h4",[t._v("매장배송으로 만나보세요!")]),e("p",[t._v(" 방이점,방배점,반포점,압구정점에서"),e("br"),t._v(" 매장배송을 만나볼 수 있습니다! ")])])])])],1)])},m=[],b=e("8d3b"),C=(e("f138"),{components:{"fb-modal":d["a"],simplebar:b["a"]},props:{options:{type:Object,required:!0}},methods:{closeModal:function(t){this.$emit("close")}}}),y=C,g=(e("ab90"),e("2877")),I=Object(g["a"])(y,f,m,!1,null,"5b8abc1a",null),S=I.exports,w=function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("fb-modal",{attrs:{open:t.options.open,width:t.options.width,height:t.options.height,classes:t.options.classes,isBackgroundClose:t.options.isBackgroundClose},on:{"close-modal":t.closeModal}},[e("section",{staticClass:"available-store"},[e("h2",{staticClass:"modal__title"},[t._v(" 픽업가능 매장 ")]),e("simplebar",{staticClass:"modal__body",attrs:{"data-simplebar-auto-hide":"false"}},[t.$FB_CODES.FETCHES.WAIT===t.fetches.pickupStoreList?e("div",{staticClass:"fb__fetching"}):t.$FB_CODES.FETCHES.SUCCESS===t.fetches.pickupStoreList?[e("section",{staticClass:"available-store__stores"},[e("h3",{staticClass:"fb__title--hidden"},[t._v("픽업가능 매장 리스트")]),t.pickUpStoreList.length?e("ul",{staticClass:"tab-list"},[t._l(t.pickUpStoreList,(function(s){return[e("li",{key:s.urStoreId,class:"tab-list__tab "+(s.urStoreId===t.selectedStoreId?"tab-list__tab--selected":""),attrs:{"data-store-id":s.urStoreId}},[e("label",[e("span",[t._v(t._s(s.name))]),e("input",{directives:[{name:"model",rawName:"v-model",value:t.selectedStoreId,expression:"selectedStoreId"}],attrs:{type:"radio"},domProps:{value:s.urStoreId,checked:t._q(t.selectedStoreId,s.urStoreId)},on:{change:function(e){t.selectedStoreId=s.urStoreId}}})])])]}))],2):[t._v(" 픽업 가능 매장 리스트가 없습니다. ")]],2),e("section",{staticClass:"available-store__contents"},[t.$FB_CODES.FETCHES.WAIT===t.fetches.shopInfo?e("div",{staticClass:"fb__fetching"}):[e("h3",{staticClass:"fb__title--hidden"},[t._v("매장 정보")]),t.shopInfo?[t.shopInfo.introImage?[e("figure",{staticClass:"available-store__banner"},[e("img",{directives:[{name:"lazy",rawName:"v-lazy",value:t.mergeImageHost(t.shopInfo.introImage),expression:"mergeImageHost(shopInfo.introImage)"}],attrs:{alt:t.shopInfo.name}})])]:t._e(),e("section",{staticClass:"available-store__info-wrapper"},[e("h4",{staticClass:"available-store__name"},[t._v(" "+t._s(t.shopInfo.name)+" ")]),e("div",{staticClass:"available-store__info"},[e("dl",[e("dt",[t._v("매장 주소")]),e("dd",[t._v(t._s(t.shopInfo.address1)+" "+t._s(t.shopInfo.address2))])]),e("dl",[e("dt",[t._v("전화번호")]),e("dd",[e("span",{staticClass:"store-call"},[e("a",{attrs:{href:"tel:"+t.shopInfo.telephone},on:{click:function(s){s.stopPropagation(),t.document.location.href="tel:"+t.shopInfo.telephone}}},[t._v(t._s(t.shopInfo.telephone))])])])]),e("dl",[e("dt",[t._v("영업시간")]),e("dd",[t._v(t._s(t.shopInfo.openTime)+" ~ "+t._s(t.shopInfo.closeTime))])]),e("dl",[e("dt",[t._v("휴무일")]),e("dd",[t._v("연중무휴 (단, 설날, 추석 당일 및 익일 휴무)")])]),e("dl",[e("dt",[t._v("배송타입")]),e("dd",[e("span",{staticClass:"ship-type ship-type--shipping",class:{active:t.isDirectShipping}},[t._v("매장배송")]),e("span",{staticClass:"ship-type ship-type--pickup",class:{active:t.isPickup}},[t._v("매장픽업")])])])]),e("h4",{staticClass:"available-store__map-title"},[t._v(" 매장 위치 ")]),t.shopInfo?e("div",{staticClass:"available-store__map"},[e("store-map",{attrs:{storeInfo:t.shopInfo}})],1):t._e()])]:t._e()]],2)]:t._e()],2)],1)])},k=[],E=e("b7f1"),O=e("dc0a"),D={mixins:[E["a"]],components:{fbModal:d["a"],StoreMap:O["a"],simplebar:b["a"]},props:{options:{type:Object,required:!0}},methods:{closeModal:function(t){this.$emit("close")}}},x=D,M=(e("d74b"),Object(g["a"])(x,w,k,!1,null,null,null)),P=M.exports,T={name:"shipping-guide",extends:l["a"],mixins:[r["a"],o["a"]],components:{fbHeader:_["a"],fbFooter:p["a"],fbSelectBox:c["a"],fbModal:d["a"],fbAlert:v["a"],dawnDeliveryModal:h["a"],searchOrgaStoreModal:u["a"],PickupStore:S,AvailableStore:P},mounted:function(){this.checkReferrer()}},$=T,A=(e("962c"),Object(g["a"])($,i,n,!1,null,null,null)),j=A.exports,F=e("8f7d"),B=e("caf9");F["a"].use(B["a"],{error:F["b"].IMAGES.NOT_FOUND,loading:F["b"].IMAGES.IMG_LOADING}),F["c"].components=Object(a["a"])({App:j},F["c"].components),F["d"].dispatch("init").then((function(){return new F["a"](F["c"])}))}});