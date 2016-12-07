function chart (teamNumber) {

    loadChart (teamNumber) ;

    var SECONDS_TO_RELOAD = 20 ;
    setInterval(loadChart (teamNumber), 1000 * SECONDS_TO_RELOAD);
}

function loadChart  (teamNumber) {
    $.ajax ({url: '../chart?teamNumber=' + teamNumber, success: function (result) {
        $('#chart').highcharts(result);
    }}) ;
}
