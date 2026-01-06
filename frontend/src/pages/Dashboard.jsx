import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Dashboard() {
  const navigate = useNavigate();
  const [merchant, setMerchant] = useState(null);
  const [stats, setStats] = useState({ totalTransactions: 0, totalAmount: 0, successRate: 0 });

  useEffect(() => {
    const storedMerchant = localStorage.getItem('merchant');
    if (!storedMerchant) {
      navigate('/login');
      return;
    }
    setMerchant(JSON.parse(storedMerchant));
  }, [navigate]);

  if (!merchant) return <div>Loading...</div>;

  return (
    <div style={{ padding: '2rem', maxWidth: '800px', margin: '0 auto' }} data-test-id="dashboard">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Merchant Dashboard</h1>
        <button onClick={() => { localStorage.removeItem('merchant'); navigate('/login'); }} style={{ padding: '8px 16px', background: '#dc3545', color: 'white', border: 'none', cursor: 'pointer' }}>Logout</button>
      </div>
      <div data-test-id="api-credentials" style={{ background: '#f8f9fa', padding: '1.5rem', borderRadius: '8px', marginBottom: '2rem', border: '1px solid #dee2e6' }}>
        <h3>API Credentials</h3>
        <div style={{ marginBottom: '10px' }}><strong>API Key: </strong><code data-test-id="api-key">{merchant.apiKey}</code></div>
        <div><strong>API Secret: </strong><code data-test-id="api-secret">{merchant.apiSecret}</code></div>
      </div>
      <div data-test-id="stats-container" style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: '1rem', marginBottom: '2rem' }}>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px', textAlign: 'center' }}><h3>Transactions</h3><div data-test-id="total-transactions">{stats.totalTransactions}</div></div>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px', textAlign: 'center' }}><h3>Total Volume</h3><div data-test-id="total-amount">₹{(stats.totalAmount / 100).toFixed(2)}</div></div>
        <div style={{ padding: '1rem', border: '1px solid #ccc', borderRadius: '8px', textAlign: 'center' }}><h3>Success Rate</h3><div data-test-id="success-rate">{stats.successRate}%</div></div>
      </div>
      <div>
        <button onClick={() => navigate('/dashboard/transactions')} style={{ padding: '10px 20px', background: '#28a745', color: 'white', border: 'none', cursor: 'pointer' }}>View Transactions History</button>
      </div>
    </div>
  );
}