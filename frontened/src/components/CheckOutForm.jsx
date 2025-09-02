import React, { useState, useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import hotelAPI from "../services/hotelAPI";

export default function CheckOutForm() {
  const { user } = useAuth();
  const [form, setForm] = useState({
    folioNo: "",
    checkoutDate: new Date().toISOString().split('T')[0],
    checkoutTime: new Date().toTimeString().split(' ')[0].slice(0, 5),
    finalAmount: "",
    paymentMethod: "CASH",
    paidAmount: "",
    balanceAmount: "",
    notes: ""
  });

  const [activeCheckins, setActiveCheckins] = useState([]);
  const [selectedCheckin, setSelectedCheckin] = useState(null);
  const [billSummary, setBillSummary] = useState(null);
  const [loading, setLoading] = useState(false);
  const [submitting, setSubmitting] = useState(false);
  const [errors, setErrors] = useState({});

  useEffect(() => {
    loadActiveCheckins();
  }, []);

  const loadActiveCheckins = async () => {
    try {
      setLoading(true);
      const result = await hotelAPI.getActiveCheckins();
      if (result.success) {
        setActiveCheckins(result.data);
      }
    } catch (error) {
      console.error("Error loading active check-ins:", error);
    } finally {
      setLoading(false);
    }
  };

  const loadBillSummary = async (folioNo) => {
    try {
      const result = await hotelAPI.getBillSummary(folioNo);
      if (result.success) {
        setBillSummary(result.data);
        setForm(prev => ({
          ...prev,
          finalAmount: result.data.totalAmount || "",
          balanceAmount: result.data.balanceAmount || ""
        }));
      }
    } catch (error) {
      console.error("Error loading bill summary:", error);
    }
  };

  const handleCheckinSelect = (checkin) => {
    setSelectedCheckin(checkin);
    setForm(prev => ({
      ...prev,
      folioNo: checkin.folioNo
    }));
    loadBillSummary(checkin.folioNo);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
    
    // Clear specific error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: "" }));
    }

    // Calculate balance when paid amount changes
    if (name === "paidAmount" && form.finalAmount) {
      const balance = parseFloat(form.finalAmount) - parseFloat(value || 0);
      setForm(prev => ({ ...prev, balanceAmount: balance.toFixed(2) }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!form.folioNo) newErrors.folioNo = "Please select a check-in to checkout";
    if (!form.checkoutDate) newErrors.checkoutDate = "Checkout date is required";
    if (!form.checkoutTime) newErrors.checkoutTime = "Checkout time is required";
    if (!form.paidAmount) newErrors.paidAmount = "Paid amount is required";

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    try {
      setSubmitting(true);

      const checkoutData = {
        folioNo: form.folioNo,
        checkoutDate: form.checkoutDate,
        checkoutTime: form.checkoutTime,
        finalAmount: parseFloat(form.finalAmount),
        paymentMethod: form.paymentMethod,
        paidAmount: parseFloat(form.paidAmount),
        balanceAmount: parseFloat(form.balanceAmount),
        notes: form.notes,
        checkedOutBy: user.id
      };

      const result = await hotelAPI.createCheckout(checkoutData);

      if (result.success) {
        alert("Guest checked out successfully!");
        // Reset form
        setForm({
          folioNo: "",
          checkoutDate: new Date().toISOString().split('T')[0],
          checkoutTime: new Date().toTimeString().split(' ')[0].slice(0, 5),
          finalAmount: "",
          paymentMethod: "CASH",
          paidAmount: "",
          balanceAmount: "",
          notes: ""
        });
        setSelectedCheckin(null);
        setBillSummary(null);
        loadActiveCheckins(); // Refresh the list
      } else {
        alert("Error during checkout: " + result.error);
      }
    } catch (error) {
      alert("Error during checkout: " + error.message);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto", padding: "20px" }}>
      <h2 style={{ marginBottom: "30px", color: "#333" }}>Guest Check-Out</h2>

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "30px" }}>
        {/* Active Check-ins List */}
        <div style={{ background: "#fff", padding: "20px", borderRadius: "8px", boxShadow: "0 2px 4px rgba(0,0,0,0.1)" }}>
          <h3 style={{ marginBottom: "20px", color: "#333" }}>Active Check-ins</h3>
          
          {loading ? (
            <p>Loading active check-ins...</p>
          ) : activeCheckins.length === 0 ? (
            <p>No active check-ins found.</p>
          ) : (
            <div style={{ maxHeight: "400px", overflowY: "auto" }}>
              {activeCheckins.map((checkin) => (
                <div
                  key={checkin.id}
                  style={{
                    padding: "15px",
                    border: "1px solid #ddd",
                    borderRadius: "6px",
                    marginBottom: "10px",
                    cursor: "pointer",
                    backgroundColor: selectedCheckin?.id === checkin.id ? "#e3f2fd" : "#f9f9f9"
                  }}
                  onClick={() => handleCheckinSelect(checkin)}
                >
                  <div style={{ fontWeight: "bold" }}>Folio: {checkin.folioNo}</div>
                  <div>Guest: {checkin.guestName}</div>
                  <div>Room: {checkin.roomNo}</div>
                  <div>Check-in: {new Date(checkin.checkinDate).toLocaleDateString()}</div>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Checkout Form */}
        <div style={{ background: "#fff", padding: "20px", borderRadius: "8px", boxShadow: "0 2px 4px rgba(0,0,0,0.1)" }}>
          <h3 style={{ marginBottom: "20px", color: "#333" }}>Checkout Details</h3>

          <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: "20px" }}>
              <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                Folio Number
              </label>
              <input
                type="text"
                name="folioNo"
                value={form.folioNo}
                readOnly
                style={{
                  width: "100%",
                  padding: "10px",
                  border: "1px solid #ddd",
                  borderRadius: "4px",
                  backgroundColor: "#f5f5f5"
                }}
                placeholder="Select a check-in from the left"
              />
              {errors.folioNo && <span style={{ color: "red", fontSize: "12px" }}>{errors.folioNo}</span>}
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "15px", marginBottom: "20px" }}>
              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Checkout Date
                </label>
                <input
                  type="date"
                  name="checkoutDate"
                  value={form.checkoutDate}
                  onChange={handleInputChange}
                  style={{ width: "100%", padding: "10px", border: "1px solid #ddd", borderRadius: "4px" }}
                />
                {errors.checkoutDate && <span style={{ color: "red", fontSize: "12px" }}>{errors.checkoutDate}</span>}
              </div>

              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Checkout Time
                </label>
                <input
                  type="time"
                  name="checkoutTime"
                  value={form.checkoutTime}
                  onChange={handleInputChange}
                  style={{ width: "100%", padding: "10px", border: "1px solid #ddd", borderRadius: "4px" }}
                />
                {errors.checkoutTime && <span style={{ color: "red", fontSize: "12px" }}>{errors.checkoutTime}</span>}
              </div>
            </div>

            {billSummary && (
              <div style={{ background: "#f0f8ff", padding: "15px", borderRadius: "6px", marginBottom: "20px" }}>
                <h4 style={{ margin: "0 0 10px 0", color: "#333" }}>Bill Summary</h4>
                <div>Room Charges: ₹{billSummary.roomCharges || 0}</div>
                <div>Service Charges: ₹{billSummary.serviceCharges || 0}</div>
                <div>Taxes: ₹{billSummary.taxes || 0}</div>
                <div style={{ fontWeight: "bold", borderTop: "1px solid #ddd", paddingTop: "10px", marginTop: "10px" }}>
                  Total Amount: ₹{billSummary.totalAmount || 0}
                </div>
              </div>
            )}

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "15px", marginBottom: "20px" }}>
              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Final Amount
                </label>
                <input
                  type="number"
                  name="finalAmount"
                  value={form.finalAmount}
                  onChange={handleInputChange}
                  step="0.01"
                  style={{ width: "100%", padding: "10px", border: "1px solid #ddd", borderRadius: "4px" }}
                />
              </div>

              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Payment Method
                </label>
                <select
                  name="paymentMethod"
                  value={form.paymentMethod}
                  onChange={handleInputChange}
                  style={{ width: "100%", padding: "10px", border: "1px solid #ddd", borderRadius: "4px" }}
                >
                  <option value="CASH">Cash</option>
                  <option value="CREDIT_CARD">Credit Card</option>
                  <option value="DEBIT_CARD">Debit Card</option>
                  <option value="UPI">UPI</option>
                  <option value="NET_BANKING">Net Banking</option>
                </select>
              </div>
            </div>

            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "15px", marginBottom: "20px" }}>
              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Paid Amount
                </label>
                <input
                  type="number"
                  name="paidAmount"
                  value={form.paidAmount}
                  onChange={handleInputChange}
                  step="0.01"
                  style={{ width: "100%", padding: "10px", border: "1px solid #ddd", borderRadius: "4px" }}
                />
                {errors.paidAmount && <span style={{ color: "red", fontSize: "12px" }}>{errors.paidAmount}</span>}
              </div>

              <div>
                <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                  Balance Amount
                </label>
                <input
                  type="number"
                  name="balanceAmount"
                  value={form.balanceAmount}
                  readOnly
                  style={{
                    width: "100%",
                    padding: "10px",
                    border: "1px solid #ddd",
                    borderRadius: "4px",
                    backgroundColor: "#f5f5f5"
                  }}
                />
              </div>
            </div>

            <div style={{ marginBottom: "20px" }}>
              <label style={{ display: "block", marginBottom: "5px", fontWeight: "bold" }}>
                Notes (Optional)
              </label>
              <textarea
                name="notes"
                value={form.notes}
                onChange={handleInputChange}
                rows="3"
                style={{
                  width: "100%",
                  padding: "10px",
                  border: "1px solid #ddd",
                  borderRadius: "4px",
                  resize: "vertical"
                }}
                placeholder="Any additional notes..."
              />
            </div>

            <button
              type="submit"
              disabled={submitting || !selectedCheckin}
              style={{
                width: "100%",
                padding: "12px",
                backgroundColor: submitting || !selectedCheckin ? "#ccc" : "#4CAF50",
                color: "white",
                border: "none",
                borderRadius: "4px",
                fontSize: "16px",
                cursor: submitting || !selectedCheckin ? "not-allowed" : "pointer"
              }}
            >
              {submitting ? "Processing Checkout..." : "Complete Checkout"}
            </button>
          </form>
        </div>
      </div>
    </div>
  );
}
