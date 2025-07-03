# Otel Reservation Frontend

## How to Run

1. Make sure your backend API is running at `http://localhost:9090`.

2. In this `frontend` directory, install dependencies (if not already):

   ```
   npm install
   ```

3. Start the frontend development server:

   ```
   npm start
   ```

4. Open your browser and go to [http://localhost:3000](http://localhost:3000)

> The frontend will connect to your backend API to list/add guests, rooms, and reservations.
> The frontend fetches data from:
> - `http://localhost:9090/otel-reservation/1/reservations`
> - `http://localhost:9090/otel-reservation/1/guests`
> - `http://localhost:9090/otel-reservation/1/rooms`
