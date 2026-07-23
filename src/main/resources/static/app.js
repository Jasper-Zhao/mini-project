const dateInput = document.querySelector('#date');
const floorInput = document.querySelector('#floor');
const monitorInput = document.querySelector('#monitor');
const desksElement = document.querySelector('#desks');
const bookingsElement = document.querySelector('#bookings');
const notice = document.querySelector('#notice');
const employeeName = document.querySelector('#employee-name');
const bookButton = document.querySelector('#book');
const selectedDesk = document.querySelector('#selected-desk');
let selectedDeskId = null;

dateInput.value = '2026-07-24';

function params() {
  const query = new URLSearchParams({ date: dateInput.value });
  if (floorInput.value) query.set('floor', floorInput.value);
  if (monitorInput.value) query.set('hasMonitor', monitorInput.value);
  return query;
}
function showNotice(message, success = false) {
  notice.textContent = message;
  notice.classList.toggle('success', success);
}
async function request(url, options) {
  const response = await fetch(url, options);
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || 'Something went wrong.');
  }
  return response.status === 204 ? null : response.json();
}
function selectDesk(desk) {
  selectedDeskId = desk.id;
  selectedDesk.textContent = `${desk.code} · Floor ${desk.floor}${desk.hasMonitor ? ' · Monitor included' : ''}`;
  employeeName.disabled = false;
  bookButton.disabled = false;
  employeeName.focus();
}
function renderDesks(desks) {
  document.querySelector('#availability-count').textContent = `${desks.length} free`;
  desksElement.innerHTML = desks.length ? '' : '<p class="empty">No desks match these filters.</p>';
  desks.forEach(desk => {
    const card = document.createElement('article');
    card.className = 'desk-card';
    card.innerHTML = `<h3>${desk.code}</h3><p>Floor ${desk.floor} · ${desk.hasMonitor ? 'Monitor included' : 'No monitor'}</p><button type="button">Choose desk</button>`;
    card.querySelector('button').addEventListener('click', () => selectDesk(desk));
    desksElement.append(card);
  });
}
function renderBookings(bookings) {
  document.querySelector('#booking-count').textContent = `${bookings.length} booked`;
  bookingsElement.innerHTML = bookings.length ? '' : '<p class="empty">No bookings for this date yet.</p>';
  bookings.forEach(booking => {
    const row = document.createElement('article');
    row.className = 'booking-row';
    row.innerHTML = `<div><strong>${booking.deskCode}</strong><p>${booking.employeeName}</p></div><button type="button">Cancel</button>`;
    row.querySelector('button').addEventListener('click', async () => {
      try { await request(`/api/bookings/${booking.id}`, { method: 'DELETE' }); showNotice('Booking cancelled.', true); load(); }
      catch (error) { showNotice(error.message); }
    });
    bookingsElement.append(row);
  });
}
async function load() {
  if (!dateInput.value) return showNotice('Choose a date first.');
  selectedDeskId = null;
  selectedDesk.textContent = 'Choose an available desk to get started.';
  employeeName.disabled = true;
  bookButton.disabled = true;
  try {
    const [desks, bookings] = await Promise.all([
      request(`/api/desks/availability?${params()}`),
      request(`/api/bookings?date=${encodeURIComponent(dateInput.value)}`)
    ]);
    renderDesks(desks); renderBookings(bookings); showNotice('');
  } catch (error) { showNotice(error.message); }
}
document.querySelector('#refresh').addEventListener('click', load);
document.querySelector('#booking-form').addEventListener('submit', async event => {
  event.preventDefault();
  try {
    await request('/api/bookings', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ deskId: selectedDeskId, employeeName: employeeName.value, date: dateInput.value }) });
    employeeName.value = ''; showNotice('Desk booked successfully.', true); load();
  } catch (error) { showNotice(error.message); }
});
load();
