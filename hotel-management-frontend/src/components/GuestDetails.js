import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import '../styles/GuestDetails.css';
import MainLayout from './MainLayout';

const GuestDetails = () => {
    const [guest, setGuest] = useState({
      name: '',
      phoneNumber: '',
      email: '',
      gender: '',
      address: '',
    });
  
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const location = useLocation();
    const queryParams = new URLSearchParams(location.search);
const roomNumber = queryParams.get('roomNumber');
const roomType = queryParams.get('roomType');

    const handleChange = (e) => {
        setGuest({ ...guest, [e.target.name]: e.target.value });
      };

      const handleSubmit = async (e) => {
        e.preventDefault();
        try {
          const response = await axios.post('http://localhost:8080/guest/add', guest);
          const memberCode = response.data.match(/Member code is: (\w+)/)?.[1];
          if (memberCode) {
            navigate(`/reservation?memberCode=${memberCode}&roomNumber=${roomNumber}&roomType=${roomType}`);
          } else {
            throw new Error('Member code not found');
          }
        } catch (err) {
          setError('Failed to add guest. Please check your details.');
        }
      };

      const GuestDetailContent= (
        <div className="guest-form-container">
          <h2>Guest Details</h2>
          <form onSubmit={handleSubmit}>
            <input type="text" name="name" placeholder="Full Name" onChange={handleChange} required />
            <input type="text" name="phoneNumber" placeholder="Phone Number" onChange={handleChange} required />
            <input type="email" name="email" placeholder="Email" onChange={handleChange} />
            <select name="gender" onChange={handleChange} required>
              <option value="">Select Gender</option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
            <textarea name="address" placeholder="Address" onChange={handleChange} required />
            <button type="submit">Continue to Reservation</button>
          </form>
          {error && <p className="error">{error}</p>}
        </div>
      );
      return <MainLayout content={GuestDetailContent}/>;
    };
    

export default GuestDetails;