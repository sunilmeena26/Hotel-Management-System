import React from 'react';
import { Link } from 'react-router-dom';
import Home from '../pages/Home'; // Reuse room listing
import '../styles/AdminDashboard.css'; // Reuse styling

const ReceptionistDashboard = () => {
  return (
    <div className="admin-dashboard-wrapper">
      <div className="admin-dashboard">
        <h2>Welcome, Receptionist 💼</h2>

        <div className="admin-sections">
          <div className="admin-row">
            <Link to="/receptionist/guests" className="admin-card">Manage Guests</Link>
            <Link to="/receptionist/reservations" className="admin-card">Manage Reservations</Link>
            <Link to="/receptionist/rooms" className="admin-card">Manage Rooms</Link>
          </div>

          <div className="admin-row">
            <Link to="/receptionist/payment-status" className="admin-card">Check Payment Status</Link>
          </div>
        </div>
      </div>

      {/* 👇 Room listing reused from Home component */}
      <div className="admin-room-section">
        <Home />
      </div>
    </div>
  );
};

export default ReceptionistDashboard;