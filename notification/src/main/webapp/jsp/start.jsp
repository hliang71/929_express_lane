<!DOCTYPE html>
<%@ include file="/taglibs.jsp"%>
<html>
<head>
    <title>Calculator App Using Spring 4 WebSocket</title>
    <script src="<c:url value='/assets/lib/sockjs/sockjs.js'/>"></script>
    <script src="<c:url value='/assets/lib/stomp/lib/stomp.min.js'/> "></script>
    <script type="text/javascript">
	
        var stompClient = null;

        function setConnected(connected) {
            document.getElementById('connect').disabled = connected;
            document.getElementById('disconnect').disabled = !connected;
            document.getElementById('calculationDiv').style.visibility = connected ? 'visible' : 'hidden';
            document.getElementById('calResponse').innerHTML = '';
        }

        function connect() {
            var socket = new SockJS('http://localhost:8080/notification/add');
			stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {
                setConnected(true);
                console.log('Connected: ' + reply_To);
                var token = 'abc1234';
                var reply_To = '/queue/response_'+token;
                stompClient.send("/app/add", {}, JSON.stringify({ 'replyTo': reply_To }));

                stompClient.subscribe(reply_To, function(calResult){
                    showResult(JSON.stringify(calResult.body));
                });

                stompClient.subscribe('/topic/showResult', function(calResult){
                	showResult(JSON.stringify(calResult.body));
                });
            });
        }

        function disconnect() {
            stompClient.disconnect();
            setConnected(false);
            console.log("Disconnected");
        }

        function sendNum() {

            var replyTo = document.getElementById('subscribeTo').value;
            replyTo = '/queue/response_' + replyTo;
            stompClient.send("/app/add", {}, JSON.stringify({ 'replyTo': replyTo}));
            stompClient.subscribe(replyTo, function(calResult){
                showResult(JSON.stringify(calResult.body));
            });
        }

        function showResult(message) {
            var response = document.getElementById('calResponse');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.appendChild(document.createTextNode(message));
            response.appendChild(p);
        }
    </script>
</head>
<body>
<noscript><h2>Enable Java script and reload this page to run Websocket Demo</h2></noscript>
<h1>Calculator App Using Spring 4 WebSocket</h1>
<div>
    <div>
        <button id="connect" onclick="connect();">Connect</button>
        <button id="disconnect" disabled="disabled" onclick="disconnect();">Disconnect</button><br/><br/>
    </div>
    <div id="calculationDiv">
        <label>Subscribe To:</label><input type="text" id="subscribeTo" /><br/><br/>
        <button id="sendNum" onclick="sendNum();">Send to Add</button>
        <p id="calResponse"></p>
    </div>
</div>
</body>
</html>