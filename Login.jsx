import React, { useState } from "react";
import { signInWithEmailAndPassword } from "firebase/auth";
import { auth } from "../firebase";
import { useNavigate, Link } from "react-router-dom";
import hospitalBg from "../assets/hospital-background.jpeg";

export default function Login() {
  const [form, setForm] = useState({ email: "", password: "" });
  const navigate = useNavigate();

  const onChange = (e) =>
    setForm({ ...form, [e.target.name]: e.target.value });

  const submit = async (e) => {
    e.preventDefault();
    try {
      await signInWithEmailAndPassword(auth, form.email, form.password);
      navigate("/dashboard");
    } catch (err) {
      alert(err.message);
    }
  };

  return (
    <div style={{ position: "relative", height: "100vh", width: "100vw" }}>
      {/* Background image */}
      <div
        style={{
          backgroundImage: `url(${hospitalBg})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
          filter: "blur(3px)",
          height: "100%",
          width: "100%",
          position: "absolute",
          top: 0,
          left: 0,
          zIndex: 0,
        }}
      ></div>

      {/* Centered Login Card */}
      <div
        style={{
          position: "absolute",
          top: 0,
          left: 0,
          height: "100%",
          width: "100%",
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          zIndex: 1,
          fontFamily: "'Segoe UI', 'Roboto', 'Helvetica Neue', sans-serif",
        }}
      >
        <div
          style={{
            maxWidth: 420,
            width: "90%",
            padding: "40px",
            borderRadius: "16px",
            boxShadow: "0 8px 24px rgba(0,0,0,0.2)",
            background: "rgba(255, 255, 255, 0.9)",
            backdropFilter: "blur(6px)",
          }}
        >
          <h2
            style={{
              textAlign: "center",
              marginBottom: "40px",
              color: "#1e6093",
              fontSize: "2.7rem",
              fontWeight: "900",
              letterSpacing: "0.02em",
              textShadow: "1px 1px 3px rgba(0,0,0,0.2)",
            }}
          >
            Med-Link
            <br/>
            Login Portal
          </h2>

          <form onSubmit={submit} style={{ display: "grid", gap: "18px" }}>
            <input
              name="email"
              type="email"
              placeholder="Email"
              value={form.email}
              onChange={onChange}
              required
              style={{
                padding: "14px",
                borderRadius: "8px",
                border: "1.5px solid #1f5f92ff",
                fontSize: "15px",
                outline: "none",
              }}
            />
            <input
              name="password"
              type="password"
              placeholder="Password"
              value={form.password}
              onChange={onChange}
              required
              style={{
                padding: "14px",
                borderRadius: "8px",
                border: "1.5px solid #1f5f92ff",
                fontSize: "15px",
                outline: "none",
              }}
            />
            <div style={{ display: "flex", justifyContent: "center", marginTop: "10px" }}>
            <button
              type="submit"
              style={{
                padding: "14px",
                borderRadius: "8px",
                border: "none",
                background: "#1e6093",
                color: "white",
                fontWeight: "bold",
                fontSize: "17px",
                cursor: "pointer",
                letterSpacing: "0.03em",
                transition: "all 0.3s ease",
                width: "60%",
                maxWidth: "200px",
              }}
              onMouseEnter={(e) =>
                (e.target.style.background = "#2596be")
              }
              onMouseLeave={(e) =>
                (e.target.style.background = "#1e6093")
              }
            >
              Log in
            </button></div>
          </form>

          <div style={{ marginTop: "18px", textAlign: "center" }}>
            <span style={{ fontSize: "15px" }}>
              No account?{" "}
              <Link
                to="/signup"
                style={{
                  color: "#2596be",
                  fontWeight: "700",
                  textDecoration: "none",
                }}
              >
                Sign up
              </Link>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
