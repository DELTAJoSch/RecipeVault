/**
 * This class represents an auth-request dto.
 */
export class AuthRequest {
  constructor(
    public email: string,
    public password: string
  ) {}
}
