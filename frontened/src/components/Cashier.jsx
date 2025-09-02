import React, { useState } from "react";
import PaymentWorkflowForm from "./PaymentWorkflowForm";
import TransactionForm from "./TransactionForm";
import BillGeneration from "./BillGeneration";

export default function Cashier() {
  const [step, setStep] = useState(0);

  const steps = [
    "Payment Workflow (3 Scenarios)",
    "Record New Transaction", 
    "Generate Guest Bill"
  ];

  return (
    <div style={{
      maxWidth: 1200,
      margin: "36px auto",
      background: "#fff",
      borderRadius: 14,
      boxShadow: "0 1px 10px rgba(60,60,80,0.10)",
      padding: "40px 36px"
    }}>
      {/* Step Tabs */}
      <div style={{ display: "flex", justifyContent: "center", marginBottom: 36 }}>
        {steps.map((label, idx) => (
          <button
            key={label}
            onClick={() => setStep(idx)}
            style={{
              flex: 1,
              background: step === idx ? "#5754e8" : "#f6f6f6",
              color: step === idx ? "#fff" : "#888",
              border: "none",
              fontWeight: 600,
              fontSize: "18px",
              padding: "14px 0",
              cursor: "pointer",
              borderRadius: idx === 0 ? "8px 0 0 0" : idx === steps.length - 1 ? "0 8px 0 0" : "0",
              boxShadow: step === idx ? "0 3px 16px rgba(87,84,232,0.12)" : "none"
            }}
          >
            {label}
          </button>
        ))}
      </div>
      
      {/* Step Content */}
      {step === 0 && <PaymentWorkflowForm />}
      {step === 1 && <TransactionForm />}
      {step === 2 && <BillGeneration />}
    </div>
  );
}
