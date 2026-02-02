/*import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getRoomByNumber } from '../services/roomService';
import { createReservation } from '../services/reservationService';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';
import '../styles/Booking.css';

const Booking = () => {
  const { roomNumber } = useParams();
  const [room, setRoom] = useState(null);
  const [isAvailable, setIsAvailable] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { user, token } = useContext(AuthContext);

  useEffect(() => {
    const fetchRoomDetails = async () => {
      try {
        const roomRes = await getRoomByNumber(roomNumber);
        const roomData = roomRes.data;
        setRoom(roomData);
    
        const availabilityRes = await axios.get(`http://localhost:8080/rooms/availability/${roomData.roomNumber}`);
        setIsAvailable(availabilityRes.data);
      } catch (error) {
        console.error('Error fetching room or availability:', error);
      } finally {
        setLoading(false);
      }
    };
    // const fetchRoomDetails = async () => {
    //   try {
    //     const roomRes = await getRoomByNumber(roomNumber);
    //     setRoom(roomRes.data);

    //     const availabilityRes = await axios.get(`http://localhost:8080/rooms/availability/${roomRes.data.roomId}`);
    //     setIsAvailable(availabilityRes.data);
    //   } catch (error) {
    //     console.error('Error fetching room or availability:', error);
    //   } finally {
    //     setLoading(false);
    //   }
    // };

    fetchRoomDetails();
  }, [roomNumber]);

  const handleReserve = async () => {
    // const reservation = {
    //   guestName: user.sub,
    //   roomId: room.roomNumber,
    //   checkInDate: new Date().toISOString().split('T')[0],
    //   checkOutDate: new Date(Date.now() + 2 * 86400000).toISOString().split('T')[0], // +2 days
    // };
    const reservation = {
      guestName: user.sub,
      roomId: room.roomNumber, // assuming roomNumber is used as ID
      checkInDate: new Date().toISOString().split('T')[0],
      checkOutDate: new Date(Date.now() + 2 * 86400000).toISOString().split('T')[0],
    };

    try {
      await createReservation(reservation, token);
      alert('Reservation successful!');
      navigate('/');
    } catch (error) {
      console.error('Reservation failed:', error);
      alert('Failed to reserve room.');
    }
  };

  if (loading) return <p>Loading room details...</p>;

  return (
    <div className="booking-page">
      {/* <h2>Booking Room #{room.roomNumber}</h2>
      <img src={`/assets/${room.image || 'default-room.jpg'}`} alt={room.type} />
      <p>Type: {room.type}</p>
      <p>Price: ₹{room.price}</p>
      <p>Status: {isAvailable ? 'Available' : 'Not Available'}</p> }
      <h2>Booking Room #{room.roomNumber}</h2>
<img src={`/assets/${room.roomPath || 'default-room.jpg'}`} alt={room.roomType} />
<p>Type: {room.roomType}</p>
<p>Price: ₹{room.price}</p>
<p>Status: {isAvailable ? 'Available' : 'Not Available'}</p>

      {isAvailable ? (
        <button onClick={handleReserve}>Confirm Reservation</button>
      ) : (
        <p>This room is currently unavailable.</p>
      )}
    </div>
  );
};

export default Booking;*/



import React, { useEffect, useState, useContext } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getRoomByNumber } from '../services/roomService';
import { AuthContext } from '../context/AuthContext';
import axios from 'axios';
import '../styles/Booking.css';
import MainLayout from '../components/MainLayout';

const Booking = () => {
  const { roomNumber } = useParams();
  const [room, setRoom] = useState(null);
  const [isAvailable, setIsAvailable] = useState(false);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const { user } = useContext(AuthContext);

  useEffect(() => {
    const fetchRoomDetails = async () => {
      try {
        const roomRes = await getRoomByNumber(roomNumber);
        const roomData = roomRes.data;
        setRoom(roomData);

        const availabilityRes = await axios.get(`http://localhost:8080/rooms/availability/${roomData.roomNumber}`);
        setIsAvailable(availabilityRes.data);
      } catch (error) {
        console.error('Error fetching room or availability:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchRoomDetails();
  }, [roomNumber]);

  const handleReserve = () => {
    if (user) {
      navigate(`/guest-details?roomNumber=${room.roomNumber}&roomType=${room.roomType}`);
    } else {
      navigate('/login');
    }
  };

  if (loading) return <p>Loading room details...</p>;

  const bookingContent= (
    <div className="booking-page">
      <h2>Booking Room #{room.roomNumber}</h2>
      <img src={`/assets/${room.roomPath || 'default-room.jpg'}`} alt={room.roomType} />
      <p>Type: {room.roomType}</p>
      <p>Price: ₹{room.price}</p>
      <p>Status: {isAvailable ? 'Available' : 'Not Available'}</p>

      {isAvailable ? (
        <button onClick={handleReserve}>Continue to Guest Details</button>
      ) : (
        <p>This room is currently unavailable.</p>
      )}
    </div>
  );
  return <MainLayout content={bookingContent}/>;
};

export default Booking;
