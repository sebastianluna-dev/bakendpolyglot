// A tiny stand-in for Keycloak's seatlock realm, for machines without Docker. It serves the same
// discovery, JWKS and token endpoints and issues tokens with the same claims Keycloak does:
// iss, sub, aud=seatlock-api, azp, realm_access.roles, scope. Never use it outside development.
//   node server.mjs            → http://localhost:8081/realms/seatlock
import { createServer } from 'node:http';
import { generateKeyPairSync, createSign, randomUUID } from 'node:crypto';

const PORT = Number(process.env.PORT ?? 8081);
const ISSUER = process.env.ISSUER ?? `http://localhost:${PORT}/realms/seatlock`;
const BASE = '/realms/seatlock';
const { publicKey, privateKey } = generateKeyPairSync('rsa', { modulusLength: 2048 });
const kid = randomUUID();
const jwk = { ...publicKey.export({ format: 'jwk' }), kid, use: 'sig', alg: 'RS256' };

// Same users, clients and secrets as keycloak/seatlock-realm.json.
const USERS = {
  alice: { sub: 'b1f0c5a4-1a6e-4a52-9c1e-0a1b2c3d4e01', roles: ['customer'] },
  bob: { sub: 'b1f0c5a4-1a6e-4a52-9c1e-0a1b2c3d4e02', roles: ['customer'] },
  olga: { sub: 'b1f0c5a4-1a6e-4a52-9c1e-0a1b2c3d4e03', roles: ['box-office'] },
};
const PASSWORD = 'password';
const CLIENTS = {
  'seatlock-web': { public: true, audience: 'seatlock-api' },
  'seatlock-tests': { public: true, audience: 'seatlock-api' },
  'other-app': { public: true, audience: 'other-app' }, // tokens for some other API
  'hold-reaper': { secret: 'reaper-secret', audience: 'seatlock-api', serviceRoles: ['box-office'] },
};

const b64 = (v) => Buffer.from(typeof v === 'string' ? v : JSON.stringify(v)).toString('base64url');

function sign(claims) {
  const head = b64({ alg: 'RS256', typ: 'JWT', kid });
  const body = b64(claims);
  const sig = createSign('RSA-SHA256').update(`${head}.${body}`).sign(privateKey).toString('base64url');
  return `${head}.${body}.${sig}`;
}

function issue(clientId, client, subject) {
  const now = Math.floor(Date.now() / 1000);
  const ttl = 300;
  const claims = {
    iss: ISSUER, sub: subject.sub, aud: client.audience, azp: clientId, typ: 'Bearer',
    iat: now, exp: now + ttl, jti: randomUUID(),
    realm_access: { roles: subject.roles }, scope: 'openid profile email',
    ...(subject.username ? { preferred_username: subject.username } : {}),
  };
  return { access_token: sign(claims), token_type: 'Bearer', expires_in: ttl };
}

function send(res, status, body) {
  res.writeHead(status, { 'content-type': 'application/json' });
  res.end(JSON.stringify(body));
}

createServer((req, res) => {
  const url = new URL(req.url, ISSUER);
  if (req.method === 'GET' && url.pathname === `${BASE}/.well-known/openid-configuration`) {
    return send(res, 200, {
      issuer: ISSUER,
      jwks_uri: `${ISSUER}/protocol/openid-connect/certs`,
      token_endpoint: `${ISSUER}/protocol/openid-connect/token`,
      id_token_signing_alg_values_supported: ['RS256'],
    });
  }
  if (req.method === 'GET' && url.pathname === `${BASE}/protocol/openid-connect/certs`) return send(res, 200, { keys: [jwk] });
  if (req.method === 'POST' && url.pathname === `${BASE}/protocol/openid-connect/token`) {
    let raw = '';
    req.on('data', (c) => (raw += c));
    req.on('end', () => {
      const f = new URLSearchParams(raw);
      const clientId = f.get('client_id') ?? '';
      const client = CLIENTS[clientId];
      if (!client) return send(res, 401, { error: 'invalid_client' });
      if (f.get('grant_type') === 'password' && client.public) {
        const user = USERS[f.get('username') ?? ''];
        if (!user || f.get('password') !== PASSWORD) return send(res, 401, { error: 'invalid_grant' });
        return send(res, 200, issue(clientId, client, { ...user, username: f.get('username') }));
      }
      if (f.get('grant_type') === 'client_credentials' && client.secret && f.get('client_secret') === client.secret) {
        return send(res, 200, issue(clientId, client, { sub: `service-account-${clientId}`, roles: client.serviceRoles }));
      }
      return send(res, 400, { error: 'unsupported_grant_type' });
    });
    return;
  }
  send(res, 404, { error: 'not_found' });
}).listen(PORT, () => console.log(`dev IdP: ${ISSUER}`));
