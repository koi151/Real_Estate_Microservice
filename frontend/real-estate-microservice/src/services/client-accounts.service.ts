import createApi from './api.service';

class AccountService {
  private api: any; 

  constructor(baseUrl = process.env.REACT_APP_API_PREFIX + "/accounts") {
    this.api = createApi(baseUrl);
  }

  async getAccountDetails(id: string) {
    return this.api.get(`/${id}`);
  }
  
}

const accountsService = new AccountService();

export default accountsService;
