// The k6 scenario in plain Node, for machines without k6. Same flow, smaller by default.
//   node load/stress.mjs [buyers=300]     (needs AUTO_WEBHOOKS=1 on the fake provider)
const BASE = process.env.SEATLOCK_URL ?? 'http://localhost:3000';
const TOKEN_URL = process.env.TOKEN_URL ?? 'http://localhost:8081/realms/seatlock/protocol/openid-connect/token';
const BUYERS = Number(process.argv[2] ?? 300);

const token = async (username) =>
  (await (await fetch(TOKEN_URL, { method: 'POST', body: new URLSearchParams({ grant_type: 'password', client_id: 'seatlock-tests', username, password: 'password' }) })).json()).access_token;
const json = (t) => ({ 'Content-Type': 'application/json', Authorization: `Bearer ${t}` });

const olga = await token('olga');
const ev = await (await fetch(`${BASE}/events`, { method: 'POST', headers: json(olga), body: JSON.stringify({ name: 'Stress', venue: 'Arena', startsAt: '2027-06-01T20:00:00Z', sections: [{ name: 'Floor', rows: 4, seatsPerRow: 25, priceCents: 5000 }] }) })).json();
const seats = (await (await fetch(`${BASE}/events/${ev.id}/seats`)).json()).items.map((s) => s.id);
const users = [await token('alice'), await token('bob')];
const tally = {};
const count = (k) => (tally[k] = (tally[k] ?? 0) + 1);

await Promise.all(Array.from({ length: BUYERS }, async (_, i) => {
  const n = 1 + Math.floor(Math.random() * 4);
  const start = Math.floor(Math.random() * (seats.length - n));
  const h = await fetch(`${BASE}/holds`, { method: 'POST', headers: json(users[i % 2]), body: JSON.stringify({ eventId: ev.id, seatIds: seats.slice(start, start + n) }) });
  count(`hold ${h.status}`);
  if (h.status !== 201) return;
  const { id } = await h.json();
  const key = `stress-${i}-${Date.now()}`;
  const tries = await Promise.all([0, 1].map(() => fetch(`${BASE}/holds/${id}/checkout`, { method: 'POST', headers: { ...json(users[i % 2]), 'Idempotency-Key': key } })));
  for (const t of tries) count(`checkout ${t.status}`);
}));
console.log({ event: ev.id, ...tally });
console.log('Wait a few seconds for webhooks, then run db/checks/invariants.sql: it must return zero rows.');
