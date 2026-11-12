# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
MVN = mvn -B -q
.PHONY: install lint build test run
install: ; $(MVN) dependency:resolve
lint: install ; $(MVN) compile
build: install ; $(MVN) package -DskipTests
test: install ; $(MVN) test
run: build ; java -jar target/seatlock.jar
