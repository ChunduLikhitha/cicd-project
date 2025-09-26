import React, { useState } from "react";
import API from "../api";

export default function Register() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [name, setName] = useState("");
  const [msg, setMsg] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    try {
      await API.post("/auth/register", { email, password, name });
      setMsg("✅ Registered! Please login.");
      setEmail("");
      setPassword("");
      setName("");
    } catch (err) {
      setMsg(err.response?.data || "❌ Error");
    }
  };

  return (
    <div>
      <h2>Register</h2>
      <form onSubmit={submit}>
        <div>
          <input
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Name"
            required
          />
        </div>
        <div>
          <input
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
            type="email"
            required
          />
        </div>
        <div>
          <input
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
            type="password"
            required
          />
        </div>
        <button type="submit">Register</button>
      </form>
      <div style={{ marginTop: "10px", color: "green" }}>{msg}</div>
    </div>
  );
}
