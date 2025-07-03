import React, { useState } from 'react';
import axios from 'axios';

function CreateReservation({ onReservationAdded, guests, rooms }) {
    const [guestId, setGuestId] = useState('');
    const [roomId, setRoomId] = useState('');
    const [checkInDate, setCheckInDate] = useState('');
    const [checkOutDate, setCheckOutDate] = useState('');
    const [numberOfPeople, setNumberOfPeople] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/otel-reservation/1/reservation', {
                guestId: Number(guestId),
                roomId: Number(roomId),
                checkInDate,
                checkOutDate,
                numberOfPeople: Number(numberOfPeople),
            });
            onReservationAdded();
            resetForm();
        } catch (error) {
            console.error('Error adding reservation:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    const resetForm = () => {
        setGuestId('');
        setRoomId('');
        setCheckInDate('');
        setCheckOutDate('');
        setNumberOfPeople('');
    };

    return (
        <form onSubmit={handleSubmit} className="mb-4">
            <h2>Add Reservation</h2>
            <div className="form-group">
                <select
                    className="form-control"
                    value={guestId}
                    onChange={(e) => setGuestId(e.target.value)}
                    required
                >
                    <option value="">Select Guest</option>
                    {guests.map((guest) => (
                        <option key={guest.id} value={guest.id}>
                            {guest.firstName} {guest.lastName}
                        </option>
                    ))}
                </select>
            </div>
            <div className="form-group">
                <select
                    className="form-control"
                    value={roomId}
                    onChange={(e) => setRoomId(e.target.value)}
                    required
                >
                    <option value="">Select Room</option>
                    {rooms.map((room) => (
                        <option key={room.id} value={room.id}>
                            {room.roomNumber}
                        </option>
                    ))}
                </select>
            </div>
            <div className="form-group">
                <input
                    type="date"
                    className="form-control"
                    value={checkInDate}
                    onChange={(e) => setCheckInDate(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="date"
                    className="form-control"
                    value={checkOutDate}
                    onChange={(e) => setCheckOutDate(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="number"
                    className="form-control"
                    placeholder="Number of People"
                    value={numberOfPeople}
                    onChange={(e) => setNumberOfPeople(e.target.value)}
                    required
                />
            </div>
            <button type="submit" className="btn btn-primary">Add Reservation</button>
        </form>
    );
}

export default CreateReservation;