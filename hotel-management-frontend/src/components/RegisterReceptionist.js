import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import '../styles/Register.css';
import MainLayout from './MainLayout';

const RegisterReceptionist = () => {
  const [credentials, setCredentials] = useState({
    name: '',
    email: '',
    password: ''
  });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials({ ...credentials, [name]: value });
  };

  const validateInputs = () => {
    const nameRegex = /^[A-Za-z ]+$/;
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&]).{8,}$/;

    if (!nameRegex.test(credentials.name)) return 'Name must contain only letters and spaces';
    if (credentials.name.length < 2 || credentials.name.length > 50) return 'Name must be between 2 and 50 characters';
    if (!credentials.email.includes('@')) return 'Email format is invalid';
    if (!passwordRegex.test(credentials.password)) return 'Password must be at least 8 characters and include uppercase, lowercase, number, and special character';
    return '';
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationError = validateInputs();
    if (validationError) {
      setError(validationError);
      return;
    }

    try {
      await axios.post('http://localhost:8080/auth/register-receptionist', credentials, {
        headers: { 'Content-Type': 'application/json' }
      });
      navigate('/admin'); // or wherever you want to redirect
    } catch (err) {
      console.error(err.response?.data || err.message);
      setError('Registration failed. Try a different email or check your input.');
    }
  };

  const registerContent = (
    <div className="auth-container">
      <h2>Register Receptionist</h2>
      <form onSubmit={handleSubmit}>
        <input type="text" name="name" value={credentials.name} placeholder="Full Name" onChange={handleChange} required />
        <input type="email" name="email" value={credentials.email} placeholder="Email" onChange={handleChange} required />
        <input type="password" name="password" value={credentials.password} placeholder="Password" onChange={handleChange} required />
        <button type="submit">Register</button>
      </form>
      {error && <p className="error">{error}</p>}
    </div>
  );

  return <MainLayout content={registerContent} />;
};

export default RegisterReceptionist;