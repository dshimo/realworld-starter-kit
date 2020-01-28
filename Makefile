test:
	echo "pizza"

install:
	mvn clean install -DskipTests

dropins:
	cp utility/com.ibm.ws.security.mp.jwt_1.0.37.jar target/liberty/wlp/lib/
	rm -rf target/liberty/wlp/usr/servers/defaultServer/workarea/

drun: install dropins
	mvn liberty:run-server

run:
	mvn liberty:run-server

fullrun:
	mvn clean install liberty:run-server -DskipTests
	