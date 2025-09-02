import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext();

// Default users created by admin
const defaultUsers = {
  'admin': { 
    username: 'admin', 
    password: 'admin', 
    role: 'admin', 
    name: 'Administrator',
    permissions: ['all']
  },
  'manager': { 
    username: 'manager', 
    password: 'manager', 
    role: 'manager', 
    name: 'Hotel Manager',
    permissions: ['view_all', 'approve_checkouts', 'reports', 'staff_management']
  },
  'accountant': { 
    username: 'accountant', 
    password: 'accountant', 
    role: 'accountant', 
    name: 'Accountant',
    permissions: ['payments', 'bills', 'financial_reports']
  },
  'receptionist': { 
    username: 'receptionist', 
    password: 'receptionist', 
    role: 'receptionist', 
    name: 'Receptionist',
    permissions: ['reservations', 'checkins', 'checkouts', 'payments', 'charges']
  },
  'housekeeping': { 
    username: 'housekeeping', 
    password: 'housekeeping', 
    role: 'housekeeping', 
    name: 'Housekeeping Staff',
    permissions: ['room_status', 'cleaning_tasks']
  }
};

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [users, setUsers] = useState(defaultUsers);

  // Check for existing session on app start
  useEffect(() => {
    const savedUser = localStorage.getItem('hotelUser');
    if (savedUser) {
      try {
        const userData = JSON.parse(savedUser);
        setUser(userData);
        setIsAuthenticated(true);
      } catch (error) {
        localStorage.removeItem('hotelUser');
      }
    }

    // Load users from localStorage if available
    const savedUsers = localStorage.getItem('hotelUsers');
    if (savedUsers) {
      try {
        setUsers(JSON.parse(savedUsers));
      } catch (error) {
        // Use default users if parsing fails
        localStorage.setItem('hotelUsers', JSON.stringify(defaultUsers));
      }
    } else {
      // Save default users to localStorage
      localStorage.setItem('hotelUsers', JSON.stringify(defaultUsers));
    }
  }, []);

  const login = (username, password) => {
    const foundUser = Object.values(users).find(
      u => u.username === username && u.password === password
    );

    if (foundUser) {
      const userSession = {
        id: username,
        username: foundUser.username,
        name: foundUser.name,
        role: foundUser.role,
        permissions: foundUser.permissions,
        loginTime: new Date().toISOString()
      };

      setUser(userSession);
      setIsAuthenticated(true);
      localStorage.setItem('hotelUser', JSON.stringify(userSession));
      return { success: true, user: userSession };
    }

    return { success: false, error: 'Invalid username or password' };
  };

  const logout = () => {
    setUser(null);
    setIsAuthenticated(false);
    localStorage.removeItem('hotelUser');
  };

  const createUser = (userData) => {
    if (user?.role !== 'admin') {
      return { success: false, error: 'Only admin can create users' };
    }

    if (users[userData.username]) {
      return { success: false, error: 'Username already exists' };
    }

    const newUsers = {
      ...users,
      [userData.username]: userData
    };

    setUsers(newUsers);
    localStorage.setItem('hotelUsers', JSON.stringify(newUsers));
    return { success: true };
  };

  const updateUser = (username, userData) => {
    if (user?.role !== 'admin') {
      return { success: false, error: 'Only admin can update users' };
    }

    if (!users[username]) {
      return { success: false, error: 'User not found' };
    }

    const newUsers = {
      ...users,
      [username]: { ...users[username], ...userData }
    };

    setUsers(newUsers);
    localStorage.setItem('hotelUsers', JSON.stringify(newUsers));
    return { success: true };
  };

  const deleteUser = (username) => {
    if (user?.role !== 'admin') {
      return { success: false, error: 'Only admin can delete users' };
    }

    if (username === 'admin') {
      return { success: false, error: 'Cannot delete admin user' };
    }

    const newUsers = { ...users };
    delete newUsers[username];

    setUsers(newUsers);
    localStorage.setItem('hotelUsers', JSON.stringify(newUsers));
    return { success: true };
  };

  const hasPermission = (permission) => {
    if (!user || !user.permissions) return false;
    return user.permissions.includes('all') || user.permissions.includes(permission);
  };

  const value = {
    user,
    isAuthenticated,
    users: Object.values(users),
    login,
    logout,
    createUser,
    updateUser,
    deleteUser,
    hasPermission
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
