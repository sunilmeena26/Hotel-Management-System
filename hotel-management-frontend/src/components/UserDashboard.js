import React, { useEffect, useState, useContext } from 'react';
import axios from 'axios';
import { AuthContext } from '../context/AuthContext';
import '../styles/UserDashboard.css';

const UserDashboard = () => {
  const [reservations, setReservations] = useState([]);
  const [error, setError] = useState('');
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const res = await axios.get('http://localhost:8080/api/reservations/getAll');
        const userReservations = res.data.filter(r => r.guestName === user.sub);
        setReservations(userReservations);
      } catch (err) {
        setError('Failed to load reservations.');
      }
    };

    if (user) {
      fetchReservations();
    }
  }, [user]);
  const fetchPaymentStatuses = async () => {
    const updated = await Promise.all(
      //userReservations.map(async (r) => {
        reservations.map(async (r) => { 
        try {
          const statusRes = await axios.get(`http://localhost:8080/api/payments/status/${r.code}`);
          return { ...r, paymentStatus: statusRes.data };
        } catch {
          return { ...r, paymentStatus: 'Unknown' };
        }
      })
    );
    setReservations(updated);
  };

  fetchPaymentStatuses();
  return (
    <div className="dashboard-container">
      <h2>Your Reservations</h2>
      {error && <p className="error">{error}</p>}
      {reservations.length === 0 ? (
        <p>No reservations found.</p>
      ) : (
        <div className="reservation-grid">
          {reservations.map((r) => (
            <div key={r.id} className="reservation-card">
              <h3>Reservation #{r.code}</h3>
              <p><strong>Room:</strong> {r.roomId}</p>
              <p><strong>Check-In:</strong> {r.checkInDate}</p>
              <p><strong>Check-Out:</strong> {r.checkOutDate}</p>
              <p><strong>Nights:</strong> {r.numNights}</p>
              <p><strong>Status:</strong> {r.status}</p>
              <p><strong>Payment:</strong> {r.paymentStatus}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default UserDashboard;