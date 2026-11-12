// The payment provider, called outside any database transaction (Chapter 27 pitfall) with the order
// id as its idempotency key, so a retried checkout never creates a second payment intent.
import { Inject, Injectable } from '@nestjs/common';
import { CONFIG, type Config } from '../config.js';
import { paymentProviderUnavailable } from '../common/errors.js';

@Injectable()
export class PaymentsClient {
  constructor(@Inject(CONFIG) private readonly config: Config) {}

  async createIntent(orderId: string, amountCents: number, currency: string): Promise<string> {
    try {
      const res = await fetch(`${this.config.PAYMENTS_URL}/payment-intents`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json', 'Idempotency-Key': orderId },
        body: JSON.stringify({ orderId, amountCents, currency }),
        signal: AbortSignal.timeout(5000),
      });
      if (!res.ok) throw new Error(`provider answered ${res.status}`);
      return ((await res.json()) as { id: string }).id;
    } catch {
      throw paymentProviderUnavailable();
    }
  }
}
