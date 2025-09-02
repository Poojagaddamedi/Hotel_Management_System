import React from "react";
import logo from "../assets/logo.png";
import avatar from "../assets/avatar.jpeg";
import { Link, useLocation, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext";

// All possible menu items with role access control
const menus = {
  dashboard: { name: "Dashboard", route: "/dashboard", icon: "🏨", roles: ['admin', 'manager', 'receptionist', 'accountant', 'housekeeping'] },
  reservations: { name: "Reservations", route: "/reservations", icon: "📅", roles: ['admin', 'manager', 'receptionist'] },
  checkin: { name: "Check-In", route: "/check-in", icon: "🔑", roles: ['admin', 'manager', 'receptionist'] },
  checkout: { name: "Check-Out", route: "/check-out", icon: "🚪", roles: ['admin', 'manager', 'receptionist'] },
  billing: { name: "Billing", route: "/billing", icon: "💰", roles: ['admin', 'manager', 'accountant', 'receptionist'] },
  reports: { name: "Reports", route: "/reports", icon: "📊", roles: ['admin', 'manager', 'accountant'] },
  housekeeping: { name: "Housekeeping", route: "/housekeeping", icon: "🧹", roles: ['admin', 'manager', 'housekeeping'] },
  inventory: { name: "Inventory", route: "/inventory", icon: "📦", roles: ['admin', 'manager'] },
  users: { name: "User Management", route: "/users", icon: "👥", roles: ['admin'] },
  comprehensiveAdmin: { name: "Complete Admin", route: "/comprehensive-admin", icon: "🔧", roles: ['admin'] },
  adminPanel: { name: "Admin Panel", route: "/admin", icon: "⚙️", roles: ['admin'] }
};

export default function Layout({
  children,
  pageName = "",
  subtitle = ""
}) {
  const location = useLocation();
  const navigate = useNavigate();
  const { user, logout } = useAuth();

  // If not authenticated, redirect to login
  if (!user) {
    navigate("/login");
    return null;
  }

  // Get menu items based on user role
  const getAvailableMenus = () => {
    return Object.values(menus).filter(menu => 
      menu.roles.includes(user.role)
    );
  };

  const menuItems = getAvailableMenus();

  const activePath = menuItems.find(item =>
    location.pathname.startsWith(item.route)
  )?.route;

  function handleLogout() {
    logout();
    navigate("/login");
  }

  return (
    <div style={{ minHeight: "100vh", display: "flex", flexDirection: "column", background: "#fafafa" }}>
      {/* Top menu */}
      <header style={{
        height: 58,
        background: "#fff",
        borderBottom: "1px solid #ececec",
        display: "flex",
        alignItems: "center",
        padding: "0 24px",
        userSelect: "none",
        width: "100%",
        boxSizing: "border-box"
      }}>
        {/* Logo and Site Name */}
        <div style={{
          display: "flex",
          alignItems: "center",
          gap: 10,
          marginRight: 20,
          maxWidth: 160,
          overflow: "hidden",
          whiteSpace: "nowrap",
          textOverflow: "ellipsis"
        }}>
          <img src={logo} alt="Logo" style={{ width: 26, flexShrink: 0 }} />
          <span style={{
            fontWeight: 600,
            fontSize: 14,
            color: "#222"
          }}>
            Hotel Management
          </span>
        </div>

        {/* Navigation */}
        <nav style={{
          display: "flex",
          gap: 10,
          marginRight: 32,
          maxWidth: "calc(100vw - 280px)",
          overflowX: "auto",
          paddingBottom: 2,
          scrollbarWidth: "thin",
          background: "#fff"
        }}>
          {menuItems.map(item => (
            <Link
              key={item.name}
              to={item.route}
              style={{
                fontWeight: activePath === item.route ? 600 : 500,
                fontSize: 14.2,
                color: activePath === item.route ? "#635bd2" : "#333",
                textDecoration: "none",
                padding: "8px 14px",
                borderRadius: 6,
                backgroundColor: activePath === item.route ? "#f5f5ff" : "transparent",
                display: "flex",
                alignItems: "center",
                gap: 6,
                whiteSpace: "nowrap",
                transition: "all 0.2s ease"
              }}
            >
              <span>{item.icon}</span>
              {item.name}
            </Link>
          ))}
        </nav>

        <div style={{ flex: 1 }} />

        {/* Role indicator and user controls */}
        <div style={{
          display: "flex",
          alignItems: "center",
          gap: 12,
          minWidth: 0,
          maxWidth: "100%",
          overflow: "hidden"
        }}>
          {/* Role Badge */}
          <div style={{
            backgroundColor: "#e3f2fd",
            color: "#1976d2",
            padding: "4px 12px",
            borderRadius: 20,
            fontSize: "12px",
            fontWeight: 600,
            border: "1px solid #bbdefb"
          }}>
            {user.permissions?.name || user.role}
          </div>

          {/* User Name */}
          <span style={{
            fontSize: "14px",
            fontWeight: 500,
            color: "#333"
          }}>
            {user.name}
          </span>

          {/* Logout button */}
          <button onClick={handleLogout} style={{
            background: "#fff",
            border: "1px solid #e2e2eb",
            color: "#222",
            fontWeight: 600,
            borderRadius: 8,
            padding: "7px 16px",
            fontSize: 15.5,
            cursor: "pointer",
            flexShrink: 0
          }}>
            Logout
          </button>

          {/* Avatar */}
          <img src={avatar} alt="Avatar"
            style={{
              width: 38,
              height: 38,
              borderRadius: "50%",
              objectFit: "cover",
              border: "1.5px solid #edeffa",
              flexShrink: 0
            }}
          />
        </div>
      </header>

      {/* Main layout: sidebar + content */}
      <div style={{ display: "flex", flexGrow: 1, overflow: "hidden" }}>
        {/* Sidebar */}
        <aside style={{
          width: 220,
          height: "calc(100vh - 58px)",
          background: "#fff",
          borderRight: "1px solid #ececec",
          paddingTop: 20,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          boxSizing: "border-box",
          overflow: "hidden"
        }}>
          <nav style={{ marginLeft: 16, overflowY: "auto", flexGrow: 1 }}>
            {menuItems.map(item => {
              const isActive = activePath === item.route;
              return (
                <Link key={item.name} to={item.route} style={{
                  display: "flex",
                  alignItems: "center",
                  gap: 10,
                  padding: "12px 14px",
                  color: isActive ? "#5754e8" : "#444",
                  backgroundColor: isActive ? "#eef0fe" : "transparent",
                  borderRadius: 8,
                  fontWeight: isActive ? 600 : 400,
                  textDecoration: "none",
                  marginBottom: 6,
                  userSelect: "none",
                  fontSize: 14
                }}>
                  <span style={{ fontSize: 16 }}>{item.icon}</span>
                  {item.name}
                </Link>
              );
            })}
          </nav>

          {/* User card */}
          <div style={{
            padding: "16px 14px",
            borderTop: "1px solid #ececec",
            display: "flex",
            alignItems: "center",
            gap: 10,
            flexShrink: 0
          }}>
            <img src={avatar} alt="Avatar" style={{ width: 36, borderRadius: "50%" }} />
            <div style={{ userSelect: "none" }}>
              <div style={{ fontWeight: 600, fontSize: 15 }}>{user.name}</div>
              <div style={{ color: "#888", fontSize: 13 }}>{user.role.charAt(0).toUpperCase() + user.role.slice(1)}</div>
            </div>
          </div>
        </aside>

        {/* Main content */}
        <main style={{
          flexGrow: 1,
          overflowY: "auto",
          padding: "16px 24px",
          boxSizing: "border-box",
          background: "#fafafa"
        }}>
          {pageName && (
            <>
              <h1 style={{ margin: "0 0 10px 0", fontWeight: 700, fontSize: 28 }}>{pageName}</h1>
              {subtitle && <p style={{ color: "#777", marginBottom: 28, fontSize: 16 }}>{subtitle}</p>}
            </>
          )}
          <div style={{ maxWidth: 1200, margin: "0 auto" }}>
            {children || <Outlet />}
          </div>
        </main>
      </div>
    </div>
  );
}
