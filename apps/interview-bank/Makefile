SHELL := /bin/bash
# Runs every solution in one language. CI calls each target (see .github/workflows/interview-bank.yml).
.PHONY: typescript python go java kotlin csharp
typescript: ; @for f in typescript/p*.ts; do node $$f || exit 1; done
python: ; @for f in python/p*.py; do python3 $$f || exit 1; done
go: ; cd go && go vet ./... && go test ./...
java: ; @for f in java/P*.java; do java $$f || exit 1; done
# One compile for all files (each has its own package), then run each file's main.
kotlin:
	kotlinc kotlin/*.kt -d build/kotlin.jar
	@for f in kotlin/p*.kt; do c=$$(basename $$f .kt); java -cp build/kotlin.jar:$$KOTLIN_STDLIB $$(head -3 $$f | sed -n 's/^package //p').$${c^}Kt || exit 1; done
csharp: ; @for f in csharp/p*.cs; do dotnet run $$f || exit 1; done
