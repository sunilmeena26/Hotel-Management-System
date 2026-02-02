import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Footer.css';

const Footer = () => (
  <footer className="footer">
    <p className="footer-text">&copy; 2025 StayEase</p>
    <nav className="footer-nav">
      <Link to="/">Home</Link> | 
      <Link to="/about">About</Link> | 
      <Link to="/contact">Contact Us</Link> | 
      <Link to="/privacy-policy">Privacy Policy</Link> | 
      <Link to="/faq">FAQs</Link>
    </nav>
  </footer>
);

export default Footer;