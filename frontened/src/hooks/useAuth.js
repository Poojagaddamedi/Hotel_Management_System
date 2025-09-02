import { useContext, createContext } from "react";

// Create a global authentication context
const AuthContext = createContext(null);

// Provider to wrap the app
export function AuthProvider({ value, children }) {
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

// Hook to access the context
export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
