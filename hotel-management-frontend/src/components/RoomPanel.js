import React, { useEffect, useState } from 'react';
import axios from '../utils/axiosConfig'
import '../styles/RoomPanel.css';
import MainLayout from '../components/MainLayout'; // adjust path if needed

const RoomPanel = () => {
  const [rooms, setRooms] = useState([]);
  const [error, setError] = useState('');

  const [newRoom, setNewRoom] = useState({
    roomNumber: '',
    roomType: '',
    price: '',
    status: 'AVAILABLE',
    roomPath: ''
  });

  const [searchParams, setSearchParams] = useState({
    roomNumber: '',
    roomType: '',
    price: '',
    availability: ''
  });

  const [filteredRooms, setFilteredRooms] = useState([]);

  useEffect(() => {
    fetchRooms();
  }, []);
  

  const fetchRooms = async () => {
    try {
      const res = await axios.get('http://localhost:8080/rooms/getAll');
      const normalizedRooms = res.data.map(room => ({
        ...room,
        isAvailable: room.isAvailable === 1 || room.isAvailable === true || room.isAvailable === '1'
      }));
      setRooms(normalizedRooms);
      setFilteredRooms(normalizedRooms);
    } catch (err) {
      setError('Failed to load rooms.');
    }
  };
  const handleSearchChange = (e) => {
    setSearchParams({ ...searchParams, [e.target.name]: e.target.value });
  };

  const handleSearch = () => {
    const filtered = rooms.filter((room) => {
      return (
        (searchParams.roomNumber === '' || room.roomNumber.toString().includes(searchParams.roomNumber)) &&
        (searchParams.roomType === '' || room.roomType.toLowerCase().includes(searchParams.roomType.toLowerCase())) &&
        (searchParams.price === '' || room.price.toString().includes(searchParams.price)) &&
        (searchParams.availability === '' ||
          (searchParams.availability === 'RESERVED' && room.isAvailable) ||
          (searchParams.availability === 'AVAILABLE' && !room.isAvailable))
      );
    });
    setFilteredRooms(filtered);
  };

  const handleChange = (e) => {
    setNewRoom({ ...newRoom, [e.target.name]: e.target.value });
  };

  const handleSaveRoom = async () => {
    const payload = {
      ...newRoom,
      isAvailable: newRoom.status === 'RESERVED'
    };

    try {
      if (rooms.some(r => r.roomNumber === parseInt(newRoom.roomNumber))) {
        await axios.put('http://localhost:8080/rooms/update', payload);
        alert('Room updated successfully!');
      } else {
        await axios.post('http://localhost:8080/rooms/addRoom', payload);
        alert('Room added successfully!');
      }
      fetchRooms();
      setNewRoom({ roomNumber: '', roomType: '', price: '', status: 'RESERVED', roomPath: '' });
    } catch (err) {
      setError('Failed to save room.');
    }
  };

  const handleDeleteRoom = async (roomNumber) => {
    const confirmDelete = window.confirm("Are you sure you want to delete this room?");
    if (!confirmDelete) return;

    try {
      await axios.delete(`http://localhost:8080/rooms/delete/${roomNumber}`);
      alert('Room deleted!');
      fetchRooms();
    } catch (err) {
      setError('Failed to delete room.');
    }
  };

  const roomContent = (
    <div className="room-panel">
      <h3>Room Management</h3>
  
      {/* 🔍 Search Section */}
      <div className="room-search">
        <input name="roomNumber" placeholder="Search by Room #" onChange={handleSearchChange} />
        <input name="roomType" placeholder="Search by Type" onChange={handleSearchChange} />
        <input name="price" placeholder="Search by Price" onChange={handleSearchChange} />
        <select name="availability" onChange={handleSearchChange}>
          <option value="">All</option>
          <option value="RESERVED">Available</option>
          <option value="AVAILABLE">Reserved</option>
        </select>
        <button onClick={handleSearch}>Search</button>
      </div>
  
      {/* ➕ Add/Edit Room Section */}
      <div className="room-form">
        <input type="number" name="roomNumber" placeholder="Room Number" value={newRoom.roomNumber} onChange={handleChange} />
        <input type="text" name="roomType" placeholder="Room Type" value={newRoom.roomType} onChange={handleChange} />
        <input type="number" name="price" placeholder="Price" value={newRoom.price} onChange={handleChange} />
        <input type="text" name="roomPath" placeholder="Image Path" value={newRoom.roomPath} onChange={handleChange} />
        <select name="status" value={newRoom.status} onChange={handleChange}>
          <option value="RESERVED">Available</option>
          <option value="AVAILABLE">Reserved</option>
        </select>
        <button onClick={handleSaveRoom} disabled={!newRoom.roomNumber || !newRoom.roomType || !newRoom.price}>
          Save Room
        </button>
      </div>
  
      {error && <p className="error">{error}</p>}
  
      {/* 📋 Room List Section */}
      <div className="room-list">
        {filteredRooms.map((room) => (
          <div key={room.roomNumber} className="room-card">
            <p><strong>Room #:</strong> {room.roomNumber}</p>
            <p><strong>Type:</strong> {room.roomType}</p>
            <p><strong>Price:</strong> ₹{room.price}</p>
            <p><strong>Status:</strong> {room.isAvailable ? 'RESERVED' : 'AVAILABLE'}</p>
            {room.roomPath && (
              <img
                src={`/assets/${room.roomPath}`}
                alt={`Room ${room.roomNumber}`}
                style={{ width: '100%', borderRadius: '4px', marginTop: '0.5rem' }}
              />
            )}
            <button
              onClick={() =>
                setNewRoom({
                  roomNumber: room.roomNumber,
                  roomType: room.roomType,
                  price: room.price,
                  status: room.isAvailable ? 'RESERVED' : 'AVAILABLE',
                  roomPath: room.roomPath
                })
              }
            >
              Edit
            </button>
            <button onClick={() => handleDeleteRoom(room.roomNumber)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
  
  return <MainLayout content={roomContent} />;
};

export default RoomPanel;