# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; dotnet restore
lint: install ; dotnet build
build: install ; dotnet publish src/SeatLock -c Release -o bin/publish
test: install ; dotnet test
run: ; dotnet run --project src/SeatLock
