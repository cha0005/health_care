import { Link } from "react-router-dom";
import hospitalBg from '../assets/hospital-background.jpeg';

export default function Welcome() {
  return (
    <div style={{ position: "relative", height: "100vh", width: "100vw" }}>
      {/* Background Image */}
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

      {/* Centered Content */}
      <div
        style={{
          position: "absolute",
          top: 0,
          left: 0,
          height: "100%",
          width: "100%",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          zIndex: 1,
        }}
      >
        <h1
          style={{
            fontSize: "6rem",
            fontWeight: "900",
            color: "#ffffff",
            fontFamily: "'Segoe UI', 'Roboto', 'Helvetica Neue', sans-serif",
            textAlign: "center",
            textShadow: "2px 2px 8px rgba(0,0,0,0.7)",
            letterSpacing: "0.08em",
            marginBottom: "40px",
          }}
        >
          WELCOME TO STAFF PORTAL
        </h1>

        <Link
          to="/login"
          style={{
            fontSize: "3rem",
            fontWeight: "bold",
            color: "#ffffff",
            backgroundColor: "transparent",
            border: "2px solid white",
            padding: "10px 30px",
            borderRadius: "8px",
            textDecoration: "none",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            gap: "10px",
            transition: "all 0.3s ease",
            textAlign: "center", 
          }}
          onMouseEnter={(e) => {
            e.target.style.backgroundColor = "white";
            e.target.style.color = "#1e5f93";
          }}
          onMouseLeave={(e) => {
            e.target.style.backgroundColor = "transparent";
            e.target.style.color = "white";
          }}
        >
          ➜ Login
        </Link>
      </div>
    </div>
  );
}
