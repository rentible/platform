WallRide
========
[![Build Status](https://travis-ci.org/tagbangers/wallride.svg?branch=master)](https://travis-ci.org/tagbangers/wallride) [![Join the chat at https://gitter.im/talk-wallride/Lobby](https://badges.gitter.im/talk-wallride/Lobby.svg)](https://gitter.im/talk-wallride/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

WallRide is a multilingual easy-to-customize open source CMS made by 100% pure Java,
using [Spring Framework](http://projects.spring.io/spring-framework/), [Hibernate](http://hibernate.org/) and [Thymeleaf](http://www.thymeleaf.org/).

WallRide focuses on sophisticated UI, simple and clean source code
and easy deploy to AWS BeansTalk(of course other servlet containers)

We hope this CMS is loved by many developers of principles all over the world.

Documentation
========
[Documentation is here.](http://wallride.org/docs/guide.html)

Screen Shots
========
[Screen Shots is here.](https://github.com/tagbangers/wallride/wiki/Screen-shots)

VM arguments to run on local machine
========


-Dspring.datasource.url=jdbc:postgresql://{url}/wallride?stringtype=unspecified<br>
-Dspring.datasource.username={username}<br>
-Dspring.datasource.password={password}<br>
-Dwallride.home={user.home}/.wallride/home/media/<br>


Build
========

war build: mvnw package -P war<br>
jab build: mvnw package -P jar && JAVA_OPTS="-Dwallride.home=file:/{user.home}/.wallride/home" ./wallride-bootstrap/target/wallride-bootstrap-X.X.X.jar<br>