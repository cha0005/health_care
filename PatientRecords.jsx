import React from "react";
import NavBar from "../components/NavBar";

export default function PatientRecords() {
  return (
    <div style={{ background: "#f4f6fb", minHeight: "100vh" }}>
      <NavBar />
      <div style={{ maxWidth: 1100, margin: "24px auto", padding: 12 }}>
        <h1 style={{ color: "#184d76" }}>Patient Records</h1>
        <p>This section will display and manage patient medical records.</p>
      </div>
    </div>
  );
}
