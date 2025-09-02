import React, { useState } from "react";
import ReservationForm from "./ReservationForm";
import ReservationList from "./ReservationList";

export default function Reservations() {
  const [tab, setTab] = useState("create");

  return (
    <div style={{ background: "#fff", borderRadius: 12, maxWidth: 1000, margin: "32px auto", boxShadow: "0 1px 8px rgba(60,60,80,0.06)", padding: "32px 36px" }}>
      <h1>Reservation Management</h1>
      <div style={{ display: "flex", marginBottom: "24px" }}>
        <button
          style={tab === "create" ? activeTabStyle : tabStyle}
          onClick={() => setTab("create")}
        >
          Create Reservation
        </button>
        <button
          style={tab === "manage" ? activeTabStyle : tabStyle}
          onClick={() => setTab("manage")}
        >
          Manage Reservations
        </button>
      </div>
      {tab === "create" ? <ReservationForm /> : <ReservationList />}
    </div>
  );
}

const tabStyle = {
  flex: 1,
  background: "#f6f6f6",
  color: "#bbb",
  border: "none",
  marginRight: "10px",
  padding: "10px 0px",
  fontSize: "17px",
  borderRadius: "7px",
  cursor: "pointer",
  fontWeight: 500
};
const activeTabStyle = {
  ...tabStyle,
  background: "#fff",
  color: "#333",
  boxShadow: "inset 0 -2px 0 #5754e8"
};
