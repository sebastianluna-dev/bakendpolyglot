// Phase 3 gate (Chapter 27): 1,000 virtual users fight over a sold-out event. Each picks random seats,
// holds them, checks out (retrying with the same Idempotency-Key), and the fake provider confirms by
// webhook — sometimes twice. Afterwards db/checks/invariants.sql must return zero rows.
//
//   k6 run -e SEATLOCK_URL=http://localhost:3000 load/checkout.k6.js
import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

const BASE = __ENV.SEATLOCK_URL || 'http://localhost:3000';
const TOKEN_URL = __ENV.TOKEN_URL || 'http://localhost:8081/realms/seatlock/protocol/openid-connect/token';

export const options = {
  scenarios: { buyers: { executor: 'per-vu-iterations', vus: Number(__ENV.VUS || 1000), iterations: 3, maxDuration: '5m' } },
  thresholds: { http_req_failed: ['rate<0.5'], checks: ['rate>0.99'] },
};

const holdsWon = new Counter('holds_won');
const holdsLost = new Counter('holds_lost');
const checkouts = new Counter('checkouts');

function token(form) {
  const r = http.post(TOKEN_URL, form);
  return r.json('access_token');
}

export function setup() {
  const olga = token({ grant_type: 'password', client_id: 'seatlock-tests', username: 'olga', password: 'password' });
  const ev = http.post(`${BASE}/events`, JSON.stringify({
    name: 'Sold-out show', venue: 'Arena', startsAt: '2027-06-01T20:00:00Z',
    sections: [{ name: 'Floor', rows: 10, seatsPerRow: 30, priceCents: 5000 }], // 300 seats for 1,000 buyers
  }), { headers: { 'Content-Type': 'application/json', Authorization: `Bearer ${olga}` } });
  const eventId = ev.json('id');
  const seats = http.get(`${BASE}/events/${eventId}/seats`).json('items').map((s) => s.id);
  const users = ['alice', 'bob'].map((u) => token({ grant_type: 'password', client_id: 'seatlock-tests', username: u, password: 'password' }));
  return { eventId, seats, users };
}

export default function ({ eventId, seats, users }) {
  const auth = { 'Content-Type': 'application/json', Authorization: `Bearer ${users[__VU % 2]}` };
  const n = 1 + Math.floor(Math.random() * 4);
  const start = Math.floor(Math.random() * (seats.length - n));
  const wanted = seats.slice(start, start + n);

  const hold = http.post(`${BASE}/holds`, JSON.stringify({ eventId, seatIds: wanted }), { headers: auth });
  check(hold, { 'hold is 201 or 409': (r) => r.status === 201 || r.status === 409 });
  if (hold.status !== 201) {
    holdsLost.add(1);
    return;
  }
  holdsWon.add(1);

  const key = `${__VU}-${__ITER}-${Date.now()}`;
  for (let attempt = 0; attempt < 3; attempt++) { // a client retrying after a "timeout"
    const res = http.post(`${BASE}/holds/${hold.json('id')}/checkout`, null, { headers: { ...auth, 'Idempotency-Key': key } });
    check(res, { 'checkout is 201, 409 in progress or 502': (r) => [201, 409, 502].includes(r.status) });
    if (res.status === 201) {
      checkouts.add(1);
      break;
    }
    sleep(0.2);
  }
}
