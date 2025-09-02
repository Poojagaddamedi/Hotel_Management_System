import React, { useState } from "react";

export default function TransactionForm() {
  const [form, setForm] = useState({
    roomNumber: "",
    folioNumber: "",
    guestName: "",
    date: "2025-06-23",
    accHead: "",
    voucherNumber: "",
    amount: "",
    narration: "",
  });

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleClear() {
    setForm({
      roomNumber: "",
      folioNumber: "",
      guestName: "",
      date: "2025-06-23",
      accHead: "",
      voucherNumber: "",
      amount: "",
      narration: "",
    });
  }

  function handleSubmit(e) {
    e.preventDefault();
    alert("Transaction Saved!");
  }

  return (
    <form
      onSubmit={handleSubmit}
      style={{ maxWidth: 720, margin: "0 auto", fontSize: "14px" }}
    >
      <h2 style={{ fontSize: "18px", marginBottom: 20 }}>Record New Transaction</h2>
      <div style={gridStyle}>
        <div style={fieldStyle}>
          <label style={labelStyle}>Room Number</label>
          <input
            name="roomNumber"
            value={form.roomNumber}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Folio Number</label>
          <input
            name="folioNumber"
            value={form.folioNumber}
            onChange={handleChange}
            style={inputStyle}
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Guest Name</label>
          <input
            name="guestName"
            value={form.guestName}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Date</label>
          <input
            type="date"
            name="date"
            value={form.date}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Account Head</label>
          <input
            name="accHead"
            value={form.accHead}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Voucher Number</label>
          <input
            name="voucherNumber"
            value={form.voucherNumber}
            onChange={handleChange}
            style={inputStyle}
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Amount</label>
          <input
            name="amount"
            value={form.amount}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Narration</label>
          <textarea
            name="narration"
            value={form.narration}
            onChange={handleChange}
            style={{ ...inputStyle, resize: "vertical", height: 50 }}
          />
        </div>
      </div>
      <div style={{ display: "flex", gap: 12, justifyContent: "flex-end", marginTop: 22 }}>
        <button type="submit" style={saveButtonStyle}>Save Transaction</button>
        <button type="button" onClick={handleClear} style={clearButtonStyle}>Clear Form</button>
      </div>
    </form>
  );
}

// Reuse styles from AdvanceForm
const gridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(220px, 1fr))",
  gap: "12px 12px",
  marginBottom: 6,
};

const fieldStyle = { display: "flex", flexDirection: "column" };

const labelStyle = { fontSize: "13px", marginBottom: 2 };

const inputStyle = {
  fontSize: "14px",
  padding: "7px 9px",
  borderRadius: "6px",
  border: "1px solid #e0e0e0",
  background: "#fff",
  marginBottom: 2,
};

const saveButtonStyle = {
  background: "#5754e8",
  color: "#fff",
  border: "none",
  borderRadius: "6px",
  padding: "9px 16px",
  fontSize: "14px",
  fontWeight: 500,
  cursor: "pointer",
};

const clearButtonStyle = {
  background: "#f6f6f6",
  color: "#666",
  border: "none",
  borderRadius: "6px",
  padding: "9px 16px",
  fontSize: "14px",
  fontWeight: 500,
  cursor: "pointer",
};
