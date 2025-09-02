import React from "react";

export default function PageLayout({ children, rightPanel }) {
  return (
    <div
      style={{
        display: "flex",
        alignItems: "flex-start",
        justifyContent: "center",
        gap: 28,
        maxWidth: 1200,
        margin: "8px 20px",
        width: "100%",
        boxSizing: "border-box"
      }}
    >
      {/* Main content (left) */}
      <div
        style={{
          flex: 1,
          background: "#fff",
          borderRadius: 14,
          boxShadow: "0 1px 8px rgba(60,60,80,0.07)",
          padding: "38px 20px",
          minWidth: 0
        }}
      >
        {children}
      </div>

      {/* Right panel */}
      {rightPanel && (
        <aside
          style={{
            flex: "0 0 330px",
            background: "#fafafb",
            borderRadius: 14,
            boxShadow: "0 1px 8px rgba(60,60,80,0.06)",
            padding: "32px 24px",
            minHeight: "90vh"
          }}
        >
          {rightPanel}
        </aside>
      )}
    </div>
  );
}
