import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import KeynoteList from './components/Keynotes/KeynoteList';
import ConferenceList from './components/Conferences/ConferenceList';
import AnalyticsDashboard from './components/Analytics/AnalyticsDashboard';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <nav className="navbar">
          <h1>Conference Management System</h1>
          <ul>
            <li><Link to="/keynotes">Keynotes</Link></li>
            <li><Link to="/conferences">Conferences</Link></li>
            <li><Link to="/analytics">Analytics</Link></li>
          </ul>
        </nav>

        <div className="container">
          <Routes>
            <Route path="/" element={<KeynoteList />} />
            <Route path="/keynotes" element={<KeynoteList />} />
            <Route path="/conferences" element={<ConferenceList />} />
            <Route path="/analytics" element={<AnalyticsDashboard />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

export default App;
