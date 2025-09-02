
import { Link } from "react-router-dom";

export default function NavBar() {
  return (
    <nav style={{ padding: "10px", background: "#f6f6f6" }}>
      <Link to="/dashboard">Dashboard</Link> |{" "}
      <Link to="/reservations">Reservations</Link> |{" "}
      <Link to="/check-in">Check-In</Link> |{" "}
      <Link to="/Cashier">Cashier</Link> |{" "}
      <Link to="/reports">Reports</Link> |{" "}
      <Link to="/admin">Admin</Link> |{" "}
      <Link to="/housekeeping">Housekeeping</Link>
    </nav>
  );
}

