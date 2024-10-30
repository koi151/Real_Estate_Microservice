import axios, { AxiosInstance } from 'axios';

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
  return axios.create({
    baseURL,
    ...commonConfig,
    withCredentials: true,
    // timeout: 120000
  });
};

export default createApi;
