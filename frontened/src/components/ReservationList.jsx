import React, { useState, useEffect } from "react";
import hotelAPI from "../services/hotelAPI";
import { generateReservationNumber } from "../utils/numberGenerators";

export default function ReservationList({ onEdit }) {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchQuery, setSearchQuery] = useState("");

  useEffect(() => {
    loadReservations();
  }, []);

  const loadReservations = async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await hotelAPI.getReservations();
      
      if (result && result.success && result.data) {
        setReservations(result.data);
      } else {
        console.warn("Reservations API failed, using demo data");
        setReservations(generateDemoReservations());
        setError("Using demo data - Backend API not responding");
      }
    } catch (err) {
      console.error("ReservationList loadReservations error:", err);
      setReservations(generateDemoReservations());
      setError("Backend connection failed - Using demo data");
    } finally {
      setLoading(false);
    }
  };

  const generateDemoReservations = () => {
    return [
      {
        id: 1,
        reservationNo: generateReservationNumber(),
        guestName: "John Doe",
        contactNo: "9876543210",
        emailId: "john@example.com",
        fromDate: "2025-09-01",
        toDate: "2025-09-03",
        arrivalDate: "2025-09-01",
        departureDate: "2025-09-03",
        totalPax: 2,
        noOfPersons: 2,
        noOfRooms: 1,
        rate: 3500.00,
        tax: 630.00,
        totalAmt: 4130.00,
        status: "0",
        selectedRoom: "101",
        purpose: "Business",
        companyName: "ABC Corp"
      },
      {
        id: 2,
        reservationNo: generateReservationNumber(),
        guestName: "Jane Smith",
        contactNo: "9876543211",
        emailId: "jane@example.com",
        fromDate: "2025-09-02",
        toDate: "2025-09-05",
        arrivalDate: "2025-09-02",
        departureDate: "2025-09-05",
        totalPax: 1,
        noOfPersons: 1,
        noOfRooms: 1,
        rate: 2800.00,
        tax: 504.00,
        totalAmt: 3304.00,
        status: "0",
        selectedRoom: "102",
        purpose: "Leisure",
        companyName: null
      },
      {
        id: 3,
        reservationNo: generateReservationNumber(),
        guestName: "Robert Wilson",
        contactNo: "9876543212",
        emailId: "robert@example.com",
        fromDate: "2025-09-03",
        toDate: "2025-09-06",
        arrivalDate: "2025-09-03",
        departureDate: "2025-09-06",
        totalPax: 4,
        noOfPersons: 4,
        noOfRooms: 2,
        rate: 7000.00,
        tax: 1260.00,
        totalAmt: 8260.00,
        status: "0",
        selectedRoom: "201,202",
        purpose: "Family Vacation",
        companyName: null
      }
    ];
  };

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      loadReservations();
      return;
    }

    try {
      setLoading(true);
      const result = await hotelAPI.searchReservations(searchQuery);
      
      if (result.success) {
        setReservations(result.data);
      } else {
        setError("Search failed: " + result.error);
      }
    } catch (err) {
      setError("Error searching reservations: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (reservationId) => {
    if (!window.confirm("Are you sure you want to delete this reservation?")) {
      return;
    }

    try {
      const result = await hotelAPI.deleteReservation(reservationId);
      
      if (result.success) {
        loadReservations(); // Reload the list
      } else {
        alert("Failed to delete reservation: " + result.error);
      }
    } catch (err) {
      alert("Error deleting reservation: " + err.message);
    }
  };

  if (loading) {
    return <div style={{ padding: 20, textAlign: "center" }}>Loading reservations...</div>;
  }

  if (error) {
    return (
      <div style={{ padding: 20, textAlign: "center", color: "red" }}>
        <p>Error: {error}</p>
        <button onClick={loadReservations} style={{ marginTop: 10, padding: "8px 16px", borderRadius: 6, border: "1px solid #ccc", cursor: "pointer" }}>
          Retry
        </button>
      </div>
    );
  }

  return (
    <div>
      {/* Search Bar */}
      <div style={{ marginBottom: 20, display: "flex", gap: 10, alignItems: "center" }}>
        <input
          type="text"
          placeholder="Search reservations by guest name, contact, or reservation number..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          style={{
            flex: 1,
            padding: "8px 12px",
            fontSize: "14px",
            borderRadius: 6,
            border: "1px solid #ccc",
            outline: "none"
          }}
          onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
        />
        <button
          onClick={handleSearch}
          style={{
            padding: "8px 16px",
            borderRadius: 6,
            border: "1px solid #5754e8",
            backgroundColor: "#5754e8",
            color: "white",
            cursor: "pointer"
          }}
        >
          Search
        </button>
        <button
          onClick={loadReservations}
          style={{
            padding: "8px 16px",
            borderRadius: 6,
            border: "1px solid #ccc",
            backgroundColor: "#f6f6f6",
            cursor: "pointer"
          }}
        >
          Clear
        </button>
      </div>

      {/* Reservations Table */}
      {reservations.length === 0 ? (
        <div style={{ textAlign: "center", padding: 40, color: "#666" }}>
          No reservations found
        </div>
      ) : (
        <table style={{ width: "100%", borderCollapse: "collapse", fontSize: "14px" }}>
          <thead>
            <tr style={{ background: "#f7f7fc" }}>
              <th style={thStyle}>Resv. No</th>
              <th style={thStyle}>Guest Name</th>
              <th style={thStyle}>Contact</th>
              <th style={thStyle}>Email</th>
              <th style={thStyle}>Check-in</th>
              <th style={thStyle}>Check-out</th>
              <th style={thStyle}>Persons</th>
              <th style={thStyle}>Rooms</th>
              <th style={thStyle}>Rate</th>
              <th style={thStyle}>Total Amount</th>
              <th style={thStyle}>Status</th>
              <th style={thStyle}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {reservations.map((r) => (
              <tr key={r.id}>
                <td style={tdStyle}>{r.reservationNo}</td>
                <td style={tdStyle}>{r.guestName}</td>
                <td style={tdStyle}>{r.contactNo}</td>
                <td style={tdStyle}>{r.emailId || 'N/A'}</td>
                <td style={tdStyle}>{r.arrivalDate || r.fromDate}</td>
                <td style={tdStyle}>{r.departureDate || r.toDate}</td>
                <td style={tdStyle}>{r.noOfPersons || r.totalPax}</td>
                <td style={tdStyle}>{r.noOfRooms}</td>
                <td style={tdStyle}>{r.rate ? `₹${r.rate.toLocaleString()}` : 'N/A'}</td>
                <td style={tdStyle}>{r.totalAmt ? `₹${r.totalAmt.toLocaleString()}` : 'N/A'}</td>
                <td style={tdStyle}>
                  <span style={{
                    padding: "4px 8px",
                    borderRadius: 4,
                    fontSize: 12,
                    backgroundColor: r.status === 'CONFIRMED' || r.status === '0' ? '#d4edda' : '#fff3cd',
                    color: r.status === 'CONFIRMED' || r.status === '0' ? '#155724' : '#856404'
                  }}>
                    {r.status === '0' ? 'BOOKED' : r.status === '1' ? 'CANCELLED' : r.status}
                  </span>
                </td>
                <td style={tdStyle}>
                  <button 
                    style={editBtnStyle} 
                    onClick={() => onEdit && onEdit(r)}
                    aria-label={`Edit reservation ${r.reservationNo}`}
                    title="Edit"
                  >
                    ✏️
                  </button>
                  <button 
                    style={{...editBtnStyle, color: "#dc3545", marginLeft: 8}} 
                    onClick={() => handleDelete(r.id)}
                    aria-label={`Delete reservation ${r.reservationNo}`}
                    title="Delete"
                  >
                    🗑️
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

// Styles for reservation list
const thStyle = {
  textAlign: "left",
  padding: "10px 16px",
  borderBottom: "1px solid #ddd",
  fontWeight: 600,
};

const tdStyle = {
  padding: "10px 16px",
  borderBottom: "1px solid #eee",
};

const editBtnStyle = {
  background: "none",
  border: "none",
  color: "#5754e8",
  cursor: "pointer",
  fontSize: 18,
  padding: "2px 6px",
};
