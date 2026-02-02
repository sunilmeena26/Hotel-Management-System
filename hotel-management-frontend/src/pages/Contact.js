import React from 'react';
import { FaInstagram, FaYoutube, FaFacebookF, FaTwitter } from 'react-icons/fa';
import '../styles/StaticPage.css';

const Contact = () => (
  <div className="static-page contact-page">
    <h2>Contact Us</h2>
    <p>If you have any questions, feedback, or need assistance, feel free to reach out to us.</p>

    <div className="contact-details">
      <div>
        <h3>Email</h3>
        <p>support@stayease.com</p>
      </div>
      <div>
        <h3>Phone</h3>
        <p>+91-9876543210</p>
      </div>
      <div>
        <h3>Address</h3>
        <p>StayEase HQ, 5th Floor, Orchid Plaza, Chennai, Tamil Nadu - 600001</p>
      </div>
    </div>

    <h3>Follow Us</h3>
    <div className="social-icons">
      <a href="https://instagram.com/stayease" target="_blank" rel="noopener noreferrer"><FaInstagram /></a>
      <a href="https://youtube.com/stayease" target="_blank" rel="noopener noreferrer"><FaYoutube /></a>
      <a href="https://facebook.com/stayease" target="_blank" rel="noopener noreferrer"><FaFacebookF /></a>
      <a href="https://twitter.com/stayease" target="_blank" rel="noopener noreferrer"><FaTwitter /></a>
    </div>
  </div>
);

export default Contact;