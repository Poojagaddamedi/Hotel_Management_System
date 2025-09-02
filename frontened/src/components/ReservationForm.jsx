import React, { useState } from "react";
import hotelAPI from "../services/hotelAPI";
import { generateReservationNumber } from "../utils/numberGenerators";

export default function ReservationForm() {
  // State
  const [reservationNumber] = useState(generateReservationNumber());
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);
  const [form, setForm] = useState({
    guestName: "",
    arrivalDate: "",
    departureDate: "",
    persons: "",
    rooms: "",
    rate: "",
    mobile: "",
    email: "",
    gst: "",
    remarks: ""
  });

  // Handlers
  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleClear() {
    setForm({
      guestName: "",
      arrivalDate: "",
      departureDate: "",
      persons: "",
      rooms: "",
      rate: "",
      mobile: "",
      email: "",
      gst: "",
      remarks: ""
    });
  }

  function handleSubmit(e) {
    e.preventDefault();
    createReservation();
  }

  const createReservation = async () => {
    try {
      setLoading(true);
      setError(null);
      setSuccess(null);

      // Prepare reservation data for API
      const reservationData = {
        reservationNo: reservationNumber,
        guestName: form.guestName,
        arrivalDate: form.arrivalDate,
        departureDate: form.departureDate,
        noOfPersons: parseInt(form.persons) || 1,
        noOfRooms: parseInt(form.rooms) || 1,
        rate: parseFloat(form.rate) || 0,
        contactNo: form.mobile,
        email: form.email,
        gstNo: form.gst,
        remarks: form.remarks,
        status: "CONFIRMED"
      };

      const result = await hotelAPI.createReservation(reservationData);
      
      if (result.success) {
        setSuccess("Reservation created successfully! Reservation Number: " + reservationNumber);
        // Clear form after successful submission
        setTimeout(() => {
          handleClear();
          setSuccess(null);
        }, 3000);
      } else {
        setError("Failed to create reservation: " + result.error);
      }
    } catch (err) {
      setError("Error creating reservation: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ maxWidth: 720, fontSize: "14px" }}>
      <h2 style={{ fontSize: "18px", marginBottom: 20 }}>Create Reservation</h2>
      
      {/* Success/Error Messages */}
      {success && (
        <div style={{ padding: 12, marginBottom: 20, borderRadius: 6, backgroundColor: "#d4edda", color: "#155724", border: "1px solid #c3e6cb" }}>
          {success}
        </div>
      )}
      
      {error && (
        <div style={{ padding: 12, marginBottom: 20, borderRadius: 6, backgroundColor: "#f8d7da", color: "#721c24", border: "1px solid #f5c6cb" }}>
          {error}
        </div>
      )}

      <div style={gridStyle}>
        <div style={fieldStyle}>
          <label style={labelStyle}>Reservation Number</label>
          <input
            value={reservationNumber}
            readOnly
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
          <label style={labelStyle}>Arrival Date</label>
          <input
            type="date"
            name="arrivalDate"
            value={form.arrivalDate}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Departure Date</label>
          <input
            type="date"
            name="departureDate"
            value={form.departureDate}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Number of Persons</label>
          <input
            type="number"
            name="persons"
            value={form.persons}
            onChange={handleChange}
            style={inputStyle}
            min="1"
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Rooms</label>
          <input
            name="rooms"
            value={form.rooms}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Rate</label>
          <input
            name="rate"
            value={form.rate}
            onChange={handleChange}
            style={inputStyle}
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Mobile</label>
          <input
            name="mobile"
            type="tel"
            value={form.mobile}
            onChange={handleChange}
            style={inputStyle}
            required
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Email</label>
          <input
            name="email"
            type="email"
            value={form.email}
            onChange={handleChange}
            style={inputStyle}
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>GST</label>
          <input
            name="gst"
            value={form.gst}
            onChange={handleChange}
            style={inputStyle}
          />
        </div>
        <div style={fieldStyle}>
          <label style={labelStyle}>Remarks</label>
          <textarea
            name="remarks"
            value={form.remarks}
            onChange={handleChange}
            style={{ ...inputStyle, height: '50px', resize: 'vertical' }}
            placeholder="Additional remarks"
          />
        </div>
      </div>
      <div style={{ display: "flex", gap: 12, justifyContent: "flex-end", marginTop: 22 }}>
        <button 
          type="submit" 
          disabled={loading}
          style={{
            ...saveButtonStyle,
            opacity: loading ? 0.6 : 1,
            cursor: loading ? "not-allowed" : "pointer"
          }}
        >
          {loading ? "Saving..." : "Save Reservation"}
        </button>
        <button 
          type="button" 
          onClick={handleClear} 
          disabled={loading}
          style={{
            ...clearButtonStyle,
            opacity: loading ? 0.6 : 1,
            cursor: loading ? "not-allowed" : "pointer"
          }}
        >
          Clear Form
        </button>
      </div>
    </form>
  );
}

// Styles
const gridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
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
