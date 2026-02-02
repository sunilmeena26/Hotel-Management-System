import React, { useState, useContext, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/authService';
import { AuthContext } from '../context/AuthContext';
import { jwtDecode } from 'jwt-decode';
import '../styles/Login.css';
import MainLayout from '../components/MainLayout'; // make sure path is correct



const Login = () => {
  const [credentials, setCredentials] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { loginUser, user } = useContext(AuthContext);

  useEffect(() => {
    if (user) {
      navigate('/');
    }
  }, [user, navigate]);

  const handleChange = (e) => {
    setCredentials({ ...credentials, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await login(credentials.email, credentials.password);
      const token = response.data.token;
      loginUser(token);

      const decoded = jwtDecode(token);
      const roles = decoded.roles || [];

      if (roles.includes('ROLE_ADMIN')) {
        navigate('/');
      } else if (roles.includes('ROLE_USER')) {
        navigate('/');
      } else if (roles.includes('ROLE_RECEPTIONIST')) {
        navigate('/receptionist');
      } else {
        navigate('/');
      }
    } catch (err) {
      setError('Invalid email or password');
    }
  };

  const loginContent = (
    <div className="auth-container">
      <h2>Login</h2>
      <form onSubmit={handleSubmit}>
        <input type="email" name="email" placeholder="Email" onChange={handleChange} required />
        <input type="password" name="password" placeholder="Password" onChange={handleChange} required />
        <p><a href="/forgot-password">Forgot Password?</a></p>
        <button type="submit">Login</button>
      </form>
      {error && <p className="error">{error}</p>}
      <p>Don't have an account? <a href="/register">Register now</a></p>
    </div>
  );

  return <MainLayout content={loginContent} />;
};


export default Login;