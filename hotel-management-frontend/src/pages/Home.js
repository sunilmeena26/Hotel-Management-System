import React, { useEffect, useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import RoomCard from '../components/RoomCard';

import {
  getAvailableRooms,
  getAvailableRoomsByType,
  getAvailableRoomsByDate
} from '../services/roomService';
import '../styles/home.css';

const Home = () => {
  const [rooms, setRooms] = useState([]);
  const { user } = useContext(AuthContext);
  const navigate = useNavigate();

  const [filterType, setFilterType] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [checkInDate, setCheckInDate] = useState('');
  const [checkOutDate, setCheckOutDate] = useState('');

  const today = new Date().toISOString().split('T')[0];

  useEffect(() => {
    const fetchRooms = async () => {
      try {
        let response;
        if (checkInDate && checkOutDate) {
          response = await getAvailableRoomsByDate(checkInDate, checkOutDate);
        } else if (filterType) {
          response = await getAvailableRoomsByType(filterType);
        } else {
          response = await getAvailableRooms();
        }
        setRooms(response.data);
      } catch (error) {
        console.error('Error fetching rooms:', error);
      }
    };
    fetchRooms();
  }, [filterType, checkInDate, checkOutDate]);

  const handleBookNow = (roomNumber) => {
    if (user) {
      navigate(`/book/${roomNumber}`);
    } else {
      navigate('/login');
    }
  };

  const filteredRooms = rooms.filter((room) => {
    const matchesType = filterType ? room.roomType === filterType : true;
    const matchesPrice = maxPrice ? room.price <= parseInt(maxPrice) : true;
    return matchesType && matchesPrice;
  });

  return (
    <div className="home-container">
      <h2 className="room-heading">Find Your Perfect Stay at StayEase</h2>

      <div className="filter-wrapper">
        <div className="filter-bar">
          <div className="filter-group">
            <label>Room Type</label>
            <select value={filterType} onChange={(e) => setFilterType(e.target.value)}>
              <option value="">All Types</option>
              <option value="Deluxe">Deluxe</option>
              <option value="Suite">Suite</option>
              <option value="Standard">Standard</option>
            </select>
          </div>

          <div className="filter-group">
            <label>Max Price (₹)</label>
            <input
              type="number"
              placeholder="e.g. 5000"
              value={maxPrice}
              onChange={(e) => setMaxPrice(e.target.value)}
            />
          </div>

          <div className="filter-group">
            <label>Check-In</label>
            <input
              type="date"
              value={checkInDate}
              onChange={(e) => setCheckInDate(e.target.value)}
              min={today}
            />
          </div>

          <div className="filter-group">
            <label>Check-Out</label>
            <input
              type="date"
              value={checkOutDate}
              onChange={(e) => setCheckOutDate(e.target.value)}
              min={checkInDate || today}
            />
          </div>
        </div>
      </div>

      {/* Show only first 5 rooms */}
      <div className="scroll-vertical-wrapper">
  <h3 className="scroll-vertical-heading">Explore All Rooms</h3>
  <div className="scroll-vertical-container">
    {filteredRooms.map((room) => (
      <div className="scroll-room-item" key={room.roomNumber}>
        <RoomCard room={room} onBook={handleBookNow} />
      </div>
    ))}
  </div>
</div>

      {/* Slider Section */}
     
    </div>
  );
};

export default Home;