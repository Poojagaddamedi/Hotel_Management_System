import React, { useState } from "react";

export default function ReportsPage() {
  // Demo Data & State (Replace with real data as needed)
  const [reportType, setReportType] = useState("Occupancy");
  const [startDate, setStartDate] = useState("2024-07-23");
  const [endDate, setEndDate] = useState("2025-06-23");

  const tableData = [
    { month: "Jan", occupied: 70, vacant: 30, blocked: 5 },
    { month: "Feb", occupied: 75, vacant: 25, blocked: 3 },
    { month: "Mar", occupied: 80, vacant: 20, blocked: 4 },
    { month: "Apr", occupied: 65, vacant: 35, blocked: 2 },
    { month: "May", occupied: 85, vacant: 15, blocked: 6 },
    { month: "Jun", occupied: 90, vacant: 10, blocked: 1 },
    { month: "Jul", occupied: 78, vacant: 22, blocked: 3 },
    { month: "Aug", occupied: 82, vacant: 18, blocked: 4 },
    { month: "Sep", occupied: 72, vacant: 28, blocked: 5 },
    { month: "Oct", occupied: 88, vacant: 12, blocked: 2 },
    { month: "Nov", occupied: 76, vacant: 24, blocked: 3 },
    { month: "Dec", occupied: 92, vacant: 8, blocked: 1 },
  ];

  return (
    <div
      style={{
        maxWidth: 1000,
        margin: "36px auto",
        background: "#fff",
        borderRadius: 16,
        boxShadow: "0 1px 12px rgba(60,60,80,0.06)",
        padding: "32px 28px",
      }}
    >
      {/* Report Filters */}
      <div
        style={{
          borderRadius: 12,
          border: "1px solid #eeeeee",
          padding: "25px 32px 20px 32px",
          marginBottom: 28,
        }}
      >
        <h2 style={{ fontSize: 19, fontWeight: 700, marginBottom: 18 }}>
          Report Filters
        </h2>
        <div style={{ display: "flex", gap: 18, alignItems: "end", flexWrap: "wrap" }}>
          <div>
            <div style={{ marginBottom: 6, fontWeight: 500, fontSize: 15 }}>Report Type</div>
            <select
              style={inputStyle}
              value={reportType}
              onChange={e => setReportType(e.target.value)}
            >
              <option value="Occupancy">Occupancy</option>
              <option value="Revenue">Revenue</option>
              {/* Add more report types as needed */}
            </select>
          </div>
          <div>
            <div style={{ marginBottom: 6, fontWeight: 500, fontSize: 15 }}>Start Date</div>
            <input
              style={inputStyle}
              type="date"
              value={startDate}
              onChange={e => setStartDate(e.target.value)}
            />
          </div>
          <div>
            <div style={{ marginBottom: 6, fontWeight: 500, fontSize: 15 }}>End Date</div>
            <input
              style={inputStyle}
              type="date"
              value={endDate}
              onChange={e => setEndDate(e.target.value)}
            />
          </div>
          <button
            style={{
              marginLeft: "auto",
              background: "#635bd2",
              color: "#fff",
              border: "none",
              borderRadius: 10,
              padding: "11px 26px",
              fontWeight: 600,
              fontSize: 16,
              cursor: "pointer",
              minWidth: 160,
            }}
          >
            Generate Report
          </button>
        </div>
      </div>
      {/* Occupancy Report Overview */}
      <div
        style={{
          borderRadius: 12,
          border: "1px solid #eeeeee",
          padding: "25px 32px 30px 32px",
          marginBottom: 24,
        }}
      >
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
          <h2 style={{ fontSize: 18, fontWeight: 700, margin: 0 }}>
            Occupancy Report Overview
          </h2>
          <div style={{ display: "flex", gap: 12 }}>
            <button style={exportBtnStyle}>Export PDF</button>
            <button style={exportBtnStyle}>Export Excel</button>
          </div>
        </div>
        <div style={{ height: 320, margin: "20px 0 32px 0", background: "#f4f4fa", borderRadius: 12, display: "flex", alignItems: "center", justifyContent: "center", color: "#aaa", fontSize: 18 }}>
          {/* Place your chart component here */}
          Bar Chart Here
        </div>
        <table style={{ width: "100%", borderCollapse: "collapse", background: "#fff", marginBottom: 10 }}>
          <thead>
            <tr style={{ background: "#f7f7fc" }}>
              <th style={thStyle}>Month</th>
              <th style={thStyle}>Occupied Rooms</th>
              <th style={thStyle}>Vacant Rooms</th>
              <th style={thStyle}>Blocked Rooms</th>
            </tr>
          </thead>
          <tbody>
            {tableData.map(row => (
              <tr key={row.month}>
                <td style={tdStyle}>{row.month}</td>
                <td style={tdStyle}>{row.occupied}</td>
                <td style={tdStyle}>{row.vacant}</td>
                <td style={tdStyle}>{row.blocked}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {/* Pagination */}
        <div style={{ display: "flex", justifyContent: "center", gap: 20, marginTop: 14 }}>
          <button style={pageBtnStyle} disabled>Previous</button>
          <span style={{ fontSize: 15, color: "#6b6b6b" }}>Page 1 of 1</span>
          <button style={pageBtnStyle} disabled>Next</button>
        </div>
      </div>
    </div>
  );
}

const inputStyle = {
  fontSize: "15px",
  padding: "10px 16px",
  borderRadius: 7,
  border: "1px solid #d9d9df",
  background: "#fafbff",
  outline: "none",
  minWidth: 150,
};

const exportBtnStyle = {
  background: "#fff",
  border: "1px solid #ececec",
  padding: "7px 16px",
  borderRadius: 8,
  color: "#635bd2",
  fontWeight: 600,
  cursor: "pointer",
  fontSize: 15,
};

const thStyle = {
  textAlign: "left",
  padding: "11px 13px",
  borderBottom: "1px solid #ebebeb",
  fontWeight: 600,
  fontSize: 15,
};

const tdStyle = {
  padding: "11px 13px",
  borderBottom: "1px solid #ebebeb",
  fontSize: 15,
  background: "#fff",
};

const pageBtnStyle = {
  background: "#f5f5fa",
  border: "1px solid #ececec",
  borderRadius: 8,
  padding: "7px 18px",
  fontSize: 15,
  color: "#888",
  fontWeight: 600,
  cursor: "pointer",
};
