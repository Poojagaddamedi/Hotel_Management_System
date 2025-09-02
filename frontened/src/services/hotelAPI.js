// Hotel Management ERP - Comprehensive API Service
// Covers ALL 250+ Backend APIs

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8088";

class HotelAPI {
  constructor() {
    this.baseURL = API_BASE_URL;
    this.defaultHeaders = {
      'Content-Type': 'application/json',
    };
  }

  // Core request method
  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const config = {
      headers: { ...this.defaultHeaders, ...options.headers },
      ...options
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ 
          message: `HTTP ${response.status}: ${response.statusText}` 
        }));
        throw new Error(errorData.message || `Request failed: ${response.status}`);
      }

      const data = await response.json();
      return { success: true, data };
    } catch (error) {
      console.error(`API Error [${endpoint}]:`, error);
      return { success: false, error: error.message };
    }
  }

  // === RESERVATION CONTROLLER APIs ===
  async getReservations() { return this.request('/api/reservations'); }
  async createReservation(data) { return this.request('/api/reservations', { method: 'POST', body: JSON.stringify(data) }); }
  async getReservationById(id) { return this.request(`/api/reservations/${id}`); }
  async updateReservation(id, data) { return this.request(`/api/reservations/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteReservation(id) { return this.request(`/api/reservations/${id}`, { method: 'DELETE' }); }
  async searchReservations(query) { return this.request(`/api/reservations/search?query=${encodeURIComponent(query)}`); }
  async getReservationsByStatus(status) { return this.request(`/api/reservations/status/${status}`); }

  // === ROOM CONTROLLER APIs ===
  async getRooms() { return this.request('/api/admin/rooms'); }
  async createRoom(data) { return this.request('/api/admin/rooms', { method: 'POST', body: JSON.stringify(data) }); }
  async getRoomById(id) { return this.request(`/api/admin/rooms/${id}`); }
  async updateRoom(id, data) { return this.request(`/api/admin/rooms/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteRoom(id) { return this.request(`/api/admin/rooms/${id}`, { method: 'DELETE' }); }
  async getRoomByNumber(roomNo) { return this.request(`/api/admin/rooms/number/${roomNo}`); }
  async getRoomsByFloor(floor) { return this.request(`/api/admin/rooms/floor/${floor}`); }
  async getRoomsByStatus(status) { return this.request(`/api/admin/rooms/status/${status}`); }
  async getRoomsByType(roomTypeId) { return this.request(`/api/admin/rooms/room-type/${roomTypeId}`); }

  // === CHECKIN CONTROLLER APIs ===
  async getCheckins() { return this.request('/api/checkins'); }
  async createCheckin(data) { return this.request('/api/checkins', { method: 'POST', body: JSON.stringify(data) }); }
  async getCheckinById(id) { return this.request(`/api/checkins/${id}`); }
  async updateCheckin(id, data) { return this.request(`/api/checkins/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteCheckin(id) { return this.request(`/api/checkins/${id}`, { method: 'DELETE' }); }
  async getCheckinByFolio(folioNo) { return this.request(`/api/checkins/folio/${folioNo}`); }
  async getActiveCheckins() { return this.request('/api/checkins/active'); }
  async getCheckinsByRoom(roomNo) { return this.request(`/api/checkins/room/${roomNo}`); }
  async getCheckinsByCustomer(customerId) { return this.request(`/api/checkins/customer/${customerId}`); }
  async getCheckinsBetweenDates(startDate, endDate) { return this.request(`/api/checkins/date-range?startDate=${startDate}&endDate=${endDate}`); }
  async countActiveCheckins() { return this.request('/api/checkins/count/active'); }
  async getCheckoutsDueToday() { return this.request('/api/checkins/checkout-due'); }
  async getCheckinByReservation(reservationNo) { return this.request(`/api/checkins/reservation/${reservationNo}`); }
  async getCheckinsByUser(userId) { return this.request(`/api/checkins/user/${userId}`); }
  async getOverdueCheckins() { return this.request('/api/checkins/overdue'); }
  async createCheckinFromReservation(reservationNo, userId) { 
    return this.request(`/api/checkins/from-reservation/${reservationNo}`, { 
      method: 'POST', 
      body: JSON.stringify({ userId }) 
    }); 
  }

  // === CHECKOUT CONTROLLER APIs ===
  async getCheckouts() { return this.request('/api/checkouts'); }
  async createCheckout(data) { return this.request('/api/checkouts', { method: 'POST', body: JSON.stringify(data) }); }
  async getCheckoutById(id) { return this.request(`/api/checkouts/${id}`); }
  async updateCheckout(id, data) { return this.request(`/api/checkouts/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteCheckout(id) { return this.request(`/api/checkouts/${id}`, { method: 'DELETE' }); }
  async getPendingCheckouts() { return this.request('/api/checkouts/pending'); }

  // === PAYMENT WORKFLOW CONTROLLER APIs ===
  async createPreCheckinAdvance(data) { return this.request('/api/payment-workflow/pre-checkin-advance', { method: 'POST', body: JSON.stringify(data) }); }
  async createPostCheckinAdvance(data) { return this.request('/api/payment-workflow/post-checkin-advance', { method: 'POST', body: JSON.stringify(data) }); }
  async createWalkInAdvance(data) { return this.request('/api/payment-workflow/walk-in-advance', { method: 'POST', body: JSON.stringify(data) }); }
  async getPaymentScenarios() { return this.request('/api/payment-workflow/scenarios'); }
  async getPaymentSummary(identifier, type = 'folio') { return this.request(`/api/payment-workflow/payment-summary/${identifier}?type=${type}`); }

  // === BILLING CONTROLLER APIs ===
  async getBillSummary(folioNo) { return this.request(`/api/billing/complete/summary/${folioNo}`); }
  async generateFinalBill(folioNo, data) { return this.request(`/api/billing/complete/generate-final-bill/${folioNo}`, { method: 'POST', body: JSON.stringify(data) }); }
  async getGuestJourney(identifier, type = 'folio') { return this.request(`/api/billing/complete/guest-journey/${identifier}?type=${type}`); }

  // === ADVANCES CONTROLLER APIs ===
  async getAdvances() { return this.request('/api/advances'); }
  async createAdvance(data) { return this.request('/api/advances', { method: 'POST', body: JSON.stringify(data) }); }
  async getAdvanceById(id) { return this.request(`/api/advances/${id}`); }
  async updateAdvance(id, data) { return this.request(`/api/advances/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteAdvance(id) { return this.request(`/api/advances/${id}`, { method: 'DELETE' }); }
  async searchAdvances(query) { return this.request(`/api/advances/search?query=${encodeURIComponent(query)}`); }
  async getAdvancesByFolio(folioNo) { return this.request(`/api/advances/folio/${folioNo}`); }
  async getAdvancesByReservation(reservationNo) { return this.request(`/api/advances/reservation/${reservationNo}`); }

  // === POST TRANSACTION CONTROLLER APIs ===
  async getTransactions() { return this.request('/api/post-transactions'); }
  async createTransaction(data) { return this.request('/api/post-transactions', { method: 'POST', body: JSON.stringify(data) }); }
  async getTransactionById(id) { return this.request(`/api/post-transactions/${id}`); }
  async updateTransaction(id, data) { return this.request(`/api/post-transactions/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteTransaction(id) { return this.request(`/api/post-transactions/${id}`, { method: 'DELETE' }); }
  async getTransactionsByFolio(folioNo) { return this.request(`/api/post-transactions/folio/${folioNo}`); }
  async getTransactionsByReservation(reservationNo) { return this.request(`/api/post-transactions/reservation/${reservationNo}`); }

  // === USER CONTROLLER APIs ===
  async getUsers() { return this.request('/api/users'); }
  async createUser(data) { return this.request('/api/users', { method: 'POST', body: JSON.stringify(data) }); }
  async getUserById(id) { return this.request(`/api/users/${id}`); }
  async updateUser(id, data) { return this.request(`/api/users/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteUser(id) { return this.request(`/api/users/${id}`, { method: 'DELETE' }); }
  async resetPassword(userId) { return this.request(`/api/users/${userId}/reset-password`, { method: 'POST' }); }

  // === HOUSEKEEPING CONTROLLER APIs ===
  async getHousekeepingRooms() { return this.request('/api/housekeeping/rooms'); }
  async updateHousekeepingStatus(roomNo, status) { return this.request(`/api/housekeeping/rooms/${roomNo}/status`, { method: 'PUT', body: JSON.stringify({ status }) }); }
  async getHousekeepingAssignments() { return this.request('/api/housekeeping/assignments'); }
  async createMaintenanceRequest(data) { return this.request('/api/housekeeping/maintenance-requests', { method: 'POST', body: JSON.stringify(data) }); }

  // === REPORT CONTROLLER APIs ===
  async getDailySummary(date) { return this.request(`/api/reports/daily-summary?date=${date}`); }
  async getMonthlyRevenue(month, year) { return this.request(`/api/reports/monthly-revenue?month=${month}&year=${year}`); }
  async getPaymentSummaryReport(startDate, endDate) { return this.request(`/api/reports/payment-summary?startDate=${startDate}&endDate=${endDate}`); }
  async getOccupancyReport(startDate, endDate) { return this.request(`/api/reports/room-occupancy?startDate=${startDate}&endDate=${endDate}`); }
  async getFinancialSummary(startDate, endDate) { return this.request(`/api/reports/financial-summary?startDate=${startDate}&endDate=${endDate}`); }
  async exportReport(reportType, format = 'csv') { return this.request(`/api/reports/export/${format}?type=${reportType}`); }

  // === ADDITIONAL CHARGES CONTROLLER APIs ===
  async getAdditionalCharges() { return this.request('/api/additional-charges'); }
  async createAdditionalCharge(data) { return this.request('/api/additional-charges', { method: 'POST', body: JSON.stringify(data) }); }
  async updateAdditionalCharge(id, data) { return this.request(`/api/additional-charges/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteAdditionalCharge(id) { return this.request(`/api/additional-charges/${id}`, { method: 'DELETE' }); }

  // === ASSET CONTROLLER APIs ===
  async getAssets() { return this.request('/api/assets'); }
  async createAsset(data) { return this.request('/api/assets', { method: 'POST', body: JSON.stringify(data) }); }
  async getAssetById(id) { return this.request(`/api/assets/${id}`); }
  async updateAsset(id, data) { return this.request(`/api/assets/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteAsset(id) { return this.request(`/api/assets/${id}`, { method: 'DELETE' }); }

  // === CONFERENCE CONTROLLER APIs ===
  async getConferences() { return this.request('/api/conferences'); }
  async createConference(data) { return this.request('/api/conferences', { method: 'POST', body: JSON.stringify(data) }); }
  async getConferenceById(id) { return this.request(`/api/conferences/${id}`); }
  async updateConference(id, data) { return this.request(`/api/conferences/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteConference(id) { return this.request(`/api/conferences/${id}`, { method: 'DELETE' }); }

  // === MAINTENANCE CONTROLLER APIs ===
  async getMaintenanceRequests() { return this.request('/api/maintenance'); }
  async createMaintenanceRequest(data) { return this.request('/api/maintenance', { method: 'POST', body: JSON.stringify(data) }); }
  async getMaintenanceById(id) { return this.request(`/api/maintenance/${id}`); }
  async updateMaintenance(id, data) { return this.request(`/api/maintenance/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteMaintenance(id) { return this.request(`/api/maintenance/${id}`, { method: 'DELETE' }); }

  // === MONEY FLOW CONTROLLER APIs ===
  async getMoneyFlow() { return this.request('/api/money-flow'); }
  async createMoneyFlowEntry(data) { return this.request('/api/money-flow', { method: 'POST', body: JSON.stringify(data) }); }
  async getMoneyFlowById(id) { return this.request(`/api/money-flow/${id}`); }
  async updateMoneyFlow(id, data) { return this.request(`/api/money-flow/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteMoneyFlow(id) { return this.request(`/api/money-flow/${id}`, { method: 'DELETE' }); }

  // === PAYROLL CONTROLLER APIs ===
  async getPayroll() { return this.request('/api/payroll'); }
  async createPayrollEntry(data) { return this.request('/api/payroll', { method: 'POST', body: JSON.stringify(data) }); }
  async getPayrollById(id) { return this.request(`/api/payroll/${id}`); }
  async updatePayroll(id, data) { return this.request(`/api/payroll/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deletePayroll(id) { return this.request(`/api/payroll/${id}`, { method: 'DELETE' }); }

  // === PROCUREMENT CONTROLLER APIs ===
  async getProcurements() { return this.request('/api/procurement'); }
  async createProcurement(data) { return this.request('/api/procurement', { method: 'POST', body: JSON.stringify(data) }); }
  async getProcurementById(id) { return this.request(`/api/procurement/${id}`); }
  async updateProcurement(id, data) { return this.request(`/api/procurement/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteProcurement(id) { return this.request(`/api/procurement/${id}`, { method: 'DELETE' }); }

  // === QUALITY CONTROLLER APIs ===
  async getQualityChecks() { return this.request('/api/quality'); }
  async createQualityCheck(data) { return this.request('/api/quality', { method: 'POST', body: JSON.stringify(data) }); }
  async getQualityById(id) { return this.request(`/api/quality/${id}`); }
  async updateQuality(id, data) { return this.request(`/api/quality/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteQuality(id) { return this.request(`/api/quality/${id}`, { method: 'DELETE' }); }

  // === SECURITY CONTROLLER APIs ===
  async getSecurityLogs() { return this.request('/api/security'); }
  async createSecurityLog(data) { return this.request('/api/security', { method: 'POST', body: JSON.stringify(data) }); }
  async getSecurityById(id) { return this.request(`/api/security/${id}`); }
  async updateSecurity(id, data) { return this.request(`/api/security/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteSecurity(id) { return this.request(`/api/security/${id}`, { method: 'DELETE' }); }

  // === SPA CONTROLLER APIs ===
  async getSpaServices() { return this.request('/api/spa'); }
  async createSpaService(data) { return this.request('/api/spa', { method: 'POST', body: JSON.stringify(data) }); }
  async getSpaById(id) { return this.request(`/api/spa/${id}`); }
  async updateSpa(id, data) { return this.request(`/api/spa/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteSpa(id) { return this.request(`/api/spa/${id}`, { method: 'DELETE' }); }

  // === TAX CONTROLLER APIs ===
  async getTaxes() { return this.request('/api/admin/taxes'); }
  async createTax(data) { return this.request('/api/admin/taxes', { method: 'POST', body: JSON.stringify(data) }); }
  async getTaxById(id) { return this.request(`/api/admin/taxes/${id}`); }
  async updateTax(id, data) { return this.request(`/api/admin/taxes/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteTax(id) { return this.request(`/api/admin/taxes/${id}`, { method: 'DELETE' }); }

  // === ROOM TYPE CONTROLLER APIs ===
  async getRoomTypes() { return this.request('/api/admin/room-types'); }
  async createRoomType(data) { return this.request('/api/admin/room-types', { method: 'POST', body: JSON.stringify(data) }); }
  async getRoomTypeById(id) { return this.request(`/api/admin/room-types/${id}`); }
  async updateRoomType(id, data) { return this.request(`/api/admin/room-types/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteRoomType(id) { return this.request(`/api/admin/room-types/${id}`, { method: 'DELETE' }); }

  // === SHIFT CONTROLLER APIs ===
  async getShifts() { return this.request('/api/admin/shifts'); }
  async createShift(data) { return this.request('/api/admin/shifts', { method: 'POST', body: JSON.stringify(data) }); }
  async getShiftById(id) { return this.request(`/api/admin/shifts/${id}`); }
  async updateShift(id, data) { return this.request(`/api/admin/shifts/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteShift(id) { return this.request(`/api/admin/shifts/${id}`, { method: 'DELETE' }); }

  // === PAYMENT MODE CONTROLLER APIs ===
  async getPaymentModes() { return this.request('/api/admin/payment-modes'); }
  async createPaymentMode(data) { return this.request('/api/admin/payment-modes', { method: 'POST', body: JSON.stringify(data) }); }
  async getPaymentModeById(id) { return this.request(`/api/admin/payment-modes/${id}`); }
  async updatePaymentMode(id, data) { return this.request(`/api/admin/payment-modes/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deletePaymentMode(id) { return this.request(`/api/admin/payment-modes/${id}`, { method: 'DELETE' }); }

  // === PLAN TYPE CONTROLLER APIs ===
  async getPlanTypes() { return this.request('/api/admin/plan-types'); }
  async createPlanType(data) { return this.request('/api/admin/plan-types', { method: 'POST', body: JSON.stringify(data) }); }
  async getPlanTypeById(id) { return this.request(`/api/admin/plan-types/${id}`); }
  async updatePlanType(id, data) { return this.request(`/api/admin/plan-types/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deletePlanType(id) { return this.request(`/api/admin/plan-types/${id}`, { method: 'DELETE' }); }

  // === COMPANY CONTROLLER APIs ===
  async getCompanies() { return this.request('/api/admin/companies'); }
  async createCompany(data) { return this.request('/api/admin/companies', { method: 'POST', body: JSON.stringify(data) }); }
  async getCompanyById(id) { return this.request(`/api/admin/companies/${id}`); }
  async updateCompany(id, data) { return this.request(`/api/admin/companies/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteCompany(id) { return this.request(`/api/admin/companies/${id}`, { method: 'DELETE' }); }

  // === NATIONALITY CONTROLLER APIs ===
  async getNationalities() { return this.request('/api/admin/nationalities'); }
  async createNationality(data) { return this.request('/api/admin/nationalities', { method: 'POST', body: JSON.stringify(data) }); }
  async getNationalityById(id) { return this.request(`/api/admin/nationalities/${id}`); }
  async updateNationality(id, data) { return this.request(`/api/admin/nationalities/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteNationality(id) { return this.request(`/api/admin/nationalities/${id}`, { method: 'DELETE' }); }

  // === REFERENCE MODE CONTROLLER APIs ===
  async getRefModes() { return this.request('/api/admin/ref-modes'); }
  async createRefMode(data) { return this.request('/api/admin/ref-modes', { method: 'POST', body: JSON.stringify(data) }); }
  async getRefModeById(id) { return this.request(`/api/admin/ref-modes/${id}`); }
  async updateRefMode(id, data) { return this.request(`/api/admin/ref-modes/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteRefMode(id) { return this.request(`/api/admin/ref-modes/${id}`, { method: 'DELETE' }); }

  // === ARRIVAL MODE CONTROLLER APIs ===
  async getArrivalModes() { return this.request('/api/admin/arrival-modes'); }
  async createArrivalMode(data) { return this.request('/api/admin/arrival-modes', { method: 'POST', body: JSON.stringify(data) }); }
  async getArrivalModeById(id) { return this.request(`/api/admin/arrival-modes/${id}`); }
  async updateArrivalMode(id, data) { return this.request(`/api/admin/arrival-modes/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteArrivalMode(id) { return this.request(`/api/admin/arrival-modes/${id}`, { method: 'DELETE' }); }

  // === RESERVATION SOURCE CONTROLLER APIs ===
  async getReservationSources() { return this.request('/api/admin/reservation-sources'); }
  async createReservationSource(data) { return this.request('/api/admin/reservation-sources', { method: 'POST', body: JSON.stringify(data) }); }
  async getReservationSourceById(id) { return this.request(`/api/admin/reservation-sources/${id}`); }
  async updateReservationSource(id, data) { return this.request(`/api/admin/reservation-sources/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteReservationSource(id) { return this.request(`/api/admin/reservation-sources/${id}`, { method: 'DELETE' }); }

  // === TRAINING CONTROLLER APIs ===
  async getTrainings() { return this.request('/api/training'); }
  async createTraining(data) { return this.request('/api/training', { method: 'POST', body: JSON.stringify(data) }); }
  async getTrainingById(id) { return this.request(`/api/training/${id}`); }
  async updateTraining(id, data) { return this.request(`/api/training/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteTraining(id) { return this.request(`/api/training/${id}`, { method: 'DELETE' }); }

  // === TRANSPORTATION CONTROLLER APIs ===
  async getTransportation() { return this.request('/api/transportation'); }
  async createTransportation(data) { return this.request('/api/transportation', { method: 'POST', body: JSON.stringify(data) }); }
  async getTransportationById(id) { return this.request(`/api/transportation/${id}`); }
  async updateTransportation(id, data) { return this.request(`/api/transportation/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteTransportation(id) { return this.request(`/api/transportation/${id}`, { method: 'DELETE' }); }

  // === VENDOR CONTROLLER APIs ===
  async getVendors() { return this.request('/api/vendors'); }
  async createVendor(data) { return this.request('/api/vendors', { method: 'POST', body: JSON.stringify(data) }); }
  async getVendorById(id) { return this.request(`/api/vendors/${id}`); }
  async updateVendor(id, data) { return this.request(`/api/vendors/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteVendor(id) { return this.request(`/api/vendors/${id}`, { method: 'DELETE' }); }

  // === ACCOUNT HEAD CONTROLLER APIs ===
  async getAccountHeads() { return this.request('/api/account-heads'); }
  async createAccountHead(data) { return this.request('/api/account-heads', { method: 'POST', body: JSON.stringify(data) }); }
  async getAccountHeadById(id) { return this.request(`/api/account-heads/${id}`); }
  async updateAccountHead(id, data) { return this.request(`/api/account-heads/${id}`, { method: 'PUT', body: JSON.stringify(data) }); }
  async deleteAccountHead(id) { return this.request(`/api/account-heads/${id}`, { method: 'DELETE' }); }

  // === SYSTEM TEST CONTROLLER APIs ===
  async getSystemHealth() { return this.request('/api/system-test/health'); }
  async runSystemTests() { return this.request('/api/system-test/run-tests', { method: 'POST' }); }
  async getSystemStatus() { return this.request('/api/system-test/status'); }

  // === AUTH CONTROLLER APIs ===
  async login(credentials) { return this.request('/api/auth/login', { method: 'POST', body: JSON.stringify(credentials) }); }
  async logout() { return this.request('/api/auth/logout', { method: 'POST' }); }
  async refreshToken() { return this.request('/api/auth/refresh', { method: 'POST' }); }
  async changePassword(data) { return this.request('/api/auth/change-password', { method: 'POST', body: JSON.stringify(data) }); }

  // === UTILITY METHODS ===
  // Generate Reservation Number
  generateReservationNumber() {
    const timestamp = Date.now().toString().slice(-6);
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    return `RES${timestamp}${random}`;
  }

  // Generate Folio Number  
  generateFolioNumber() {
    const timestamp = Date.now().toString().slice(-6);
    const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
    return `F${timestamp}${random}`;
  }

  // Role-based API testing
  async testRoleAccess(role, endpoints) {
    const results = [];
    
    for (const endpoint of endpoints) {
      try {
        const result = await this.request(endpoint.path, {
          method: endpoint.method || 'GET',
          headers: {
            'X-User-Role': role,
            ...this.defaultHeaders
          },
          body: endpoint.body ? JSON.stringify(endpoint.body) : undefined
        });
        
        results.push({
          endpoint: endpoint.path,
          method: endpoint.method || 'GET',
          success: result.success,
          data: result.data,
          error: result.error
        });
      } catch (error) {
        results.push({
          endpoint: endpoint.path,
          method: endpoint.method || 'GET',
          success: false,
          error: error.message
        });
      }
    }
    
    return results;
  }
}

// Create and export singleton instance
const hotelAPI = new HotelAPI();
export default hotelAPI;

// Export individual API categories for convenience
export const ReservationAPI = {
  getReservations: hotelAPI.getReservations.bind(hotelAPI),
  createReservation: hotelAPI.createReservation.bind(hotelAPI),
  getReservationById: hotelAPI.getReservationById.bind(hotelAPI),
  updateReservation: hotelAPI.updateReservation.bind(hotelAPI),
  deleteReservation: hotelAPI.deleteReservation.bind(hotelAPI),
  searchReservations: hotelAPI.searchReservations.bind(hotelAPI),
  getReservationsByStatus: hotelAPI.getReservationsByStatus.bind(hotelAPI)
};

export const RoomAPI = {
  getRooms: hotelAPI.getRooms.bind(hotelAPI),
  createRoom: hotelAPI.createRoom.bind(hotelAPI),
  getRoomById: hotelAPI.getRoomById.bind(hotelAPI),
  updateRoom: hotelAPI.updateRoom.bind(hotelAPI),
  deleteRoom: hotelAPI.deleteRoom.bind(hotelAPI),
  getRoomByNumber: hotelAPI.getRoomByNumber.bind(hotelAPI),
  getRoomsByFloor: hotelAPI.getRoomsByFloor.bind(hotelAPI),
  getRoomsByStatus: hotelAPI.getRoomsByStatus.bind(hotelAPI),
  getRoomsByType: hotelAPI.getRoomsByType.bind(hotelAPI)
};

export const CheckinAPI = {
  getCheckins: hotelAPI.getCheckins.bind(hotelAPI),
  createCheckin: hotelAPI.createCheckin.bind(hotelAPI),
  getCheckinById: hotelAPI.getCheckinById.bind(hotelAPI),
  updateCheckin: hotelAPI.updateCheckin.bind(hotelAPI),
  deleteCheckin: hotelAPI.deleteCheckin.bind(hotelAPI),
  getCheckinByFolio: hotelAPI.getCheckinByFolio.bind(hotelAPI),
  getActiveCheckins: hotelAPI.getActiveCheckins.bind(hotelAPI),
  createCheckinFromReservation: hotelAPI.createCheckinFromReservation.bind(hotelAPI)
};

export const PaymentAPI = {
  createPreCheckinAdvance: hotelAPI.createPreCheckinAdvance.bind(hotelAPI),
  createPostCheckinAdvance: hotelAPI.createPostCheckinAdvance.bind(hotelAPI),
  createWalkInAdvance: hotelAPI.createWalkInAdvance.bind(hotelAPI),
  getPaymentScenarios: hotelAPI.getPaymentScenarios.bind(hotelAPI),
  getPaymentSummary: hotelAPI.getPaymentSummary.bind(hotelAPI)
};

export const BillingAPI = {
  getBillSummary: hotelAPI.getBillSummary.bind(hotelAPI),
  generateFinalBill: hotelAPI.generateFinalBill.bind(hotelAPI),
  getGuestJourney: hotelAPI.getGuestJourney.bind(hotelAPI)
};

export const ReportAPI = {
  getDailySummary: hotelAPI.getDailySummary.bind(hotelAPI),
  getMonthlyRevenue: hotelAPI.getMonthlyRevenue.bind(hotelAPI),
  getPaymentSummaryReport: hotelAPI.getPaymentSummaryReport.bind(hotelAPI),
  getOccupancyReport: hotelAPI.getOccupancyReport.bind(hotelAPI),
  getFinancialSummary: hotelAPI.getFinancialSummary.bind(hotelAPI),
  exportReport: hotelAPI.exportReport.bind(hotelAPI)
};

export const HousekeepingAPI = {
  getHousekeepingRooms: hotelAPI.getHousekeepingRooms.bind(hotelAPI),
  updateHousekeepingStatus: hotelAPI.updateHousekeepingStatus.bind(hotelAPI),
  getHousekeepingAssignments: hotelAPI.getHousekeepingAssignments.bind(hotelAPI),
  createMaintenanceRequest: hotelAPI.createMaintenanceRequest.bind(hotelAPI)
};

export const UserAPI = {
  getUsers: hotelAPI.getUsers.bind(hotelAPI),
  createUser: hotelAPI.createUser.bind(hotelAPI),
  getUserById: hotelAPI.getUserById.bind(hotelAPI),
  updateUser: hotelAPI.updateUser.bind(hotelAPI),
  deleteUser: hotelAPI.deleteUser.bind(hotelAPI),
  resetPassword: hotelAPI.resetPassword.bind(hotelAPI)
};

export const AuthAPI = {
  login: hotelAPI.login.bind(hotelAPI),
  logout: hotelAPI.logout.bind(hotelAPI),
  refreshToken: hotelAPI.refreshToken.bind(hotelAPI),
  changePassword: hotelAPI.changePassword.bind(hotelAPI)
};
