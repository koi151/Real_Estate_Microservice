import React, { useEffect } from 'react';
import axios from 'axios';
import { message } from 'antd';

const generateRandomString = (length: number) => {
  const array = new Uint8Array(length);
  window.crypto.getRandomValues(array);
  return Array.from(array, (byte) => ('0' + (byte & 0xff).toString(16)).slice(-2)).join('');
};

const base64UrlEncode = (str: string) => {
  return btoa(str)
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=+$/, '');
};

const sha256 = async (plain: string) => {
  const encoder = new TextEncoder();
  const data = encoder.encode(plain);
  const hash = await crypto.subtle.digest('SHA-256', data);
  return base64UrlEncode(String.fromCharCode(...new Uint8Array(hash)));
};

const KeycloakLogin: React.FC = () => {
  useEffect(() => {
    const handleLogin = async () => {
      try {
        // Generate PKCE parameters
        const codeVerifier = generateRandomString(32);
        const codeChallenge = await sha256(codeVerifier);
        console.log("Generated code_verifier:", codeVerifier);


        // Generate state
        const state = generateRandomString(32);

        sessionStorage.setItem('pkce_code_verifier', codeVerifier);
        sessionStorage.setItem('pkce_state', state);

        // request get url login keycloak
        const response = await axios.get(
          `${process.env.REACT_APP_API_PREFIX}/accounts/auth/login`,
          {
            params: {
              code_challenge: codeChallenge,
              code_challenge_method: 'S256',
              state,
            },
            withCredentials: true,
          }
        );

        if (response.status === 200 && response.data.loginUrl) {
          const { loginUrl } = response.data;
          window.location.href = loginUrl; // redirect to keycloak login page
        } else {
          throw new Error('Failed to get login URL.');
        }
      } catch (error) {
        console.error('Error during login:', error);
        message.error('Failed to initiate login. Please try again.', 3);
      }
    };

    handleLogin();
  }, []);

  return null;
};

export default KeycloakLogin;
