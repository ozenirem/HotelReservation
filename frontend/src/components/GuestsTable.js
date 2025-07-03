import React, { useEffect, useState } from 'react';
import axios from 'axios';

function GuestsTable() {
    const [guests, setGuests] = useState([]);
    const [loading, setLoading] = useState(true);
    const [editGuestId, setEditGuestId] = useState(null); // Track which guest is being edited
    const [updatedFirstName, setUpdatedFirstName] = useState('');
    const [updatedLastName, setUpdatedLastName] = useState('');
    const [updatedEmail, setUpdatedEmail] = useState('');
    const [updatedPhoneNumber, setUpdatedPhoneNumber] = useState('');

    useEffect(() => {
        const fetchGuests = async () => {
            try {
                const response = await axios.get('/otel-reservation/1/guests');
                setGuests(response.data);
            } catch (error) {
                console.error('Error fetching guests:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchGuests();
    }, []);

    const handleDelete = async (id) => {
        if (window.confirm('Are you sure you want to delete this guest?')) {
            try {
                await axios.delete(`/otel-reservation/1/guest/${id}`);
                setGuests(guests.filter(guest => guest.id !== id));
                alert('Guest deleted successfully!');
            } catch (error) {
                console.error('Error deleting guest:', error);
                if (error.response && error.response.status === 400) {
                    alert('Error: ' + error.response.data.message);
                }
            }
        }
    };

    const handleEdit = (guest) => {
        setEditGuestId(guest.id);
        setUpdatedFirstName(guest.firstName);
        setUpdatedLastName(guest.lastName);
        setUpdatedEmail(guest.email);
        setUpdatedPhoneNumber(guest.phoneNumber);
    };

    const handleUpdate = async (id) => {
        try {
            await axios.put(`/otel-reservation/1/guest/${id}`, {
                firstName: updatedFirstName,
                lastName: updatedLastName,
                email: updatedEmail,
                phoneNumber: updatedPhoneNumber,
            });
            setGuests(guests.map(guest =>
                guest.id === id ? { ...guest, firstName: updatedFirstName, lastName: updatedLastName, email: updatedEmail, phoneNumber: updatedPhoneNumber } : guest
            ));
            setEditGuestId(null);
            alert('Guest updated successfully!');
        } catch (error) {
            console.error('Error updating guest:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    if (loading) return <div>Loading...</div>;

    const sortedGuests = [...guests].sort((a, b) => a.id - b.id);

    return (
        <div className="table-responsive">
            <table className="table table-striped table-bordered">
                <thead className="thead-dark">
                <tr>
                    <th>ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Phone Number</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {sortedGuests.length > 0 ? (
                    sortedGuests.map(guest => (
                        <tr key={guest.id}>
                            <td>{guest.id}</td>
                            <td>
                                {editGuestId === guest.id ? (
                                    <input
                                        type="text"
                                        value={updatedFirstName}
                                        onChange={(e) => setUpdatedFirstName(e.target.value)}
                                    />
                                ) : guest.firstName}
                            </td>
                            <td>
                                {editGuestId === guest.id ? (
                                    <input
                                        type="text"
                                        value={updatedLastName}
                                        onChange={(e) => setUpdatedLastName(e.target.value)}
                                    />
                                ) : guest.lastName}
                            </td>
                            <td>
                                {editGuestId === guest.id ? (
                                    <input
                                        type="email"
                                        value={updatedEmail}
                                        onChange={(e) => setUpdatedEmail(e.target.value)}
                                    />
                                ) : guest.email}
                            </td>
                            <td>
                                {editGuestId === guest.id ? (
                                    <input
                                        type="text"
                                        value={updatedPhoneNumber}
                                        onChange={(e) => setUpdatedPhoneNumber(e.target.value)}
                                    />
                                ) : guest.phoneNumber}
                            </td>
                            <td>
                                {editGuestId === guest.id ? (
                                    <button className="btn btn-success" onClick={() => handleUpdate(guest.id)}>Save</button>
                                ) : (
                                    <>
                                        <button className="btn btn-warning" onClick={() => handleEdit(guest)}>Edit</button>
                                        <button className="btn btn-danger" onClick={() => handleDelete(guest.id)}>Delete</button>
                                    </>
                                )}
                            </td>
                        </tr>
                    ))
                ) : (
                    <tr>
                        <td colSpan="6" className="text-center">No guests found.</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    );
}

export default GuestsTable;