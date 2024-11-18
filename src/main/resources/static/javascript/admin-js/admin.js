// admin.js
document.addEventListener('DOMContentLoaded', function() {
    const modal = document.getElementById('responsesModal');
    const closeBtn = document.getElementsByClassName('close')[0];

    window.showResponses = function(appointmentId) {
        const appointment = appointments.find(a => a.id === appointmentId);
        if (appointment && appointment.responses) {
            const container = document.getElementById('responsesContainer');
            container.innerHTML = appointment.responses.map(response => `
                <div class="response-item">
                    <div class="question">${response.question ? response.question.questionText : 'Pregunta no disponible'}</div>
                    <div class="answer">${response.response}</div>
                </div>
            `).join('');
            modal.style.display = 'block';
        }
    };

    window.cancelAppointment = function(appointmentId) {
        if (confirm('¿Estás seguro de que deseas cancelar esta cita?')) {
            fetch(`/appointments/${appointmentId}/cancel`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (response.ok) {
                        window.location.reload();
                    } else {
                        alert('Error al cancelar la cita');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Error al cancelar la cita');
                });
        }
    };

    closeBtn.onclick = function() {
        modal.style.display = 'none';
    };

    window.onclick = function(event) {
        if (event.target === modal) {
            modal.style.display = 'none';
        }
    };
});