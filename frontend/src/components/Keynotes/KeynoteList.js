import React, { useState, useEffect } from 'react';
import { keynoteService } from '../services/api';

function KeynoteList() {
  const [keynotes, setKeynotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    fonction: ''
  });

  useEffect(() => {
    fetchKeynotes();
  }, []);

  const fetchKeynotes = async () => {
    try {
      const response = await keynoteService.getAll();
      setKeynotes(response.data);
      setLoading(false);
    } catch (error) {
      console.error('Error fetching keynotes:', error);
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await keynoteService.create(formData);
      setFormData({ nom: '', prenom: '', email: '', fonction: '' });
      fetchKeynotes();
    } catch (error) {
      console.error('Error creating keynote:', error);
    }
  };

  const handleDelete = async (id) => {
    try {
      await keynoteService.delete(id);
      fetchKeynotes();
    } catch (error) {
      console.error('Error deleting keynote:', error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div className="keynote-list">
      <h2>Keynote Management</h2>
      
      <form onSubmit={handleSubmit} className="keynote-form">
        <input
          type="text"
          placeholder="Nom"
          value={formData.nom}
          onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
          required
        />
        <input
          type="text"
          placeholder="Prenom"
          value={formData.prenom}
          onChange={(e) => setFormData({ ...formData, prenom: e.target.value })}
          required
        />
        <input
          type="email"
          placeholder="Email"
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          required
        />
        <input
          type="text"
          placeholder="Fonction"
          value={formData.fonction}
          onChange={(e) => setFormData({ ...formData, fonction: e.target.value })}
          required
        />
        <button type="submit">Add Keynote</button>
      </form>

      <div className="keynote-items">
        {keynotes.map((keynote) => (
          <div key={keynote.id} className="keynote-item">
            <h3>{keynote.prenom} {keynote.nom}</h3>
            <p>Email: {keynote.email}</p>
            <p>Function: {keynote.fonction}</p>
            <button onClick={() => handleDelete(keynote.id)}>Delete</button>
          </div>
        ))}
      </div>
    </div>
  );
}

export default KeynoteList;
