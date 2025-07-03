import React, { useState } from 'react';
import axios from 'axios';

function CreateGuest({ onGuestAdded }) {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await axios.post('/otel-reservation/1/guest', {
                firstName,
                lastName,
                email,
                phoneNumber,
            });
            onGuestAdded(); // Callback to refresh the guest list
        } catch (error) {
            console.error('Error adding guest:', error);
            if (error.response && error.response.status === 400) {
                alert('Error: ' + error.response.data.message);
            }
        }
    };

    return (
        <form onSubmit={handleSubmit} className="mb-4">
            <h2>Add Guest</h2>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="First Name"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Last Name"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="email"
                    className="form-control"
                    placeholder="Email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
            </div>
            <div className="form-group">
                <input
                    type="text"
                    className="form-control"
                    placeholder="Phone Number"
                    value={phoneNumber}
                    onChange={(e) => setPhoneNumber(e.target.value)}
                    required
                />
            </div>
            <button type="submit" className="btn btn-primary">Add Guest</button>
        </form>
    );
}

export default CreateGuest;
