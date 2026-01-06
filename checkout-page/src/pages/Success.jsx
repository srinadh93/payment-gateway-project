import { useSearchParams } from 'react-router-dom';

export default function Success() {
  const [searchParams] = useSearchParams();
  const paymentId = searchParams.get('payment_id');

  return (
    <div style={{ padding: '2rem', maxWidth: '500px', margin: '2rem auto', textAlign: 'center' }} data-test-id="success-state">
      <div style={{ color: '#28a745', fontSize: '3rem', marginBottom: '1rem' }}>✓</div>
      <h2 style={{ marginBottom: '1rem' }}>Payment Successful!</h2>
      
      <div style={{ background: 'white', padding: '1rem', borderRadius: '8px', boxShadow: '0 2px 4px rgba(0,0,0,0.1)' }}>
        <p style={{ color: '#666', marginBottom: '0.5rem' }}>Payment ID:</p>
        <div data-test-id="payment-id" style={{ fontWeight: 'bold', fontSize: '1.1rem' }}>
          {paymentId || 'pay_unknown'}
        </div>
      </div>

      <p data-test-id="success-message" style={{ marginTop: '1.5rem', color: '#666' }}>
        Your payment has been processed successfully
      </p>
    </div>
  );
}