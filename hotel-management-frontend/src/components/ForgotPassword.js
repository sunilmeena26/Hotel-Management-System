import React, { useState } from 'react';
import axios from 'axios';
import '../styles/Login.css'; // reuse styling
import MainLayout from '../components/MainLayout';
import { useNavigate } from 'react-router-dom';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://localhost:8080/auth/request-otp', { email });
      setMessage(response.data);
      setError('');
      navigate('/verify-otp', { state: { email } }); // ✅ redirect to OTP page
    } catch (err) {
      setError('Failed to send OTP. Please check your email.');
      setMessage('');
    }
  };

  const content = (
    <div className="auth-container">
      <h2>Forgot Password</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="email"
          placeholder="Enter your registered email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <button type="submit">Send OTP</button>
      </form>
      {message && <p className="success">{message}</p>}
      {error && <p className="error">{error}</p>}
    </div>
  );

  return <MainLayout content={content} />;
};

export default ForgotPassword;