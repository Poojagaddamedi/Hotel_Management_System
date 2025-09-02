// Hotel Management ERP API Service
// Copy this file to your frontend project

class HotelAPIService {
  constructor() {
    this.baseURL = 'http://localhost:8088';
    this.headers = {
      'Content-Type': 'application/json',
      'Authorization': 'Basic YWRtaW46YWRtaW4=' // admin:admin encoded
    };
  }

  // Helper method for all API calls
  async apiCall(endpoint, method = 'GET', data = null) {
    const config = {
      method,
      headers: this.headers
    };
    
    if (data) {
      config.body = JSON.stringify(data);
    }

    try {
      const response = await fetch(`${this.baseURL}${endpoint}`, config);
      
      if (!response.ok) {
        throw new Error(`API Error: ${response.status} ${response.statusText}`);
      }
      
      return await response.json();
    } catch (error) {
      console.error(`API call failed for ${endpoint}:`, error);
      throw error;
    }
  }

  // ===== AUTHENTICATION =====
  async login(username, password) {
    return await this.apiCall('/api/auth/login', 'POST', { username, password });
  }

  // ===== USER MANAGEMENT =====
  async getUsers() {
    return await this.apiCall('/api/users');
  }

  async createUser(userData) {
    return await this.apiCall('/api/users', 'POST', userData);
  }

  async updateUser(userId, userData) {
    return await this.apiCall(`/api/users/${userId}`, 'PUT', userData);
  }

  async deleteUser(userId) {
    return await this.apiCall(`/api/users/${userId}`, 'DELETE');
  }

  async getUserByUsername(username) {
    return await this.apiCall(`/api/users/username/${username}`);
  }

  async getUsersByRole(role) {
    return await this.apiCall(`/api/users/role/${role}`);
  }

  // ===== ADMIN DASHBOARD =====
  async getDashboard() {
    return await this.apiCall('/api/admin/dashboard');
  }

  async changeAuditDate(request) {
    return await this.apiCall('/api/admin/audit-date-change', 'POST', request);
  }

  // ===== ROOM MANAGEMENT =====
  async getRooms() {
    return await this.apiCall('/api/admin/rooms');
  }

  async createRoom(roomData) {
    return await this.apiCall('/api/admin/rooms', 'POST', roomData);
  }

  async updateRoom(roomId, roomData) {
    return await this.apiCall(`/api/admin/rooms/${roomId}`, 'PUT', roomData);
  }

  async deleteRoom(roomId) {
    return await this.apiCall(`/api/admin/rooms/${roomId}`, 'DELETE');
  }

  async getRoomByNumber(roomNo) {
    return await this.apiCall(`/api/admin/rooms/number/${roomNo}`);
  }

  async getRoomsByFloor(floor) {
    return await this.apiCall(`/api/admin/rooms/floor/${floor}`);
  }

  async getRoomsByStatus(status) {
    return await this.apiCall(`/api/admin/rooms/status/${status}`);
  }

  // ===== ROOM TYPES =====
  async getRoomTypes() {
    return await this.apiCall('/api/admin/room-types');
  }

  async createRoomType(roomTypeData) {
    return await this.apiCall('/api/admin/room-types', 'POST', roomTypeData);
  }

  async updateRoomType(roomTypeId, roomTypeData) {
    return await this.apiCall(`/api/admin/room-types/${roomTypeId}`, 'PUT', roomTypeData);
  }

  async deleteRoomType(roomTypeId) {
    return await this.apiCall(`/api/admin/room-types/${roomTypeId}`, 'DELETE');
  }

  // ===== TAX MANAGEMENT =====
  async getTaxes() {
    return await this.apiCall('/api/admin/taxes');
  }

  async createTax(taxData) {
    return await this.apiCall('/api/admin/taxes', 'POST', taxData);
  }

  async updateTax(taxId, taxData) {
    return await this.apiCall(`/api/admin/taxes/${taxId}`, 'PUT', taxData);
  }

  async deleteTax(taxId) {
    return await this.apiCall(`/api/admin/taxes/${taxId}`, 'DELETE');
  }

  // ===== COMPANY MANAGEMENT =====
  async getCompanies() {
    return await this.apiCall('/api/admin/companies');
  }

  async createCompany(companyData) {
    return await this.apiCall('/api/admin/companies', 'POST', companyData);
  }

  async updateCompany(companyId, companyData) {
    return await this.apiCall(`/api/admin/companies/${companyId}`, 'PUT', companyData);
  }

  async deleteCompany(companyId) {
    return await this.apiCall(`/api/admin/companies/${companyId}`, 'DELETE');
  }

  // ===== ACCOUNT HEADS =====
  async getAccountHeads() {
    return await this.apiCall('/api/admin/account-heads');
  }

  async createAccountHead(accountHeadData) {
    return await this.apiCall('/api/admin/account-heads', 'POST', accountHeadData);
  }

  async updateAccountHead(accountHeadId, accountHeadData) {
    return await this.apiCall(`/api/admin/account-heads/${accountHeadId}`, 'PUT', accountHeadData);
  }

  async deleteAccountHead(accountHeadId) {
    return await this.apiCall(`/api/admin/account-heads/${accountHeadId}`, 'DELETE');
  }

  // ===== SHIFT MANAGEMENT =====
  async getShifts() {
    return await this.apiCall('/api/admin/shifts');
  }

  async createShift(shiftData) {
    return await this.apiCall('/api/admin/shifts', 'POST', shiftData);
  }

  async updateShift(shiftId, shiftData) {
    return await this.apiCall(`/api/admin/shifts/${shiftId}`, 'PUT', shiftData);
  }

  async deleteShift(shiftId) {
    return await this.apiCall(`/api/admin/shifts/${shiftId}`, 'DELETE');
  }

  // ===== RESERVATIONS =====
  async getReservations() {
    return await this.apiCall('/api/reservations');
  }

  async createReservation(reservationData) {
    return await this.apiCall('/api/reservations', 'POST', reservationData);
  }

  async getReservationById(reservationId) {
    return await this.apiCall(`/api/reservations/${reservationId}`);
  }

  async updateReservation(reservationId, reservationData) {
    return await this.apiCall(`/api/reservations/${reservationId}`, 'PUT', reservationData);
  }

  async deleteReservation(reservationId) {
    return await this.apiCall(`/api/reservations/${reservationId}`, 'DELETE');
  }

  async searchReservations(query) {
    return await this.apiCall(`/api/reservations/search?query=${encodeURIComponent(query)}`);
  }

  async getReservationsByStatus(status) {
    return await this.apiCall(`/api/reservations/status/${status}`);
  }

  // ===== CHECK-INS =====
  async getCheckins() {
    return await this.apiCall('/api/checkins');
  }

  async createCheckin(checkinData) {
    return await this.apiCall('/api/checkins', 'POST', checkinData);
  }

  async getCheckinById(checkinId) {
    return await this.apiCall(`/api/checkins/${checkinId}`);
  }

  async updateCheckin(checkinId, checkinData) {
    return await this.apiCall(`/api/checkins/${checkinId}`, 'PUT', checkinData);
  }

  async deleteCheckin(checkinId) {
    return await this.apiCall(`/api/checkins/${checkinId}`, 'DELETE');
  }

  async getCheckinsByFolio(folioNo) {
    return await this.apiCall(`/api/checkins/folio/${folioNo}`);
  }

  async getActiveCheckins() {
    return await this.apiCall('/api/checkins/active');
  }

  // ===== ADVANCES/PAYMENTS =====
  async getAdvances() {
    return await this.apiCall('/api/advances');
  }

  async createAdvance(advanceData) {
    return await this.apiCall('/api/advances', 'POST', advanceData);
  }

  async getAdvanceById(advanceId) {
    return await this.apiCall(`/api/advances/${advanceId}`);
  }

  async updateAdvance(advanceId, advanceData) {
    return await this.apiCall(`/api/advances/${advanceId}`, 'PUT', advanceData);
  }

  async deleteAdvance(advanceId) {
    return await this.apiCall(`/api/advances/${advanceId}`, 'DELETE');
  }

  async getAdvancesByFolio(folioNo) {
    return await this.apiCall(`/api/advances/folio/${folioNo}`);
  }

  async getAdvancesByReservation(reservationNo) {
    return await this.apiCall(`/api/advances/reservation/${reservationNo}`);
  }

  async getAdvancesByPaymentMode(paymentMode) {
    return await this.apiCall(`/api/advances/payment-mode/${paymentMode}`);
  }

  async getTotalAdvancesByFolio(folioNo) {
    return await this.apiCall(`/api/advances/total/folio/${folioNo}`);
  }

  async getTotalAdvancesByReservation(reservationNo) {
    return await this.apiCall(`/api/advances/total/reservation/${reservationNo}`);
  }

  async searchAdvances(query) {
    return await this.apiCall(`/api/advances/search?query=${encodeURIComponent(query)}`);
  }

  // ===== BILLING =====
  async getBills() {
    return await this.apiCall('/api/billing/bills');
  }

  async createBill(billData) {
    return await this.apiCall('/api/billing/bills', 'POST', billData);
  }

  async updateBill(billId, billData) {
    return await this.apiCall(`/api/billing/bills/${billId}`, 'PUT', billData);
  }

  async deleteBill(billId) {
    return await this.apiCall(`/api/billing/bills/${billId}`, 'DELETE');
  }

  async getBillsByFolio(folioNo) {
    return await this.apiCall(`/api/billing/bills/folio/${folioNo}`);
  }

  async calculateBillTotal(folioNo) {
    return await this.apiCall(`/api/billing/calculate/${folioNo}`);
  }

  async getBillSummary(folioNo) {
    return await this.apiCall(`/api/billing/summary/${folioNo}`);
  }

  // ===== POST TRANSACTIONS =====
  async getPostTransactions() {
    return await this.apiCall('/api/post-transactions');
  }

  async createPostTransaction(transactionData) {
    return await this.apiCall('/api/post-transactions', 'POST', transactionData);
  }

  async getPostTransactionById(transactionId) {
    return await this.apiCall(`/api/post-transactions/${transactionId}`);
  }

  async updatePostTransaction(transactionId, transactionData) {
    return await this.apiCall(`/api/post-transactions/${transactionId}`, 'PUT', transactionData);
  }

  async deletePostTransaction(transactionId) {
    return await this.apiCall(`/api/post-transactions/${transactionId}`, 'DELETE');
  }

  async getPostTransactionsByFolio(folioNo) {
    return await this.apiCall(`/api/post-transactions/folio/${folioNo}`);
  }

  // ===== ADDITIONAL CHARGES =====
  async getCharges() {
    return await this.apiCall('/api/charges');
  }

  async createCharge(chargeData) {
    return await this.apiCall('/api/charges', 'POST', chargeData);
  }

  async getChargeById(chargeId) {
    return await this.apiCall(`/api/charges/${chargeId}`);
  }

  async updateCharge(chargeId, chargeData) {
    return await this.apiCall(`/api/charges/${chargeId}`, 'PUT', chargeData);
  }

  async deleteCharge(chargeId) {
    return await this.apiCall(`/api/charges/${chargeId}`, 'DELETE');
  }

  async getChargesByFolio(folioNo) {
    return await this.apiCall(`/api/charges/folio/${folioNo}`);
  }

  // ===== HOUSEKEEPING =====
  async getHousekeepingTasks() {
    return await this.apiCall('/api/housekeeping');
  }

  async createHousekeepingTask(taskData) {
    return await this.apiCall('/api/housekeeping', 'POST', taskData);
  }

  async getHousekeepingTaskById(taskId) {
    return await this.apiCall(`/api/housekeeping/${taskId}`);
  }

  async updateHousekeepingTask(taskId, taskData) {
    return await this.apiCall(`/api/housekeeping/${taskId}`, 'PUT', taskData);
  }

  async deleteHousekeepingTask(taskId) {
    return await this.apiCall(`/api/housekeeping/${taskId}`, 'DELETE');
  }

  async getHousekeepingSummary() {
    return await this.apiCall('/api/housekeeping/summary');
  }

  async searchHousekeepingTasks(query) {
    return await this.apiCall(`/api/housekeeping/search?query=${encodeURIComponent(query)}`);
  }

  // ===== UTILITY METHODS =====
  
  // Set custom authorization (if different user)
  setAuth(username, password) {
    const encoded = btoa(`${username}:${password}`);
    this.headers['Authorization'] = `Basic ${encoded}`;
  }

  // Get current auth header
  getAuthHeader() {
    return this.headers['Authorization'];
  }

  // Test server connection
  async testConnection() {
    try {
      await this.getDashboard();
      return { success: true, message: 'Server connected successfully' };
    } catch (error) {
      return { success: false, message: `Connection failed: ${error.message}` };
    }
  }
}

// Export for use in frontend
export default new HotelAPIService();

// Also export the class for creating multiple instances if needed
export { HotelAPIService };
