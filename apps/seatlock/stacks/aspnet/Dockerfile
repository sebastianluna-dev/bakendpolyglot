FROM mcr.microsoft.com/dotnet/sdk:10.0 AS build
WORKDIR /src
COPY src/SeatLock/SeatLock.csproj src/SeatLock/
RUN dotnet restore src/SeatLock/SeatLock.csproj
COPY src ./src
RUN dotnet publish src/SeatLock -c Release -o /out --no-restore

FROM mcr.microsoft.com/dotnet/aspnet:10.0
WORKDIR /app
COPY --from=build /out ./
USER $APP_UID
EXPOSE 3000
ENTRYPOINT ["dotnet", "SeatLock.dll"]
