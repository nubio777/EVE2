function map(requestedTeamNumber) {

    // Request new data after this seconds
    SECONDS_TO_RELOAD = 10;

    // Important variable: it indicates whether all teams should be seen or only just one
    // In the first case, only 'MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS' (see bellow) will be shown in the image.
    allTeamsTogether = typeof requestedTeamNumber === 'undefined' || requestedTeamNumber == null || requestedTeamNumber == 0;

    // Data variables
    data = [];                  // Structure of the type data[team][minute][serviceNr]
    services = [];              // where all services names are stores
    nrOfActiveServices = [];    // Nr. of active services for each team number
    originOfTime = new Date(); // It is the Date at which data recording starts
    lastMinute = 0;             // Last minute of data available here in the client
    dataPendingInServer = false;  // Indicates whether the server has sent, or not, all data available (the server will send     dataPendingInServer = false;  // Indicates whether the server has sent, or not, all data available (the server will send no more than 60 minutes)


    // Drawing
    ctx = document.getElementById("map").getContext("2d"); // The context where the image is drawn
    firstMinuteToDraw = 0;      // By default, it is zero, but it needs to be calculated when drawing when allTeamsTogether = true

    PIXELS_PER_MINUTE = 10;
    PIXELS_PER_SERVICE = 10;
    NUMBER_OF_TEAMS = 20;       // Important: keep this number exactly equals to the number of blues
    PIXELS_TO_DRAW_SERVICES = 150;
    MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS = 10;
    ANIMATION_TIME = 50;        // Time in milliseconds to perform animation loading
    INFO_COLOR = "#202BB4";    // Color used in titles and header

    OK = "rgb(9,197,42)";
    WARNING = "rgb(255, 201, 54)";
    CRITICAL = "rgb(255, 30, 12)";
    ERROR = "rgb(32, 43, 180)";
    UNKNOWN = "#000000";


    // Variables related to click event
    posX = -1;
    posY = -1;
    CROSS_LENGTH = 20;
    CROSS_COLOR = "#000000";


    // Function to init data requests
    function init() {
        var teamNumber = allTeamsTogether ? 0 : requestedTeamNumber;
        $.ajax({url: "../load?teamNumber=" + teamNumber + "&lastMinuteAvailable=" + lastMinute, success: loadDataAndDraw});
    }

    function loadDataAndDraw(result) {

        var bulk = result.split("H");

        // OK ?
        if (bulk[0] === 'OK') {

            // Pending data in server ?
            dataPendingInServer = bulk[1] == "true";

            // Time (in millisecs from 1 jan 70) of the very first data record
            originOfTime = new Date(parseInt(bulk[2]));

            // First and last minute in this packet
            var dataFirstMinute = parseInt(bulk[3]);
            lastMinute = parseInt(bulk[4]);

            // The contents for each team
            var contents = bulk[5].split("T");
            for (var i = 0; i < contents.length; i++)
                loadDataForTeam(dataFirstMinute, contents[i]);

            // The services
            loadServices(bulk[6]);

            // Draw
            draw();
        }
    }

    function loadDataForTeam(firstMinute, content) {
        if (isNull(content)) return;

        var parts = content.split("$");

        var teamNumber = parseInt(parts[0]);
        nrOfActiveServices[teamNumber] = parseInt(parts[1]);
        var lines = parts[2];
        if (isNull(lines)) return;

        var destination = isNull(data[teamNumber]) ? [] : data[teamNumber];

        loadLinesForTeam(firstMinute, destination, lines);

        data[teamNumber] = destination;
    }

    function loadLinesForTeam(firstMinute, destination, lines) {

        var line = lines.split("L");

        for (var minute = firstMinute; minute <= lastMinute; minute++) {

            var lineNumber = minute - firstMinute;
            var colors = line[lineNumber].split("|");

            if (isNull(destination[minute]))  destination[minute] = [];

            for (var numService = 0; numService < colors.length; numService++)
                destination[minute][numService] = colors[numService];
        }
    }

    function loadServices(content) {
        if (isNull(content)) return;
        var parts = content.split("|");
        if (isNull(parts)) return;
        for (i = 0; i < parts.length; i++) services[i] = parts[i];
    }

    function draw() {
        var initTime = new Date().getTime();

        // Set canvas width
        if (allTeamsTogether)   ctx.canvas.width = MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS * PIXELS_PER_MINUTE * NUMBER_OF_TEAMS;
        else                    ctx.canvas.width = (lastMinute + 1) * PIXELS_PER_MINUTE;
        ctx.canvas.width += PIXELS_TO_DRAW_SERVICES;

        // Set canvas height
        ctx.canvas.height = services.length * PIXELS_PER_SERVICE;


        // Find first minute to draw
        if (allTeamsTogether)
            firstMinuteToDraw = lastMinute > MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS ? lastMinute - MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS + 1 : 0;
        else
            firstMinuteToDraw = 0;


        // Draw
        drawData();
        drawNet();
        drawServices();
        drawGlobalInfo();
        if (posX != -1) drawCross();


        // Scroll to the right as much as possible (in case there's no click !)
//        if (posX == -1) $("body").scrollLeft(10000);

        // Animation
        if (dataPendingInServer) setTimeout(init, ANIMATION_TIME);

//        console.info("draw process = " + (new Date().getTime() - initTime));
    }

    function getTitleColor(percentageOfActiveServices) {
        if (percentageOfActiveServices > 90) return OK;
        if (percentageOfActiveServices > 80) return CRITICAL;
        else return WARNING;
    }

    // Function to draw data
    function drawData() {

        for (var teamNumber = 1; teamNumber <= NUMBER_OF_TEAMS; teamNumber++) {

            var dataForThisTeam = data[teamNumber];
            if (isNull(dataForThisTeam)) continue;

            var xOrigin = allTeamsTogether ? (teamNumber - 1) * MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS * PIXELS_PER_MINUTE : 0;

            drawDataForTeam(dataForThisTeam, xOrigin);
        }
    }

    // Function to draw data for a given team, from firstMinuteToDraw on, and from a given x origin
    function drawDataForTeam(dataForThisTeam, xOrigin) {
        for (var i = firstMinuteToDraw; i <= lastMinute; i++) {

            var xPosition = xOrigin + (i - firstMinuteToDraw) * PIXELS_PER_MINUTE;

            for (var j = 0; j < dataForThisTeam[i].length; j++) {
                var yPosition = j * PIXELS_PER_SERVICE;
                ctx.fillStyle = getColorFromSpec(dataForThisTeam[i][j]);
                ctx.fillRect(xPosition, yPosition, PIXELS_PER_MINUTE, PIXELS_PER_SERVICE);
            }
        }
    }

    // Function to calculate the color of a spot based on the incoming data
    function getColorFromSpec(colorSpec) {

        if (isNull(colorSpec) || colorSpec == "" || colorSpec === 'O') return OK;
        if (colorSpec === 'W') return WARNING;
        if (colorSpec === 'C') return CRITICAL;
        if (colorSpec === 'E') return ERROR;
        if (colorSpec === 'U') return UNKNOWN;

        return "#" + colorSpec;

    }

    // Draw services at the end
    var verticalScrollFixed = false ;
    function drawServices() {
        var servicesCtx = document.getElementById('mapServices').getContext("2d");
        servicesCtx.canvas.height = services.length * PIXELS_PER_SERVICE ;
        servicesCtx.canvas.width = PIXELS_TO_DRAW_SERVICES;
        servicesCtx.fillStyle = INFO_COLOR;
        servicesCtx.font = "10px Arial";
        for (var i = 0; i < services.length; i++)
            servicesCtx.fillText(services[i], 5, PIXELS_PER_SERVICE * (i + 0.9));

        if (!verticalScrollFixed) {
            $(window).scroll(function () {
                $('#mapServices').css('top', -$(window).scrollTop() + 150);
            });
            verticalScrollFixed = true ;
        }
    }

    // Function to draw global info (title and header contents)
    var horizontalScrollFixed = false;

    function drawGlobalInfo() {

        var HEADER_HEIGHT = 50;

        var titleDiv = $('#title');
        var teamNameDiv = $('#teamName');
        var activeServicesDiv = $('#activeServices');

        var headerCtx = document.getElementById("mapHeader").getContext("2d");
        headerCtx.canvas.width = ctx.canvas.width;
        headerCtx.canvas.height = HEADER_HEIGHT;

        // Blues...
        headerCtx.fillStyle = INFO_COLOR;
        headerCtx.strokeStyle = INFO_COLOR;
        ctx.strokeStyle = INFO_COLOR;

        // Scroll mapTitles horizontally following general window scroll (otherwise the position: fixed won't allow it !!!)
        if (!horizontalScrollFixed) {
            $(window).scroll(function () {
                $('#mapHeader').css('left', -$(window).scrollLeft());
            });
            horizontalScrollFixed = true;
        }

        if (allTeamsTogether) {

            var percentageOfTotalActive = 0;
            for (var teamNr = 1; teamNr <= NUMBER_OF_TEAMS; teamNr++) {
                // Draw teams names
                var active = nrOfActiveServices[teamNr];
                var percentage = Math.round(active / services.length * 100);
                percentageOfTotalActive += percentage;

                headerCtx.fillStyle = getTitleColor(percentage);
                headerCtx.font = "30px Arial";
                var xTextPosition = (teamNr - 2 / 3) * PIXELS_PER_MINUTE * MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS;
                headerCtx.fillText(teamNr, xTextPosition, HEADER_HEIGHT * 0.6);

                headerCtx.font = "10px Arial";
                xTextPosition = xTextPosition - 10;
                var text = active + " ↑ (" + percentage + " %)";
                headerCtx.fillText(text, xTextPosition, HEADER_HEIGHT * 0.9);
            }

            // Title info
            teamNameDiv.html("All Teams");
            activeServicesDiv.html(Math.round(percentageOfTotalActive / NUMBER_OF_TEAMS) + " % services up");


        } else {
            // Draw time (hours)
            headerCtx.font = "30px Arial";

            var OFFSET = 5;
            for (var minute = firstMinuteToDraw; minute <= lastMinute; minute++) {
                if (minute == 0)
                    headerCtx.fillText(originOfTime.getHours() + ":" + originOfTime.getMinutes(), OFFSET, HEADER_HEIGHT * 0.8);
                else {
                    var now = new Date(originOfTime.getTime() + minute * 60 * 1000);
                    if (now.getMinutes() == 0 || now.getMinutes() == 30) {
                        var xPosition = OFFSET + minute * PIXELS_PER_MINUTE;
                        var mins = now.getMinutes() == 0 ? "00" : now.getMinutes();
                        headerCtx.fillText(now.getHours() + ":" + mins, xPosition, HEADER_HEIGHT * 0.8);

                        headerCtx.lineWidth = 1;
                        headerCtx.beginPath();
                        headerCtx.moveTo(minute * PIXELS_PER_MINUTE, 0);
                        headerCtx.lineTo(minute * PIXELS_PER_MINUTE, headerCtx.canvas.height);
                        headerCtx.stroke();

                        ctx.lineWidth = 1;
                        ctx.beginPath();
                        ctx.moveTo(minute * PIXELS_PER_MINUTE, 0);
                        ctx.lineTo(minute * PIXELS_PER_MINUTE, ctx.canvas.height);
                        ctx.stroke();
                    }
                }
            }

            // Title info
            active = nrOfActiveServices[requestedTeamNumber];
            percentage = Math.round(active / services.length * 100);

            teamNameDiv.html("Team " + requestedTeamNumber);
            activeServicesDiv.html(active + ' services up (' + percentage + '%)');
        }

        // Show title
        var popupOptions = {modal: false, opacity: 0.5, escClose: false, positionStyle: "fixed"};
        popupOptions.position = [130, 250];
        titleDiv.bPopup(popupOptions);
        titleDiv.draggable();
    }

    // Function to draw horizontal and vertical white lines
    function drawNet() {
        ctx.strokeStyle = "#FFFFFF";

        var dataWidth = ctx.canvas.width - PIXELS_TO_DRAW_SERVICES;
        ctx.lineWidth = 0.6;

        // Vertical lines
        for (var minute = 0; minute < dataWidth; minute += PIXELS_PER_MINUTE) {
            ctx.beginPath();
            ctx.moveTo(minute, 0);
            ctx.lineTo(minute, ctx.canvas.height);
            ctx.stroke();
        }

        // Horizontal lines
        for (var pixelY = 0; pixelY < ctx.canvas.height; pixelY += PIXELS_PER_SERVICE) {
            ctx.beginPath();
            ctx.moveTo(0, pixelY);
            ctx.lineTo(dataWidth, pixelY);
            ctx.stroke();
        }

        // When all teams together, vertical lines...
        if (allTeamsTogether) {
            ctx.strokeStyle = "#000000";
            for (var teamNr = 2; teamNr <= NUMBER_OF_TEAMS + 1; teamNr++) {
                ctx.beginPath();
                var xPosition = (teamNr - 1) * MINS_VISIBLE_PER_TEAM_IF_ALL_TEAMS * PIXELS_PER_MINUTE;
                ctx.moveTo(xPosition, 0);
                ctx.lineTo(xPosition, ctx.canvas.height);
                ctx.lineWidth = 3;
                ctx.stroke();
            }
        }
    }

    // Function to draw a cross around a mouse click
    function drawCross() {
        if (posX == -1) return;

        var crossLength = 20;
        ctx.beginPath();
        ctx.lineWidth = "2";
        ctx.strokeStyle = INFO_COLOR;
        ctx.beginPath();
        ctx.moveTo(posX - crossLength, posY);
        ctx.lineTo(posX + crossLength, posY);
        ctx.moveTo(posX, posY - crossLength);
        ctx.lineTo(posX, posY + crossLength);
        ctx.stroke();
    }

    // Function to handle the click on the image
    function clickHandle(event) {
        // Prevent default
        event.preventDefault();

        // Fix click position and draw
        posX = Math.floor(event.pageX - $(this).offset().left);
        posY = Math.floor(event.pageY - $(this).offset().top);

        // Find and validate the service
        var serviceNumber = Math.floor(posY / PIXELS_PER_SERVICE);
        if (serviceNumber >= services.length || serviceNumber < 0) return; // Basic check: do nothing if clicked outside the image

        // Find team and minute
        var teamNumber = requestedTeamNumber;
        var minute = Math.floor(posX / PIXELS_PER_MINUTE);
        if (allTeamsTogether) {
            var pixelsPerTeam = (lastMinute - firstMinuteToDraw) * PIXELS_PER_MINUTE;

            teamNumber = Math.floor(posX / pixelsPerTeam) + 1;
            minute = firstMinuteToDraw + Math.floor((posX - teamNumber * pixelsPerTeam) / PIXELS_PER_MINUTE);
        }

        // Find value via ajax and show in popup when result arrives
        var urlToData = "../value?teamNumber=" + teamNumber + "&minute=" + minute + "&serviceNumber=" + serviceNumber;
        $.ajax({url: urlToData, success: showClickResult});

        // Redraw (so that a cross appears at click point)
        draw();
    }

    // Function to show click results
    function showClickResult(result) {
        var res = result.split('|');

        $('#status').html(res[0]);
        $('#service').html(res[1]);
        $('#host').html(res[2] + " " + res[3]);
        $('#time').html(res[4]);
        $('#message').html(res[5]);

        $('#infoAttack').bPopup({
            modal: false,
            opacity: 0,
            positionStyle: "absolute",
            position: ["auto", "auto"],
            onClose: function () {
                posX = -1;
                draw();
            }
        }).draggable();
    }

    // Helper function
    function isNull(something) {
        return typeof something === 'undefined' || something === null;
    }

    // Init data requests and redraw cycles
    init();

    // Schedule permanent data requests and redraws
    setInterval(init, 1000 * SECONDS_TO_RELOAD);

    // Listen to click events
    $('#map').click(clickHandle);
}