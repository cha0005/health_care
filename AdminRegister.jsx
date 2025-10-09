import React, { useState, useEffect } from "react";
import { createUserWithEmailAndPassword } from "firebase/auth";
import { auth, db } from "../firebaseConfig";
import { doc, setDoc } from "firebase/firestore";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import "./AdminRegister.css";

import img1 from "../assets/Hospital.jpg";
import img2 from "../assets/R.jpeg";
import img3 from "../assets/Dash.jpg";
import img4 from "../assets/org.jpg";
import img5 from "../assets/staff.jpeg";
import img6 from "../assets/scan.jpeg";
import img7 from "../assets/ref.jpg";

const images = [img1,img2,img3,img4,img5,img6,img7];

export default function AdminRegister() {
  const [currentImage, setCurrentImage] = useState(0);
  
    useEffect(() => {
      const interval = setInterval(() => {
        setCurrentImage((prev) => (prev + 1) % images.length);
      }, 5000); // change every 5 seconds
      return () => clearInterval(interval);
    }, []);

  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

const handleRegister = async (e) => {
    e.preventDefault();
    if (!username || !email || !password ) {
      alert("Please fill all fields");
      return;
    }
    try {
       const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      await setDoc(doc(db, "admins", userCredential.user.uid), {
        username: username,
        email: email
      });
       alert("Registration successful!");
      navigate("/"); // go to login
    } catch (error) {
      console.error("Registration error:", error.code, error.message);
      alert(error.message);
    }
  };

  return (
    <div
        className="New-container"
        style={{
          backgroundImage: `url(${images[currentImage]})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
            height: "100vh",
          }}
          >
    <div className="container">
      <div style={{ textAlign: "center"}}>
      <div className="register-box">
        <h2>Admin Register</h2>
        <form onSubmit={handleRegister}>
          <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
          <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Register</button>
        </form>
        <p style={{ marginTop: "10px" }}>
          Already have an account? <Link to="/">Login here</Link>
        </p>
        </div>
      </div>
      
      <div className="AboutUs-card">
          <h2>About Us</h2>
          <p>
           MedLink has been a trusted pillar of healthcare excellence for over two decades, 
           proudly serving the Sri Lankan community with unwavering dedication, compassion, and innovation. 
           From our humble beginnings, we have grown into a leading healthcare institution known for our commitment 
           to patient-centered care and medical integrity. Our mission is to deliver outstanding healthcare that combines advanced 
           medical technology with the warmth of personalized attention — ensuring every individual receives not just treatment, 
           but genuine care.
          </p>
          <p>
           We provide a comprehensive range of medical services including emergency and critical care, specialized surgeries, 
           diagnostic imaging, laboratory testing, maternity services, and preventive health programs. Our team of highly qualified doctors, 
           nurses, and healthcare professionals work tirelessly to ensure safe, effective, and empathetic treatment for every patient who walks 
           through our doors.
          </p>
          <p>
            At MedLink, we believe health is not merely the absence of disease — it is the foundation of a fulfilled life. That’s why 
            we continuously invest in state-of-the-art facilities, innovative medical technologies, and ongoing professional development for 
            our staff. We also take pride in our active role within the community through health awareness campaigns, wellness workshops, 
            and free medical outreach programs aimed at promoting long-term well-being for all Sri Lankans.
          </p>
           <p>
            Your health will always be our highest priority. At MedLink, we don’t just treat symptoms — we care for people, nurture families,
             and strengthen communities through compassionate, world-class healthcare. Together, we are building a healthier, happier 
             Sri Lanka — one patient at a time.
           </p>

          
        </div>
    </div>
    </div>
  );
}

