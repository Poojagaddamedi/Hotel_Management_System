import React, { useState, useEffect } from "react";
import hotelAPI from "../services/hotelAPI";

export default function HousekeepingPanel() {
  const [rooms, setRooms] = useState([]);
  const [assignments, setAssignments] = useState([]);
  const [maintenanceRequests, setMaintenanceRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("rooms");
  const [filter, setFilter] = useState("All");
  const [editingRoom, setEditingRoom] = useState(null);
  const [editedState, setEditedState] = useState("");

  useEffect(() => {
    loadHousekeepingData();
  }, []);

  const loadHousekeepingData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Try to load housekeeping rooms
      try {
        const roomsResult = await hotelAPI.getHousekeepingRooms();
        if (roomsResult && roomsResult.success && roomsResult.data) {
          setRooms(roomsResult.data);
        } else {
          throw new Error("Housekeeping rooms API failed");
        }
      } catch (err) {
        console.warn("Failed to load housekeeping rooms, using demo data");
        setRooms(generateDemoRooms());
        setError("Using demo data - Backend API not responding");
      }

      // Try to load assignments
      try {
        const assignmentsResult = await hotelAPI.getHousekeepingAssignments();
        if (assignmentsResult && assignmentsResult.success) {
          setAssignments(assignmentsResult.data || []);
        } else {
          setAssignments(generateDemoAssignments());
        }
      } catch (err) {
        setAssignments(generateDemoAssignments());
      }

      // Try to load maintenance requests
      try {
        const maintenanceResult = await hotelAPI.getMaintenanceRequests();
        if (maintenanceResult && maintenanceResult.success) {
          setMaintenanceRequests(maintenanceResult.data || []);
        } else {
          setMaintenanceRequests(generateDemoMaintenance());
        }
      } catch (err) {
        setMaintenanceRequests(generateDemoMaintenance());
      }

    } catch (err) {
      console.error("HousekeepingPanel loadHousekeepingData error:", err);
      setRooms(generateDemoRooms());
      setAssignments(generateDemoAssignments());
      setMaintenanceRequests(generateDemoMaintenance());
      setError("Backend connection failed - Using demo data");
    } finally {
      setLoading(false);
    }
  };

  const generateDemoRooms = () => {
    return [
      { roomNo: "101", status: "CLEAN", lastCleaned: "2025-08-30", staff: "Alice" },
      { roomNo: "102", status: "DIRTY", lastCleaned: "2025-08-29", staff: "Bob" },
      { roomNo: "103", status: "MAINTENANCE", lastCleaned: "2025-08-28", staff: null },
      { roomNo: "201", status: "CLEAN", lastCleaned: "2025-08-30", staff: "Alice" },
      { roomNo: "202", status: "DIRTY", lastCleaned: "2025-08-29", staff: "Carol" }
    ];
  };

  const generateDemoAssignments = () => {
    return [
      { id: 1, staff: "Alice", rooms: ["101", "201"], shift: "Morning", date: "2025-08-31" },
      { id: 2, staff: "Bob", rooms: ["102"], shift: "Afternoon", date: "2025-08-31" },
      { id: 3, staff: "Carol", rooms: ["202"], shift: "Morning", date: "2025-08-31" }
    ];
  };

  const generateDemoMaintenance = () => {
    return [
      { id: 1, roomNo: "103", description: "AC not working", priority: "HIGH", status: "PENDING", date: "2025-08-30" },
      { id: 2, roomNo: "205", description: "Bathroom leak", priority: "MEDIUM", status: "IN_PROGRESS", date: "2025-08-29" }
    ];
  };

  const updateRoomStatus = async (roomNo, status) => {
    try {
      const result = await hotelAPI.updateHousekeepingStatus(roomNo, status);
      if (result.success) {
        loadHousekeepingData();
      } else {
        alert("Failed to update room status: " + result.error);
      }
    } catch (err) {
      alert("Error updating room status: " + err.message);
    }
  };

  const createMaintenanceRequest = async (roomNo) => {
    const description = prompt("Enter maintenance description:");
    if (!description) return;

    try {
      const data = {
        roomNo,
        description,
        priority: "MEDIUM",
        status: "PENDING"
      };
      
      const result = await hotelAPI.createMaintenanceRequest(data);
      if (result.success) {
        loadHousekeepingData();
        alert("Maintenance request created successfully!");
      } else {
        alert("Failed to create maintenance request: " + result.error);
      }
    } catch (err) {
      alert("Error creating maintenance request: " + err.message);
    }
  };

  function handleFilterChange(e) {
    setFilter(e.target.value);
  }

  function startEdit(roomNumber, currentState) {
    setEditingRoom(roomNumber);
    setEditedState(currentState);
  }

  function cancelEdit() {
    setEditingRoom(null);
    setEditedState("");
  }

  function handleStateChange(e) {
    setEditedState(e.target.value);
  }

  async function saveEdit(roomNumber) {
    try {
      await updateRoomStatus(roomNumber, editedState);
      setEditingRoom(null);
      setEditedState("");
    } catch (err) {
      alert("Error saving changes: " + err.message);
    }
  }

  if (loading) {
    return <div style={{ padding: 20, textAlign: "center" }}>Loading housekeeping data...</div>;
  }

  if (error) {
    return (
      <div style={{ padding: 20, textAlign: "center", color: "red" }}>
        <p>Error: {error}</p>
        <button onClick={loadHousekeepingData}>Retry</button>
      </div>
    );
  }

  const filteredRooms = Array.isArray(rooms) 
    ? (filter === "All" ? rooms : rooms.filter(room => room.state === filter || room.status === filter))
    : [];

  // Safety check to ensure filteredRooms is always an array
  const safeFilteredRooms = filteredRooms || [];

  return (
    <div style={{
      maxWidth: 700,
      margin: "40px auto",
      padding: 20,
      border: "1px solid #ddd",
      borderRadius: 12,
      backgroundColor: "#fff",
      boxShadow: "0 2px 8px rgb(0 0 0 / 0.1)",
      fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif"
    }}>
      <h2 style={{ textAlign: "center", marginBottom: 20, color: "#444" }}>Housekeeping Panel</h2>
      
      {/* Tab Navigation */}
      <div style={{ marginBottom: 20, borderBottom: "1px solid #ccc" }}>
        <button 
          onClick={() => setActiveTab("rooms")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "rooms" ? "#635bd2" : "transparent",
            color: activeTab === "rooms" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Room Status
        </button>
        <button 
          onClick={() => setActiveTab("assignments")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "assignments" ? "#635bd2" : "transparent",
            color: activeTab === "assignments" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Assignments
        </button>
        <button 
          onClick={() => setActiveTab("maintenance")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "maintenance" ? "#635bd2" : "transparent",
            color: activeTab === "maintenance" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Maintenance
        </button>
      </div>

      {/* Room Status Tab */}
      {activeTab === "rooms" && (
        <div>
          <label htmlFor="filterSelect" style={{ fontWeight: "600", marginRight: 10 }}>
            Filter by State:
          </label>
          <select
            id="filterSelect"
            value={filter}
            onChange={handleFilterChange}
            style={{
              padding: "6px 12px",
              borderRadius: 6,
              border: "1px solid #ccc",
              fontSize: 14,
              marginBottom: 20,
              minWidth: 140
            }}
          >
            <option value="All">All</option>
            <option value="Maintenance">Maintenance</option>
            <option value="Dirty">Dirty</option>
            <option value="Cleaned">Cleaned</option>
          </select>

          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f5f5f5" }}>
                <th style={thStyle}>Room Number</th>
                <th style={thStyle}>Status</th>
                <th style={thStyle}>Current State</th>
                <th style={thStyle}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {safeFilteredRooms.map(room => (
                <tr key={room.number || room.roomNo} style={{ borderBottom: "1px solid #eee" }}>
                  <td style={tdStyle}>{room.number || room.roomNo}</td>
                  <td style={tdStyle}>{room.status}</td>
                  <td style={tdStyle}>
                    {editingRoom === (room.number || room.roomNo) ? (
                      <select
                        value={editedState}
                        onChange={handleStateChange}
                        style={{
                          padding: "4px 8px",
                          borderRadius: 6,
                          border: "1px solid #ccc",
                          fontSize: 14
                        }}
                      >
                        <option value="Maintenance">Maintenance</option>
                        <option value="Dirty">Dirty</option>
                        <option value="Cleaned">Cleaned</option>
                      </select>
                    ) : (
                      room.state
                    )}
                  </td>
                  <td style={tdStyle}>
                    {editingRoom === (room.number || room.roomNo) ? (
                      <>
                        <button
                          onClick={() => saveEdit(room.number || room.roomNo)}
                          style={buttonStyleSave}
                        >
                          Save
                        </button>
                        <button
                          onClick={cancelEdit}
                          style={buttonStyleCancel}
                        >
                          Cancel
                        </button>
                      </>
                    ) : (
                      <>
                        <button
                          onClick={() => startEdit(room.number || room.roomNo, room.state)}
                          style={buttonStyleEdit}
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => createMaintenanceRequest(room.number || room.roomNo)}
                          style={{...buttonStyleEdit, backgroundColor: "#ff9800", color: "white", marginLeft: 8}}
                        >
                          Maintenance
                        </button>
                      </>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Assignments Tab */}
      {activeTab === "assignments" && (
        <div>
          <h3>Staff Assignments</h3>
          {assignments.length === 0 ? (
            <p>No assignments found</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ backgroundColor: "#f8f9fa" }}>
                  <th style={thStyle}>Staff</th>
                  <th style={thStyle}>Room</th>
                  <th style={thStyle}>Task</th>
                  <th style={thStyle}>Status</th>
                </tr>
              </thead>
              <tbody>
                {assignments.map((assignment, index) => (
                  <tr key={index}>
                    <td style={tdStyle}>{assignment.staffName}</td>
                    <td style={tdStyle}>{assignment.roomNo}</td>
                    <td style={tdStyle}>{assignment.task}</td>
                    <td style={tdStyle}>{assignment.status}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Maintenance Tab */}
      {activeTab === "maintenance" && (
        <div>
          <h3>Maintenance Requests</h3>
          {maintenanceRequests.length === 0 ? (
            <p>No maintenance requests</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ backgroundColor: "#f8f9fa" }}>
                  <th style={thStyle}>Room</th>
                  <th style={thStyle}>Description</th>
                  <th style={thStyle}>Priority</th>
                  <th style={thStyle}>Status</th>
                  <th style={thStyle}>Date</th>
                </tr>
              </thead>
              <tbody>
                {maintenanceRequests.map((request, index) => (
                  <tr key={index}>
                    <td style={tdStyle}>{request.roomNo}</td>
                    <td style={tdStyle}>{request.description}</td>
                    <td style={tdStyle}>{request.priority}</td>
                    <td style={tdStyle}>{request.status}</td>
                    <td style={tdStyle}>{request.createdDate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      <button
        onClick={loadHousekeepingData}
        style={{
          marginTop: 20,
          backgroundColor: "#635bd2",
          color: "#fff",
          border: "none",
          borderRadius: 8,
          padding: "10px 20px",
          fontWeight: "600",
          cursor: "pointer",
          float: "right"
        }}
      >
        Refresh Data
      </button>
    </div>
  );
}

// Styles
const thStyle = {
  padding: "12px 8px",
  textAlign: "left",
  fontWeight: "600",
  fontSize: 14,
  color: "#555"
};

const tdStyle = {
  padding: "12px 8px",
  fontSize: 14,
  color: "#333"
};

const buttonStyleEdit = {
  backgroundColor: "#f0f0f0",
  border: "1px solid #ccc",
  borderRadius: 6,
  padding: "6px 12px",
  cursor: "pointer",
  fontWeight: "600",
  marginRight: 8
};

const buttonStyleSave = {
  backgroundColor: "#4caf50",
  color: "#fff",
  border: "none",
  borderRadius: 6,
  padding: "6px 14px",
  fontWeight: "600",
  cursor: "pointer",
  marginRight: 8
};

const buttonStyleCancel = {
  backgroundColor: "#e53935",
  color: "#fff",
  border: "none",
  borderRadius: 6,
  padding: "6px 14px",
  fontWeight: "600",
  cursor: "pointer"
};
