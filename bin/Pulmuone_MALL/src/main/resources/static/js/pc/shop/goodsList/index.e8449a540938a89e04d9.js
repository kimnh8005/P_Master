(function(e){function t(t){for(var a,l,n=t[0],c=t[1],o=t[2],h=0,g=[];h<n.length;h++)l=n[h],Object.prototype.hasOwnProperty.call(s,l)&&s[l]&&g.push(s[l][0]),s[l]=0;for(a in c)Object.prototype.hasOwnProperty.call(c,a)&&(e[a]=c[a]);u&&u(t);while(g.length)g.shift()();return i.push.apply(i,o||[]),r()}function r(){for(var e,t=0;t<i.length;t++){for(var r=i[t],a=!0,n=1;n<r.length;n++){var c=r[n];0!==s[c]&&(a=!1)}a&&(i.splice(t--,1),e=l(l.s=r[0]))}return e}var a={},s={"pc/shop/goodsList/index":0},i=[];function l(t){if(a[t])return a[t].exports;var r=a[t]={i:t,l:!1,exports:{}};return e[t].call(r.exports,r,r.exports,l),r.l=!0,r.exports}l.m=e,l.c=a,l.d=function(e,t,r){l.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:r})},l.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},l.t=function(e,t){if(1&t&&(e=l(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var r=Object.create(null);if(l.r(r),Object.defineProperty(r,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var a in e)l.d(r,a,function(t){return e[t]}.bind(null,a));return r},l.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return l.d(t,"a",t),t},l.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},l.p="/";var n=window["webpackJsonp"]=window["webpackJsonp"]||[],c=n.push.bind(n);n.push=t,n=n.slice();for(var o=0;o<n.length;o++)t(n[o]);var u=c;i.push([175,"chunk-commons"]),r()})({175:function(e,t,r){e.exports=r("1b3f")},"1b3f":function(e,t,r){"use strict";r.r(t);var a=r("5530"),s=(r("e260"),r("e6cf"),r("cca6"),r("a79d"),function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("main",{staticClass:"fb__shopSearch"},[r("fb-header",{attrs:{pageType:"sub",brand:e.headerBrand,searchWord:e.searchResult.word}}),e.valuableItemId?r("section",{staticClass:"search",class:e.mainClasses},[r("div",{staticClass:"product-layout__body search__inner"},[r("div",{staticClass:"product-layout__body__left"},[e.sameLevelCategory?r("div",{staticClass:"goods__category"},[r("p",{staticClass:"category__title"},[e._v("카테고리")]),r("ul",{staticClass:"category__cont"},e._l(e.sameLevelCategory,(function(t,a){return r("li",{key:"sameLevelCategory-"+a,staticClass:"category__list"},[r("details",{ref:"categories-"+t.ilCategoryId,refInFor:!0,on:{toggle:function(r){return r.target!==r.currentTarget?null:e.categoryToggle(r,t.ilCategoryId)}}},[r("summary",{staticClass:"category__button",class:(t.ilCategoryId==e.allCategory.lev2CategoryId?"listAcitve":"")+(e.subCategory[t.ilCategoryId]?"":"disabled")},[r("span",{staticClass:"ga_filter",attrs:{"data-ga-filter-label":t.categoryName,"data-ga-filter-action":"카테고리"},on:{click:function(r){return r.stopPropagation(),r.preventDefault(),e.goToRoute(e.$APP_CONFIG.PAGE_URL.GOODS_LIST+"?itemId="+t.ilCategoryId)}}},[e._v(" "+e._s(t.categoryName)+" ")])]),e.subCategory[t.ilCategoryId]?r("ul",e._l(e.subCategory[t.ilCategoryId],(function(a,s){return r("li",{key:"subCategory-"+s,staticClass:"category__threeDepth"},[r("details",{ref:"categories-"+a.ilCategoryId,refInFor:!0,on:{toggle:function(t){return t.target!==t.currentTarget?null:e.categoryToggle(t,a.ilCategoryId)}}},[r("summary",{staticClass:"category__button",class:(a.ilCategoryId==e.allCategory.lev3CategoryId?"listAcitve":"")+(e.subsubCategory[a.ilCategoryId]?"":"disabled")},[r("a",{staticClass:"threeDepth__list ga_filter",class:{listAcitve:a.ilCategoryId==e.allCategory.lev3CategoryId},attrs:{href:"#","data-ga-filter-label":t.categoryName+" - "+a.categoryName,"data-ga-filter-action":"카테고리"},on:{click:function(t){return t.preventDefault(),e.goToRoute(e.$APP_CONFIG.PAGE_URL.GOODS_LIST+"?itemId="+a.ilCategoryId)}}},[e._v(e._s(a.categoryName))])]),e.subsubCategory[a.ilCategoryId]?r("ul",e._l(e.subsubCategory[a.ilCategoryId],(function(s,i){return r("li",{key:"subsubCategory-"+i,ref:"categories-"+s.ilCategoryId,refInFor:!0,staticClass:"category__fourDepth"},[r("a",{staticClass:"fourDepth__list ga_filter",class:{listAcitve:s.ilCategoryId==e.allCategory.lev4CategoryId},attrs:{href:"#","data-ga-filter-label":t.categoryName+" - "+a.categoryName+" - "+s.categoryName,"data-ga-filter-action":"카테고리"},on:{click:function(t){return t.preventDefault(),e.goToRoute(e.$APP_CONFIG.PAGE_URL.GOODS_LIST+"?itemId="+s.ilCategoryId)}}},[e._v(e._s(s.categoryName))])])})),0):e._e()])])})),0):e._e()])])})),0)]):e._e(),r("div",{staticClass:"search__filter"},[r("div",{staticClass:"filter"},[e._l(e.searchRespons,(function(t,a,s){return[t.length?[r("div",{key:s+t,staticClass:"filter__box ",class:"category"==a?"filter__type__radio":"filter__type__checkbox"},[r("button",{staticClass:"filter__title",class:e.searchResult.filterVisible[a]?"":"filter__title--close",attrs:{type:"button"},on:{click:function(t){return e.filterVisible(a)}}},[e._v(" "+e._s(t[0].title)+" ")]),e.searchResult.filterVisible[a]?r("div",{staticClass:"filter__inner"},e._l(t,(function(s,i){return r("label",{key:i+s.name,class:"category"==a?"fb__radio":"fb__checkbox"},[r("input","category"==a?{directives:[{name:"model",rawName:"v-model",value:e.searchResult.filter[a],expression:"searchResult.filter[name]"}],staticClass:"ga_filter",attrs:{"data-ga-filter-label":s.name,"data-ga-filter-action":t[0].title,type:"radio"},domProps:{value:s.code,checked:e._q(e.searchResult.filter[a],s.code)},on:{change:function(t){return e.$set(e.searchResult.filter,a,s.code)}}}:{directives:[{name:"model",rawName:"v-model",value:e.searchResult.filter[a],expression:"searchResult.filter[name]"}],staticClass:"ga_filter",attrs:{type:"checkbox","data-ga-filter-label":s.name,"data-ga-filter-action":t[0].title},domProps:{value:s.code,checked:Array.isArray(e.searchResult.filter[a])?e._i(e.searchResult.filter[a],s.code)>-1:e.searchResult.filter[a]},on:{change:function(t){var r=e.searchResult.filter[a],i=t.target,l=!!i.checked;if(Array.isArray(r)){var n=s.code,c=e._i(r,n);i.checked?c<0&&e.$set(e.searchResult.filter,a,r.concat([n])):c>-1&&e.$set(e.searchResult.filter,a,r.slice(0,c).concat(r.slice(c+1)))}else e.$set(e.searchResult.filter,a,l)}}}),r("span",[e._v(e._s(s.name)+" ")])])})),0):e._e()])]:e._e()]})),r("div",{staticClass:"filter__box"},[r("button",{staticClass:"filter__title",class:e.searchResult.filterVisible["price"]?"":"filter__title--close",attrs:{type:"button"},on:{click:function(t){return e.filterVisible("price")}}},[e._v(" 가격 ")]),e.searchResult.filterVisible["price"]?r("div",{staticClass:"filter__inner"},[r("div",{staticClass:"price"},[r("div",{staticClass:"price__min"},[r("vue-autonumeric",{ref:"priceMin",staticClass:"emptyPrice",attrs:{placeholder:"최소가격",options:e.numericOption},on:{input:e.isEmptyPrice},model:{value:e.price.min,callback:function(t){e.$set(e.price,"min",t)},expression:"price.min"}}),r("span",{staticClass:"unit"},[e._v("원")])],1),r("span",{staticClass:"tilde"},[e._v("~")]),r("div",{staticClass:"price__max"},[r("vue-autonumeric",{ref:"priceMax",staticClass:"emptyPrice",attrs:{placeholder:"최대가격",options:e.numericOption},on:{input:e.isEmptyPrice},model:{value:e.price.max,callback:function(t){e.$set(e.price,"max",t)},expression:"price.max"}}),r("span",{staticClass:"unit"},[e._v("원")])],1),r("div",{staticClass:"price__apply"},[r("button",{attrs:{type:"button"},on:{click:e.priceApply}},[e._v("적용")])])])]):e._e()])],2)])]),r("div",{staticClass:"product-layout__body__right"},[r("h2",{staticClass:"goods__title"},[e._v(" "+e._s(e.pageTitle)+" "),r("ol",{staticClass:"breadscrum__area"},e._l(e.currentDepth,(function(t,a){return r("li",{key:"depth-"+a,staticClass:"breadscrum"},[r("a",{attrs:{href:t.path}},[e._v(e._s(t.name))])])})),0)]),e.pageBannerList&&e.pageBannerList[e.lev1Id]&&e.pageBannerList[e.lev1Id].level1?r("div",{staticClass:"fb__page-banner"},[r("swiper",{ref:"banner2Swiper",staticClass:"page-banner__swiper",attrs:{options:e.banner2SwiperOptions}},e._l(e.pageBannerList[e.lev1Id].level1,(function(t,a){return r("swiper-slide",{key:"pageBanner-"+a,staticClass:"page-banner__list"},[r("a",{staticClass:"page-banner__link",attrs:{href:t.linkUrlPc,title:t.titleName}},[r("img",{directives:[{name:"lazy",rawName:"v-lazy",value:e.mergeImageHost(t.imagePathPc),expression:"mergeImageHost(banner.imagePathPc)"}],staticClass:"page-banner__image",attrs:{alt:t.titleName}})])])})),1),e.pageBannerList[e.lev1Id].level1.length>1?r("div",{staticClass:"banner__slider__nav"},[r("div",{staticClass:"banner__slider__nav-prev"}),r("div",{staticClass:"banner__slider__nav-next"})]):e._e()],1):e._e(),e.search.searchComplete&&e.searchResult.filterIs&&e.searchResult.filterList.length?r("div",{staticClass:"search__filterBox"},[r("div",{staticClass:"search__filterList"},[r("ul",{staticClass:"fb__filter-list"},e._l(e.searchResult.filterList,(function(t,a){return r("li",{key:t.name+a,staticClass:"fb__filter-list__item"},[e._v(" "+e._s(t.name)+" "),r("button",{staticClass:"fb__filter-list__delete",attrs:{type:"button"},on:{click:function(r){return e.filterDel(t,r)}}},[e._v("삭제")])])})),0),e._e(),r("button",{staticClass:"search__filterList__clear",attrs:{type:"button"},on:{click:e.filterAllDel}},[e._v("초기화")])])]):e._e(),e.search.searchComplete?r("div",{staticClass:"search__top"},[r("div",{staticClass:"search__total"},[e._v("총 "),r("span",[e._v(e._s(e.searchResult.total))]),e._v("개")]),r("div",{staticClass:"search__listType"},[r("label",{staticClass:"fb__checkbox  search__soldOut"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.soldOut,expression:"search.soldOut"}],staticClass:"ga_product_filter",attrs:{type:"checkbox","data-ga-filter-label":"품절상품 제외"},domProps:{checked:Array.isArray(e.search.soldOut)?e._i(e.search.soldOut,null)>-1:e.search.soldOut},on:{change:function(t){var r=e.search.soldOut,a=t.target,s=!!a.checked;if(Array.isArray(r)){var i=null,l=e._i(r,i);a.checked?l<0&&e.$set(e.search,"soldOut",r.concat([i])):l>-1&&e.$set(e.search,"soldOut",r.slice(0,l).concat(r.slice(l+1)))}else e.$set(e.search,"soldOut",s)}}}),r("span",[e._v(" 품절상품 제외 ")])]),r("div",{staticClass:"search__listType__sort"},e._l(e.sortList,(function(t,a){return r("label",{key:"sortList-"+a},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.sorts,expression:"search.sorts"}],staticClass:"ga_product_filter",attrs:{type:"radio","data-ga-filter-label":t.name},domProps:{value:t.value,checked:e._q(e.search.sorts,t.value)},on:{change:function(r){return e.$set(e.search,"sorts",t.value)}}}),r("span",[e._v(e._s(t.name))])])})),0),r("div",{staticClass:"search__listType__btn"},[r("label",{staticClass:"search__listType__list"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.views,expression:"search.views"}],attrs:{type:"radio"},domProps:{value:["wide"],checked:e._q(e.search.views,["wide"])},on:{change:function(t){return e.$set(e.search,"views",["wide"])}}}),r("span",[e._v(" 리스트 ")])]),r("label",{staticClass:"search__listType__photo"},[r("input",{directives:[{name:"model",rawName:"v-model",value:e.search.views,expression:"search.views"}],attrs:{type:"radio"},domProps:{value:["small"],checked:e._q(e.search.views,["small"])},on:{change:function(t){return e.$set(e.search,"views",["small"])}}}),r("span",[e._v(" 포토 ")])])])])]):e._e(),r("div",{staticClass:"search__result"},[e.searchResult.goodsResultList&&e.searchResult.goodsResultList.length?r("div",{staticClass:"search__result__productWrap"},[r("fb-product",{attrs:{viewType:[e.search.views],"product-list":e.searchResult.goodsResultList,mallType:e.mall,PageCd:e.getAdMallKey+"_PC_Cate",ContentCd:e.lev1Title,CategoryId:""+e.getCategoryId}}),r("div",{staticClass:"search__result__pagination"},[e.searchResult.total>0?r("fb-pagination",{attrs:{pagination:e.pagination},on:{movePaging:e.movePaging}}):e._e()],1)],1):r("div",{staticClass:"search__result--noItem"},[e.search.searchComplete?[e._v(" 선택된 필터에 대한 검색결과가 없습니다.​"),r("br"),e._v(" 다른 필터를 선택해 보세요.​ ")]:[e._v(" 상품이 존재하지 않습니다. ")]],2)])])])]):e._e(),r("fb-footer"),r("fb-alert",{attrs:{message:e.alert.message,open:e.alert.open,buttonText:e.alert.buttonText,sendData:e.sendData},on:{"close-alert":e.closeAlert}}),r("fb-alert",{attrs:{message:e.emptyItemId.message,open:e.emptyItemId.open,buttonText:e.emptyItemId.buttonText,sendData:e.emptyItemId.sendData},on:{"close-alert":e.emptyItemIdCloseAlert}})],1)}),i=[],l=(r("a4d3"),r("e01a"),r("d28b"),r("99af"),r("4de4"),r("7db0"),r("c740"),r("a15b"),r("a434"),r("b0c0"),r("4fad"),r("d3b7"),r("07ac"),r("ac1f"),r("25f0"),r("3ca3"),r("841c"),r("1276"),r("ddb0"),r("2b3d"),r("3835")),n=(r("96cf"),r("1da1")),c=r("2909"),o=r("53ca"),u=r("ade3"),h=r("dcbe"),g=r("4402"),d=r("7212"),f=(r("a7a3"),r("7c17")),p=r("9d0b"),y=r("3af9"),m=r("7e74"),v=r("0bf4"),_=r("f18a"),b=r("181c"),C=r("2ef0"),R={name:"goodsList",mixins:[v["a"],_["a"]],components:{fbHeader:h["a"],fbFooter:g["a"],fbAlert:p["a"],VueAutonumeric:b["a"],fbProduct:f["a"],fbSelectBox:y["a"],fbPagination:m["a"],Swiper:d["Swiper"],SwiperSlide:d["SwiperSlide"]},directives:{swiper:d["directive"]},data:function(){var e;return e={valuableItemId:!0,pageTitle:null,filterLayerState:!1,sendData:null,alert:{message:null,open:!1},currentPageStyle:!1,numericOption:{decimalPlaces:0,maximumValue:9999999,minimumValue:0},fetches:{subCategory:!1,subsubCategory:!1},sameLevelCategoryVisible:{},sameLevelCategory:null,subCategory:{},subsubCategory:{},currentDepth:[{name:"홈",path:this.$APP_CONFIG.PAGE_URL.ROOT}],lev1Id:null,lev1Title:"",pageBannerList:null,banner2SwiperOptions:{slidesPerView:"auto",pagination:{el:".banner-swiper-pagination",type:"bullets"},navigation:{prevEl:".banner__slider__nav-prev",nextEl:".banner__slider__nav-next"}}},Object(u["a"])(e,"filterLayerState",!1),Object(u["a"])(e,"sortList",[{name:"인기순",value:"POPULARITY"},{name:"신상품순",value:"NEW "},{name:"낮은가격순",value:"LOW_PRICE"},{name:"높은가격순",value:"HIGH_PRICE"},{name:"할인율높은순",value:"HIGH_DISCOUNT_RATE"}]),Object(u["a"])(e,"search",{category:"ALL",soldOut:!1,filterBtn:!1,views:["small"],sorts:"POPULARITY",url:"",load:!1,mallType:null,searchComplete:!1}),Object(u["a"])(e,"searchResult",{total:"0",word:"",filter:{},filterIs:!1,filterList:[],filterVisible:[],allFilter:{},goodsResultList:null}),Object(u["a"])(e,"price",{min:"",max:""}),Object(u["a"])(e,"searchRespons",null),Object(u["a"])(e,"pagination",{id:"goods-pagination",currentPage:1,goodsCount:40,totalCount:0,disabled:!1,updated:!0}),Object(u["a"])(e,"pageNum",0),Object(u["a"])(e,"alert",{message:null,open:!1,buttonText:null}),Object(u["a"])(e,"emptyItemId",{message:"존재하지 않는 상품입니다.",open:!1,buttonText:null}),Object(u["a"])(e,"allCategory",null),Object(u["a"])(e,"commonCategory",null),Object(u["a"])(e,"lev1Category",null),Object(u["a"])(e,"lev2Category",null),Object(u["a"])(e,"lev3Category",null),Object(u["a"])(e,"lev4Category",null),Object(u["a"])(e,"isListAcitve",!1),Object(u["a"])(e,"isPageLoaded",!1),e},created:function(){var e=this,t=this.getQueryString();this.search.url=t.toString(),this.getHistory?this.filterInit():this.requestTerms();var r=this.getViewType();r&&(this.search.views=r),this.reqeustInitCategory();var a=this.$store.getters["common/categories"];this.fetches.commonCategory=!!a&&a.fetched,this.fetches.commonCategory&&(this.commonCategory=a),this.$nextTick((function(){e.isPageLoaded=!0})),this.requestInventoryInfo()},mounted:function(){this.queryString.get("itemId")?this.valuableItemId:this.emptyItemId.open=!0},watch:{"search.category":{handler:function(e,t){this.requestGoods()}},"search.soldOut":{handler:function(e,t){this.isPageLoaded&&this.requestGoods()}},"searchResult.filter":{handler:function(e,t){var r=this;this.searchResult.filterList=[],this.searchResult.url="";var a=this.getQueryString();if(this.search.load){var s=function(e){if(r.searchResult.filter[e].length>0||a.get("minPrice")||a.get("maxPrice")){if(r.searchResult.filterIs=!0,"object"==Object(o["a"])(r.searchResult.filter[e]))if(Array.isArray(r.searchResult.filter[e]))if(r.searchResult.filter[e].length>0){a.set(e,r.searchResult.filter[e].join("%")),history.replaceState(r.filterData(),null,"?"+a.toString());var t=!0,s=!1,i=void 0;try{for(var l,n=function(){var t=l.value,a=r.searchRespons[e].find((function(e){return e.code==t}));r.searchResult.filterList.push(a)},c=r.searchResult.filter[e][Symbol.iterator]();!(t=(l=c.next()).done);t=!0)n()}catch(h){s=!0,i=h}finally{try{t||null==c.return||c.return()}finally{if(s)throw i}}}else a.delete(e),history.replaceState(r.filterData(),null,"?"+a.toString());else a.get("minPrice")>=0&&a.get("maxPrice")>0&&r.searchResult.filterList.push({name:"".concat(r.toPrice(a.get("minPrice"))," ~ ").concat(r.toPrice(a.get("maxPrice")),"원"),category:"price",value:{minPrice:a.get("minPrice"),maxPrice:a.get("maxPrice")}});else if(r.searchResult.filter[e]){a.set(e,r.searchResult.filter[e]),history.replaceState(r.filterData(),null,"?"+a.toString());var u=r.searchRespons[e].find((function(t){return t.code==r.searchResult.filter[e]}));r.searchResult.filterList.push(u)}}else a.delete(e),history.replaceState(r.filterData(),null,"?"+a.toString()),r.searchResult.filterList.length||(r.searchResult.filterIs=!1)};for(var i in this.searchResult.filter)s(i);this.search.url!==a.toString()&&this.search.load&&(this.search.url=a.toString(),this.requestGoods())}else{var l=function(e){if(r.searchResult.filter[e].length>0||a.get("minPrice")||a.get("maxPrice"))if(r.searchResult.filterIs=!0,"object"==Object(o["a"])(r.searchResult.filter[e]))if(Array.isArray(r.searchResult.filter[e])){var t=!0,s=!1,i=void 0;try{for(var l,n=function(){var t=l.value,a=r.searchRespons[e].find((function(e){return e.code==t}));r.searchResult.filterList.push(a)},c=r.searchResult.filter[e][Symbol.iterator]();!(t=(l=c.next()).done);t=!0)n()}catch(h){s=!0,i=h}finally{try{t||null==c.return||c.return()}finally{if(s)throw i}}}else a.get("minPrice")>=0&&a.get("maxPrice")>0&&(r.searchResult.filterList.push({name:"".concat(r.toPrice(a.get("minPrice"))," ~ ").concat(r.toPrice(a.get("maxPrice")),"원"),category:"price",value:{minPrice:a.get("minPrice"),maxPrice:a.get("maxPrice")}}),r.search.url!==a.toString()&&r.search.load&&(r.search.url=a.toString(),r.requestGoods()));else if(r.searchResult.filter[e]){a.set(e,r.searchResult.filter[e]),history.replaceState(r.filterData(),null,"?"+a.toString());var u=r.searchRespons[e].find((function(t){return t.code==r.searchResult.filter[e]}));r.searchResult.filterList.push(u)}};for(var n in this.searchResult.filter)l(n)}},deep:!0},"search.views":{handler:function(e,t){this.setViewType(e)}},"search.sorts":{handler:function(e,t){this.isPageLoaded&&this.requestGoods()}},"searchResult.goodsResultList":{handler:function(e,t){this.pagination.updated||(window.scrollTo(0,0),this.pagination.updated=!0)},deep:!0}},computed:{mall:function(){return this.$store.getters["common/mall"]},headerBrand:function(){var e=this.$FB_CODES.MALL_TYPES;if(this.mall===e.PULMUONE)return"main";var t,r="";for(var a in e){var s=e[a];if(s===this.mall){r=a;break}}return null===(t=r)||void 0===t?void 0:t.toLowerCase()},mainClasses:function(){return"search--".concat(this.headerBrand)},getCategoryId:function(){return"".concat(this.getQueryString().get("itemId"))}},methods:{isEmptyPrice:function(e,t){""==t.detail.newRawValue?t.target.classList.add("emptyPrice"):t.target.classList.remove("emptyPrice")},emptyItemIdCloseAlert:function(){this.goToRoute("/")},categoryVisible:function(e){this.sameLevelCategoryVisible[e]=!this.sameLevelCategoryVisible[e]},urlSearch:function(){var e,t=this,r=this.getQueryString(),a=!0,s=!1,i=void 0;try{for(var l,n=this.getQueryString().keys()[Symbol.iterator]();!(a=(l=n.next()).done);a=!0){var o=l.value;switch(o){case"category":this.$set(this.searchResult.filter,o,""),this.searchResult.filter[o]=r.get(o);break;case"minPrice":this.searchResult.filter.price.min=r.get(o);break;case"maxPrice":this.searchResult.filter.price.max=r.get(o);break;case"searchword":this.searchResult.word=r.get(o);break;case"page":this.pagination.currentPage=r.get(o);break;case"itemId":break;default:this.$set(this.searchResult.filter,o,[]),(e=this.searchResult.filter[o]).push.apply(e,Object(c["a"])(r.get(o).split("%")));break}}}catch(u){s=!0,i=u}finally{try{a||null==n.return||n.return()}finally{if(s)throw i}}this.$nextTick((function(){t.search.load=!0}))},filterInit:function(){var e=null;for(var t in this.searchRespons){switch(t){case"category":e="카테고리";break;case"brand":e="브랜드";break;case"certification":e="인증제품";break;case"storage":e="보관방법";break;case"delivery":e="배송";break;case"benefit":e="혜택";break;case"mall":this.search.mallType=this.searchRespons[t];var r=!0,a=!1,s=void 0;try{for(var i,l=this.search.mallType[Symbol.iterator]();!(r=(i=l.next()).done);r=!0){var n=i.value;n.count}}catch(p){a=!0,s=p}finally{try{r||null==l.return||l.return()}finally{if(a)throw s}}delete this.searchRespons.mall;break;default:e="기타"}if(Array.isArray(this.searchRespons[t])){this.$set(this.searchResult.allFilter,t,[]);var c=!0,o=!1,u=void 0;try{for(var h,g=this.searchRespons[t][Symbol.iterator]();!(c=(h=g.next()).done);c=!0){var d=h.value;this.searchResult.allFilter[t].push(d.code)}}catch(p){o=!0,u=p}finally{try{c||null==g.return||g.return()}finally{if(o)throw u}}}for(var f in"category"==t?this.$set(this.searchResult.filter,t,""):this.$set(this.searchResult.filter,t,[]),this.$set(this.searchResult.filterVisible,t,!0),this.$set(this.searchResult.filterVisible,"price",!0),this.searchRespons[t])this.searchRespons[t][f].category=t,this.searchRespons[t][f].title=e}this.$set(this.searchResult.filter,"price",{}),this.$set(this.searchResult.filter.price,"min",""),this.$set(this.searchResult.filter.price,"max",""),this.urlSearch()},getQueryString:function(){return new URLSearchParams(window.location.search)},filterVisible:function(e){this.searchResult.filterVisible[e]=!this.searchResult.filterVisible[e]},filterDel:function(e,t){var r=this.getQueryString();switch(this.search.load=!0,e.category){case"category":this.searchResult.filter[e.category]="";break;case"price":this.search.url=r.toString(),this.searchResult.filter[e.category].min="",this.searchResult.filter[e.category].max="",r.delete("minPrice"),r.delete("maxPrice"),history.replaceState(this.filterData(),null,"?"+r.toString());break;default:var a=this.searchResult.filter[e.category].findIndex((function(t){return t==e.code}));this.searchResult.filter[e.category].splice(a,1);break}},filterAllDel:function(e,t){var r=this.getQueryString();for(var a in this.search.load=!0,this.searchResult.filter)switch(a){case"category":this.searchResult.filter[a]="";break;case"price":this.search.url=r.toString(),this.searchResult.filter[a].min="",this.searchResult.filter[a].max="",r.delete("minPrice"),r.delete("maxPrice"),history.replaceState(this.filterData(),null,"?"+r.toString());break;default:this.searchResult.filter[a]=[];break}},priceFilter:function(e){this.searchResult.filter.price.min+=e.key},priceApply:function(){var e=this;if(""==this.$refs.priceMin.$el.value||""==this.$refs.priceMax.$el.value)return this.alert.message=this.$FB_MESSAGES.SYSTEM.ALERT_237,this.sendData=""==this.$refs.priceMin.$el.value?"priceMin":"priceMax",this.alert.open=!0,!1;if(this.price.min>=this.price.max)return this.alert.message=this.$FB_MESSAGES.SYSTEM.ALERT_224,this.sendData="priceMin",this.alert.open=!0,!1;this.searchResult.filter.price.min=this.price.min,this.searchResult.filter.price.max=this.price.max;var t=this.getQueryString();if(this.searchResult.filterIs=!0,this.searchResult.filter.price.min!=t.get("minPrice")||this.searchResult.filter.price.max!=t.get("maxPrice")){t.set("minPrice",this.searchResult.filter.price.min),t.set("maxPrice",this.searchResult.filter.price.max),history.replaceState(this.filterData(),null,"?"+t.toString());var r=this.searchResult.filterList.findIndex((function(e){return"price"==e.category}));r>=0?this.searchResult.filterList[r].name="".concat(this.toPrice(t.get("minPrice"))," ~ ").concat(this.toPrice(t.get("maxPrice")),"원"):this.searchResult.filterList.push({name:"".concat(this.toPrice(t.get("minPrice"))," ~ ").concat(this.toPrice(t.get("maxPrice")),"원"),category:"price",value:{minPrice:t.get("minPrice"),maxPrice:t.get("maxPrice")}}),this.search.load=!1,this.requestGoods(),this.$nextTick((function(){e.search.load=!0}))}},closeAlert:function(e){var t=e.sendData;this.alert.open=!1,"priceMin"==t?this.$refs.priceMin.$el.focus():"priceMax"==t&&this.$refs.priceMax.$el.focus()},requestTerms:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var r,a,s,i,l,n,c,o,u,h,g;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,r=e.queryString,t.next=4,e.$store.dispatch("network/request",{method:"post",url:"/goods/search/getCategoryGoodsList",data:{ilCategoryId:r.get("itemId"),isFirstSearch:!0,page:r.get("page")?r.get("page"):0,limit:e.pagination.goodsCount,excludeSoldOutGoods:!1,benefitTypeIdList:r.get("benefit")?r.get("benefit").split("%"):[],brandIdList:r.get("brand")?r.get("brand").split("%"):[],deliveryTypeIdList:r.get("delivery")?r.get("delivery").split("%"):[],certificationTypeIdList:r.get("certification")?r.get("certification").split("%"):[],storageMethodIdList:r.get("storage")?r.get("storage").split("%"):[],minimumPrice:r.get("minPrice")?r.get("minPrice"):0,maximumPrice:r.get("maxPrice")?r.get("maxPrice"):9999999,sortCode:"POPULARITY"}});case 4:if(a=t.sent,s=a.data,a.code!=e.$FB_CODES.API.SUCCESS){t.next=43;break}if(e.searchResult.total=s.count,i={},i.mall=s.filter.mall&&s.filter.mall.length?s.filter.mall:[],i.benefit=s.filter.benefit&&s.filter.benefit.length?s.filter.benefit:[],i.brand=s.filter.brand&&s.filter.brand.length?s.filter.brand:[],i.delivery=s.filter.delivery&&s.filter.delivery.length?s.filter.delivery:[],i.certification=s.filter.certification&&s.filter.certification.length?s.filter.certification:[],i.storage=s.filter.storage&&s.filter.storage.length?s.filter.storage:[],e.searchRespons=i,e.searchResult.goodsResultList=s.document,!e.searchResult.goodsResultList){t.next=40;break}for(l=null,n=!0,c=!1,o=void 0,t.prev=22,u=i.mall[Symbol.iterator]();!(n=(h=u.next()).done);n=!0)g=h.value,l+=g.count;t.next=30;break;case 26:t.prev=26,t.t0=t["catch"](22),c=!0,o=t.t0;case 30:t.prev=30,t.prev=31,n||null==u.return||u.return();case 33:if(t.prev=33,!c){t.next=36;break}throw o;case 36:return t.finish(33);case 37:return t.finish(30);case 38:e.searchResult.total||l?e.search.searchComplete=!0:e.search.searchComplete=!1;case 40:e.pagination.totalCount=e.searchResult.total,e.pagination.currentPage=r.get("page")?r.get("page"):1,e.$nextTick((function(){e.filterInit()}));case 43:t.next=48;break;case 45:t.prev=45,t.t1=t["catch"](0),console.error("requestTerms error...",t.t1.message);case 48:case"end":return t.stop()}}),t,null,[[0,45],[22,26,30,38],[31,,33,37]])})))()},requestGoods:function(e){var t=this;return Object(n["a"])(regeneratorRuntime.mark((function r(){var a,s,i;return regeneratorRuntime.wrap((function(r){while(1)switch(r.prev=r.next){case 0:return r.prev=0,a=t.getQueryString(),"undefined"==typeof e&&a.get("page")&&(a.delete("page"),history.replaceState(t.filterData(),null,"?"+a.toString())),r.next=5,t.$store.dispatch("network/request",{method:"post",url:"/goods/search/getCategoryGoodsList",data:{ilCategoryId:a.get("itemId"),isFirstSearch:!1,page:a.get("page")?a.get("page"):0,limit:t.pagination.goodsCount,excludeSoldOutGoods:t.search.soldOut,benefitTypeIdList:t.searchResult.filter.benefit?t.searchResult.filter.benefit:[],brandIdList:t.searchResult.filter.brand?t.searchResult.filter.brand:[],deliveryTypeIdList:t.searchResult.filter.delivery?t.searchResult.filter.delivery:[],certificationTypeIdList:t.searchResult.filter.certification?t.searchResult.filter.certification:[],storageMethodIdList:t.searchResult.filter.storage?t.searchResult.filter.storage:[],minimumPrice:a.get("minPrice")?a.get("minPrice"):0,maximumPrice:a.get("maxPrice")?a.get("maxPrice"):9999999,sortCode:t.search.sorts}});case 5:s=r.sent,i=s.data,s.code==t.$FB_CODES.API.SUCCESS&&(i.count?(t.searchResult.total=i.count,t.searchResult.goodsResultList=i.document):(t.searchResult.goodsResultList=[],t.search.searchComplete=!0,t.searchResult.total=0),t.pagination.totalCount=t.searchResult.total,t.pagination.currentPage=a.get("page")?a.get("page"):1),r.next=13;break;case 10:r.prev=10,r.t0=r["catch"](0),console.error("requestTerms error...",r.t0.message);case 13:case"end":return r.stop()}}),r,null,[[0,10]])})))()},movePaging:function(e){var t=this.getQueryString();t.get("page")!=e&&(t.set("page",e),history.replaceState(this.filterData(),null,"?"+t.toString()),this.pagination.updated=!1,this.requestGoods(e),this.pagination.currentPage=parseInt(t.get("page"))),this.$nextTick((function(){window.scrollTo(0,0)}))},pageBack:function(){var e=this.$refs["search-header-form"];e.$el.classList.contains("fb__search--fixed")?e.$el.classList.remove("fb__search--fixed"):history.back()},getViewType:function(){var e=sessionStorage.getItem("viewTypePc");return e?e.split(","):this.search.views},setViewType:function(e){sessionStorage.setItem("viewTypePc",e)},categoryToggle:function(e,t){for(var r=0,a=Object.entries(this.$refs);r<a.length;r++){var s=Object(l["a"])(a[r],2),i=s[0],n=s[1];/categories-/.test(i)&&i!=="categories-".concat(t)&&n[0].removeAttribute("open")}},reqeustInitCategory:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var r,a,s,i,l,n,c,o,u,h,g,d,f,p,y,m,v,_,b,C,R,I,P;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return r=e.queryString,t.prev=1,t.next=4,e.$store.dispatch("network/request",{method:"post",url:"/goods/category/getCategoryPageInfo",data:{ilCategoryId:r.get("itemId")}});case 4:a=t.sent,a.data,s=e.$store.getters["common/categories"],a.code==e.$FB_CODES.API.SUCCESS&&(e.allCategory=a.data,e.allCategory&&(e.newCategories(),e.pageTitle=e.allCategory.categoryName,e.lev1Id=e.allCategory.lev1CategoryId,e.lev1Title=null!==(i=null===(l=e.lev1Category[e.lev1Id])||void 0===l?void 0:l.categoryName)&&void 0!==i?i:"",(null===s||void 0===s?void 0:s.length)&&1===e.currentDepth.length&&(n=null,c=null,o=null,u=null,e.allCategory.lev1CategoryId&&(n=s.find((function(t){return t.ilCategoryId==e.allCategory.lev1CategoryId})),e.currentDepth.push({name:null===(h=n)||void 0===h?void 0:h.categoryName,path:"".concat(e.$APP_CONFIG.PAGE_URL.GOODS_LIST,"?itemId=").concat(null===(g=n)||void 0===g?void 0:g.ilCategoryId)})),n&&e.allCategory.lev2CategoryId&&(c=n.subCategoryList.find((function(t){return t.ilCategoryId==e.allCategory.lev2CategoryId})),e.currentDepth.push({name:null===(d=c)||void 0===d?void 0:d.categoryName,path:"".concat(e.$APP_CONFIG.PAGE_URL.GOODS_LIST,"?itemId=").concat(null===(f=c)||void 0===f?void 0:f.ilCategoryId)})),c&&e.allCategory.lev3CategoryId&&(o=c.subCategoryList.find((function(t){return t.ilCategoryId==e.allCategory.lev3CategoryId})),e.currentDepth.push({name:null===(p=o)||void 0===p?void 0:p.categoryName,path:"".concat(e.$APP_CONFIG.PAGE_URL.GOODS_LIST,"?itemId=").concat(null===(y=o)||void 0===y?void 0:y.ilCategoryId)})),o&&e.allCategory.lev4CategoryId&&(u=o.subCategoryList.find((function(t){return t.ilCategoryId==e.allCategory.lev4CategoryId})),e.currentDepth.push({name:null===(m=u)||void 0===m?void 0:m.categoryName,path:"".concat(e.$APP_CONFIG.PAGE_URL.GOODS_LIST,"?itemId=").concat(null===(v=u)||void 0===v?void 0:v.ilCategoryId)}))),e.$nextTick((function(){var t=e.$refs["categories-".concat(e.allCategory.lev2CategoryId)],r=e.$refs["categories-".concat(e.allCategory.lev3CategoryId)],a=e.$refs["categories-".concat(e.allCategory.lev4CategoryId)];t&&t.length&&(t[0].setAttribute("open",!0),a&&a.length&&r[0].setAttribute("open",!0))})))),t.next=34;break;case 10:for(t.prev=10,t.t0=t["catch"](1),console.error("reqeustInitCategory error...",t.t0.message),e.fetches.subCategory="error",e.fetches.subsubCategory="error",_=!0,b=!1,C=void 0,t.prev=18,R=e.sameLevelCategory[Symbol.iterator]();!(_=(I=R.next()).done);_=!0)P=I.value,P.categoryName===e.pageTitle?e.$set(e.sameLevelCategoryVisible,"id-".concat(P.ilCategoryId),!0):e.$set(e.sameLevelCategoryVisible,"id-".concat(P.ilCategoryId),!1);t.next=26;break;case 22:t.prev=22,t.t1=t["catch"](18),b=!0,C=t.t1;case 26:t.prev=26,t.prev=27,_||null==R.return||R.return();case 29:if(t.prev=29,!b){t.next=32;break}throw C;case 32:return t.finish(29);case 33:return t.finish(26);case 34:case"end":return t.stop()}}),t,null,[[1,10],[18,22,26,34],[27,,29,33]])})))()},newCategories:function(){var e=this.commonCategory,t=this.allCategory;if(this.$FB_CODES.FETCHES.SUCCESS===e.fetched){this.lev1Category=Object(C["mapKeys"])(e,"ilCategoryId"),this.lev2Category=Object(C["mapKeys"])(this.lev1Category[t.lev1CategoryId].subCategoryList,(function(e,t){return"".concat(t,"-").concat(e.ilCategoryId)})),this.sameLevelCategory=this.lev2Category;for(var r=Object.values(this.lev2Category),a=0,s=r;a<s.length;a++){var i=s[a];if(i["subCategoryList"]){this.subCategory[i.ilCategoryId]=Object.values(i["subCategoryList"]);var l=!0,n=!1,c=void 0;try{for(var o,u=this.subCategory[i.ilCategoryId][Symbol.iterator]();!(l=(o=u.next()).done);l=!0){var h=o.value;h["subCategoryList"]&&(this.subsubCategory[h.ilCategoryId]=Object.values(h["subCategoryList"]))}}catch(g){n=!0,c=g}finally{try{l||null==u.return||u.return()}finally{if(n)throw c}}}}}},requestInventoryInfo:function(){var e=this;return Object(n["a"])(regeneratorRuntime.mark((function t(){var r,a,s;return regeneratorRuntime.wrap((function(t){while(1)switch(t.prev=t.next){case 0:return t.prev=0,e.queryString,t.next=4,e.$store.dispatch("network/request",{method:"get",url:"/display/contents/getCategoryInfo/".concat("1depth-mainbanner","/","PC","/","NORMAL")});case 4:r=t.sent,a=r.data,e.lev1BannerData=Object(C["mapKeys"])(a,"ilCtgryId"),s=e.lev1BannerData,r.code==e.$FB_CODES.API.SUCCESS&&(e.pageBannerList=s),t.next=14;break;case 11:t.prev=11,t.t0=t["catch"](0),console.error("requestInventoryInfo error...",t.t0.message);case 14:case"end":return t.stop()}}),t,null,[[0,11]])})))()}}},I=R,P=(r("87df"),r("2877")),S=Object(P["a"])(I,s,i,!1,null,null,null),x=S.exports,L=r("8f7d"),O=r("caf9"),w=r("802e"),k=r("79a5");L["a"].use(w["a"]),L["a"].use(k["a"]),L["a"].use(O["a"],{error:L["b"].IMAGES.NOT_FOUND,loading:L["b"].IMAGES.IMG_LOADING}),L["c"].components=Object(a["a"])({App:x},L["c"].components),L["d"].dispatch("init").then((function(){return new L["a"](L["c"])}))},"87df":function(e,t,r){"use strict";var a=r("ee60"),s=r.n(a);s.a},ee60:function(e,t,r){}});