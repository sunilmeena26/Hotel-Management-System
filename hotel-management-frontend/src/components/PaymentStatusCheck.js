import React, { useState } from 'react';
import axios from 'axios';
import '../styles/PaymentPanel.css';
import MainLayout from './MainLayout';

const PaymentStatusCheck = () => {
  const [bookingId, setBookingId] = useState('');
  const [paymentInfo, setPaymentInfo] = useState(null);
  const [error, setError] = useState('');

  const handleStatusCheck = async () => {
    try {
      const res = await axios.get(`http://localhost:8080/api/payments/status/${bookingId}`);
      setPaymentInfo(res.data);
      setError('');
      console.log('Payment Response: ',res.data)
    } catch {
      setError('Failed to fetch payment status.');
    }
  };

  const content = (
    <div className="payment-panel">
  <h3>Check Payment Status</h3>
  <input
    type="text"
    placeholder="Enter Booking ID"
    value={bookingId}
    onChange={(e) => setBookingId(e.target.value)}
  />
  <button onClick={handleStatusCheck}>Check Status</button>

  {paymentInfo && (
    <div className="payment-details">
      <p><strong>Status:</strong> {paymentInfo.status}</p>
      <p><strong>Amount:</strong> ₹{paymentInfo.amount}</p>
      <p><strong>Email:</strong> {paymentInfo.customerEmail}</p>
      <p><strong>Booking ID:</strong> {paymentInfo.bookingId}</p>
      <p><strong>Timestamp:</strong> {new Date(paymentInfo.createdAt).toLocaleString()}</p>
      <p><strong>Currency:</strong> {paymentInfo.currency}</p>
    </div>
  )}

  {error && <p className="error">{error}</p>}
</div>
  );

  return <MainLayout content={content} />;
};

export default PaymentStatusCheck;