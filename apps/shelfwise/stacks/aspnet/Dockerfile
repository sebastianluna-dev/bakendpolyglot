FROM mcr.microsoft.com/dotnet/sdk:10.0 AS build
WORKDIR /src
COPY src/Shelfwise/Shelfwise.csproj src/Shelfwise/
RUN dotnet restore src/Shelfwise/Shelfwise.csproj
COPY src ./src
RUN dotnet publish src/Shelfwise -c Release -o /out --no-restore

FROM mcr.microsoft.com/dotnet/aspnet:10.0
WORKDIR /app
COPY --from=build /out ./
USER $APP_UID
EXPOSE 3000
ENTRYPOINT ["dotnet", "Shelfwise.dll"]
