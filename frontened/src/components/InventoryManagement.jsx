import React, { useState, useEffect } from "react";
import hotelAPI from "../services/hotelAPI";
import { formatCurrency } from "../utils/numberGenerators";

export default function InventoryManagement() {
  const [inventory, setInventory] = useState([]);
  const [categories, setCategories] = useState([]);
  const [suppliers, setSuppliers] = useState([]);
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("items");
  const [showAddItem, setShowAddItem] = useState(false);
  const [showAddCategory, setShowAddCategory] = useState(false);
  const [showAddSupplier, setShowAddSupplier] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState("All");
  const [newItem, setNewItem] = useState({
    name: "",
    category: "",
    description: "",
    unit: "",
    costPrice: "",
    sellingPrice: "",
    reorderLevel: "",
    supplierId: ""
  });
  const [newCategory, setNewCategory] = useState({
    name: "",
    description: ""
  });
  const [newSupplier, setNewSupplier] = useState({
    name: "",
    contact: "",
    email: "",
    address: ""
  });

  useEffect(() => {
    loadInventoryData();
  }, []);

  const loadInventoryData = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Load inventory items with fallback
      try {
        const inventoryResult = await hotelAPI.getInventoryItems();
        if (inventoryResult && inventoryResult.success && Array.isArray(inventoryResult.data)) {
          setInventory(inventoryResult.data);
        } else {
          setInventory(generateDemoInventory());
        }
      } catch (err) {
        console.warn("Inventory: getInventoryItems failed, using demo data");
        setInventory(generateDemoInventory());
      }

      // Load categories with fallback
      try {
        const categoriesResult = await hotelAPI.getInventoryCategories();
        if (categoriesResult && categoriesResult.success && Array.isArray(categoriesResult.data)) {
          setCategories(categoriesResult.data);
        } else {
          setCategories(generateDemoCategories());
        }
      } catch (err) {
        console.warn("Inventory: getInventoryCategories failed, using demo data");
        setCategories(generateDemoCategories());
      }

      // Load suppliers with fallback
      try {
        const suppliersResult = await hotelAPI.getSuppliers();
        if (suppliersResult && suppliersResult.success && Array.isArray(suppliersResult.data)) {
          setSuppliers(suppliersResult.data);
        } else {
          setSuppliers(generateDemoSuppliers());
        }
      } catch (err) {
        console.warn("Inventory: getSuppliers failed, using demo data");
        setSuppliers(generateDemoSuppliers());
      }

      // Load inventory transactions with fallback
      try {
        const transactionsResult = await hotelAPI.getInventoryTransactions();
        if (transactionsResult && transactionsResult.success && Array.isArray(transactionsResult.data)) {
          setTransactions(transactionsResult.data);
        } else {
          setTransactions(generateDemoTransactions());
        }
      } catch (err) {
        console.warn("Inventory: getInventoryTransactions failed, using demo data");
        setTransactions(generateDemoTransactions());
      }

    } catch (err) {
      console.error("Inventory: Complete data loading failed", err);
      setError("Backend connection failed - Using demo data");
      // Set all demo data on complete failure
      setInventory(generateDemoInventory());
      setCategories(generateDemoCategories());
      setSuppliers(generateDemoSuppliers());
      setTransactions(generateDemoTransactions());
    } finally {
      setLoading(false);
    }
  };

  const generateDemoInventory = () => [
    {
      id: 1,
      name: "Bath Towels",
      category: "Linens",
      description: "Premium cotton bath towels",
      unit: "pieces",
      currentStock: 25,
      costPrice: 15.00,
      sellingPrice: 30.00,
      reorderLevel: 10,
      supplierId: 1
    },
    {
      id: 2,
      name: "Toilet Paper",
      category: "Amenities",
      description: "2-ply toilet paper rolls",
      unit: "rolls",
      currentStock: 5,
      costPrice: 2.50,
      sellingPrice: 5.00,
      reorderLevel: 20,
      supplierId: 1
    },
    {
      id: 3,
      name: "Shampoo Bottles",
      category: "Amenities",
      description: "Hotel-grade shampoo bottles",
      unit: "bottles",
      currentStock: 30,
      costPrice: 3.00,
      sellingPrice: 8.00,
      reorderLevel: 15,
      supplierId: 2
    },
    {
      id: 4,
      name: "Bed Sheets",
      category: "Linens",
      description: "Cotton bed sheets sets",
      unit: "sets",
      currentStock: 40,
      costPrice: 25.00,
      sellingPrice: 50.00,
      reorderLevel: 10,
      supplierId: 1
    },
    {
      id: 5,
      name: "Air Freshener",
      category: "Cleaning",
      description: "Room air freshener spray",
      unit: "bottles",
      currentStock: 8,
      costPrice: 5.00,
      sellingPrice: 12.00,
      reorderLevel: 12,
      supplierId: 3
    }
  ];

  const generateDemoCategories = () => [
    {
      id: 1,
      name: "Linens",
      description: "Towels, sheets, and other textile items"
    },
    {
      id: 2,
      name: "Amenities",
      description: "Guest bathroom and room amenities"
    },
    {
      id: 3,
      name: "Cleaning",
      description: "Cleaning supplies and maintenance items"
    },
    {
      id: 4,
      name: "Food & Beverage",
      description: "Restaurant and minibar supplies"
    }
  ];

  const generateDemoSuppliers = () => [
    {
      id: 1,
      name: "Hotel Linens Co.",
      contact: "+1-555-0101",
      email: "orders@hotellinens.com",
      address: "123 Textile Drive, City, State 12345"
    },
    {
      id: 2,
      name: "Premium Amenities Ltd.",
      contact: "+1-555-0202",
      email: "sales@premiumamenities.com",
      address: "456 Supply Street, City, State 67890"
    },
    {
      id: 3,
      name: "CleanPro Supplies",
      contact: "+1-555-0303",
      email: "info@cleanpro.com",
      address: "789 Industrial Blvd, City, State 11111"
    }
  ];

  const generateDemoTransactions = () => [
    {
      date: "2024-01-15",
      itemName: "Bath Towels",
      type: "IN",
      quantity: 50,
      user: "admin",
      notes: "Monthly restock"
    },
    {
      date: "2024-01-16",
      itemName: "Toilet Paper",
      type: "OUT",
      quantity: 15,
      user: "housekeeping",
      notes: "Room restocking"
    },
    {
      date: "2024-01-17",
      itemName: "Shampoo Bottles",
      type: "IN",
      quantity: 100,
      user: "manager",
      notes: "Bulk purchase"
    },
    {
      date: "2024-01-18",
      itemName: "Air Freshener",
      type: "OUT",
      quantity: 5,
      user: "housekeeping",
      notes: "Room maintenance"
    }
  ];

  const addInventoryItem = async () => {
    try {
      const result = await hotelAPI.createInventoryItem(newItem);
      if (result && result.success) {
        loadInventoryData();
        setShowAddItem(false);
        setNewItem({
          name: "",
          category: "",
          description: "",
          unit: "",
          costPrice: "",
          sellingPrice: "",
          reorderLevel: "",
          supplierId: ""
        });
        alert("Item added successfully!");
      } else {
        alert("Failed to add item: " + (result?.error || "Unknown error"));
      }
    } catch (err) {
      console.error("Error adding item:", err);
      alert("Error adding item - Backend may be unavailable");
    }
  };

  const updateStock = async (itemId, quantity, type) => {
    try {
      const result = await hotelAPI.updateInventoryStock(itemId, { quantity, type });
      if (result && result.success) {
        loadInventoryData();
        alert("Stock updated successfully!");
      } else {
        alert("Failed to update stock: " + (result?.error || "Unknown error"));
      }
    } catch (err) {
      console.error("Error updating stock:", err);
      alert("Error updating stock - Backend may be unavailable");
    }
  };

  const addCategory = async () => {
    try {
      const result = await hotelAPI.createInventoryCategory(newCategory);
      if (result && result.success) {
        loadInventoryData();
        setShowAddCategory(false);
        setNewCategory({ name: "", description: "" });
        alert("Category added successfully!");
      } else {
        alert("Failed to add category: " + (result?.error || "Unknown error"));
      }
    } catch (err) {
      console.error("Error adding category:", err);
      alert("Error adding category - Backend may be unavailable");
    }
  };

  const addSupplier = async () => {
    try {
      const result = await hotelAPI.createSupplier(newSupplier);
      if (result && result.success) {
        loadInventoryData();
        setShowAddSupplier(false);
        setNewSupplier({ name: "", contact: "", email: "", address: "" });
        alert("Supplier added successfully!");
      } else {
        alert("Failed to add supplier: " + (result?.error || "Unknown error"));
      }
    } catch (err) {
      console.error("Error adding supplier:", err);
      alert("Error adding supplier - Backend may be unavailable");
    }
  };

  const generatePurchaseOrder = async (items) => {
    try {
      const result = await hotelAPI.generatePurchaseOrder(items);
      if (result && result.success) {
        alert("Purchase order generated successfully!");
        // You could open the PO in a new window or download it
        if (result.data && result.data.poUrl) {
          window.open(result.data.poUrl, '_blank');
        }
      } else {
        alert("Failed to generate purchase order: " + (result?.error || "Unknown error"));
      }
    } catch (err) {
      console.error("Error generating purchase order:", err);
      alert("Error generating purchase order - Backend may be unavailable");
    }
  };

  if (loading) {
    return <div style={{ padding: 20, textAlign: "center" }}>Loading inventory data...</div>;
  }

  if (error) {
    return (
      <div style={{ padding: 20, textAlign: "center", color: "red" }}>
        <p>Error: {error}</p>
        <button onClick={loadInventoryData}>Retry</button>
      </div>
    );
  }

  const filteredInventory = selectedCategory === "All" 
    ? inventory 
    : inventory.filter(item => item.category === selectedCategory);

  const lowStockItems = inventory.filter(item => item.currentStock <= item.reorderLevel);

  return (
    <div style={{ padding: 20 }}>
      <h2>Inventory Management System</h2>
      
      {/* Alert for low stock items */}
      {lowStockItems.length > 0 && (
        <div style={{
          backgroundColor: "#fff3cd",
          border: "1px solid #ffeaa7",
          borderRadius: 4,
          padding: 15,
          marginBottom: 20,
          color: "#856404"
        }}>
          <strong>Low Stock Alert:</strong> {lowStockItems.length} item(s) are running low on stock!
          <button 
            onClick={() => generatePurchaseOrder(lowStockItems.map(item => ({ itemId: item.id, quantity: item.reorderLevel * 2 })))}
            style={{ marginLeft: 15, padding: "5px 10px", backgroundColor: "#ffc107", border: "none", borderRadius: 4 }}
          >
            Generate Purchase Order
          </button>
        </div>
      )}

      {/* Tab Navigation */}
      <div style={{ marginBottom: 20, borderBottom: "1px solid #ccc" }}>
        {[
          { key: "items", label: "Inventory Items" },
          { key: "categories", label: "Categories" },
          { key: "suppliers", label: "Suppliers" },
          { key: "transactions", label: "Transactions" },
          { key: "reports", label: "Reports" }
        ].map(tab => (
          <button 
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            style={{
              padding: "10px 20px",
              border: "none",
              background: activeTab === tab.key ? "#5754e8" : "transparent",
              color: activeTab === tab.key ? "white" : "black",
              cursor: "pointer"
            }}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Inventory Items Tab */}
      {activeTab === "items" && (
        <div>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
            <div>
              <label style={{ marginRight: 10 }}>Filter by Category:</label>
              <select 
                value={selectedCategory}
                onChange={(e) => setSelectedCategory(e.target.value)}
                style={{ padding: "5px 10px", marginRight: 20 }}
              >
                <option value="All">All Categories</option>
                {categories.map(cat => (
                  <option key={cat.id} value={cat.name}>{cat.name}</option>
                ))}
              </select>
            </div>
            <button 
              onClick={() => setShowAddItem(true)}
              style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
            >
              Add New Item
            </button>
          </div>

          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f8f9fa" }}>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Item Name</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Category</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Current Stock</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Unit</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Cost Price</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Selling Price</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Reorder Level</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredInventory.map(item => (
                <tr key={item.id} style={{ backgroundColor: item.currentStock <= item.reorderLevel ? "#fff3cd" : "white" }}>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{item.name}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{item.category}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>
                    {item.currentStock}
                    {item.currentStock <= item.reorderLevel && (
                      <span style={{ color: "red", marginLeft: 5 }}>⚠️</span>
                    )}
                  </td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{item.unit}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{formatCurrency(item.costPrice)}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{formatCurrency(item.sellingPrice)}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{item.reorderLevel}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>
                    <button 
                      onClick={() => {
                        const quantity = prompt("Enter quantity to add:");
                        if (quantity && parseInt(quantity) > 0) {
                          updateStock(item.id, parseInt(quantity), "ADD");
                        }
                      }}
                      style={{ padding: "5px 10px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4, marginRight: 5 }}
                    >
                      Add Stock
                    </button>
                    <button 
                      onClick={() => {
                        const quantity = prompt("Enter quantity to remove:");
                        if (quantity && parseInt(quantity) > 0) {
                          updateStock(item.id, parseInt(quantity), "REMOVE");
                        }
                      }}
                      style={{ padding: "5px 10px", backgroundColor: "#dc3545", color: "white", border: "none", borderRadius: 4 }}
                    >
                      Remove Stock
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Categories Tab */}
      {activeTab === "categories" && (
        <div>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
            <h3>Inventory Categories</h3>
            <button 
              onClick={() => setShowAddCategory(true)}
              style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
            >
              Add Category
            </button>
          </div>

          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(300px, 1fr))", gap: 15 }}>
            {categories.map(category => (
              <div key={category.id} style={{
                border: "1px solid #ddd",
                borderRadius: 8,
                padding: 15,
                backgroundColor: "#f8f9fa"
              }}>
                <h4>{category.name}</h4>
                <p>{category.description}</p>
                <p><strong>Items:</strong> {inventory.filter(item => item.category === category.name).length}</p>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* Suppliers Tab */}
      {activeTab === "suppliers" && (
        <div>
          <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: 20 }}>
            <h3>Suppliers</h3>
            <button 
              onClick={() => setShowAddSupplier(true)}
              style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
            >
              Add Supplier
            </button>
          </div>

          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f8f9fa" }}>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Supplier Name</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Contact</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Email</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Address</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Items Supplied</th>
              </tr>
            </thead>
            <tbody>
              {suppliers.map(supplier => (
                <tr key={supplier.id}>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{supplier.name}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{supplier.contact}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{supplier.email}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{supplier.address}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>
                    {inventory.filter(item => item.supplierId === supplier.id).length}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Transactions Tab */}
      {activeTab === "transactions" && (
        <div>
          <h3>Inventory Transactions</h3>
          <table style={{ width: "100%", borderCollapse: "collapse" }}>
            <thead>
              <tr style={{ backgroundColor: "#f8f9fa" }}>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Date</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Item</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Type</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Quantity</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>User</th>
                <th style={{ padding: 10, border: "1px solid #ddd" }}>Notes</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction, index) => (
                <tr key={index}>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.date}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.itemName}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>
                    <span style={{
                      padding: "2px 8px",
                      borderRadius: 4,
                      backgroundColor: transaction.type === "IN" ? "#d4edda" : "#f8d7da",
                      color: transaction.type === "IN" ? "#155724" : "#721c24"
                    }}>
                      {transaction.type}
                    </span>
                  </td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.quantity}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.user}</td>
                  <td style={{ padding: 10, border: "1px solid #ddd" }}>{transaction.notes}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Reports Tab */}
      {activeTab === "reports" && (
        <div>
          <h3>Inventory Reports</h3>
          <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(250px, 1fr))", gap: 20 }}>
            <div style={{ border: "1px solid #ddd", borderRadius: 8, padding: 15, textAlign: "center" }}>
              <h4>Total Items</h4>
              <p style={{ fontSize: "24px", fontWeight: "bold" }}>{inventory.length}</p>
            </div>
            <div style={{ border: "1px solid #ddd", borderRadius: 8, padding: 15, textAlign: "center" }}>
              <h4>Low Stock Items</h4>
              <p style={{ fontSize: "24px", fontWeight: "bold", color: "#dc3545" }}>{lowStockItems.length}</p>
            </div>
            <div style={{ border: "1px solid #ddd", borderRadius: 8, padding: 15, textAlign: "center" }}>
              <h4>Total Value</h4>
              <p style={{ fontSize: "24px", fontWeight: "bold" }}>
                {formatCurrency(inventory.reduce((total, item) => total + (item.currentStock * item.costPrice), 0))}
              </p>
            </div>
            <div style={{ border: "1px solid #ddd", borderRadius: 8, padding: 15, textAlign: "center" }}>
              <h4>Categories</h4>
              <p style={{ fontSize: "24px", fontWeight: "bold" }}>{categories.length}</p>
            </div>
          </div>
        </div>
      )}

      {/* Add Item Modal */}
      {showAddItem && (
        <div style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: "white",
            padding: 30,
            borderRadius: 8,
            maxWidth: 500,
            width: "90%"
          }}>
            <h3>Add New Inventory Item</h3>
            <div style={{ marginBottom: 15 }}>
              <label>Item Name:</label>
              <input 
                type="text"
                value={newItem.name}
                onChange={(e) => setNewItem({...newItem, name: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Category:</label>
              <select 
                value={newItem.category}
                onChange={(e) => setNewItem({...newItem, category: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              >
                <option value="">Select Category</option>
                {categories.map(cat => (
                  <option key={cat.id} value={cat.name}>{cat.name}</option>
                ))}
              </select>
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Description:</label>
              <textarea 
                value={newItem.description}
                onChange={(e) => setNewItem({...newItem, description: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5, height: 60 }}
              />
            </div>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, marginBottom: 15 }}>
              <div>
                <label>Unit:</label>
                <input 
                  type="text"
                  value={newItem.unit}
                  onChange={(e) => setNewItem({...newItem, unit: e.target.value})}
                  style={{ width: "100%", padding: 8, marginTop: 5 }}
                />
              </div>
              <div>
                <label>Reorder Level:</label>
                <input 
                  type="number"
                  value={newItem.reorderLevel}
                  onChange={(e) => setNewItem({...newItem, reorderLevel: e.target.value})}
                  style={{ width: "100%", padding: 8, marginTop: 5 }}
                />
              </div>
            </div>
            <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 10, marginBottom: 15 }}>
              <div>
                <label>Cost Price:</label>
                <input 
                  type="number"
                  step="0.01"
                  value={newItem.costPrice}
                  onChange={(e) => setNewItem({...newItem, costPrice: e.target.value})}
                  style={{ width: "100%", padding: 8, marginTop: 5 }}
                />
              </div>
              <div>
                <label>Selling Price:</label>
                <input 
                  type="number"
                  step="0.01"
                  value={newItem.sellingPrice}
                  onChange={(e) => setNewItem({...newItem, sellingPrice: e.target.value})}
                  style={{ width: "100%", padding: 8, marginTop: 5 }}
                />
              </div>
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Supplier:</label>
              <select 
                value={newItem.supplierId}
                onChange={(e) => setNewItem({...newItem, supplierId: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              >
                <option value="">Select Supplier</option>
                {suppliers.map(supplier => (
                  <option key={supplier.id} value={supplier.id}>{supplier.name}</option>
                ))}
              </select>
            </div>
            
            <div style={{ textAlign: "right" }}>
              <button 
                onClick={() => setShowAddItem(false)}
                style={{ padding: "10px 20px", backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4, marginRight: 10 }}
              >
                Cancel
              </button>
              <button 
                onClick={addInventoryItem}
                style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
              >
                Add Item
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Add Category Modal */}
      {showAddCategory && (
        <div style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: "white",
            padding: 30,
            borderRadius: 8,
            maxWidth: 400,
            width: "90%"
          }}>
            <h3>Add New Category</h3>
            <div style={{ marginBottom: 15 }}>
              <label>Category Name:</label>
              <input 
                type="text"
                value={newCategory.name}
                onChange={(e) => setNewCategory({...newCategory, name: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Description:</label>
              <textarea 
                value={newCategory.description}
                onChange={(e) => setNewCategory({...newCategory, description: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5, height: 80 }}
              />
            </div>
            
            <div style={{ textAlign: "right" }}>
              <button 
                onClick={() => setShowAddCategory(false)}
                style={{ padding: "10px 20px", backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4, marginRight: 10 }}
              >
                Cancel
              </button>
              <button 
                onClick={addCategory}
                style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
              >
                Add Category
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Add Supplier Modal */}
      {showAddSupplier && (
        <div style={{
          position: "fixed",
          top: 0,
          left: 0,
          right: 0,
          bottom: 0,
          backgroundColor: "rgba(0,0,0,0.5)",
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          zIndex: 1000
        }}>
          <div style={{
            backgroundColor: "white",
            padding: 30,
            borderRadius: 8,
            maxWidth: 500,
            width: "90%"
          }}>
            <h3>Add New Supplier</h3>
            <div style={{ marginBottom: 15 }}>
              <label>Supplier Name:</label>
              <input 
                type="text"
                value={newSupplier.name}
                onChange={(e) => setNewSupplier({...newSupplier, name: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Contact Number:</label>
              <input 
                type="text"
                value={newSupplier.contact}
                onChange={(e) => setNewSupplier({...newSupplier, contact: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Email:</label>
              <input 
                type="email"
                value={newSupplier.email}
                onChange={(e) => setNewSupplier({...newSupplier, email: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5 }}
              />
            </div>
            <div style={{ marginBottom: 15 }}>
              <label>Address:</label>
              <textarea 
                value={newSupplier.address}
                onChange={(e) => setNewSupplier({...newSupplier, address: e.target.value})}
                style={{ width: "100%", padding: 8, marginTop: 5, height: 80 }}
              />
            </div>
            
            <div style={{ textAlign: "right" }}>
              <button 
                onClick={() => setShowAddSupplier(false)}
                style={{ padding: "10px 20px", backgroundColor: "#6c757d", color: "white", border: "none", borderRadius: 4, marginRight: 10 }}
              >
                Cancel
              </button>
              <button 
                onClick={addSupplier}
                style={{ padding: "10px 20px", backgroundColor: "#28a745", color: "white", border: "none", borderRadius: 4 }}
              >
                Add Supplier
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
