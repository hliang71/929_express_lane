## Overview

A sample demonstrating capabilities in the Spring Framework to build WebSocket-style messaging applications. The application uses [STOMP](http://stomp.github.io/) (over WebSocket) for messaging between browsers and server and [SockJS](https://github.com/sockjs/sockjs-protocol) for WebSocket fallback options.

Client-side libraries used:
* [stomp-websocket](https://github.com/jmesnil/stomp-websocket/)
* [sockjs-client](https://github.com/sockjs/sockjs-client)
* [Twitter Bootstrap](http://twitter.github.io/bootstrap/)
* [Knockout.js](http://knockoutjs.com/)

Server-side runs on `Tomcat 7.0.47+`, `Jetty 9.0.7+`, or `Glassfish 4.0`. Other servlet containers should also function correctly via fallback options (assuming Servlet 3.0) but they don't support WebSocket yet.

Also see the [blog post](http://blog.springsource.org/2013/07/24/spring-framework-4-0-m2-websocket-messaging-architectures/) introducing these features.

### Tomcat 7/8

For Tomcat 8, set `TOMCAT8_HOME` as an environment variable and use [deployTomcat8.sh] in this directory.

For Tomcat 7, you can use `mvn tomcat7:run`.

Open a browser and go to <http://localhost:8080/notification/index.html>


### Using a Message Broker

Out of the box, a _"simple" message broker_ is used to send messages to subscribers (e.g. stock quotes) but you can optionally use a fully featured STOMP message broker such as `RabbitMQ`, `ActiveMQ`, and others, by following these steps:

1.   Install and start the message broker. For RabbitMQ make sure you've also installed the [RabbitMQ STOMP plugin](http://www.rabbitmq.com/stomp.html). For ActiveMQ you need to configure a [STOMP transport connnector](http://activemq.apache.org/stomp.html).
2.   Use the `MessageBrokerConfigurer` in [WebSocketConfig.java](/config/WebSocketConfig.java) to enable the STOMP broker relay instead of the simple broker.
3.   You may also need to configure additional STOMP broker relay properties such as `relayHost`, `relayPort`, `systemLogin`, `systemPassword`, depending on your message broker. The default settings should work for RabbitMQ and ActiveMQ.




