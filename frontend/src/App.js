// src/App.js
import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Dashboard from "./pages/Dashboard";

function Home() {
  const token = localStorage.getItem("token");
  return (
    <div>
      <h2>Welcome to FitTrack</h2>
      <p>
        Track workouts, calories, and progress. Connect a wearable later if you
        like.
      </p>
      {token ? (
        <p>You’re logged in — go to your Dashboard.</p>
      ) : (
        <p>
          Please <a href="/register">Register</a> or <a href="/login">Login</a>{" "}
          to continue.
        </p>
      )}
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <div style={{ padding: 20, fontFamily: "Arial, sans-serif" }}>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/dashboard" element={<Dashboard />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
