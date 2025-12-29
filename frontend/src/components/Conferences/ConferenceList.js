import React, { useState, useEffect } from 'react';
import { conferenceService } from '../services/api';

function ConferenceList() {
  const [conferences, setConferences] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    titre: '',
    type: 'ACADEMIQUE',
    date: '',
    duree: '',
    nombreInscrits: 0
  });

  useEffect(() => {
    fetchConferences();
  }, []);

  const fetchConferences = async () => {
    try {
      const response = await conferenceService.getAll();
      setConferences(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching conferences:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await conferenceService.create(formData);
      setFormData({
        titre: '',
        type: 'ACADEMIQUE',
        date: '',
        duree: '',
        nombreInscrits: 0
      });
      fetchConferences();
    } catch (error) {
      console.error('Error creating conference:', error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="conference-list">
      <h2>Conference Management</h2>
      
      <form onSubmit={handleSubmit} className="conference-form">
        <input
          type="text"
          placeholder="Titre"
          value={formData.titre}
          onChange={(e) => setFormData({ ...formData, titre: e.target.value })}
          required
        />
        <select
          value={formData.type}
          onChange={(e) => setFormData({ ...formData, type: e.target.value })}
        >
          <option value="ACADEMIQUE">Académique</option>
          <option value="COMMERCIALE">Commerciale</option>
        </select>
        <input
          type="date"
          value={formData.date}
          onChange={(e) => setFormData({ ...formData, date: e.target.value })}
          required
        />
        <input
          type="number"
          placeholder="Durée (minutes)"
          value={formData.duree}
          onChange={(e) => setFormData({ ...formData, duree: e.target.value })}
          required
        />
        <button type="submit">Add Conference</button>
      </form>

      <div className="conference-items">
        {conferences.map((conference) => (
          <div key={conference.id} className="conference-item">
            <h3>{conference.titre}</h3>
            <p>Type: {conference.type}</p>
            <p>Date: {conference.date}</p>
            <p>Durée: {conference.duree} minutes</p>
            <p>Inscrits: {conference.nombreInscrits}</p>
            <p>Score: {conference.score}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default ConferenceList;
