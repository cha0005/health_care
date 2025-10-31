import React, { useState } from "react";
import { signOut } from "firebase/auth";
import { auth } from "../firebase/firebaseConfig";
import { useNavigate } from "react-router-dom";
import "./Dashboard.css";

export default function Dashboard() {
  const navigate = useNavigate();

  const [isEditing, setIsEditing] = useState(false);
  const [profile, setProfile] = useState({
    fullName: "Chathuni Prabodha",
    dob: "2003-03-07",
    address: "123, Main Street, Kalutara",
    phone: "+94701234568",
    email: "chathuni@gmail.com",
    picture:
      "https://imgcdn.stablediffusionweb.com/2024/10/2/e422911e-e632-4a91-a4af-110348c278f4.jpg",
  });

  const handleLogout = () => {
    signOut(auth).then(() => navigate("/"));
  };

  const handleEditToggle = () => setIsEditing(!isEditing);

  const handleChange = (e) => {
    setProfile({ ...profile, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    setIsEditing(false);
    console.log("Saved profile:", profile);
    // Add Firebase update logic here if needed
  };

  return (
    <div className="dashboard-container">
      <div className="dashboard-topbar">
        <h1 className="dashboard-title">Admin Dashboard</h1>
        <button className="logout-button" onClick={handleLogout}>
          Logout
        </button>
      </div>

      <div className="dashboard-main">
        <div className="left-panel">
          <div className="profile-card">
            <img
              src={profile.picture}
              alt="Admin Profile"
              className="profile-picture"
            />

            {isEditing ? (
              <div className="profile-edit-form">
                <label>
                  Full Name:
                  <input
                    type="text"
                    name="fullName"
                    value={profile.fullName}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  DOB:
                  <input
                    type="date"
                    name="dob"
                    value={profile.dob}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  Address:
                  <input
                    type="text"
                    name="address"
                    value={profile.address}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  Phone:
                  <input
                    type="text"
                    name="phone"
                    value={profile.phone}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  Email:
                  <input
                    type="email"
                    name="email"
                    value={profile.email}
                    onChange={handleChange}
                  />
                </label>
                <label>
                  Profile Picture URL:
                  <input
                    type="text"
                    name="picture"
                    value={profile.picture}
                    onChange={handleChange}
                  />
                </label>

                <button className="save-btn" onClick={handleSave}>
                  Save
                </button>
              </div>
            ) : (
              <div className="profile-details">
                <h2>{profile.fullName}</h2>
                <p>
                  <strong>DOB:</strong> {profile.dob}
                </p>
                <p>
                  <strong>Address:</strong> {profile.address}
                </p>
                <p>
                  <strong>Phone:</strong> {profile.phone}
                </p>
                <p>
                  <strong>Email:</strong> {profile.email}
                </p>
                <button
                  className="edit-profile-btn"
                  onClick={handleEditToggle}
                >
                  Edit Profile
                </button>
              </div>
            )}
          </div>
        </div>

        {/* Right panel with cards */}
        <div className="right-panel">
          <div
            className="card-button"
            onClick={() => navigate("/manage-users")}
          >
            <h3>Manage User Accounts</h3>
            <p>View, edit, or delete user accounts</p>
          </div>

          <div
            className="card-button"
            onClick={() => navigate("/manage-appointments")}
          >
            <h3>Manage Appointments</h3>
            <p>Track and update appointment schedules</p>
          </div>

          <div
            className="card-button"
            onClick={() => navigate("/view-patient-records")}
          >
            <h3>View Patient Records</h3>
            <p>Access and review patient medical data</p>
          </div>
          </div>
        </div>
      </div>
  
  );
}
