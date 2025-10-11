import React, { useState } from "react";
import "./ViewPatientRecords.css";
import profilePic from "../assets/Women.webp"; // replace with actual image path
import { useNavigate } from "react-router-dom";

export default function ViewPatientRecords() {
  const navigate = useNavigate();

  const [searchId, setSearchId] = useState("");

  const handleBack = () => {
    navigate("/dashboard");
  };

  const handleSearch = () => {
    alert(`Searching for user ID: ${searchId}`);
  };

  const handleDeactivate = () => {
    alert("User account deactivated!");
  };

  return (
    <div className="view-patient-container">
      {/* Header */}
      <div className="header">
        <h2>View Patient Records</h2>
        <button className="logout-btn" onClick={handleBack}>Back to Dashboard</button>
      </div>

      {/* Top section */}
      <div className="top-section">
        <div className="patient-details">
          <h3>Patient Records - User Profile</h3>
          <div className="details-box">
            <div className="patient-info">
              <p><strong>Full Name:</strong> Hettiarachchige Don Mihira Jayasekara</p>
              <p><strong>ID:</strong> UD1205</p>
              <p><strong>Email:</strong> mihira@gmail.com</p>
              <p><strong>Contact:</strong> +94715849635</p>
            </div>
            <div className="patient-photo">
              <img src={profilePic} alt="Profile" />
            </div>
          </div>

          <h4>Medical Records</h4>
          <table className="records-table">
            <thead>
              <tr>
                <th>No</th>
                <th>Record ID</th>
                <th>URL</th>
                <th>Date</th>
                <th>Type</th>
              </tr>
            </thead>
            <tbody>
              <tr><td>1</td><td>PR0134</td><td>https://www.newhospital.com/rec134</td><td>2025.05.27</td><td>Consultation</td></tr>
              <tr><td>2</td><td>PR0135</td><td>https://www.newhospital.com/rec135</td><td>2025.05.29</td><td>Physical</td></tr>
              <tr><td>3</td><td>PR0136</td><td>https://www.newhospital.com/rec136</td><td>2025.06.01</td><td>Diagnosis</td></tr>
              <tr><td>4</td><td>PR0137</td><td>https://www.newhospital.com/rec137</td><td>2025.06.07</td><td>Observation</td></tr>
              <tr><td>5</td><td>PR0138</td><td>https://www.newhospital.com/rec138</td><td>2025.06.10</td><td>Prescription</td></tr>
            </tbody>
          </table>
        </div>

        {/* Right side search panel */}
        <div className="search-panel">
          <h4>Patient Search:</h4>
          <input
            type="text"
            placeholder="Enter User ID"
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
          />
          <input type="text" placeholder="Name (Auto)" />
          <button className="apply-btn" onClick={handleSearch}>Apply</button>
          <button className="deactivate-btn" onClick={handleDeactivate}>Deactivate User Account</button>
        </div>
      </div>

      {/* Bottom section */}
      <div className="bottom-section">
        <h3>Patient Records</h3>
        <p>Found: 110</p>

        <table className="patient-table">
          <thead>
            <tr>
              <th>Full name</th>
              <th>Address</th>
              <th>Email</th>
              <th>Contract Number</th>
              <th>Start Date</th>
              <th>User ID</th>
            </tr>
          </thead>
          <tbody>
            <tr><td>Kavidu Silva</td><td>Colombo</td><td>Kavidu@gmail.com</td><td>+94721548798</td><td>12.01.2025</td><td>14321</td></tr>
            <tr><td>Andrew Salgado</td><td>Galle</td><td>Andrew@gmail.com</td><td>+94714585963</td><td>21.04.2025</td><td>21345</td></tr>
            <tr><td>Nimali Perera</td><td>Kalutara</td><td>Nimali@gmail.com</td><td>+94704589647</td><td>02.10.2025</td><td>1984</td></tr>
          </tbody>
        </table>

        <div className="pagination">
          <button>Previous</button>
          <button className="active">1</button>
          <button>2</button>
          <button>3</button>
          <button>Next</button>
        </div>
      </div>
    </div>
  );
}

