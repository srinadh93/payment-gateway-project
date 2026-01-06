import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Transactions() {
  const navigate = useNavigate();
  const [transactions, setTransactions] = useState([
    { paymentId: 'pay_H8sK3jD9s2L1pQr', orderId: 'order_NXhj67fGH2jk9mPq', amount: 50000, method: 'upi', status: 'success', created: '2024-01-15 10:31:00' },
    { paymentId: 'pay_9sK2jD8s2L1pAz', orderId: 'order_8xhj67fGH2jk9mBx', amount: 1500, method: 'card', status: 'failed', created: '2024-01-15 11:45:00' }
  ]);

  useEffect(() => {
    const storedMerchant = localStorage.getItem('merchant');
    if (!storedMerchant) navigate('/login');
  }, [navigate]);

  return (
    <div style={{ padding: '2rem', maxWidth: '1000px', margin: '0 auto' }}>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
        <h1>Transaction History</h1>
        <button onClick={() => navigate('/dashboard')} style={{ padding: '8px 16px', background: '#6c757d', color: 'white', border: 'none', cursor: 'pointer', borderRadius: '4px' }}>Back to Dashboard</button>
      </div>
      <table data-test-id="transactions-table" style={{ width: '100%', borderCollapse: 'collapse', border: '1px solid #dee2e6' }}>
        <thead style={{ background: '#f8f9fa' }}>
          <tr>
            <th style={{ padding: '12px', textAlign: 'left' }}>Payment ID</th>
            <th style={{ padding: '12px', textAlign: 'left' }}>Order ID</th>
            <th style={{ padding: '12px', textAlign: 'left' }}>Amount</th>
            <th style={{ padding: '12px', textAlign: 'left' }}>Method</th>
            <th style={{ padding: '12px', textAlign: 'left' }}>Status</th>
            <th style={{ padding: '12px', textAlign: 'left' }}>Created</th>
          </tr>
        </thead>
        <tbody>
          {transactions.map((tx) => (
            <tr key={tx.paymentId} data-test-id="transaction-row" data-payment-id={tx.paymentId} style={{ borderBottom: '1px solid #dee2e6' }}>
              <td data-test-id="payment-id" style={{ padding: '12px' }}>{tx.paymentId}</td>
              <td data-test-id="order-id" style={{ padding: '12px' }}>{tx.orderId}</td>
              <td data-test-id="amount" style={{ padding: '12px' }}>₹{(tx.amount / 100).toFixed(2)}</td>
              <td data-test-id="method" style={{ padding: '12px' }}>{tx.method}</td>
              <td data-test-id="status" style={{ padding: '12px' }}>{tx.status}</td>
              <td data-test-id="created-at" style={{ padding: '12px' }}>{tx.created}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}