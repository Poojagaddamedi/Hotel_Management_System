import React from "react";

const users = [
  { id: "admin001", role: "Admin", status: "Active" },
  { id: "cashier_b", role: "Cashier", status: "Active" },
  { id: "housekeep_c", role: "Housekeeping", status: "Inactive" },
  { id: "res_desk_d", role: "Reservations", status: "Active" },
  { id: "night_audit", role: "Admin", status: "Active" },
  { id: "front_desk_e", role: "Check-In", status: "Active" },
];

export default function ManageUsers() {
  return (
    <div style={{ padding: "24px", fontSize: "14px" }}>
      <h2 style={{ fontWeight: 700, fontSize: "22px", marginBottom: 24 }}>Manage Users</h2>
      <button style={addUserBtnStyle}>
        <span style={{ fontSize: 20, lineHeight: 1 }}>＋</span> Add User
      </button>
      <table style={{ width: "100%", borderCollapse: "collapse", marginTop: 16 }}>
        <thead>
          <tr style={{ background: "#f7f7fc" }}>
            <th style={thStyle}>User ID</th>
            <th style={thStyle}>Role</th>
            <th style={thStyle}>Status</th>
            <th style={thStyle}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map(user => (
            <tr key={user.id}>
              <td style={tdStyle}>{user.id}</td>
              <td style={tdStyle}>{user.role}</td>
              <td style={{ ...tdStyle, fontWeight: 600, color: user.status === "Active" ? "#248b35" : "#b62626" }}>
                {user.status}
              </td>
              <td style={tdStyle}>
                <button style={iconBtnStyle} aria-label={`Edit user ${user.id}`}><span role="img" aria-label="edit">✏️</span></button>
                <button style={iconDeleteBtnStyle} aria-label={`Delete user ${user.id}`}><span role="img" aria-label="delete">🗑️</span></button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const addUserBtnStyle = {
  background: "#635bd2",
  color: "#fff",
  padding: "10px 20px",
  borderRadius: 8,
  border: "none",
  fontWeight: 600,
  fontSize: 16,
  cursor: "pointer",
};

const thStyle = {
  textAlign: "left",
  padding: "10px 16px",
  fontWeight: 700,
  fontSize: 15,
  borderBottom: "1px solid #ebecec",
};

const tdStyle = {
  padding: "10px 16px",
  fontSize: 14,
  borderBottom: "1px solid #f0f0f0",
  background: "#fff",
};

const iconBtnStyle = {
  background: "#fff",
  border: "1px solid #c6c6db",
  borderRadius: 7,
  padding: "6px 10px",
  fontSize: 15,
  cursor: "pointer",
  marginRight: 6,
  color: "#4a4a9d"
};
const iconDeleteBtnStyle = {
  ...iconBtnStyle,
  background: "#c23737",
  border: "none",
  color: "#fff",
  marginRight: 0,
};
