export function timestamp(): string {
  return Date.now().toString();
}

export function randomEmail(prefix = "playwright.user"): string {
  return `${prefix}.${timestamp()}@test.com`;
}

export function randomCompany(prefix = "PW Startup"): string {
  return `${prefix} ${timestamp()}`;
}
