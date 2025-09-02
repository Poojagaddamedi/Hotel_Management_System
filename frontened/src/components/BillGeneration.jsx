import React, { useState } from "react";
import hotelAPI from "../services/hotelAPI";

export default function BillGeneration() {
  const [folioNumber, setFolioNumber] = useState("");
  const [sms, setSms] = useState(true);
  const [email, setEmail] = useState(true);
  const [billData, setBillData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(null);

  const loadBillData = async () => {
    if (!folioNumber.trim()) {
      setError("Please enter a folio number");
      return;
    }

    try {
      setLoading(true);
      setError(null);

      // Load bill summary from backend
      const result = await hotelAPI.getBillSummary(folioNumber);
      
      if (result.success) {
        setBillData(result.data);
      } else {
        setError("Failed to load bill data: " + result.error);
      }
    } catch (err) {
      setError("Error loading bill data: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  const generateBill = async () => {
    if (!billData) {
      setError("Please load bill data first");
      return;
    }

    try {
      setLoading(true);
      setError(null);

      const billOptions = {
        sendSms: sms,
        sendEmail: email,
        finalizeCheckout: true
      };

      const result = await hotelAPI.generateFinalBill(folioNumber, billOptions);
      
      if (result.success) {
        setSuccess("Bill generated successfully! Bill Number: " + result.data.billNo);
        // Clear form after successful generation
        setTimeout(() => {
          setFolioNumber("");
          setBillData(null);
          setSuccess(null);
        }, 3000);
      } else {
        setError("Failed to generate bill: " + result.error);
      }
    } catch (err) {
      setError("Error generating bill: " + err.message);
    } finally {
      setLoading(false);
    }
  };

  // Calculate totals
  const totalExpenses = billData?.expenses?.reduce((sum, e) => sum + (e.amount || 0), 0) || 0;
  const totalAdvances = billData?.advances?.reduce((sum, a) => sum + (a.amount || 0), 0) || 0;
  const balance = totalExpenses - totalAdvances;

  return (
    <div style={{ maxWidth: 720, margin: "0 auto", fontSize: "14px" }}>
      <h2 style={{ fontSize: "18px", marginBottom: 22 }}>Generate Guest Bill</h2>
      <div style={fieldStyle}>
        <label style={labelStyle}>Folio Number</label>
        <input
          value={folioNumber}
          onChange={(e) => setFolioNumber(e.target.value)}
          style={inputStyle}
          placeholder="Enter Folio Number"
        />
      </div>
      <div style={{ marginTop: 16 }}>
        <label style={{ ...labelStyle, fontWeight: 600 }}>Guest Details</label>
        <div>{billData?.guest?.name || billData?.guest?.guestName || "N/A"}</div>
        <div>{billData?.guest?.mobile || billData?.guest?.contactNo || "N/A"}</div>
        <div>
          Room: {billData?.guest?.roomNumber || billData?.guest?.roomNo || "N/A"} (
          {billData?.guest?.checkIn || billData?.guest?.checkInDate || "N/A"} - {billData?.guest?.checkOut || billData?.guest?.checkOutDate || "N/A"})
        </div>
      </div>
      <div style={{ marginTop: 22 }}>
        <label style={{ ...labelStyle, fontWeight: 600 }}>Expenses</label>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ background: "#f7f7fc" }}>
              <th style={thStyle}>Account Head</th>
              <th style={thStyle}>Amount</th>
            </tr>
          </thead>
          <tbody>
            {billData?.expenses?.map((e, idx) => (
              <tr key={idx}>
                <td style={tdStyle}>{e.account || e.accountHead || e.description || "N/A"}</td>
                <td style={tdStyle}>₹ {(e.amount || 0).toFixed(2)}</td>
              </tr>
            )) || []}
            <tr style={{ fontWeight: "bold", borderTop: "2px solid #ccc" }}>
              <td style={{ ...tdStyle, fontWeight: "bold" }}>Total</td>
              <td style={tdStyle}>₹ {totalExpenses.toFixed(2)}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div style={{ marginTop: 22 }}>
        <label style={{ ...labelStyle, fontWeight: 600 }}>Advances</label>
        <table style={{ width: "100%", borderCollapse: "collapse" }}>
          <thead>
            <tr style={{ background: "#f7f7fc" }}>
              <th style={thStyle}>Receipt Number</th>
              <th style={thStyle}>Mode of Payment</th>
              <th style={thStyle}>Amount</th>
            </tr>
          </thead>
          <tbody>
            {billData?.advances?.map((a, idx) => (
              <tr key={idx}>
                <td style={tdStyle}>{a.receipt || a.receiptNo || `ADV${idx + 1}`}</td>
                <td style={tdStyle}>{a.mode || a.paymentMethod || "Cash"}</td>
                <td style={tdStyle}>₹ {(a.amount || 0).toFixed(2)}</td>
              </tr>
            )) || []}
            <tr style={{ fontWeight: "bold", borderTop: "2px solid #ccc" }}>
              <td colSpan={2} style={{ ...tdStyle, fontWeight: "bold" }}>
                Total Advances
              </td>
              <td style={tdStyle}>₹ {totalAdvances.toFixed(2)}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div style={{ marginTop: 22, fontWeight: 600, fontSize: "16px" }}>
        Balance Amount: ₹ {balance.toFixed(2)}
      </div>
      <div style={{ marginTop: 22, display: "flex", gap: 14 }}>
        <label style={{ display: "flex", alignItems: "center", gap: 6 }}>
          <input
            type="checkbox"
            checked={sms}
            onChange={(e) => setSms(e.target.checked)}
          />
          Send SMS
        </label>
        <label style={{ display: "flex", alignItems: "center", gap: 6 }}>
          <input
            type="checkbox"
            checked={email}
            onChange={(e) => setEmail(e.target.checked)}
          />
          Send Email
        </label>
      </div>
      <button style={saveButtonStyle} onClick={() => alert("Bill generated!")}>
        Generate Bill
      </button>
    </div>
  );
}

// Reuse styles for form fields
const fieldStyle = { display: "flex", flexDirection: "column", marginBottom: 10 };

const labelStyle = { fontSize: "13px", marginBottom: 4 };

const inputStyle = {
  fontSize: "14px",
  padding: "7px 9px",
  borderRadius: "6px",
  border: "1px solid #e0e0e0",
  background: "#fff",
};

const thStyle = {
  textAlign: "left",
  padding: "8px 12px",
  borderBottom: "1px solid #bbb",
  fontWeight: 600,
  fontSize: "14px",
};

const tdStyle = {
  padding: "8px 12px",
  borderBottom: "1px solid #ddd",
  fontSize: "14px",
};

const saveButtonStyle = {
  marginTop: 18,
  background: "#5754e8",
  color: "#fff",
  border: "none",
  borderRadius: "6px",
  padding: "10px 24px",
  fontSize: "15px",
  fontWeight: 600,
  cursor: "pointer",
};
