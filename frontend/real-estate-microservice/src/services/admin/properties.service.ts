import createApi from '../api.service';

class PropertiesServiceAdmin {
  private api: any;

  constructor(baseUrl = process.env.REACT_APP_API_PREFIX + "/properties") {
    this.api = createApi(baseUrl);
  }


  async getAllProperties(query: string) {
    return this.api.get(`/${query}`);
  }


  async getSingleProperty(id: string) {
    return this.api.get(`/${id}`);
  }

  async changePropertyStatus(id: string, status: string) {
    const payload = { status }; 
    const config = {
      headers: {
        'Content-Type': 'application/json', 
      },
    };
  
    return this.api.patch(`/${id}/status`, payload, config);
  }

  async createProperty(property: FormData) {
    const config = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return this.api.post('/', property, config);
  }

  async updateProperty(property: FormData, id: number) {
    const config = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return this.api.patch(`/${id}`, property, config);
  }

  
}

const propertiesService = new PropertiesServiceAdmin();

export default propertiesService;
