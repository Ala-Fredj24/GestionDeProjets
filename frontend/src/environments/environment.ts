const isBrowser = typeof globalThis !== 'undefined' && 'location' in globalThis;
const locationRef = isBrowser ? globalThis.location : undefined;
const isLocalAngularDevServer =
  locationRef?.hostname === 'localhost' && locationRef?.port === '4200';

export const environment = {
  production: false,
  apiBaseUrl: isLocalAngularDevServer ? 'http://localhost:8085/api' : '/api',
};
