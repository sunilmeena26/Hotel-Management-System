import React, { useContext, useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import '../styles/Header.css';
import { useNavigate } from 'react-router-dom';

const Header = () => {
  const { user, logoutUser } = useContext(AuthContext);
  const [showDropdown, setShowDropdown] = useState(false);
  const roles = user?.roles || [];
  const navigate = useNavigate();
  const dropdownRef = useRef(null);

  const handleLogout = () => {
    logoutUser();         // clears auth state
    navigate('/');        // redirects to home
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
    };
  
    if (showDropdown) {
      document.addEventListener('mousedown', handleClickOutside);
    }
  
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
    };
  }, [showDropdown]);

  const toggleDropdown = () => setShowDropdown(!showDropdown);

  return (
    <header className="header">
      <Link to="/" className="logo">
  <img src="/assets/logo.png" alt="StayEase Logo" className="logo-img" />
  <span className="logo-text">StayEase</span>
</Link>
      <nav className="nav">
        {user ? (
          <div className="user-section">
            <span className="welcome">Welcome, {user.name}</span>
            <div className="profile-icon" onClick={toggleDropdown}>👤</div>

            {showDropdown && (
  <div className="profile-dropdown" ref={dropdownRef}>
    <div className="dropdown-header-box">
      <p className="dropdown-name">{user.name}</p>
      <p className="dropdown-email">{user.sub}</p>
    </div>

    <div className="dropdown-divider" />

    <div className="dropdown-links">
      <Link to="/profile">View Profile</Link>
      {(roles.includes('ROLE_USER') || roles.includes('ROLE_ADMIN') || roles.includes('ROLE_RECEPTIONIST')) && (
        <Link to="/bookings">My Bookings</Link>
      )}
      <Link to="/about">About</Link>
      <Link to="/">Home</Link>
    </div>

    <div className="dropdown-divider" />

    <button className="logout-btn" onClick={handleLogout}>Logout</button>
  </div>
            )}
          </div>
        ) : (
          <>
            <Link to="/" className="auth-btn">Home</Link>
  <Link to="/about" className="auth-btn">About</Link>
  <Link to="/contact" className="auth-btn">Contact Us</Link>
  <Link to="/login" className="auth-btn">Login</Link>
  <Link to="/register" className="auth-btn">Register</Link>

          </>
        )}
      </nav>
    </header>
  );
};

export default Header;