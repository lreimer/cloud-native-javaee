<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Cloud Native Java EE Dashboard</title>
</head>
<body>

<div id="gui">
    <form action="/api/gui" method="post">
        <label for="processId">Reference Nr.</label>
        <input type="text" name="processId" id="processId"/><br>
        <label for="name">Name</label>
        <input type="text" name="name" id="name"/><br>
        <label for="amount">Amount</label>
        <input type="text" name="amount" id="amount"/><br>
        <br>
        <input type="submit" value="Send"/>
    </form>
</div>

<div id="events"></div>

<script>
    if (typeof(EventSource) !== "undefined") {
        var source = new EventSource("/api/broadcast");

        source.addEventListener("event", function (e) {
            document.getElementById("events").innerHTML += e.data + "<br>";
        }, false);
    } else {
        document.getElementById("events").innerHTML = "Sorry, your browser does not support server-sent events...";
    }
</script>
</body>
</html>
