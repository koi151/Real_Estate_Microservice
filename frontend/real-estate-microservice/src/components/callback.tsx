import React, { useEffect } from 'react';
import axios from 'axios';
import { message } from 'antd';
import { useNavigate } from 'react-router-dom';

const Callback: React.FC = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const handleCallback = async () => {
      try {
        // Lấy Authorization Code và state từ URL callback
        const params = new URLSearchParams(window.location.search);
        const code = params.get('code');
        const state = params.get('state'); // Lấy state từ URL

        if (!code || !state) {
          throw new Error('Authorization code or state is missing.');
        }

        // Lấy code_verifier từ sessionStorage
        const codeVerifier = sessionStorage.getItem('pkce_code_verifier');
        if (!codeVerifier) {
          throw new Error('Code verifier is missing or expired.');
        }


        await axios.post(
          `${process.env.REACT_APP_API_PREFIX}/accounts/auth/token`,
          new URLSearchParams({
            grant_type: 'authorization_code',
            state,
            code,
            redirect_uri: process.env.REACT_APP_KEYCLOAK_REDIRECT_URI!,
            client_id: process.env.REACT_APP_KEYCLOAK_CLIENT_ID!,
            code_verifier: codeVerifier,
          }),
          {
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          }
        );
        
        message.success('Login successful!', 2);
        navigate('/admin/properties');

      } catch (error) {
        console.error('Error during callback:', error);
        message.error('Failed to complete login. Please try again.', 3);
      }
    };

    handleCallback();
  }, [navigate]);

  return <div>Processing login...</div>;
};

export default Callback;
