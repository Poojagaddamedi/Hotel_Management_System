import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import logo from "../assets/logo.png";        // Update with your actual logo path
import hotelBg from "../assets/hotel-bg.png"; // Update with actual hotel image path

export default function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (userId && password) {
      try {
        const result = login(userId, password);
        
        if (result.success) {
          // Role is automatically determined by the login function based on username/password
          // No need for manual role selection - it comes from user database
          navigate('/dashboard');
        } else {
          setError(result.error);
        }
      } catch (err) {
        setError('Login failed. Please try again.');
      } finally {
        setLoading(false);
      }
    } else {
      setError("Please enter your User ID and Password");
      setLoading(false);
    }
  }

  return (
    <div
      style={{
        height: "100vh",
        width: "100vw",
        display: "flex",
        background: "#fafafa",
        minHeight: "100vh"
      }}
    >
      {/* Left: Login card */}
      <div
        style={{
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "flex-end",
        }}
      >
        <form
          onSubmit={handleSubmit}
          style={{
            width: "340px",
            background: "#fff",
            borderRadius: "17px",
            boxShadow: "0 2px 12px rgba(40,40,70,0.11)",
            padding: "32px 28px 28px 28px",
            marginRight: "58px",
            display: "flex",
            flexDirection: "column",
            alignItems: "center"
          }}
        >
          <img src={logo} alt="Logo" style={{ width: 48, marginBottom: 16 }} />
          <h1
            style={{
              fontWeight: 800,
              fontSize: 23,
              margin: "0 0 7px 0",
              textAlign: "center"
            }}
          >
            Hotel Management System
          </h1>
          <div
            style={{
              color: "#555",
              marginBottom: 20,
              fontSize: 15,
              textAlign: "center"
            }}
          >
            Welcome back. Please login to your account.
          </div>

          {/* Error message */}
          {error && (
            <div style={{
              color: "#d32f2f",
              backgroundColor: "#ffebee",
              padding: "8px 12px",
              borderRadius: "6px",
              fontSize: "14px",
              marginBottom: "15px",
              width: "100%",
              textAlign: "center"
            }}>
              {error}
            </div>
          )}

          <div style={{ alignSelf: "stretch", marginBottom: 13 }}>
            <label
              style={{ display: "block", fontSize: 13, marginBottom: 4, fontWeight: 500 }}
            >
              User ID
            </label>
            <input
              type="text"
              value={userId}
              onChange={e => setUserId(e.target.value)}
              style={{
                width: "100%",
                padding: "10px",
                fontSize: 14,
                borderRadius: 9,
                border: "1px solid #e2e2eb",
                outline: "none",
                background: "#fafafa",
                boxSizing: "border-box"
              }}
              required
              disabled={loading}
            />
          </div>
          <div style={{ alignSelf: "stretch", marginBottom: 18 }}>
            <label
              style={{ display: "block", fontSize: 13, marginBottom: 4, fontWeight: 500 }}
            >
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={e => setPassword(e.target.value)}
              style={{
                width: "100%",
                padding: "10px",
                fontSize: 14,
                borderRadius: 9,
                border: "1px solid #e2e2eb",
                outline: "none",
                background: "#fafafa",
                boxSizing: "border-box"
              }}
              required
              disabled={loading}
            />
          </div>

          {/* Demo credentials */}
          <div style={{
            alignSelf: "stretch",
            marginBottom: 15,
            fontSize: 12,
            color: "#666",
            background: "#f8f9fa",
            padding: "10px",
            borderRadius: "6px",
            border: "1px solid #e9ecef"
          }}>
            <strong>User Accounts (Role auto-assigned):</strong><br/>
            Admin: admin/admin<br/>
            Manager: manager/manager<br/>
            Receptionist: receptionist/receptionist<br/>
            Accountant: accountant/accountant<br/>
            Housekeeping: housekeeping/housekeeping
          </div>

          <button
            type="submit"
            disabled={loading}
            style={{
              width: "100%",
              background: loading ? "#ccc" : "#635bd2",
              color: "#fff",
              border: "none",
              borderRadius: 9,
              padding: "12px 0",
              fontWeight: 700,
              fontSize: 16,
              marginTop: 2,
              cursor: loading ? "not-allowed" : "pointer",
              boxShadow: "0 1px 6px rgba(99,91,210,0.07)"
            }}
          >
            {loading ? "Logging in..." : "Login"}
          </button>
        </form>
      </div>
      <div
        style={{
          flex: 1,
          backgroundImage: `url(${hotelBg})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          filter: "brightness(97%) blur(2px)",
          opacity: 0.31,
          minHeight: "100vh"
        }}
      />
    </div>
  );
}
