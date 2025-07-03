import React, { useEffect, useState } from 'react';
import axios from 'axios';

function ReservationsTable() {
    const [reservations, setReservations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [editReservationId, setEditReservationId] = useState(null);
    const [updatedGuestId, setUpdatedGuestId] = useState('');
    const [updatedRoomId, setUpdatedRoomId] = useState('');
    const [updatedCheckInDate, setUpdatedCheckInDate] = useState('');
    const [updatedCheckOutDate, setUpdatedCheckOutDate] = useState('');
    const [updatedNumberOfPeople, setUpdatedNumberOfPeople] = useState('');

    const fetchReservations = async () => {
        try {
            const response = await axios.get('/otel-reservation/1/reservations');
            setReservations(response.data);
        } catch (error) {
            console.error('Error fetching reservations:', error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchReservations();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this reservation?')) {
            try {
                await axios.delete(`/otel-reservation/1/reservation/${id}`);
                setReservations(reservations.filter(r => r.id !== id));
                alert('Reservation deleted successfully!');
            } catch (error) {
                console.error('Error deleting reservation:', error);
                if (error.response && error.response.status === 400) {
                    alert('Error: ' + error.response.data.message);
                }
            }
        }
    };

    const handleEdit = (reservation) => {
        setEditReservationId(reservation.id);
        setUpdatedGuestId(reservation.guest.id?.toString() ?? '');
        setUpdatedRoomId(reservation.room.id?.toString() ?? '');
        setUpdatedCheckInDate(reservation.checkInDate);
        setUpdatedCheckOutDate(reservation.checkOutDate);
        setUpdatedNumberOfPeople(reservation.numberOfPeople);
    };

    const handleUpdate = async (id) => {
        try {
            await axios.put(`/otel-reservation/1/reservation/${id}`, {
                guestId: Number(updatedGuestId),
                roomId: Number(updatedRoomId),
                checkInDate: updatedCheckInDate,
                checkOutDate: updatedCheckOutDate,
                numberOfPeople: Number(updatedNumberOfPeople),
            });
            await fetchReservations(); // Refresh to get updated total price
            setEditReservationId(null); // Reset edit state
            alert('Reservation updated successfully!');
        } catch (error) {
            console.error('Error updating reservation:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    if (loading) return <div>Loading...</div>;

    const sortedReservations = [...reservations].sort((a, b) => a.id - b.id);

    return (
        <div className="table-responsive">
            <table className="table table-striped table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>Guest (ID / Name)</th>
                    <th>Room (ID / RoomNumber)</th>
                    <th>Check-in Date</th>
                    <th>Check-out Date</th>
                    <th>Number of People</th>
                    <th>Total Price</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {sortedReservations.length > 0 ? (
                    sortedReservations.map(r => (
                        <tr key={r.id}>
                            <td>{r.id}</td>
                            <td>
                                {editReservationId === r.id ? (
                                    <select value={updatedGuestId} onChange={(e) => setUpdatedGuestId(e.target.value)}>
                                        <option value={r.guest.id}>
                                            {r.guest.id} - {r.guest.firstName} {r.guest.lastName}
                                        </option>
                                    </select>
                                ) : (
                                    <>
                                        {r.guest.id} - {r.guest.firstName} {r.guest.lastName}
                                    </>
                                )}
                            </td>
                            <td>
                                {editReservationId === r.id ? (
                                    <select value={updatedRoomId} onChange={(e) => setUpdatedRoomId(e.target.value)}>
                                        <option value={r.room.id}>
                                            {r.room.id} - {r.room.roomNumber}
                                        </option>
                                    </select>
                                ) : (
                                    <>
                                        {r.room.id} - {r.room.roomNumber}
                                    </>
                                )}
                            </td>
                            <td>
                                {editReservationId === r.id ? (
                                    <input
                                        type="date"
                                        value={updatedCheckInDate}
                                        onChange={(e) => setUpdatedCheckInDate(e.target.value)}
                                    />
                                ) : r.checkInDate}
                            </td>
                            <td>
                                {editReservationId === r.id ? (
                                    <input
                                        type="date"
                                        value={updatedCheckOutDate}
                                        onChange={(e) => setUpdatedCheckOutDate(e.target.value)}
                                    />
                                ) : r.checkOutDate}
                            </td>
                            <td>
                                {editReservationId === r.id ? (
                                    <input
                                        type="number"
                                        value={updatedNumberOfPeople}
                                        onChange={(e) => setUpdatedNumberOfPeople(e.target.value)}
                                    />
                                ) : r.numberOfPeople}
                            </td>
                            <td>${r.totalPrice?.toFixed(2)}</td>
                            <td>
                                {editReservationId === r.id ? (
                                    <button className="btn btn-success" onClick={() => handleUpdate(r.id)}>Save</button>
                                ) : (
                                    <>
                                        <button className="btn btn-warning" onClick={() => handleEdit(r)}>Edit</button>
                                        <button className="btn btn-danger" onClick={() => handleDelete(r.id)}>Delete</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="8" className="text-center">No reservations found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default ReservationsTable;