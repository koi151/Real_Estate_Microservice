import createApi from './api.service';

class AuthorizationService {
  private api: any;

  constructor(baseUrl = "http://localhost:9011/api/v1/accounts/auth") {
    this.api = createApi(baseUrl);
  }

  async submitLogin(data: any) {
    try {
      const response = await this.api.post("/login", data);
      
      return {
        data: response.data,
        status: response.status,
      };
    } catch (error: any) {
      console.error("Login Error Res:", error.response)

      throw error;
    }
  }
}

const authorizationService = new AuthorizationService();

export default authorizationService;
