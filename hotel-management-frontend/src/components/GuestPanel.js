import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/GuestPanel.css';
import MainLayout from './MainLayout';

const GuestPanel = () => {
  const [guests, setGuests] = useState([]);
  const [error, setError] = useState('');
  const [searchName, setSearchName] = useState('');
const [searchCode, setSearchCode] = useState('');
  const [newGuest, setNewGuest] = useState({
    name: '',
    email: '',
    phoneNumber: '',
    address: '',
    gender: '',
  });

  useEffect(() => {
    const fetchGuests = async () => {
      try {
        const res = await axios.get('http://localhost:8080/guest/getAll'); // You may need to add this endpoint
        setGuests(res.data);
      } catch (err) {
        setError('Failed to load guests.');
      }
    };
    fetchGuests();
  }, []);

  const handleChange = (e) => {
    setNewGuest({ ...newGuest, [e.target.name]: e.target.value });
  };

  const handleSearch = async () => {
    try {
      let res;
      if (searchCode) {
        res = await axios.get(`http://localhost:8080/guest/get/memberCode/${searchCode}`);
        setGuests([res.data]);
      } else if (searchName) {
        res = await axios.get(`http://localhost:8080/guest/get/name/${searchName}`);
        setGuests(res.data);
      } else {
        const all = await axios.get('http://localhost:8080/guest/getAll');
        setGuests(all.data);
      }
    } catch (err) {
      setError('Search failed.');
    }
  };

  const handleDelete = async (id) => {
    try {
      await axios.delete(`http://localhost:8080/guest/delete/id/${id}`);
      alert('Guest deleted');
      setGuests(guests.filter(g => g.id !== id));
    } catch (err) {
      setError('Delete failed.');
    }
  };

  const handleAddGuest = async () => {
    try {
      await axios.post('http://localhost:8080/guest/add', newGuest);
      alert('Guest added successfully!');
      window.location.reload();
    } catch (err) {
      setError('Failed to add guest.');
    }
  };

  const guestContent= (
    <div className="guest-panel">
      <h3>Guest Management</h3>

      <div className="guest-search">
  <input
    type="text"
    placeholder="Search by Name"
    value={searchName}
    onChange={(e) => setSearchName(e.target.value)}
  />
  <input
    type="text"
    placeholder="Search by Member Code"
    value={searchCode}
    onChange={(e) => setSearchCode(e.target.value)}
  />
  <button onClick={handleSearch}>Search</button>
</div>
      <div className="guest-form">
        <input type="text" name="name" placeholder="Name" onChange={handleChange} />
        <input type="email" name="email" placeholder="Email" onChange={handleChange} />
        <input type="text" name="phoneNumber" placeholder="Phone" onChange={handleChange} />
        <input type="text" name="address" placeholder="Address" onChange={handleChange} />
        <select name="gender" onChange={handleChange}>
  <option value="">Select Gender</option>
  <option value="Male">Male</option>
  <option value="Female">Female</option>
  <option value="Other">Other</option>
</select>
        <button onClick={handleAddGuest}>Add Guest</button>
      </div>

      {error && <p className="error">{error}</p>}

      <div className="guest-list">
        {guests.map((guest) => (
          <div key={guest.id} className="guest-card">
            <p><strong>Name:</strong> {guest.name}</p>
            <p><strong>Email:</strong> {guest.email}</p>
            <p><strong>Phone:</strong> {guest.phoneNumber}</p>
            <p><strong>Address:</strong> {guest.address}</p>
            <p><strong>Member Code:</strong> {guest.memberCode}</p>
            <p><strong>Gender:</strong> {guest.gender}</p>
            <button onClick={() => handleDelete(guest.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
  return <MainLayout content={guestContent}/>;
};

export default GuestPanel;