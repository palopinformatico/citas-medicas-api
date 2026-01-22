# API de Citas Médicas

API REST desarrollada con Spring Boot para la gestión de citas médicas.

## Tecnologías utilizadas

- Java 17
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- MySQL
- Lombok
- Maven

## Estructura de directorios

src/main/java/com/example/citas_medicas_api
│
├── CitasMedicasApiApplication.java
│
├── config
│   └── WebConfig.java
│
├── controller
│   ├── AppointmentController.java
│   └── PaymentController.java
│
├── exception
│   ├── BusinessException.java
│   ├── ErrorResponse.java
│   └── GlobalExceptionHandler.java
│
├── interceptor
│   └── ApiKeyInterceptor.java
│
├── model
│   ├── dto
│   │   └── CreateAppointmentRequest.java
│   ├── entity
│   │   ├── Appointment.java
│   │   ├── Doctor.java
│   │   ├── Patient.java
│   │   └── Payment.java
│   └── enums
│       ├── AppointmentStatus.java
│       └── Users.java
│
├── repository
│   ├── AppointmentRepository.java
│   ├── DoctorRepository.java
│   ├── PatientRepository.java
│   └── PaymentRepository.java
│
└── service
    ├── AppointmentService.java
    ├── PaymentService.java
	├── SandboxPaymentGateway
    └── payment
        ├── PaymentGateway.java
        └── PaymentResult.java


## Roles
- PACIENTE: puede solicitar y pagar citas.
- MEDICO: puede confirmar o rechazar citas y ver su agenda diaria.

## Seguridad
Autenticación simple mediante api key:

x-api-key : paciente-key-123
x-api-key : medico-key-456

## Endpoints principales

### Crear cita (Paciente)
POST /api/appointments

### Pagar cita
POST /api/payments/{appointmentId}

### Confirmar cita (Médico)
PUT /api/appointments/{id}/confirm

### Listar citas del Médico
GET /api/appointments/doctor/{id}

### Listar citas del paciente
GET /api/appointments/patient/{id}

## Reglas de negocio
- Horarios válidos:
  - 07:00 – 12:00
  - 14:00 – 18:00
- No se puede confirmar una cita no pagada.
- No se puede agendar en horarios ocupados.

## Ejecución
1. Crear BD, tablas y registros MySQL con script SQL (scripts.sql)
2. Evidencia de pruebas en doc evidencia de pruebas.docx
3. para levantar servidor: mvn spring-boot:run
4. para correr pruebas: mvn test