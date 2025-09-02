import React, { useState, useEffect } from 'react';
import hotelAPI from '../services/hotelAPI';
import './ComprehensiveAdminPanel.css';

const ComprehensiveAdminPanel = () => {
  const [activeModule, setActiveModule] = useState('users');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [data, setData] = useState({});
  const [roomTypes, setRoomTypes] = useState([]);
  const [pagination, setPagination] = useState({
    page: 1,
    limit: 20,
    total: 0
  });
  const [formData, setFormData] = useState({});
  const [editMode, setEditMode] = useState(false);
  const [selectedId, setSelectedId] = useState(null);
  const [showForm, setShowForm] = useState(false);

  // Load room types for dropdown
  useEffect(() => {
    const loadRoomTypes = async () => {
      try {
        const result = await hotelAPI.getRoomTypes();
        if (result.success) {
          setRoomTypes(result.data || []);
        }
      } catch (error) {
        console.error('Error loading room types:', error);
      }
    };
    loadRoomTypes();
  }, []);

  // Admin modules configuration
  const adminModules = [
    {
      id: 'users',
      name: 'User Management',
      icon: '👥',
      api: {
        getAll: () => hotelAPI.getUsers(),
        create: (data) => hotelAPI.createUser(data),
        getById: (id) => hotelAPI.getUserById(id),
        update: (id, data) => hotelAPI.updateUser(id, data),
        delete: (id) => hotelAPI.deleteUser(id)
      },
      fields: [
        { name: 'username', label: 'Username', type: 'text', required: true },
        { name: 'password', label: 'Password', type: 'password', required: true },
        { name: 'fullName', label: 'Full Name', type: 'text', required: true },
        { name: 'userRole', label: 'Role', type: 'select', options: ['ADMIN', 'MANAGER', 'STAFF', 'RECEPTIONIST'], required: true },
        { name: 'isActive', label: 'Active', type: 'checkbox' }
      ],
      allowedFields: ['username', 'password', 'fullName', 'userRole', 'isActive', 'userType'],
      idField: 'userId'
    },
    {
      id: 'rooms',
      name: 'Room Management',
      icon: '🏨',
      api: {
        getAll: () => hotelAPI.getRooms(),
        create: (data) => hotelAPI.createRoom(data),
        getById: (id) => hotelAPI.getRoomById(id),
        update: (id, data) => hotelAPI.updateRoom(id, data),
        delete: (id) => hotelAPI.deleteRoom(id)
      },
      fields: [
        { name: 'roomNo', label: 'Room Number', type: 'text', required: true },
        { name: 'floor', label: 'Floor', type: 'number', required: true },
        { name: 'noOfPersons', label: 'Max Occupancy', type: 'number', required: true },
        { name: 'roomDescription', label: 'Description', type: 'text' },
        { name: 'status', label: 'Status', type: 'select', options: ['VR', 'VC', 'OC', 'OO'], required: true },
        { name: 'rate', label: 'Rate', type: 'number', required: true },
        { name: 'roomTypeId', label: 'Room Type', type: 'roomTypeSelect', required: true }
      ],
      allowedFields: ['roomNo', 'floor', 'noOfPersons', 'roomDescription', 'status', 'rate', 'roomTypeId'],
      idField: 'id'
    },
    {
      id: 'roomTypes',
      name: 'Room Types',
      icon: '🛏️',
      api: {
        getAll: () => hotelAPI.getRoomTypes(),
        create: (data) => hotelAPI.createRoomType(data),
        getById: (id) => hotelAPI.getRoomTypeById(id),
        update: (id, data) => hotelAPI.updateRoomType(id, data),
        delete: (id) => hotelAPI.deleteRoomType(id)
      },
      fields: [
        { name: 'roomTypeName', label: 'Type Name', type: 'text', required: true },
        { name: 'roomTypeDescription', label: 'Description', type: 'textarea' },
        { name: 'roomTypeRate', label: 'Base Rate', type: 'number', required: true }
      ],
      allowedFields: ['roomTypeName', 'roomTypeDescription', 'roomTypeRate'],
      idField: 'roomTypeId'
    },
    {
      id: 'taxes',
      name: 'Tax Management',
      icon: '💰',
      api: {
        getAll: () => hotelAPI.getTaxes(),
        create: (data) => hotelAPI.createTax(data),
        getById: (id) => hotelAPI.getTaxById(id),
        update: (id, data) => hotelAPI.updateTax(id, data),
        delete: (id) => hotelAPI.deleteTax(id)
      },
      fields: [
        { name: 'taxName', label: 'Tax Name', type: 'text', required: true },
        { name: 'taxPercentage', label: 'Tax Percentage', type: 'number' }
      ],
      allowedFields: ['taxName', 'taxPercentage'],
      idField: 'taxId'
    },
    {
      id: 'shifts',
      name: 'Shift Management',
      icon: '⏰',
      api: {
        getAll: () => hotelAPI.getShifts(),
        create: (data) => hotelAPI.createShift(data),
        getById: (id) => hotelAPI.getShiftById(id),
        update: (id, data) => hotelAPI.updateShift(id, data),
        delete: (id) => hotelAPI.deleteShift(id)
      },
      fields: [
        { name: 'shiftName', label: 'Shift Name', type: 'text', required: true },
        { name: 'shiftNumber', label: 'Shift Number', type: 'number', required: true },
        { name: 'date', label: 'Date', type: 'datetime-local' },
        { name: 'auditDate', label: 'Audit Date', type: 'datetime-local' }
      ],
      allowedFields: ['shiftName', 'shiftNumber', 'date', 'auditDate'],
      idField: 'id'
    },
    {
      id: 'paymentModes',
      name: 'Payment Modes',
      icon: '💳',
      api: {
        getAll: () => hotelAPI.getPaymentModes(),
        create: (data) => hotelAPI.createPaymentMode(data),
        getById: (id) => hotelAPI.getPaymentModeById(id),
        update: (id, data) => hotelAPI.updatePaymentMode(id, data),
        delete: (id) => hotelAPI.deletePaymentMode(id)
      },
      fields: [
        { name: 'modeName', label: 'Payment Mode', type: 'text', required: true },
        { name: 'settlementType', label: 'Settlement Type', type: 'text', required: true },
        { name: 'description', label: 'Description', type: 'textarea' },
        { name: 'isActive', label: 'Active', type: 'checkbox' }
      ],
      allowedFields: ['modeName', 'settlementType', 'description', 'isActive'],
      idField: 'id'
    },
    {
      id: 'planTypes',
      name: 'Plan Types',
      icon: '📋',
      api: {
        getAll: () => hotelAPI.getPlanTypes(),
        create: (data) => hotelAPI.createPlanType(data),
        getById: (id) => hotelAPI.getPlanTypeById(id),
        update: (id, data) => hotelAPI.updatePlanType(id, data),
        delete: (id) => hotelAPI.deletePlanType(id)
      },
      fields: [
        { name: 'planName', label: 'Plan Name', type: 'text', required: true },
        { name: 'planRate', label: 'Plan Rate', type: 'number', required: true },
        { name: 'description', label: 'Description', type: 'textarea' },
        { name: 'isActive', label: 'Active', type: 'checkbox' }
      ],
      allowedFields: ['planName', 'planRate', 'description', 'isActive'],
      idField: 'planId'
    },
    {
      id: 'companies',
      name: 'Company Management',
      icon: '🏢',
      api: {
        getAll: () => hotelAPI.getCompanies(),
        create: (data) => hotelAPI.createCompany(data),
        getById: (id) => hotelAPI.getCompanyById(id),
        update: (id, data) => hotelAPI.updateCompany(id, data),
        delete: (id) => hotelAPI.deleteCompany(id)
      },
      fields: [
        { name: 'companyName', label: 'Company Name', type: 'text', required: true },
        { name: 'address1', label: 'Address 1', type: 'text', required: true },
        { name: 'address2', label: 'Address 2', type: 'text' },
        { name: 'address3', label: 'Address 3', type: 'text' },
        { name: 'pincode', label: 'Pincode', type: 'text' },
        { name: 'emailId', label: 'Email', type: 'email' },
        { name: 'smsTeleNo', label: 'Phone', type: 'tel' },
        { name: 'gst', label: 'GST', type: 'text' },
        { name: 'pan', label: 'PAN', type: 'text' }
      ],
      allowedFields: ['companyName', 'address1', 'address2', 'address3', 'pincode', 'emailId', 'smsTeleNo', 'gst', 'pan'],
      idField: 'id'
    },
    {
      id: 'nationalities',
      name: 'Nationalities',
      icon: '🌍',
      api: {
        getAll: () => hotelAPI.getNationalities(),
        create: (data) => hotelAPI.createNationality(data),
        getById: (id) => hotelAPI.getNationalityById(id),
        update: (id, data) => hotelAPI.updateNationality(id, data),
        delete: (id) => hotelAPI.deleteNationality(id)
      },
      fields: [
        { name: 'countryName', label: 'Country Name', type: 'text' },
        { name: 'countryCode', label: 'Country Code', type: 'text', required: true }
      ],
      allowedFields: ['countryName', 'countryCode'],
      idField: 'id'
    },
    {
      id: 'refModes',
      name: 'Reference Modes',
      icon: '📞',
      api: {
        getAll: () => hotelAPI.getRefModes(),
        create: (data) => hotelAPI.createRefMode(data),
        getById: (id) => hotelAPI.getRefModeById(id),
        update: (id, data) => hotelAPI.updateRefMode(id, data),
        delete: (id) => hotelAPI.deleteRefMode(id)
      },
      fields: [
        { name: 'refMode', label: 'Reference Mode', type: 'text', required: true }
      ],
      allowedFields: ['refMode'],
      idField: 'refId'
    },
    {
      id: 'arrivalModes',
      name: 'Arrival Modes',
      icon: '🚗',
      api: {
        getAll: () => hotelAPI.getArrivalModes(),
        create: (data) => hotelAPI.createArrivalMode(data),
        getById: (id) => hotelAPI.getArrivalModeById(id),
        update: (id, data) => hotelAPI.updateArrivalMode(id, data),
        delete: (id) => hotelAPI.deleteArrivalMode(id)
      },
      fields: [
        { name: 'arrivalMode', label: 'Arrival Mode', type: 'text', required: true }
      ],
      allowedFields: ['arrivalMode'],
      idField: 'arrId'
    },
    {
      id: 'reservationSources',
      name: 'Reservation Sources',
      icon: '📱',
      api: {
        getAll: () => hotelAPI.getReservationSources(),
        create: (data) => hotelAPI.createReservationSource(data),
        getById: (id) => hotelAPI.getReservationSourceById(id),
        update: (id, data) => hotelAPI.updateReservationSource(id, data),
        delete: (id) => hotelAPI.deleteReservationSource(id)
      },
      fields: [
        { name: 'resSource', label: 'Source Name', type: 'text', required: true }
      ],
      allowedFields: ['resSource'],
      idField: 'resId'
    }
  ];

  // Helper function to get actual ID from item
  const getActualId = (item, moduleId) => {
    const module = adminModules.find(m => m.id === moduleId);
    if (module && module.idField) {
      return item[module.idField];
    }
    
    // Fallback ID mappings based on backend API response
    const idMappings = {
      'users': 'userId',
      'rooms': 'id',
      'roomTypes': 'roomTypeId', 
      'taxes': 'taxId',
      'shifts': 'id',
      'paymentModes': 'id',
      'planTypes': 'planId',
      'companies': 'id',
      'nationalities': 'id',
      'refModes': 'refId',
      'arrivalModes': 'arrId',
      'reservationSources': 'resId'
    };
    
    const idField = idMappings[moduleId] || 'id';
    return item[idField] || item.id;
  };

  // Helper function to clean form data based on allowed fields
  const cleanFormData = (data, allowedFields) => {
    const cleanData = {};
    allowedFields.forEach(field => {
      if (data[field] !== undefined && data[field] !== '') {
        cleanData[field] = data[field];
      }
    });
    return cleanData;
  };

  // Get current module configuration
  const getCurrentModule = () => {
    return adminModules.find(module => module.id === activeModule);
  };

  // Helper function to get room type name by ID
  const getRoomTypeName = (roomTypeId) => {
    const roomType = roomTypes.find(rt => rt.roomTypeId === parseInt(roomTypeId));
    return roomType ? roomType.roomTypeName : `ID: ${roomTypeId}`;
  };

  // Helper function to format field values for display
  const formatFieldValue = (field, value, item) => {
    if (field.type === 'checkbox') {
      return value ? '✅' : '❌';
    }
    if (field.name === 'roomTypeId' && activeModule === 'rooms') {
      return getRoomTypeName(value);
    }
    if (field.name === 'userRole' || field.name === 'role') {
      return value || '-';
    }
    return value || '-';
  };

  // Load data for current module
  const loadData = async () => {
    setLoading(true);
    setError(null);
    
    try {
      const currentModule = getCurrentModule();
      const response = await currentModule.api.getAll();
      console.log(`Loading ${currentModule.name} data:`, response);
      
      // Handle different response formats
      let dataArray = [];
      if (Array.isArray(response)) {
        dataArray = response;
      } else if (response && response.data && Array.isArray(response.data)) {
        dataArray = response.data;
      } else if (response && response.content && Array.isArray(response.content)) {
        dataArray = response.content;
      } else if (response && typeof response === 'object') {
        // If response is an object but not an array, try to find the data
        const possibleDataKeys = ['items', 'results', 'records', 'list'];
        for (const key of possibleDataKeys) {
          if (response[key] && Array.isArray(response[key])) {
            dataArray = response[key];
            break;
          }
        }
        // If still no array found, wrap the response in an array if it's a valid object
        if (dataArray.length === 0 && response.id) {
          dataArray = [response];
        }
      }
      
      setData(prevData => ({
        ...prevData,
        [activeModule]: dataArray
      }));
      
      setPagination(prev => ({
        ...prev,
        total: dataArray.length
      }));
    } catch (err) {
      console.error(`Error loading ${getCurrentModule()?.name} data:`, err);
      setError(`Failed to load ${getCurrentModule()?.name}: ${err.message || 'Unknown error'}`);
      setData(prevData => ({
        ...prevData,
        [activeModule]: []
      }));
    } finally {
      setLoading(false);
    }
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!formData || Object.keys(formData).length === 0) {
      setError('Please fill in the required fields');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const currentModule = getCurrentModule();
      const cleanedData = cleanFormData(formData, currentModule.allowedFields);
      console.log(`${editMode ? 'Updating' : 'Creating'} ${currentModule.name}:`, cleanedData);
      
      let response;
      if (editMode && selectedId) {
        response = await currentModule.api.update(selectedId, cleanedData);
      } else {
        response = await currentModule.api.create(cleanedData);
      }
      
      console.log(`${editMode ? 'Update' : 'Create'} response:`, response);
      
      // Reset form and reload data
      setFormData({});
      setShowForm(false);
      setEditMode(false);
      setSelectedId(null);
      await loadData();
    } catch (err) {
      console.error(`Error ${editMode ? 'updating' : 'creating'} ${getCurrentModule()?.name}:`, err);
      setError(`Failed to ${editMode ? 'update' : 'create'} ${getCurrentModule()?.name}: ${err.message || 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  // Handle edit
  const handleEdit = async (id) => {
    if (!id) return;
    
    setLoading(true);
    try {
      const currentModule = getCurrentModule();
      const response = await currentModule.api.getById(id);
      console.log(`Loading item ${id} for editing:`, response);
      
      // Handle different response formats
      let itemData = {};
      if (response && typeof response === 'object') {
        if (response.data && typeof response.data === 'object') {
          itemData = response.data;
        } else if (response.id) {
          // Direct object response
          itemData = response;
        }
      }
      
      setFormData(itemData);
      setSelectedId(id);
      setEditMode(true);
      setShowForm(true);
    } catch (err) {
      console.error(`Error loading item ${id} for editing:`, err);
      setError(`Failed to load item for editing: ${err.message || 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  // Handle delete
  const handleDelete = async (id) => {
    if (!id || !window.confirm('Are you sure you want to delete this item?')) return;
    
    setLoading(true);
    try {
      const currentModule = getCurrentModule();
      console.log(`Deleting ${currentModule.name} with ID: ${id}`);
      const response = await currentModule.api.delete(id);
      console.log('Delete response:', response);
      await loadData();
    } catch (err) {
      console.error(`Error deleting item ${id}:`, err);
      setError(`Failed to delete item: ${err.message || 'Unknown error'}`);
    } finally {
      setLoading(false);
    }
  };

  // Get paginated data
  const getPaginatedData = () => {
    const moduleData = data[activeModule] || [];
    const startIndex = (pagination.page - 1) * pagination.limit;
    const endIndex = startIndex + pagination.limit;
    return moduleData.slice(startIndex, endIndex);
  };

  // Calculate total pages
  const totalPages = Math.ceil((data[activeModule]?.length || 0) / pagination.limit);

  // Load data when module changes
  useEffect(() => {
    loadData();
  }, [activeModule]);

  const currentModule = getCurrentModule();

  return (
    <div className="comprehensive-admin-panel">
      <div className="admin-header">
        <h1>🔧 Hotel Administration Panel</h1>
        <p>Manage all hotel operations from one central location</p>
      </div>

      <div className="admin-content">
        {/* Module Navigation */}
        <div className="admin-sidebar">
          <h3>Admin Modules</h3>
          <div className="module-list">
            {adminModules.map(module => (
              <button
                key={module.id}
                className={`module-button ${activeModule === module.id ? 'active' : ''}`}
                onClick={() => setActiveModule(module.id)}
                disabled={loading}
              >
                <span className="module-icon">{module.icon}</span>
                <span className="module-name">{module.name}</span>
              </button>
            ))}
          </div>
        </div>

        {/* Main Content */}
        <div className="admin-main">
          {currentModule && (
            <>
              {/* Module Header */}
              <div className="module-header">
                <h2>{currentModule.icon} {currentModule.name}</h2>
                <button
                  className="btn btn-primary"
                  onClick={() => {
                    setFormData({});
                    setEditMode(false);
                    setSelectedId(null);
                    setShowForm(true);
                  }}
                  disabled={loading}
                >
                  ➕ Add New
                </button>
              </div>

              {/* Error Display */}
              {error && (
                <div className="error-message">
                  ❌ {error}
                  <button onClick={() => setError(null)} className="close-error">×</button>
                </div>
              )}

              {/* Data Table */}
              <div className="data-table-container">
                {loading ? (
                  <div className="loading-spinner">🔄 Loading...</div>
                ) : (
                  <>
                    <table className="data-table">
                      <thead>
                        <tr>
                          {currentModule.fields.slice(0, 4).map(field => (
                            <th key={field.name}>{field.label}</th>
                          ))}
                          <th>Actions</th>
                        </tr>
                      </thead>
                      <tbody>
                        {getPaginatedData().map((item, index) => {
                          const actualId = getActualId(item, activeModule);
                          const uniqueId = `${activeModule}-${actualId || index}`;
                          return (
                            <tr key={uniqueId}>
                              {currentModule.fields.slice(0, 4).map((field, fieldIndex) => (
                                <td key={`${uniqueId}-${field.name}-${fieldIndex}`}>
                                  {formatFieldValue(field, item[field.name], item)}
                                </td>
                              ))}
                              <td className="actions">
                                <button
                                  className="btn btn-edit"
                                  onClick={() => handleEdit(getActualId(item, activeModule))}
                                  disabled={loading}
                                >
                                  ✏️
                                </button>
                                <button
                                  className="btn btn-delete"
                                  onClick={() => handleDelete(getActualId(item, activeModule))}
                                  disabled={loading}
                                >
                                  🗑️
                                </button>
                              </td>
                            </tr>
                          );
                        })}
                      </tbody>
                    </table>

                    {/* Pagination */}
                    <div className="pagination">
                      <button
                        className="btn btn-pagination"
                        onClick={() => setPagination(prev => ({ ...prev, page: Math.max(1, prev.page - 1) }))}
                        disabled={pagination.page === 1 || loading}
                      >
                        ⬅️ Previous
                      </button>
                      
                      <span className="page-info">
                        Page {pagination.page} of {totalPages} 
                        ({data[activeModule]?.length || 0} total items)
                      </span>
                      
                      <button
                        className="btn btn-pagination"
                        onClick={() => setPagination(prev => ({ ...prev, page: Math.min(totalPages, prev.page + 1) }))}
                        disabled={pagination.page >= totalPages || loading}
                      >
                        Next ➡️
                      </button>
                    </div>
                  </>
                )}
              </div>
            </>
          )}
        </div>
      </div>

      {/* Form Modal */}
      {showForm && currentModule && (
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="modal-header">
              <h3>{editMode ? 'Edit' : 'Add New'} {currentModule.name}</h3>
              <button
                className="close-button"
                onClick={() => {
                  setShowForm(false);
                  setFormData({});
                  setEditMode(false);
                  setSelectedId(null);
                }}
              >
                ×
              </button>
            </div>
            
            <form onSubmit={handleSubmit} className="admin-form">
              {currentModule.fields.map((field, index) => (
                <div key={`${activeModule}-form-${field.name}-${index}`} className="form-group">
                  <label htmlFor={field.name}>
                    {field.label} {field.required && <span className="required">*</span>}
                  </label>
                  
                  {field.type === 'select' ? (
                    <select
                      id={field.name}
                      name={field.name}
                      value={formData[field.name] || ''}
                      onChange={(e) => setFormData(prev => ({ ...prev, [field.name]: e.target.value }))}
                      required={field.required}
                    >
                      <option value="">Select {field.label}</option>
                      {field.options?.map(option => (
                        <option key={option} value={option}>{option}</option>
                      ))}
                    </select>
                  ) : field.type === 'roomTypeSelect' ? (
                    <select
                      id={field.name}
                      name={field.name}
                      value={formData[field.name] || ''}
                      onChange={(e) => setFormData(prev => ({ ...prev, [field.name]: e.target.value }))}
                      required={field.required}
                    >
                      <option value="">Select Room Type</option>
                      {roomTypes.map(roomType => (
                        <option key={roomType.roomTypeId} value={roomType.roomTypeId}>
                          {roomType.roomTypeName} (ID: {roomType.roomTypeId})
                        </option>
                      ))}
                    </select>
                  ) : field.type === 'textarea' ? (
                    <textarea
                      id={field.name}
                      name={field.name}
                      value={formData[field.name] || ''}
                      onChange={(e) => setFormData(prev => ({ ...prev, [field.name]: e.target.value }))}
                      required={field.required}
                      rows={3}
                    />
                  ) : field.type === 'checkbox' ? (
                    <input
                      type="checkbox"
                      id={field.name}
                      name={field.name}
                      checked={formData[field.name] || false}
                      onChange={(e) => setFormData(prev => ({ ...prev, [field.name]: e.target.checked }))}
                    />
                  ) : (
                    <input
                      type={field.type}
                      id={field.name}
                      name={field.name}
                      value={formData[field.name] || ''}
                      onChange={(e) => setFormData(prev => ({ ...prev, [field.name]: e.target.value }))}
                      required={field.required}
                    />
                  )}
                </div>
              ))}
              
              <div className="form-actions">
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => {
                    setShowForm(false);
                    setFormData({});
                    setEditMode(false);
                    setSelectedId(null);
                  }}
                  disabled={loading}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={loading}
                >
                  {loading ? '⏳ Saving...' : (editMode ? 'Update' : 'Create')}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default ComprehensiveAdminPanel;
