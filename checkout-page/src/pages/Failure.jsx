import { useNavigate } from 'react-router-dom';

export default function Failure() {
  const navigate = useNavigate();

  return (
    <div style={{ padding: '2rem', maxWidth: '500px', margin: '2rem auto', textAlign: 'center' }} data-test-id="error-state">
      <div style={{ color: '#dc3545', fontSize: '3rem', marginBottom: '1rem' }}>✕</div>
      <h2 style={{ marginBottom: '1rem' }}>Payment Failed</h2>
      
      <p data-test-id="error-message" style={{ color: '#666', marginBottom: '2rem' }}>
        Payment could not be processed. Please try again.
      </p>

      <button 
        data-test-id="retry-button"
        onClick={() => navigate(-1)}
        style={{ background: '#007bff', color: 'white', border: 'none', padding: '10px 20px', borderRadius: '4px', cursor: 'pointer' }}
      >
        Try Again
      </button>
    </div>
  );
}