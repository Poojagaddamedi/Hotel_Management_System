import { ROLES } from "../constants/roles";

export const permissions = {
  [ROLES.ADMIN]: {
    access: ["all"],
    apis: ["*"],
    ui: ["adminPanel", "reports", "staffManagement", "settings"],
  },
  [ROLES.MANAGER]: {
    access: ["reservations", "check-ins", "payments", "bills", "charges", "housekeeping"],
    apis: ["/api/reports/*", "/api/staff", "/api/staff/{id}"],
    ui: ["dashboard", "reports", "staffManagement"],
  },
  [ROLES.ACCOUNTANT]: {
    access: ["payments", "bills", "reports"],
    apis: ["/api/advances", "/api/bill-settlements", "/api/reports/*"],
    ui: ["paymentForms", "reportFilters", "csvExport"],
  },
  [ROLES.RECEPTIONIST]: {
    access: ["reservations", "check-ins", "check-outs", "payments", "charges"],
    apis: [
      "/api/reservations",
      "/api/checkins",
      "/api/advances",
      "/api/checkouts",
      "/api/charges",
    ],
    ui: ["dataEntryForms", "basicReports"],
  },
  [ROLES.HOUSEKEEPING]: {
    access: ["housekeeping"],
    apis: ["/api/housekeeping"],
    ui: ["taskManagement"],
  },
};
