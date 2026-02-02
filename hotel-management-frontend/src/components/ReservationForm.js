import React, { useState, useEffect, useContext } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AuthContext } from '../context/AuthContext'; // ✅ Import AuthContext
import '../styles/ReservationForm.css';
import MainLayout from './MainLayout';

const ReservationForm = () => {
  const { user } = useContext(AuthContext); // ✅ Get logged-in user
  const [reservation, setReservation] = useState({
    numChildren: 0,
    numAdults: 1,
    checkInDate: '',
    checkOutDate: '',
    status: 'PENDING',
    numNights: 1,
    guestId: '',
    roomId: '',
    code: '',
    userEmail: '', // ✅ Add userEmail field
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();
  const location = useLocation();
  const today = new Date().toISOString().split('T')[0]; // ✅ Get today's date in YYYY-MM-DD format

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const memberCode = params.get('memberCode');
    const roomNumber = params.get('roomNumber');

    const fetchGuestId = async () => {
      try {
        const res = await axios.get(`http://localhost:8080/guest/get/memberCode/${memberCode}`);
        setReservation((prev) => ({
          ...prev,
          guestId: res.data.id,
          roomId: roomNumber,
          userEmail: user?.sub || '', // ✅ Attach email from logged-in user
        }));
      } catch (err) {
        setError('Failed to fetch guest details.');
      }
    };

    if (memberCode && roomNumber) {
      fetchGuestId();
    }
  }, [location.search, user]);

  const calculateNights = () => {
    const checkIn = new Date(reservation.checkInDate);
    const checkOut = new Date(reservation.checkOutDate);
    const diff = Math.ceil((checkOut - checkIn) / (1000 * 60 * 60 * 24));
    return diff > 0 ? diff : 1;
  };

  const handleChange = (e) => {
    setReservation({ ...reservation, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const nights = calculateNights();
    try {
      const payload = {
        ...reservation,
        numNights: nights,
        userEmail: user?.sub || '', // ✅ Ensure email is included in final payload
      };
      const res = await axios.post('http://localhost:8080/api/reservations/add', payload);
      const reservationCode = res.data.reservationNumber;
      navigate(`/payment?reservationCode=${reservationCode}`);
    } catch (err) {
      setError('Reservation failed. Please try again.');
    }
  };

  const reservationContent= (
    <div className="reservation-form-container">
      <h2>Reservation Details</h2>
      <form onSubmit={handleSubmit}>
      <label>Check-In Date</label>
<input
  type="date"
  name="checkInDate"
  onChange={handleChange}
  required
  min={today} // ✅ Prevent past dates
/>

<label>Check-Out Date</label>
<input
  type="date"
  name="checkOutDate"
  onChange={handleChange}
  required
  min={reservation.checkInDate || today} // ✅ Prevent check-out before check-in
/>
        <label>Adults</label>
        <input type="number" name="numAdults" min="0" max="2" onChange={handleChange} required />

        <label>Children</label>
        <input type="number" name="numChildren" min="0" max="2" onChange={handleChange} required />

        <button type="submit">Proceed to Payment</button>
      </form>
      {error && <p className="error">{error}</p>}
    </div>
  );
  return <MainLayout content={reservationContent}/>;
};

export default ReservationForm;