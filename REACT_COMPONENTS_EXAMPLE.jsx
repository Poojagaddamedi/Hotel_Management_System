// Example React Components for Hotel Management ERP
// These show how to integrate with the working backend APIs

import React, { useState, useEffect } from 'react';
import { hotelAPI } from './FRONTEND_API_SERVICE_COMPLETE.js';

// Dashboard Component
export const Dashboard = () => {
  const [dashboardData, setDashboardData] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboard();
  }, []);

  const loadDashboard = async () => {
    try {
      const data = await hotelAPI.getAdminDashboard();
      setDashboardData(data);
    } catch (error) {
      console.error('Failed to load dashboard:', error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading dashboard...</div>;

  return (
    <div className="dashboard">
      <h1>Hotel Management Dashboard</h1>
      <div className="dashboard-stats">
        <div className="stat-card">
          <h3>System Status</h3>
          <p>{dashboardData?.status || 'OK'}</p>
        </div>
        <div className="stat-card">
          <h3>Current Time</h3>
          <p>{dashboardData?.currentTime}</p>
        </div>
      </div>
    </div>
  );
};

// Users Management Component
export const UsersManagement = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newUser, setNewUser] = useState({
    username: '',
    password: '',
    fullName: '',
    userRole: 'STAFF'
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const usersData = await hotelAPI.getUsers();
      setUsers(usersData);
    } catch (error) {
      console.error('Failed to load users:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (e) => {
    e.preventDefault();
    try {
      await hotelAPI.createUser(newUser);
      setNewUser({ username: '', password: '', fullName: '', userRole: 'STAFF' });
      loadUsers(); // Reload users list
      alert('User created successfully!');
    } catch (error) {
      console.error('Failed to create user:', error);
      alert('Failed to create user');
    }
  };

  const handleDeleteUser = async (userId) => {
    if (window.confirm('Are you sure you want to delete this user?')) {
      try {
        await hotelAPI.deleteUser(userId);
        loadUsers(); // Reload users list
        alert('User deleted successfully!');
      } catch (error) {
        console.error('Failed to delete user:', error);
        alert('Failed to delete user');
      }
    }
  };

  return (
    <div className="users-management">
      <h2>Users Management</h2>
      
      {/* Create User Form */}
      <form onSubmit={handleCreateUser} className="create-user-form">
        <h3>Create New User</h3>
        <div className="form-group">
          <input
            type="text"
            placeholder="Username"
            value={newUser.username}
            onChange={(e) => setNewUser({...newUser, username: e.target.value})}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={newUser.password}
            onChange={(e) => setNewUser({...newUser, password: e.target.value})}
            required
          />
          <input
            type="text"
            placeholder="Full Name"
            value={newUser.fullName}
            onChange={(e) => setNewUser({...newUser, fullName: e.target.value})}
            required
          />
          <select
            value={newUser.userRole}
            onChange={(e) => setNewUser({...newUser, userRole: e.target.value})}
          >
            <option value="STAFF">Staff</option>
            <option value="ADMIN">Admin</option>
            <option value="MANAGER">Manager</option>
            <option value="RECEPTIONIST">Receptionist</option>
          </select>
          <button type="submit">Create User</button>
        </div>
      </form>

      {/* Users List */}
      <div className="users-list">
        <h3>Current Users</h3>
        {loading ? (
          <p>Loading users...</p>
        ) : (
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Full Name</th>
                <th>Role</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map(user => (
                <tr key={user.userId}>
                  <td>{user.userId}</td>
                  <td>{user.username}</td>
                  <td>{user.fullName}</td>
                  <td>{user.userRole}</td>
                  <td>{user.isActive ? 'Active' : 'Inactive'}</td>
                  <td>
                    <button onClick={() => handleDeleteUser(user.userId)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

// Reservations Component
export const ReservationsManagement = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [newReservation, setNewReservation] = useState({
    guestName: '',
    fromDate: '',
    toDate: '',
    noOfRooms: 1,
    totalPax: 1,
    contactNo: '',
    emailId: '',
    rate: 0
  });

  useEffect(() => {
    loadReservations();
  }, []);

  const loadReservations = async () => {
    try {
      const reservationsData = await hotelAPI.getReservations();
      setReservations(reservationsData);
    } catch (error) {
      console.error('Failed to load reservations:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateReservation = async (e) => {
    e.preventDefault();
    try {
      await hotelAPI.createReservation(newReservation);
      setNewReservation({
        guestName: '', fromDate: '', toDate: '', noOfRooms: 1,
        totalPax: 1, contactNo: '', emailId: '', rate: 0
      });
      loadReservations();
      alert('Reservation created successfully!');
    } catch (error) {
      console.error('Failed to create reservation:', error);
      alert('Failed to create reservation');
    }
  };

  return (
    <div className="reservations-management">
      <h2>Reservations Management</h2>
      
      {/* Create Reservation Form */}
      <form onSubmit={handleCreateReservation} className="create-reservation-form">
        <h3>New Reservation</h3>
        <div className="form-grid">
          <input
            type="text"
            placeholder="Guest Name"
            value={newReservation.guestName}
            onChange={(e) => setNewReservation({...newReservation, guestName: e.target.value})}
            required
          />
          <input
            type="date"
            placeholder="Check-in Date"
            value={newReservation.fromDate}
            onChange={(e) => setNewReservation({...newReservation, fromDate: e.target.value})}
            required
          />
          <input
            type="date"
            placeholder="Check-out Date"
            value={newReservation.toDate}
            onChange={(e) => setNewReservation({...newReservation, toDate: e.target.value})}
            required
          />
          <input
            type="number"
            placeholder="Number of Rooms"
            value={newReservation.noOfRooms}
            onChange={(e) => setNewReservation({...newReservation, noOfRooms: parseInt(e.target.value)})}
            min="1"
            required
          />
          <input
            type="number"
            placeholder="Total Guests"
            value={newReservation.totalPax}
            onChange={(e) => setNewReservation({...newReservation, totalPax: parseInt(e.target.value)})}
            min="1"
            required
          />
          <input
            type="tel"
            placeholder="Contact Number"
            value={newReservation.contactNo}
            onChange={(e) => setNewReservation({...newReservation, contactNo: e.target.value})}
            required
          />
          <input
            type="email"
            placeholder="Email ID"
            value={newReservation.emailId}
            onChange={(e) => setNewReservation({...newReservation, emailId: e.target.value})}
            required
          />
          <input
            type="number"
            placeholder="Room Rate"
            value={newReservation.rate}
            onChange={(e) => setNewReservation({...newReservation, rate: parseFloat(e.target.value)})}
            min="0"
            step="0.01"
            required
          />
          <button type="submit" className="submit-btn">Create Reservation</button>
        </div>
      </form>

      {/* Reservations List */}
      <div className="reservations-list">
        <h3>Current Reservations</h3>
        {loading ? (
          <p>Loading reservations...</p>
        ) : (
          <div className="reservations-grid">
            {reservations.map(reservation => (
              <div key={reservation.id} className="reservation-card">
                <h4>{reservation.guestName}</h4>
                <p><strong>Reservation #:</strong> {reservation.reservationNo}</p>
                <p><strong>Dates:</strong> {reservation.fromDate} to {reservation.toDate}</p>
                <p><strong>Rooms:</strong> {reservation.noOfRooms}</p>
                <p><strong>Guests:</strong> {reservation.totalPax}</p>
                <p><strong>Contact:</strong> {reservation.contactNo}</p>
                <p><strong>Rate:</strong> ₹{reservation.rate}</p>
                <p><strong>Status:</strong> {reservation.status}</p>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

// Room Management Component
export const RoomManagement = () => {
  const [rooms, setRooms] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadRooms();
    loadRoomTypes();
  }, []);

  const loadRooms = async () => {
    try {
      const roomsData = await hotelAPI.getRooms();
      setRooms(roomsData);
    } catch (error) {
      console.error('Failed to load rooms:', error);
    }
  };

  const loadRoomTypes = async () => {
    try {
      const roomTypesData = await hotelAPI.getRoomTypes();
      setRoomTypes(roomTypesData);
    } catch (error) {
      console.error('Failed to load room types:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="room-management">
      <h2>Room Management</h2>
      
      <div className="room-overview">
        <div className="room-stats">
          <div className="stat-card">
            <h3>Total Rooms</h3>
            <p>{rooms.length}</p>
          </div>
          <div className="stat-card">
            <h3>Available Rooms</h3>
            <p>{rooms.filter(room => room.status === 'VR').length}</p>
          </div>
          <div className="stat-card">
            <h3>Occupied Rooms</h3>
            <p>{rooms.filter(room => room.status === 'OC').length}</p>
          </div>
        </div>
      </div>

      {loading ? (
        <p>Loading rooms...</p>
      ) : (
        <div className="rooms-grid">
          {rooms.map(room => (
            <div key={room.id} className={`room-card status-${room.status}`}>
              <h4>Room {room.roomNo}</h4>
              <p><strong>Floor:</strong> {room.floor}</p>
              <p><strong>Capacity:</strong> {room.noOfPersons} persons</p>
              <p><strong>Rate:</strong> ₹{room.rate}</p>
              <p><strong>Status:</strong> {room.status}</p>
              <p><strong>Type:</strong> {room.roomtypeId}</p>
            </div>
          ))}
        </div>
      )}

      <div className="room-types-section">
        <h3>Room Types</h3>
        <div className="room-types-list">
          {roomTypes.map(type => (
            <div key={type.roomtypeId} className="room-type-card">
              <h4>{type.roomtypeName}</h4>
              <p>{type.roomtypeDescription}</p>
              <p><strong>Rate:</strong> ₹{type.roomtypeRate}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

// Main App Component
export const App = () => {
  const [currentPage, setCurrentPage] = useState('dashboard');
  const [isConnected, setIsConnected] = useState(false);

  useEffect(() => {
    testConnection();
  }, []);

  const testConnection = async () => {
    const connected = await hotelAPI.testConnection();
    setIsConnected(connected);
  };

  if (!isConnected) {
    return (
      <div className="connection-error">
        <h1>Backend Connection Failed</h1>
        <p>Please ensure the backend server is running on http://localhost:8088</p>
        <button onClick={testConnection}>Retry Connection</button>
      </div>
    );
  }

  return (
    <div className="app">
      <nav className="sidebar">
        <h1>Hotel ERP</h1>
        <ul>
          <li onClick={() => setCurrentPage('dashboard')} 
              className={currentPage === 'dashboard' ? 'active' : ''}>
            Dashboard
          </li>
          <li onClick={() => setCurrentPage('users')} 
              className={currentPage === 'users' ? 'active' : ''}>
            Users
          </li>
          <li onClick={() => setCurrentPage('reservations')} 
              className={currentPage === 'reservations' ? 'active' : ''}>
            Reservations
          </li>
          <li onClick={() => setCurrentPage('rooms')} 
              className={currentPage === 'rooms' ? 'active' : ''}>
            Rooms
          </li>
        </ul>
      </nav>
      
      <main className="main-content">
        {currentPage === 'dashboard' && <Dashboard />}
        {currentPage === 'users' && <UsersManagement />}
        {currentPage === 'reservations' && <ReservationsManagement />}
        {currentPage === 'rooms' && <RoomManagement />}
      </main>
    </div>
  );
};

export default App;
