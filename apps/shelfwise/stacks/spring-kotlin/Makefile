# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
# Uses an installed Gradle (CI: gradle/actions/setup-gradle). Run `gradle wrapper` once to pin one.
GRADLE = gradle --console=plain -q
.PHONY: install lint build test run
install: ; $(GRADLE) dependencies --configuration runtimeClasspath > /dev/null
lint: install ; $(GRADLE) compileKotlin compileTestKotlin
build: install ; $(GRADLE) bootJar
test: install ; $(GRADLE) test
run: build ; java -jar build/libs/shelfwise.jar
