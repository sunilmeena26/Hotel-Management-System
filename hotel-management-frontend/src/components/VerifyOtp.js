import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import '../styles/Login.css';
import MainLayout from '../components/MainLayout';
import { useLocation } from 'react-router-dom';

const VerifyOtp = () => {
    const location = useLocation();
    const emailFromState = location.state?.email || '';
    const [email, setEmail] = useState(emailFromState);
  const [otp, setOtp] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/auth/verify-otp', {
        email,
        otp,
      });
      setMessage(response.data);
      setError('');
      // Redirect to reset password screen
      navigate('/reset-password', { state: { email } });
    } catch (err) {
      setError('Invalid or expired OTP');
      setMessage('');
    }
  };

  const content = (
    <div className="auth-container">
      <h2>Verify OTP</h2>
      <form onSubmit={handleSubmit}>
      <input
  type="email"
  placeholder="Enter your email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  required
  readOnly // ✅ prevents accidental change
/>
        <input
          type="text"
          placeholder="Enter OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          required
        />
        <button type="submit">Verify</button>
      </form>
      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}
    </div>
  );

  return <MainLayout content={content} />;
};

export default VerifyOtp;