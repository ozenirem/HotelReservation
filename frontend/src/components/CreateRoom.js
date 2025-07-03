import React, { useState } from 'react';
import axios from 'axios';

function CreateRoom({ onRoomAdded }) {
    const [roomNumber, setRoomNumber] = useState('');
    const [roomType, setRoomType] = useState('');
    const [description, setDescription] = useState('');
    const [capacity, setCapacity] = useState('');
    const [pricePerNight, setPricePerNight] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/otel-reservation/1/room', {
                roomNumber,
                roomType,
                description,
                capacity: parseInt(capacity, 10),
                pricePerNight: parseFloat(pricePerNight),
            });
            onRoomAdded(); // Callback to refresh the room list and navigate
            // Reset form fields after submission
            resetForm();
        } catch (error) {
            console.error('Error adding room:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    const resetForm = () => {
        setRoomNumber('');
        setRoomType('');
        setDescription('');
        setCapacity('');
        setPricePerNight('');
    };

    return (
        <form onSubmit={handleSubmit} className="mb-4">
            <h2>Add Room</h2>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Room Number"
                    value={roomNumber}
                    onChange={(e) => setRoomNumber(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Room Type"
                    value={roomType}
                    onChange={(e) => setRoomType(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="number"
                    className="form-control"
                    placeholder="Capacity"
                    value={capacity}
                    onChange={(e) => setCapacity(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="number"
                    className="form-control"
                    placeholder="Price Per Night"
                    value={pricePerNight}
                    onChange={(e) => setPricePerNight(e.target.value)}
                    required
                />
            </div>
            <button type="submit" className="btn btn-primary">Add Room</button>
        </form>
    );
}

export default CreateRoom;
