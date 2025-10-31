import React, { useState, useEffect } from "react";
import { signInWithEmailAndPassword } from "firebase/auth";
import { Link, useNavigate } from "react-router-dom";
import "./Login.css";

import img1 from "../assets/Hospital.jpg";
import img2 from "../assets/R.jpeg";
import img3 from "../assets/Dash.jpg";
import img4 from "../assets/org.jpg";
import img5 from "../assets/staff.jpeg";
import img6 from "../assets/scan.jpeg";
import img7 from "../assets/ref.jpg";

const images = [img1,img2,img3,img4,img5,img6,img7];

export default function Login() {
  const [currentImage, setCurrentImage] = useState(0);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentImage((prev) => (prev + 1) % images.length);
    }, 5000); // change every 5 seconds
    return () => clearInterval(interval);
  }, []);

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  // Temporary login check
  const handleLogin = (e) => {
    e.preventDefault();

    // Simple dummy validation
    if (username === "Chathuni" && password === "123456789") {
      alert("Login successful!");
      navigate("/dashboard"); // Redirect to dashboard
    } else {
      alert("Invalid email or password!");
    }
  };

  return (
    <div
    className="container"
    style={{
      backgroundImage: `url(${images[currentImage]})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        height: "100vh",
      }}
      >
      <div className="top-right-title">
      <h1 className="title">Welcome to the MedLink</h1>
      </div>
      <div style={{ textAlign: "center"}}>
      <div className="Login-Container">
        <div className="login-box">
          <h1>Admin Login </h1>
          <form onSubmit={handleLogin}>
      <input type="text" placeholder="Username" value={username} onChange={(e) =>setUsername(e.target.value)}/>
       <input type="password" placeholder="Password" value={password} onChange={(e) =>setPassword(e.target.value)}/>
      <button type="submit">Login</button>
      </form>
      <p style={{marginTop:"10px", color: "White"}} >
        Don't have an account? <Link to="/admin-register"style={{ color: "White", fontWeight: "bold", textDecoration: "none" }}>Register here</Link>
      </p>
    </div>
    <div className="hospital-card">
          <h2>About Us</h2>
          <p>
            MedLink has been a trusted pillar of healthcare excellence for over two decades, serving the Sri Lankan community with dedication, compassion, and innovation. Our mission is to provide patient-centered care that combines advanced medical technology with personalized attention.
We offer a comprehensive range of services including emergency care, specialized surgeries, diagnostic imaging, and preventive health programs. Our team of highly qualified doctors, nurses, and healthcare professionals work tirelessly to ensure each patient receives safe, effective, and empathetic treatment.
At MedLink, we believe health is the foundation of a good life. That is why we continuously invest in cutting-edge facilities, state-of-the-art equipment, and ongoing staff training. We also engage actively with the community through health awareness programs, free checkups, and wellness initiatives to promote a healthier future for everyone.
Your health is our priority — at MedLink, we donot just treat symptoms, we care for people.

          </p>
        </div>
    </div>
    </div>
    
   </div>
  );
}
