import React from 'react';
import '../styles/StaticPage.css';

const FAQ = () => (
  <div className="static-page faq-page">
    <h2>Frequently Asked Questions</h2>
    <ul className="faq-list">
      <li>
        <strong>Q: How do I book a room?</strong><br />
        A: You can book a room directly through our <a href="/book/101">Booking Page</a>. Just select your preferred room and follow the steps.
      </li>
      <li>
        <strong>Q: Can I cancel my reservation?</strong><br />
        A: Yes, cancellations are allowed up to 24 hours before check-in. Visit your booking history to cancel.
      </li>
      <li>
        <strong>Q: Do you offer airport pickup?</strong><br />
        A: Yes, we offer complimentary airport pickup for premium guests. Please mention it during booking.
      </li>
      <li>
        <strong>Q: Are pets allowed?</strong><br />
        A: Pets are allowed in designated pet-friendly rooms. Additional charges may apply.
      </li>
      <li>
        <strong>Q: Is breakfast included?</strong><br />
        A: Yes, all bookings include complimentary breakfast served between 7 AM and 10 AM.
      </li>
      <li>
        <strong>Q: How do I contact customer support?</strong><br />
        A: You can reach us via email at support@stayease.com or call +91-9876543210.
      </li>
    </ul>
  </div>
);

export default FAQ;