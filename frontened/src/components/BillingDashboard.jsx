import React, { useState, useEffect } from "react";
import hotelAPI from "../services/hotelAPI";
import { formatCurrency, generateFolioNumber } from "../utils/numberGenerators";

export default function BillingDashboard() {
  const [bills, setBills] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [taxRates, setTaxRates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("bills");
  const [selectedBill, setSelectedBill] = useState(null);
  const [showCreateBill, setShowCreateBill] = useState(false);
  const [newBill, setNewBill] = useState({
    guestId: "",
    roomNo: "",
    checkInDate: "",
    checkOutDate: "",
    services: []
  });

  useEffect(() => {
    loadBillingData();
  }, []);

  const loadBillingData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Try to load active checkins for billing
      try {
        const checkinsResult = await hotelAPI.getActiveCheckins();
        if (checkinsResult && checkinsResult.success && checkinsResult.data) {
          // Transform checkins to bills format
          const mockBills = checkinsResult.data.map((checkin, index) => ({
            billId: checkin.id || `BILL_${index + 1}`,
            id: checkin.id || index + 1,
            folioNo: checkin.folioNo || generateFolioNumber(),
            guestName: checkin.guestName || 'Guest',
            roomNo: checkin.roomNo || 'N/A',
            checkInDate: checkin.checkinDate || new Date().toISOString().split('T')[0],
            checkOutDate: checkin.expectedCheckoutDate || new Date().toISOString().split('T')[0],
            totalAmount: checkin.totalAmount || 0,
            status: 'PENDING'
          }));
          setBills(mockBills);
        } else {
          throw new Error("No active checkins");
        }
      } catch (err) {
        console.warn("Failed to load billing data, using demo data");
        setBills(generateDemoBills());
        setError("Using demo data - Backend API not responding");
      }

      // Load transactions
      try {
        const transactionsResult = await hotelAPI.getTransactions();
        if (transactionsResult && transactionsResult.success) {
          setTransactions(transactionsResult.data || []);
        } else {
          setTransactions(generateDemoTransactions());
        }
      } catch (err) {
        setTransactions(generateDemoTransactions());
      }

      // Set default payment methods and tax rates
      setPaymentMethods([
        { id: 1, name: "Cash", active: true },
        { id: 2, name: "Credit Card", active: true },
        { id: 3, name: "UPI", active: true },
        { id: 4, name: "Bank Transfer", active: true }
      ]);

      setTaxRates([
        { id: 1, name: "GST", rate: 18, active: true },
        { id: 2, name: "Service Tax", rate: 10, active: true }
      ]);

    } catch (err) {
      console.error("BillingDashboard loadBillingData error:", err);
      setBills(generateDemoBills());
      setTransactions(generateDemoTransactions());
      setError("Backend connection failed - Using demo data");
    } finally {
      setLoading(false);
    }
  };

  const generateDemoBills = () => {
    return [
      {
        id: 1,
        folioNo: "FOL001",
        guestName: "John Doe",
        roomNo: "101",
        checkInDate: "2025-08-30",
        checkOutDate: "2025-09-02",
        totalAmount: 15000,
        status: "PENDING"
      },
      {
        id: 2,
        folioNo: "FOL002",
        guestName: "Jane Smith",
        roomNo: "102",
        checkInDate: "2025-08-29",
        checkOutDate: "2025-09-01",
        totalAmount: 12000,
        status: "PAID"
      }
    ];
  };

  const generateDemoTransactions = () => {
    return [
      {
        id: 1,
        folioNo: "FOL001",
        amount: 5000,
        method: "Cash",
        date: "2025-08-30",
        type: "Advance"
      },
      {
        id: 2,
        folioNo: "FOL002",
        amount: 12000,
        method: "Credit Card",
        date: "2025-09-01",
        type: "Final Payment"
      }
    ];
  };

  const createNewBill = async (billData) => {
    try {
      const result = await hotelAPI.createBill(billData);
      if (result.success) {
        loadBillingData();
        setShowCreateBill(false);
        setNewBill({
          guestId: "",
          roomNo: "",
          checkInDate: "",
          checkOutDate: "",
          services: []
        });
        alert("Bill created successfully!");
      } else {
        alert("Failed to create bill: " + result.error);
      }
    } catch (err) {
      alert("Error creating bill: " + err.message);
    }
  };

  const processPayment = async (billId, paymentData) => {
    try {
      const result = await hotelAPI.processPayment(billId, paymentData);
      if (result.success) {
        loadBillingData();
        alert("Payment processed successfully!");
      } else {
        alert("Failed to process payment: " + result.error);
      }
    } catch (err) {
      alert("Error processing payment: " + err.message);
    }
  };

  const generateInvoice = async (billId) => {
    try {
      const result = await hotelAPI.generateInvoice(billId);
      if (result.success) {
        // Download or display invoice
        window.open(result.data.invoiceUrl, '_blank');
      } else {
        alert("Failed to generate invoice: " + result.error);
      }
    } catch (err) {
      alert("Error generating invoice: " + err.message);
    }
  };

  if (loading) {
    return <div style={{ padding: 20, textAlign: "center" }}>Loading billing data...</div>;
  }

  if (error) {
    return (
      <div style={{ padding: 20, textAlign: "center", color: "red" }}>
        <p>Error: {error}</p>
        <button onClick={loadBillingData}>Retry</button>
      </div>
    );
  }

  return (
    <div style={{ padding: 20 }}>
      <h2>Billing & Payment Dashboard</h2>
      
      {/* Tab Navigation */}
      <div style={{ marginBottom: 20, borderBottom: "1px solid #ccc" }}>
        <button 
          onClick={() => setActiveTab("bills")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "bills" ? "#5754e8" : "transparent",
            color: activeTab === "bills" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Bills & Invoices
        </button>
        <button 
          onClick={() => setActiveTab("transactions")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "transactions" ? "#5754e8" : "transparent",
            color: activeTab === "transactions" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Transactions
        </button>
        <button 
          onClick={() => setActiveTab("payments")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "payments" ? "#5754e8" : "transparent",
            color: activeTab === "payments" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Payment Methods
        </button>
        <button 
          onClick={() => setActiveTab("taxes")}
          style={{
            padding: "10px 20px",
            border: "none",
            background: activeTab === "taxes" ? "#5754e8" : "transparent",
            color: activeTab === "taxes" ? "white" : "black",
            cursor: "pointer"
          }}
        >
          Tax Configuration
        </button>
      </div>

      {/* Bills Tab */}
      {activeTab === "bills" && (
        <div>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
            <h3>Bills & Invoices</h3>
            <button 
              onClick={() => setShowCreateBill(true)}
              style={{
                padding: "10px 20px",
                backgroundColor: "#28a745",
                color: "white",
                border: "none",
                borderRadius: 4,
                cursor: "pointer"
              }}
            >
              Create New Bill
            </button>
          </div>

          {bills.length === 0 ? (
            <p>No bills found</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ backgroundColor: "#f8f9fa" }}>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Bill ID</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Guest</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Room</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Amount</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Status</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Actions</th>
                </tr>
              </thead>
              <tbody>
                {bills.map((bill) => (
                  <tr key={bill.billId}>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{bill.billId}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{bill.guestName}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{bill.roomNo}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{formatCurrency(bill.totalAmount)}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>
                      <span style={{
                        padding: "2px 8px",
                        borderRadius: 4,
                        backgroundColor: bill.status === "PAID" ? "#d4edda" : "#fff3cd",
                        color: bill.status === "PAID" ? "#155724" : "#856404"
                      }}>
                        {bill.status}
                      </span>
                    </td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>
                      <button 
                        onClick={() => setSelectedBill(bill)}
                        style={{ padding: "5px 10px", marginRight: 5, backgroundColor: "#007bff", color: "white", border: "none", borderRadius: 4 }}
                      >
                        View
                      </button>
                      <button 
                        onClick={() => generateInvoice(bill.billId)}
                        style={{ padding: "5px 10px", marginRight: 5, backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4 }}
                      >
                        Invoice
                      </button>
                      {bill.status !== "PAID" && (
                        <button 
                          onClick={() => {
                            const amount = prompt("Enter payment amount:");
                            if (amount) {
                              processPayment(bill.billId, { amount: parseFloat(amount), method: "CASH" });
                            }
                          }}
                          style={{ padding: "5px 10px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
                        >
                          Pay
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Transactions Tab */}
      {activeTab === "transactions" && (
        <div>
          <h3>Payment Transactions</h3>
          {transactions.length === 0 ? (
            <p>No transactions found</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ backgroundColor: "#f8f9fa" }}>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Transaction ID</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Bill ID</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Amount</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Method</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Status</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Date</th>
                </tr>
              </thead>
              <tbody>
                {transactions.map((transaction) => (
                  <tr key={transaction.transactionId}>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.transactionId}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.billId}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{formatCurrency(transaction.amount)}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.paymentMethod}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.status}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.transactionDate}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Payment Methods Tab */}
      {activeTab === "payments" && (
        <div>
          <h3>Payment Methods Configuration</h3>
          {paymentMethods.length === 0 ? (
            <p>No payment methods configured</p>
          ) : (
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))", gap: 15 }}>
              {paymentMethods.map((method, index) => (
                <div key={index} style={{
                  border: "1px solid #ddd",
                  borderRadius: 8,
                  padding: 15,
                  backgroundColor: "#f8f9fa"
                }}>
                  <h4>{method.name}</h4>
                  <p>Type: {method.type}</p>
                  <p>Status: {method.isActive ? "Active" : "Inactive"}</p>
                  <p>Processing Fee: {method.processingFee}%</p>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Tax Configuration Tab */}
      {activeTab === "taxes" && (
        <div>
          <h3>Tax Rates Configuration</h3>
          {taxRates.length === 0 ? (
            <p>No tax rates configured</p>
          ) : (
            <table style={{ width: "100%", borderCollapse: "collapse" }}>
              <thead>
                <tr style={{ backgroundColor: "#f8f9fa" }}>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Tax Name</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Rate (%)</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Type</th>
                  <th style={{ padding: 10, border: "1px solid #ddd" }}>Status</th>
                </tr>
              </thead>
              <tbody>
                {taxRates.map((tax, index) => (
                  <tr key={index}>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{tax.name}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{tax.rate}%</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{tax.type}</td>
                    <td style={{ padding: 10, border: "1px solid #ddd" }}>{tax.isActive ? "Active" : "Inactive"}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      )}

      {/* Bill Details Modal */}
      {selectedBill && (
        <div style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: "white",
            padding: 30,
            borderRadius: 8,
            maxWidth: 500,
            width: "90%",
            maxHeight: "80%",
            overflow: "auto"
          }}>
            <h3>Bill Details - {selectedBill.billId}</h3>
            <p><strong>Guest:</strong> {selectedBill.guestName}</p>
            <p><strong>Room:</strong> {selectedBill.roomNo}</p>
            <p><strong>Check-in:</strong> {selectedBill.checkInDate}</p>
            <p><strong>Check-out:</strong> {selectedBill.checkOutDate}</p>
            <p><strong>Total Amount:</strong> {formatCurrency(selectedBill.totalAmount)}</p>
            <p><strong>Status:</strong> {selectedBill.status}</p>
            
            <h4>Services & Charges</h4>
            {selectedBill.services && selectedBill.services.map((service, index) => (
              <div key={index} style={{ display: "flex", justifyContent: "space-between", marginBottom: 5 }}>
                <span>{service.name}</span>
                <span>{formatCurrency(service.amount)}</span>
              </div>
            ))}
            
            <div style={{ marginTop: 20, textAlign: "right" }}>
              <button 
                onClick={() => setSelectedBill(null)}
                style={{ padding: "10px 20px", backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4 }}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Create Bill Modal */}
      {showCreateBill && (
        <div style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: "white",
            padding: 30,
            borderRadius: 8,
            maxWidth: 500,
            width: "90%"
          }}>
            <h3>Create New Bill</h3>
            <div style={{ marginBottom: 15 }}>
              <label>Guest ID:</label>
              <input 
                type="text"
                value={newBill.guestId}
                onChange={(e) => setNewBill({...newBill, guestId: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Room Number:</label>
              <input 
                type="text"
                value={newBill.roomNo}
                onChange={(e) => setNewBill({...newBill, roomNo: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Check-in Date:</label>
              <input 
                type="date"
                value={newBill.checkInDate}
                onChange={(e) => setNewBill({...newBill, checkInDate: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Check-out Date:</label>
              <input 
                type="date"
                value={newBill.checkOutDate}
                onChange={(e) => setNewBill({...newBill, checkOutDate: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            
            <div style={{ marginTop: 20, textAlign: "right" }}>
              <button 
                onClick={() => setShowCreateBill(false)}
                style={{ padding: "10px 20px", backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4, marginRight: 10 }}
              >
                Cancel
              </button>
              <button 
                onClick={() => createNewBill(newBill)}
                style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
              >
                Create Bill
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
