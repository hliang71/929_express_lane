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
                console.log('Connected: ' + frame);
                stompClient.subscribe('/user/topic/showResult', function(calResult){
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
            var num1 = document.getElementById('num1').value;
            var num2 = document.getElementById('num2').value;
            stompClient.send("/app/add", {}, JSON.stringify({ 'num1': num1, 'num2': num2 }));
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
        <label>Number One:</label><input type="text" id="num1" /><br/>
        <label>Number Two:</label><input type="text" id="num2" /><br/><br/>
        <button id="sendNum" onclick="sendNum();">Send to Add</button>
        <p id="calResponse"></p>
    </div>
</div>
</body>
</html>