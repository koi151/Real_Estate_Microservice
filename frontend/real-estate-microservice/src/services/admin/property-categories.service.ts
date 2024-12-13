import createApi from '../api.service';

class PropertyCategoriesServiceAdmin {
  private api: any; 

  constructor(baseUrl = process.env.REACT_APP_API_PREFIX + "/properties/categories") {
    this.api = createApi(baseUrl);
  }

  async getPropertyCategories(query: string) {
    return this.api.get(`${query}`);
  }

  async createCategory(category?: FormData) {
    const config = {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    };
    return this.api.post('/create', category, config);
  }

  async getCategoryTree() {
    return this.api.get(`/tree`);
  };

  async changeCategoryStatus(id: string, status: string) {
    const payload = { status }; 
    const config = {
      headers: {
        'Content-Type': 'application/json', 
      },
    };
  
    return this.api.patch(`/${id}/status`, payload, config);
  }

}

const propertyCategoriesService = new PropertyCategoriesServiceAdmin();

export default propertyCategoriesService;
