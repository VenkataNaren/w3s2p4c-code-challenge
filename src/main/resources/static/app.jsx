const { useState, useEffect } = React;

const LoanDetails = ({ loanId, token }) => {
  const [loan, setLoan] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!loanId) {
      setLoan(null);
      setError(null);
      return;
    }

    setError(null);
    axios
      .get(`/api/v1/loans/${loanId}`, {
        headers: token ? { Authorization: `Bearer ${token}` } : {}
      })
      .then((response) => setLoan(response.data))
      .catch((error) => {
        console.error("Error fetching loan:", error);
        setError(error);
      });
  }, [loanId, token]);

  if (!loanId) {
    return <div className="loading">Enter a loan ID to load details.</div>;
  }

  if (error) {
    return <div className="error">Error fetching loan. Check the console for details.</div>;
  }

  if (!loan) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="loan-card">
      <h3>Application for {loan.applicantName}</h3>
      <p>
        Status: <strong>{loan.status}</strong>
      </p>
    </div>
  );
};

const App = () => {
  const params = new URLSearchParams(window.location.search);
  const [loanId, setLoanId] = useState(params.get("loanId") || "");
  const [token, setToken] = useState(localStorage.getItem("loanAuthToken") || "");

  useEffect(() => {
    localStorage.setItem("loanAuthToken", token);
  }, [token]);

  return (
    <div className="container">
      <div className="card">
        <h1>Loan Details</h1>
        <label htmlFor="loanId">Loan ID</label>
        <input
          id="loanId"
          value={loanId}
          onChange={(event) => setLoanId(event.target.value)}
          placeholder="e.g. 12345"
        />
        <label htmlFor="token">Bearer Token (optional)</label>
        <input
          id="token"
          value={token}
          onChange={(event) => setToken(event.target.value)}
          placeholder="paste JWT here"
        />
        <div className="status">
          <LoanDetails loanId={loanId} token={token} />
        </div>
        <div className="footer">API endpoint: /api/v1/loans/&lt;loanId&gt;</div>
      </div>
    </div>
  );
};

ReactDOM.createRoot(document.getElementById("root")).render(<App />);
