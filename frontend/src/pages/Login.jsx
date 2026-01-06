import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = (e) => {
    e.preventDefault();
    if (email === 'test@example.com') {
      const merchantData = {
        email: email,
        apiKey: 'key_test_abc123',
        apiSecret: 'secret_test_xyz789'
      };
      localStorage.setItem('merchant', JSON.stringify(merchantData));
      navigate('/dashboard');
    } else {
      alert('Invalid Credentials! Use test@example.com');
    }
  };

  return (
    <div style={{ padding: '2rem', maxWidth: '400px', margin: '0 auto' }}>
      <h2>Merchant Login</h2>
      <form onSubmit={handleLogin} data-test-id="login-form">
        <div style={{ marginBottom: '1rem' }}>
          <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} data-test-id="email-input" required style={{ width: '100%', padding: '8px' }} />
        </div>
        <div style={{ marginBottom: '1rem' }}>
          <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} data-test-id="password-input" required style={{ width: '100%', padding: '8px' }} />
        </div>
        <button type="submit" data-test-id="login-button" style={{ width: '100%', padding: '10px', background: '#007bff', color: 'white', border: 'none', cursor: 'pointer' }}>Login</button>
      </form>
      <div style={{ marginTop: '20px', fontSize: '0.9em', color: '#666' }}>
        <p><strong>Test Credentials:</strong></p>
        <p>Email: test@example.com</p>
      </div>
    </div>
  );
}