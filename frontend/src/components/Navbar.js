import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { setToken } from "../api";

export default function Navbar() {
  const [token, setTok] = useState(localStorage.getItem("token"));
  const navigate = useNavigate();

  useEffect(() => {
    // keep state updated if token changes in localStorage
    const syncToken = () => setTok(localStorage.getItem("token"));
    window.addEventListener("storage", syncToken);
    return () => window.removeEventListener("storage", syncToken);
  }, []);

  const logout = () => {
    setToken(null); // clears localStorage
    setTok(null);
    navigate("/login");
  };

  return (
    <nav
      style={{ marginBottom: 20, padding: 10, borderBottom: "1px solid #ccc" }}
    >
      <Link to="/" style={{ marginRight: 10 }}>
        Home
      </Link>
      <Link to="/dashboard" style={{ marginRight: 10 }}>
        Dashboard
      </Link>

      {!token ? (
        <>
          <Link to="/login" style={{ marginRight: 10 }}>
            Login
          </Link>
          <Link to="/register">Register</Link>
        </>
      ) : (
        <button onClick={logout} style={{ marginLeft: 10 }}>
          Logout
        </button>
      )}
    </nav>
  );
}
