import createApi from '../api.service';

class PropertiesServiceClient {
  private api: any; 

  constructor(baseUrl = process.env.REACT_APP_API_PREFIX + "/properties") {
    this.api = createApi(baseUrl);
  }

  
  async getAllProperties(query: string) {
    return this.api.get(`/${query}`);
  }
}

const propertiesServiceClient = new PropertiesServiceClient();

export default propertiesServiceClient;
