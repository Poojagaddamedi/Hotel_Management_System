// Role-Based Authentication Service
import hotelAPI from './hotelAPI';

const AUTH_STORAGE_KEY = 'hotel_auth';

export const USER_ROLES = {
  ADMIN: 'ADMIN',
  MANAGER: 'MANAGER', 
  ACCOUNTANT: 'ACCOUNTANT',
  RECEPTIONIST: 'RECEPTIONIST',
  HOUSEKEEPING: 'HOUSEKEEPING'
};

export const ROLE_PERMISSIONS = {
  [USER_ROLES.ADMIN]: {
    name: 'Admin',
    description: 'Full system access',
    permissions: ['*'], // All permissions
    dashboardCards: ['all-stats', 'revenue', 'reservations', 'rooms', 'staff', 'reports'],
    navigation: ['dashboard', 'reservations', 'check-in', 'check-out', 'billing', 'housekeeping', 'reports', 'admin', 'staff-management'],
    apis: ['*'] // All API endpoints
  },
  [USER_ROLES.MANAGER]: {
    name: 'Manager',
    description: 'View all data, approve check-outs, financial reports',
    permissions: ['view_all', 'approve_checkout', 'financial_reports', 'staff_management'],
    dashboardCards: ['occupancy', 'revenue', 'reservations', 'rooms', 'reports'],
    navigation: ['dashboard', 'reservations', 'check-in', 'check-out', 'billing', 'housekeeping', 'reports', 'staff-management'],
    apis: [
      'GET:/api/reports/*',
      'GET:/api/reservations/*',
      'GET:/api/checkins/*',
      'GET:/api/bills/*',
      'GET:/api/advances/*',
      'GET:/api/charges/*',
      'GET:/api/housekeeping/*',
      'GET:/api/staff',
      'PUT:/api/staff/*',
      'POST:/api/checkouts/approve/*'
    ]
  },
  [USER_ROLES.ACCOUNTANT]: {
    name: 'Accountant',
    description: 'Payments, bills, financial reports',
    permissions: ['payments', 'bills', 'financial_reports'],
    dashboardCards: ['revenue', 'payments', 'bills', 'reports'],
    navigation: ['dashboard', 'billing', 'reports'],
    apis: [
      'POST:/api/advances',
      'POST:/api/bill-settlements',
      'GET:/api/bills/*',
      'GET:/api/advances/*',
      'GET:/api/charges/*',
      'GET:/api/reports/financial/*',
      'GET:/api/reports/revenue/*',
      'GET:/api/reports/payments/*'
    ]
  },
  [USER_ROLES.RECEPTIONIST]: {
    name: 'Receptionist',
    description: 'Reservations, check-ins, payments, charges',
    permissions: ['reservations', 'checkins', 'checkouts', 'payments', 'charges'],
    dashboardCards: ['reservations', 'rooms', 'checkins', 'today-arrivals'],
    navigation: ['dashboard', 'reservations', 'check-in', 'check-out', 'billing'],
    apis: [
      'POST:/api/reservations',
      'GET:/api/reservations/*',
      'PUT:/api/reservations/*',
      'POST:/api/checkins',
      'GET:/api/checkins/*',
      'POST:/api/checkouts',
      'POST:/api/advances',
      'POST:/api/charges',
      'GET:/api/bills/*',
      'GET:/api/admin/rooms'
    ]
  },
  [USER_ROLES.HOUSEKEEPING]: {
    name: 'Housekeeping Staff',
    description: 'Room status management',
    permissions: ['housekeeping'],
    dashboardCards: ['rooms', 'housekeeping-tasks'],
    navigation: ['dashboard', 'housekeeping'],
    apis: [
      'POST:/api/housekeeping',
      'GET:/api/housekeeping/*',
      'PUT:/api/housekeeping/*',
      'GET:/api/admin/rooms',
      'PUT:/api/admin/rooms/*/status'
    ]
  }
};

class AuthService {
  constructor() {
    this.currentUser = this.loadAuthData();
  }

  // Load authentication data from localStorage
  loadAuthData() {
    try {
      const authData = localStorage.getItem(AUTH_STORAGE_KEY);
      return authData ? JSON.parse(authData) : null;
    } catch (error) {
      console.error('Error loading auth data:', error);
      return null;
    }
  }

  // Save authentication data to localStorage
  saveAuthData(authData) {
    try {
      localStorage.setItem(AUTH_STORAGE_KEY, JSON.stringify(authData));
      this.currentUser = authData;
    } catch (error) {
      console.error('Error saving auth data:', error);
    }
  }

  // Login with username and password
  async login(username, password) {
    try {
      // For demo purposes, we'll simulate login
      // In real implementation, this would call the backend authentication API
      const mockUsers = {
        'admin': { role: USER_ROLES.ADMIN, name: 'System Administrator' },
        'manager': { role: USER_ROLES.MANAGER, name: 'Hotel Manager' },
        'accountant': { role: USER_ROLES.ACCOUNTANT, name: 'Accountant' },
        'receptionist': { role: USER_ROLES.RECEPTIONIST, name: 'Receptionist' },
        'housekeeping': { role: USER_ROLES.HOUSEKEEPING, name: 'Housekeeping Staff' }
      };

      const user = mockUsers[username.toLowerCase()];
      if (user && password === 'password123') { // Demo password
        const authData = {
          username,
          role: user.role,
          name: user.name,
          permissions: ROLE_PERMISSIONS[user.role],
          loginTime: new Date().toISOString(),
          token: `mock_token_${Date.now()}` // Mock JWT token
        };

        this.saveAuthData(authData);
        return { success: true, user: authData };
      } else {
        return { success: false, error: 'Invalid username or password' };
      }
    } catch (error) {
      return { success: false, error: error.message };
    }
  }

  // Logout
  logout() {
    localStorage.removeItem(AUTH_STORAGE_KEY);
    this.currentUser = null;
  }

  // Check if user is authenticated
  isAuthenticated() {
    return this.currentUser !== null;
  }

  // Get current user
  getCurrentUser() {
    return this.currentUser;
  }

  // Check if user has specific permission
  hasPermission(permission) {
    if (!this.currentUser) return false;
    
    const userPermissions = this.currentUser.permissions?.permissions || [];
    
    // Admin has all permissions
    if (userPermissions.includes('*')) return true;
    
    // Check specific permission
    return userPermissions.includes(permission);
  }

  // Check if user can access specific API endpoint
  canAccessAPI(method, endpoint) {
    if (!this.currentUser) return false;
    
    const userAPIs = this.currentUser.permissions?.apis || [];
    
    // Admin has all API access
    if (userAPIs.includes('*')) return true;
    
    // Check specific API pattern
    const apiPattern = `${method}:${endpoint}`;
    
    return userAPIs.some(allowedAPI => {
      if (allowedAPI.endsWith('/*')) {
        const basePath = allowedAPI.replace('/*', '');
        return apiPattern.startsWith(basePath);
      }
      return allowedAPI === apiPattern;
    });
  }

  // Check if user can access navigation item
  canAccessNavigation(navItem) {
    if (!this.currentUser) return false;
    
    const userNavigation = this.currentUser.permissions?.navigation || [];
    return userNavigation.includes(navItem);
  }

  // Get dashboard cards for current user role
  getDashboardCards() {
    if (!this.currentUser) return [];
    return this.currentUser.permissions?.dashboardCards || [];
  }

  // Get user role display name
  getRoleDisplayName() {
    if (!this.currentUser) return '';
    return this.currentUser.permissions?.name || this.currentUser.role;
  }

  // Update user token (for token refresh)
  updateToken(newToken) {
    if (this.currentUser) {
      this.currentUser.token = newToken;
      this.saveAuthData(this.currentUser);
    }
  }

  // Get authorization header for API requests
  getAuthHeader() {
    if (this.currentUser?.token) {
      return { 'Authorization': `Bearer ${this.currentUser.token}` };
    }
    return {};
  }
}

// Create singleton instance
const authService = new AuthService();

export default authService;
