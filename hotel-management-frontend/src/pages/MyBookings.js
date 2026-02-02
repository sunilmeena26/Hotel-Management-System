// MyBookings.js
import React, { useEffect, useState, useContext } from 'react';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';
import '../styles/MyBookings.css';

const MyBookings = () => {
  const { user } = useContext(AuthContext);
  const [bookings, setBookings] = useState([]);

  useEffect(() => {
    if (user) {
      axios
        .get(`http://localhost:8080/api/reservations/user/${user.sub}`)
        .then((res) => setBookings(res.data))
        .catch((err) => console.error(err));
    }
  }, [user]);

  const handleCancel = (bookingId) => {
    axios
      .put(`http://localhost:8080/api/reservations/cancel/${bookingId}`)
      .then(() => {
        setBookings((prev) => prev.filter((b) => b.id !== bookingId));
      })
      .catch((err) => console.error('Cancel failed:', err));
  };

  return (
    <div className="static-page">
      <h2>My Bookings</h2>
      {bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        <ul className="booking-list">
          {bookings.map((booking) => (
            <li key={booking.id} className="booking-item">
              <div className="flip-card">
                <div className="flip-card-inner">
                  <div className="flip-card-front">
                    <h3>Room #{booking.roomId}</h3>
                    <p><strong>Check-in:</strong> {booking.checkInDate}</p>
                    <p><strong>Check-out:</strong> {booking.checkOutDate}</p>
                  </div>
                  <div className="flip-card-back">
                    <p><strong>Adults:</strong> {booking.numAdults}</p>
                    <p><strong>Children:</strong> {booking.numChildren}</p>
                    <p><strong>Status:</strong> {booking.status}</p>
                    <button
                      className="cancel-btn"
                      onClick={() => handleCancel(booking.id)}
                    >
                      Cancel Reservation
                    </button>
                  </div>
                </div>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default MyBookings;