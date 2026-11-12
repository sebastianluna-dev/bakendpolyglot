# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; dotnet restore
lint: install ; dotnet build
build: install ; dotnet publish src/Shelfwise -c Release -o bin/publish
test: install ; dotnet test
run: ; dotnet run --project src/Shelfwise
