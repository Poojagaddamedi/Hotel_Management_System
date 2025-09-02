import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import RoomCard from "./RoomCard";
import hotelAPI from "../services/hotelAPI";

export default function Dashboard() {
  const navigate = useNavigate();
  
  // State management
  const [rooms, setRooms] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchText, setSearchText] = useState("");
  const [page, setPage] = useState(1);
  const roomsPerPage = 12;

  // Fetch rooms data on component mount
  useEffect(() => {
    loadRooms();
  }, []);

  const loadRooms = async () => {
    try {
      setLoading(true);
      setError(null);
      const result = await hotelAPI.getRooms();
      
      if (result && result.success && result.data) {
        // Transform backend data to frontend format
        const transformedRooms = result.data.map(room => ({
          id: room.id,
          number: room.roomNo || room.roomNumber || room.number,
          status: mapRoomStatus(room.status),
          guest: room.currentGuest || room.guestName || null,
          checkIn: room.checkInDate || room.checkinDate || null,
          checkOut: room.checkOutDate || room.checkoutDate || null,
          roomType: room.roomType || room.type || 'Standard',
          floor: room.floor || 1,
          capacity: room.capacity || 2
        }));
        setRooms(transformedRooms);
      } else {
        // Fallback: Create demo data if API fails
        console.warn("API failed, using demo data");
        setRooms(generateDemoRooms());
        setError("Using demo data - Backend API not responding");
      }
    } catch (err) {
      console.error("Dashboard loadRooms error:", err);
      setRooms(generateDemoRooms());
      setError("Backend connection failed - Using demo data");
    } finally {
      setLoading(false);
    }
  };

  const generateDemoRooms = () => {
    const demoRooms = [];
    for (let i = 1; i <= 20; i++) {
      demoRooms.push({
        id: i,
        number: (100 + i).toString(),
        status: i % 3 === 0 ? 'Occupied' : 'Vacant',
        guest: i % 3 === 0 ? `Guest ${i}` : null,
        checkIn: i % 3 === 0 ? '2025-08-30' : null,
        checkOut: i % 3 === 0 ? '2025-09-02' : null,
        roomType: i % 2 === 0 ? 'Deluxe' : 'Standard',
        floor: Math.ceil(i / 10),
        capacity: 2
      });
    }
    return demoRooms;
  };

  // Map backend room status to frontend status
  const mapRoomStatus = (backendStatus) => {
    const statusMap = {
      'VACANT_CLEAN': 'Vacant',
      'VACANT_DIRTY': 'Vacant',
      'OCCUPIED_CLEAN': 'Occupied',
      'OCCUPIED_DIRTY': 'Occupied',
      'OUT_OF_ORDER': 'Blocked',
      'MAINTENANCE': 'Blocked'
    };
    return statusMap[backendStatus] || 'Unknown';
  };

  const filteredRooms = rooms.filter(room =>
    room.number.toString().includes(searchText) ||
    (room.status && room.status.toLowerCase().includes(searchText.toLowerCase()))
  );
  const pageCount = Math.ceil(filteredRooms.length / roomsPerPage);
  const shownRooms = filteredRooms.slice((page - 1) * roomsPerPage, page * roomsPerPage);

  function handleViewDetails(room) {
    alert(`Viewing details for Room ${room.number}\nStatus: ${room.status}\nGuest: ${room.guest || 'None'}\nRoom Type: ${room.roomType || 'N/A'}`);
  }

  function handleCheckIn(room) {
    if (room.status === 'Vacant') {
      // Navigate to check-in form with room pre-selected
      navigate("/check-in", { 
        state: { 
          selectedRoom: room,
          roomNo: room.number,
          roomType: room.roomType || 'Standard'
        }
      });
    } else {
      alert(`Room ${room.number} is currently ${room.status} and not available for check-in`);
    }
  }

  function handleRoomStatusUpdate(roomNumber, newStatus) {
    // Update room status via API
    updateRoomStatus(roomNumber, newStatus);
  }

  const updateRoomStatus = async (roomNumber, status) => {
    try {
      const result = await hotelAPI.updateRoomStatus(roomNumber, status);
      if (result.success) {
        // Reload rooms to get updated data
        loadRooms();
      } else {
        alert("Failed to update room status: " + result.error);
      }
    } catch (err) {
      alert("Error updating room status: " + err.message);
    }
  };

  // Loading state
  if (loading) {
    return (
      <div style={{ padding: 24, fontSize: "14px", maxWidth: 960, margin: "0 auto", textAlign: "center" }}>
        <h1 style={{ fontSize: "22px", fontWeight: 700, marginBottom: 20 }}>Dashboard Overview</h1>
        <div style={{ padding: 40 }}>Loading rooms...</div>
      </div>
    );
  }

  // Error state
  if (error) {
    return (
      <div style={{ padding: 24, fontSize: "14px", maxWidth: 960, margin: "0 auto" }}>
        <h1 style={{ fontSize: "22px", fontWeight: 700, marginBottom: 20 }}>Dashboard Overview</h1>
        <div style={{ padding: 40, color: "red", textAlign: "center" }}>
          <p>Error: {error}</p>
          <button onClick={loadRooms} style={{ marginTop: 10, padding: "8px 16px", borderRadius: 6, border: "1px solid #ccc", cursor: "pointer" }}>
            Retry
          </button>
        </div>
      </div>
    );
  }

  return (
    <div style={{ padding: 24, fontSize: "14px", maxWidth: 960, margin: "0 auto" }}>
      <h1 style={{ fontSize: "22px", fontWeight: 700, marginBottom: 20 }}>Dashboard Overview</h1>
      <div style={{
        display: "flex",
        gap: 20,
        marginBottom: 28,
        flexWrap: "wrap"
      }}>
        <StatCard label="Total Rooms" value={rooms.length} icon="🏨" />
        <StatCard label="Occupied" value={rooms.filter(r => r.status === "Occupied").length} icon="👤" />
        <StatCard label="Vacant" value={rooms.filter(r => r.status === "Vacant").length} icon="🧳" />
        <StatCard label="Blocked" value={rooms.filter(r => r.status === "Blocked").length} icon="🔒" />
      </div>
      <input
        type="text"
        placeholder="Search rooms by number or status..."
        value={searchText}
        onChange={e => { setSearchText(e.target.value); setPage(1); }}
        style={{
          width: "280px",
          padding: "8px 12px",
          fontSize: "14px",
          borderRadius: 6,
          border: "1px solid #ccc",
          marginBottom: 16,
          outline: "none",
          backgroundColor: "#fff"
        }}
      />
      <div style={{
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(210px, 1fr))",
        gap: 18
      }}>
        {shownRooms.map(room => (
          <RoomCard
            room={room}
            key={room.number}
            onViewDetails={handleViewDetails}
            onCheckIn={handleCheckIn}
          />
        ))}
      </div>
      <div style={{ marginTop: 28, display: "flex", justifyContent: "center", alignItems: "center", gap: 12 }}>
        <button disabled={page === 1} onClick={() => setPage(page - 1)} style={paginationBtnStyle}>&lt; Previous</button>
        <span style={{ fontSize: 15, color: "#666" }}>Page {page} of {pageCount}</span>
        <button disabled={page === pageCount} onClick={() => setPage(page + 1)} style={paginationBtnStyle}>Next &gt;</button>
      </div>

      {/* Quick Actions Section */}
      <div style={{
        marginTop: 40,
        padding: 24,
        backgroundColor: "#f8f9fa",
        borderRadius: 12,
        border: "1px solid #e0e0e0"
      }}>
        <h3 style={{ marginBottom: 20, fontSize: "18px", fontWeight: 600, textAlign: "center" }}>
          Quick Actions
        </h3>
        <div style={{
          display: "flex",
          justifyContent: "center",
          gap: 16,
          flexWrap: "wrap"
        }}>
          <button 
            onClick={() => navigate("/reservations")}
            style={{
              ...quickActionBtnStyle,
              backgroundColor: "#28a745",
              color: "white"
            }}
          >
            📅 New Reservation
          </button>
          <button 
            onClick={() => navigate("/check-in")}
            style={{
              ...quickActionBtnStyle,
              backgroundColor: "#007bff",
              color: "white"
            }}
          >
            🔑 Check In Guest
          </button>
          <button 
            onClick={() => navigate("/billing")}
            style={{
              ...quickActionBtnStyle,
              backgroundColor: "#ffc107",
              color: "#000"
            }}
          >
            💰 Billing & Payments
          </button>
          <button 
            onClick={() => navigate("/housekeeping")}
            style={{
              ...quickActionBtnStyle,
              backgroundColor: "#6f42c1",
              color: "white"
            }}
          >
            🧹 Housekeeping
          </button>
          <button 
            onClick={() => navigate("/reports")}
            style={{
              ...quickActionBtnStyle,
              backgroundColor: "#17a2b8",
              color: "white"
            }}
          >
            📊 Reports
          </button>
        </div>
      </div>
    </div>
  );
}

function StatCard({ label, value, icon }) {
  return (
    <div style={{
      background: "#fff",
      minWidth: 120,
      boxShadow: "0 1px 6px 0 rgba(0,0,0,0.08)",
      borderRadius: 12,
      padding: 14,
      textAlign: "left",
      fontSize: "14px"
    }}>
      <div style={{ fontSize: 18, fontWeight: 600 }}>{value}</div>
      <div>{label} {icon}</div>
    </div>
  );
}

const paginationBtnStyle = {
  backgroundColor: "#f6f6f6",
  border: "1px solid #ccc",
  borderRadius: 6,
  cursor: "pointer",
  fontSize: 14,
  padding: "7px 18px",
  minWidth: 90,
  color: "#333",
};

const quickActionBtnStyle = {
  padding: "12px 20px",
  border: "none",
  borderRadius: 8,
  cursor: "pointer",
  fontSize: 14,
  fontWeight: 600,
  minWidth: 140,
  transition: "all 0.2s ease",
  boxShadow: "0 2px 4px rgba(0,0,0,0.1)"
};
