
import React, { useState } from "react";
import hotelAPI from "../services/hotelAPI";

export default function AdvanceForm() {
  // Payment scenario: pre-checkin, post-checkin, walk-in
  const [scenario, setScenario] = useState("");
  const [form, setForm] = useState({
    reservationNo: "",
    folioNo: "",
    amount: "",
    mode: "",
    date: new Date().toISOString().slice(0, 10),
    details: "",
    narration: "",
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleScenarioChange(e) {
    setScenario(e.target.value);
    setForm({
      reservationNo: "",
      folioNo: "",
      amount: "",
      mode: "",
      date: new Date().toISOString().slice(0, 10),
      details: "",
      narration: "",
    });
    setMessage("");
  }

  function handleClear() {
    setForm({
      reservationNo: "",
      folioNo: "",
      amount: "",
      mode: "",
      date: new Date().toISOString().slice(0, 10),
      details: "",
      narration: "",
    });
    setMessage("");
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setMessage("");
    let result;
    try {
      if (scenario === "pre-checkin") {
        // Reservation Number only
        result = await hotelAPI.createPreCheckinAdvance({
          reservationNo: form.reservationNo,
          amount: form.amount,
          mode: form.mode,
          date: form.date,
          details: form.details,
          narration: form.narration,
        });
      } else if (scenario === "post-checkin") {
        // Folio Number + Reservation Number
        result = await hotelAPI.createPostCheckinAdvance({
          folioNo: form.folioNo,
          reservationNo: form.reservationNo,
          amount: form.amount,
          mode: form.mode,
          date: form.date,
          details: form.details,
          narration: form.narration,
        });
      } else if (scenario === "walk-in") {
        // Folio Number only
        result = await hotelAPI.createWalkInAdvance({
          folioNo: form.folioNo,
          amount: form.amount,
          mode: form.mode,
          date: form.date,
          details: form.details,
          narration: form.narration,
        });
      } else {
        setMessage("Please select a payment scenario.");
        setLoading(false);
        return;
      }
      if (result && result.success) {
        setMessage("Advance saved successfully!");
        handleClear();
      } else {
        setMessage("Error: " + (result?.error || "Failed to save advance."));
      }
    } catch (err) {
      setMessage("Error: " + err.message);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ display: "flex", gap: 24, minWidth: 0, alignItems: "flex-start" }}>
      {/* Left: Form */}
      <form style={{ flex: "1 1 380px", minWidth: 0 }} onSubmit={handleSubmit}>
        <h2 style={{ fontSize: "18px", margin: "0 0 14px 0" }}>Record New Advance</h2>
        {/* Payment Scenario Selection */}
        <div style={{ marginBottom: 16 }}>
          <label style={labelStyle}>Payment Scenario</label>
          <select value={scenario} onChange={handleScenarioChange} style={inputStyle} required>
            <option value="">Select scenario</option>
            <option value="pre-checkin">Pre-Checkin Advance (Reservation Only)</option>
            <option value="post-checkin">Post-Checkin Advance (Folio + Reservation)</option>
            <option value="walk-in">Walk-in Advance (Folio Only)</option>
          </select>
        </div>
        {/* Dynamic Fields */}
        {scenario === "pre-checkin" && (
          <div style={fieldStyle}>
            <label style={labelStyle}>Reservation Number</label>
            <input name="reservationNo" value={form.reservationNo} onChange={handleChange} style={inputStyle} required />
          </div>
        )}
        {scenario === "post-checkin" && (
          <>
            <div style={fieldStyle}>
              <label style={labelStyle}>Folio Number</label>
              <input name="folioNo" value={form.folioNo} onChange={handleChange} style={inputStyle} required />
            </div>
            <div style={fieldStyle}>
              <label style={labelStyle}>Reservation Number</label>
              <input name="reservationNo" value={form.reservationNo} onChange={handleChange} style={inputStyle} required />
            </div>
          </>
        )}
        {scenario === "walk-in" && (
          <div style={fieldStyle}>
            <label style={labelStyle}>Folio Number</label>
            <input name="folioNo" value={form.folioNo} onChange={handleChange} style={inputStyle} required />
          </div>
        )}
        {/* Common Fields */}
        <div style={fieldStyle}>
          <label style={labelStyle}>Date</label>
          <input name="date" type="date" value={form.date} onChange={handleChange} style={inputStyle} required />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Mode of Payment</label>
          <select name="mode" value={form.mode} onChange={handleChange} style={inputStyle} required>
            <option value="">Select mode</option>
            <option value="Cash">Cash</option>
            <option value="Credit Card">Credit Card</option>
            <option value="Bank Transfer">Bank Transfer</option>
          </select>
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Amount</label>
          <input name="amount" type="number" min="0" step="0.01" value={form.amount} onChange={handleChange} style={inputStyle} required />
        </div>
        <div style={{ marginTop: 12 }}>
          <label style={labelStyle}>Details</label>
          <textarea name="details" value={form.details} onChange={handleChange} style={{ ...inputStyle, height: "32px" }} placeholder="Payment details..." />
        </div>
        <div style={{ marginTop: 12 }}>
          <label style={labelStyle}>Narration</label>
          <textarea name="narration" value={form.narration} onChange={handleChange} style={{ ...inputStyle, height: "32px" }} placeholder="Additional notes..." />
        </div>
        {message && (
          <div style={{ color: message.startsWith("Error") ? "#d32f2f" : "#388e3c", marginTop: 10, fontWeight: 500 }}>{message}</div>
        )}
        <div style={{ display: "flex", gap: 12, justifyContent: "flex-end", marginTop: 18 }}>
          <button type="submit" style={saveButtonStyle} disabled={loading}>
            {loading ? "Saving..." : "Save Advance"}
          </button>
          <button type="button" onClick={handleClear} style={clearButtonStyle} disabled={loading}>
            Clear Form
          </button>
        </div>
      </form>
      {/* Right: Summary (unchanged) */}
      <div style={{ flex: "0 0 260px", background: "#fafafb", borderRadius: 12, padding: "20px 15px", minWidth: 220, maxWidth: 260, fontSize: "14px" }}>
        <h3 style={{ marginTop: 0, fontSize: "16px" }}>Summary of Advances</h3>
        <div style={{ marginBottom: 6 }}>
          Total Advances Today <b>Rs. 15,00</b>
        </div>
        <div style={{ marginBottom: 6 }}>
          Number of Transactions <b>5</b>
        </div>
        <div style={{ marginBottom: 6 }}>
          Avg. Advance Amount <b>Rs. 3,00</b>
        </div>
        <div style={{ marginBottom: 10 }}>
          Last Week Total <b>Rs. 58,50</b>
        </div>
        <div>
          <img src="https://via.placeholder.com/180x80?text=Chart" alt="Summary Chart" style={{ borderRadius: 8, width: "100%", height: "auto" }} />
        </div>
        <div style={{ color: "#888", fontSize: "12px", marginTop: 12 }}>
          Real-time data synchronization is active.
        </div>
      </div>
    </div>
  );
}

const gridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(3, 1fr)",
  gap: "12px 10px",
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

