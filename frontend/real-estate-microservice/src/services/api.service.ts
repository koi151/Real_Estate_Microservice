import axios, { AxiosInstance } from 'axios';
import { store } from '../redux/stores';

interface CommonConfig {
  headers: {
    'Content-Type': string;
    Accept: string;
  };
}

const commonConfig: CommonConfig = {
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },
};

const createApi = (baseURL: string): AxiosInstance => {
  const api = axios.create({
    baseURL,
    ...commonConfig,
    withCredentials: true,
  });

  // Interceptor attach access token
  api.interceptors.request.use(
    (config) => {
      const state = store.getState();
      const accessToken = state.auth.accessToken;

      // Log token information
      console.log('Access Token:', accessToken);

      if (accessToken) {
        config.headers['Authorization'] = `Bearer ${accessToken}`;
      }

      return config;
    },
    (error) => {
      console.error('Request Error:', error);
      return Promise.reject(error);
    }
  );

  api.interceptors.response.use(
    (response) => {
      return response;
    },
    async (error) => {
      const originalRequest = error.config;

      if (error.response?.status === 401 && !originalRequest._retry) {
        originalRequest._retry = true;

        try {
          // Send req renew token
          const refreshResponse = await axios.post(
            `${process.env.REACT_APP_API_PREFIX}/accounts/auth/refresh`,
            {},
            {
              withCredentials: true, // Ensure sending with HttpOnly cookie
            }
          );

          const { accessToken } = refreshResponse.data;

          // Save new access token to Redux
          store.dispatch({
            type: 'auth/setAccessToken',
            payload: {
              accessToken,
              refreshToken: null,
              expiresIn: null,
            },
          });

          // Resend req with new token
          originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;
          return axios(originalRequest);
        } catch (refreshError) {
          console.error('Failed to refresh token', refreshError);
          return Promise.reject(refreshError);
        }
      }

      return Promise.reject(error);
    }
  );

  return api;
};

export default createApi;
