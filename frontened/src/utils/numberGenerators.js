// Utility functions for generating reservation and folio numbers

/**
 * Generate a unique reservation number
 * Format: 001/XX-YY (where XX-YY is account year, e.g., 24-25)
 */
export function generateReservationNumber() {
  // Get current date for account year calculation
  const now = new Date();
  const currentYear = now.getFullYear();
  const month = now.getMonth(); // 0-11
  
  // Account year typically runs from April to March
  let accountYear;
  if (month >= 3) { // April onwards
    accountYear = `${currentYear.toString().slice(-2)}-${(currentYear + 1).toString().slice(-2)}`;
  } else { // January to March
    accountYear = `${(currentYear - 1).toString().slice(-2)}-${currentYear.toString().slice(-2)}`;
  }
  
  // Generate sequential number (in real app, this would come from backend)
  const sequentialNumber = Math.floor(Math.random() * 999) + 1;
  
  // Format: "001/24-25" where 24-25 is the account year
  return `${sequentialNumber.toString().padStart(3, '0')}/${accountYear}`;
}

/**
 * Generate a unique folio number
 * Format: FOL/YYYYMM/NNNN (e.g., FOL/202508/0001)
 */
export function generateFolioNumber() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const sequence = Math.floor(Math.random() * 9999) + 1;
  
  return `FOL/${year}${month}/${sequence.toString().padStart(4, '0')}`;
}

/**
 * Generate a unique walk-in folio number
 * Format: WI + 8-digit timestamp (e.g., WI12345678)
 */
export function generateWalkInFolioNumber() {
  const timestamp = Date.now().toString();
  return `WI${timestamp.slice(-8)}`;
}

/**
 * Generate a unique advance receipt number
 * Format: ADV-YYYYMMDD-XXXX
 */
export function generateAdvanceNumber() {
  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0');
  const day = String(today.getDate()).padStart(2, '0');
  const random = Math.floor(1000 + Math.random() * 9000);
  
  return `ADV-${year}${month}${day}-${random}`;
}

/**
 * Generate a unique transaction ID
 * Format: TXN-YYYYMMDDHHMMSS-XXX
 */
export function generateTransactionId() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hours = String(now.getHours()).padStart(2, '0');
  const minutes = String(now.getMinutes()).padStart(2, '0');
  const seconds = String(now.getSeconds()).padStart(2, '0');
  const random = Math.floor(100 + Math.random() * 900);
  
  return `TXN-${year}${month}${day}${hours}${minutes}${seconds}-${random}`;
}

/**
 * Validate reservation number format (001/XX-YY)
 */
export function validateReservationNumber(reservationNo) {
  const pattern = /^\d{3}\/\d{2}-\d{2}$/;
  return pattern.test(reservationNo);
}

/**
 * Validate folio number format (FOL/YYYYMM/NNNN or WI12345678)
 */
export function validateFolioNumber(folioNo) {
  const regularPattern = /^FOL\/\d{6}\/\d{4}$/;
  const walkInPattern = /^WI\d{8}$/;
  return regularPattern.test(folioNo) || walkInPattern.test(folioNo);
}

/**
 * Format currency amount
 */
export function formatCurrency(amount, currency = '₹') {
  if (typeof amount !== 'number') {
    amount = parseFloat(amount) || 0;
  }
  return `${currency} ${amount.toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })}`;
}

/**
 * Format date for display
 */
export function formatDate(date) {
  if (!date) return 'N/A';
  const d = new Date(date);
  return d.toLocaleDateString('en-IN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
}

/**
 * Format date and time for display
 */
export function formatDateTime(date) {
  if (!date) return 'N/A';
  const d = new Date(date);
  return d.toLocaleString('en-IN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  });
}
