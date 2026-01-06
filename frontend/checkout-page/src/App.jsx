import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Checkout from './Checkout';
import Success from './Success';
import Failure from './Failure';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* THIS IS THE MISSING LINE THAT FIXES YOUR ERROR: */}
        <Route path="/" element={<Checkout />} />
        
        <Route path="/success" element={<Success />} />
        <Route path="/failure" element={<Failure />} />
      </Routes>
    </BrowserRouter>
  );
}