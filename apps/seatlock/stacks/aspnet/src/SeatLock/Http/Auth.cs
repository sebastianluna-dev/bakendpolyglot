using System.Security.Claims;
using System.Text.Json;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Protocols;
using Microsoft.IdentityModel.Protocols.OpenIdConnect;
using Microsoft.IdentityModel.Tokens;
using SeatLock.Domain;

namespace SeatLock.Http;

/// <summary>
/// JwtBearer setup (Chapter 19). Keys come straight from the JWKS URL rather than discovery, so the
/// API can fetch them over the internal network while tokens carry the public issuer URL.
/// </summary>
public static class Auth
{
    public static void Configure(JwtBearerOptions o, AppConfig config)
    {
        o.MapInboundClaims = false; // keep "sub" as "sub"
        o.RequireHttpsMetadata = false;
        o.ConfigurationManager = new ConfigurationManager<OpenIdConnectConfiguration>(
            config.JwksUrl, new JwksRetriever(), new HttpDocumentRetriever { RequireHttps = false });
        o.TokenValidationParameters = new TokenValidationParameters
        {
            ValidIssuer = config.Issuer,
            ValidAudience = config.Audience,
            ValidAlgorithms = [SecurityAlgorithms.RsaSha256], // never "none", never HS256 with a public key
            ClockSkew = TimeSpan.FromSeconds(30),
            RoleClaimType = "role",
            NameClaimType = "sub",
        };
        o.Events = new JwtBearerEvents
        {
            // Keycloak nests roles in realm_access.roles; flatten them so [Authorize(Roles = …)] works.
            OnTokenValidated = ctx =>
            {
                if (ctx.Principal?.Identity is ClaimsIdentity id && id.FindFirst("realm_access") is { } claim)
                {
                    using var doc = JsonDocument.Parse(claim.Value);
                    if (doc.RootElement.TryGetProperty("roles", out var roles) && roles.ValueKind == JsonValueKind.Array)
                        foreach (var r in roles.EnumerateArray())
                            id.AddClaim(new Claim("role", r.GetString() ?? ""));
                }
                return Task.CompletedTask;
            },
            OnChallenge = async ctx =>
            {
                ctx.HandleResponse(); // our problem body instead of an empty 401
                await Problems.Write(ctx.HttpContext, DomainException.Unauthorized());
            },
            OnForbidden = ctx => Problems.Write(ctx.HttpContext, DomainException.Forbidden()),
        };
    }

    /// <summary>Turns a bare JWKS document into the configuration JwtBearer expects.</summary>
    sealed class JwksRetriever : IConfigurationRetriever<OpenIdConnectConfiguration>
    {
        public async Task<OpenIdConnectConfiguration> GetConfigurationAsync(string address, IDocumentRetriever retriever, CancellationToken cancel)
        {
            var json = await retriever.GetDocumentAsync(address, cancel);
            var config = new OpenIdConnectConfiguration();
            foreach (var key in new JsonWebKeySet(json).GetSigningKeys()) config.SigningKeys.Add(key);
            return config;
        }
    }

    public static string Sub(this ClaimsPrincipal user) => user.FindFirstValue("sub") ?? throw DomainException.Unauthorized();
}
