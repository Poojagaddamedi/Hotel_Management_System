import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import authService from '../services/authService';

// Component to protect routes based on authentication
export function ProtectedRoute({ children }) {
  const location = useLocation();

  if (!authService.isAuthenticated()) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return children;
}

// Component to protect routes based on user role/permissions
export function RoleProtectedRoute({ children, requiredPermission, requiredNavigation }) {
  const location = useLocation();

  if (!authService.isAuthenticated()) {
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // Check permission if specified
  if (requiredPermission && !authService.hasPermission(requiredPermission)) {
    return <AccessDenied />;
  }

  // Check navigation access if specified
  if (requiredNavigation && !authService.canAccessNavigation(requiredNavigation)) {
    return <AccessDenied />;
  }

  return children;
}

// Legacy component for backward compatibility
export default function ProtectedRouteOld({ children, allowedRoles }) {
  const currentUser = authService.getCurrentUser();

  if (!currentUser) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(currentUser.role)) {
    return <AccessDenied />;
  }

  return children;
}

// Access denied component
function AccessDenied() {
  const currentUser = authService.getCurrentUser();
  
  return (
    <div style={{
      minHeight: '70vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      padding: '40px',
      textAlign: 'center'
    }}>
      <div style={{
        fontSize: '72px',
        marginBottom: '20px'
      }}>
        🔒
      </div>
      <h1 style={{
        fontSize: '32px',
        fontWeight: '700',
        color: '#dc3545',
        marginBottom: '16px'
      }}>
        Access Denied
      </h1>
      <p style={{
        fontSize: '18px',
        color: '#666',
        marginBottom: '8px'
      }}>
        You don't have permission to access this page.
      </p>
      <p style={{
        fontSize: '16px',
        color: '#999',
        marginBottom: '32px'
      }}>
        Current role: <strong>{currentUser?.permissions?.name || 'Unknown'}</strong>
      </p>
      <button
        onClick={() => window.history.back()}
        style={{
          padding: '12px 24px',
          backgroundColor: '#007bff',
          color: 'white',
          border: 'none',
          borderRadius: '8px',
          fontSize: '16px',
          fontWeight: '600',
          cursor: 'pointer'
        }}
      >
        Go Back
      </button>
    </div>
  );
}

export { ProtectedRoute, RoleProtectedRoute };
