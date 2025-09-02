// API Service Configuration for Frontend Integration
// Configure this in your frontend project to connect with working backend APIs

const API_CONFIG = {
  BASE_URL: 'http://localhost:8088',
  TIMEOUT: 10000,
  
  // Authentication
  AUTH: {
    USERNAME: 'admin',
    PASSWORD: 'admin',
    ENCODED: btoa('admin:admin') // Base64 encoded
  },
  
  // Headers for all requests
  DEFAULT_HEADERS: {
    'Content-Type': 'application/json',
    'Authorization': 'Basic ' + btoa('admin:admin'),
    'Accept': 'application/json'
  }
};

// API Service Class
class HotelAPIService {
  constructor() {
    this.baseURL = API_CONFIG.BASE_URL;
    this.headers = API_CONFIG.DEFAULT_HEADERS;
  }

  // Generic request method
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const config = {
      headers: this.headers,
      timeout: API_CONFIG.TIMEOUT,
      ...options
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error('API Request failed:', error);
      throw error;
    }
  }

  // Authentication APIs
  async login(credentials) {
    return this.request('/api/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials)
    });
  }

  // User Management APIs ✅ WORKING
  async getUsers() {
    return this.request('/api/users');
  }

  async createUser(userData) {
    return this.request('/api/users', {
      method: 'POST',
      body: JSON.stringify(userData)
    });
  }

  async updateUser(userId, userData) {
    return this.request(`/api/users/${userId}`, {
      method: 'PUT',
      body: JSON.stringify(userData)
    });
  }

  async deleteUser(userId) {
    return this.request(`/api/users/${userId}`, {
      method: 'DELETE'
    });
  }

  // Admin APIs ✅ WORKING
  async getAdminDashboard() {
    return this.request('/api/admin/dashboard');
  }

  async getRooms() {
    return this.request('/api/admin/rooms');
  }

  async createRoom(roomData) {
    return this.request('/api/admin/rooms', {
      method: 'POST',
      body: JSON.stringify(roomData)
    });
  }

  async getRoomTypes() {
    return this.request('/api/admin/room-types');
  }

  async createRoomType(roomTypeData) {
    return this.request('/api/admin/room-types', {
      method: 'POST',
      body: JSON.stringify(roomTypeData)
    });
  }

  async getTaxes() {
    return this.request('/api/admin/taxes');
  }

  async createTax(taxData) {
    return this.request('/api/admin/taxes', {
      method: 'POST',
      body: JSON.stringify(taxData)
    });
  }

  async getCompanies() {
    return this.request('/api/admin/companies');
  }

  async createCompany(companyData) {
    return this.request('/api/admin/companies', {
      method: 'POST',
      body: JSON.stringify(companyData)
    });
  }

  async getShifts() {
    return this.request('/api/admin/shifts');
  }

  async getAccountHeads() {
    return this.request('/api/admin/account-heads');
  }

  // Core Business APIs ✅ WORKING
  async getReservations() {
    return this.request('/api/reservations');
  }

  async createReservation(reservationData) {
    return this.request('/api/reservations', {
      method: 'POST',
      body: JSON.stringify(reservationData)
    });
  }

  async updateReservation(id, reservationData) {
    return this.request(`/api/reservations/${id}`, {
      method: 'PUT',
      body: JSON.stringify(reservationData)
    });
  }

  async deleteReservation(id) {
    return this.request(`/api/reservations/${id}`, {
      method: 'DELETE'
    });
  }

  async getCheckins() {
    return this.request('/api/checkins');
  }

  async createCheckin(checkinData) {
    return this.request('/api/checkins', {
      method: 'POST',
      body: JSON.stringify(checkinData)
    });
  }

  async getAdvances() {
    return this.request('/api/advances');
  }

  async createAdvance(advanceData) {
    return this.request('/api/advances', {
      method: 'POST',
      body: JSON.stringify(advanceData)
    });
  }

  async getAdvancesByFolio(folioNo) {
    return this.request(`/api/advances/folio/${folioNo}`);
  }

  async getAdvancesByReservation(reservationNo) {
    return this.request(`/api/advances/reservation/${reservationNo}`);
  }

  async getBills() {
    return this.request('/api/billing/bills');
  }

  async createBill(billData) {
    return this.request('/api/billing/bills', {
      method: 'POST',
      body: JSON.stringify(billData)
    });
  }

  async getBillsByFolio(folioNo) {
    return this.request(`/api/billing/bills/folio/${folioNo}`);
  }

  async getPostTransactions() {
    return this.request('/api/post-transactions');
  }

  async createPostTransaction(transactionData) {
    return this.request('/api/post-transactions', {
      method: 'POST',
      body: JSON.stringify(transactionData)
    });
  }

  async getCharges() {
    return this.request('/api/charges');
  }

  async createCharge(chargeData) {
    return this.request('/api/charges', {
      method: 'POST',
      body: JSON.stringify(chargeData)
    });
  }

  async getHousekeeping() {
    return this.request('/api/housekeeping');
  }

  async createHousekeepingTask(taskData) {
    return this.request('/api/housekeeping', {
      method: 'POST',
      body: JSON.stringify(taskData)
    });
  }

  // Utility methods
  async testConnection() {
    try {
      const result = await this.getAdminDashboard();
      console.log('✅ Backend connection successful:', result);
      return true;
    } catch (error) {
      console.error('❌ Backend connection failed:', error);
      return false;
    }
  }
}

// Create global instance
const hotelAPI = new HotelAPIService();

// Export for different module systems
if (typeof module !== 'undefined' && module.exports) {
  module.exports = { HotelAPIService, hotelAPI, API_CONFIG };
}

if (typeof window !== 'undefined') {
  window.HotelAPIService = HotelAPIService;
  window.hotelAPI = hotelAPI;
  window.API_CONFIG = API_CONFIG;
}

export { HotelAPIService, hotelAPI, API_CONFIG };
