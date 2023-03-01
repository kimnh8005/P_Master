const odCommUtil = {
    getCode: function(id) {
      let input = $("#" + id).val();
      let query = "";

      if( input && input.length ) {

        input = input.split(",");

        query = input.filter(function(i){ 
          return i && i.length;
        }).join(",");
      }

      return query;
  },
}

const odCommCalendar = (function() {
  var instance;
  
  function initiate() {
    return {
      el: document.querySelector("#" + id),
      dayOffs: params.dayOffs,
      disableDay: params.disableDay,
    }
  }

  return {
    getInstance: function() {
      if( !instance ) {
        instance = initiate();
      }

      return instance;
    }
  }
})();