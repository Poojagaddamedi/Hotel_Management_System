import React, { useState } from "react";
import logo from "../assets/logo.png";        // Update with your actual logo path
import hotelBg from "../assets/hotel-bg.png"; // Update with actual hotel image path
import hotelAPI from "../services/hotelAPI";

export default function LoginForm({ onLogin }) {
  const [userId, setUserId] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(e) {
    e.preventDefault();
    if (userId && password) {
      setLoading(true);
      try {
        // First try to authenticate and get user role from backend
        const loginResult = await hotelAPI.login({ username: userId, password });
        
        if (loginResult.success && loginResult.data) {
          // Get the user role from login response
          const userRole = loginResult.data.role || loginResult.data.userType || "staff";
          const normalizedRole = userRole.toLowerCase();
          
          // Store user info in localStorage for session management
          localStorage.setItem('user', JSON.stringify({
            userId: userId,
            username: userId,
            role: userRole,
            userType: loginResult.data.userType || userRole
          }));
          
          onLogin(normalizedRole);
        } else {
          // Fallback: Try to get user by username to determine role
          const userResult = await hotelAPI.getUserByUsername(userId);
          if (userResult.success && userResult.data) {
            const userRole = userResult.data.userRole || userResult.data.userType || "staff";
            const normalizedRole = userRole.toLowerCase();
            
            localStorage.setItem('user', JSON.stringify({
              userId: userId,
              username: userId,
              role: userRole,
              userType: userResult.data.userType || userRole
            }));
            
            onLogin(normalizedRole);
          } else {
            // Last fallback - basic role detection from username
            let detectedRole = "staff";
            const usernameLower = userId.toLowerCase();
            
            if (usernameLower.includes('admin')) {
              detectedRole = "admin";
            } else if (usernameLower.includes('manager')) {
              detectedRole = "manager";
            } else if (usernameLower.includes('reception') || usernameLower.includes('recept')) {
              detectedRole = "receptionist";
            } else if (usernameLower.includes('account')) {
              detectedRole = "accountant";
            } else if (usernameLower.includes('housekeep')) {
              detectedRole = "housekeeping";
            }
            
            localStorage.setItem('user', JSON.stringify({
              userId: userId,
              username: userId,
              role: detectedRole.toUpperCase(),
              userType: detectedRole.charAt(0).toUpperCase() + detectedRole.slice(1)
            }));
            
            onLogin(detectedRole);
          }
        }
      } catch (error) {
        console.error('Login error:', error);
        alert("Login failed. Please check your credentials.");
      } finally {
        setLoading(false);
      }
    } else {
      alert("Please enter your User ID and Password");
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
                background: "#fafafa"
              }}
              required
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
                background: "#fafafa"
              }}
              required
            />
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
