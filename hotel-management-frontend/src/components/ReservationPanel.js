import React, { useState, useEffect } from 'react';
import axios from 'axios';
import MainLayout from './MainLayout';
import '../styles/ReservationPanel.css'; // optional styling

const ReservationPanel = () => {
  const [reservations, setReservations] = useState([]);
  const [filteredReservations, setFilteredReservations] = useState([]);
  const [error, setError] = useState('');

  const [searchParams, setSearchParams] = useState({
    code: '',
    roomId: '',
    guestId: '',
    status: '',
    checkInDate: ''
  });

  useEffect(() => {
    const fetchReservations = async () => {
      try {
        const res = await axios.get('http://localhost:8080/api/reservations/getAll');
        setReservations(res.data);
        setFilteredReservations(res.data);
      } catch (err) {
        setError('Failed to load reservations.');
      }
    };
    fetchReservations();
  }, []);

  const handleSearchChange = (e) => {
    setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
  };

  const handleSearch = () => {
    const filtered = reservations.filter((r) => {
      return (
        (searchParams.code === '' || r.code.toLowerCase().includes(searchParams.code.toLowerCase())) &&
        (searchParams.roomId === '' || r.roomId.toString().includes(searchParams.roomId)) &&
        (searchParams.guestId === '' || r.guestId.toString().includes(searchParams.guestId)) &&
        (searchParams.status === '' || r.status.toLowerCase().includes(searchParams.status.toLowerCase())) &&
        (searchParams.checkInDate === '' || r.checkInDate.includes(searchParams.checkInDate))
      );
    });
    setFilteredReservations(filtered);
  };

  const handleUpdateStatus = async (code) => {
    try {
      const res = await axios.get(`http://localhost:8080/api/reservations/update-status/${code}`);
      alert(`Status updated: ${res.data}`);
      window.location.reload();
    } catch {
      alert('Failed to update status.');
    }
  };

  const reservationContent = (
    <div className="reservation-panel">
      <h3>Reservation Management</h3>

      {/* 🔍 Search Section */}
      <div className="reservation-search">
        <input name="code" placeholder="Search by Code" onChange={handleSearchChange} />
        <input name="roomId" placeholder="Search by Room ID" onChange={handleSearchChange} />
        <input name="guestId" placeholder="Search by Guest ID" onChange={handleSearchChange} />
        <input name="checkInDate" type="date" onChange={handleSearchChange} />
        <select name="status" onChange={handleSearchChange}>
          <option value="">All Statuses</option>
          <option value="PENDING">Pending</option>
          <option value="CONFIRMED">Confirmed</option>
          <option value="CANCELLED">Cancelled</option>
        </select>
        <button onClick={handleSearch}>Search</button>
      </div>

      {error && <p className="error">{error}</p>}

      {/* 📋 Reservation List */}
      <div className="reservation-list">
        {filteredReservations.map((r) => (
          <div key={r.id} className="reservation-card">
            <p><strong>Code:</strong> {r.code}</p>
            <p><strong>Room:</strong> {r.roomId}</p>
            <p><strong>Guest ID:</strong> {r.guestId}</p>
            <p><strong>Check-In:</strong> {r.checkInDate}</p>
            <p><strong>Check-Out:</strong> {r.checkOutDate}</p>
            <p><strong>Status:</strong> {r.status}</p>
            <p><strong>Nights:</strong> {r.numNights}</p>
            <button onClick={() => handleUpdateStatus(r.code)}>Update Status</button>
          </div>
        ))}
      </div>
    </div>
  );

  return <MainLayout content={reservationContent} />;
};

export default ReservationPanel;