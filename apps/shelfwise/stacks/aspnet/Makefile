# Same targets in every stack, so one CI workflow drives them all (Chapter 42).
.PHONY: install lint build test run
install: ; dotnet restore
lint: install ; dotnet build --no-restore
build: install ; dotnet publish src/Shelfwise -c Release -o bin/publish --no-restore
test: install ; dotnet test --no-restore
run: ; dotnet run --project src/Shelfwise
