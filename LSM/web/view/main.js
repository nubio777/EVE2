function loadHTMLContents(url, htmlDestination, functionToExecuteAfter) {
    $.ajax({url: url, success: function (result) {
        htmlDestination.html(result);
        functionToExecuteAfter();
    }});
}

function initMap(teamNumber) {
    $(document).ready(function () {
        loadHTMLContents('./map/map.html', $('#mainContent'), function () {
            map(teamNumber);
        });
    });
}

function initChart(teamNumber) {
    $(document).ready(function () {
        loadHTMLContents('./chart/chart.html', $('#mainContent'), function () {
            chart(teamNumber) ;
        });
    });
}

function initCurrent(teamNumber) {
    $(document).ready(function () {
        loadHTMLContents('./current/current.html', $('#mainContent'), function () {
        });
    });
}
