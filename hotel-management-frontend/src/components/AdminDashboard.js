import React from 'react';
import { Link } from 'react-router-dom';
import Home from '../pages/Home'; // Import user dashboard
import '../styles/AdminDashboard.css';

const AdminDashboard = () => {
  return (
    <div className="admin-dashboard-wrapper">
      <div className="admin-dashboard">
        <h2>Welcome, Admin 👑</h2>

        <div className="admin-sections">
          <div className="admin-row">
            <Link to="/admin/guests" className="admin-card">Manage Guests</Link>
            <Link to="/admin/rooms" className="admin-card">Manage Rooms</Link>
            <Link to="/admin/reservations" className="admin-card">Manage Reservations</Link>
          </div>

          <div className="admin-row">
            
           
            <Link to="/admin/register-receptionist" className="admin-card">Register Receptionist</Link>
          </div>
        </div>
      </div>

      {/* 👇 User-style room listing below admin controls */}
      <div className="admin-room-section">
        <Home />
      </div>
    </div>
  );
};

export default AdminDashboard;