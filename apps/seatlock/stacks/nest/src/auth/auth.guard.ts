// Authentication and roles as one global guard: deny by default, opt out with @Public() (Chapters 19 and 21).
import { type CanActivate, type ExecutionContext, Inject, Injectable, SetMetadata, createParamDecorator } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import type { Request } from 'express';
import { createRemoteJWKSet, jwtVerify } from 'jose';
import { CONFIG, type Config } from '../config.js';
import { forbidden, unauthorized } from '../common/errors.js';

export interface Caller {
  sub: string;
  roles: string[];
}

const IS_PUBLIC = 'seatlock:public';
const ROLES = 'seatlock:roles';
export const Public = () => SetMetadata(IS_PUBLIC, true);
export const Roles = (...roles: string[]) => SetMetadata(ROLES, roles);
export const CurrentUser = createParamDecorator((_: unknown, ctx: ExecutionContext): Caller => ctx.switchToHttp().getRequest<Request & { caller: Caller }>().caller);

@Injectable()
export class AuthGuard implements CanActivate {
  // jose caches the provider's keys and refetches when a token names an unknown kid (rotation).
  private readonly jwks;

  constructor(
    private readonly reflector: Reflector,
    @Inject(CONFIG) private readonly config: Config,
  ) {
    this.jwks = createRemoteJWKSet(new URL(config.OIDC_JWKS_URL));
  }

  async canActivate(ctx: ExecutionContext): Promise<boolean> {
    const targets = [ctx.getHandler(), ctx.getClass()];
    if (this.reflector.getAllAndOverride<boolean>(IS_PUBLIC, targets)) return true;

    const req = ctx.switchToHttp().getRequest<Request & { caller?: Caller }>();
    const [scheme, token] = (req.headers.authorization ?? '').split(' ');
    if (scheme?.toLowerCase() !== 'bearer' || !token) throw unauthorized();
    try {
      const { payload } = await jwtVerify(token, this.jwks, {
        issuer: this.config.OIDC_ISSUER,
        audience: this.config.OIDC_AUDIENCE,
        algorithms: ['RS256'], // never "none", never HS256 with a public key
        requiredClaims: ['exp', 'sub'],
        clockTolerance: 30,
      });
      const roles = (payload as { realm_access?: { roles?: string[] } }).realm_access?.roles ?? [];
      req.caller = { sub: payload.sub!, roles };
    } catch {
      throw unauthorized();
    }

    const required = this.reflector.getAllAndOverride<string[] | undefined>(ROLES, targets);
    if (required?.length && !required.some((r) => req.caller!.roles.includes(r))) throw forbidden();
    return true;
  }
}
