import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import axios from 'axios';
import '../styles/PaymentPage.css';

const PaymentPage = () => {
  const [reservationDetails, setReservationDetails] = useState(null);
  const [error, setError] = useState('');
  const location = useLocation();

  const params = new URLSearchParams(location.search);
  const reservationCode = params.get('reservationCode');

  useEffect(() => {
    const fetchDetails = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/api/reservations/code/${reservationCode}`);
        setReservationDetails(res.data);
      } catch (err) {
        setError('Failed to fetch reservation details.');
      }
    };
    if (reservationCode) {
      fetchDetails();
    }
  }, [reservationCode]);

  const handlePayment = () => {
    if (reservationDetails?.paymentLink) {
      window.location.href = reservationDetails.paymentLink;
    } else {
      setError('Payment link not available.');
    }
  };

  return (
    <div className="payment-page">
      <h2>Payment Summary</h2>
      {reservationDetails ? (
        <>
          <p>Reservation Code: <strong>{reservationDetails.reservationNumber}</strong></p>
          <p>Guest Name: <strong>{reservationDetails.guestName}</strong></p>
          <p>Room Number: <strong>{reservationDetails.roomNumber}</strong></p>
          <p>Check-In: <strong>{reservationDetails.checkInDate}</strong></p>
          <p>Check-Out: <strong>{reservationDetails.checkOutDate}</strong></p>
          <p>Total Amount: <strong>{reservationDetails.paymentMassage}</strong></p>
          <button onClick={handlePayment}>Pay Now</button>
        </>
      ) : (
        <p>Loading reservation details...</p>
      )}
      {error && <p className="error">{error}</p>}
    </div>
  );
};

export default PaymentPage;