(function(t){function s(s){for(var a,r,n=s[0],o=s[1],_=s[2],p=0,d=[];p<n.length;p++)r=n[p],Object.prototype.hasOwnProperty.call(i,r)&&i[r]&&d.push(i[r][0]),i[r]=0;for(a in o)Object.prototype.hasOwnProperty.call(o,a)&&(t[a]=o[a]);c&&c(s);while(d.length)d.shift()();return l.push.apply(l,_||[]),e()}function e(){for(var t,s=0;s<l.length;s++){for(var e=l[s],a=!0,n=1;n<e.length;n++){var o=e[n];0!==i[o]&&(a=!1)}a&&(l.splice(s--,1),t=r(r.s=e[0]))}return t}var a={},i={"mobile/main/shipping-guide/index":0},l=[];function r(s){if(a[s])return a[s].exports;var e=a[s]={i:s,l:!1,exports:{}};return t[s].call(e.exports,e,e.exports,r),e.l=!0,e.exports}r.m=t,r.c=a,r.d=function(t,s,e){r.o(t,s)||Object.defineProperty(t,s,{enumerable:!0,get:e})},r.r=function(t){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(t,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(t,"__esModule",{value:!0})},r.t=function(t,s){if(1&s&&(t=r(t)),8&s)return t;if(4&s&&"object"===typeof t&&t&&t.__esModule)return t;var e=Object.create(null);if(r.r(e),Object.defineProperty(e,"default",{enumerable:!0,value:t}),2&s&&"string"!=typeof t)for(var a in t)r.d(e,a,function(s){return t[s]}.bind(null,a));return e},r.n=function(t){var s=t&&t.__esModule?function(){return t["default"]}:function(){return t};return r.d(s,"a",s),s},r.o=function(t,s){return Object.prototype.hasOwnProperty.call(t,s)},r.p="/";var n=window["webpackJsonp"]=window["webpackJsonp"]||[],o=n.push.bind(n);n.push=s,n=n.slice();for(var _=0;_<n.length;_++)s(n[_]);var c=o;l.push([46,"chunk-commons"]),e()})({"085c":function(t,s,e){"use strict";var a=e("7b06"),i=e.n(a);i.a},"3e78":function(t,s,e){"use strict";var a=e("a7a0"),i=e.n(a);i.a},46:function(t,s,e){t.exports=e("470d")},"470d":function(t,s,e){"use strict";e.r(s);var a=e("5530"),i=(e("e260"),e("e6cf"),e("cca6"),e("a79d"),function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("main",{staticClass:"fb__shipping-guide"},[e("fb-header",{attrs:{title:t.header.title,buttons:t.header.buttons,actions:t.header.actions}}),t.isPageError&&!t.isReloading?[e("error-layout",{staticClass:"fb__error--main",attrs:{"error-type":"default"}},[[e("div",{staticClass:"fb__btn-wrap margin"},[e("button",{staticClass:"fb__btn-margin--white",attrs:{type:"button",tabindex:"0"},on:{click:t.reloadPage}},[t._v("새로고침")]),e("button",{staticClass:"fb__btn-margin--green",attrs:{type:"button",tabindex:"0"},on:{click:function(s){return t.goToRoute(t.$APP_CONFIG.PAGE_URL.ROOT)}}},[t._v("홈으로")])])]],2)]:t._e(),t.$FB_CODES.FETCHES.WAIT===t.fetches.shippingAddress||t.isReloading?e("div",{staticClass:"fb__loading"},[e("div",{staticClass:"fb__fetching"})]):t.$FB_CODES.FETCHES.SUCCESS===t.fetches.shippingAddress?[e("section",{staticClass:"fb__shipping-guide__banner"},[e("figure",{staticClass:"banner__img"},[e("img",{attrs:{src:"/assets/mobile/images/main/daily-shipping-banner.png",alt:"배너이미지"},on:{error:function(s){s.target.src=t.$APP_CONFIG.IMAGES.NOT_FOUND}}})])]),e("section",{staticClass:"fb__section search-type__area fb__section__radius"},[e("h3",{staticClass:"fb__title--hidden"},[t._v(" 배송 유형 검색 ")]),t._e(),e("section",{staticClass:"user-info"},[e("h4",{staticClass:"fb__title-hidden"}),e("div",{staticClass:"user-info__inner"},[e("div",{staticClass:"user-info__box"},[t.isLogin?[t.isDeliveryAvailable?t._m(0):e("p",{staticClass:"user-info__detail"},[t._v(" 회원님의 배송지는 배송불가 지역입니다. ")])]:e("p",{staticClass:"user-info__detail"},[e("a",{staticClass:"point",attrs:{href:t.$APP_CONFIG.PAGE_URL.SIGN_IN}},[t._v("로그인")]),t._v("하시면, 해당하는 배송유형을 확인하실 수 있습니다. ")])],2),e("div",{staticClass:"user-info__box"},[e("button",{staticClass:"btn--find-address",attrs:{type:"button"},on:{click:function(s){return t.findAddress(s)}}},[t._v(" 다른 주소지 검색 ")])])]),t.isLogin&&(t.userAddresss1||t.userAddresss2)?[e("div",{staticClass:"user-info__address"},[e("p",{staticClass:"user-address"},[t._v(" "+t._s(t.userAddresss1)+" "+t._s(t.userAddresss2)+" ")])])]:t._e()],2),e("section",{staticClass:"shipping-type"},[e("h4",{staticClass:"shipping-type__title dawndelivery"},[t._v("새벽배송")]),e("p",{staticClass:"shipping-type__desc"},[t._v("가장 신선한 배송으로 시작하는 하루")]),e("div",{staticClass:"shipping-type__button"},[e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("dawnDelivery")}}},[t._v("배송지역 보기")])]),t._m(1),t._m(2)]),t._m(3),t._m(4),e("section",{staticClass:"shipping-type shipping-type__store"},[e("h4",{staticClass:"shipping-type__title storedelivery"},[t._v("매장배송")]),e("p",{staticClass:"shipping-type__desc"},[t._v("올가 매장에서 신선함을 만나보세요.")]),e("div",{staticClass:"shipping-type__button"},[e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("pickupStore")}}},[t._v("배송지역 보기")]),e("button",{staticClass:"btn__find-store",attrs:{type:"button"},on:{click:function(s){return t.openModal("searchOrgaStore")}}},[t._v("올가 매장 찾기")])]),t._m(5)]),t._m(6),e("section",{staticClass:"shipping-type shipping-type__store-pickup"},[e("h4",{staticClass:"shipping-type__title storedelivery"},[t._v("매장픽업")]),t._m(7),e("div",{staticClass:"shipping-type__button"},[e("button",{staticClass:"btn__view-shipping-area",attrs:{type:"button"},on:{click:function(s){return t.openModal("availableStore")}}},[t._v("픽업가능 매장보기")])]),t._m(8),t._m(9)])])]:t._e(),e("fb-footer"),e("fb-dockbar"),e("fb-alert",{attrs:{open:t.alert.open,message:t.alert.message,sendData:t.alert.type},on:{"close-alert":t.closeAlert}}),e("fb-modal",{attrs:{classes:t.modals.daumPost.classes,width:t.modals.daumPost.width="100%",height:t.modals.daumPost.height="100%",open:t.modals.daumPost.open},on:{"close-modal":function(s){return t.closeModal("daumPost")}}},[e("header",[e("h2",[t._v("주소검색")])]),e("div",{ref:"daum-post-api-wrapper",staticClass:"daum-post",class:t.modals.daumPost.open?"open":null})]),e("fb-modal",{attrs:{classes:t.modals.dawnDelivery.classes,width:t.modals.dawnDelivery.width="100%",height:t.modals.dawnDelivery.height="100%",open:t.modals.dawnDelivery.open},on:{"close-modal":function(s){return t.closeModal("dawnDelivery")}}},[[e("dawn-delivery-modal")]],2),e("fb-modal",{attrs:{classes:t.modals.searchOrgaStore.classes,width:t.modals.searchOrgaStore.width="100%",height:t.modals.searchOrgaStore.height="100%",open:t.modals.searchOrgaStore.open,"is-close-button":!1}},[[e("search-orga-store-modal",{on:{"close-modal":function(s){return t.closeModal("searchOrgaStore")}}})]],2),e("fb-modal",{attrs:{classes:t.modals.shippingType.classes,open:t.modals.shippingType.open},on:{"close-modal":function(s){return t.closeModal("shippingType")}}},[[e("div",[e("h2",{staticClass:"fb__title--hidden"},[t._v("배송 유형 검색 결과")]),e("div",{staticClass:"shipping-type__contents"},[e("span",{staticClass:"result__title"},[t.addressInfo.shippingCompDelivery?e("em",[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("em",[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("em",[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("em",[t._v("매장배송")]):t._e(),e("br"),t._v("가능지역입니다. ")]),e("div",{staticClass:"result__icons"},[t.addressInfo.shippingCompDelivery?e("span",{staticClass:"icon__list normal"},[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("span",{staticClass:"icon__list dawn"},[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("span",{staticClass:"icon__list daily"},[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("span",{staticClass:"icon__list store"},[t._v("매장배송")]):t._e()]),e("div",{staticClass:"result__address__area"},[e("span",{staticClass:"result__address__title"},[t._v("선택한 주소지")]),e("span",{staticClass:"result__address"},[t._v(t._s(t.addressInfo.address1)+" "+t._s(t.addressInfo.address2))])]),e("button",{staticClass:"shipping-type__btn__ok fb__btn-full--green",attrs:{type:"button"},on:{click:function(s){return t.closeShippingTypeModal(s)}}},[t._v("확인")])])])]],2),t.modals.availableStore.open?e("available-store",{attrs:{options:t.modals.availableStore},on:{close:function(s){return t.closeModal("availableStore")}}}):t._e(),t.modals.pickupStore.open?e("pickup-store",{attrs:{options:t.modals.pickupStore},on:{close:function(s){return t.closeModal("pickupStore")}}}):t._e(),e("fb-alert",{attrs:{open:t.alert.open,message:t.alert.message},on:{"close-alert":function(s){return t.closeAlert(s)}}})],2)}),l=[function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("p",{staticClass:"user-info__detail"},[t._v(" 회원님의 배송지는"),e("br"),t.addressInfo.shippingCompDelivery?e("em",{staticClass:"delivery-type delivery-type--normal"},[t._v("택배배송")]):t._e(),t.addressInfo.dawnDelivery?e("em",{staticClass:"delivery-type delivery-type--dawn"},[t._v("새벽배송")]):t._e(),t.addressInfo.dailyDelivery?e("em",{staticClass:"delivery-type delivery-type--daily"},[t._v("일일배송")]):t._e(),t.addressInfo.storeDelivery?e("em",{staticClass:"delivery-type delivery-type--store"},[t._v("매장배송")]):t._e(),e("br"),t._v(" 가능지역입니다. ")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__step"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 7시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start-dawn"},[e("em",[t._v("배송출발")]),t._v("밤 10시 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")]),t._v("아침 7시까지 도착 ")])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("배송가능 지역")]),e("td",[e("span",{attrs:{color:"black"}},[e("strong",[t._v("서울 전지역, 경기/인천 지역")]),e("br"),t._v(" (일부 지역 제외) ")]),e("br"),t._v(" 타 지역은 일반 택배 배송으로 진행됩니다 ")])]),e("tr",[e("th",[t._v("주문마감 시간")]),e("td",[e("span",{attrs:{color:"black"}},[t._v(" 오후 7시 주문 마감 ")]),e("br"),t._v(" ※ 주문 물량 일시 증가 시 오후 7시 이전"),e("br"),t._v(" 주문 시에도 당일 출고가 불가할 수"),e("br"),t._v(" 있습니다. ")])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type"},[e("h4",{staticClass:"shipping-type__title normaldelivery"},[t._v("택배배송")]),e("p",{staticClass:"shipping-type__desc"},[t._v("오늘 주문하면 내일 배송 시작!")]),e("div",{staticClass:"shipping-type__step"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 6시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start"},[e("em",[t._v("배송출발")]),t._v("다음날 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("배송가능 지역")]),e("td",[e("span",{attrs:{color:"black"}},[e("strong",[t._v("국내 전 지역")]),t._v(" (단, 옹진군 / 울릉도 제외) ")])])]),e("tr",[e("th",[t._v("주문마감 시간")]),e("td",[e("span",{attrs:{color:"black"}},[t._v(" 오후 6시까지 주문 완료 시, "),e("br"),t._v(" 다음날 배송 진행 ")])])])]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 출고처에 따라 마감 시간은 다를 수 있습니다.")])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type shipping-type__dailydelivery"},[e("h4",{staticClass:"shipping-type__title dailydelivery"},[t._v("일일배송")]),e("p",{staticClass:"shipping-type__desc"},[t._v("매일 아침, 건강함을 만나세요")]),e("div",{staticClass:"shipping-type__step"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")])]),e("span",{staticClass:"delivery-step delivery-start"},[e("em",[t._v("배송출발")]),t._v("주문 시 지정한"),e("br"),t._v("배송예정일 배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])])]),e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",{staticStyle:{background:"white"}}),e("td",[e("span",[t._v("가정집 배송 시간")])]),e("td",[e("span",[t._v("기타 배송 시간(회사/사무실)")])])]),e("tr",[e("th",{staticStyle:{background:"white"}},[t._v("잇슬림")]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 7시 전")])]),e("tr",[e("th",{staticStyle:{background:"white"}},[t._v("베이비밀")]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 7시 전")])]),e("tr",[e("th",{staticStyle:{background:"white"}},[t._v("풀무원 녹즙")]),e("td",[t._v("오전 7시 전")]),e("td",[t._v("오전 8시 ~ 11시")])])]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 배송가능 권역의 일부지역은 매장/가맹점 사정에 따라 배송이 불가 할 수 있습니다.")]),e("p",{staticClass:"shipping-type__table__noti"},[t._v("※ 일부 가맹점 사정에 따라 배송 시간이 다소 변경 될 수 있습니다.")])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__step"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")]),t._v("저녁 6시까지 주문 ")]),e("span",{staticClass:"delivery-step delivery-start-store"},[e("em",[t._v("배송출발")]),t._v("원하는 시간대"),e("br"),t._v("배송 시작 ")]),e("span",{staticClass:"delivery-step delivery-completed"},[e("em",[t._v("배송완료")])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("section",{staticClass:"shipping-type shipping-type__store-info"},[e("h5",{staticClass:"fb__title--hidden"},[t._v(" 매장 정보 ")]),e("div",{staticClass:"store-info__title"},[t._v(" 매장과 "),e("em",[t._v("가까워요!")]),t._v(" "),e("span",{staticClass:"store-info__badge"},[t._v("2시간 내")])]),e("div",{staticClass:"store-info__step"},[e("div",{staticClass:"steps"},[e("span",{staticClass:"steps__step five-times"},[e("em",[t._v("일 5회 배송")])]),e("span",{staticClass:"steps__step within2hours"},[e("em",[t._v("2시간 이내 배송")])]),e("span",{staticClass:"steps__step free"},[e("em",[t._v("무료배송")])])])]),e("table",{staticClass:"store-info__table store-info__table--time"},[e("colgroup",[e("col",{staticStyle:{width:"50%"}}),e("col",{staticStyle:{width:"50%"}})]),e("thead",[e("tr",[e("th",[t._v("주문마감 시간")]),e("th",[t._v("배송 시간")])])]),e("tbody",[e("tr",[e("td",[t._v(" 10:00"),e("br"),t._v(" 12:00"),e("br"),t._v(" 14:00"),e("br"),t._v(" 16:00"),e("br"),t._v(" 18:00 ")]),e("td",[t._v(" 11:00 ~ 13:00"),e("br"),t._v(" 13:00 ~ 15:00"),e("br"),t._v(" 15:00 ~ 17:00"),e("br"),t._v(" 17:00 ~ 19:00"),e("br"),t._v(" 19:00 ~ 21:00 ")])])])]),e("h5",{staticClass:"store-info__table-title"},[t._v(" 배송가능 지역 ")]),e("table",{staticClass:"store-info__table store-info__table--type2"},[e("tr",[e("th",[t._v("송파구")]),e("td",[t._v(" 잠실동, 방이동, 문정동, 가락동, 신천동, "),e("br"),t._v(" 오금동, 송파동, 풍납동, 장지동, 거여동, "),e("br"),t._v(" 석촌동, 마천동, 삼전동 ")])]),e("tr",[e("th",[t._v("강동구")]),e("td",[t._v(" 성내동, 둔촌동 ")])]),e("tr",[e("th",[t._v("서초구")]),e("td",[t._v(" 서초동, 반포동, 잠원동, 양재동, 방배동, 우면동 ")])]),e("tr",[e("th",[t._v("동작구")]),e("td",[t._v(" 사당동 ")])]),e("tr",[e("th",[t._v("성동구")]),e("td",[t._v(" 금호동, 옥수동, 성수동 ")])]),e("tr",[e("th",[t._v("용산구")]),e("td",[t._v(" 한남동 ")])]),e("tr",[e("th",[t._v("분당구")]),e("td",[t._v(" 야탑동, 백현동, 삼평동, 서현동, 수내동, "),e("br"),t._v(" 분당동, 정자동 ")])])]),e("div",{staticClass:"store-info__title"},[t._v(" 매장과 "),e("em",[t._v("조금 멀어요~")]),t._v(" "),e("span",{staticClass:"store-info__badge"},[t._v("3시간 내")])]),e("div",{staticClass:"store-info__step"},[e("div",{staticClass:"steps"},[e("span",{staticClass:"steps__step five-times"},[e("em",[t._v("일 5회 배송")])]),e("span",{staticClass:"steps__step within3hours"},[e("em",[t._v("3시간 이내 배송")])]),e("span",{staticClass:"steps__step free"},[e("em",[t._v("무료배송")])])])]),e("table",{staticClass:"store-info__table store-info__table--time"},[e("colgroup",[e("col",{staticStyle:{width:"50%"}}),e("col",{staticStyle:{width:"50%"}})]),e("thead",[e("tr",[e("th",[t._v("주문마감 시간")]),e("th",[t._v("배송 시간")])])]),e("tbody",[e("tr",[e("td",[t._v(" 10:00"),e("br"),t._v(" 14:00"),e("br"),t._v(" 18:00 ")]),e("td",[t._v(" 11:00 ~ 12:30"),e("br"),t._v(" 15:00 ~ 16:30"),e("br"),t._v(" 19:00 ~ 20:30 ")])])])]),e("h5",{staticClass:"store-info__table-title"},[t._v(" 배송가능 지역 ")]),e("table",{staticClass:"store-info__table store-info__table--type3"},[e("colgroup",[e("col")]),e("tr",[e("td",[e("em",[t._v("강남구 전체")]),t._v("(추후 배송 구역 확대 예정) ")])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("p",{staticClass:"shipping-type__desc"},[t._v("가까운 올가 매장에서 원하는"),e("br"),t._v("시간에 매장 픽업과 퀵 배송을 이용해보세요.")])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("div",{staticClass:"shipping-type__step"},[e("div",{staticClass:"delivery-step__area"},[e("span",{staticClass:"delivery-step order-completed"},[e("em",[t._v("주문완료")])]),e("span",{staticClass:"delivery-step pickup-store"},[e("em",[t._v("매장방문")])]),e("span",{staticClass:"delivery-step pickup-customer"},[e("em",[t._v("고객수령")])])])])},function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("table",{staticClass:"shipping-type__table"},[e("tr",[e("th",[t._v("픽업 안내")]),e("td",[e("div",{staticClass:"table__step"},[e("div",{staticClass:"steps"},[e("span",{staticClass:"steps__step five-times-day"},[e("em",[t._v("일 5회 픽업")])]),e("span",{staticClass:"steps__step within30m"},[e("em",[t._v("30분 이내")])]),e("span",{staticClass:"steps__step pickup-free"},[e("em",[t._v("서비스 무료")])])])])])]),e("tr",[e("th",[t._v("픽업 가능 매장")]),e("td",{staticClass:"shipping-zone"},[e("strong",[t._v("방이점, 방배점, 반포점, 압구정점")])])])])}],r=e("0bf4"),n=e("fc84"),o=e("e412"),_=e("8e32"),c=e("629b"),p=e("cbaa"),d=e("533e"),v=e("2479"),u=e("face"),h=e("5631"),f=e("7e0d"),b=function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("fb-modal",{attrs:{open:t.options.open,width:"100%",height:"100vh",classes:t.options.classes,isCloseButton:!1},on:{"close-modal":t.closeModal}},[e("section",{staticClass:"available-store"},[e("div",{staticClass:"modal__header"},[e("h2",{staticClass:"modal__title"},[t._v(" 픽업가능 매장 "),e("button",{staticClass:"modal__close",attrs:{type:"button",tabindex:"0"},on:{click:t.closeModal}})])]),e("section",{staticClass:"modal__body"},[t.$FB_CODES.FETCHES.WAIT===t.fetches.pickupStoreList?e("div",{staticClass:"fb__fetching"}):[e("section",{staticClass:"available-store__stores"},[e("h3",{staticClass:"fb__title--hidden"},[t._v("픽업가능 매장 리스트")]),t.pickUpStoreList.length?e("ul",{staticClass:"tab-list"},[t._l(t.pickUpStoreList,(function(s){return[e("li",{key:s.urStoreId,class:"tab-list__tab "+(s.urStoreId===t.selectedStoreId?"tab-list__tab--selected":""),attrs:{"data-store-id":s.urStoreId}},[e("label",[e("span",[t._v(t._s(s.name))]),e("input",{directives:[{name:"model",rawName:"v-model",value:t.selectedStoreId,expression:"selectedStoreId"}],attrs:{type:"radio"},domProps:{value:s.urStoreId,checked:t._q(t.selectedStoreId,s.urStoreId)},on:{change:function(e){t.selectedStoreId=s.urStoreId}}})])])]}))],2):[t._v(" 픽업 가능 매장 리스트가 없습니다. ")]],2),e("section",{staticClass:"available-store__contents"},[t.$FB_CODES.FETCHES.WAIT===t.fetches.shopInfo?e("div",{staticClass:"fb__fetching"}):[e("h3",{staticClass:"fb__title--hidden"},[t._v("매장 정보")]),t.shopInfo?[t.shopInfo.introImage?[e("figure",{staticClass:"available-store__banner"},[e("img",{directives:[{name:"lazy",rawName:"v-lazy",value:t.mergeImageHost(t.shopInfo.introImage),expression:"mergeImageHost(shopInfo.introImage)"}],attrs:{alt:t.shopInfo.name}})])]:t._e(),e("section",{staticClass:"available-store__info-wrapper",class:{"is-banner":!!t.shopInfo.introImage}},[e("h4",{staticClass:"available-store__name"},[t._v(" "+t._s(t.shopInfo.name)+" ")]),e("div",{staticClass:"available-store__info"},[e("dl",[e("dt",[t._v("매장 주소")]),e("dd",[t._v(t._s(t.shopInfo.address1)+" "+t._s(t.shopInfo.address2))])]),e("dl",[e("dt",[t._v("전화번호")]),e("dd",[e("span",{staticClass:"store-call"},[e("a",{attrs:{href:"tel:"+t.shopInfo.telephone},on:{click:function(s){s.stopPropagation(),t.document.location.href="tel:"+t.shopInfo.telephone}}},[t._v(t._s(t.shopInfo.telephone))])])])]),e("dl",[e("dt",[t._v("영업시간")]),e("dd",[t._v(t._s(t.shopInfo.openTime)+" ~ "+t._s(t.shopInfo.closeTime))])]),e("dl",[e("dt",[t._v("휴무일")]),e("dd",[t._v("연중무휴 (단, 설날, 추석 당일 및 익일 휴무)")])]),e("dl",[e("dt",[t._v("배송타입")]),e("dd",[e("span",{staticClass:"ship-type ship-type--shipping",class:{active:t.isDirectShipping}},[t._v("매장배송")]),e("span",{staticClass:"ship-type ship-type--pickup",class:{active:t.isPickup}},[t._v("매장픽업")])])])]),e("h4",{staticClass:"available-store__map-title"},[t._v(" 매장 위치 ")]),t.shopInfo?e("div",{staticClass:"available-store__map"},[e("div",{staticClass:"map-wrapper"},[e("store-map",{attrs:{storeInfo:t.shopInfo}})],1)]):t._e()])]:t._e()]],2)]],2)])])},m=[],C=e("863d"),y=e("b7f1"),g=e("dc0a"),S={mixins:[y["a"]],components:{"fb-modal":C["a"],"store-map":g["a"]},props:{options:{type:Object,required:!0}},data:function(){return{selectedStore:null}},methods:{closeModal:function(t){this.$emit("close")}}},I=S,w=(e("e57a"),e("2877")),k=Object(w["a"])(I,b,m,!1,null,null,null),O=k.exports,E=function(){var t=this,s=t.$createElement,e=t._self._c||s;return e("fb-modal",{attrs:{open:t.options.open,width:"100%",height:"100vh",classes:t.options.classes,isCloseButton:!1},on:{"close-modal":t.closeModal}},[e("section",{staticClass:"pickup-store"},[e("div",{staticClass:"modal__header"},[e("h2",{staticClass:"modal__title"},[t._v(" 매장배송지역 "),e("button",{staticClass:"modal__close",attrs:{type:"button",tabindex:"0"},on:{click:t.closeModal}})])]),e("div",{staticClass:"modal__body"},[e("section",{staticClass:"pickup-store__contents"},[e("h3",{staticClass:"fb__title--hidden"},[t._v("매장 배송 지역 정보")]),e("section",{staticClass:"pickup-store__maps"},[e("h4",{staticClass:"fb__title--hidden"},[t._v("매장 정보 지역 지도")]),e("div",{staticClass:"pickup-store__map"},[e("span",{staticClass:"pickup-store__map__title"},[t._v("서울특별시")]),e("figure",{staticClass:"store-map"},[e("img",{attrs:{src:"/assets/mobile/images/main/img-map-355-1.svg",alt:"서울특별시 지도"}})])]),e("div",{staticClass:"pickup-store__map"},[e("span",{staticClass:"pickup-store__map__title"},[t._v("경기도")]),e("figure",{staticClass:"store-map"},[e("img",{attrs:{src:"/assets/mobile/images/main/img-map-355-2.svg",alt:"경기도 지도"}})])])]),e("section",{staticClass:"pickup-store__message"},[e("span",{staticClass:"pickup-store__message__icon"}),e("h4",[t._v("매장배송으로 만나보세요!")]),e("p",[t._v(" 방이점,방배점,반포점,압구정점에서"),e("br"),t._v(" 매장배송을 만나볼 수 있습니다! ")])])])])])])},D=[],M={components:{"fb-modal":C["a"]},props:{options:{type:Object,required:!0}},methods:{closeModal:function(t){this.$emit("close")}}},P=M,A=(e("3e78"),Object(w["a"])(P,E,D,!1,null,"4d9edf52",null)),x=A.exports,$={name:"shipping-guide",extends:n["a"],mixins:[r["a"],o["a"]],components:{fbHeader:_["a"],fbFooter:c["a"],fbDockbar:p["a"],fbSelectBox:d["a"],fbModal:v["a"],fbAlert:u["a"],dawnDeliveryModal:h["a"],searchOrgaStoreModal:f["a"],AvailableStore:O,PickupStore:x},data:function(){return{header:{title:"배송안내",buttons:["back","home","search","cart"],actions:[]}}},updated:function(){this.checkReferrer()}},T=$,j=(e("085c"),Object(w["a"])(T,i,l,!1,null,null,null)),F=j.exports,N=e("dd69"),G=e("caf9");N["a"].use(G["a"],{error:N["b"].IMAGES.NOT_FOUND,loading:N["b"].IMAGES.IMG_LOADING}),N["c"].components=Object(a["a"])({App:F},N["c"].components),N["d"].dispatch("init").then((function(){return new N["a"](N["c"])}))},"4c7d":function(t,s,e){},"7b06":function(t,s,e){},a7a0:function(t,s,e){},e57a:function(t,s,e){"use strict";var a=e("4c7d"),i=e.n(a);i.a}});