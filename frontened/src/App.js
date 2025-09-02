import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Layout from './components/Layout';
import Login from './components/Login';
import Dashboard from './components/Dashboard';
import Reservations from './components/Reservations';
import CheckInForm from './components/CheckInForm';
import CheckOutForm from './components/CheckOutForm';
import BillingDashboard from './components/BillingDashboard';
import HousekeepingPanel from './components/HousekeepingPanel';
import ReportsPanel from './components/ReportsPanel';
import InventoryManagement from './components/InventoryManagement';
import AdminPanel from './components/AdminPanel';
import UserManagement from './components/UserManagement';
import ComprehensiveAdminPanel from './components/ComprehensiveAdminPanel';
import './App.css';

// Role-based route configuration
const roleRoutes = {
  admin: [
    { path: "dashboard", element: <Dashboard /> },
    { path: "reservations", element: <Reservations /> },
    { path: "check-in", element: <CheckInForm /> },
    { path: "check-out", element: <CheckOutForm /> },
    { path: "billing", element: <BillingDashboard /> },
    { path: "housekeeping", element: <HousekeepingPanel /> },
    { path: "reports", element: <ReportsPanel /> },
    { path: "inventory", element: <InventoryManagement /> },
    { path: "admin", element: <AdminPanel /> },
    { path: "comprehensive-admin", element: <ComprehensiveAdminPanel /> },
    { path: "users", element: <UserManagement /> }
  ],
  manager: [
    { path: "dashboard", element: <Dashboard /> },
    { path: "reservations", element: <Reservations /> },
    { path: "check-in", element: <CheckInForm /> },
    { path: "check-out", element: <CheckOutForm /> },
    { path: "billing", element: <BillingDashboard /> },
    { path: "housekeeping", element: <HousekeepingPanel /> },
    { path: "reports", element: <ReportsPanel /> },
    { path: "inventory", element: <InventoryManagement /> }
  ],
  accountant: [
    { path: "dashboard", element: <Dashboard /> },
    { path: "billing", element: <BillingDashboard /> },
    { path: "reports", element: <ReportsPanel /> }
  ],
  receptionist: [
    { path: "dashboard", element: <Dashboard /> },
    { path: "reservations", element: <Reservations /> },
    { path: "check-in", element: <CheckInForm /> },
    { path: "check-out", element: <CheckOutForm /> },
    { path: "billing", element: <BillingDashboard /> }
  ],
  housekeeping: [
    { path: "dashboard", element: <Dashboard /> },
    { path: "housekeeping", element: <HousekeepingPanel /> }
  ]
};

// Protected Route Component
function ProtectedRoute({ children, requiredRole }) {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user.role !== requiredRole && user.role !== 'admin') {
    return <Navigate to="/dashboard" replace />;
  }

  return children;
}

// Main App Component
function AppContent() {
  const { user, isAuthenticated } = useAuth();

  if (!isAuthenticated) {
    return (
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    );
  }

  const userRoutes = roleRoutes[user.role] || roleRoutes.receptionist;

  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/dashboard" replace />} />
        
        {/* Dynamic routes based on user role */}
        {userRoutes.map((route) => (
          <Route
            key={route.path}
            path={`/${route.path}`}
            element={route.element}
          />
        ))}

        {/* Role-specific protected routes */}
        <Route
          path="/admin/*"
          element={
            <ProtectedRoute requiredRole="admin">
              <AdminPanel />
            </ProtectedRoute>
          }
        />
        
        <Route
          path="/comprehensive-admin"
          element={
            <ProtectedRoute requiredRole="admin">
              <ComprehensiveAdminPanel />
            </ProtectedRoute>
          }
        />
        
        <Route
          path="/users"
          element={
            <ProtectedRoute requiredRole="admin">
              <UserManagement />
            </ProtectedRoute>
          }
        />

        {/* Fallback route */}
        <Route path="*" element={<Navigate to="/dashboard" replace />} />
      </Routes>
    </Layout>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="App">
          <AppContent />
        </div>
      </Router>
    </AuthProvider>
  );
}
