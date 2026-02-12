import React, { useEffect, useState } from "react";
import NavBar from "../components/NavBar";
import { getAuth, onAuthStateChanged } from "firebase/auth";

export default function Dashboard() {
  const [userName, setUserName] = useState("");

  useEffect(() => {
    const auth = getAuth();
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        // If the user has a display name, show it — otherwise use their email prefix
        setUserName(user.displayName || user.email?.split("@")[0]);
      } else {
        setUserName("Doctor");
      }
    });

    return () => unsubscribe();
  }, []);

  return (
    <div style={{ background: "#f4f6fb", minHeight: "100vh" }}>
      <NavBar />
      <div
        style={{
          maxWidth: 1000,
          margin: "24px auto",
          padding: "40px 20px",
          textAlign: "center",
        }}
      >
        <h1 style={{ color: "#184d76", fontSize: "2.2rem", fontWeight: "700" }}>
          Welcome, {userName} 👋
        </h1>
        <p style={{ color: "#444", marginTop: "10px" }}>
          You’re logged into the Med-Link Staff Portal.
        </p>
      </div>
    </div>
  );
}