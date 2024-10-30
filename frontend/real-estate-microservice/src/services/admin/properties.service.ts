import createApi from '../api.service';
// import { GetPropertiesOptions, ValidMultiChangeType, ValidStatus } from '../../../../backend/commonTypes';

class PropertiesServiceAdmin {
  private api: any; 

  constructor(baseUrl = "http://localhost:3000/api/v1/admin/properties") {
    this.api = createApi(baseUrl);
  }

  private getAuthHeaders() {
    const accessToken = localStorage.getItem('accessToken');
    
    if (!accessToken) {
      console.log("Access token not found");
      throw new Error('Unauthorized');
    }
    return {
      headers: {
        Authorization: `Bearer ${accessToken}`
      }
    };
  }

  private async handleRequest(request: Promise<any>): Promise<any> {
    try {
      const response = await request;
      return response.data;

    } catch (err: any) {      
      if (err.message === 'Access token not found') {
        throw new Error('Unauthorized')

      } else if (err.response && err.response.status === 401) {

        // Attempt to refresh the token
        try {
          const refreshToken = localStorage.getItem('refreshToken');
          if (!refreshToken) {
            throw new Error('Refresh token not found in localStorage');
          }
  
          const refreshResponse = await this.api.post('/refresh', { refreshToken });
          const newAccessToken = refreshResponse.data.accessToken;
          localStorage.setItem('accessToken', newAccessToken);
  
          return await this.handleRequest(request);

        } catch (refreshError) {
          throw new Error('Unauthorized');
        }

      } else {
        console.error('An error occurred:', err);
        throw new Error('An unexpected error occurred. Please try again later.');
      }
    }
  }

  async getAllProperties() {
    const request = this.api.get(`/`);
    return this.handleRequest(request);
  }

  
}

const propertiesService = new PropertiesServiceAdmin();

export default propertiesService;
