import React, { useState } from "react";
import { PaymentAPI } from "../services/hotelAPI";

export default function PaymentWorkflowForm() {
  const [scenario, setScenario] = useState("pre-checkin");
  const [form, setForm] = useState({
    reservationNo: "",
    folioNo: "",
    guestName: "",
    paymentMode: "Cash",
    amount: "",
    remarks: "",
    userId: 1 // This should come from authentication
  });
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleScenarioChange(newScenario) {
    setScenario(newScenario);
    // Clear form when scenario changes
    setForm({
      reservationNo: "",
      folioNo: "",
      guestName: "",
      paymentMode: "Cash",
      amount: "",
      remarks: "",
      userId: 1
    });
    setResult(null);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setLoading(true);
    setResult(null);

    try {
      let result;
      let payload = {
        paymentMode: form.paymentMode,
        amount: parseFloat(form.amount),
        remarks: form.remarks,
        userId: form.userId
      };

      switch (scenario) {
        case "pre-checkin":
          payload = {
            ...payload,
            reservationNo: form.reservationNo,
            guestName: form.guestName
          };
          result = await PaymentAPI.createPreCheckinAdvance(payload);
          break;
        case "post-checkin":
          payload = {
            ...payload,
            folioNo: form.folioNo
          };
          result = await PaymentAPI.createPostCheckinAdvance(payload);
          break;
        case "walk-in":
          payload = {
            ...payload,
            guestName: form.guestName,
            folioNo: form.folioNo
          };
          result = await PaymentAPI.createWalkInAdvance(payload);
          break;
        default:
          throw new Error("Invalid scenario");
      }

      if (result.success) {
        setResult({
          success: true,
          message: result.data.message || "Payment recorded successfully",
          data: result.data
        });
        // Clear form on success
        setForm({
          reservationNo: "",
          folioNo: "",
          guestName: "",
          paymentMode: "Cash",
          amount: "",
          remarks: "",
          userId: 1
        });
      } else {
        setResult({
          success: false,
          message: result.error || "Payment failed",
          error: result.error
        });
      }
    } catch (error) {
      setResult({
        success: false,
        message: "Network error occurred",
        error: error.message
      });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ display: "flex", gap: 24, minWidth: 0, alignItems: "flex-start" }}>
      {/* Left: Form */}
      <form style={{ flex: "1 1 380px", minWidth: 0 }} onSubmit={handleSubmit}>
        <h2 style={{ fontSize: "18px", margin: "0 0 14px 0" }}>
          Payment Workflow - Record Advance
        </h2>

        {/* Scenario Selection */}
        <div style={{ marginBottom: 20 }}>
          <label style={labelStyle}>Payment Scenario</label>
          <div style={{ display: "flex", gap: 10, marginBottom: 10 }}>
            {[
              { value: "pre-checkin", label: "Pre-Checkin (Reservation Only)" },
              { value: "post-checkin", label: "Post-Checkin (Folio + Reservation)" },
              { value: "walk-in", label: "Walk-In (Folio Only)" }
            ].map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => handleScenarioChange(option.value)}
                style={{
                  padding: "8px 12px",
                  fontSize: "12px",
                  borderRadius: "6px",
                  border: "1px solid #e0e0e0",
                  background: scenario === option.value ? "#5754e8" : "#fff",
                  color: scenario === option.value ? "#fff" : "#666",
                  cursor: "pointer",
                  fontWeight: scenario === option.value ? "600" : "400"
                }}
              >
                {option.label}
              </button>
            ))}
          </div>
        </div>

        <div style={gridStyle}>
          {/* Conditional Fields Based on Scenario */}
          {scenario === "pre-checkin" && (
            <>
              <div style={fieldStyle}>
                <label style={labelStyle}>Reservation Number *</label>
                <input
                  name="reservationNo"
                  value={form.reservationNo}
                  onChange={handleChange}
                  style={inputStyle}
                  placeholder="Enter reservation number"
                  required
                />
              </div>
              <div style={fieldStyle}>
                <label style={labelStyle}>Guest Name *</label>
                <input
                  name="guestName"
                  value={form.guestName}
                  onChange={handleChange}
                  style={inputStyle}
                  placeholder="Enter guest name"
                  required
                />
              </div>
            </>
          )}

          {scenario === "post-checkin" && (
            <div style={fieldStyle}>
              <label style={labelStyle}>Folio Number *</label>
              <input
                name="folioNo"
                value={form.folioNo}
                onChange={handleChange}
                style={inputStyle}
                placeholder="Enter folio number"
                required
              />
            </div>
          )}

          {scenario === "walk-in" && (
            <>
              <div style={fieldStyle}>
                <label style={labelStyle}>Guest Name *</label>
                <input
                  name="guestName"
                  value={form.guestName}
                  onChange={handleChange}
                  style={inputStyle}
                  placeholder="Enter guest name"
                  required
                />
              </div>
              <div style={fieldStyle}>
                <label style={labelStyle}>Folio Number (Optional)</label>
                <input
                  name="folioNo"
                  value={form.folioNo}
                  onChange={handleChange}
                  style={inputStyle}
                  placeholder="Auto-generated if empty"
                />
              </div>
            </>
          )}

          <div style={fieldStyle}>
            <label style={labelStyle}>Payment Mode *</label>
            <select
              name="paymentMode"
              value={form.paymentMode}
              onChange={handleChange}
              style={inputStyle}
              required
            >
              <option value="Cash">Cash</option>
              <option value="Credit Card">Credit Card</option>
              <option value="Debit Card">Debit Card</option>
              <option value="UPI">UPI</option>
              <option value="Bank Transfer">Bank Transfer</option>
              <option value="Cheque">Cheque</option>
            </select>
          </div>

          <div style={fieldStyle}>
            <label style={labelStyle}>Amount *</label>
            <input
              name="amount"
              type="number"
              step="0.01"
              value={form.amount}
              onChange={handleChange}
              style={inputStyle}
              placeholder="0.00"
              required
            />
          </div>
        </div>

        <div style={{ marginTop: 12 }}>
          <label style={labelStyle}>Remarks</label>
          <textarea
            name="remarks"
            value={form.remarks}
            onChange={handleChange}
            style={{ ...inputStyle, height: "60px" }}
            placeholder="Payment remarks..."
          />
        </div>

        {/* Result Display */}
        {result && (
          <div style={{
            marginTop: 15,
            padding: "12px",
            borderRadius: "6px",
            background: result.success ? "#d4edda" : "#f8d7da",
            border: `1px solid ${result.success ? "#c3e6cb" : "#f5c6cb"}`,
            color: result.success ? "#155724" : "#721c24"
          }}>
            <strong>{result.success ? "Success!" : "Error:"}</strong> {result.message}
            {result.data && result.data.advanceId && (
              <div style={{ marginTop: 5, fontSize: "13px" }}>
                Advance ID: {result.data.advanceId}
              </div>
            )}
          </div>
        )}

        <div style={{ display: "flex", gap: 12, justifyContent: "flex-end", marginTop: 18 }}>
          <button
            type="submit"
            disabled={loading}
            style={{
              ...saveButtonStyle,
              opacity: loading ? 0.6 : 1,
              cursor: loading ? "not-allowed" : "pointer"
            }}
          >
            {loading ? "Processing..." : "Record Advance"}
          </button>
        </div>
      </form>

      {/* Right: Scenario Info */}
      <div style={{
        flex: "0 0 280px",
        background: "#fafafb",
        borderRadius: 12,
        padding: "20px 15px",
        minWidth: 240,
        maxWidth: 280,
        fontSize: "14px",
      }}>
        <h3 style={{ marginTop: 0, fontSize: "16px" }}>
          {scenario === "pre-checkin" && "Pre-Checkin Advance"}
          {scenario === "post-checkin" && "Post-Checkin Advance"}
          {scenario === "walk-in" && "Walk-In Advance"}
        </h3>
        
        <div style={{ marginBottom: 15, color: "#666", lineHeight: 1.5 }}>
          {scenario === "pre-checkin" && (
            <>
              <strong>For guests with reservations before check-in:</strong>
              <ul style={{ margin: "8px 0", paddingLeft: "16px" }}>
                <li>Requires reservation number</li>
                <li>Guest name mandatory</li>
                <li>Payment recorded against reservation</li>
                <li>Will link to folio upon check-in</li>
              </ul>
            </>
          )}
          
          {scenario === "post-checkin" && (
            <>
              <strong>For guests already checked in:</strong>
              <ul style={{ margin: "8px 0", paddingLeft: "16px" }}>
                <li>Requires folio number</li>
                <li>Guest info auto-populated</li>
                <li>Payment recorded against folio</li>
                <li>Linked to existing reservation</li>
              </ul>
            </>
          )}
          
          {scenario === "walk-in" && (
            <>
              <strong>For walk-in guests without reservations:</strong>
              <ul style={{ margin: "8px 0", paddingLeft: "16px" }}>
                <li>No reservation required</li>
                <li>Guest name mandatory</li>
                <li>Folio auto-generated if not provided</li>
                <li>Direct payment to folio</li>
              </ul>
            </>
          )}
        </div>

        <div style={{ 
          padding: "10px", 
          background: "#e8f4fd", 
          borderRadius: "6px",
          border: "1px solid #bee5eb"
        }}>
          <div style={{ fontWeight: "600", marginBottom: "5px" }}>
            💡 Quick Tip
          </div>
          <div style={{ fontSize: "12px", color: "#0c5460" }}>
            All payments are individually tracked and can be viewed in the guest journey report.
          </div>
        </div>
      </div>
    </div>
  );
}

const gridStyle = {
  display: "grid",
  gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
  gap: "12px 10px",
  marginBottom: 6,
};

const fieldStyle = { display: "flex", flexDirection: "column" };

const labelStyle = { 
  fontSize: "13px", 
  marginBottom: 4, 
  fontWeight: "500",
  color: "#333"
};

const inputStyle = {
  fontSize: "14px",
  padding: "8px 10px",
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
  padding: "10px 20px",
  fontSize: "14px",
  fontWeight: 600,
  cursor: "pointer",
  boxShadow: "0 2px 8px rgba(87, 84, 232, 0.2)"
};
