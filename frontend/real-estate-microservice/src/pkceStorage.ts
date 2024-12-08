let codeVerifier: string | null = null;

export const setCodeVerifier = (verifier: string) => {
  codeVerifier = verifier;
};

export const getCodeVerifier = (): string | null => {
  return codeVerifier;
};
