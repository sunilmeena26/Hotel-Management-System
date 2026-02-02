import React from 'react';
import { FaInstagram, FaYoutube, FaFacebookF, FaTwitter } from 'react-icons/fa';
import '../styles/StaticPage.css';

const About = () => (
  <div className="static-page">
    <h2>About StayEase</h2>
    <p>
      Welcome to <strong>StayEase</strong>, where comfort meets elegance. Since our founding in 1990, we've been dedicated to providing world-class hospitality experiences for travelers from around the globe. Whether you're here for business, leisure, or a romantic getaway, StayEase ensures every moment is memorable.
    </p>
    <p>
      Our properties are designed with a blend of modern luxury and local charm, offering spacious rooms, gourmet dining, wellness spas, and personalized services. With a presence in over 15 cities, StayEase continues to redefine what it means to feel at home — even when you're away.
    </p>
    <p>
      We believe in sustainability, inclusivity, and innovation. Our team is committed to making your stay not just comfortable, but truly exceptional.
    </p>

    <h3>Connect With Us</h3>
    <div className="social-icons">
      <a href="https://instagram.com/stayease" target="_blank" rel="noopener noreferrer">
        <FaInstagram />
      </a>
      <a href="https://youtube.com/stayease" target="_blank" rel="noopener noreferrer">
        <FaYoutube />
      </a>
      <a href="https://facebook.com/stayease" target="_blank" rel="noopener noreferrer">
        <FaFacebookF />
      </a>
      <a href="https://twitter.com/stayease" target="_blank" rel="noopener noreferrer">
        <FaTwitter />
      </a>
    </div>
  </div>
);

export default About;