import React, { useEffect, useState } from "react";
import API from "../api";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const [profile, setProfile] = useState(null);
  const nav = useNavigate();

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const res = await API.get("/user/me");
        setProfile(res.data);
      } catch (err) {
        nav("/login"); // if token invalid
      }
    };
    fetchProfile();
  }, [nav]);

  if (!profile) return <div>Loading...</div>;

  return (
    <div>
      <h2>Welcome, {profile.name || profile.email}</h2>
      <p>Your fitness dashboard will appear here.</p>
      <ul>
        <li>Email: {profile.email}</li>
        <li>User ID: {profile.id}</li>
      </ul>
    </div>
  );
}
