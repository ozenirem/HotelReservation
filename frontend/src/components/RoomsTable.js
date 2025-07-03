import React, { useEffect, useState } from 'react';
import axios from 'axios';

function RoomsTable({ onRoomUpdated }) {
    const [rooms, setRooms] = useState([]);
    const [loading, setLoading] = useState(true);
    const [editRoomId, setEditRoomId] = useState(null);
    const [updatedRoomNumber, setUpdatedRoomNumber] = useState('');
    const [updatedRoomType, setUpdatedRoomType] = useState('');
    const [updatedDescription, setUpdatedDescription] = useState('');
    const [updatedCapacity, setUpdatedCapacity] = useState('');
    const [updatedPricePerNight, setUpdatedPricePerNight] = useState('');

    useEffect(() => {
        const fetchRooms = async () => {
            try {
                const response = await axios.get('/otel-reservation/1/rooms');
                setRooms(response.data);
            } catch (error) {
                console.error('Error fetching rooms:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchRooms();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this room?')) {
            try {
                await axios.delete(`/otel-reservation/1/room/${id}`);
                setRooms(rooms.filter(room => room.id !== id));
                alert('Room deleted successfully!');
            } catch (error) {
                console.error('Error deleting room:', error);
                if (error.response && error.response.status === 400) {
                    alert('Error: ' + error.response.data.message);
                }
            }
        }
    };

    const handleEdit = (room) => {
        setEditRoomId(room.id);
        setUpdatedRoomNumber(room.roomNumber);
        setUpdatedRoomType(room.roomType);
        setUpdatedDescription(room.description);
        setUpdatedCapacity(room.capacity);
        setUpdatedPricePerNight(room.pricePerNight);
    };

    const handleUpdate = async (roomId) => {
        try {
            await axios.put(`/otel-reservation/1/room/${roomId}`, {
                roomNumber: updatedRoomNumber,
                roomType: updatedRoomType,
                description: updatedDescription,
                capacity: Number(updatedCapacity),
                pricePerNight: parseFloat(updatedPricePerNight),
            });
            setRooms(rooms.map(room =>
                room.id === roomId ? { ...room, roomNumber: updatedRoomNumber, roomType: updatedRoomType, description: updatedDescription, capacity: updatedCapacity, pricePerNight: updatedPricePerNight } : room
            ));
            setEditRoomId(null);
            alert('Room updated successfully!');
            if (onRoomUpdated) {
                onRoomUpdated(); // Notify parent to refresh reservations
            }
        } catch (error) {
            console.error('Error updating room:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    if (loading) return <div>Loading...</div>;

    const sortedRooms = [...rooms].sort((a, b) => a.id - b.id);

    return (
        <div className="table-responsive">
            <table className="table table-striped table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th>Room ID</th>
                    <th>Room Number</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Capacity</th>
                    <th>Price Per Night</th>
                    <th>Actions</th> {/* New actions column */}
                </tr>
                </thead>
                <tbody>
                {sortedRooms.length > 0 ? (
                    sortedRooms.map(room => (
                        <tr key={room.id}>
                            <td>{room.id}</td>
                            <td>
                                {editRoomId === room.id ? (
                                    <input
                                        type="text"
                                        value={updatedRoomNumber}
                                        onChange={(e) => setUpdatedRoomNumber(e.target.value)}
                                    />
                                ) : room.roomNumber}
                            </td>
                            <td>
                                {editRoomId === room.id ? (
                                    <input
                                        type="text"
                                        value={updatedRoomType}
                                        onChange={(e) => setUpdatedRoomType(e.target.value)}
                                    />
                                ) : room.roomType}
                            </td>
                            <td>
                                {editRoomId === room.id ? (
                                    <input
                                        type="text"
                                        value={updatedDescription}
                                        onChange={(e) => setUpdatedDescription(e.target.value)}
                                    />
                                ) : room.description}
                            </td>
                            <td>
                                {editRoomId === room.id ? (
                                    <input
                                        type="number"
                                        value={updatedCapacity}
                                        onChange={(e) => setUpdatedCapacity(e.target.value)}
                                    />
                                ) : room.capacity}
                            </td>
                            <td>
                                {editRoomId === room.id ? (
                                    <input
                                        type="number"
                                        value={updatedPricePerNight}
                                        onChange={(e) => setUpdatedPricePerNight(e.target.value)}
                                    />
                                ) : (
                                    typeof room.pricePerNight === 'number'
                                        ? `$${room.pricePerNight.toFixed(2)}`
                                        : `$${Number(room.pricePerNight || 0).toFixed(2)}`
                                )}
                            </td>
                            <td>
                                {editRoomId === room.id ? (
                                    <button className="btn btn-success" onClick={() => handleUpdate(room.id)}>Save</button>
                                ) : (
                                    <>
                                        <button className="btn btn-warning" onClick={() => handleEdit(room)}>Edit</button>
                                        <button className="btn btn-danger" onClick={() => handleDelete(room.id)}>Delete</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="7" className="text-center">No rooms found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default RoomsTable;