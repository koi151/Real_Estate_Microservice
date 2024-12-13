// import { ValidStatus } from '../../../../backend/commonTypes';
import createApi from '../api.service';

class AccountsServiceAdmin {
  private api: any; 

  constructor(baseUrl = process.env.REACT_APP_API_PREFIX + "/admin/accounts") {
    this.api = createApi(baseUrl);
  }

  async getAccounts() {
    return this.api.get(`/`);
  }


}

const adminAccountsService = new AccountsServiceAdmin();

export default adminAccountsService;
