import React, { useState, useEffect } from 'react';
import ReservationsTable from './components/ReservationsTable';
import GuestsTable from './components/GuestsTable';
import RoomsTable from './components/RoomsTable';
import CreateGuest from './components/CreateGuest';
import CreateRoom from './components/CreateRoom';
import CreateReservation from './components/CreateReservation';
import {API_BASE_URL} from "./components/constants";    
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
    const [view, setView] = useState(null);
    const [guests, setGuests] = useState([]);
    const [rooms, setRooms] = useState([]);
    const [reservationsRefreshKey, setReservationsRefreshKey] = useState(0);

    useEffect(() => {
        const fetchGuests = async () => {
            const response = await axios.get('/otel-reservation/1/guests');
            setGuests(response.data);
        };

        const fetchRooms = async () => {
            const response = await axios.get('/otel-reservation/1/rooms');
            setRooms(response.data);
        };

        fetchGuests();
        fetchRooms();
    }, []);

    const handleGuestAdded = () => {
        axios.get('/otel-reservation/1/guests')
            .then(response => {
                setGuests(response.data);
                setView('guests'); // Navigate to Guests page after addition
            });
    };

    const handleRoomAdded = () => {
        axios.get('/otel-reservation/1/rooms')
            .then(response => {
                setRooms(response.data);
                setView('rooms'); // Navigate to Rooms page after addition
            });
    };

    const handleReservationAdded = () => {
        setReservationsRefreshKey(prev => prev + 1);
        setView('reservations');
    };

    const handleRoomUpdated = () => {
        setReservationsRefreshKey(prev => prev + 1);
    };

    return (
        <div className="container mt-4">
            <h1 className="text-center">Hotel Reservation System</h1>

            <div className="text-center mb-4">
                <button className="btn btn-primary mx-2" onClick={() => setView('reservations')}>Reservations</button>
                <button className="btn btn-secondary mx-2" onClick={() => setView('guests')}>Guests</button>
                <button className="btn btn-success mx-2" onClick={() => setView('rooms')}>Rooms</button>
            </div>

            {view === 'reservations' && <ReservationsTable key={reservationsRefreshKey} />}
            {view === 'guests' && <GuestsTable />}
            {view === 'rooms' && <RoomsTable onRoomUpdated={handleRoomUpdated} />}
            {view === 'createGuest' && <CreateGuest onGuestAdded={handleGuestAdded} />}
            {view === 'createRoom' && <CreateRoom onRoomAdded={handleRoomAdded} />}
            {view === 'createReservation' && <CreateReservation
                onReservationAdded={handleReservationAdded}
                guests={guests}
                rooms={rooms}
            />}

            {/* Add buttons for creating entities only when viewing tables */}
            {(view === 'guests' || view === 'rooms' || view === 'reservations') && (
                <div className="text-center mb-4 mt-4">
                    <button className="btn btn-info mx-2" onClick={() => setView('createGuest')}>Add Guest</button>
                    <button className="btn btn-info mx-2" onClick={() => setView('createRoom')}>Add Room</button>
                    <button className="btn btn-info mx-2" onClick={() => setView('createReservation')}>Add Reservation</button>
                </div>
            )}
        </div>
    );
}

export default App;