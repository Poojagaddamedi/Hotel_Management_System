import React, { useState, useEffect } from "react";
import hotelAPI from "../services/hotelAPI";

// Role-based API access definitions
const ROLE_APIS = {
  admin: {
    name: "Admin",
    color: "#dc3545",
    apis: [
      { endpoint: "/api/payment-workflow/scenarios", method: "GET", description: "View payment scenarios" },
      { endpoint: "/api/billing/complete/summary/TEST_FOLIO", method: "GET", description: "Complete bill summary" },
      { endpoint: "/api/billing/complete/guest-journey/TEST_GUEST?type=guest", method: "GET", description: "Guest journey tracking" },
      { endpoint: "/api/advances", method: "GET", description: "All advances" },
      { endpoint: "/api/reservations", method: "GET", description: "All reservations" },
      { endpoint: "/api/checkins", method: "GET", description: "All check-ins" },
      { endpoint: "/api/checkouts", method: "GET", description: "All check-outs" },
      { endpoint: "/api/post-transactions", method: "GET", description: "All transactions" }
    ]
  },
  manager: {
    name: "Manager",
    color: "#007bff",
    apis: [
      { endpoint: "/api/payment-workflow/scenarios", method: "GET", description: "View payment scenarios" },
      { endpoint: "/api/billing/complete/summary/TEST_FOLIO", method: "GET", description: "Complete bill summary" },
      { endpoint: "/api/advances", method: "GET", description: "All advances" },
      { endpoint: "/api/reservations", method: "GET", description: "All reservations" },
      { endpoint: "/api/checkins", method: "GET", description: "All check-ins" },
      { endpoint: "/api/post-transactions", method: "GET", description: "All transactions" }
    ]
  },
  accountant: {
    name: "Accountant",
    color: "#28a745",
    apis: [
      { endpoint: "/api/payment-workflow/pre-checkin-advance", method: "POST", description: "Record pre-checkin advance", requiresBody: true },
      { endpoint: "/api/payment-workflow/post-checkin-advance", method: "POST", description: "Record post-checkin advance", requiresBody: true },
      { endpoint: "/api/payment-workflow/walk-in-advance", method: "POST", description: "Record walk-in advance", requiresBody: true },
      { endpoint: "/api/billing/complete/summary/TEST_FOLIO", method: "GET", description: "Generate bill summary" },
      { endpoint: "/api/billing/complete/guest-journey/TEST_GUEST?type=guest", method: "GET", description: "Track guest payments" },
      { endpoint: "/api/advances", method: "GET", description: "View advances" }
    ]
  },
  receptionist: {
    name: "Receptionist",
    color: "#ffc107",
    apis: [
      { endpoint: "/api/payment-workflow/pre-checkin-advance", method: "POST", description: "Record pre-checkin advance", requiresBody: true },
      { endpoint: "/api/payment-workflow/post-checkin-advance", method: "POST", description: "Record post-checkin advance", requiresBody: true },
      { endpoint: "/api/payment-workflow/walk-in-advance", method: "POST", description: "Record walk-in advance", requiresBody: true },
      { endpoint: "/api/reservations", method: "GET", description: "View reservations" },
      { endpoint: "/api/checkins", method: "GET", description: "View check-ins" },
      { endpoint: "/api/checkouts", method: "GET", description: "Process check-outs" }
    ]
  },
  housekeeping: {
    name: "Housekeeping Staff",
    color: "#6f42c1",
    apis: [
      { endpoint: "/api/rooms", method: "GET", description: "View room status" },
      { endpoint: "/api/housekeeping/assignments", method: "GET", description: "Daily assignments" }
    ]
  }
};

const TEST_PAYLOADS = {
  "pre-checkin-advance": {
    reservationNo: "TEST_RES_" + Date.now(),
    guestName: "Test Guest",
    paymentMode: "Cash",
    amount: 1000.00,
    remarks: "Test pre-checkin advance",
    userId: 1
  },
  "post-checkin-advance": {
    folioNo: "TEST_FOL_" + Date.now(),
    paymentMode: "Credit Card",
    amount: 500.00,
    remarks: "Test post-checkin advance",
    userId: 1
  },
  "walk-in-advance": {
    guestName: "Walk-in Test Guest",
    paymentMode: "UPI",
    amount: 750.00,
    remarks: "Test walk-in advance",
    userId: 1,
    folioNo: "WI_" + Date.now()
  }
};

export default function RoleBasedAPITester() {
  const [selectedRole, setSelectedRole] = useState("admin");
  const [testResults, setTestResults] = useState({});
  const [testing, setTesting] = useState(false);
  const [logs, setLogs] = useState([]);

  const addLog = (message, type = "info") => {
    const timestamp = new Date().toLocaleTimeString();
    setLogs(prev => [...prev, { timestamp, message, type }]);
  };

  const testAPI = async (api) => {
    const testKey = `${selectedRole}_${api.endpoint}_${api.method}`;
    setTestResults(prev => ({ ...prev, [testKey]: { status: "testing" } }));

    try {
      addLog(`Testing ${api.method} ${api.endpoint} as ${selectedRole}`, "info");

      let result;
      if (api.method === "POST" && api.requiresBody) {
        const endpointKey = api.endpoint.split("/").pop().split("?")[0];
        const payload = TEST_PAYLOADS[endpointKey];
        
        if (payload) {
          if (endpointKey === "pre-checkin-advance") {
            result = await hotelAPI.createPreCheckinAdvance(payload);
          } else if (endpointKey === "post-checkin-advance") {
            result = await hotelAPI.createPostCheckinAdvance(payload);
          } else if (endpointKey === "walk-in-advance") {
            result = await hotelAPI.createWalkInAdvance(payload);
          } else {
            result = await hotelAPI.request(api.endpoint, {
              method: api.method,
              body: JSON.stringify(payload),
              headers: { "X-User-Role": selectedRole }
            });
          }
        } else {
          result = await hotelAPI.request(api.endpoint, {
            method: api.method,
            headers: { "X-User-Role": selectedRole }
          });
        }
      } else {
        result = await hotelAPI.request(api.endpoint, {
          method: api.method || "GET",
          headers: { "X-User-Role": selectedRole }
        });
      }

      if (result.success) {
        setTestResults(prev => ({
          ...prev,
          [testKey]: {
            status: "success",
            statusCode: 200,
            data: result.data
          }
        }));
        addLog(`✅ ${api.description} - SUCCESS`, "success");
      } else {
        setTestResults(prev => ({
          ...prev,
          [testKey]: {
            status: "error",
            statusCode: 400,
            error: result.error
          }
        }));
        addLog(`❌ ${api.description} - ERROR: ${result.error}`, "error");
      }
    } catch (error) {
      setTestResults(prev => ({
        ...prev,
        [testKey]: {
          status: "error",
          error: error.message
        }
      }));
      addLog(`❌ ${api.description} - NETWORK ERROR: ${error.message}`, "error");
    }
  };

  const testAllAPIsForRole = async () => {
    setTesting(true);
    setTestResults({});
    setLogs([]);
    
    addLog(`🚀 Starting API tests for ${ROLE_APIS[selectedRole].name}`, "info");

    const apis = ROLE_APIS[selectedRole].apis;
    for (const api of apis) {
      await testAPI(api);
      // Small delay between requests
      await new Promise(resolve => setTimeout(resolve, 500));
    }

    setTesting(false);
    addLog(`🏁 Completed API tests for ${ROLE_APIS[selectedRole].name}`, "info");
  };

  const getTestResultIcon = (testKey) => {
    const result = testResults[testKey];
    if (!result) return "⏳";
    if (result.status === "testing") return "🔄";
    if (result.status === "success") return "✅";
    return "❌";
  };

  const getTestResultStyle = (testKey) => {
    const result = testResults[testKey];
    if (!result) return { background: "#f8f9fa" };
    if (result.status === "testing") return { background: "#fff3cd", borderLeft: "4px solid #ffc107" };
    if (result.status === "success") return { background: "#d4edda", borderLeft: "4px solid #28a745" };
    return { background: "#f8d7da", borderLeft: "4px solid #dc3545" };
  };

  return (
    <div style={{ padding: "20px", maxWidth: "1200px", margin: "0 auto" }}>
      <h1 style={{ marginBottom: "30px", textAlign: "center" }}>
        🔐 Role-Based API Access Tester
      </h1>

      {/* Role Selection */}
      <div style={{ marginBottom: "30px", textAlign: "center" }}>
        <h3 style={{ marginBottom: "15px" }}>Select User Role</h3>
        <div style={{ display: "flex", justifyContent: "center", gap: "10px", flexWrap: "wrap" }}>
          {Object.entries(ROLE_APIS).map(([roleKey, roleData]) => (
            <button
              key={roleKey}
              onClick={() => setSelectedRole(roleKey)}
              style={{
                padding: "10px 20px",
                borderRadius: "8px",
                border: `2px solid ${roleData.color}`,
                background: selectedRole === roleKey ? roleData.color : "white",
                color: selectedRole === roleKey ? "white" : roleData.color,
                fontWeight: "600",
                cursor: "pointer",
                fontSize: "14px"
              }}
            >
              {roleData.name}
            </button>
          ))}
        </div>
      </div>

      {/* Test Button */}
      <div style={{ textAlign: "center", marginBottom: "30px" }}>
        <button
          onClick={testAllAPIsForRole}
          disabled={testing}
          style={{
            padding: "12px 30px",
            fontSize: "16px",
            fontWeight: "600",
            borderRadius: "8px",
            border: "none",
            background: testing ? "#6c757d" : "#007bff",
            color: "white",
            cursor: testing ? "not-allowed" : "pointer",
            boxShadow: "0 2px 8px rgba(0,123,255,0.3)"
          }}
        >
          {testing ? "Testing..." : `Test All APIs for ${ROLE_APIS[selectedRole].name}`}
        </button>
      </div>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "30px" }}>
        {/* API Test Results */}
        <div>
          <h3 style={{ marginBottom: "20px", color: ROLE_APIS[selectedRole].color }}>
            📋 {ROLE_APIS[selectedRole].name} API Access
          </h3>
          <div style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
            {ROLE_APIS[selectedRole].apis.map((api, index) => {
              const testKey = `${selectedRole}_${api.endpoint}_${api.method}`;
              return (
                <div
                  key={index}
                  style={{
                    ...getTestResultStyle(testKey),
                    padding: "15px",
                    borderRadius: "8px",
                    border: "1px solid #e0e0e0"
                  }}
                >
                  <div style={{ display: "flex", alignItems: "center", justifyContent: "space-between" }}>
                    <div>
                      <div style={{ fontWeight: "600", marginBottom: "5px" }}>
                        {getTestResultIcon(testKey)} {api.description}
                      </div>
                      <div style={{ fontSize: "12px", color: "#666", fontFamily: "monospace" }}>
                        {api.method} {api.endpoint}
                      </div>
                    </div>
                    <button
                      onClick={() => testAPI(api)}
                      disabled={testing}
                      style={{
                        padding: "5px 10px",
                        fontSize: "12px",
                        borderRadius: "4px",
                        border: "1px solid #ccc",
                        background: "white",
                        cursor: testing ? "not-allowed" : "pointer"
                      }}
                    >
                      Test
                    </button>
                  </div>
                  {testResults[testKey] && testResults[testKey].status !== "testing" && (
                    <div style={{ marginTop: "10px", fontSize: "12px" }}>
                      {testResults[testKey].statusCode && (
                        <div>Status: {testResults[testKey].statusCode}</div>
                      )}
                      {testResults[testKey].error && (
                        <div style={{ color: "#dc3545" }}>Error: {testResults[testKey].error}</div>
                      )}
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        </div>

        {/* Test Logs */}
        <div>
          <h3 style={{ marginBottom: "20px" }}>📝 Test Logs</h3>
          <div
            style={{
              height: "400px",
              overflowY: "auto",
              background: "#f8f9fa",
              border: "1px solid #e0e0e0",
              borderRadius: "8px",
              padding: "15px",
              fontFamily: "monospace",
              fontSize: "13px"
            }}
          >
            {logs.length === 0 ? (
              <div style={{ color: "#666", fontStyle: "italic" }}>
                No logs yet. Click "Test All APIs" to start testing.
              </div>
            ) : (
              logs.map((log, index) => (
                <div
                  key={index}
                  style={{
                    marginBottom: "5px",
                    color: log.type === "success" ? "#28a745" : 
                           log.type === "error" ? "#dc3545" : "#333"
                  }}
                >
                  [{log.timestamp}] {log.message}
                </div>
              ))
            )}
          </div>
        </div>
      </div>

      {/* Role Access Summary */}
      <div style={{ marginTop: "30px" }}>
        <h3>🛡️ Role Access Control Summary</h3>
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))", gap: "15px", marginTop: "20px" }}>
          {Object.entries(ROLE_APIS).map(([roleKey, roleData]) => (
            <div
              key={roleKey}
              style={{
                padding: "15px",
                border: `2px solid ${roleData.color}`,
                borderRadius: "8px",
                background: "white"
              }}
            >
              <h4 style={{ margin: "0 0 10px 0", color: roleData.color }}>
                {roleData.name}
              </h4>
              <div style={{ fontSize: "14px", color: "#666" }}>
                {roleData.apis.length} API endpoints accessible
              </div>
              <div style={{ fontSize: "12px", marginTop: "5px" }}>
                {roleData.apis.slice(0, 2).map(api => api.description).join(", ")}
                {roleData.apis.length > 2 && "..."}
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
