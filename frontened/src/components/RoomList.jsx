const mockRooms = [
  { number: 101, status: "Occupied", guest: "Alice Smith", checkIn: "2024-05-10", checkOut: "2024-05-15" },
  { number: 102, status: "Vacant", guest: "", checkIn: "", checkOut: "" },
  { number: 103, status: "Occupied", guest: "Bob Johnson", checkIn: "2024-05-09", checkOut: "2024-05-14" },
  { number: 104, status: "Blocked", guest: "Maintenance", checkIn: "2024-05-08", checkOut: "2024-05-12" },
  // ...add more mock rooms as needed
];

export default function RoomList() {
  return (
    <table>
      <thead>
        <tr>
          <th>Room No</th>
          <th>Status</th>
          <th>Guest</th>
          <th>Check-In</th>
          <th>Check-Out</th>
        </tr>
      </thead>
      <tbody>
        {mockRooms.map(room => (
          <tr key={room.number}>
            <td>{room.number}</td>
            <td>{room.status}</td>
            <td>{room.guest || "No guest"}</td>
            <td>{room.checkIn}</td>
            <td>{room.checkOut}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}
