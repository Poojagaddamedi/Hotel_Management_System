import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import hotelAPI from "../services/hotelAPI";
import { generateFolioNumber, formatCurrency, formatDate } from "../utils/numberGenerators";

export default function CheckInPage() {
  const [advances, setAdvances] = useState([]);

  return (
    <div
      style={{
        display: "flex",
        gap: 10,
        alignItems: "flex-start",
        justifyContent: "space-between",
        maxWidth: 1120,
        margin: "24px 16px",
        flexWrap: "nowrap",
      }}
    >
      {/* Left - Check-In Form */}
      <div
        style={{
          flex: "0 0 65%",
          background: "#fff",
          borderRadius: 14,
          boxShadow: "0 1px 8px rgba(60,60,80,0.07)",
          padding: "24px 24px",
          minWidth: 0
        }}
      >
        <CheckInForm onAdvancesUpdate={setAdvances} />
      </div>

      {/* Right - Advance Details */}
      <aside
        style={{
          flex: "0 0 32%",
          background: "#fafafb",
          borderRadius: 16,
          boxShadow: "0 1px 8px rgba(60,60,80,0.06)",
          padding: "28px 20px",
          height: "fit-content",
          maxHeight: "90vh",
          overflowY: "auto",
        }}
      >
        <AdvanceDetails advances={advances} />
      </aside>
    </div>
  );
}

function CheckInForm({ onAdvancesUpdate }) {
  const location = useLocation();
  const selectedRoom = location.state?.selectedRoom;
  const roomNo = location.state?.roomNo;
  const roomType = location.state?.roomType;

  const [form, setForm] = useState({
    reservationNumber: "",
    folioNumber: "",
    guestName: "",
    arrivalDate: "",
    departureDate: "",
    noOfDays: "",
    persons: "",
    mobile: "",
    rate: "",
    gstIncluded: true,
    remarks: "",
    roomNumber: roomNo || "",
  });

  const [vacantRooms, setVacantRooms] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(null);
  const [error, setError] = useState(null);
  const [loadingRooms, setLoadingRooms] = useState(true);
  const [advances, setAdvances] = useState([]);

  // Load vacant rooms on component mount
  useEffect(() => {
    loadVacantRooms();
  }, []);

  // Handle pre-selected room from navigation
  useEffect(() => {
    if (selectedRoom && roomNo) {
      // Generate folio number for new check-in
      const newFolioNumber = generateFolioNumber();
      setForm(prev => ({
        ...prev,
        roomNumber: roomNo,
        folioNumber: newFolioNumber,
        arrivalDate: new Date().toISOString().split('T')[0], // Today's date
      }));
    }
  }, [selectedRoom, roomNo]);

  // Auto-populate data if reservation number is entered
  useEffect(() => {
    if (form.reservationNumber && form.reservationNumber.length >= 6) {
      loadReservationData(form.reservationNumber);
    }
  }, [form.reservationNumber]);

  const loadVacantRooms = async () => {
    try {
      setLoadingRooms(true);
      const result = await hotelAPI.getRoomsByStatus("VACANT_CLEAN");
      
      if (result && result.success && result.data) {
        setVacantRooms(result.data.map(room => room.roomNo || room.roomNumber || room.number));
      } else {
        // Fallback: get all rooms and filter vacant ones
        try {
          const allRoomsResult = await hotelAPI.getRooms();
          if (allRoomsResult && allRoomsResult.success && allRoomsResult.data) {
            const vacant = allRoomsResult.data
              .filter(room => 
                room.status === 'VACANT_CLEAN' || 
                room.status === 'VACANT_DIRTY' ||
                room.status === 'Vacant'
              )
              .map(room => room.roomNo || room.roomNumber || room.number);
            setVacantRooms(vacant);
          } else {
            // Demo rooms fallback
            setVacantRooms(['101', '102', '103', '201', '202', '203']);
            setError("Using demo room data - Backend API not responding");
          }
        } catch (fallbackErr) {
          setVacantRooms(['101', '102', '103', '201', '202', '203']);
          setError("Backend connection failed - Using demo room data");
        }
      }
    } catch (err) {
      console.error("CheckIn loadVacantRooms error:", err);
      setVacantRooms(['101', '102', '103', '201', '202', '203']);
      setError("Backend connection failed - Using demo room data");
    } finally {
      setLoadingRooms(false);
    }
  };

  const loadReservationData = async (reservationNo) => {
    try {
      const result = await hotelAPI.searchReservations(reservationNo);
      
      if (result.success && result.data.length > 0) {
        const reservation = result.data[0];
        setForm(prev => ({
          ...prev,
          guestName: reservation.guestName || "",
          arrivalDate: reservation.arrivalDate || "",
          departureDate: reservation.departureDate || "",
          persons: reservation.noOfPersons?.toString() || "",
          mobile: reservation.contactNo || "",
          rate: reservation.rate?.toString() || "",
        }));

        // Load advance payments for this reservation
        loadAdvances(reservationNo);
        
        // Calculate number of days
        if (reservation.arrivalDate && reservation.departureDate) {
          const arrival = new Date(reservation.arrivalDate);
          const departure = new Date(reservation.departureDate);
          const days = Math.ceil((departure - arrival) / (1000 * 60 * 60 * 24));
          setForm(prev => ({ ...prev, noOfDays: days.toString() }));
        }
      }
    } catch (err) {
      console.error("Error loading reservation data:", err);
    }
  };

  const loadAdvances = async (reservationNo) => {
    try {
      const result = await hotelAPI.getAdvancesByReservation(reservationNo);
      if (result.success) {
        setAdvances(result.data);
        if (onAdvancesUpdate) {
          onAdvancesUpdate(result.data);
        }
      }
    } catch (err) {
      console.error("Error loading advances:", err);
    }
  };

  function handleChange(e) {
    const { name, value, type, checked } = e.target;
    setForm(f =>
      type === "checkbox" ? { ...f, [name]: checked } : { ...f, [name]: value }
    );
  }

  function handleClear() {
    setForm({
      reservationNumber: "",
      folioNumber: "",
      guestName: "",
      arrivalDate: "",
      departureDate: "",
      noOfDays: "",
      persons: "",
      mobile: "",
      rate: "",
      gstIncluded: false,
      remarks: "",
      roomNumber: "",
    });
    setAdvances([]);
    setSuccess(null);
    setError(null);
  }

  function handleSubmit(e) {
    e.preventDefault();
    createCheckin();
  }

  const createCheckin = async () => {
    try {
      setLoading(true);
      setError(null);
      setSuccess(null);

      // Generate folio number if not provided
      const folioNo = form.folioNumber || generateFolioNumber();

      // Prepare checkin data for API
      const checkinData = {
        folioNo: folioNo,
        guestName: form.guestName,
        contactNo: form.mobile,
        emailId: "", // Could add email field if needed
        checkInDate: form.arrivalDate,
        checkOutDate: form.departureDate,
        noOfPersons: parseInt(form.persons) || 1,
        roomNo: form.roomNumber,
        rate: parseFloat(form.rate) || 0,
        remarks: form.remarks,
        reservationNo: form.reservationNumber || null,
        userId: 1, // Should come from auth context
        status: "ACTIVE",
        isTaxInclusive: form.gstIncluded,
        arrivalTime: new Date().toTimeString().split(' ')[0] // Current time
      };

      const result = await hotelAPI.createCheckin(checkinData);
      
      if (result.success) {
        setSuccess(`Guest checked in successfully! Folio Number: ${folioNo}`);
        // Clear form after successful check-in
        setTimeout(() => {
          handleClear();
        }, 3000);
        
        // Reload vacant rooms
        loadVacantRooms();
      } else {
        setError("Failed to check-in guest: " + result.error);
      }
    } catch (err) {
      setError("Error during check-in: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h1 style={{ marginBottom: 12, fontSize: 22 }}>Check-In Guest</h1>
      <p style={{ color: "#6b6b6b", marginBottom: 26, fontSize: 14 }}>
        Process guest arrivals and update room statuses.
      </p>

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

      <h3 style={{ marginBottom: 24, fontWeight: 500 }}>Guest Information</h3>

      <div style={gridStyle}>
        {[
          { label: "Reservation Number", name: "reservationNumber", type: "text" },
          { label: "Folio Number", name: "folioNumber", type: "text" },
          { label: "Guest Name", name: "guestName", type: "text" },
          { label: "Arrival Date", name: "arrivalDate", type: "date" },
          { label: "Departure Date", name: "departureDate", type: "date" },
          { label: "No of Days", name: "noOfDays", type: "number" },
          { label: "No of Persons", name: "persons", type: "number" },
          { label: "Mobile Number", name: "mobile", type: "tel" },
          { label: "Rate", name: "rate", type: "text" },
        ].map(({ label, name, type }, idx) => (
          <div style={fieldStyle} key={idx}>
            <label style={labelStyle}>{label}</label>
            <input
              style={inputStyle}
              name={name}
              value={form[name]}
              type={type || "text"}
              onChange={handleChange}
            />
          </div>
        ))}
      </div>

      {/* GST toggle */}
      <div style={{ display: "flex", alignItems: "center", margin: "22px 0" }}>
        <label
          style={{ display: "flex", alignItems: "center", gap: 10, fontWeight: 500 }}
        >
          <input
            type="checkbox"
            checked={form.gstIncluded}
            name="gstIncluded"
            onChange={handleChange}
            style={{
              appearance: "none",
              width: "44px",
              height: "24px",
              borderRadius: "12px",
              position: "relative",
              background: form.gstIncluded ? "#5754e8" : "#d4d4d4",
              transition: "background 0.2s",
              outline: "none",
              marginRight: "8px",
              cursor: "pointer",
            }}
          />
          <span>Including GST</span>
        </label>
      </div>

      {/* Remarks */}
      <div style={{ marginBottom: 16 }}>
        <label style={labelStyle}>Remarks</label>
        <textarea
          style={{ ...inputStyle, height: "48px", resize: "vertical" }}
          name="remarks"
          value={form.remarks}
          onChange={handleChange}
          placeholder="Any special requests or notes"
        />
      </div>

      {/* Room Number */}
      <div style={{ marginBottom: 8 }}>
        <label style={{ ...labelStyle, fontWeight: 600 }}>
          Room Number <span style={{ color: "red" }}>*</span>
        </label>
        <select
          name="roomNumber"
          value={form.roomNumber}
          onChange={handleChange}
          style={{ ...inputStyle, appearance: "auto", background: "#fafafa" }}
          required
          disabled={loadingRooms}
        >
          <option value="" disabled>
            {loadingRooms ? "Loading rooms..." : "Select a vacant room"}
          </option>
          {vacantRooms.map((room) => (
            <option key={room} value={room}>
              {room}
            </option>
          ))}
        </select>
      </div>

      <div style={{ color: "#888", fontSize: "13px", margin: "9px 0 22px 0" }}>
        Note: For walk-in guests, room status will automatically update to "OD (Occupied Dirty)" upon check-in.
      </div>

      <div style={{ display: "flex", gap: 14 }}>
        <button 
          type="submit" 
          disabled={loading || !form.roomNumber}
          style={{
            ...checkinButtonStyle,
            opacity: (loading || !form.roomNumber) ? 0.6 : 1,
            cursor: (loading || !form.roomNumber) ? "not-allowed" : "pointer"
          }}
        >
          {loading ? "Processing..." : "Check-In"}
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
          Clear
        </button>
      </div>
    </form>
  );
}

function AdvanceDetails({ advances = [] }) {
  return (
    <>
      <h2 style={{ fontSize: 20, margin: 0, fontWeight: 700 }}>Advance Details</h2>
      <div style={{ color: "#666", fontSize: 15, marginBottom: 22 }}>
        Payments recorded for this reservation or folio.
      </div>
      
      {advances.length === 0 ? (
        <div style={{ textAlign: "center", color: "#888", padding: 20 }}>
          No advance payments found
        </div>
      ) : (
        advances.map((item, idx) => (
          <div
            key={idx}
            style={{
              marginBottom: 22,
              paddingBottom: 14,
              borderBottom: idx < advances.length - 1 ? "1px solid #ececec" : "none",
            }}
          >
            <div style={{ fontWeight: 600, color: "#635bd2", fontSize: 18, marginBottom: 2 }}>
              ₹{item.amount?.toFixed(2) || "0.00"}
            </div>
            <div style={{ fontSize: 15, fontWeight: 500 }}>{item.paymentMethod || "Cash"}</div>
            <div style={{ fontStyle: "italic", color: "#888", fontSize: 14 }}>
              {item.remarks || item.note || "Advance payment"}
            </div>
            <div style={{ fontSize: 14, color: "#aaa", marginTop: 6 }}>
              {item.advanceDate || item.date || item.createdOn || "N/A"}
            </div>
          </div>
        ))
      )}
    </>
  );
}

/* Shared Styles */
const gridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
  gap: "24px 20px",
  marginBottom: 12,
};

const fieldStyle = {
  display: "flex",
  flexDirection: "column",
};

const labelStyle = {
  marginBottom: 6,
  fontWeight: 500,
  fontSize: 15,
};

const inputStyle = {
  fontSize: "16px",
  padding: "11px 13px",
  borderRadius: "8px",
  border: "1px solid #e0e0e0",
  outline: "none",
  background: "#fff",
  marginBottom: 3,
};

const checkinButtonStyle = {
  background: "#5754e8",
  color: "#fff",
  border: "none",
  borderRadius: "8px",
  padding: "11px 23px",
  fontSize: "16px",
  fontWeight: 500,
  cursor: "pointer",
};

const clearButtonStyle = {
  background: "#f6f6f6",
  color: "#666",
  border: "none",
  borderRadius: "8px",
  padding: "11px 23px",
  fontSize: "16px",
  fontWeight: 500,
  cursor: "pointer",
};
