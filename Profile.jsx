import React, { useEffect, useState } from "react";
import NavBar from "../components/NavBar";
import { getAuth, onAuthStateChanged } from "firebase/auth";
import { getFirestore, doc, getDoc, setDoc } from "firebase/firestore";

export default function Profile() {
  const [userData, setUserData] = useState(null);
  const [editMode, setEditMode] = useState(false);
  const [formData, setFormData] = useState({});
  const [loading, setLoading] = useState(false);

  const departmentWardMap = {
    "Internal Medicine": "Ward 1",
    Cardiology: "Ward 2",
    Dermatology: "Ward 3",
    Neurology: "Ward 4",
    Pediatrics: "Ward 5",
    Orthopedic: "Ward 6",
    "Obstetrics and Gynecology (OB/GYN)": "Ward 7",
    Psychiatry: "Ward 8",
  };

  useEffect(() => {
    const auth = getAuth();
    const db = getFirestore();

    const unsubscribe = onAuthStateChanged(auth, async (user) => {
      if (user) {
        try {
          const docRef = doc(db, "users", user.uid);
          const docSnap = await getDoc(docRef);

          if (docSnap.exists()) {
            setUserData(docSnap.data());
            setFormData(docSnap.data());
          }
        } catch (error) {
          console.error("Error fetching user data:", error);
        }
      }
    });

    return () => unsubscribe();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    let updated = { ...formData, [name]: value };

    if (name === "department") {
      updated.assignedWard = departmentWardMap[value] || "";
    }

    setFormData(updated);
  };

  const handleUpdate = async () => {
    const auth = getAuth();
    const db = getFirestore();
    const user = auth.currentUser;

    if (!user) return;

    try {
      setLoading(true);

      const updatedData = {
        ...formData,
        profilePicture: formData.profilePicture || "https://cdn.vectorstock.com/i/preview-1x/66/10/default-avatar-profile-icon-social-media-user-vector-49816610.jpg", 
      };

      const docRef = doc(db, "users", user.uid);
      await setDoc(docRef, updatedData, { merge: true });

      setUserData(updatedData);
      setFormData(updatedData);
      setEditMode(false);
      alert("Profile updated successfully!");
    } catch (error) {
      console.error("Error updating profile:", error);
      alert("Failed to update profile");
    } finally {
      setLoading(false);
    }
  };

  if (!userData) {
    return (
      <div style={{ background: "#f4f6fb", minHeight: "100vh" }}>
        <NavBar />
        <div style={{ maxWidth: 1100, margin: "24px auto", padding: 12 }}>
          <h1 style={{ color: "#184d76" }}>Profile</h1>
          <p>Loading user data...</p>
        </div>
      </div>
    );
  }

  const fields = [
    { label: "Full Name", name: "fullName" },
    { label: "Contact No", name: "contactNo" },
    { label: "Department", name: "department", type: "select" },
    { label: "Assigned Ward", name: "assignedWard", readOnly: true },
    { label: "Educational Qualifications", name: "eduQualifications" },
    { label: "Medical Registration No", name: "medicalRegNo", type: "number" },
    { label: "Years of Experience", name: "yearsOfExperience", type: "number" },
    { label: "Email", name: "email" },
    { label: "Profile Picture URL", name: "profilePicture" },
  ];

  return (
    <div style={{ background: "#f4f6fb", minHeight: "100vh" }}>
      <NavBar />
      <div
        style={{
          maxWidth: 800,
          margin: "40px auto",
          padding: "40px 60px",
          background: "#fff",
          borderRadius: "12px",
          boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
        }}
      >
        <h1 style={{ color: "#184d76", textAlign: "center" }}>Profile</h1>

        <div style={{ textAlign: "center", marginBottom: "25px" }}>
          <img
            src={formData.profilePicture || "https://cdn.vectorstock.com/i/preview-1x/66/10/default-avatar-profile-icon-social-media-user-vector-49816610.jpg"}
            alt="Profile"
            style={{
              width: "230px",
              height: "230px",
              borderRadius: "50%",
              objectFit: "cover",
              marginBottom: "15px",
              border: "3px solid #1e6093",
            }}
          />
          <h2 style={{ margin: 0 }}>{userData.fullName}</h2>
        </div>

        <div style={{ display: "grid", gap: "20px" }}>
          {fields.map((field) => (
            <div key={field.name} style={boxStyle}>
              <label
                style={{
                  fontWeight: "bold",
                  marginBottom: "6px",
                  color: "#184d76",
                }}
              >
                {field.label}
              </label>
              {editMode ? (
                field.type === "select" ? (
                  <select
                    name={field.name}
                    value={formData[field.name] || ""}
                    onChange={handleChange}
                    style={inputStyle}
                  >
                    <option value="">Select Department</option>
                    {Object.keys(departmentWardMap).map((dept) => (
                      <option key={dept} value={dept}>
                        {dept}
                      </option>
                    ))}
                  </select>
                ) : field.readOnly ? (
                  <input
                    type="text"
                    name={field.name}
                    value={formData[field.name] || ""}
                    readOnly
                    style={{
                      ...inputStyle,
                      backgroundColor: "#eaeaea",
                      color: "#555",
                    }}
                  />
                ) : (
                  <input
                    type={field.type || "text"}
                    name={field.name}
                    value={formData[field.name] || ""}
                    onChange={handleChange}
                    style={inputStyle}
                  />
                )
              ) : (
                <div style={valueStyle}>{userData[field.name] || "—"}</div>
              )}
            </div>
          ))}
        </div>

        <div
          style={{ display: "flex", justifyContent: "center", marginTop: "30px" }}
        >
          {!editMode ? (
            <button
              style={buttonStyle}
              onMouseEnter={(e) => (e.target.style.background = "#2596be")}
              onMouseLeave={(e) => (e.target.style.background = "#1e6093")}
              onClick={() => setEditMode(true)}
            >
              Edit Profile
            </button>
          ) : (
            <button
              style={buttonStyle}
              disabled={loading}
              onMouseEnter={(e) => (e.target.style.background = "#2596be")}
              onMouseLeave={(e) => (e.target.style.background = "#1e6093")}
              onClick={handleUpdate}
            >
              {loading ? "Updating..." : "Update Profile"}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

const boxStyle = {
  display: "flex",
  flexDirection: "column",
};

const inputStyle = {
  width: "100%",
  padding: "14px",
  fontSize: "15px",
  borderRadius: "8px",
  border: "1.5px solid #1f5f92ff",
  outline: "none",
  backgroundColor: "#fff",
  boxSizing: "border-box",
};

const valueStyle = {
  fontSize: "15px",
  color: "#333",
  padding: "14px",
  borderRadius: "8px",
  border: "1.5px solid #1f5f92ff",
  background: "#f9f9f9",
  boxSizing: "border-box",
};

const buttonStyle = {
  padding: "14px",
  borderRadius: "8px",
  border: "none",
  background: "#1e6093",
  color: "white",
  fontWeight: "bold",
  fontSize: "17px",
  cursor: "pointer",
  width: "200px",
  transition: "all 0.3s ease",
  outline: "none",
  boxShadow: "none",
};
