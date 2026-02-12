import React from "react";
import NavBar from "../components/NavBar";

export default function Calendar() {
  const year = new Date().getFullYear();
  const months = [
    "January","February","March","April","May","June",
    "July","August","September","October","November","December"
  ];

  const generateMonthDays = (monthIndex) => {
    const daysInMonth = new Date(year, monthIndex + 1, 0).getDate();
    const firstDay = new Date(year, monthIndex, 1).getDay(); // 0 = Sun
    const blanks = Array(firstDay).fill(null);
    return [...blanks, ...Array.from({ length: daysInMonth }, (_, i) => i + 1)];
  };

  return (
    <div style={{ background: "#f4f6fb", minHeight: "100vh" }}>
      <NavBar />
      <div style={{ maxWidth: 1200, margin: "24px auto", padding: 12 }}>
        <h1 style={{ color: "#184d76", marginBottom: 20 }}>
          {year} Calendar
        </h1>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "repeat(auto-fit, minmax(280px, 1fr))",
            gap: 20,
          }}
        >
          {months.map((month, index) => {
            const days = generateMonthDays(index);
            return (
              <div
                key={month}
                style={{
                  background: "#fff",
                  borderRadius: 8,
                  padding: 16,
                  boxShadow: "#d3eaf2",
                }}
              >
                <h3 style={{ textAlign: "center", color: "#184d76" }}>
                  {month}
                </h3>
                <div
                  style={{
                    display: "grid",
                    gridTemplateColumns: "repeat(7, 1fr)",
                    textAlign: "center",
                    gap: 4,
                    fontSize: "0.85rem",
                  }}
                >
                  {["S", "M", "T", "W", "T", "F", "S"].map((d) => (
                    <div key={d} style={{ fontWeight: "600", color: "#555" }}>
                      {d}
                    </div>
                  ))}
                  {days.map((day, i) =>
                    day ? (
                      <div
                        key={i}
                        style={{
                          padding: "4px 0",
                          background: "#e9f5f9",
                          borderRadius: 4,
                          color: "#333",
                        }}
                      >
                        {day}
                      </div>
                    ) : (
                      <div key={i}></div>
                    )
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
