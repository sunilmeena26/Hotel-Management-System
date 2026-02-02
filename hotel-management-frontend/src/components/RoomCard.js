import React from 'react';
import '../styles/RoomCard.css';

const RoomCard = ({ room, onBook }) => (
  <div className="room-card-horizontal">
    <img
  src={`/assets/${room.roomPath}`}
  alt={`Room ${room.roomNumber}`}
  onError={(e) => {
    e.target.onerror = null;
    e.target.src = '/assets/default-room.jpg'; // fallback only if image not found
  }}

    />
   <div className="room-details">
  <div className="room-info-row">
    <span className="room-type-label">{room.roomType}</span>
    <span className="room-price-label">₹{room.price}</span>
    <button onClick={() => onBook(room.roomNumber)}>Book Now</button>
  </div>
</div>
  </div>
);

export default RoomCard;