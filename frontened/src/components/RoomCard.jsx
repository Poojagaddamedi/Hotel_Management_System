export default function RoomCard({ room, onViewDetails, onCheckIn }) {
  const statusColors = {
    Occupied: "#ffe5e5",
    Vacant: "#e7ffe5",
    Blocked: "#f0e5ff",
  };
  const badge = (status) =>
    ({
      Occupied: (
        <span
          style={{
            color: "#d32f2f",
            border: "1px solid #d32f2f",
            borderRadius: "8px",
            padding: "2px 8px",
            fontSize: "12px",
            marginLeft: "6px",
            fontWeight: 600,
          }}
        >
          Occupied
        </span>
      ),
      Vacant: (
        <span
          style={{
            color: "#388e3c",
            border: "1px solid #388e3c",
            borderRadius: "8px",
            padding: "2px 8px",
            fontSize: "12px",
            marginLeft: "6px",
            fontWeight: 600,
          }}
        >
          Vacant
        </span>
      ),
      Blocked: (
        <span
          style={{
            color: "#7b1fa2",
            border: "1px solid #7b1fa2",
            borderRadius: "8px",
            padding: "2px 8px",
            fontSize: "12px",
            marginLeft: "6px",
            fontWeight: 600,
          }}
        >
          Blocked
        </span>
      ),
    }[status]);

  return (
    <div
      style={{
        background: statusColors[room.status] || "#fff",
        borderRadius: 14,
        padding: 16,
        minWidth: 220,
        minHeight: 180,
        marginBottom: 20,
        display: "flex",
        flexDirection: "column",
        boxShadow: "0 2px 8px rgba(0,0,0,0.1)",
        fontSize: "14px",
        transition: "box-shadow 0.3s ease",
        cursor: "default",
      }}
      onMouseEnter={(e) => (e.currentTarget.style.boxShadow = "0 6px 16px rgba(0,0,0,0.15)")}
      onMouseLeave={(e) => (e.currentTarget.style.boxShadow = "0 2px 8px rgba(0,0,0,0.1)")}
    >
      <div style={{ fontWeight: "bold", fontSize: "18px", marginBottom: 8 }}>
        Room {room.number} {badge(room.status)}
      </div>
      {room.guest ? (
        <div style={{ flex: 1 }}>
          <div>
            Guest: <strong>{room.guest}</strong>
          </div>
          <div style={{ fontSize: "13px", marginTop: 4 }}>
            Check-in: {room.checkIn}
          </div>
          <div style={{ fontSize: "13px", marginTop: 2 }}>
            Check-out: {room.checkOut}
          </div>
        </div>
      ) : (
        <div style={{ flex: 1, color: "#666", fontStyle: "italic" }}>
          No guest checked in.
        </div>
      )}
      <div style={{ marginTop: 16 }}>
        {room.guest ? (
          <button
            onClick={() => onViewDetails(room)}
            style={buttonStyle}
            aria-label={`View details of room ${room.number}`}
          >
            View Details
          </button>
        ) : (
          <button
            onClick={() => onCheckIn(room)}
            style={buttonStyle}
            aria-label={`Check in guest to room ${room.number}`}
          >
            Check-In Guest
          </button>
        )}
      </div>
    </div>
  );
}

const buttonStyle = {
  backgroundColor: "#5754e8",
  border: "none",
  color: "#fff",
  fontWeight: 600,
  padding: "9px 18px",
  borderRadius: 8,
  fontSize: 14,
  cursor: "pointer",
  width: "100%",
  transition: "background-color 0.3s ease",
};

