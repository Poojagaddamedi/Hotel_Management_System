import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';

export default function UserManagement() {
  const { users, createUser, updateUser, deleteUser, user } = useAuth();
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    name: '',
    role: 'receptionist'
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  // Only admin can access this component
  if (user?.role !== 'admin') {
    return (
      <div style={{ padding: 20, textAlign: 'center' }}>
        <h2>Access Denied</h2>
        <p>Only administrators can access user management.</p>
      </div>
    );
  }

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!formData.username || !formData.password || !formData.name) {
      setError('Please fill in all fields');
      return;
    }

    let result;
    if (editingUser) {
      result = updateUser(editingUser, formData);
    } else {
      result = createUser(formData);
    }

    if (result.success) {
      setSuccess(editingUser ? 'User updated successfully' : 'User created successfully');
      setFormData({ username: '', password: '', name: '', role: 'receptionist' });
      setShowCreateForm(false);
      setEditingUser(null);
    } else {
      setError(result.error);
    }
  };

  const handleEdit = (userToEdit) => {
    setEditingUser(userToEdit.username);
    setFormData({
      username: userToEdit.username,
      password: userToEdit.password,
      name: userToEdit.name,
      role: userToEdit.role
    });
    setShowCreateForm(true);
  };

  const handleDelete = (username) => {
    if (window.confirm(`Are you sure you want to delete user "${username}"?`)) {
      const result = deleteUser(username);
      if (result.success) {
        setSuccess('User deleted successfully');
      } else {
        setError(result.error);
      }
    }
  };

  const cancelForm = () => {
    setShowCreateForm(false);
    setEditingUser(null);
    setFormData({ username: '', password: '', name: '', role: 'receptionist' });
    setError('');
  };

  const roleColors = {
    admin: '#dc3545',
    manager: '#fd7e14',
    accountant: '#20c997',
    receptionist: '#007bff',
    housekeeping: '#6f42c1'
  };

  const rolePermissions = {
    admin: 'Full system access, user management',
    manager: 'All operations, reports, staff oversight',
    accountant: 'Payments, billing, financial reports',
    receptionist: 'Check-ins, reservations, basic billing',
    housekeeping: 'Room status, cleaning tasks'
  };

  return (
    <div style={{ padding: 20 }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: 30 }}>
        <div>
          <h2 style={{ margin: 0, fontSize: '28px', fontWeight: '700' }}>User Management</h2>
          <p style={{ color: '#666', margin: '8px 0 0 0' }}>Manage system users and their roles</p>
        </div>
        <button
          onClick={() => setShowCreateForm(true)}
          style={{
            padding: '12px 24px',
            backgroundColor: '#28a745',
            color: 'white',
            border: 'none',
            borderRadius: '8px',
            fontSize: '16px',
            fontWeight: '600',
            cursor: 'pointer'
          }}
        >
          + Add New User
        </button>
      </div>

      {/* Success/Error Messages */}
      {success && (
        <div style={{
          backgroundColor: '#d4edda',
          color: '#155724',
          padding: '12px 16px',
          borderRadius: '8px',
          marginBottom: '20px',
          border: '1px solid #c3e6cb'
        }}>
          {success}
        </div>
      )}

      {error && (
        <div style={{
          backgroundColor: '#f8d7da',
          color: '#721c24',
          padding: '12px 16px',
          borderRadius: '8px',
          marginBottom: '20px',
          border: '1px solid #f5c6cb'
        }}>
          {error}
        </div>
      )}

      {/* Create/Edit User Form */}
      {showCreateForm && (
        <div style={{
          backgroundColor: 'white',
          border: '1px solid #ddd',
          borderRadius: '12px',
          padding: '24px',
          marginBottom: '30px',
          boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
        }}>
          <h3 style={{ marginBottom: '20px', fontSize: '20px' }}>
            {editingUser ? 'Edit User' : 'Create New User'}
          </h3>
          
          <form onSubmit={handleSubmit}>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginBottom: '20px' }}>
              <div>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '600' }}>
                  Username
                </label>
                <input
                  type="text"
                  name="username"
                  value={formData.username}
                  onChange={handleInputChange}
                  disabled={editingUser} // Can't change username when editing
                  style={{
                    width: '100%',
                    padding: '12px',
                    border: '2px solid #e0e0e0',
                    borderRadius: '8px',
                    fontSize: '16px',
                    backgroundColor: editingUser ? '#f8f9fa' : 'white',
                    boxSizing: 'border-box'
                  }}
                  placeholder="Enter username"
                />
              </div>
              
              <div>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '600' }}>
                  Password
                </label>
                <input
                  type="password"
                  name="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  style={{
                    width: '100%',
                    padding: '12px',
                    border: '2px solid #e0e0e0',
                    borderRadius: '8px',
                    fontSize: '16px',
                    boxSizing: 'border-box'
                  }}
                  placeholder="Enter password"
                />
              </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginBottom: '20px' }}>
              <div>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '600' }}>
                  Full Name
                </label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  style={{
                    width: '100%',
                    padding: '12px',
                    border: '2px solid #e0e0e0',
                    borderRadius: '8px',
                    fontSize: '16px',
                    boxSizing: 'border-box'
                  }}
                  placeholder="Enter full name"
                />
              </div>
              
              <div>
                <label style={{ display: 'block', marginBottom: '8px', fontWeight: '600' }}>
                  Role
                </label>
                <select
                  name="role"
                  value={formData.role}
                  onChange={handleInputChange}
                  style={{
                    width: '100%',
                    padding: '12px',
                    border: '2px solid #e0e0e0',
                    borderRadius: '8px',
                    fontSize: '16px',
                    boxSizing: 'border-box'
                  }}
                >
                  <option value="receptionist">Receptionist</option>
                  <option value="housekeeping">Housekeeping</option>
                  <option value="accountant">Accountant</option>
                  <option value="manager">Manager</option>
                  <option value="admin">Admin</option>
                </select>
              </div>
            </div>

            <div style={{
              backgroundColor: '#f8f9fa',
              padding: '12px',
              borderRadius: '8px',
              marginBottom: '20px',
              fontSize: '14px',
              color: '#666'
            }}>
              <strong>Role Permissions:</strong> {rolePermissions[formData.role]}
            </div>

            <div style={{ display: 'flex', gap: '12px' }}>
              <button
                type="submit"
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
                {editingUser ? 'Update User' : 'Create User'}
              </button>
              <button
                type="button"
                onClick={cancelForm}
                style={{
                  padding: '12px 24px',
                  backgroundColor: '#6c757d',
                  color: 'white',
                  border: 'none',
                  borderRadius: '8px',
                  fontSize: '16px',
                  fontWeight: '600',
                  cursor: 'pointer'
                }}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      )}

      {/* Users List */}
      <div style={{
        backgroundColor: 'white',
        border: '1px solid #ddd',
        borderRadius: '12px',
        overflow: 'hidden',
        boxShadow: '0 2px 8px rgba(0,0,0,0.1)'
      }}>
        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr style={{ backgroundColor: '#f8f9fa' }}>
              <th style={{ padding: '16px', textAlign: 'left', fontWeight: '600', borderBottom: '1px solid #ddd' }}>
                User
              </th>
              <th style={{ padding: '16px', textAlign: 'left', fontWeight: '600', borderBottom: '1px solid #ddd' }}>
                Role
              </th>
              <th style={{ padding: '16px', textAlign: 'left', fontWeight: '600', borderBottom: '1px solid #ddd' }}>
                Permissions
              </th>
              <th style={{ padding: '16px', textAlign: 'center', fontWeight: '600', borderBottom: '1px solid #ddd' }}>
                Actions
              </th>
            </tr>
          </thead>
          <tbody>
            {users.map((userItem) => (
              <tr key={userItem.username} style={{ borderBottom: '1px solid #eee' }}>
                <td style={{ padding: '16px' }}>
                  <div>
                    <div style={{ fontWeight: '600', fontSize: '16px' }}>{userItem.name}</div>
                    <div style={{ color: '#666', fontSize: '14px' }}>@{userItem.username}</div>
                  </div>
                </td>
                <td style={{ padding: '16px' }}>
                  <span style={{
                    padding: '6px 12px',
                    backgroundColor: roleColors[userItem.role],
                    color: 'white',
                    borderRadius: '20px',
                    fontSize: '14px',
                    fontWeight: '600'
                  }}>
                    {userItem.role.charAt(0).toUpperCase() + userItem.role.slice(1)}
                  </span>
                </td>
                <td style={{ padding: '16px', fontSize: '14px', color: '#666', maxWidth: '300px' }}>
                  {rolePermissions[userItem.role]}
                </td>
                <td style={{ padding: '16px', textAlign: 'center' }}>
                  <div style={{ display: 'flex', justifyContent: 'center', gap: '8px' }}>
                    <button
                      onClick={() => handleEdit(userItem)}
                      style={{
                        padding: '8px 16px',
                        backgroundColor: '#ffc107',
                        color: '#000',
                        border: 'none',
                        borderRadius: '6px',
                        fontSize: '14px',
                        fontWeight: '600',
                        cursor: 'pointer'
                      }}
                    >
                      Edit
                    </button>
                    {userItem.username !== 'admin' && (
                      <button
                        onClick={() => handleDelete(userItem.username)}
                        style={{
                          padding: '8px 16px',
                          backgroundColor: '#dc3545',
                          color: 'white',
                          border: 'none',
                          borderRadius: '6px',
                          fontSize: '14px',
                          fontWeight: '600',
                          cursor: 'pointer'
                        }}
                      >
                        Delete
                      </button>
                    )}
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
