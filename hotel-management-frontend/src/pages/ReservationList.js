import React, { useEffect, useState, useContext } from 'react';
import { getAllReservations } from '../services/reservationService';
import { AuthContext } from '../context/AuthContext';

const ReservationList = () => {
  const [reservations, setReservations] = useState([]);
  const { token } = useContext(AuthContext);

  useEffect(() => {
    const fetchReservations = async () => {
      const res = await getAllReservations(token);
      setReservations(res.data);
    };
    fetchReservations();
  }, [token]);

  return (
    <div>
      <h2>All Reservations</h2>
      <ul>
        {reservations.map((r) => (
          <li key={r.id}>
            Guest: {r.guestName}, Room: {r.roomId}, Check-in: {r.checkInDate}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default ReservationList;