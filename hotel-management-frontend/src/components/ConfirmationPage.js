import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import '../styles/ConfirmationPage.css';
import MainLayout from './MainLayout';

const ConfirmationPage = () => {
  const [reservation, setReservation] = useState(null);
  const [paymentStatus, setPaymentStatus] = useState('');
  const [error, setError] = useState('');
  const location = useLocation();
  //const navigate = useNavigate(); // ✅ Add this

  const params = new URLSearchParams(location.search);
  const reservationCode = params.get('reservationCode');

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/reservations/code/${reservationCode}`);
        setReservation(res.data);

        const paymentRes = await axios.get(`http://localhost:8080/api/reservations/update-status/${reservationCode}`);
        setPaymentStatus(paymentRes.data.status);
      } catch (err) {
        setError('Failed to load confirmation details.');
      }
    };

    if (reservationCode) {
      fetchDetails();
    }
  }, [reservationCode]);

  const handleDashboardRedirect = () => {
    window.location.href = 'http://localhost:3000/';
  };
  

  if (error) return <p className="error">{error}</p>;
  if (!reservation) return <p>Loading confirmation...</p>;

  const confirmationContent= (
    <div className="confirmation-container">
      <h2>Reservation Confirmed 🎉</h2>
      <p><strong>Reservation Code:</strong> {reservation.reservationNumber}</p>
      <p><strong>Guest Name:</strong> {reservation.guestName}</p>
      <p><strong>Room:</strong> {reservation.roomNumber}</p>
      <p><strong>Check-In:</strong> {reservation.checkInDate}</p>
      <p><strong>Check-Out:</strong> {reservation.checkOutDate}</p>
      <p><strong>Payment Status:</strong> {paymentStatus}</p>

      {/* ✅ Dashboard Button */}
      <button onClick={handleDashboardRedirect} className="dashboard-button">
  Go to Dashboard
</button>
    </div>
  );
  return <MainLayout content={confirmationContent}/>;
};

export default ConfirmationPage;