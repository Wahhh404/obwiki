const SessionStorage = {
  get: (key) => {
    const v = sessionStorage.getItem(key);
    if (v && typeof v !== 'undefined' && v !== 'undefined') {
      return JSON.parse(v);
    }
  },
  set: (key, data) => {
    sessionStorage.setItem(key, JSON.stringify(data));
  },
  remove: (key) => {
    sessionStorage.removeItem(key);
  },
  clearAll: () => {
    sessionStorage.clear();
  },
}; 