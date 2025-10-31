import React, { useState } from "react";
import "./ManageUsers.css";
import { useNavigate } from "react-router-dom";
import { auth } from "../firebase/firebaseConfig";
import backgroundImage from "../assets/ref.jpg"; // background image

export default function ManageUsers() {
  const navigate = useNavigate();

  const [searchId, setSearchId] = useState("");
  const [searchName, setSearchName] = useState("");

  const [filteredUsers, setFilteredUsers] = useState(users);
  const [showForm, setShowForm] = useState(false);
  const [formType, setFormType] = useState("");
  const [formData, setFormData] = useState({
    id: "",
    name: "",
    email: "",
    role: "",
    registered: "",
    status: "Active",
  });

  const handleSearch = () => {
    const searchTermId = searchId.trim().toLowerCase();
    const searchTermName = searchName.trim().toLowerCase();
    const results = users.filter(
      (u) =>
        (searchTermId && u.id.toLowerCase().includes(searchTermId)) ||
        (searchTermName && u.name.toLowerCase().includes(searchTermName))
    );
    if (results.length > 0) setFilteredUsers(results);
    else {
      alert("No user found!");
      setFilteredUsers(users);
    }
  };

  const handleAddUser = () => {
    setFormType("add");
    setFormData({
      id: "",
      name: "",
      email: "",
      role: "",
      registered: new Date().toISOString().slice(0, 10),
      status: "Active",
    });
    setShowForm(true);
  };

  const handleUpdateUser = (user) => {
    setFormType("update");
    setFormData(user);
    setShowForm(true);
  };

  const handleSaveUser = () => {
    if (!formData.id || !formData.name || !formData.email || !formData.role) {
      alert("Please fill all fields!");
      return;
    }

    if (formType === "add") {
      const exists = users.some((u) => u.id === formData.id);
      if (exists) {
        alert("User ID already exists!");
        return;
      }
      const newUsers = [...users, formData];
      setUsers(newUsers);
      setFilteredUsers(newUsers);
      alert("User added successfully!");
    } else {
      const updated = users.map((u) => (u.id === formData.id ? formData : u));
      setUsers(updated);
      setFilteredUsers(updated);
      alert("User updated successfully!");
    }
    setShowForm(false);
  };

  const handleDeleteUser = (id) => {
    if (window.confirm(`Delete user ${id}?`)) {
      const updated = users.filter((u) => u.id !== id);
      setUsers(updated);
      setFilteredUsers(updated);
    }
  };

  const handleToggleStatus = (id) => {
    const updated = users.map((u) =>
      u.id === id
        ? { ...u, status: u.status === "Active" ? "Inactive" : "Active" }
        : u
    );
    setUsers(updated);
    setFilteredUsers(updated);
  };

  const handleCloseForm = () => setShowForm(false);
  const handleClearSearch = () => {
    setSearchId("");
    setSearchName("");
    setFilteredUsers(users);
  };

  return (
    <div
      className="manage-users-page"
      style={{
        backgroundImage: `url(${backgroundImage})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        minHeight: "100vh",
      }}
    >
      {/* 🟩 Top Bar */}
      <div className="top-bar">
        <h2>Manage User Accounts</h2>
        <button className="back-btn" onClick={() => navigate("/Dashboard")}>
          Back to Dashboard
        </button>
      </div>

      {/* 🟩 Content Below Top Bar */}
      <div className="content-container">
        <div className="search-section">
          <h3>Search Users</h3>
          <div className="search-inputs">
            <input
              type="text"
              placeholder="Enter User ID"
              value={searchId}
              onChange={(e) => setSearchId(e.target.value)}
            />
            <input
              type="text"
              placeholder="Enter Name"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
            />
            <button onClick={handleSearch} className="search-btn">
              Apply
            </button>
            <button onClick={handleClearSearch} className="clear-btn">
              Clear
            </button>
          </div>
        </div>

        <div className="user-actions">
          <button className="add-btn" onClick={handleAddUser}>
            Add User
          </button>
        </div>

        <div className="user-table-container">
          <h3>User Records</h3>
          <table className="user-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Full Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Registered</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map((u, i) => (
                <tr key={i}>
                  <td>{u.id}</td>
                  <td>{u.name}</td>
                  <td>{u.email}</td>
                  <td>{u.role}</td>
                  <td>{u.registered}</td>
                  <td className={u.status === "Active" ? "active-status" : "inactive-status"}>
                    {u.status}
                  </td>
                  <td>
                    <button className="edit-btn" onClick={() => handleUpdateUser(u)}>Edit</button>
                    <button
                      className={u.status === "Active" ? "deactivate-btn" : "activate-btn"}
                      onClick={() => handleToggleStatus(u.id)}
                    >
                      {u.status === "Active" ? "Deactivate" : "Activate"}
                    </button>
                    <button className="delete-btn" onClick={() => handleDeleteUser(u.id)}>
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {showForm && (
          <div className="user-form-popup">
            <div className="form-container">
              <h3>{formType === "add" ? "Add User" : "Update User"}</h3>
              <input
                type="text"
                placeholder="User ID"
                value={formData.id}
                onChange={(e) => setFormData({ ...formData, id: e.target.value })}
                disabled={formType === "update"}
              />
              <input
                type="text"
                placeholder="Full Name"
                value={formData.name}
                onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              />
              <input
                type="email"
                placeholder="Email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              />
              <input
                type="text"
                placeholder="Role"
                value={formData.role}
                onChange={(e) => setFormData({ ...formData, role: e.target.value })}
              />
              <input
                type="date"
                value={formData.registered}
                onChange={(e) => setFormData({ ...formData, registered: e.target.value })}
              />
              <div className="form-buttons">
                <button className="save-btn" onClick={handleSaveUser}>
                  Save
                </button>
                <button className="cancel-btn" onClick={handleCloseForm}>
                  Cancel
                </button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
