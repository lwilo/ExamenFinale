import React, { useState, useEffect } from 'react';
import { analyticsService } from '../services/api';

function AnalyticsDashboard() {
  const [analytics, setAnalytics] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchAnalytics();
    const interval = setInterval(fetchAnalytics, 5000); // Refresh every 5 seconds
    return () => clearInterval(interval);
  }, []);

  const fetchAnalytics = async () => {
    try {
      const response = await analyticsService.getRealTimeAnalytics();
      setAnalytics(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching analytics:', error);
      setLoading(false);
    }
  };

  if (loading) return <div>Loading analytics...</div>;

  return (
    <div className="analytics-dashboard">
      <h2>Real-Time Analytics (5-second windows)</h2>
      <div className="analytics-items">
        {analytics.length === 0 ? (
          <p>No analytics data available yet</p>
        ) : (
          analytics.map((item, index) => (
            <div key={index} className="analytics-item">
              <h3>Conference: {item.conferenceId}</h3>
              <p>Total Reviews: {item.totalReviews}</p>
              <p>Sum of Notes: {item.sumOfNotes}</p>
              <p>Average Note: {item.averageNote?.toFixed(2)}</p>
              <p>Window: {new Date(item.windowStart).toLocaleTimeString()} - {new Date(item.windowEnd).toLocaleTimeString()}</p>
            </div>
          ))
        )}
      </div>
    </div>
  );
}

export default AnalyticsDashboard;
