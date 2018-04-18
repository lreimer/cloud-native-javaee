<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html class="js flexbox flexboxlegacy canvas canvastext webgl no-touch geolocation postmessage websqldatabase indexeddb hashchange history draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow textshadow opacity cssanimations csscolumns cssgradients cssreflections csstransforms csstransforms3d csstransitions fontface generatedcontent video audio localstorage sessionstorage webworkers applicationcache svg inlinesvg smil svgclippaths">
<head>
    <title>Cloud Native Java EE Dashboard</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link rel="stylesheet" type="text/css" href="static/main.1521502160.css" media="all">
    <link rel="stylesheet" type="text/css" href="static/jquery.fancybox.min.1506407939.css" media="all">
    <link type="text/css" rel="stylesheet" href="static/1.css">

    <script src="static/particles.js" type="text/javascript"></script>
    <script src="static/jquery.min.js" type="text/javascript"></script>

    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
</head>
<body id="www-qaware-de" class="p12 l0">

<header id="stick-header" class="header-main">
    <div class="row r-11  bg-white">
        <div class="container">
            <h1>
                <a href="http://www.qaware.de/">
                    <img src="static/logo-qaware.svg" width="168" height="37" alt="QAware">
                </a>
            </h1>
            <nav class="nav-main">
                <ul>
                    <li>
                        <a href="http://www.qaware.de/kontakt/" title="Kontakt">Kontakt</a>
                    </li>
                </ul>
            </nav>
        </div>
    </div>
</header>

<article class="main" style="margin-top: 0px;">

    <div class="row r-11 bg-blue remove-padding" id="c91">
        <div class="e-header e-image" id="c90">
            <div class="wrapper">
                <div class="container">
                    <div class="col-12">
                        <h1>Cloud-native Java EE<br>
                            Process Dashboard</h1>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <section class="s-anchor bg-white" id="arbeitsplatz">
        <div class="r-11 row" id="c23">
            <div class="container">
                <form action="" method="post" onsubmit="submitForm(); return false;">
                    <fieldset style="border: 1px solid darkgray; padding: 0.5em;">
                        <legend>New Process Details</legend>
                        <label for="processId">Reference Nr.</label>
                        <input type="text" name="processId" id="processId"/>&nbsp;
                        <label for="name">Name</label>
                        <input type="text" name="name" id="name"/>&nbsp;
                        <label for="amount">Amount</label>
                        <input type="text" name="amount" id="amount"/>&nbsp;
                        <input type="submit" value="Send"/>
                    </fieldset>
                </form>
                <script>
                    function submitForm() {
                        var urlEncodedData = "";
                        var urlEncodedDataPairs = [];
                        var http = new XMLHttpRequest();

                        http.open("POST", "/api/gui", true);
                        http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

                        var processId = document.getElementById('processId').value;
                        document.getElementById('processId').value = "";

                        var name = document.getElementById('name').value;
                        document.getElementById('name').value = "";

                        var amount = document.getElementById('amount').value;
                        document.getElementById('amount').value = "";

                        urlEncodedDataPairs.push('processId' + '=' + encodeURIComponent(processId));
                        urlEncodedDataPairs.push('name' + '=' + encodeURIComponent(name));
                        urlEncodedDataPairs.push('amount' + '=' + encodeURIComponent(amount));

                        urlEncodedData = urlEncodedDataPairs.join('&').replace(/%20/g, '+');
                        http.send(urlEncodedData);
                    }
                </script>
            </div>
        </div>

        <div class="row r-12-12 bg-white" id="c333">
            <div class="container">
                <div id="events"></div>

                <script>
                    if (typeof(EventSource) !== "undefined") {
                        var source = new EventSource("/api/broadcast");

                        source.addEventListener("event", function (e) {
                            document.getElementById("events").innerHTML += "(SSE) " + e.data + "<br>";
                        }, false);
                    } else {
                        document.getElementById("events").innerHTML = "Sorry, your browser does not support server-sent events...";
                    }

                    var socket = new WebSocket(location.href.replace(/^http/, 'ws').replace('index.jsp', '') + 'events');
                    socket.onmessage = onMessage;

                    function onMessage(event) {
                        document.getElementById("events").innerHTML += "(WebSocket) " + event.data + "<br>";
                    }
                </script>
            </div>
        </div>

    </section>

</article>

</body>
</html>
