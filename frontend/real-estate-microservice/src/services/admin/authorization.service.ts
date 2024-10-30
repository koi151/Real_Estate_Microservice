import createApi from '../api.service';

class AdminAuthorizationService {
  private api: any;

  constructor(baseUrl = "http://localhost:9011/api/v1/accounts/auth") {
    this.api = createApi(baseUrl);
  }

  async submitLogin(data: any) {
    console.log("Data being sent:", data);

    console.log("Request Headers:", this.api.defaults.headers);

    try {
      const response = await this.api.post("/login", data);
      
      console.log("Response:", response);
      
      return response.data;
    } catch (error: any) {
      console.error("Login Error Res:", error.response)

      throw error;
    }
  }
}

const adminAuthorizationService = new AdminAuthorizationService();

export default adminAuthorizationService;
