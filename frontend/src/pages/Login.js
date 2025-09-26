import React, { useState } from "react";
import API, { setToken } from "../api";
import { useNavigate } from "react-router-dom";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [msg, setMsg] = useState("");
  const nav = useNavigate();

  const submit = async (e) => {
    e.preventDefault();
    try {
      const res = await API.post("/auth/login", { email, password });
      const token = res.data.token;
      setToken(token); // saves to localStorage
      nav("/dashboard");
    } catch (err) {
      setMsg("❌ Login failed. Check your credentials.");
    }
  };

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={submit}>
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
        <button type="submit">Login</button>
      </form>
      <div style={{ marginTop: "10px", color: "red" }}>{msg}</div>
    </div>
  );
}
